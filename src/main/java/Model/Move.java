package Model;

import Patterns.Command;

public class Move implements Command {
    Match match;
    int line, column;
    int[] previousState;

    public Move(Match match, int l, int c){
        this.match = match;
        line = l;
        column = c;
    }

    public Move(Match match, int l, int c, int[] previousState){
        this.match = match;
        this.line = l;
        this.column = c;
        this.previousState = previousState;
    }

    /**
     *
     * @return
     */
    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public void execute() {
        // sauvegarder l'état du plateau;
        match.playMove(line, column);
    }

    @Override
    public void desexecute() {
        match.restoreState(previousState);
    }
}
