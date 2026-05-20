package View;

import Controller.Controller;
import Model.Game;
import Model.GameDataManager;
import View.Adapter.LoadGameAdapter;
import View.Adapter.LoadGamesAdapter;
import View.CustomComponents.CustomLabel;
import View.Utils.FontScaler;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class GraphicalLoadGame extends JPanel {
    JPanel contentPanel, currentGamePanel;
    JScrollPane scrollPane;
    JButton cancelButton, renameBtn, deleteBtn, loadBtn;
    String currentGame;
    Controller controller;
    GraphicalUserInterface userInterface;


    public GraphicalLoadGame(Controller controller, GraphicalUserInterface userInterface){
        this.controller = controller;
        this.userInterface = userInterface;
        this.currentGame = null;
        MigLayout layout = new MigLayout("fill, insets 10", "[]", "[80%]push[15%, align center]");
        this.setLayout(layout);
        this.setBackground(UIColor.BACKGROUND);

        scrollPane = new JScrollPane();
        scrollPane.setBorder(new RoundedBorder(15, UIColor.LIGHT_BLUE, 3));
        contentPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(contentPanel, BoxLayout.Y_AXIS);
        contentPanel.setLayout(boxLayout);
        contentPanel.setBorder(new RoundedBorder(30, UIColor.BROWN, 10));
        contentPanel.setBackground(UIColor.BACKGROUND);
        scrollPane.setViewportView(contentPanel);
        scrollPane.setBackground(UIColor.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        for(String game: GameDataManager.getSaveFiles()) {
            addGame(game, controller, userInterface);
        }

        scrollPane.setWheelScrollingEnabled(true);

        MigLayout layoutBtns = new MigLayout("fill", "10%[15%, sg]push[15%, sg]push[15%, sg]push[15%, sg]10%", "");
        JPanel buttonComp = new JPanel(layoutBtns);

        cancelButton = createBorderedButton("Revenir au menu");
        cancelButton.setPreferredSize(new Dimension(250, 75));
        cancelButton.setMaximumSize(new Dimension(250, 75));
        cancelButton.setBackground(Color.GRAY);

        renameBtn = createBorderedButton("Renommer");
        renameBtn.setPreferredSize(new Dimension(250, 75));
        renameBtn.setMaximumSize(new Dimension(250, 75));
        renameBtn.setBackground(UIColor.BROWN);
        renameBtn.addActionListener(e -> renameGame());
        renameBtn.setEnabled(false);

        deleteBtn = createBorderedButton("Supprimer");
        deleteBtn.setPreferredSize(new Dimension(250, 75));
        deleteBtn.setMaximumSize(new Dimension(250, 75));
        deleteBtn.setBackground(UIColor.RED);
        deleteBtn.addActionListener(e -> deleteGame());
        deleteBtn.setEnabled(false);

        loadBtn = createBorderedButton("Charger");
        loadBtn.setPreferredSize(new Dimension(250, 75));
        loadBtn.setMaximumSize(new Dimension(250, 75));
        loadBtn.setBackground(UIColor.GREEN);
        loadBtn.addActionListener(e -> loadGame());
        loadBtn.setEnabled(false);

        cancelButton.addComponentListener(new FontScaler(cancelButton, renameBtn, deleteBtn, loadBtn));

        buttonComp.setBackground(UIColor.BACKGROUND);
        buttonComp.add(cancelButton, "cell 0 0");
        buttonComp.add(renameBtn, "cell 1 0");
        buttonComp.add(deleteBtn, "cell 2 0");
        buttonComp.add(loadBtn, "cell 3 0");

        this.add(scrollPane, "cell 0 0, grow");
        this.add(buttonComp, "cell 0 1, grow");
        this.addMouseListener(getMouseDeselector());
        scrollPane.addMouseListener(getMouseDeselector());
        contentPanel.addMouseListener(getMouseDeselector());
        buttonComp.addMouseListener(getMouseDeselector());

    }

    private void loadGame() {
        if(currentGame == null) return;
        controller.loadGame(currentGame);
        userInterface.startGame();
    }

    private void renameGame() {

    }

    private void deleteGame() {
        if (currentGame == null) return;
        int indexComponent = Arrays.stream(contentPanel.getComponents()).toList().indexOf(currentGamePanel);
        contentPanel.remove(currentGamePanel);
        contentPanel.remove(indexComponent - 1);
        controller.deleteGame(currentGame);
        this.revalidate();
    }

    private void selectGame(JPanel gamePanel, String gameFile) {
        if(currentGame != null) currentGamePanel.setBorder(new RoundedBorder(15, UIColor.BROWN, 5));
        this.currentGame = gameFile;
        this.currentGamePanel = gamePanel;
        this.renameBtn.setEnabled(true);
        this.deleteBtn.setEnabled(true);
        this.loadBtn.setEnabled(true);
        gamePanel.setBorder(new RoundedBorder(10, UIColor.HOVER_COLOR, 5));
    }

    private void deselectGame() {
        if(currentGame == null) return;
        currentGame = null;
        currentGamePanel.setBorder(new RoundedBorder(15, UIColor.BROWN, 5));
        currentGamePanel = null;
        this.renameBtn.setEnabled(false);
        this.deleteBtn.setEnabled(false);
        this.loadBtn.setEnabled(false);
    }


    public void addGame(String game, Controller controller, GraphicalUserInterface userInterface) {
        String[] gameData = GameDataManager.parseFileName(game);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        MigLayout layout = new MigLayout("fill, insets 0 10 0 10", "[70%, align left][30%]",
                "push[50%]10%[30%, align center]push" );
        JPanel gamePanel = new JPanel(layout);
        gamePanel.setPreferredSize(new Dimension(500, 150));
        gamePanel.setMaximumSize(new Dimension(10000, 150));
        gamePanel.setFocusable(true);
        gamePanel.setBorder(new RoundedBorder(15, UIColor.BROWN, 5));
        gamePanel.setBackground(UIColor.BACKGROUND);
        gamePanel.setName(game);

        String[] dates = gameData[0].split(" ");
        String date = dates[0] + "/" + dates[1] + "/" + dates[2] + ", à " + dates[3];
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

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectGame(gamePanel, game);
            }
        });
        contentPanel.add(gamePanel);
    }

    private JButton createBorderedButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        return button;
    }

    private MouseAdapter getMouseDeselector() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                deselectGame();
            }
        };
    }
}
