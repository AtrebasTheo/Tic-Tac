package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

/** First screen of the application. Displayed after the application is created. */
public class PlayScreen implements Screen {
    private final FitViewport viewport;
    private final ShapeRenderer shape;
    private final SpriteBatch spriteBatch;
    private ClassicFieldTable playTable;
    TextureRegion backgroundTexture;
    private final Main game;
    private final Stage stage;
    private final TextButtonStyle roundedStyle;
    private final List<Texture> disposableTextures = new ArrayList<>();
    private InputMultiplexer inputMultiplexer;

    public PlayScreen(Main game) {
        this.game = game;
        viewport = new FitViewport( 680, 1000);
        shape = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        playTable = new ClassicFieldTable(3);

        backgroundTexture=new TextureRegion(new Texture("backgrounds/paper_1280.jpg"));
        disposableTextures.add(backgroundTexture.getTexture());
        playTable.setCenter(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);
        stage = new Stage(new ScreenViewport());
        inputMultiplexer= new InputMultiplexer(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);


        // Pixmap für die Buttons erzeugen (normal, hover, pressed)
        int btnWidth = 400, btnHeight = 100, outlineWidth = 5; float rounding = 0.8f;
        // Normal
        Pixmap btnPixmap =PixmapLibrary.getRoundedSquare(btnWidth, btnHeight, rounding, new Color(0.2f, 0.4f, 0.9f, 1f),outlineWidth);
        Texture btnTexture = new Texture(btnPixmap);
        // Hover
        Pixmap btnOverPixmap = PixmapLibrary.getRoundedSquare(btnWidth, btnHeight, rounding, new Color(0.2f*0.5f, 0.4f*1.7f, 0.9f*1.4f, 1f), outlineWidth);
        Texture btnOverTexture = new Texture(btnOverPixmap);
        // Pressed
        Pixmap btnDownPixmap = PixmapLibrary.getRoundedSquare(btnWidth, btnHeight, rounding, new Color(0.2f*0.4f, 0.4f*1.5f, 0.9f*1.25f, 1f), outlineWidth);
        Texture btnDownTexture = new Texture(btnDownPixmap);

        btnPixmap.dispose();
        btnOverPixmap.dispose();
        btnDownPixmap.dispose();
        // ButtonStyle mit abgerundeten Ecken, schwarzem Rand und Hover/Pressed
        roundedStyle = new TextButtonStyle();
        roundedStyle.up = new Image(btnTexture).getDrawable();
        roundedStyle.over = new Image(btnOverTexture).getDrawable();
        roundedStyle.down = new Image(btnDownTexture).getDrawable();
        roundedStyle.font = game.assetManager.get("bruce.ttf", BitmapFont.class);;
        roundedStyle.fontColor = Color.WHITE;
        disposableTextures.add(btnTexture);
        disposableTextures.add(btnOverTexture);
        disposableTextures.add(btnDownTexture);


        LabelStyle labelStyle = new LabelStyle(roundedStyle.font, Color.WHITE);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // SettingsButton mit eigenem Style (up/over/down)
        Texture settings = game.assetManager.get("Buttons/Blue_Buttons_Pixel.png");

        TextureRegionDrawable settingsUpDrawable = new TextureRegionDrawable(new TextureRegion(settings, 32, 0, 16, 16));
        TextureRegionDrawable settingsOverDrawable = new TextureRegionDrawable(new TextureRegion(settings, 32, 0, 16, 16));
        TextureRegionDrawable settingsDownDrawable = new TextureRegionDrawable(new TextureRegion(settings, 192, 0, 16, 16));
        ImageButton.ImageButtonStyle settingsStyle = new ImageButton.ImageButtonStyle();
        settingsStyle.up = settingsUpDrawable;
        settingsStyle.over = settingsOverDrawable;
        settingsStyle.down = settingsDownDrawable;
        ImageButton settingsButton = new ImageButton(settingsStyle);
        settingsButton.setSize(63, 64);
        Label winnerLabel = new Label("Player:", labelStyle);
        winnerLabel.setAlignment(Align.center);

        // SettingsButton oben rechts platzieren, WinnerLabel darunter
        table.add(settingsButton).padTop(10f).padLeft(50f).size(48,48).expandX().align(Align.topRight).colspan(2).row();
        table.add(winnerLabel).padTop(20f).padBottom(10f).padRight(0).expandX().row();
        // ...restlicher Code bleibt...
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.remove();
                System.out.println("Settings");
            }
        });
    }

    @Override
    public void show() {
        //Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (playTable.gameOver) return true;
                Vector2 screenCoords=viewport.unproject(new Vector2(screenX, screenY));
                if (playTable.makeMove(screenCoords.x,screenCoords.y)) {
                    if (playTable.gameOver) {
                        showWinMenu();
                        inputMultiplexer.removeProcessor(1);
                        //Gdx.input.setInputProcessor(stage);
                    }
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

    private void showWinMenu() {
        LabelStyle labelStyle = new LabelStyle(roundedStyle.font, Color.WHITE);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // WinnerLabel mit weißer Schrift und schwarzer Outline
        String winnerText = "Winner:" + playTable.getCurrentPlayer();

        // Weißes Label oben drauf
        Label winnerLabel = new Label(winnerText, labelStyle);
        winnerLabel.setFontScale(1);
        winnerLabel.setAlignment(Align.center);

        TextButton restartButton = new TextButton("Restart", roundedStyle);
        TextButton menuButton = new TextButton("Startmenu", roundedStyle);

        // Layout-Anpassung: WinnerLabel hoch, Buttons weiter unten
        table.add(winnerLabel).padTop(170f).padBottom(120f).expandX().row();
        table.add(restartButton).padTop(180f).padBottom(30f).expandX().row();
        table.add(menuButton).padTop(10f).expandX().row();
        Gdx.input.setInputProcessor(stage);
        // Button-Listener
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playTable.reset();
                winnerLabel.remove();
                restartButton.remove();
                menuButton.remove();
                table.remove();
                show();
            }
        });

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.startScreen);
            }
        });
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        shape.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();

        shape.begin(ShapeRenderer.ShapeType.Line);
        playTable.render(shape);
        shape.end();

        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
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
