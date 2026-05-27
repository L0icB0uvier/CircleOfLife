package Controller.Animation;

import Controller.Controller;

public abstract class Animation {
    int updateRate;
    int count;
    Controller control;

    public Animation(int updateRate, Controller c) {
        this.updateRate = updateRate;
        count = updateRate;
        control = c;
    }

    public void tictac() {
        count--;
        if (count <= 0) {
            count = updateRate;
            update();
        }
    }

    public abstract void endAnimation();

    public abstract void update();

    public boolean isOver() {
        return false;
    }
}
