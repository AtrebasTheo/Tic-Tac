package Tic_Tac_Atrebas.jav;

import java.util.Arrays;

public class ClassicGameModel extends GameModel
{
    final int[][] fields;
    int[][] aiFields;

    public ClassicGameModel(GameConfiguration config) {
        fields = new int[config.tableWidth][config.tableHeight]; // 0=empty, 1=X, 2=O
        aiFields = new int[config.tableWidth][config.tableHeight];
        currentPlayer = 1; // Start with player 1
        winLength= (int) Math.ceil(Math.max(config.tableWidth,config.tableHeight)/2f)+1;
        //winlength=config.winLength
    }

    // New constructor to attach to an existing fields array and explicit winLength
    public ClassicGameModel(int[][] fieldsRef, int winLength) {
        this.fields = fieldsRef;
        this.aiFields = new int[fieldsRef.length][fieldsRef[0].length];
        this.currentPlayer = 1;
        this.winLength = winLength;
        refreshAiBoard();
    }

    public boolean makeMoveAtField(int index) {
        int x = index / fields[0].length;
        int y = index % fields[0].length;
        return makeMoveAtField(x, y);
    }

        @Override
    public boolean makeMoveAtField(int x, int y) {
        if (x < 0 || x >= fields.length || y < 0 || y >= fields[0].length || fields[x][y] != 0) {
            System.out.println("invalid");
            System.out.println("x: "+x+" y: "+y);
            return false; // Invalid move
        }
        fields[x][y] = currentPlayer;
        refreshAiBoard();

        //gameOver=checkWin(currentPlayer);
        gameOver=checkWin(currentPlayer,x,y);

        if(gameOver){
            winner=currentPlayer;
        }
        else{
            if(full()){
                gameOver=true;//Draw
                winner=0;}
        }

        if(!gameOver)
        {
            currentPlayer = (currentPlayer == 1) ? 2 : 1; // Switch player
        }
        return true;
    }

    @Override
    public synchronized void refreshAiBoard() {
        for (int i = 0; i < fields.length; i++) {
            System.arraycopy(fields[i], 0, aiFields[i], 0, fields[i].length);
        }
    }

    public int[][] getAiBoard() {
        return aiFields;
    }

    public double[] getFieldsArray() {
        double[] flatFields = new double[fields.length * fields[0].length];
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                flatFields[i * fields[i].length + j] = fields[i][j];
            }
        }
        return flatFields;
    }

    public void reset() {
        for(int i = 0; i < fields.length; i++) {
            // Reset all fields to empty
            Arrays.fill(fields[i], 0);
        }
        refreshAiBoard();
        currentPlayer = 1;
        gameOver=false;
    }

    @Override
    boolean checkWin(int player, int lastmoveX, int lastmoveY) {
        return checkWin(fields, player, lastmoveX, lastmoveY);
    }

    @Override
    public boolean checkWin(int[][] board, int player, int lastmoveX, int lastmoveY) {
        if (1 + countDir(board, player, lastmoveX, lastmoveY,  1,  0) +
            countDir(board, player, lastmoveX, lastmoveY, -1,  0) >= winLength) return true;

        if (1 + countDir(board, player, lastmoveX, lastmoveY,  0,  1) +
            countDir(board, player, lastmoveX, lastmoveY,  0, -1) >= winLength) return true;

        if (1 + countDir(board, player, lastmoveX, lastmoveY,  1,  1) +
            countDir(board, player, lastmoveX, lastmoveY, -1, -1) >= winLength) return true;

        if (1 + countDir(board, player, lastmoveX, lastmoveY, -1,  1) +
            countDir(board, player, lastmoveX, lastmoveY,  1, -1) >= winLength) return true;

        return false;
    }

    private int countDir(int[][] board, int player, int lastX, int lastY, int dx, int dy) {
        int sizeX = board.length;
        int sizeY = board[0].length;
        int cx = lastX + dx;
        int cy = lastY + dy;
        int c = 0;
        while (cx >= 0 && cy >= 0 && cx < sizeX && cy < sizeY
            && board[cx][cy] == player) {
            c++;
            cx += dx;
            cy += dy;
        }
        return c;
    }



    public boolean empty()
    {
        return empty(fields);
    }

    public boolean empty(int[][] board)
    {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j]!=0) return false;
            }
        }
        return true;
    }

    public boolean full()
    {
        return full(fields);
    }

    public boolean full(int[][] board)
    {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j]==0) return false;
            }
        }
        return true;
    }
}
