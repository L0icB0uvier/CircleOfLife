package View.Adapter;

import Global.Configuration;
import Model.Coordinate;
import Model.MatchUtils;
import View.CustomComponents.GamePanel;
import View.EventCollector;

import java.awt.event.MouseEvent;

public class MouseAdapter extends java.awt.event.MouseAdapter {
    EventCollector control;
    GamePanel gamePanel;

    public MouseAdapter(EventCollector control, GamePanel gamePanel){
        this.control = control;
        this.gamePanel = gamePanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int n = gamePanel.getNSelected();
        int m = gamePanel.getMSelected();

        Configuration.info(String.format("Clic souris aux coordonnées %d:%d - Correspond à la case %s du plateau", e.getX(), e.getY(), new Coordinate(n, m)));
        control.handleClick(m, n);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gamePanel.updateMousePosition(e.getX(), e.getY());
    }
}
