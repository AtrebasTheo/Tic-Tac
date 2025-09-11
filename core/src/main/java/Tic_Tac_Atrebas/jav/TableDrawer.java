package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.tommyettinger.colorful.ipt.ColorfulBatch;

public abstract class TableDrawer {
    abstract void drawClassicTable(float x,float y,int[][] fields, float fieldwidth,int hoverX,int hoverY);
    abstract void drawX(float x, float y);
    abstract void drawO(float x, float y);
    abstract void drawBackground();
    abstract void drawSquareHoverHighlight(int x, int y, float width);
    abstract void dispose();
    TextureRegion background;
    ShapeRenderer shapeRenderer;
    //SpriteBatch spriteBatch;
    ColorfulBatch colorfulBatch;
}

class PaperTableDrawer extends TableDrawer{

    PaperTableDrawer()
    {
        background=new TextureRegion(new Texture("backgrounds/paper_1280.jpg"));
        shapeRenderer=new ShapeRenderer();
        colorfulBatch=new ColorfulBatch();
    }
    @Override
    public void drawClassicTable(float x, float y, int[][] fields, float fieldwidth,int hoverX,int hoverY) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (hoverX >= 0 && hoverY >= 0) {
            drawSquareHoverHighlight(hoverX, hoverY,fieldwidth);
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl20.glLineWidth(5);
        shapeRenderer.setColor(0, 0, 0, 1);
        for (int i = 1; i < 3; i++) {
            shapeRenderer.line(x +i * fieldwidth, y, x +i * fieldwidth, y +fields[0].length * fieldwidth);
            shapeRenderer.line(x, y +i * fieldwidth, x +fields.length*fieldwidth, y +i * fieldwidth);
        }


        // X und O zeichnen
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (fields[i][j] == 1) drawX(i, j);
                if (fields[i][j] == 2) drawO(i, j);
            }
        }
    }

    @Override
    public void drawX(float x, float y) {

    }

    @Override
    public void drawO(float x, float y) {

    }

    @Override
    public void drawBackground() {

    }

    @Override
    void drawSquareHoverHighlight(int x, int y, float width) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setColor(0.2f, 0.6f, 1f, 0.35f); // halbtransparentes Blau
        float px = x + x * width;
        float py = y + y * width;
        shapeRenderer.rect(px, py, width, width);
    }

    @Override
    public void dispose() {

    }
}
