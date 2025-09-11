package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class FieldTable {
     float xco, yco;
     float fieldwidth = 100f; // Width of each field
     int currentPlayer;
    int hoverX = -1, hoverY = -1;
    boolean gameOver;
    abstract  void setCenter(float centerx, float centery);
    abstract boolean makeMove(float xt, float yt);
    abstract void render(ShapeRenderer shape);
    abstract void reset();
    abstract boolean checkWin(int player);
    abstract void mouseMoved(float screenX, float screenY);

    public FieldTable(float fieldwidth) {
        currentPlayer = 1; // Start with player 1
        this.fieldwidth = fieldwidth; // Set the field width
    }
    public FieldTable() {
        this(100); // Default
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }




    void drawX(float x, float y,ShapeRenderer shape) {
        shape.setColor(1, 0, 0, 1);
        shape.line(x + 10, y + 10, x + 90, y + 90);
        shape.line(x + 10, y + 90, x + 90, y + 10);
    }

     void drawO(float x, float y,ShapeRenderer shape) {
        shape.setColor(0, 0, 1, 1);
        shape.circle(x, y, 40);
    }
}
