package Patterns;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    List<Observer> observers;

    public Observable() {
        observers = new ArrayList<>();
    }

    /**
     * Ajoute un observateur.
     * @param observer Observateur à ajouter.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Retire un observateur.
     * @param observer Observateur à retirer.
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Appel update sur tous les observateurs enregistrés.
     */
    public void update() {
        var it = observers.iterator();

        while (it.hasNext()) {
            Observer o = it.next();
            o.update();
        }
    }
}
