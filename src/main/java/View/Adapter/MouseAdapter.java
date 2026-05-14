package View.Adapter;

import Controller.Player;
import Global.Configuration;
import Model.Coordinate;
import View.EventCollector;
import View.GraphicalGame;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MouseAdapter extends java.awt.event.MouseAdapter {
    EventCollector control;
    //GraphicalGame graphialGame;
    GraphicalGame graphicalGame;

    public MouseAdapter(EventCollector control, GraphicalGame graphicalGame){
        this.control = control;
        this.graphicalGame = graphicalGame;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int n = graphicalGame.getGamePanel().getnSelected();
        int m = graphicalGame.getGamePanel().getmSelected();
        Configuration.info(String.format("Clic souris aux coordonnées %d:%d - Correspond à la case %s du plateau", e.getX(), e.getY(), new Coordinate(n, m)));
        int action = control.handleClick(m, n);
        if(action != Player.ACTION_NULL) {
            graphicalGame.gameHistory.addAction(new Coordinate(n, m), (action == Player.ACTION_EAT));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        graphicalGame.getGamePanel().updateMousePosition(e.getX(), e.getY());
    }
}
