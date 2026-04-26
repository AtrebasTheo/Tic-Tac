package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.tommyettinger.colorful.ipt.ColorfulBatch;

public abstract class TableDrawer {
    abstract void drawClassicTable(float x,float y,int[][] fields, float fieldwidth,int hoverX,int hoverY);
    abstract void drawX(float x, float y);
    abstract void drawO(float x, float y);
    abstract void drawTableBackground();
    abstract void drawSquareHoverHighlight(int x, int y,float xco, float yco, float width);
    abstract void dispose();
    TextureRegion background;
    Texture xTexture,oTexture;
    ShapeRenderer shapeRenderer;
    //SpriteBatch spriteBatch;
    SpriteBatch colorfulBatch;
}

class PaperTableDrawer extends TableDrawer{
    PaperTableDrawer()
    {
        shapeRenderer=new ShapeRenderer();
        colorfulBatch=new SpriteBatch();

        int iconSize=100;
        Pixmap xPixmap = new Pixmap(iconSize, iconSize, Pixmap.Format.RGBA8888);
        xPixmap.setBlending(Pixmap.Blending.None);

        xPixmap.setColor(1, 1, 0, 1);
        //xPixmap.fillRectangle(0,0,iconSize,iconSize);

        xPixmap.setColor(1, 0, 0, 1);
        int offset=(int)(iconSize*0.2f);
            for (int i = -4; i <= 4; i++)
            {
                xPixmap.drawLine(offset+i, offset, iconSize-offset+i, iconSize-offset);
                xPixmap.drawLine(offset+i, iconSize-offset, iconSize-offset+i, offset);
            }
        xTexture = new Texture(xPixmap);
        xPixmap.dispose();

        Pixmap oPixmap = new Pixmap(iconSize, iconSize, Pixmap.Format.RGBA8888);
        oPixmap.setBlending(Pixmap.Blending.None);
       // xPixmap.setColor(1, 1, 0, 1);
        //xPixmap.fillRectangle(0,0,iconSize,iconSize);
        oPixmap.setColor(0, 0, 1, 1);
        oPixmap.fillCircle(iconSize/2, iconSize/2, iconSize/2-8);
        oPixmap.setColor(0, 0, 0, 0);
        oPixmap.fillCircle(iconSize/2, iconSize/2, iconSize/2-14);
        oTexture = new Texture(oPixmap);
        oPixmap.dispose();

    }
    @Override
    public void drawClassicTable(float x, float y, int[][] fields, float fieldwidth,int hoverX,int hoverY) {
        // fixed:  y-=fieldwidth/2; //I have no idea why this is necessary but otherwise the table is drawn too high (it worked before I refactored/copyd the code into TableDrawer)

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (hoverX >= 0 && hoverY >= 0) {
            drawSquareHoverHighlight(hoverX, hoverY,x,y,fieldwidth);
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl20.glLineWidth(5);
        shapeRenderer.setColor(0, 0, 0, 1);
        for (int i = 1; i < fields.length; i++) {
            shapeRenderer.line(x +i * fieldwidth, y, x +i * fieldwidth, y +fields[0].length * fieldwidth);
            shapeRenderer.line(x, y +i * fieldwidth, x +fields.length*fieldwidth, y +i * fieldwidth);
        }
        shapeRenderer.end();
        colorfulBatch.begin();
        // X und O zeichnen
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                if (fields[i][j] == 1){
                    float xX = x +i * fieldwidth, yX = y +j * fieldwidth;
                    drawX(xX, yX);
                }
                if (fields[i][j] == 2){
                    float xO = x +i * fieldwidth , yO = y +j * fieldwidth;
                    drawO(xO, yO);
                }
            }
        }
        colorfulBatch.end();
    }

    @Override
    public void drawX(float x, float y) {
        /*shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.line(x + 10, y + 10, x + 90, y + 90);
        shapeRenderer.line(x + 10, y + 90, x + 90, y + 10);*/
        colorfulBatch.draw(xTexture,x,y);
    }

    @Override
    public void drawO(float x, float y) {
        /*shapeRenderer.setColor(0, 0, 1, 1);
        shapeRenderer.circle(x, y, 40);*/
        colorfulBatch.draw(oTexture,x,y);
    }

    @Override
    public void drawTableBackground() {
        //colorfulBatch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    }

    @Override
    void drawSquareHoverHighlight(int x, int y, float xco, float yco, float width) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setColor(0.2f, 0.6f, 1f, 0.35f); // halbtransparentes Blau
        float px = xco+x + x * width;
        float py = yco+y + y * width;
        shapeRenderer.rect(px, py, width, width);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        colorfulBatch.dispose();
        oTexture.dispose();
        xTexture.dispose();
    }
}
