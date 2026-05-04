package View.Adapter;

import Global.Configuration;
import View.EventCollector;
import View.GraphicalGame;

import java.awt.event.MouseEvent;

public class MouseAdapter extends java.awt.event.MouseAdapter {
    EventCollector control;
    GraphicalGame graphicalGame;

    public MouseAdapter(EventCollector control, GraphicalGame graphicalGame){
        this.control = control;
        this.graphicalGame = graphicalGame;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Configuration.info("Click at " + e.getX() + ":" + e.getY());
        int n = graphicalGame.getGamePanel().xToN(e.getX(),e.getY());
        int m = graphicalGame.getGamePanel().yToM(e.getY());
        //control.handleClic(line, col);
        System.out.println("index n: " + n);
        System.out.println("index m: " + m);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        graphicalGame.getGamePanel().updateMousePosition(e.getX(), e.getY());
    }
}
