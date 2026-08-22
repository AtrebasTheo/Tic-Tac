package Tic_Tac_Atrebas.jav;

import java.util.ArrayList;
import java.util.List;

abstract class AIPlayer{
    public Difficulty difficulty = Difficulty.HARD;
    public int aiNumber; // 1 for X, 2 for O
    public abstract void makeAIMove();
    public abstract int[] calculateBestMove();
    public abstract int[] calculateBestMove(int[][] board);
    public abstract float analyzeWinningState();
    enum Difficulty{EASY,MEDIUM,HARD}
}

public class ClassicAI extends AIPlayer {
    ClassicFieldTable table;
    int humanPlayer;
    public ClassicAI(ClassicFieldTable table, int ainumPlayer) {
        this.table = table;
        this.aiNumber = ainumPlayer;
        this.humanPlayer = (ainumPlayer == 1) ? 2 : 1;
    }

    private int evaluateBoard(int[][] board, int x, int y) {
        if (table.checkWin(board, aiNumber, x, y)) return 10;
        if (table.checkWin(board, humanPlayer, x, y)) return -10;
        return 0;
    }

    int[] checkImmediateWinOrBlock(int[][] board) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == 0) {
                    board[x][y] = aiNumber;
                    if (table.checkWin(board, aiNumber, x, y)) {
                        board[x][y] = 0;
                        return new int[]{x, y};
                    }
                    board[x][y] = 0;
                }
            }
        }

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == 0) {
                    board[x][y] = humanPlayer;
                    if (table.checkWin(board, humanPlayer, x, y)) {
                        board[x][y] = 0;
                        return new int[]{x, y};
                    }
                    board[x][y] = 0;
                }
            }
        }

        return null;
    }

    private int minimax(int[][] board, int depth, int alpha, int beta, boolean maximizingPlayer, int lastX, int lastY) {
        int score = evaluateBoard(board, lastX, lastY);
        if (Math.abs(score) == 10 || table.full(board) || depth == 0) {
            return score;
        }
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int x = 0; x < board.length; x++) {
                for (int y = 0; y < board[0].length; y++) {
                    if (board[x][y] == 0 && hasNeighbor(board, x, y)) {
                        board[x][y] = aiNumber;
                        if (table.checkWin(board, aiNumber, x, y)) {
                            board[x][y] = 0;
                            return 10;
                        }
                        int eval = minimax(board, depth - 1, alpha, beta, false, x, y);
                        board[x][y] = 0;
                        maxEval = Math.max(maxEval, eval);
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha) break;
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int x = 0; x < board.length; x++) {
                for (int y = 0; y < board[0].length; y++) {
                    if (board[x][y] == 0 && hasNeighbor(board, x, y)) {
                        board[x][y] = humanPlayer;
                        if (table.checkWin(board, humanPlayer, x, y)) {
                            board[x][y] = 0;
                            return -10;
                        }
                        int eval = minimax(board, depth - 1, alpha, beta, true, x, y);
                        board[x][y] = 0;
                        minEval = Math.min(minEval, eval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) break;
                    }
                }
            }
            return minEval;
        }
    }

    private boolean hasNeighbor(int[][] board, int x, int y) {
        int[] dx = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};
        for (int i = 0; i < dx.length; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx >= 0 && nx < board.length && ny >= 0 && ny < board[0].length && board[nx][ny] != 0) {
                return true;
            }
        }
        return false;
    }

    void makeRandomMove() {
        List<int[]> availableMoves = new ArrayList<>();
        for (int i = 0; i < table.fields.length; i++) {
            for (int j = 0; j < table.fields[0].length; j++) {
                if (table.fields[i][j] == 0) {
                    availableMoves.add(new int[]{i, j});
                }
            }
        }
        int randomIndex = (int) (Math.random() * availableMoves.size());
        int i = availableMoves.get(randomIndex)[0];
        int j = availableMoves.get(randomIndex)[1];
    }

    public int[] calculateBestMove() {
        return calculateBestMove(table.getAiBoard());
    }

    public int[] calculateBestMove(int[][] board) {
        if (table.empty(board)) {
            return new int[]{board.length / 2, board[0].length / 2};
        }

        int[] field = checkImmediateWinOrBlock(board);
        if (field != null) {
            return field;
        }

        int bestScore = Integer.MIN_VALUE;
        int bestX = -1, bestY = -1;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == 0 && hasNeighbor(board, x, y)) {
                    board[x][y] = aiNumber;
                    int score = minimax(board, 6, Integer.MIN_VALUE, Integer.MAX_VALUE, false, x, y);
                    board[x][y] = 0;
                    if (score >= bestScore) {
                        if (score == bestScore && Math.random() < 0.3) continue;
                        bestScore = score;
                        bestX = x;
                        bestY = y;
                    }
                }
            }
        }

        if (bestX != -1 && bestY != -1) {
            return new int[]{bestX, bestY};
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public void makeAIMove() {
        int[] bestMove = calculateBestMove();
        if (bestMove != null) {
            //table.makeMoveAtField(bestMove[0], bestMove[1]);
        }
    }

    public float analyzeWinningState() {
        return 1;
    }
}
