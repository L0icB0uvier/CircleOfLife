import Controller.Controller;
import Model.Game;
import View.EventCollector;
import View.GraphicalUserInterface;

public class App {
    public static void main(String[] args) {
        Game game = new Game();
        EventCollector control = new Controller(game);
        GraphicalUserInterface.start(game, control);
    }
}