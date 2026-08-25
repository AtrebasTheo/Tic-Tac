package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Arrays;

@SuppressWarnings("ALL")
public class ClassicFieldTable extends FieldTable{
     final int[][] fields;
     GameConfiguration configuration;
     private final GameModel model;
    public ClassicFieldTable(GameConfiguration config) {
         // Instantiate a model that owns the board and rules
         this.model = new ClassicGameModel(config);
         this.fields = ((ClassicGameModel)model).fields; // share the same backing array for rendering
         setSize(config.tableWidth*config.fieldWidth, config.tableHeight*config.fieldWidth);
         currentPlayer = model.currentPlayer; // Start with player 1 from model
         this.fieldWidth = config.fieldWidth; // Set the field width
         drawer= new PaperTableDrawer();
         winLength = ((ClassicGameModel)model).winLength;
         configuration=config;
     }


    public void setCenter(float centerx, float centery) {
        setPosition(centerx-getWidth()/2, centery-getHeight()/2);
    }
    public float getCenterX()
    {return getX() + getWidth() / 2;}
    public float getCenterY()
    {return getY() + getHeight() / 2;}

    public boolean makeMove(float xt, float yt) {
        int x=(int) (  (xt- getX()) / fieldWidth);
        int y=(int) ((yt- getY()) / fieldWidth);
        if(hoverX==x&&hoverY==y) { // If the hovered field was just played, remove the hover effect
            hoverX=-1; hoverY=-1;}
        return makeMoveAtField(x,y);
    }

    public boolean makeMoveAtField(int x, int y) {
        // Delegate move application to the model. Keep classic FieldTable currentPlayer in sync with model.
        model.currentPlayer = this.currentPlayer;
        boolean applied = model.makeMoveAtField(x, y);
        if (!applied) {
            return false;
        }
        // synchronize state
        refreshAiBoard();
        this.gameOver = model.gameOver;
        this.winner = model.winner;
        this.currentPlayer = model.currentPlayer;
        if(hoverX==x&&hoverY==y) { // If the hovered field was just played, remove the hover effect
            hoverX=-1; hoverY=-1;}
        return true;
    }

    public synchronized void refreshAiBoard() {
        model.refreshAiBoard();
    }

    public int[][] getAiBoard() {
        return model.getAiBoard();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.WHITE);

        //super.draw(batch, parentAlpha);
        drawer.drawClassicTable(getX(),getY(),fields, fieldWidth,hoverX,hoverY);
        //drawer.drawClassicTable(getX(),getY(),fields,fieldwidth,hoverX,hoverY);
        batch.setColor(Color.WHITE);

    }

    //deprecated
    boolean checkllWin(int player, int lastmoveX, int lastmoveY) {

        int sizeX = fields.length;
        int sizeY = fields[0].length;
        // Zeilen
        for (int of = 0; of < sizeX-winLength+1; of++) {
            boolean rowWin = true;
            for (int ix = of; ix < winLength+of; ix++) {
                if (fields[ix][lastmoveY] != player) rowWin = false;
            }
            if(rowWin) return true;
        }
        // Spalten
        for (int of = 0; of < sizeY-winLength+1; of++) {
            boolean colWin = true;
            for (int iy = of; iy < winLength+of; iy++) {
                if (fields[lastmoveX][iy] != player) colWin = false;
            }
            if(colWin) return true;
        }

        // Diagonale ↘ (links oben nach rechts unten)
        int startOffset = Math.min(lastmoveX,Math.min( lastmoveY, winLength-1));
        int startX = lastmoveX - startOffset;
        int startY = lastmoveY - startOffset;
        int endX = startX + winLength - 1;
        int endY = startY + winLength - 1;
        if (startX >= 0 && startY >= 0 && endX < sizeX && endY < sizeY) {
            boolean diagWin1 = true;
            for (int i = 0; i < winLength; i++) {
                if (fields[startX + i][startY + i] != player) diagWin1 = false;
            }
            if (diagWin1) return true;
        }
        // Diagonale ↙ (rechts oben nach links unten)
        startOffset = Math.min(sizeX - 1 - lastmoveX,Math.min( lastmoveY, winLength-1));
        startX = lastmoveX + startOffset;
        startY = lastmoveY - startOffset;
        endX = startX - winLength + 1;
        endY = startY + winLength - 1;
        if (startX < sizeX && startY >= 0 && endX >= 0 && endY < sizeY) {
            boolean diagWin2 = true;
            for (int i = 0; i < winLength; i++) {
                if (fields[startX - i][startY + i] != player) diagWin2 = false;
            }
            if (diagWin2) return true;
        }
        return false;
    }

     boolean checkWin(int player, int lastmoveX, int lastmoveY) {
        return model.checkWin(player, lastmoveX, lastmoveY);
    }

    public boolean checkWin(int[][] board, int player, int lastmoveX, int lastmoveY) {
        return model.checkWin(board, player, lastmoveX, lastmoveY);
    }



    public boolean empty(int[][] board)
    {
       return model.empty(board);
    }



    public boolean full(int[][] board)
    {
       return model.full(board);
    }
    public void reset() {
        model.reset();
        refreshAiBoard();
        currentPlayer = model.currentPlayer;
        gameOver = model.gameOver;
    }
    void mouseMoved(float screenX, float screenY)
    {
        int x = (int) Math.floor((screenX - getX()) / fieldWidth);
        int y = (int) Math.floor((screenY - getY()) / fieldWidth);
        if (x >= 0 && x < fields.length && y >= 0 && y < fields[0].length && fields[x][y] == 0 && !gameOver) {
            hoverX = x;
            hoverY = y;
        } else {
            hoverX = -1;
            hoverY = -1;
        }

    }
    public void drawHighlight(int x, int y, ShapeRenderer shape) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.setColor(0.2f, 0.6f, 1f, 0.35f); // halbtransparentes Blau
        float px = getX() + x * fieldWidth;
        float py = getY() + y * fieldWidth;
        shape.rect(px, py, fieldWidth, fieldWidth);
    }
    public void setFieldWidth(float fieldwidth) {
        this.fieldWidth = fieldwidth;
        // Nach Änderung der Feldgröße muss auch die Position neu gesetzt werden!
        setCenter(getX() + (fieldwidth * fields.length) / 2f, getY() + (fieldwidth * fields[0].length) / 2f);
        setSize(fields.length*fieldwidth, fields[0].length *fieldwidth);
    }
}
