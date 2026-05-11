package Model;

import Global.Configuration;
import Patterns.Command;

import java.util.Set;


public class Move implements Command {
    Match match;
    private final int line, column;
    byte[][] previousState;
    Set<Critter> critters;
    PlayerData[] previousScore;

    public Move(Match match, int l, int c){
        this.match = match;
        this.line = l;
        this.column = c;
        this.previousState = match.getBoardState();
        this.critters = match.getCritters();
        this.previousScore=match.getPlayerData();
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public void execute() {
        match.playMove(line, column);
    }

    @Override
    public void desexecute() {
        match.restoreState(previousState, critters, previousScore);
    }
}
