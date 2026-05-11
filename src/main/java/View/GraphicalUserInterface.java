package View;

import Controller.IA.AILevel;
import Global.Configuration;
import Model.Game;
import Model.PlayerData;
import Patterns.Observer;
import View.Adapter.*;
import View.CustomComponents.PopUpPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GraphicalUserInterface implements Runnable, UserInterface, Observer {
    Game game;
    EventCollector controller;
    JFrame frame;
    //GraphicalGame graphicalGame;
    GraphicalGame graphicalGame;

    GraphicalMainMenu graphicalMainMenu;
    GraphicalNewGame graphicalNewGame;

    public GraphicalUserInterface(Game game, EventCollector controller){
        this.game = game;
        this.controller = controller;
    }

    public static void start(Game game, EventCollector controller){
        GraphicalUserInterface vue = new GraphicalUserInterface(game, controller);
        controller.addUserInterface(vue);
        SwingUtilities.invokeLater(vue);
    }

    @Override
    public void toggleFullscreen() {

    }

    public JFrame getFrame() {
        return frame;
    }

    @Override
    public void updateSettings() {

        if(graphicalNewGame.player1Button.isSelected()) {
            Configuration.setPlayer1Settings(null);
        } else {
            if (Objects.equals(graphicalNewGame.AI1ComboBox.getSelectedItem(), "Facile")) {
                Configuration.setPlayer1Settings(AILevel.EASY);
            } else if (Objects.equals(graphicalNewGame.AI1ComboBox.getSelectedItem(), "Moyen")) {
                Configuration.setPlayer1Settings(AILevel.MEDIUM);
            } else {
                Configuration.setPlayer1Settings(AILevel.HARD);
            }
        }
        if(graphicalNewGame.player2Button.isSelected()) {
            Configuration.setPlayer2Settings(null);

        }else {
            if (Objects.equals(graphicalNewGame.AI2ComboBox.getSelectedItem(), "Facile")) {
                Configuration.setPlayer2Settings(AILevel.EASY);
            } else if (Objects.equals(graphicalNewGame.AI2ComboBox.getSelectedItem(), "Moyen")) {
                Configuration.setPlayer2Settings(AILevel.MEDIUM);
            } else {
                Configuration.setPlayer2Settings(AILevel.HARD);
            }
        }
    }

    @Override
    public void playerTurn(int nPlayer) {
        graphicalGame.playerTurn(nPlayer);
    }

    @Override
    public void updateScore(PlayerData[] playerData) {
        graphicalGame.updateScore(playerData);
    }

    public void updateUndoRedoEnabled(){
        //TODO : ajouter les images des boutons grisés
        //graphicalGame.gameControlBar.undoBt.setEnabled(game.getMatch().canUndo());
        //graphicalGame.gameControlBar.redoBt.setEnabled(game.getMatch().canRedo());
    }


    public void startGame() {
        graphicalGame = new GraphicalGame(game);

        graphicalGame.gameControlBar.undoBt.addActionListener(new ControlButtonAdapter(controller,"Undo"));
        graphicalGame.gameControlBar.redoBt.addActionListener(new ControlButtonAdapter(controller,"Redo"));
        graphicalGame.gameControlBar.saveBt.addActionListener(new ControlButtonAdapter(controller,"Save"));

        updateUndoRedoEnabled();
        PopUpAdapter pua;

        pua = new PopUpAdapter(frame,controller,"Voulez-vous sauvegarder la partie en cours ?","","annuler","continuer");
        graphicalGame.gameControlBar.saveBt.addActionListener(pua);
        pua.setActionLeftButton("annuler");
        pua.setActionRightButton("Save");

        pua = new PopUpAdapter(frame,controller,"Voulez-vous abandonner la manche en cours ?","","annuler","continuer");
        graphicalGame.gameControlBar.forfeitBt.addActionListener(pua);
        pua.setActionLeftButton("annuler");
        pua.setActionRightButton("GiveUp");

        MouseAdapter mouseAdapter = new MouseAdapter(controller, graphicalGame);
        graphicalGame.gamePanel.addMouseListener(mouseAdapter);
        graphicalGame.gamePanel.addMouseMotionListener(mouseAdapter);

        Configuration.info("Changement de page vers " + graphicalGame.getClass());
        frame.setContentPane(graphicalGame);
        frame.revalidate();
        playerTurn(game.getCurrentPlayerIndex());
    }

    @Override
    public void update() {
        playerTurn(game.getCurrentPlayerIndex());
        updateScore(game.getMatch().getPlayerData());
        updateUndoRedoEnabled();
    }

    @Override
    public void run() {
        frame = new JFrame("Circle of life");
        Configuration.initSettings();
        frame.setSize(Configuration.readInt("WindowWidth"), Configuration.readInt("WindowHeight"));
        frame.setMinimumSize(new Dimension(800,600));
        game.addObserver(this);

        graphicalMainMenu = new GraphicalMainMenu(frame);
        graphicalNewGame = new GraphicalNewGame(frame);

        graphicalMainMenu.newGameButton.addActionListener(new ChangePageAdapter(this, graphicalNewGame));
        graphicalMainMenu.continueButton.addActionListener(new ContinueGameAdapter(controller, this));

        graphicalNewGame.startButton.addActionListener(new StartGameAdapter(controller, this));
        graphicalNewGame.startButton.addActionListener(new NewGameAdapter(controller));
        graphicalNewGame.cancelButton.addActionListener(new ChangePageAdapter(this, graphicalMainMenu));

        frame.addKeyListener(new KeyboardAdapter(controller));

        frame.setContentPane(graphicalNewGame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void continueGame(int nJoueur){
        PopUpPanel popup = new PopUpPanel(controller);
        popup.setLeftButton("Menu");
        popup.setRightButton("Prochaine manche");
        popup.setMainLabel("mainText");
        popup.setSecondaryLabel("Le Joueur " + (nJoueur+1) + " a gagner la manche !");

        JDialog dialog = new JDialog(frame,"",true);
        dialog.setSize(800, 300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(frame);
        dialog.add(popup);

        popup.setActionRightButton("",dialog);
        popup.setActionLeftButton(this,graphicalMainMenu);
    }


}
