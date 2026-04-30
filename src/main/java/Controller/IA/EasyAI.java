package Controller.IA;

import Global.Configuration;
import Model.Match;
import Model.Move;

import java.util.Random;

public class EasyAI extends AI{

    public EasyAI(Match match) {
        super(match);
        aiLevel = AILevel.EASY;
    }

    @Override
    public Move findMove() {
       return null;
    }
}
