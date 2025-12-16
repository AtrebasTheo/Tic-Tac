package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    Texture backgroundTexture;
    private StretchViewport stretchViewport;
    private SpriteBatch spriteBatch;
    private GameConfiguration config;
    AssetManager assetManager;
    enum State {
        LOAD,START, PLAY,GAMEOVER, RESTART, EXIT
    }
    State state= State.LOAD;
    StartScreen startScreen;
    @Override
    public void create() {
        spriteBatch=new SpriteBatch();
        stretchViewport=new StretchViewport(1000, 1000);
        assetManager= new AssetManager();
        config=new GameConfiguration();
        loadAssets();
        Pixmap pixmap = PixmapLibrary.getColourGradientMap(new Color(0.1f, 0.3f , 0.8f, 1f),new Color(0.1f, 0.3f + 0.45f, 0.8f, 1f),1,256);

        backgroundTexture= new Texture(pixmap);
        pixmap.dispose();

    }

    GameConfiguration getConfig(){
        return config;
    }

    @Override
    public void render() {
       // System.out.println(viewport.getScreenWidth());
        if(state==State.LOAD){
            if(assetManager.update(5))
            {
                state=State.START;
                startScreen= new StartScreen(this);

                //startScreen.setButtonAssetKeys(leftKey, rightKey, confirmKey, cancelKey);
                 setScreen(startScreen);
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stretchViewport.apply();
        spriteBatch.setProjectionMatrix(stretchViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, stretchViewport.getWorldWidth(), stretchViewport.getWorldHeight());
        spriteBatch.end();
        super.render();
    }

    void loadAssets() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter normalFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        normalFont.fontFileName = "BruceForeverRegular-X3jd2.ttf";
        normalFont.fontParameters.size = 45;
        normalFont.fontParameters.borderWidth=5;
        normalFont.fontParameters.borderColor = Color.BLACK; // Farbe der Umrandung
        normalFont.fontParameters.borderStraight = false;
        assetManager.load("bruce.ttf", BitmapFont.class, normalFont);
        assetManager.load("Buttons/Blue_Buttons_Pixel.png", Texture.class);
    }


    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
        stretchViewport.update(width, height, true);
        // Hintergrund-Textur an neue Größe anpassen
        super.resize(width, height);

    }
}
