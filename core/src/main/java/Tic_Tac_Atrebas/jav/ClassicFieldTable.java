package Tic_Tac_Atrebas.jav;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Arrays;

@SuppressWarnings("ALL")
public class ClassicFieldTable extends FieldTable{
     final int[][] fields;
     GameConfiguration configuration;
    public ClassicFieldTable(int width, int height,float fieldwidth, GameConfiguration config) {
        fields = new int[width][height]; // 0=empty, 1=X, 2=O
        currentPlayer = 1; // Start with player 1
        this.fieldwidth = fieldwidth; // Set the field width
        drawer= new PaperTableDrawer();
        winLength= (int) Math.ceil(Math.max(width,height)/2f)+1;
        aiPlayer= new ClassicAI(this,config.playercount);
        configuration=config;
    }
    public ClassicFieldTable(int size,GameConfiguration config) {
        this(size, size,100,config); // Default field
    }
    public ClassicFieldTable(int size,float fieldwidth,GameConfiguration config) {
        this(size, size,fieldwidth, config); // Default to a 3x3 field
    }

    public void setCenter(float centerx, float centery) {
        this.xco = centerx- (fieldwidth * fields.length) / 2f;
        this.yco = centery- (fieldwidth * fields[0].length) / 2f;
    }
    public boolean makeMove(float xt, float yt) {
        int x=(int) (Math.floor(  (xt- xco) / fieldwidth));
        int y=(int) (Math.floor((yt- yco) / fieldwidth));
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
        gameOver=checkWin(currentPlayer);
        if(gameOver){
            winner=currentPlayer;
        }
        else{
            if(full()){
                gameOver=true;//Draw
                winner=0;}
        }
        //winningstate
        if(!gameOver)
        {currentPlayer = (currentPlayer == 1) ? 2 : 1; // Switch player
            if(configuration.aiEnabled&&currentPlayer==aiPlayer.aiNumber)
            {
                aiPlayer.makeAIMove();
            }
        }
        return true;
    }

    public void render(ShapeRenderer shape) {

        shape.end();
        drawer.drawClassicTable(xco,yco,fields,fieldwidth,hoverX,hoverY);
    }



    boolean checkWin(int player) {
        int sizeX = fields.length;
        int sizeY = fields[0].length;
        // Zeilen und Spalten
        for (int iy = 0; iy < sizeY; iy++) {

            for (int of = 0; of < sizeX-winLength+1; of++) { // of = offset, if the field is larger than the winLength
                boolean rowWin = true;
                for (int ix = of; ix < winLength+of; ix++) {
                    if (fields[ix][iy] != player) rowWin = false;
                }
                if(rowWin) return true;

            }
        }

        for (int ix = 0; ix < sizeX; ix++) {

            for (int of = 0; of < sizeY-winLength+1; of++) {
                boolean colWin = true;
                for (int iy = of; iy < winLength+of; iy++) {
                    if (fields[ix][iy] != player) colWin = false;
                }
                if(colWin) return true;

            }
        }

        for (int ix = 0; ix < sizeX-winLength+1; ix++) {//left to right diagonal
            for (int iy = 0; iy < sizeY-winLength+1; iy++) {
                boolean diagwin1 = true;
                for (int i = 0; i < winLength; i++) {
                    if (fields[ix+i][iy+i] != player) diagwin1 = false;
                }
                if(diagwin1) return true;
            }
        }
        for (int ix = 0; ix < sizeX-winLength+1; ix++) {//left to right diagonal
            for (int iy = 0; iy < sizeY-winLength+1; iy++) {
                boolean diagwin2 = true;
                for (int i = 0; i < winLength; i++) {
                    if (fields[sizeX-1-ix-i][iy+i] != player) diagwin2 = false;
                }
                if(diagwin2) return true;
            }
        }


        return false;
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
        int x = (int) Math.floor((screenX - xco) / fieldwidth);
        int y = (int) Math.floor((screenY - yco) / fieldwidth);
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
        float px = xco + x * fieldwidth;
        float py = yco + y * fieldwidth;
        shape.rect(px, py, fieldwidth, fieldwidth);
    }
    public void setFieldWidth(float fieldwidth) {
        this.fieldwidth = fieldwidth;
        // Nach Änderung der Feldgröße muss auch die Position neu gesetzt werden!
        setCenter(xco + (fieldwidth * fields.length) / 2f, yco + (fieldwidth * fields[0].length) / 2f);
    }
}
