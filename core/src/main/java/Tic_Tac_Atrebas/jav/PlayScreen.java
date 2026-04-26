package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

/** First screen of the application. Displayed after the application is created. */
public class PlayScreen implements Screen {
    private final ScreenViewport viewport;
    private final ShapeRenderer shape;
    private final SpriteBatch spriteBatch;
    private ClassicFieldTable playTable;
    TextureRegion backgroundTexture;
    private final Main game;
    private final Stage stage;
    private final TextButtonStyle roundedStyle;
    private final List<Texture> disposableTextures = new ArrayList<>();
    private InputMultiplexer inputMultiplexer;
    private Image playerIcon; // Referenz für das Player-Icon
    private Label playerLabel;
    private ImageButton settingsButton;
    AIPlayer aiPlayer;
    boolean aiIsThinking = false;
    public PlayScreen(Main game) {
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

        LabelStyle labelStyle = new LabelStyle(roundedStyle.font, Color.WHITE);
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);



        ImageButton.ImageButtonStyle settingsStyle = game.assetManager.getSettingsButtonStyle();



         settingsButton = new ImageButton(settingsStyle);

        settingsButton.setSize(96, 96);
        playerLabel = new Label("Player:", labelStyle);
        playerLabel.setAlignment(Align.center);
        playerIcon = new Image(getPlayerIcon(playTable.getCurrentPlayer())); // Referenz speichern
        Table playerRow = new Table();
        playerRow.add(playerLabel).padRight(10f);
        playerRow.add(playerIcon).size(100, 100);
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
            case GameMode.Classic -> playTable = new ClassicFieldTable(5,game.getGameConfiguration());
            /*case 3 -> playTable = new ClassicFieldTable(5,game.getConfig());
            case 4 -> playTable = new ClassicFieldTable(7,game.getConfig());
            default -> playTable = new ClassicFieldTable(3,game.getConfig());*/
        }
        playTable.setCenter(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);
        if(game.getGameConfiguration().aiEnabled)
        {aiPlayer= new ClassicAI(playTable,game.getGameConfiguration().playercount);}

    }


    public void checkAI() {

        if (playTable.gameOver||aiIsThinking) return;
        if(game.getGameConfiguration().aiEnabled && playTable.currentPlayer==aiPlayer.aiNumber) {
            final AIPlayer ai = aiPlayer;
            aiIsThinking = true;
            new Thread(() -> {
                int[] bestMove = ai.calculateBestMove();
                Gdx.app.postRunnable(() -> {
                    if (bestMove != null) {
                        playTable.makeMoveAtField(bestMove[0], bestMove[1]);
                        aiIsThinking = false;
                        onCompletedMove();
                    }
                    else{
                        throw new IllegalArgumentException("AI konnte keinen Zug finden!");
                    }
                });
            }).start();
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
        LabelStyle labelStyle = new LabelStyle(roundedStyle.font, Color.WHITE);



        playerLabel.setText((playTable.winner>0?"Winner:":"Draw"));

        /*Label winnerLabel = new Label("Winner:", labelStyle);
        winnerLabel.setFontScale(1);
        winnerLabel.setAlignment(Align.center);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        TextButton restartButton = new TextButton("Restart", roundedStyle);
        TextButton menuButton = new TextButton("Startmenu", roundedStyle);
        menuButton.getLabel().setFontScale(0.9f);*/

        int winner = playTable.getCurrentPlayer();
        int iconSize = 80;

        Texture iconTexture = (winner==1? playTable.drawer.xTexture:playTable.drawer.oTexture);

        Image winnerIcon = new Image(iconTexture);
        Table winnerRow = new Table();
        //winnerRow.add(winnerLabel).padRight(10f);
        winnerRow.add(winnerIcon).size(iconSize,iconSize);
        //table.add(winnerRow).padTop(170f).padBottom(120f).expandX().row();
      //  table.add(restartButton).padTop(180f).padBottom(30f).expandX().row();
      //  table.add(menuButton).padTop(10f).expandX().row();
        Gdx.input.setInputProcessor(stage);

        /*restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playTable.reset();
                winnerLabel.remove();
                restartButton.remove();
                menuButton.remove();
                table.remove();
                show();
                updatePlayerIcon(); // Icon nach Reset aktualisieren
            }
        });

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.startScreen);
            }
        });*/
        showSettingsMenu();
    }




    private void showSettingsMenu() {

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);
        settingsButton.removeListener(settingsButton.getListeners().get(1));
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.remove();
                show();
                settingsButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        showSettingsMenu();
                    }
                });
                settingsButton.removeListener(settingsButton.getListeners().get(1));

            }
        });


       ImageButton backButton = new ImageButton(game.assetManager.getLeftArrowButtonStyle());
        TextButton restartButton = new TextButton("Restart", roundedStyle);
        TextButton menuButton = new TextButton("Startmenu", roundedStyle);
        menuButton.getLabel().setFontScale(0.9f);



        //table.add(backButton).padBottom(220f).padRight(510f).size(96,96).expandX().row();
        table.add(restartButton).padTop(100f).padBottom(30f).expandX().row();
        table.add(menuButton).padTop(10f).padBottom(100f).expandX().row();
        Gdx.input.setInputProcessor(stage);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.remove();
                show();
                settingsButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        showSettingsMenu();
                    }
                });
                settingsButton.removeListener(settingsButton.getListeners().get(1));

            }
        });
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playerLabel.setText("Player:");
                playTable.reset();
                restartButton.remove();
                menuButton.remove();
                backButton.remove();
                table.remove();
                show();
                updatePlayerIcon(); // Icon nach Reset aktualisieren
            }
        });

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.startScreen);
            }
        });
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
        dispose();
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        shape.dispose();
        stage.dispose();
        spriteBatch.dispose();
        for (Texture texture : disposableTextures) {
            texture.dispose();
        }
        disposableTextures.clear();
    }
}
