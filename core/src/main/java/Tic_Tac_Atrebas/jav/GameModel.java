package Tic_Tac_Atrebas.jav;

public abstract class GameModel {
    int currentPlayer;
    int winner= -1 ; // 0=draw, 1=player1, 2=player2
    int winLength;
    boolean gameOver;
    float winningState=0;
    abstract void reset();
    abstract  int[][] getAiBoard();
    abstract boolean makeMoveAtField(int x, int y);
    abstract boolean checkWin(int[][] board, int player, int lastmoveX, int lastmoveY);
    abstract boolean checkWin( int player, int lastmoveX, int lastmoveY);
    abstract boolean full();
    abstract boolean full(int [][] board);
    abstract  boolean empty();
    abstract  boolean empty(int [][] board);
    abstract void refreshAiBoard();
    public GameModel() {
        currentPlayer = 1; // Start with player 1
    }
}



