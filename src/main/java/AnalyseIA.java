import Controller.Controller;

import Model.Game;
import View.AnalyseIAView;
import View.EventCollector;

public class AnalyseIA {

    public static void main(String[] args) {
        Game game = new Game();
        EventCollector control = new Controller(game);
        new AnalyseIAView(game, control, args);
    }

}
