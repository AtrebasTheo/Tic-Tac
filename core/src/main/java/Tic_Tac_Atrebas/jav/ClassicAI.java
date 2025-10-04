package Tic_Tac_Atrebas.jav;
abstract class AIPlayer{
    public Difficulty difficulty = Difficulty.HARD;
    public int aiNumber; // 1 for X, 2 for O
    public abstract void makeAIMove();
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

    // Bewertungsfunktion: +10 für KI-Gewinn, -10 für Mensch-Gewinn, 0 für Unentschieden/kein Ende
    private int evaluateBoard() {
        if (table.checkWin(aiNumber)) return 10;
        if (table.checkWin(humanPlayer)) return -10;
        if (table.full()) return 0;
        return 0;
    }

    // Minimax mit Alpha-Beta-Pruning
    private int minimax(int depth, int alpha, int beta, boolean maximizingPlayer) {
        int score = evaluateBoard();
        if (Math.abs(score) == 10 || table.full() || depth == 0) {
            return score;
        }
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int x = 0; x < table.fields.length; x++) {
                for (int y = 0; y < table.fields[0].length; y++) {
                    if (table.fields[x][y] == 0) {
                        table.fields[x][y] = aiNumber;
                        if (table.checkWin(aiNumber)) {
                            table.fields[x][y] = 0;
                            return 10;
                        }
                        int eval = minimax(depth - 1, alpha, beta, false);
                        table.fields[x][y] = 0;
                        maxEval = Math.max(maxEval, eval);
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha) break;
                    }
                }
            }
            return maxEval;
        } else {// Minimizing opponent player
            int minEval = Integer.MAX_VALUE;
            for (int x = 0; x < table.fields.length; x++) {
                for (int y = 0; y < table.fields[0].length; y++) {
                    if (table.fields[x][y] == 0) {
                        table.fields[x][y] = humanPlayer;
                        if (table.checkWin(humanPlayer)) {
                            table.fields[x][y] = 0;
                            return -10;
                        }
                        int eval = minimax(depth - 1, alpha, beta, true);
                        table.fields[x][y] = 0;
                        minEval = Math.min(minEval, eval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) break;
                    }
                }
            }
            return minEval;
        }
    }

    // Führt den besten Zug aus
    public void makeAIMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestX = -1, bestY = -1;
        for (int x = 0; x < table.fields.length; x++) {
            for (int y = 0; y < table.fields[0].length; y++) {
                if (table.fields[x][y] == 0) {
                    table.fields[x][y] = aiNumber;
                    int score = minimax(9, Integer.MIN_VALUE, Integer.MAX_VALUE, false); // Tiefe 6
                    table.fields[x][y] = 0;
                    if (score > bestScore) {
                        bestScore = score;
                        bestX = x;
                        bestY = y;
                    }
                }
            }
        }
        if (bestX != -1 && bestY != -1) {
            table.makeMoveAtField(bestX, bestY);
        }
        else{

            System.out.println("AI konnte keinen Zug finden!");
            for (int i = 0; i < table.fields.length; i++) {
                for (int j = 0; j < table.fields[0].length; j++) {
                    if (table.fields[i][j] == 0) {
                        table.makeMoveAtField(i, j);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Bewertet den aktuellen Spielstand: Werte von -1 (Mensch gewinnt) bis 1 (KI gewinnt), 0 = ausgeglichen
     * Berücksichtigt Reihen, Spalten und Diagonalen
     */
    public float analyzeWinningState() {
        return 1; //minimax(6, Integer.MIN_VALUE, Integer.MAX_VALUE, false); // Tiefe 6
    }

}
