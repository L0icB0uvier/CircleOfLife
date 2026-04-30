package View;

import Controller.IA.AILevel;
import Global.Configuration;

public class Settings {
    int lines, columns;
    int[] state;
    PlayerSettings player1Settings, player2Settings;

    public Settings(){
        lines = Configuration.readInt("DefaultWaffleLines");
        columns = Configuration.readInt("DefaultWaffleColumns");
        player1Settings = new PlayerSettings();
        player2Settings = new PlayerSettings();
        player2Settings.isAI = true;
    }

    public int getNbLines(){
        return lines;
    }
    public int getNbCol(){
        return columns;
    }

    public PlayerSettings getPlayer1Settings(){
        return player1Settings;
    }

    public PlayerSettings getPlayer2Settings(){
        return player2Settings;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setState(int[] state){
        this.state = state;
    }

    public void setPlayer1Settings(AILevel aiLevel) {
        if(aiLevel == null) this.player1Settings = new PlayerSettings();
        else this.player1Settings = new PlayerSettings(aiLevel);
    }

    public void setPlayer2Settings(AILevel aiLevel) {
        if(aiLevel == null) this.player2Settings = new PlayerSettings();
        else this.player2Settings = new PlayerSettings(aiLevel);
    }
}
