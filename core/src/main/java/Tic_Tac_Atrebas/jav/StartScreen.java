package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;
import java.util.List;

public class StartScreen implements Screen {
    private final Stage stage;
    private final List<Texture> disposableTextures = new ArrayList<>();
    private final BitmapFont normalFont;
    private final Main game;
    private  Table table;
    private  Table settingsTable;
    private InputListener settingsInputListener; // listener, damit wir ihn entfernen können
    TextButtonStyle roundedStyle;
    // Asset keys für Buttons (kannst du später per Setter setzen)


    public StartScreen(final Main game) {
        this.game=game;
        stage = new Stage(new ScreenViewport());
        normalFont = game.assetManager.get("bruce.ttf", BitmapFont.class);

        // Hintergrundbild oder Farbverlauf erzeugen (hier: einfacher Verlauf)
        Pixmap bgpixmap= PixmapLibrary.getColourGradientMap(new Color(0.1f, 0.3f , 0.8f, 1f),new Color(0.1f, 0.3f + 0.45f, 0.8f, 1f),1,256);
        Texture bgTexture = new Texture(bgpixmap);
        disposableTextures.add(bgTexture);
        bgpixmap.dispose();
        Image bg = new Image(bgTexture);
        bg.setFillParent(true);
        stage.addActor(bg);

        initializeStartMenu();
    }



