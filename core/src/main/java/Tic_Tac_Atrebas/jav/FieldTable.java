package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class FieldTable {
     float xco, yco;
     float fieldwidth = 100f; // Width of each field
     int currentPlayer;
     int winner=-1; // 0=draw, 1=player1, 2=player2
    int winLength;
    int hoverX = -1, hoverY = -1;
    boolean gameOver;
    float winningstate=0;
    AIPlayer aiPlayer;
    TableDrawer drawer;
    abstract  void setCenter(float centerx, float centery);
    abstract boolean makeMove(float xt, float yt);
    abstract void render(ShapeRenderer shape);
    abstract void reset();
    abstract boolean checkWin(int player);
    abstract void mouseMoved(float screenX, float screenY);
    //abstract void act(float delta);
    abstract boolean full();
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

}
