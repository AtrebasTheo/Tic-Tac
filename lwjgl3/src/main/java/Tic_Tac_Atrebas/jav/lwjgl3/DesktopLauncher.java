package Tic_Tac_Atrebas.jav.lwjgl3;

import Tic_Tac_Atrebas.jav.Main;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("TicTacToe");
        config.setWindowedMode(300, 300);
        new Lwjgl3Application(new Main(), config);
    }
}

