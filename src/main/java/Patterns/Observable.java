package Patterns;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    List<Observateur> observateurs;

    public Observable() {
        observateurs = new ArrayList<>();
    }

    public void ajouteObservateur(Observateur observateur) {
        observateurs.add(observateur);
    }

    public void retireObservateur(Observateur observateur) {
        observateurs.remove(observateur);
    }

    public void update() {
        var it = observateurs.iterator();

        while (it.hasNext()) {
            Observateur o = it.next();
            o.update();
        }
    }
}
