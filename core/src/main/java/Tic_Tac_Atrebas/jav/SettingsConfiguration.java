package Tic_Tac_Atrebas.jav;

public class SettingsConfiguration {

    GameStyle standardGameStyle=GameStyle.Classic_Paper;
    SettingsStyle settingsStyle= SettingsStyle.Blue;
    public SettingsConfiguration copy()
    {
        SettingsConfiguration copy= new SettingsConfiguration();

        copy.standardGameStyle=standardGameStyle;
        copy.settingsStyle=settingsStyle;
        return copy;
    }
}


enum SettingsStyle{Blue, Dark_Mode}
