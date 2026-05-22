package View.Adapter;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import View.GraphicalLoadGame;

public class SelectGameMouseAdapter implements MouseListener {
    private GraphicalLoadGame graphicalLoadGame;
    private JPanel gamePanel;
    private String game;


    public SelectGameMouseAdapter(GraphicalLoadGame graphicalLoadGame, JPanel gamePanel, String game) {
        this.graphicalLoadGame = graphicalLoadGame;
        this.gamePanel = gamePanel;
        this.game = game;
    }

    @Override public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        graphicalLoadGame.selectGame(gamePanel, game);
    }

    public void updateGame (String game) {
        this.game = game;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

}
