package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
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
    private Image playerIcon; // Referenz für das Player-Icon

    public PlayScreen(Main game) {
        this.game = game;
        viewport = new FitViewport( 680, 1000);
        shape = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        //stage = new Stage(new ScreenViewport());
        stage = new Stage(viewport);

        initializeTable();

        backgroundTexture=new TextureRegion(new Texture("backgrounds/paper_1280.jpg"));
        disposableTextures.add(backgroundTexture.getTexture());

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
        // SettingsButton
        Texture settings = game.assetManager.get("Buttons/Blue_Buttons_Pixel.png");

        TextureData textureData = settings.getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }
        Pixmap overmap =textureData.consumePixmap();
        Pixmap overmap2=new Pixmap(overmap.getWidth(), overmap.getHeight(),overmap.getFormat());
        Texture sOverTexture=new Texture(PixmapLibrary.addColorOverlay(overmap2, new Color(1f, 1f, 1f, 0.5f),true));
        //overmap2.dispose();
        //overmap.dispose();
        //disposableTextures.add(sOverTexture);

        TextureRegionDrawable settingsUpDrawable = new TextureRegionDrawable(new TextureRegion(settings, 32, 0, 16, 16));
        TextureRegionDrawable settingsOverDrawable = new TextureRegionDrawable(new TextureRegion(sOverTexture, 32, 0, 16, 16));
        TextureRegionDrawable settingsDownDrawable = new TextureRegionDrawable(new TextureRegion(settings, 192, 0, 16, 16));
        ImageButton.ImageButtonStyle settingsStyle = new ImageButton.ImageButtonStyle();

        settingsStyle.up = settingsUpDrawable;
        settingsStyle.over = settingsOverDrawable;
        settingsStyle.down = settingsDownDrawable;
        ImageButton settingsButton = new ImageButton(settingsStyle);
        settingsButton.setSize(64, 64);
        Label playerLabel = new Label("Player:", labelStyle);
        playerLabel.setAlignment(Align.center);
        playerIcon = new Image(getPlayerIcon(playTable.getCurrentPlayer())); // Referenz speichern
        Table playerRow = new Table();
        playerRow.add(playerLabel).padRight(10f);
        playerRow.add(playerIcon).size(100, 100);
        table.add(settingsButton).padBottom(100f).padLeft(510f).size(80,80).expandX().row();
        table.add(playerRow).padBottom(650f).padRight(300).expandX().row();
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.remove();
                System.out.println("Settings");
            }
        });
    }

    void initializeTable() {
        switch(game.getConfig().gameMode) {
            case GameMode.Classic -> playTable = new ClassicFieldTable(5,game.getConfig());
            /*case 3 -> playTable = new ClassicFieldTable(5,game.getConfig());
            case 4 -> playTable = new ClassicFieldTable(7,game.getConfig());
            default -> playTable = new ClassicFieldTable(3,game.getConfig());*/
        }
        playTable.setCenter(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);

    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (playTable.gameOver) return true;
                Vector2 screenCoords=viewport.unproject(new Vector2(screenX, screenY));
                if (playTable.makeMove(screenCoords.x,screenCoords.y)) {
                    updatePlayerIcon(); // Icon nach jedem Zug aktualisieren
                    if (playTable.gameOver) {
                        showWinMenu();
                        inputMultiplexer.removeProcessor(1);
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

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // WinnerLabel mit weißer Schrift und schwarzer Outline
        String winnerText = "Winner:";

        // Weißes Label oben drauf
        Label winnerLabel = new Label(winnerText, labelStyle);
        winnerLabel.setFontScale(1);
        winnerLabel.setAlignment(Align.center);

        TextButton restartButton = new TextButton("Restart", roundedStyle);
        TextButton menuButton = new TextButton("Startmenu", roundedStyle);

        // Layout-Anpassung: WinnerLabel hoch, Buttons weiter unten
        // WinnerLabel und Symbol (X oder O) nebeneinander
        int winner = playTable.getCurrentPlayer();
        int iconSize = 80;
        Pixmap iconPixmap = new Pixmap(iconSize, iconSize, Pixmap.Format.RGBA8888);
        iconPixmap.setBlending(Pixmap.Blending.None);
        if (winner == 1) { // X
            iconPixmap.setColor(1, 0, 0, 1);
            for (int i = -4; i <= 4; i++) { // dickeres X
                iconPixmap.drawLine(8+i, 8, iconSize-8+i, iconSize-8);
                iconPixmap.drawLine(8+i, iconSize-8, iconSize-8+i, 8);
            }
        } else { // O
            iconPixmap.setColor(0, 0, 1, 1);
            iconPixmap.fillCircle(iconSize/2, iconSize/2, iconSize/2-8);
            iconPixmap.setColor(0, 0, 0, 0);
            iconPixmap.fillCircle(iconSize/2, iconSize/2, iconSize/2-14);
        }
        //Texture iconTexture = new Texture(iconPixmap);
        Texture iconTexture = (winner==1? playTable.drawer.xTexture:playTable.drawer.oTexture);
       // disposableTextures.add(iconTexture);
        iconPixmap.dispose();
        Image winnerIcon = new Image(iconTexture);
        Table winnerRow = new Table();
        winnerRow.add(winnerLabel).padRight(10f);
        winnerRow.add(winnerIcon).size(iconSize,iconSize);
        table.add(winnerRow).padTop(170f).padBottom(120f).expandX().row();
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
                System.out.println("no player");
                return null;
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
        shape.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();

        shape.begin(ShapeRenderer.ShapeType.Line);
        playTable.drawer.shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        playTable.drawer.colorfulBatch.setProjectionMatrix(viewport.getCamera().combined);
        playTable.render(shape);
        shape.end();

        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        //stage.getViewport().update(width, height, true);
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
