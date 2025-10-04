package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;
import java.util.List;

public class StartScreen implements Screen {
    private Stage stage;
    private final List<Texture> disposableTextures = new ArrayList<>();

    public StartScreen(final Main game) {
        stage = new Stage(new ScreenViewport());
        BitmapFont normalFont = game.assetManager.get("bruce.ttf", BitmapFont.class);

        // Hintergrundbild oder Farbverlauf erzeugen (hier: einfacher Verlauf)
        Pixmap bgpixmap= PixmapLibrary.getColourGradientMap(new Color(0.1f, 0.3f , 0.8f, 1f),new Color(0.1f, 0.3f + 0.45f, 0.8f, 1f),1,256);
        Texture bgTexture = new Texture(bgpixmap);
        disposableTextures.add(bgTexture);
        bgpixmap.dispose();
        Image bg = new Image(bgTexture);
        bg.setFillParent(true);
        stage.addActor(bg);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // Pixmap für abgerundete Buttons erzeugen (normal, hover, pressed)
        int btnWidth = 400, btnHeight = 100, outlineWidth = 5; float rounding = 0.8f;
        // Normal
        Pixmap btnPixmap =PixmapLibrary.getRoundedSquare(btnWidth, btnHeight, rounding, new Color(0.2f, 0.4f, 0.9f, 1f),outlineWidth);
        Texture btnTexture = new Texture(btnPixmap);
        // Hover
        //Pixmap btnOverPixmap = PixmapLibrary.getRoundedSquare(btnWidth, btnHeight, rounding, new Color(0.2f*0.8f, 0.4f*0.8f, 0.9f*0.8f, 1f),outlineWidth);
        Pixmap btnOverPixmap = PixmapLibrary.getRoundedSquare(btnWidth, btnHeight, rounding, new Color(0.2f*0.5f, 0.4f*1.7f, 0.9f*1.4f, 1f), outlineWidth);
        Texture btnOverTexture = new Texture(btnOverPixmap);
        Pixmap btnDownPixmap = PixmapLibrary.getRoundedSquare(btnWidth, btnHeight, rounding, new Color(0.2f*0.4f, 0.4f*1.5f, 0.9f*1.25f, 1f), outlineWidth);
        Texture btnDownTexture = new Texture(btnDownPixmap);

        btnPixmap.dispose();
        btnOverPixmap.dispose();
        btnDownPixmap.dispose();
        // ButtonStyle mit abgerundeten Ecken, schwarzem Rand und Hover/Pressed
        TextButtonStyle roundedStyle = new TextButtonStyle();
        roundedStyle.up = new Image(btnTexture).getDrawable();
        roundedStyle.over = new Image(btnOverTexture).getDrawable();
        roundedStyle.down = new Image(btnDownTexture).getDrawable();
        roundedStyle.font = normalFont;
        roundedStyle.fontColor = Color.WHITE;
        disposableTextures.add(btnTexture);
        disposableTextures.add(btnOverTexture);
        disposableTextures.add(btnDownTexture);
        TextButton btn1 = new TextButton("1 Player", roundedStyle);
        TextButton btn2 = new TextButton("2 Players", roundedStyle);
        TextButton btn3 = new TextButton("3 Players", roundedStyle);
        TextButton settingsBtn = new TextButton("Settings", roundedStyle);



        // Buttons langsam einblenden (Fade-In)
        btn1.getColor().a = 0f;
        btn2.getColor().a = 0f;
        btn3.getColor().a = 0f;
        settingsBtn.getColor().a = 0f;
        btn1.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn(0.3f));
        btn2.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn(0.3f));
        btn3.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn(0.3f));
        settingsBtn.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn(0.3f));

        btn1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getConfig().playercount=2;
                game.getConfig().winLength=0;
                game.getConfig().aiEnabled=true;
                game.getConfig().gameMode=GameMode.Classic;
                game.setScreen(new PlayScreen(game));

            }
        });
        btn2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getConfig().playercount=2;
                game.getConfig().winLength=0;
                game.getConfig().gameMode=GameMode.Classic;
                game.getConfig().aiEnabled=false;
                game.setScreen(new PlayScreen(game));
            }
        });
        btn3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Noch keine Aktion
            }
        });
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Noch keine Aktion
            }
        });

        table.add(btn1).pad(20).width(btnWidth).height(btnHeight).row();
        table.add(btn2).pad(20).width(btnWidth).height(btnHeight).row();
        table.add(btn3).pad(20).width(btnWidth).height(btnHeight).row();
        table.add(settingsBtn).pad(40).width(btnWidth).height(btnHeight);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.1f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null); // Eingabeprozessor entfernen, wenn der Screen versteckt wird
    }

    @Override
    public void dispose() {
        stage.dispose();
        for(Texture texture : disposableTextures) {
            texture.dispose();
        }
        disposableTextures.clear();

    }
}
