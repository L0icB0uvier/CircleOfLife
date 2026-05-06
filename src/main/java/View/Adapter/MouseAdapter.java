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
        Configuration.info(String.format("Clic souris sur case %d:%d", m, n));
        control.handleClic(m, n);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        graphicalGame.getGamePanel().updateMousePosition(e.getX(), e.getY());
    }
}
