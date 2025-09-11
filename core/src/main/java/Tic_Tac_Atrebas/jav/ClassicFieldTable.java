package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Arrays;

@SuppressWarnings("ALL")
public class ClassicFieldTable extends FieldTable{
    private final int[][] fields;
    public ClassicFieldTable(int width, int height,float fieldwidth) {
        fields = new int[width][height]; // 0=empty, 1=X, 2=O
        currentPlayer = 1; // Start with player 1
        this.fieldwidth = fieldwidth; // Set the field width
    }
    public ClassicFieldTable(int size) {
        this(size, size,100); // Default to a 3x3 field
    }
    public ClassicFieldTable(int size,float fieldwidth) {
        this(size, size,fieldwidth); // Default to a 3x3 field
    }

    public void setCenter(float centerx, float centery) {
        this.xc = centerx- (fieldwidth * fields.length) / 2f;
        this.yc = centery- (fieldwidth * fields[0].length) / 2f;
    }
    public boolean makeMove(float xt, float yt) {
        int x=(int) (Math.floor(  (xt-xc) / fieldwidth));
        int y=(int) (Math.floor((yt-yc) / fieldwidth));
        if (x < 0 || x >= 3 || y < 0 || y >= 3 || fields[x][y] != 0) {
            System.out.println("invalid");
            System.out.println("x: "+x+" y: "+y);
            return false; // Invalid move
        }
        fields[x][y] = currentPlayer;
        gameOver=checkWin(currentPlayer);
        currentPlayer = (currentPlayer == 1) ? 2 : 1; // Switch player
        return true;
    }

    public void render(ShapeRenderer shape) {
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            gameOver=true; // For testing purposes, pressing Enter ends the game
        }
        Gdx.gl20.glLineWidth(5);
        shape.setColor(0, 0, 0, 1);
        for (int i = 1; i < 3; i++) {
            shape.line(xc+i * fieldwidth, yc, xc+i * fieldwidth, yc+fields[0].length * fieldwidth);
            shape.line(xc, yc+i * fieldwidth, xc+fields.length*fieldwidth, yc+i * fieldwidth);
        }


        // X und O zeichnen
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (fields[i][j] == 1) drawX(i, j,shape);
                if (fields[i][j] == 2) drawO(i, j,shape);
            }
        }
    }

    private void drawX(int i, int j,ShapeRenderer shape) {
        float x = xc+i * fieldwidth, y = yc+j * fieldwidth;
        super.drawX(x,y,shape);
    }

    private void drawO(int i, int j,ShapeRenderer shape) {
        float x = xc+i * fieldwidth + fieldwidth/2f, y = yc+j * fieldwidth+fieldwidth/2f;
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
    }
}
