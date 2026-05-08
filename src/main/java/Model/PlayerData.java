package Model;

import Global.Configuration;

public class PlayerData implements Cloneable {
    int score;
    int playableTilesNumber;

    public PlayerData(){
        score = 0;
        playableTilesNumber = 61;
    }

    public PlayerData(int score){
        this.score = score;
    }

    public void increaseScore(int increaseAmount){
        score += increaseAmount;
    }

    public int getScore() {
        return score;
    }

    public int getPlayableTilesNumber() {
        return playableTilesNumber;
    }

    @Override
    protected PlayerData clone() {
        
        try {
            return (PlayerData) super.clone();
        } catch (CloneNotSupportedException e) {
            Configuration.error("Internal bug, given PlayerData non cloneable");
        }
        return null;
    }
}
