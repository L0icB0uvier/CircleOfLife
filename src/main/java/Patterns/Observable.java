package Patterns;

import Model.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Observable {
    List<Observer> updateObserver;
    List<ScoreEventObserver> scoreEventObserver;

    public Observable() {
        updateObserver = new ArrayList<>();
        scoreEventObserver = new ArrayList<>();
    }

    /**
     * Ajoute un observateur.
     * @param observer Observateur à ajouter.
     */
    public void addUpdateObserver(Observer observer) {
        updateObserver.add(observer);
    }

    public void addUpdateScoreObserver(ScoreEventObserver scoreEventObserver){
        this.scoreEventObserver.add(scoreEventObserver);
    }

    /**
     * Retire un observateur.
     * @param observer Observateur à retirer.
     */
    public void removeUpdateObserver(Observer observer) {
        updateObserver.remove(observer);
    }

    /**
     * Appel update sur tous les observateurs enregistrés.
     */
    public void update() {
        for (Observer o : updateObserver) {
            o.update();
        }
    }

    public void updateScore(Map<Set<Coordinate>, Integer> crittersEaten, int player){
        for (ScoreEventObserver o : scoreEventObserver) {
            o.onScoreUpdated(crittersEaten, player);
        }
    }
}
