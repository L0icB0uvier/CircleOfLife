package Controller.Animation;

import Controller.Controller;
import Global.Configuration;

public class ImpossibleMoveAnimation extends Animation {
    float progress;
    float animationSpeed;
    int l, c;
    String id;

    public ImpossibleMoveAnimation(Controller controller, float animationSpeed, int l, int c, String id) {
        super(1, controller);
        this.animationSpeed = animationSpeed;
        this.l = l;
        this.c = c;
        this.id = id;
        progress = 0;
        Configuration.info("Animation coup impossible créée");
    }

    @Override
    public void endAnimation() {
        progress = 1;
        control.animateImpossibleMoveAnimation(id, l, c, progress);
    }

    @Override
    public void update() {
        if(!isOver()){
            progress += animationSpeed;
            if(progress > 1)
                progress = 1;

            control.animateImpossibleMoveAnimation(id, l, c, progress);
        }
    }

    @Override
    public boolean isOver() {
        return progress >= 1;
    }
}
