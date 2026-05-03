package Model;

public class PlayerData {
    int score;

    public PlayerData(){
        score = 0;
    }

    public PlayerData(int score){
        this.score = score;
    }

    /**
     * Ajoute 1 au score du joueur.
     */
    public void incrementScore(){
        score++;
    }

    /**
     * Récupère le score du joueur.
     * @return Le score du joueur.
     */
    public int getScore() {
        return score;
    }
}
