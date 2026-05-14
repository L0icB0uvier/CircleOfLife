package Model;

import Global.Configuration;

/**
 * Stocke le score du joueur.
 */
public class PlayerData implements Cloneable {
    int score;
    String name;

    public PlayerData(String name){
        this.name = name;
        reset();
    }

    public void increaseScore(int increaseAmount){
        score += increaseAmount;
    }

    public int getScore() {
        return score;
    }

    public String getName() { return name; }

    public void reset(){
        score = 0;
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
