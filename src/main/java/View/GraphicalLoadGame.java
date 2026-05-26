package View;

import Controller.Controller;
import Global.Configuration;
import Model.GameDataManager;
import View.Adapter.PopUpAdapter;
import View.Adapter.SelectGameMouseAdapter;
import View.CustomComponents.CustomButton;
import View.CustomComponents.ImageButton;
import View.Utils.FontScaler;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

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
        MigLayout layout = new MigLayout("fill, insets 10", "[]", "[90%][10%, align center]");
        this.setLayout(layout);
        this.setBackground(UIColor.BACKGROUND);

        scrollPane = new JScrollPane();
        scrollPane.setBorder(new RoundedBorder(15, UIColor.LIGHT_BLUE, 3));
        contentPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(contentPanel, BoxLayout.Y_AXIS);
        contentPanel.setLayout(boxLayout);
        contentPanel.setBackground(UIColor.BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

        scrollPane.setViewportView(contentPanel);
        scrollPane.setBackground(UIColor.BACKGROUND);

        for(String game: GameDataManager.getSaveFiles()) {
            addGame(game);
        }

        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        MigLayout layoutBtns = new MigLayout("fill", "10%[15%, sg]push[15%, sg]push[15%, sg]10%", "");
        JPanel buttonComp = new JPanel(layoutBtns);

        cancelButton = createJButton("Annuler",true,UIColor.ORANGE);
        cancelButton.setMinimumSize(new Dimension(0, 0));

        renameBtn = createJButton("Renommer",false,UIColor.BROWN);
        renameBtn.setMinimumSize(new Dimension(0, 0));
        renameBtn.addActionListener(e -> renameGame());
        renameBtn.setEnabled(false);

        loadBtn = createJButton("Charger",false,UIColor.GREEN);
        loadBtn.setMinimumSize(new Dimension(0, 0));
        loadBtn.addActionListener(e -> loadGame());
        loadBtn.setEnabled(false);

        buttonComp.setBackground(UIColor.BACKGROUND);
        buttonComp.add(cancelButton, "cell 0 0,grow");
        buttonComp.add(renameBtn, "cell 1 0,grow");
        buttonComp.add(loadBtn, "cell 2 0,grow");

        this.add(scrollPane, "cell 0 0, grow");
        this.add(buttonComp, "cell 0 1, grow");

        buttonComp.addComponentListener(new FontScaler(cancelButton, renameBtn, /*deleteBtn,*/ loadBtn));

        this.addMouseListener(getMouseDeselector());
        scrollPane.addMouseListener(getMouseDeselector());
        contentPanel.addMouseListener(getMouseDeselector());

        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                int visibleHeight = scrollPane.getViewport().getHeight();
                int panelHeight = visibleHeight / 5;
                for (Component c : contentPanel.getComponents()) {

                    if (c instanceof JPanel panel) {

                        panel.setPreferredSize(new Dimension(c.getPreferredSize().width, panelHeight));

                        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,panelHeight));
                    }
                }


            }
        });


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

        JLabel renameLabel = new JLabel("Nouveau nom : (sans '_')\t ");
        renameLabel.setFocusable(true);
        JTextField renameTextField = createJTextField(gameLabel.getText());

        JButton cancelButton = createBorderedButton("Annuler");
        cancelButton.addActionListener(e-> {
            renameMenu.dispose();
        });
        JButton confirmButton = createBorderedButton("Renommer");
        confirmButton.addActionListener(e -> {
            if(!renameTextField.getText().equals(gameLabel.getText())) { //on recupere et mets a jour l'ancien nom du jeu avec le nouvel
                if (GameDataManager.newNameContainsSeparator(renameTextField.getText())) {
                    renameLabel.setText("Supprimer des caracteres '_'\t");
                    renameLabel.requestFocusInWindow();
                    return;
                }
                 currentGame = controller.renameGame(currentGame, renameTextField.getText());
                SelectGameMouseAdapter selectMouseAdapter = (SelectGameMouseAdapter) currentGamePanel.getMouseListeners()[0];
                selectMouseAdapter.updateGame(currentGame);
            }
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

    private void deleteGame(String game, JPanel gamePanel) {
        int indexComponent = Arrays.stream(contentPanel.getComponents()).toList().indexOf(gamePanel);
        contentPanel.remove(gamePanel);
        contentPanel.remove(indexComponent - 1);
        controller.deleteGame(game);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void selectGame(JPanel gamePanel, String gameFile) {
        if(currentGame != null) currentGamePanel.setBorder(new RoundedBorder(15, UIColor.BROWN, 5));
        this.currentGame = gameFile;
        this.currentGamePanel = gamePanel;
        this.renameBtn.setEnabled(true);
        this.loadBtn.setEnabled(true);
        gamePanel.setBorder(new RoundedBorder(15, UIColor.HOVER_COLOR, 5));
    }

    private void deselectGame() {
        if(currentGame == null) return;
        currentGame = null;
        currentGamePanel.setBorder(new RoundedBorder(15, UIColor.BROWN, 5));
        currentGamePanel = null;
        this.renameBtn.setEnabled(false);
        this.loadBtn.setEnabled(false);
    }


    public void addGame(String game) {
        String[] gameData = GameDataManager.parseFileName(game);
        if (gameData == null)
            return;
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        MigLayout layout = new MigLayout("fill, insets 0 10 0 10", "[90%, align left][10%, align right]", "[10%][20%]5%[30%]5%[20%, align center][10%]" );
        JPanel gamePanel = new JPanel(layout);
        gamePanel.setPreferredSize(new Dimension(500,150));
        gamePanel.setMaximumSize(new Dimension(10000,150));
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

        ImageButton deleteBtn = new ImageButton("delete.png", UIColor.RED);

        JPanel deleteButtonPanel = new JPanel();
        deleteButtonPanel.setLayout(new GridLayout());
        deleteButtonPanel.setOpaque(false);
        deleteButtonPanel.add(deleteBtn);

        gamePanel.add(nameLabel, "cell 0 0,span 1 3, grow");
        gamePanel.add(player1Label, "cell 0 3,span 1 2, growy");
        gamePanel.add(sepLabel, "cell 0 3, span 1 2, growy");
        gamePanel.add(player2Label, "cell 0 3,span 1 2, growy");
        gamePanel.add(deleteButtonPanel, "cell 1 1,span 1 3, grow");

        nameLabel.addComponentListener(new FontScaler(0.7f, nameLabel));
        player1Label.addComponentListener(new FontScaler(0.7f, player1Label, sepLabel, player2Label));

        gamePanel.addMouseListener(new SelectGameMouseAdapter(this, gamePanel, game));
        PopUpAdapter pua = new PopUpAdapter(this.userInterface.getFrame(), controller,3,"Voulez-vous supprimer "+
                (gameData[4].isEmpty() ? name : game) +"?","");
        pua.setButtonVisibility(1,false);
        pua.setButtonLabel(0,"Annuler");
        pua.setButtonLabel(2,"Supprimer");
        pua.setActionButton(0,"Annuler",true);
        pua.setActionButton(2, e -> deleteGame(game,gamePanel),true);
        deleteBtn.addActionListener(pua);

        contentPanel.add(gamePanel);
    }

    private JButton createJButton(String text,boolean enabled,Color bgColor){
        CustomButton button = new CustomButton(text,bgColor, true);
        button.setFocusable(false);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setEnabled(enabled);
        return button;

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
