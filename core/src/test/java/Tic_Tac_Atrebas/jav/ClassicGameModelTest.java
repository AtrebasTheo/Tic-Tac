package Tic_Tac_Atrebas.jav;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

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

    @Test
    public void aiMovePlacesMarkerOnEmptyBoard() throws Exception {
        GameConfiguration config = new GameConfiguration();
        config.tableWidth = 3;
        config.tableHeight = 3;
        config.fieldWidth = 100;
        ClassicGameModel model = new ClassicGameModel(config);

        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);

        ClassicFieldTable table = (ClassicFieldTable) unsafe.allocateInstance(ClassicFieldTable.class);

        Field modelField = ClassicFieldTable.class.getDeclaredField("model");
        modelField.setAccessible(true);
        modelField.set(table, model);

        Field fieldsField = ClassicFieldTable.class.getDeclaredField("fields");
        fieldsField.setAccessible(true);
        fieldsField.set(table, model.fields);

        Field currentPlayerField = FieldTable.class.getDeclaredField("currentPlayer");
        currentPlayerField.setAccessible(true);
        currentPlayerField.setInt(table, 1);

        ClassicAI ai = new ClassicAI(table, 1);
        ai.makeAIMove();

        int filledCells = 0;
        for (int x = 0; x < table.fields.length; x++) {
            for (int y = 0; y < table.fields[x].length; y++) {
                if (table.fields[x][y] != 0) {
                    filledCells++;
                    assertEquals(1, table.fields[x][y]);
                }
            }
        }

        assertEquals(1, filledCells);
    }
}
