package View;

import Controller.Controller;
import Controller.IA.AILevel;
import Global.Configuration;
import Global.PlayerNumber;
import Model.Game;
import Model.PlayerData;
import Patterns.Observer;
import View.Adapter.*;
import View.Utils.ChoiceBox;

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
    GraphicalTutorial graphicalTutorial;

    public GraphicalUserInterface(Game game, EventCollector controller){
        this.game = game;
        this.controller = controller;
    }

    public GraphicalTutorial getGraphicalTutorial() {
        return graphicalTutorial;
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
        // Update Settings Joueur 1
        if(graphicalNewGame.player1Choice.getValue().equals("Joueur"))
            Configuration.setPlayerSettings(PlayerNumber.PLAYER_1, null, graphicalNewGame.player1NameTextField.getText().isEmpty() ? "Joueur 1" : graphicalNewGame.player1NameTextField.getText());
        else
            Configuration.updateAISettings(graphicalNewGame.AI1LevelChoice.getValue(), PlayerNumber.PLAYER_1);

        // Update Settings Joueur 2
        if(graphicalNewGame.player2Choice.getValue().equals("Joueur"))
            Configuration.setPlayerSettings(PlayerNumber.PLAYER_2, null, graphicalNewGame.player2NameTextField.getText().isEmpty() ? "Joueur 2" : graphicalNewGame.player2NameTextField.getText());
        else
            Configuration.updateAISettings(graphicalNewGame.AI2LevelChoice.getValue(), PlayerNumber.PLAYER_2);

        // Update Setting premier joueur
        Configuration.updateStartingPlayerSetting(graphicalNewGame.startingPlayerChoice.getValue());
    }


    public void updateUndoRedoEnabled() {
        //TODO : ajouter les images des boutons grisés
        //graphicalGame.gameControlBar.undoBt.setEnabled(game.getMatch().canUndo());
        //graphicalGame.gameControlBar.redoBt.setEnabled(game.getMatch().canRedo());
        graphicalGame.undoBt.setEnabled(game.getMatch().canUndo());
        graphicalGame.redoBt.setEnabled(game.getMatch().canRedo());
    }

    public void startLoadPage() {
        graphicalLoadGame = new GraphicalLoadGame((Controller) controller, this);

        graphicalLoadGame.cancelButton.addActionListener(new ChangePageAdapter(this, graphicalMainMenu));

        frame.setContentPane(graphicalLoadGame);
        frame.revalidate();
    }


    public void startGame() {
        graphicalGame = new GraphicalGame(game);

        graphicalGame.undoBt.addActionListener(new ControlButtonAdapter(controller, "Undo"));
        graphicalGame.redoBt.addActionListener(new ControlButtonAdapter(controller, "Redo"));

        updateUndoRedoEnabled();
        PopUpAdapter pua;

        pua = new PopUpAdapter(frame, controller, 3, "Voulez-vous sauvegarder la partie en cours ?", "");
        graphicalGame.gameControlBar.saveBt.addActionListener(pua);
        pua.setActionButton(0, "Annuler", true);
        pua.setButtonLabel(0, "Annuler");
        pua.setActionButton(2, "Save", true);
        pua.setButtonLabel(2, "Sauvegarder");
        pua.setButtonVisibility(1, false);

        pua = new PopUpAdapter(frame, controller, 3, "Voulez-vous quittez la partie en cours ?", "Attention les données non sauvegardées seront supprimées !");
        graphicalGame.gameControlBar.forfeitBt.addActionListener(pua);
        pua.setActionButton(0, "Annuler", true);
        pua.setButtonLabel(0, "Annuler");
        pua.setActionButton(2, this, graphicalMainMenu);
        pua.setButtonLabel(2, "Menu");
        pua.setButtonVisibility(1, false);

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

        if (game.isReviewModeActive()) {
            graphicalGame.updateGameInfo();
            graphicalGame.replayBt.getParent().setVisible(true);
            graphicalGame.reviewBt.getParent().setVisible(true);
        } else if (game.isGameOver()){
            graphicalGame.replayBt.getParent().setVisible(true);
            graphicalGame.reviewBt.getParent().setVisible(true);
        }; //TODO : ajouter un bouton review qui apparait une fois la partie fini;
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
        graphicalTutorial = new GraphicalTutorial(this);

        graphicalMainMenu.newGameButton.addActionListener(new ChangePageAdapter(this, graphicalNewGame));
        graphicalMainMenu.continueButton.addActionListener(new ContinueGameAdapter(controller, this));
        graphicalMainMenu.loadButton.addActionListener(new LoadGamesAdapter(this));
        graphicalMainMenu.tutorialButton.addActionListener(new TutorialAdapter(this));
        graphicalMainMenu.quitButton.addActionListener(new QuitAdapter());

        graphicalNewGame.startButton.addActionListener(new StartGameAdapter(controller, this));
        graphicalNewGame.startButton.addActionListener(new NewGameAdapter(controller));
        graphicalNewGame.cancelButton.addActionListener(new ChangePageAdapter(this, graphicalMainMenu));

        frame.addKeyListener(new KeyboardAdapter(controller));

        frame.setContentPane(graphicalMainMenu);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
