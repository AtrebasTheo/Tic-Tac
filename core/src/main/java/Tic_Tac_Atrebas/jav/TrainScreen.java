package Tic_Tac_Atrebas.jav;

import Tic_Tac_Atrebas.jav.Neural.NeuralNetwork;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;


 public class TrainScreen implements Screen {
     private final ScreenViewport viewport;
     private final ShapeRenderer shape;
     private final SpriteBatch spriteBatch;
     private FieldTable playTable;
     TextureRegion backgroundTexture;
     private final Main game;
     private final Stage stage;
     private final TextButton.TextButtonStyle roundedStyle;
     private final List<Texture> disposableTextures = new ArrayList<>();
     private InputMultiplexer inputMultiplexer;
     private  Table settingsTable;
     private double[] neuralTrainingSettingsSnapshot;
     private Image playerIcon; // Referenz für das Player-Icon
     private Label playerLabel;
     private ImageButton settingsButton;
     GameConfiguration originalConfig;

     AIPlayer aiPlayer;
        volatile boolean aiIsThinking = false;
        private Future<?> aiFuture = null;
        public TrainScreen(Main game) {
            this.game = game;
            viewport = new ScreenViewport();
            shape = new ShapeRenderer();
            spriteBatch = new SpriteBatch();
            //stage = new Stage(new ScreenViewport());
            stage = new Stage(viewport);

            inputMultiplexer= new InputMultiplexer(stage);
            Gdx.input.setInputProcessor(inputMultiplexer);

            initializeTable();

            backgroundTexture=new TextureRegion(new Texture("backgrounds/paper_1280.jpg"));
            Table papertable = new Table();
            papertable.setFillParent(true);
            stage.addActor(papertable);
            Image textureImage = new Image(new TextureRegionDrawable(backgroundTexture));
            papertable.center();
            papertable.add(textureImage).center();


            disposableTextures.add(backgroundTexture.getTexture());



            Table tabletable = new Table();
            tabletable.setFillParent(true);
            tabletable.center();
            tabletable.add(playTable).center();
            // stage.addActor(tabletable);


            roundedStyle = game.assetManager.getStandartBlueTextButtonStyle();

            Label.LabelStyle labelStyle = new Label.LabelStyle(roundedStyle.font, Color.WHITE);
            Table table = new Table();
            table.setFillParent(true);
            table.center();
            stage.addActor(table);



            ImageButton.ImageButtonStyle settingsStyle = game.assetManager.getSettingsButtonStyle();



            settingsButton = new ImageButton(settingsStyle);

            settingsButton.setSize(96, 96); //keep this size for the button, otherwise it's glitchy
            playerLabel = new Label("Player:", labelStyle);
            playerLabel.setAlignment(Align.center);
            playerIcon = new Image(getPlayerIcon(playTable.getCurrentPlayer())); // Referenz speichern
            Table playerRow = new Table();
            playerRow.add(playerLabel).padRight(10f);
            playerRow.add(playerIcon).size(game.getGameConfiguration().playerIconSize, game.getGameConfiguration().playerIconSize);
            playerRow.align(Align.topLeft);
            settingsButton.align(Align.topRight);
            table.add(settingsButton).padTop(10f).padBottom(-60f).padLeft(510f).size(80,80).expandX().row();
            table.add(playerRow).padTop(1).padBottom(750f).padRight(300).expandX().row();

            settingsButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    showSettingsMenu();
                }
            });

            stage.addActor(tabletable);

        }

        void initializeTable() {
            switch(game.getGameConfiguration().gameMode) {
                case GameMode.Classic -> playTable = new ClassicFieldTable(game.getGameConfiguration());
            /*case 3 -> playTable = new ClassicFieldTable(5,game.getConfig());
            case 4 -> playTable = new ClassicFieldTable(7,game.getConfig());
            default -> playTable = new ClassicFieldTable(3,game.getConfig());*/
            }
            playTable.setCenter(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);
            if(game.getGameConfiguration().aiEnabled)
            {aiPlayer= new ClassicAI((ClassicFieldTable) playTable,game.getGameConfiguration().playerCount);}

        }


        private void cancelAiTask() {
            if (aiFuture != null) {
                aiFuture.cancel(true);
                aiFuture = null;
            }
            aiIsThinking = false;
        }

        public void checkAI() {

            if (playTable.gameOver || aiIsThinking) return;
            if (game.getGameConfiguration().aiEnabled && playTable.currentPlayer == aiPlayer.aiNumber) {
                cancelAiTask();
                final AIPlayer ai = aiPlayer;
                aiIsThinking = true;
                playTable.refreshAiBoard();
                aiFuture = game.aiExecutor.submit(() -> {
                    int[] bestMove;
                    try {
                        bestMove = ai.calculateBestMove(playTable.getAiBoard());
                    } catch (Throwable t) {
                        Gdx.app.error("PlayScreen", "AI calculation failed", t);
                        Gdx.app.postRunnable(() -> {
                            cancelAiTask();
                            onCompletedMove();
                        });
                        return;
                    }
                    final int[] move = bestMove;
                    Gdx.app.postRunnable(() -> {
                        if (move != null) {
                            playTable.makeMoveAtField(move[0], move[1]);
                        } else {
                            Gdx.app.error("PlayScreen", "AI couldn't find a move");
                        }
                        cancelAiTask();
                        onCompletedMove();
                    });
                });
            }
        }

        public void onCompletedMove()
        {
            updatePlayerIcon(); // Icon nach jedem Zug aktualisieren
            if (playTable.gameOver) {
                showWinMenu();
                inputMultiplexer.removeProcessor(1);
            }
            else
            {
                checkAI();
            }
        }


        @Override
        public void show() {
            Gdx.input.setInputProcessor(inputMultiplexer);
            if(inputMultiplexer.getProcessors().size>1) {
                return;
            }
            inputMultiplexer.addProcessor(new InputAdapter() {
                @Override
                public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                    if (playTable.gameOver||game.getGameConfiguration().aiEnabled &&playTable.currentPlayer==aiPlayer.aiNumber) return true;
                    Vector2 screenCoords=viewport.unproject(new Vector2(screenX, screenY));
                    if (playTable.makeMove(screenCoords.x,screenCoords.y)) {
                        onCompletedMove();
                    }
                    return true;
                }
                @Override
                public boolean mouseMoved(int screenX, int screenY) {
                    Vector2 screenCoords = viewport.unproject(new Vector2(screenX, screenY));
                    playTable.mouseMoved(screenCoords.x, screenCoords.y);
                    return false;
                }
            });
        }

        private void updatePlayerIcon() {
            if (playerIcon != null) {
                int currentPlayer = playTable.getCurrentPlayer();
                Texture newTexture = null;
                if (currentPlayer == 1) newTexture = playTable.drawer.xTexture;
                else if (currentPlayer == 2) newTexture = playTable.drawer.oTexture;
                if (newTexture != null) playerIcon.setDrawable(new Image(newTexture).getDrawable());
            }
        }

        private void showWinMenu() {
            Label.LabelStyle labelStyle = new Label.LabelStyle(roundedStyle.font, Color.WHITE);



            playerLabel.setText((playTable.winner>0?"Winner:":"Draw"));

            int winner = playTable.getCurrentPlayer();
            int iconSize = 80;

            Texture iconTexture = (winner==1? playTable.drawer.xTexture:playTable.drawer.oTexture);

            Image winnerIcon = new Image(iconTexture);
            Table winnerRow = new Table();
            winnerRow.add(winnerIcon).size(iconSize,iconSize);


            Gdx.input.setInputProcessor(stage);

            showSettingsMenu();
        }


        private void showSettingsMenu() {

            EventListener savedListener =settingsButton.getListeners().get(0);
            settingsButton.removeListener(settingsButton.getListeners().get(1));
            settingsButton.removeListener(settingsButton.getListeners().get(0));
            Gdx.input.setInputProcessor(stage);

            if (settingsTable != null) {
                if (neuralTrainingSettingsSnapshot != null) {
                    applyNeuralNetworkSettings(neuralTrainingSettingsSnapshot);
                }
                settingsTable.setVisible(true);
                return;
            }

            final double[] originalValues = captureNeuralNetworkSettings();
            final double[] currentValues = originalValues.clone();
            neuralTrainingSettingsSnapshot = currentValues.clone();

            BitmapFont normalFont = game.assetManager.get("bruce.ttf", BitmapFont.class);
            settingsTable = new Table();
            settingsTable.setFillParent(true);
            Pixmap map = PixmapLibrary.getTunnelMap(new Color(0f, 0f, 0f, 0.7f), new Color(0f, 0f, 0f, 0.75f), 40, 1, 100);
            settingsTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(map))));
            map.dispose();
            settingsTable.center();
            settingsTable.padTop(30);
            stage.addActor(settingsTable);

            Label.LabelStyle labelStyle = new Label.LabelStyle(normalFont, Color.WHITE);
            final String[] settingNames = {
                "addNodes",
                "removeNodes",
                "addConnections",
                "removeConnections",
                "mutateWeights",
                "toggleConnections",
                "fitnessKeep"
            };
            final Label[] valueLabels = new Label[settingNames.length];

            final ImageButton confirmImgBtn = new ImageButton(game.assetManager.getConfirmButtonStyle());
            final ImageButton cancelImgBtn = new ImageButton(game.assetManager.getCancelButtonStyle());

            for (int i = 0; i < settingNames.length; i++) {
                final int index = i;
                valueLabels[i] = new Label(String.format(java.util.Locale.US, "%.2f", currentValues[i]), labelStyle);
                valueLabels[i].setAlignment(Align.center);
                valueLabels[i].setFontScale(0.5f);

                final ImageButton leftBtn = new ImageButton(game.assetManager.getLeftArrowButtonStyle());
                final ImageButton rightBtn = new ImageButton(game.assetManager.getRightArrowButtonStyle());
                leftBtn.setSize(52, 52);
                rightBtn.setSize(52, 52);

                leftBtn.addListener(createHoldValueChangeListener(currentValues, index, valueLabels, -0.01f));
                rightBtn.addListener(createHoldValueChangeListener(currentValues, index, valueLabels, 0.01f));

                Label settingLabel = new Label(settingNames[index], labelStyle);
                settingLabel.setFontScale(0.5f);
                settingsTable.add(settingLabel).left().width(320f).pad(10f).padRight(30f);
                settingsTable.add(valueLabels[i]).width(120f).pad(10f);
                settingsTable.add(leftBtn).size(52, 52).pad(8f);
                settingsTable.add(rightBtn).size(52, 52).pad(8f).row();
            }

            confirmImgBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    applyNeuralNetworkSettings(currentValues);
                    neuralTrainingSettingsSnapshot = currentValues.clone();
                    settingsTable.setVisible(false);
                    settingsButton.addListener(savedListener);
                    show();
                    settingsButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            showSettingsMenu();
                        }
                    });
                }
            });
            cancelImgBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    applyNeuralNetworkSettings(originalValues);
                    neuralTrainingSettingsSnapshot = originalValues.clone();
                    for (int i = 0; i < valueLabels.length; i++) {
                        valueLabels[i].setText(String.format(java.util.Locale.US, "%.2f", originalValues[i]));
                    }
                    settingsTable.setVisible(false);
                    show();
                    settingsButton.addListener(savedListener);
                    settingsButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            showSettingsMenu();
                        }
                    });

                }
            });

            settingsTable.add(confirmImgBtn).pad(12).width(64).height(64);
            settingsTable.add(cancelImgBtn).pad(12).width(64).height(64);
            settingsTable.setVisible(true);
        }

        private InputListener createHoldValueChangeListener(final double[] values, final int index, final Label[] valueLabels, final float step) {
            return new InputListener() {
                private Timer.Task repeatTask;

                private void changeValue() {
                    double nextValue = values[index] + step;
                    if (nextValue > 1.0) {
                        nextValue = 0.0;
                    } else if (nextValue < 0.0) {
                        nextValue = 1.0;
                    }
                    values[index] = nextValue;
                    valueLabels[index].setText(String.format(java.util.Locale.US, "%.2f", values[index]));
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    changeValue();
                    if (repeatTask != null) {
                        repeatTask.cancel();
                    }
                    repeatTask = Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            changeValue();
                        }
                    }, 0.18f, 0.08f);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (repeatTask != null) {
                        repeatTask.cancel();
                        repeatTask = null;
                    }
                }
            };
        }

        private double clamp(double value, double min, double max) {
            return Math.max(min, Math.min(max, value));
        }

        private double[] captureNeuralNetworkSettings() {
            return new double[] {
                NeuralNetwork.addNodeRate,
                NeuralNetwork.removeNodeRate,
                NeuralNetwork.addConnectionRate,
                NeuralNetwork.removeConnectionRate,
                NeuralNetwork.mutateWeightRate,
                NeuralNetwork.toggleConnectionRate,
                NeuralNetwork.fitnessKeepRate
            };
        }

        private void applyNeuralNetworkSettings(double[] values) {
            NeuralNetwork.addNodeRate = values[0];
            NeuralNetwork.removeNodeRate = values[1];
            NeuralNetwork.addConnectionRate = values[2];
            NeuralNetwork.removeConnectionRate = values[3];
            NeuralNetwork.mutateWeightRate = values[4];
            NeuralNetwork.toggleConnectionRate = values[5];
            NeuralNetwork.fitnessKeepRate = values[6];
        }



        Texture getPlayerIcon(int player) {
            Texture iconTexture;
            switch (player) {
                case 0:
                    throw new IllegalArgumentException("invalid player 0");
                case 1:
                    iconTexture=playTable.drawer.xTexture;
                    break;
                case 2:
                    iconTexture=playTable.drawer.oTexture;
                    break;
                default:
                    return null;
            }
            return iconTexture;
        }


        @Override
        public void render(float delta) {

            viewport.apply();
            stage.getViewport().apply();
            shape.setProjectionMatrix(viewport.getCamera().combined);
            spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
            playTable.drawer.shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
            playTable.drawer.colorfulBatch.setProjectionMatrix(viewport.getCamera().combined);

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);



            stage.act(delta);
            stage.draw();

        }

        @Override
        public void resize(int width, int height) {
            viewport.update(width, height, false);
            stage.getViewport().update(width, height, true);
            viewport.getCamera().update();
            //Camera camera = viewport.getCamera();

            //camera.position.set(0, viewport.getWorldHeight()/2 , 0);
            //camera.update();
            // playTable nach Resize neu zentrieren
        }


        @Override
        public void pause() {
            // Invoked when your application is paused.
        }

        @Override
        public void resume() {
            // Invoked when your application is resumed after pause.
        }

        @Override
        public void hide() {
            cancelAiTask();
            Gdx.input.setInputProcessor(null);
            dispose();
        }

        @Override
        public void dispose() {
            cancelAiTask();
            Gdx.input.setInputProcessor(null);
            if (playTable != null && playTable.drawer != null) {
                playTable.drawer.dispose();
            }
            shape.dispose();
            stage.dispose();
            spriteBatch.dispose();
            for (Texture texture : disposableTextures) {
                texture.dispose();
            }
            disposableTextures.clear();
        }
    }


