package Controller.Animation;

import Controller.Controller;
import Global.Configuration;
import Model.Coordinate;

import java.util.Set;

public class ScoreAnimation extends Animation {
    int player;
    int scoreGained;
    Set<Coordinate> startLocation;
    float progress;
    float animationSpeed;

    public ScoreAnimation(float animationSpeed, Set<Coordinate> eatenGroupOrigin, int scoreGained, int player, Controller c) {
        super(1, c);
        this.player = player;
        this.scoreGained = scoreGained;
        this.startLocation = eatenGroupOrigin;
        this.animationSpeed = animationSpeed;
        progress = 0;
        Configuration.info("Score animation créée");
    }

    @Override
    public void endAnimation() {
        progress = 1;
        control.animateScore(startLocation, scoreGained, player, progress);
    }

    @Override
    public void update() {
        if(!isOver()){
            progress += animationSpeed;
            if(progress > 1)
                progress = 1;

            control.animateScore(startLocation, scoreGained, player, progress);
        }
    }

    @Override
    public boolean isOver() {
        return progress >= 1;
    }
}
