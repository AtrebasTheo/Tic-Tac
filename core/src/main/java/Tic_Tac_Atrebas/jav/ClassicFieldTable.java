package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

@SuppressWarnings("ALL")
public class ClassicFieldTable extends FieldTable{
     final int[][] fields;
     GameConfiguration configuration;
    public ClassicFieldTable(int width, int height,float fieldwidth, GameConfiguration config) {
        fields = new int[width][height]; // 0=empty, 1=X, 2=O
        setSize(width*fieldwidth, height*fieldwidth);
        currentPlayer = 1; // Start with player 1
        this.fieldwidth = fieldwidth; // Set the field width
        drawer= new PaperTableDrawer();
        winLength= (int) Math.ceil(Math.max(width,height)/2f)+1;
        configuration=config;
    }
    public ClassicFieldTable(int size,GameConfiguration config) {
        this(size, size,100,config); // Default field
    }
    public ClassicFieldTable(int size,float fieldwidth,GameConfiguration config) {
        this(size, size,fieldwidth, config); // Default to a 3x3 field
    }

    public void setCenter(float centerx, float centery) {
        setPosition(centerx-getWidth()/2, centery-getHeight()/2);
    }
    public float getCenterX()
    {return getX() + getWidth() / 2;}
    public float getCenterY()
    {return getY() + getHeight() / 2;}

    public boolean makeMove(float xt, float yt) {
        int x=(int) (  (xt- getX()) / fieldwidth);
        int y=(int) ((yt- getY()) / fieldwidth);
        return makeMoveAtField(x,y);
    }

    public boolean makeMoveAtField(int x, int y) {
        if (x < 0 || x >= fields.length || y < 0 || y >= fields[0].length || fields[x][y] != 0) {
            System.out.println("invalid");
            System.out.println("x: "+x+" y: "+y);
            return false; // Invalid move
        }
        fields[x][y] = currentPlayer;
        if(hoverX==x&&hoverY==y) { // If the hovered field was just played, remove the hover effect
            hoverX=-1; hoverY=-1;}
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
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.WHITE);

        //super.draw(batch, parentAlpha);
        drawer.drawClassicTable(getX(),getY(),fields,fieldwidth,hoverX,hoverY);
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


        if (1 + countDir( player, lastmoveX, lastmoveY,  1,  0) +
            countDir( player, lastmoveX, lastmoveY, -1,  0) >= winLength) return true;

        if (1 + countDir( player, lastmoveX, lastmoveY,  0,  1) +
            countDir( player, lastmoveX, lastmoveY,  0, -1) >= winLength) return true;

        if (1 + countDir( player, lastmoveX, lastmoveY,  1,  1) +
            countDir( player, lastmoveX, lastmoveY, -1, -1) >= winLength) return true;

        if (1 + countDir(player, lastmoveX, lastmoveY, -1,  1) +
            countDir(player, lastmoveX, lastmoveY,  1, -1) >= winLength) return true;

        return false;
    }

    private  int countDir( int player, int lastX, int lastY,
                                int dx, int dy) {
        int sizeX = fields.length;
        int sizeY = fields[0].length;
        int cx = lastX + dx;
        int cy = lastY + dy;
        int c = 0;
        while (cx >= 0 && cy >= 0 && cx < sizeX && cy < sizeY
            && fields[cx][cy] == player) {
            c++;
            cx += dx;
            cy += dy;
        }
        return c;
    }



    public boolean empty()
    {
        for(int i = 0; i < fields.length; i++) {
            for(int j = 0; j < fields[i].length; j++) {
                if(fields[i][j]!=0) return false;
            }
        }
        return true;
    }



    public boolean full()
    {
        for(int i = 0; i < fields.length; i++) {
            for(int j = 0; j < fields[i].length; j++) {
                if(fields[i][j]==0) return false;
            }
        }
        return true;
    }
    public void reset() {
        for(int i = 0; i < fields.length; i++) {
            // Reset all fields to empty
            Arrays.fill(fields[i], 0);
        }
        currentPlayer = 1;
        gameOver=false;
    }
    void mouseMoved(float screenX, float screenY)
    {
        int x = (int) Math.floor((screenX - getX()) / fieldwidth);
        int y = (int) Math.floor((screenY - getY()) / fieldwidth);
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
        float px = getX() + x * fieldwidth;
        float py = getY() + y * fieldwidth;
        shape.rect(px, py, fieldwidth, fieldwidth);
    }
    public void setFieldWidth(float fieldwidth) {
        this.fieldwidth = fieldwidth;
        // Nach Änderung der Feldgröße muss auch die Position neu gesetzt werden!
        setCenter(getX() + (fieldwidth * fields.length) / 2f, getY() + (fieldwidth * fields[0].length) / 2f);
        setSize(fields.length*fieldwidth, fields[0].length *fieldwidth);
    }
}
