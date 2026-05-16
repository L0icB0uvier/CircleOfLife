package View;

import Controller.Controller;
import Model.Game;
import Model.GameDataManager;
import View.Adapter.LoadGameAdapter;
import View.CustomComponents.CustomLabel;
import View.Utils.FontScaler;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

public class GraphicalLoadGame extends JPanel {
    ArrayList<CustomLabel> customLabelsList;
    Game game;
    JPanel contentPanel ;
    JScrollPane scrollPane;
    JButton cancelButton;


    public GraphicalLoadGame(Controller controller, GraphicalUserInterface userInterface){
        MigLayout layout = new MigLayout("fill, insets 10", "[]", "[80%]push[15%, align center]");
        this.setLayout(layout);
        this.setBackground(UIColor.BACKGROUND);

        scrollPane = new JScrollPane();
        scrollPane.setBorder(new RoundedBorder(15, UIColor.LIGHT_BLUE, 3));
        contentPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(contentPanel, BoxLayout.Y_AXIS);
        contentPanel.setLayout(boxLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        scrollPane.setViewportView(contentPanel);

        for(String game: GameDataManager.getSaveFiles()) {
            addGame(game, controller, userInterface);
        }

        scrollPane.setWheelScrollingEnabled(true);
        SwingUtilities.invokeLater(()->{
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });

        cancelButton = createBorderedButton("Revenir au menu");
        cancelButton.setPreferredSize(new Dimension(300, 75));
        cancelButton.setMaximumSize(new Dimension(300, 75));
        cancelButton.setBackground(UIColor.RED);
        cancelButton.addComponentListener(new FontScaler(cancelButton));


        this.add(scrollPane, "cell 0 0, grow");
        this.add(Box.createGlue(), "cell 0 1, growx");
        this.add(cancelButton, "cell 0 1");
        this.add(Box.createGlue(), "cell 0 1, growx");
    }

    public void addGame(String game, Controller controller, GraphicalUserInterface userInterface) {
        String[] gameData = GameDataManager.parseFileName(game);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        MigLayout layout = new MigLayout("fill, insets 0 10 0 10", "[70%, align left][30%]",
                "push[50%]10%[30%, align center]push" );
        JPanel gamePanel = new JPanel(layout);
        gamePanel.setPreferredSize(new Dimension(500, 150));
        gamePanel.setMaximumSize(new Dimension(10000, 150));
        gamePanel.setBorder(new RoundedBorder(15, UIColor.BROWN, 5));
        gamePanel.setBackground(UIColor.BACKGROUND);

        String[] dates = gameData[0].split(" ");
        String date = dates[1] + " " + dates[2] + " " + dates[3] + ", à " + dates[4];
        JLabel dateLabel = new JLabel(date);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String player1 = gameData[1] + " " + gameData[3].substring(0, gameData[3].indexOf(' '));
        String sep = "|";
        String player2 = gameData[3].substring(gameData[3].lastIndexOf(' ')) + " " + gameData[2];

        JLabel player1Label = new JLabel(player1);
        player1Label.setForeground(UIColor.BLUE);
        JLabel sepLabel = new JLabel(sep);
        JLabel player2Label = new JLabel(player2);
        player2Label.setForeground(UIColor.RED);

        gamePanel.add(dateLabel, "cell 0 0, grow");
        gamePanel.add(player1Label, "cell 0 1, growy");
        gamePanel.add(sepLabel, "cell 0 1, growy");
        gamePanel.add(player2Label, "cell 0 1, growy");

        dateLabel.addComponentListener(new FontScaler(0.7f, dateLabel));
        player1Label.addComponentListener(new FontScaler(0.7f, player1Label, sepLabel, player2Label));

        gamePanel.addMouseListener(new LoadGameAdapter(controller, userInterface, game));
        contentPanel.add(gamePanel);
    }

    private JButton createBorderedButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        return button;
    }
}
