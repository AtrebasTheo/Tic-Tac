package Tic_Tac_Atrebas.jav;

public class GameConfiguration {
    int playerCount;
    int winLength;
    float fieldWidth=100;
    int tableWidth =5;
    int tableHeight =5;

    boolean aiEnabled;
    GameMode gameMode=GameMode.Classic;
    //Modifiers
    boolean iced;
    boolean erasable;
    boolean reinforced;
    GameStyle gameStyle=GameStyle.Classic_Paper;
    ButtonColour buttonColour=ButtonColour.Blue;
    public GameConfiguration copy()
    {
        GameConfiguration copy= new GameConfiguration();
        copy.playerCount = playerCount;
        copy.winLength=winLength;
        copy.fieldWidth=fieldWidth;
        copy.tableWidth=tableWidth;
        copy.tableHeight=tableHeight;
        copy.aiEnabled=aiEnabled;
        copy.gameMode=gameMode;
        copy.iced=iced;
        copy.erasable = erasable;
        copy.reinforced=reinforced;
        copy.gameStyle=gameStyle;
        return copy;
    }


}
enum GameStyle{Classic_Paper,Ice,Purple_Futuristic,Neon_Futuristic,Black_White/*,Retro,Minimalist,Space,Wooden,Metallic*/}
enum ButtonColour{Blue,Brown,Gray,Green,Purple,Red,Yellow}
enum GameMode{Classic}

