package View.CustomComponents;

import Model.Coordinate;
import Model.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameHistory extends JScrollPane {
    ArrayList<CustomLabel> customLabelsList;
    Game game;
    JPanel contentPanel ;


    public GameHistory(Game g){
        game = g;

        contentPanel = new JPanel();
        BoxLayout layout = new BoxLayout(contentPanel, BoxLayout.Y_AXIS);
        contentPanel.setLayout(layout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        this.setViewportView(contentPanel);
        this.setWheelScrollingEnabled(true);
    }

    public void addAction(Coordinate coordinate, boolean eat){
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        CustomLabel action = new CustomLabel(game.getCurrentPlayerIndex(), "a posé une pièce en "+coordinate.historyFormat());
        action.setPreferredSize(new Dimension(100, 50));
        action.setMaximumSize(new Dimension(10000, 50));
        contentPanel.add(action);
        SwingUtilities.invokeLater(()->{
            this.getVerticalScrollBar().setValue(this.getVerticalScrollBar().getMaximum());
        });
    }
}
