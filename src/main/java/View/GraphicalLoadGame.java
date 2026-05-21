package View;

import Controller.Controller;
import Global.Configuration;
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
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
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
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        for(String game: GameDataManager.getSaveFiles()) {
            addGame(game);
        }

        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        MigLayout layoutBtns = new MigLayout("fill", "10%[15%, sg]push[15%, sg]push[15%, sg]push[15%, sg]10%", "");
        JPanel buttonComp = new JPanel(layoutBtns);

        cancelButton = createBorderedButton("Revenir au menu");
        cancelButton.setPreferredSize(new Dimension(250, 75));
        cancelButton.setMaximumSize(new Dimension(250, 75));
        cancelButton.setBackground(Color.DARK_GRAY);
        cancelButton.setForeground(Color.WHITE);

        renameBtn = createDisabledButton("Renommer", UIColor.BROWN);
        renameBtn.setPreferredSize(new Dimension(250, 75));
        renameBtn.setMaximumSize(new Dimension(250, 75));
        renameBtn.addActionListener(e -> renameGame());
        renameBtn.setEnabled(false);

        deleteBtn = createDisabledButton("Supprimer", UIColor.RED);
        deleteBtn.setPreferredSize(new Dimension(250, 75));
        deleteBtn.setMaximumSize(new Dimension(250, 75));
        deleteBtn.addActionListener(e -> deleteGame());
        deleteBtn.setEnabled(false);

        loadBtn = createDisabledButton("Charger", UIColor.GREEN);
        loadBtn.setPreferredSize(new Dimension(250, 75));
        loadBtn.setMaximumSize(new Dimension(250, 75));
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
        JLabel gameLabel = (JLabel) currentGamePanel.getComponent(0);

        Configuration.info("Renommage du fichier " + gameLabel.getText());

        MigLayout layoutPopup = new MigLayout("fill, insets 10", "[20%]push[20%]", "[50%][50%]");
        JDialog renameMenu = new JDialog(userInterface.frame, "", true);
        renameMenu.setLayout(layoutPopup);
        renameMenu.setResizable(false);
        renameMenu.setLocationRelativeTo(userInterface.frame);
        renameMenu.setSize(new Dimension(500, 150));

        JLabel renameLabel = new JLabel("Nouveau nom :\t ");
        renameLabel.setFocusable(true);
        JTextField renameTextField = createJTextField(gameLabel.getText());

        JButton cancelButton = createBorderedButton("Annuler");
        cancelButton.addActionListener(e-> {
            renameMenu.dispose();
        });
        JButton confirmButton = createBorderedButton("Renommer");
        confirmButton.addActionListener(e -> {
            if(!renameTextField.getText().equals(gameLabel.getText())) controller.renameGame(currentGame, renameTextField.getText());
            gameLabel.setText(renameTextField.getText());
            renameMenu.dispose();
        });

        renameMenu.add(renameLabel, "cell 0 0, span 2 1");
        renameMenu.add(renameTextField, "cell 0 0, span 2 1, grow");
        renameMenu.add(cancelButton, "cell 0 1, grow");
        renameMenu.add(confirmButton, "cell 1 1, grow");

        renameTextField.addComponentListener(new FontScaler(0.3f, renameLabel, renameTextField));
        cancelButton.addComponentListener(new FontScaler(cancelButton, confirmButton));

        renameMenu.setVisible(true);
        renameLabel.requestFocusInWindow();
    }

    private static JTextField createJTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getForeground().equals(Color.LIGHT_GRAY)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.LIGHT_GRAY);
                    textField.setText(placeholder);
                }
            }
        });
        textField.setForeground(Color.LIGHT_GRAY);
        textField.setText(placeholder);
        textField.requestFocus(false);
        return textField;
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


    public void addGame(String game) {
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
        String name = dates[0] + "/" + dates[1] + "/" + dates[2] + ", à " + dates[3];
        if(!gameData[4].isEmpty()) name = gameData[4];
        JLabel nameLabel = new JLabel(name);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String player1 = gameData[1] + " " + gameData[3].substring(0, gameData[3].indexOf(' '));
        String sep = "|";
        String player2 = gameData[3].substring(gameData[3].lastIndexOf(' ')) + " " + gameData[2];

        JLabel player1Label = new JLabel(player1);
        player1Label.setForeground(UIColor.BLUE);
        JLabel sepLabel = new JLabel(sep);
        JLabel player2Label = new JLabel(player2);
        player2Label.setForeground(UIColor.RED);

        gamePanel.add(nameLabel, "cell 0 0, grow");
        gamePanel.add(player1Label, "cell 0 1, growy");
        gamePanel.add(sepLabel, "cell 0 1, growy");
        gamePanel.add(player2Label, "cell 0 1, growy");

        nameLabel.addComponentListener(new FontScaler(0.7f, nameLabel));
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

    private JButton createDisabledButton(String text, Color enabledColor) {
        JButton button = new JButton();
        button.setFocusable(false);
        button.setIcon(new ImageIcon(getImage(enabledColor,500,1000)));
        button.setDisabledIcon(new ImageIcon(getImage(Color.LIGHT_GRAY,500,1000)));
        button.setText(text);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        return button;
    }

    private BufferedImage getImage(Color color, int w, int h) {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.setColor(color);
        g.fillRect(0,0,w,h);
        g.setColor(Color.BLACK);
        g.dispose();

        return bi;
    }
}
