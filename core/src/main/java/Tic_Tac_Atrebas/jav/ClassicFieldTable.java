package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Arrays;

@SuppressWarnings("ALL")
public class ClassicFieldTable extends FieldTable{
    private final int[][] fields;
    public ClassicFieldTable(int width, int height,float fieldwidth) {
        fields = new int[width][height]; // 0=empty, 1=X, 2=O
        currentPlayer = 1; // Start with player 1
        this.fieldwidth = fieldwidth; // Set the field width
        drawer= new PaperTableDrawer();
    }
    public ClassicFieldTable(int size) {
        this(size, size,100); // Default field
    }
    public ClassicFieldTable(int size,float fieldwidth) {
        this(size, size,fieldwidth); // Default to a 3x3 field
    }

    public void setCenter(float centerx, float centery) {
        this.xco = centerx- (fieldwidth * fields.length) / 2f;
        this.yco = centery- (fieldwidth * fields[0].length) / 2f;
    }
    public boolean makeMove(float xt, float yt) {
        int x=(int) (Math.floor(  (xt- xco) / fieldwidth));
        int y=(int) (Math.floor((yt- yco) / fieldwidth));
        if (x < 0 || x >= fields.length || y < 0 || y >= fields[0].length || fields[x][y] != 0) {
            System.out.println("invalid");
            System.out.println("x: "+x+" y: "+y);
            return false; // Invalid move
        }
        fields[x][y] = currentPlayer;
        if(hoverX==x&&hoverY==y) { // If the hovered field was just played, remove the hover effect
        hoverX=-1; hoverY=-1;}
        gameOver=checkWin(currentPlayer);
        if(!gameOver)
        {currentPlayer = (currentPlayer == 1) ? 2 : 1; // Switch player
        }
        return true;
    }

    public void render(ShapeRenderer shape) {

        shape.end();
        /*shape.begin(ShapeRenderer.ShapeType.Filled);
        if (hoverX >= 0 && hoverY >= 0) {
            drawHighlight(hoverX, hoverY, shape);
        }
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            gameOver=true; ///FIXME For testing purposes, pressing Enter ends the game
        }
        Gdx.gl20.glLineWidth(5);
        shape.setColor(0, 0, 0, 1);
        for (int i = 1; i < 3; i++) {
            shape.line(xco +i * fieldwidth, yco, xco +i * fieldwidth, yco +fields[0].length * fieldwidth);
            shape.line(xco, yco +i * fieldwidth, xco +fields.length*fieldwidth, yco +i * fieldwidth);
        }


        // X und O zeichnen
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (fields[i][j] == 1) drawX(i, j,shape);
                if (fields[i][j] == 2) drawO(i, j,shape);
            }
        }*/
        drawer.drawClassicTable(xco,yco,fields,fieldwidth,hoverX,hoverY);
    }

    private void drawX(int i, int j,ShapeRenderer shape) {
        float x = xco +i * fieldwidth, y = yco +j * fieldwidth;
        super.drawX(x,y,shape);
    }

    private void drawO(int i, int j,ShapeRenderer shape) {
        float x = xco +i * fieldwidth + fieldwidth/2f, y = yco +j * fieldwidth+fieldwidth/2f;
        super.drawO(x,y,shape);
    }

   boolean checkWin(int player) {
        for (int i = 0; i < 3; i++) {
            if (fields[i][0] == player && fields[i][1] == player && fields[i][2] == player) return true;
            if (fields[0][i] == player && fields[1][i] == player && fields[2][i] == player) return true;
        }
        if (fields[0][0] == player && fields[1][1] == player && fields[2][2] == player) return true;
      return fields[0][2] == player && fields[1][1] == player && fields[2][0] == player;
  }

    public void reset() {
        for(int i = 0; i < fields.length; i++) {
            // Reset all fields to empty
            Arrays.fill(fields[i], 0);
        }
        currentPlayer = 1;
        gameOver=false;
    }
    void mouseMoved(float screenX, float screenY)
    {
        int x = (int) Math.floor((screenX - xco) / fieldwidth);
        int y = (int) Math.floor((screenY - yco) / fieldwidth);
        if (x >= 0 && x < fields.length && y >= 0 && y < fields[0].length && fields[x][y] == 0 && !gameOver) {
            hoverX = x;
            hoverY = y;
        } else {
            hoverX = -1;
            hoverY = -1;
        }

    }
    public void drawHighlight(int x, int y, ShapeRenderer shape) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.setColor(0.2f, 0.6f, 1f, 0.35f); // halbtransparentes Blau
        float px = xco + x * fieldwidth;
        float py = yco + y * fieldwidth;
        shape.rect(px, py, fieldwidth, fieldwidth);
    }
}
