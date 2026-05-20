package View;

import Controller.Controller;
import Controller.IA.AILevel;
import Global.Configuration;
import Model.Game;
import Model.PlayerData;
import Patterns.Observer;
import View.Adapter.*;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GraphicalUserInterface implements Runnable, UserInterface, Observer {
    Game game;
    EventCollector controller;
    JFrame frame;
    GraphicalGame graphicalGame;

    GraphicalMainMenu graphicalMainMenu;
    GraphicalNewGame graphicalNewGame;
    GraphicalLoadGame graphicalLoadGame;

    public GraphicalUserInterface(Game game, EventCollector controller) {
        this.game = game;
        this.controller = controller;
    }

    public static void start(Game game, EventCollector controller) {
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

        if (graphicalNewGame.player1Choice.getValue().equals("Joueur")) {
            Configuration.setPlayer1Settings(null, graphicalNewGame.player1NameTextField.getText().isEmpty() ? "Joueur 1" : graphicalNewGame.player1NameTextField.getText());
        } else {
            if (Objects.equals(graphicalNewGame.AI1LevelChoice.getValue(), "Facile")) {
                Configuration.setPlayer1Settings(AILevel.EASY, "IA Facile");
            } else if (Objects.equals(graphicalNewGame.AI1LevelChoice.getValue(), "Moyen")) {
                Configuration.setPlayer1Settings(AILevel.MEDIUM, "IA Moyenne");
            } else {
                Configuration.setPlayer1Settings(AILevel.HARD, "IA Difficile");
            }
        }
        if (graphicalNewGame.player2Choice.getValue().equals("Joueur")) {
            Configuration.setPlayer2Settings(null, graphicalNewGame.player2NameTextField.getText().isEmpty() ? "Joueur 2" : graphicalNewGame.player2NameTextField.getText());

        } else {
            if (Objects.equals(graphicalNewGame.AI2LevelChoice.getValue(), "Facile")) {
                Configuration.setPlayer2Settings(AILevel.EASY, "IA Facile");
            } else if (Objects.equals(graphicalNewGame.AI2LevelChoice.getValue(), "Moyen")) {
                Configuration.setPlayer2Settings(AILevel.MEDIUM, "IA Moyen");
            } else {
                Configuration.setPlayer2Settings(AILevel.HARD, "IA Difficile");
            }
        }
    }

    public void updateUndoRedoEnabled() {
        //TODO : ajouter les images des boutons grisés
        //graphicalGame.gameControlBar.undoBt.setEnabled(game.getMatch().canUndo());
        //graphicalGame.gameControlBar.redoBt.setEnabled(game.getMatch().canRedo());
    }

    public void startLoadPage() {
        graphicalLoadGame = new GraphicalLoadGame((Controller) controller, this);

        graphicalLoadGame.cancelButton.addActionListener(new ChangePageAdapter(this, graphicalMainMenu));

        frame.setContentPane(graphicalLoadGame);
        frame.revalidate();
    }


    public void startGame() {
        graphicalGame = new GraphicalGame(game);

        graphicalGame.gameControlBar.undoBt.addActionListener(new ControlButtonAdapter(controller, "Undo"));
        graphicalGame.gameControlBar.redoBt.addActionListener(new ControlButtonAdapter(controller, "Redo"));
        graphicalGame.gameControlBar.saveBt.addActionListener(new ControlButtonAdapter(controller, "Save"));

        updateUndoRedoEnabled();
        PopUpAdapter pua;

        pua = new PopUpAdapter(frame, controller,3, "Voulez-vous sauvegarder la partie en cours ?", "");
        graphicalGame.gameControlBar.saveBt.addActionListener(pua);
        pua.setActionButton(0,"Annuler",true);
        pua.setButtonLabel(0,"Annuler");
        pua.setActionButton(2,"Save",true);
        pua.setButtonLabel(2,"Sauvegarder");
        pua.setButtonVisibility(1,false);

        pua = new PopUpAdapter(frame, controller, 3,"Voulez-vous abandonner la manche en cours ?", "Attention les données non sauvegardées seront supprimées !");
        graphicalGame.gameControlBar.forfeitBt.addActionListener(pua);
        pua.setActionButton(0,"Annuler",true);
        pua.setButtonLabel(0,"Annuler");
        pua.setActionButton(2,"GiveUp",true);
        pua.setButtonLabel(2,"Abandonner");
        pua.setButtonVisibility(1,false);

        MouseAdapter mouseAdapter = new MouseAdapter(controller, graphicalGame);
        graphicalGame.gamePanel.addMouseListener(mouseAdapter);
        graphicalGame.gamePanel.addMouseMotionListener(mouseAdapter);

        Configuration.info("Changement de page vers " + graphicalGame.getClass());
        frame.setContentPane(graphicalGame);
        frame.revalidate();

        graphicalGame.updateGameInfo();
    }

    @Override
    public void update() {
        if (graphicalGame == null) return;
        graphicalGame.updateGameInfo();
        updateUndoRedoEnabled();

        if(game.isReviewModeActive()){
            graphicalGame.updateGameInfo();
        }

        else if(game.isGameOver())
            displayGameOverPopup(game.getWinningPlayer());
    }

    @Override
    public void run() {
        frame = new JFrame("Circle of life");
        Configuration.initSettings();
        frame.setSize(Configuration.readInt("WindowWidth"), Configuration.readInt("WindowHeight"));
        frame.setMinimumSize(new Dimension(800, 600));
        game.addObserver(this);

        graphicalMainMenu = new GraphicalMainMenu(frame);
        graphicalNewGame = new GraphicalNewGame(frame);

        graphicalMainMenu.newGameButton.addActionListener(new ChangePageAdapter(this, graphicalNewGame));
        graphicalMainMenu.continueButton.addActionListener(new ContinueGameAdapter(controller, this));
        graphicalMainMenu.loadButton.addActionListener(new LoadGamesAdapter(this));
        graphicalMainMenu.tutorialButton.addActionListener(new ChangePageAdapter(this, null)); //TODO Ajouter la page de tuto
        graphicalMainMenu.quitButton.addActionListener(new QuitAdapter());

        graphicalNewGame.startButton.addActionListener(new StartGameAdapter(controller, this));
        graphicalNewGame.startButton.addActionListener(new NewGameAdapter(controller));
        graphicalNewGame.cancelButton.addActionListener(new ChangePageAdapter(this, graphicalMainMenu));

        frame.addKeyListener(new KeyboardAdapter(controller));

        frame.setContentPane(graphicalMainMenu);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void displayGameOverPopup(int nJoueur) {

        PopUpAdapter pua = new PopUpAdapter(frame,controller,5,"Le Joueur " + (nJoueur + 1) + " a gagné la manche !","");

        pua.setButtonLabel(0,"Menu");
        pua.setButtonLabel(1,"Sauvegarder");
        pua.setButtonVisibility(2,false);
        pua.setButtonLabel(3,"Analyser");
        pua.setButtonLabel(4,"Rejouer");

        pua.setActionButton(0, this, graphicalMainMenu);
        pua.setActionButton(1, "Save",false);
        pua.setActionButton(3, "Review", true);
        pua.setActionButton(4, "Replay",true);

        pua.show();
    }
}
