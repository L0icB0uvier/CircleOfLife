package Controller.IA;

import Global.Configuration;
import Model.Match;
import Model.Move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class HardAI extends AI {
    public HardAI(Match match) {
        super(match);
        aiLevel = AILevel.HARD;
    }

    /**
     * Uses a AND/OR tree to generate a valid Move on its current Match.
     * @return Either : a move that locks the AI in a win; a uniformly random valid move
     */
    @Override
    public Move findMove() {
       return null;
    }
}
