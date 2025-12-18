package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AdvancedAssetManager extends AssetManager {
    private BitmapFont normalFont;
    private BitmapFont smallFont;
    private TextButton.TextButtonStyle roundedButtonStyle;
    private ImageButton.ImageButtonStyle settingsStyle;
    ImageButton.ImageButtonStyle leftStyle;
    ImageButton.ImageButtonStyle rightStyle;
    ImageButton.ImageButtonStyle confirmStyle;
    ImageButton.ImageButtonStyle cancelStyle;
    public AdvancedAssetManager()
    {

    }

    public void createRemainingAssets()
    {
        createFonts();
        createButtonStyles();
    }

    private void createFonts()
    {
        normalFont = get("bruce.ttf", BitmapFont.class);
    }

    private void createButtonStyles()
    {
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
        roundedButtonStyle = new TextButton.TextButtonStyle();
        roundedButtonStyle.up = new Image(btnTexture).getDrawable();
        roundedButtonStyle.over = new Image(btnOverTexture).getDrawable();
        roundedButtonStyle.down = new Image(btnDownTexture).getDrawable();
        roundedButtonStyle.font = normalFont;
        roundedButtonStyle.fontColor = Color.WHITE;


        Texture blueButtonSheet = get("Buttons/Blue_Buttons_Pixel.png");

        settingsStyle = new ImageButton.ImageButtonStyle();
        settingsStyle.up = new TextureRegionDrawable(new TextureRegion(blueButtonSheet, 32, 0, 16, 16));
        settingsStyle.down = new TextureRegionDrawable(new TextureRegion(blueButtonSheet, 192, 0, 16, 16));

        leftStyle = new ImageButton.ImageButtonStyle();
        leftStyle.up = new TextureRegionDrawable(new TextureRegion(blueButtonSheet, 80, 0, 16,16));
        leftStyle.down = new TextureRegionDrawable(new TextureRegion(blueButtonSheet, 240, 0, 16,16));

        rightStyle = new ImageButton.ImageButtonStyle();
        rightStyle.up =  new TextureRegionDrawable(new TextureRegion(blueButtonSheet , 96, 0,16,16));
        rightStyle.down =  new TextureRegionDrawable(new TextureRegion(blueButtonSheet , 256, 0,16,16));

        confirmStyle = new ImageButton.ImageButtonStyle();
        confirmStyle.up = new TextureRegionDrawable(new TextureRegion(blueButtonSheet , 0, 32,16,16));
        confirmStyle.down = new TextureRegionDrawable(new TextureRegion(blueButtonSheet , 160, 32,16,16));

        cancelStyle = new ImageButton.ImageButtonStyle();
        cancelStyle.up =  new TextureRegionDrawable(new TextureRegion(blueButtonSheet , 0, 16, 16,16));
        cancelStyle.down = new TextureRegionDrawable(new TextureRegion(blueButtonSheet , 160, 16, 16,16));

    }

    public ImageButton.ImageButtonStyle getSettingsButtonStyle() {
        return settingsStyle;
    }


    public TextButton.TextButtonStyle getStandartBlueTextButtonStyle() {return roundedButtonStyle;}
    public ImageButton.ImageButtonStyle getLeftArrowButtonStyle() {return leftStyle;}
    public ImageButton.ImageButtonStyle getRightArrowButtonStyle() {return rightStyle;}
    public ImageButton.ImageButtonStyle getConfirmButtonStyle() {return confirmStyle;}
    public ImageButton.ImageButtonStyle getCancelButtonStyle() {return cancelStyle;}

}
