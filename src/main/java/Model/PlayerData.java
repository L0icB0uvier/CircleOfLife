package Model;

public class PlayerData {
    int score;

    public PlayerData(){
        score = 0;
    }

    public PlayerData(int score){
        this.score = score;
    }

    public void incrementScore(){
        score++;
    }

    public int getScore() {
        return score;
    }
}
