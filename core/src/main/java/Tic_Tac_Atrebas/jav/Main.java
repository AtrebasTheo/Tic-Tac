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
        loadAssets();
       // Pixmap pixmap = PixmapLibrary.getTunnelMap(new Color(0.3f,0.7f,1f,1),1.4f,1f,1000);
        //Pixmap pixmap = PixmapLibrary.getTunnelMap(new Color(0.3f,0.7f,1f,1),new Color(0.1f,0.2f,0.4f,1),2f,1,1000);
        //Pixmap pixmap = PixmapLibrary.getTunnelMap(new Color(0.1f,0.2f,0.4f,1),new Color(0.3f,0.7f,1f,1),2f,2f,1000);
        Pixmap pixmap = PixmapLibrary.getColourGradientMap(new Color(0.1f, 0.3f , 0.8f, 1f),new Color(0.1f, 0.3f + 0.45f, 0.8f, 1f),1,256);
        /*for (int y = 0; y < 256; y++) {
            pixmap.setColor(new Color(0.1f, 0.3f + 0.45f * y / 256f, 0.8f, 1f));
            pixmap.drawPixel(0, y);
        }*/
        backgroundTexture= new Texture(pixmap);
        pixmap.dispose();
    }

    private static Pixmap getPixmap() {
        Pixmap pixmap2= new Pixmap(500,500,Pixmap.Format.RGBA8888);
        pixmap2.setBlending(Pixmap.Blending.None);
        for (int i = 0; i < 250; i++) {
            pixmap2.setColor(new Color(0.3f,0.7f,1f,1-i/200f));
            pixmap2.fillRectangle( i,i,500-i*2,500-i*2);
        }
        return pixmap2;
    }

    @Override
    public void render() {
       // System.out.println(viewport.getScreenWidth());
        if(state==State.LOAD){
            if(assetManager.update(5))
            {
                state=State.START;
                startScreen= new StartScreen(this);
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
        super.resize(width, height);

    }
}
