package Model;

import Global.Configuration;
import Patterns.Command;

public class Move implements Command {
    Match match;
    private final int line, column;
    byte[][] previousState;

    public Move(Match match, int l, int c){
        this.match = match;
        this.line = l;
        this.column = c;
        this.previousState = match.getBoardState();
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public void execute() {
        // sauvegarder l'état du plateau;
        Configuration.info(String.format("Exécution du move %d:%d", column, line));
        match.playMove(line, column);
    }

    @Override
    public void desexecute() {
        match.restoreState(previousState);
    }
}
