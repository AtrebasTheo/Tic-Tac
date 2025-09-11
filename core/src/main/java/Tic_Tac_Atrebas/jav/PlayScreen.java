package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
    private int hoverX = -1, hoverY = -1;

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
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (playTable.gameOver) return true;
                Vector2 screenCoords=viewport.unproject(new Vector2(screenX, screenY));
                if (playTable.makeMove(screenCoords.x,screenCoords.y)) {
                    if (playTable.gameOver) {
                        showWinMenu();
                        Gdx.input.setInputProcessor(stage);
                    }
                }
                return true;
            }
            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                Vector2 screenCoords = viewport.unproject(new Vector2(screenX, screenY));
                int x = (int) Math.floor((screenCoords.x - playTable.xc) / playTable.fieldwidth);
                int y = (int) Math.floor((screenCoords.y - playTable.yc) / playTable.fieldwidth);
                if (x >= 0 && x < 3 && y >= 0 && y < 3 && !playTable.gameOver) {
                    hoverX = x;
                    hoverY = y;
                } else {
                    hoverX = -1;
                    hoverY = -1;
                }
                return false;
            }

        });
        // Prepare your screen here.
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
        winnerLabel.setFontScale(1.2f);
        winnerLabel.setAlignment(Align.center);

        TextButton restartButton = new TextButton("Restart", roundedStyle);
        TextButton menuButton = new TextButton("Startmenu", roundedStyle);

        // Füge das Stack-Label dem Table hinzu
        table.add(winnerLabel).padBottom(20f).row();
        table.add(restartButton).pad(10f).row();
        table.add(menuButton).pad(10f).row();
        Gdx.input.setInputProcessor(stage);
        // Button-Listener
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playTable.reset();
                winnerLabel.remove();
                restartButton.remove();
                menuButton.remove();
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
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        if (hoverX >= 0 && hoverY >= 0) {
            playTable.drawHighlight(hoverX, hoverY, shape);
        }
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        // Gitter zeichnen
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
