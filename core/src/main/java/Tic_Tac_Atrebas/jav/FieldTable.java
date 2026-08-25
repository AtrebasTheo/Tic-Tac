package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class FieldTable extends Actor {
     float fieldWidth = 100f; // Width of each field
     int currentPlayer;
     int winner= -1 ; // 0=draw, 1=player1, 2=player2
    int winLength;
    int hoverX = -1, hoverY = -1;
    boolean gameOver;
    float winningState=0;
    TableDrawer drawer;
    abstract  void setCenter(float centerx, float centery);
    abstract boolean makeMove(float xt, float yt);
    abstract void reset();
    abstract boolean checkWin(int player,int lx, int ly);
    abstract void mouseMoved(float screenX, float screenY);
    abstract  void refreshAiBoard();
    abstract  int[][] getAiBoard();
    abstract boolean makeMoveAtField(int x, int y);
    public FieldTable(float fieldwidth) {
        currentPlayer = 1; // Start with player 1
        this.fieldWidth = fieldwidth; // Set the field width
    }
    public FieldTable() {
        this(100); // Default
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

}
