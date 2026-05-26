package Model;

import Patterns.Command;

import java.util.HashSet;
import java.util.Set;

public class Move implements Command {
    Match match;
    private final int line, column;
    byte[][] previousState;
    Set<Critter> critters;
    PlayerData[] previousScore;
    Set<Critter> previouslyEatenCritters;

    public Move(Match match, int l, int c){
        this.match = match;
        this.line = l;
        this.column = c;
        this.previousState = match.getBoardState();
        this.critters = match.getCritters();
        this.previousScore = match.getPlayerData();
        this.previouslyEatenCritters = new HashSet<>();
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public void setPreviouslyEatenCritters(Set<Critter> eatenCritters){
        this.previouslyEatenCritters = eatenCritters;
    }

    @Override
    public void execute() {
        match.playMove(line, column);
    }

    @Override
    public void desexecute() {
        match.restoreState(previousState, critters, previousScore, new HashSet<>(previouslyEatenCritters));
    }
}
