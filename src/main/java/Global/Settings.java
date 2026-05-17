package Global;

import Controller.IA.AILevel;

public class Settings {
    int lines, columns;
    int[] state;
    PlayerSettings player1Settings, player2Settings;

    public Settings(){
        player1Settings = new PlayerSettings("Joueur 1");
        player2Settings = new PlayerSettings("Joueur 2");
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
    
    public void setState(int[] state){
        this.state = state;
    }

    /**
     * Mise à jour des setting du joueur 1.
     * @param aiLevel Difficulté de l'IA. Si null, le joueur est humain.
     * @param name Le nom du joueur
     */
    public void setPlayer1Settings(AILevel aiLevel, String name) {
        if(aiLevel == null) this.player1Settings = new PlayerSettings(name);
        else this.player1Settings = new PlayerSettings(aiLevel, name);
    }

    /**
     * Mise à jour des setting du joueur 2.
     * @param aiLevel Difficulté de l'IA. Si null, le joueur est humain.
     * @param name Le nom du joueur
     */
    public void setPlayer2Settings(AILevel aiLevel, String name) {
        if(aiLevel == null) this.player2Settings = new PlayerSettings(name);
        else this.player2Settings = new PlayerSettings(aiLevel, name);
    }
}
