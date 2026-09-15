package Tic_Tac_Atrebas.jav;

public abstract class GameModel {
    int currentPlayer;
    public int winner= -1 ; // 0=draw, 1=player1, 2=player2
    int winLength;
    public boolean gameOver;
    float winningState=0;
    public abstract void reset();
    public abstract  int[][] getAiBoard();
    public abstract boolean makeMoveAtField(int x, int y);
    public abstract boolean makeMoveAtField(int i);
    abstract boolean checkWin(int[][] board, int player, int lastmoveX, int lastmoveY);
    abstract boolean checkWin( int player, int lastmoveX, int lastmoveY);
    abstract public double[] getFieldsArray();
    public abstract boolean full();
    abstract boolean full(int [][] board);
    abstract  boolean empty();
    abstract  boolean empty(int [][] board);
    abstract void refreshAiBoard();
    public GameModel() {
        currentPlayer = 1; // Start with player 1
    }

}



