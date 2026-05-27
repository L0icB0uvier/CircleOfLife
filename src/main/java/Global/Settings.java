package Global;

import Controller.IA.AILevel;

public class Settings {
    PlayerSettings player1Settings, player2Settings;

    /**
     * Joueur commençant la partie. 0 pour joueur 1, 1 pour joueur 1, -1 pour aléatoire.
     */
    int startingPlayer = 0;

    public Settings(){
        player1Settings = new PlayerSettings("Joueur 1");
        player2Settings = new PlayerSettings("Joueur 2");
        player2Settings.isAI = true;
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

    /**
     * Mise à jour du joueur commençant la partie.
     * @param startingPlayer L'index du nouveau premier joueur.
     */
    public void setStartingPlayer(int startingPlayer){
        this.startingPlayer = startingPlayer;
    }

    public PlayerSettings getPlayer1Settings(){
        return player1Settings;
    }

    public PlayerSettings getPlayer2Settings(){
        return player2Settings;
    }

    public int getStartingPlayerSetting(){
        return startingPlayer;
    }
}
