package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class PixmapLibrary {
    public static Pixmap getColourGradientMap(Color topColor, Color bottomColor, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for (int y = 0; y < height; y++) {
            float ratio = (float) y / (height - 1);
            Color color = new Color(
                topColor.r * (1 - ratio) + bottomColor.r * ratio,
                topColor.g * (1 - ratio) + bottomColor.g * ratio,
                topColor.b * (1 - ratio) + bottomColor.b * ratio,
                topColor.a * (1 - ratio) + bottomColor.a * ratio
            );
            pixmap.setColor(color);
            pixmap.drawLine(0, y, width - 1, y);
        }
        return pixmap;
    }

    // Generates a square tunnel-like gradient map with two colors
    //@param innerPuffer: Between 1-Infinity, How much the inner color is "puffed out" towards the outer color
    //@param outerPuffer: Between 1-Infinity, How much the outer color is "puffed out" towards
    public static Pixmap getTunnelMap(Color outerColor, Color innerColor,float innerPuffer,float outerPuffer, int width) {
        Pixmap pixmap = new Pixmap(width, width, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        for (int i = 0; i < width/2; i++) {
            float ratio = (float) i / (width/2 - 1);
            Color color = new Color(
                outerColor.r * Math.clamp((1*outerPuffer - ratio*innerPuffer),0,1) + innerColor.r * Math.min((ratio*innerPuffer-outerPuffer+1)*outerPuffer,1),
                outerColor.g * Math.clamp((1*outerPuffer - ratio*innerPuffer),0,1) + innerColor.g *  Math.min((ratio*innerPuffer-outerPuffer+1)*outerPuffer,1),
                outerColor.b * Math.clamp((1*outerPuffer - ratio*innerPuffer),0,1) + innerColor.b *  Math.min((ratio*innerPuffer-outerPuffer+1)*outerPuffer,1),
                outerColor.a * Math.clamp((1*outerPuffer - ratio*innerPuffer),0,1) + innerColor.a *  Math.min((ratio*innerPuffer-outerPuffer+1)*outerPuffer,1)
            );
            pixmap.setColor(color);
            pixmap.fillRectangle( i,i,width-i*2,width-i*2);

        }
        return pixmap;
    }
    public static Pixmap getTunnelMap(Color outerColor, int width)
    {
        return getTunnelMap(outerColor,new Color(outerColor.r*0.2f,outerColor.g*0.2f,outerColor.b*0.2f,outerColor.a),1f,1,width);
    }
    public static Pixmap getTunnelMap(Color outerColor,float innerPuffer,float outerPuffer, int width)
    {
        return getTunnelMap(outerColor,new Color(outerColor.r*0.01f,outerColor.g*0.01f,outerColor.b*0.01f,outerColor.a),innerPuffer,outerPuffer,width);
    }


    public static Pixmap getRoundedSquare(int width, int height, int radius,Color color)
    {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(radius, 0, width - 2 * radius, height);
        pixmap.fillRectangle(0, radius, width, height - 2 * radius);
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(width - radius - 1, radius, radius);
        pixmap.fillCircle(radius, height - radius - 1, radius);
        pixmap.fillCircle(width - radius - 1, height - radius - 1, radius);
       return pixmap;
    }
    public static Pixmap getRoundedSquare(int width, int height, float rounding,Color color)
    {
        return getRoundedSquare(width,height,(int) (Math.clamp(rounding,0,1)* Math.min(width/2,height/2)),color);
    }
    public static Pixmap getRoundedSquare(int width, int height, float rounding,Color color,Color outlinecolor,int outlineWidth)
    {
        Pixmap pixmap;
        if(outlineWidth>0){
            outlineWidth =Math.min(outlineWidth,height/2);
            outlineWidth =Math.min(outlineWidth,width/2);
            int radius= (int) (Math.clamp(rounding,0,1)* Math.min(width/2-outlineWidth,height/2 -outlineWidth));
            pixmap=  getRoundedSquare(width,height,radius+outlineWidth/2,outlinecolor);
            Pixmap addmap=getRoundedSquare(width-2*outlineWidth,height-2*outlineWidth,radius,color);
            pixmap.drawPixmap(addmap,outlineWidth,outlineWidth);
            addmap.dispose();
        }
        else{
            pixmap= getRoundedSquare(width,height,rounding,color);
        }
        return pixmap;
    }
    public static Pixmap getRoundedSquare(int width, int height, float rounding,Color color,int outlineWidth)
    {
        return getRoundedSquare(width,height,rounding,color,Color.BLACK,outlineWidth);
    }

    public static Pixmap addColorOverlay(Pixmap pixmap, Color overlay, boolean exclusive) {
        pixmap.setBlending(Pixmap.Blending.SourceOver);
        if(exclusive)
        {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                for (int x = 0; x < pixmap.getWidth(); x++) {
                    int pixel = pixmap.getPixel(x, y);
                    Color c = new Color();
                    Color.rgba8888ToColor(c, pixel);
                    if (c.a > 0f) { // Nur nicht-leere Pixel
                        // Overlay anwenden (Alpha-Blending)
                        c.lerp(overlay, overlay.a);
                        pixmap.drawPixel(x, y, Color.rgba8888(c));
                    }
                }
            }
        }
        else{
        pixmap.setColor(overlay);
        pixmap.fillRectangle(0,0, pixmap.getWidth(), pixmap.getHeight());
        }
        return pixmap;
    }
    public static Pixmap addColorOverlay(Pixmap pixmap, Color color) {
        return addColorOverlay(pixmap, color, true);
    }


}
