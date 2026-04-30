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
        int line = graphicalGame.getGamePanel().yToNbLine(e.getY());
        int col = graphicalGame.getGamePanel().xToNbColumn(e.getX());
        control.handleClic(line, col);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        graphicalGame.getGamePanel().updateMousePosition(e.getX(), e.getY());
    }
}