    private void initializeStartMenu()
    {

        table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // Pixmap für abgerundete Buttons erzeugen (normal, hover, pressed)
        int btnWidth = 400, btnHeight = 100, outlineWidth = 5; float rounding = 0.8f;
        // Normal
        Pixmap btnPixmap =PixmapLibrary.getRoundedSquare(btnWidth, btnHeight, rounding, new Color(0.2f, 0.4f, 0.9f, 1f),outlineWidth);
        Texture btnTexture = new Texture(btnPixmap);
        // Hover
        Pixmap btnOverPixmap = PixmapLibrary.getRoundedSquare(btnWidth, btnHeight, rounding, new Color(0.2f*0.5f, 0.4f*1.7f, 0.9f*1.4f, 1f), outlineWidth);
        Texture btnOverTexture = new Texture(btnOverPixmap);
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
                // öffne das Settings-Menü
                table.setVisible(false);
                showSettingsMenu();
            }
        });

        table.add(btn1).pad(20).width(btnWidth).height(btnHeight).row();
        table.add(btn2).pad(20).width(btnWidth).height(btnHeight).row();
        table.add(btn3).pad(20).width(btnWidth).height(btnHeight).row();
        table.add(settingsBtn).pad(40).width(btnWidth).height(btnHeight);
    }

    private void showSettingsMenu() {

        if(settingsTable!=null)
        {
            settingsTable.setVisible(true);
            stage.addListener(settingsInputListener);
            return;
        }

        settingsTable= new Table();
        settingsTable.setFillParent(true);
        settingsTable.center();
        // Engere Anordnung für Hochkant (Mobilgerät)
        final float panelWidth = 300f; // schmaleres Layout
        settingsTable.padTop(30);
        stage.addActor(settingsTable);

        Label.LabelStyle labelStyle = new Label.LabelStyle(normalFont, Color.WHITE);
        // kleiner skalierter Font für Mobil
        //labelStyle.font.getData().setScale(0.5f);


        final Label styleLabel = new Label(game.getConfig().gameStyle.name(), labelStyle);
       // styleLabel.setWrap(true);
        styleLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
        styleLabel.setFontScale(0.5f);

        TextButtonStyle simpleStyle = new TextButtonStyle();
        simpleStyle.font = normalFont;
        simpleStyle.fontColor = Color.WHITE;



        Texture tsheet = game.assetManager.get("Buttons/Blue_Buttons_Pixel.png");

        Drawable leftup = new TextureRegionDrawable(new TextureRegion(tsheet, 80, 0, 16,16));
        Drawable leftdown = new TextureRegionDrawable(new TextureRegion(tsheet, 240, 0, 16,16));

        Drawable rightup =  new TextureRegionDrawable(new TextureRegion(tsheet , 96, 0,16,16));
        Drawable rightdown =  new TextureRegionDrawable(new TextureRegion(tsheet , 256, 0,16,16));

        Drawable confirmup = new TextureRegionDrawable(new TextureRegion(tsheet , 0, 32,16,16));
        Drawable confirmdown = new TextureRegionDrawable(new TextureRegion(tsheet , 160, 32,16,16));

        Drawable cancelup = new TextureRegionDrawable(new TextureRegion(tsheet , 0, 16, 16,16));
        Drawable canceldown = new TextureRegionDrawable(new TextureRegion(tsheet , 160, 16, 16,16));



        ImageButton.ImageButtonStyle leftStyle = new ImageButton.ImageButtonStyle();
        ImageButton.ImageButtonStyle rightStyle = new ImageButton.ImageButtonStyle();
        ImageButton.ImageButtonStyle confirmStyle = new ImageButton.ImageButtonStyle();
        ImageButton.ImageButtonStyle cancelStyle = new ImageButton.ImageButtonStyle();



        leftStyle.up = leftup;
        leftStyle.down = leftdown;
        rightStyle.up = rightup;
        rightStyle.down = rightdown;
        confirmStyle.up = confirmup;
        confirmStyle.down = confirmdown;
        cancelStyle.up = cancelup;
        cancelStyle.down = canceldown;


        final ImageButton leftImgBtn =  new ImageButton(leftStyle) ;
        final ImageButton rightImgBtn = new ImageButton(rightStyle);
        final ImageButton confirmImgBtn =  new ImageButton(confirmStyle);
        final ImageButton cancelImgBtn =  new ImageButton(cancelStyle);



        ClickListener changeLeft = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameStyle[] styles = GameStyle.values();
                int idx = game.getConfig().gameStyle.ordinal();
                idx = (idx - 1 + styles.length) % styles.length;
                game.getConfig().gameStyle = styles[idx];
                styleLabel.setText(game.getConfig().gameStyle.name());
            }
        };
        ClickListener changeRight = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameStyle[] styles = GameStyle.values();
                int idx = game.getConfig().gameStyle.ordinal();
                idx = (idx + 1) % styles.length;
                game.getConfig().gameStyle = styles[idx];
                styleLabel.setText(game.getConfig().gameStyle.name());
            }
        };
        ClickListener confirmClick = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsTable.setVisible(false);
                table.setVisible(true);
                // Entferne Listener
                stage.removeListener(settingsInputListener);
            }
        };
        ClickListener cancelClick = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Abbrechen: keine Änderung an game.getConfig() nötig, wir gehen zurück
                settingsTable.setVisible(false);
                table.setVisible(true);
                stage.removeListener(settingsInputListener);
            }
        };



        leftImgBtn.addListener(changeLeft);
        rightImgBtn.addListener(changeRight);
        confirmImgBtn.addListener(confirmClick);
        cancelImgBtn.addListener(cancelClick);

        // Aufbau: Titel, mittleres schmales Panel mit links/label/rechts (in einer Zeile), dann Confirm/Cancel
        //settingsTable.add(title).width(panelWidth).padBottom(12).row();

        leftImgBtn.setSize(64, 64);
        rightImgBtn.setSize(64, 64);
        // Style-Name in eigener, schmaler Zeile (besser für Hochkant/Handy)
        settingsTable.add(leftImgBtn).size(64,64).pad(10);
        settingsTable.add(styleLabel).width(panelWidth - 40).pad(60);
        settingsTable.add(rightImgBtn).size(64,64).pad(10).row();

       // settingsTable.add().width(20); // kleiner Abstand



       settingsTable.add(confirmImgBtn).pad(12).width(64).height(64);
       settingsTable.add(cancelImgBtn).pad(12).width(64).height(64);




        settingsTable.setVisible(true);



        // Erstelle neuen Listener, der Pfeiltasten verarbeitet (noch sinnvoll für Desktop)
        settingsInputListener = new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                GameStyle[] styles = GameStyle.values();
                int idx = game.getConfig().gameStyle.ordinal();
                if(keycode == Input.Keys.LEFT) {
                    idx = (idx - 1 + styles.length) % styles.length;
                    game.getConfig().gameStyle = styles[idx];
                    styleLabel.setText(game.getConfig().gameStyle.name());
                    return true;
                } else if(keycode == Input.Keys.RIGHT) {
                    idx = (idx + 1) % styles.length;
                    game.getConfig().gameStyle = styles[idx];
                    styleLabel.setText(game.getConfig().gameStyle.name());
                    return true;
                } else if(keycode == Input.Keys.ENTER) {
                    // Bestätigen: Schließe Settings und zurück zum Hauptmenü
                    settingsTable.setVisible(false);
                    table.setVisible(true);
                    stage.removeListener(this);

                    return true;
                } else if(keycode == Input.Keys.ESCAPE) {
                    // Abbrechen: nicht bestätigen, zurück zum Hauptmenü
                    settingsTable.setVisible(false);
                    table.setVisible(true);
                    stage.removeListener(this);

                    return true;
                }
                return false;
            }
        };


        Gdx.input.setInputProcessor(stage);
        stage.addListener(settingsInputListener);
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
        Gdx.input.setInputProcessor(null);
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
