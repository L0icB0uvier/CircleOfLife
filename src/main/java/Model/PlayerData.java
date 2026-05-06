package Model;

public class PlayerData {
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
}
