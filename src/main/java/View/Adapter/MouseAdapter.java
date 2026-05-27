package View.Adapter;

import Global.Configuration;
import Model.Coordinate;
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
        int n = graphicalGame.getGamePanel().getNSelected();
        int m = graphicalGame.getGamePanel().getMSelected();
         
        control.handleClick(m, n);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        graphicalGame.getGamePanel().updateMousePosition(e.getX(), e.getY());
    }
}
