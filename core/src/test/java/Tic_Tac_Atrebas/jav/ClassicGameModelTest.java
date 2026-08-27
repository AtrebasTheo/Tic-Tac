package Tic_Tac_Atrebas.jav;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassicGameModelTest {

    @Test
    public void horizontalWin() {
        int[][] board = new int[3][3];
        ClassicGameModel model = new ClassicGameModel(board, 3);

        // player 1 at (0,0)
        assertTrue(model.makeMoveAtField(0,0));
        // player 2 at (0,1)
        assertTrue(model.makeMoveAtField(0,1));
        // player 1 at (1,0)
        assertTrue(model.makeMoveAtField(1,0));
        // player 2 at (1,1)
        assertTrue(model.makeMoveAtField(1,1));
        // player 1 at (2,0) -> win
        assertTrue(model.makeMoveAtField(2,0));

        assertTrue(model.gameOver);
        assertEquals(1, model.winner);
    }

    @Test
    public void diagonalWin() {
        int[][] board = new int[3][3];
        ClassicGameModel model = new ClassicGameModel(board, 3);

        // Sequence to produce diagonal for player1
        assertTrue(model.makeMoveAtField(0,0)); // p1
        assertTrue(model.makeMoveAtField(1,0)); // p2
        assertTrue(model.makeMoveAtField(1,1)); // p1
        assertTrue(model.makeMoveAtField(2,0)); // p2
        assertTrue(model.makeMoveAtField(2,2)); // p1 -> diagonal (0,0),(1,1),(2,2)

        assertTrue(model.gameOver);
        assertEquals(1, model.winner);
    }

    @Test
    public void fullBoardDraw() {
        int[][] board = new int[3][3];
        ClassicGameModel model = new ClassicGameModel(board, 3);

        // Fill board without any winning line
        assertTrue(model.makeMoveAtField(0,0)); // p1
        assertTrue(model.makeMoveAtField(0,1)); // p2
        assertTrue(model.makeMoveAtField(0,2)); // p1

        assertTrue(model.makeMoveAtField(1,1)); // p2
        assertTrue(model.makeMoveAtField(1,0)); // p1
        assertTrue(model.makeMoveAtField(1,2)); // p2

        assertTrue(model.makeMoveAtField(2,1)); // p1
        assertTrue(model.makeMoveAtField(2,0)); // p2
        assertTrue(model.makeMoveAtField(2,2)); // p1

        assertTrue(model.gameOver);
        assertEquals(0, model.winner);
    }
}
