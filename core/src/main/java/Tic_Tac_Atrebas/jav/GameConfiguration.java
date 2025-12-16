package Tic_Tac_Atrebas.jav;

public class GameConfiguration {
    int playercount;
    int winLength;
    boolean aiEnabled;
    GameMode gameMode;
    //Modifiers
    boolean iced;
    boolean eraseable;
    boolean reinforced;
    GameStyle gameStyle=GameStyle.Paper_Classic;
}
enum GameStyle{Paper_Classic,Ice,Putple_Futuristic,Neon_Futuristic,Black_White/*,Retro,Minimalist,Space,Wooden,Metallic*/}
enum GameMode{Classic}
