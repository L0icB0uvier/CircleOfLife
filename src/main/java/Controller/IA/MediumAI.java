package Controller.IA;

import Global.Configuration;
import Model.Match;
import Model.Move;

import java.util.Random;

public class MediumAI extends AI {

    public MediumAI(Match match) {
        super(match);
        aiLevel = AILevel.MEDIUM;
    }

    /**
     * Generates a valid Move on its current Match.
     * Avoids losing moves if possible, and prioritizes winning moves if possible
     * @return Either : a winning Move; a uniformly random non-losing Move; a losing Move
     */
    @Override
    public Move findMove() {
       return null;
    }
}
