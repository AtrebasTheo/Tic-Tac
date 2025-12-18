package Tic_Tac_Atrebas.jav;

public class GameConfiguration {
    int playercount;
    int winLength;
    boolean aiEnabled;
    GameMode gameMode=GameMode.Classic;
    //Modifiers
    boolean iced;
    boolean eraseable;
    boolean reinforced;
    GameStyle gameStyle=GameStyle.Classic_Paper;
    public GameConfiguration copy()
    {
        GameConfiguration copy= new GameConfiguration();
        copy.playercount=playercount;
        copy.winLength=winLength;
        copy.aiEnabled=aiEnabled;
        copy.gameMode=gameMode;
        copy.iced=iced;
        copy.eraseable=eraseable;
        copy.reinforced=reinforced;
        copy.gameStyle=gameStyle;
        return copy;
    }
}
enum GameStyle{Classic_Paper,Ice,Purple_Futuristic,Neon_Futuristic,Black_White/*,Retro,Minimalist,Space,Wooden,Metallic*/}
enum GameMode{Classic}

