package Tic_Tac_Atrebas.jav;

public class GameConfiguration {
    int playerCount;
    int winLength;
    float fieldWidth = 100f;
    int tableWidth = 5;
    int tableHeight = 5;

    int aiMinimaxDepth = 6;
    float backgroundRed = 0.1f;
    float backgroundGreen = 0.3f;
    float backgroundBlue = 0.8f;
    float buttonWidth = 400f;
    float buttonHeight = 100f;
    float panelWidth = 300f;
    float playerIconSize = 100f;

    boolean aiEnabled;
    GameMode gameMode = GameMode.Classic;
    //Modifiers
    boolean iced;
    boolean erasable;
    boolean reinforced;
    GameStyle gameStyle = GameStyle.Classic_Paper;
    ButtonColour buttonColour = ButtonColour.Blue;

    public GameConfiguration copy() {
        GameConfiguration copy = new GameConfiguration();
        copy.playerCount = playerCount;
        copy.winLength = winLength;
        copy.fieldWidth = fieldWidth;
        copy.tableWidth = tableWidth;
        copy.tableHeight = tableHeight;
        copy.aiMinimaxDepth = aiMinimaxDepth;
        copy.backgroundRed = backgroundRed;
        copy.backgroundGreen = backgroundGreen;
        copy.backgroundBlue = backgroundBlue;
        copy.buttonWidth = buttonWidth;
        copy.buttonHeight = buttonHeight;
        copy.panelWidth = panelWidth;
        copy.playerIconSize = playerIconSize;
        copy.aiEnabled = aiEnabled;
        copy.gameMode = gameMode;
        copy.iced = iced;
        copy.erasable = erasable;
        copy.reinforced = reinforced;
        copy.gameStyle =gameStyle;
        copy.buttonColour = buttonColour;
        return copy;
    }
}
enum GameStyle{Classic_Paper,Ice,Purple_Futuristic,Neon_Futuristic,Black_White/*,Retro,Minimalist,Space,Wooden,Metallic*/}
enum ButtonColour{Blue,Brown,Gray,Green,Purple,Red,Yellow}
enum GameMode{Classic}

