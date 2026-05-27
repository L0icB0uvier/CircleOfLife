package View;

import Controller.Controller;
import Global.Configuration;
import Global.PlayerNumber;
import Model.Coordinate;
import Model.Game;
import Patterns.Observer;
import View.Adapter.*;
import View.CustomComponents.ErrorPopUpPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class GraphicalUserInterface implements Runnable, UserInterface, Observer {
    Game game;
    EventCollector controller;
    JFrame frame;
    GraphicalGame graphicalGame;

    GraphicalMainMenu graphicalMainMenu;
    GraphicalNewGame graphicalNewGame;
    GraphicalLoadGame graphicalLoadGame;
    GraphicalTutorial graphicalTutorial;

    Timer gameAnimationTimer;

    private boolean maximized;

    public GraphicalUserInterface(Game game, EventCollector controller){
        this.game = game;
        this.controller = controller;
    }

    public GraphicalTutorial getGraphicalTutorial() {
        return graphicalTutorial;
    }

    public GraphicalMainMenu getGraphicalMainMenu() {
        return graphicalMainMenu;
    }

    public static void start(Game game, EventCollector controller) {
        GraphicalUserInterface vue = new GraphicalUserInterface(game, controller);
        controller.addUserInterface(vue);
        SwingUtilities.invokeLater(vue);
    }

    @Override
    public void toggleFullscreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        if (maximized) {
            device.setFullScreenWindow(null);
            maximized = false;
        } else {
            device.setFullScreenWindow(frame);
            maximized = true;
        }
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

    @Override
    public void animateScore(Set<Coordinate> groupCoords, int scoreGained, int player, float progress) {
        graphicalGame.animateScore(groupCoords, scoreGained, player, progress);
    }

    public void startLoadPage() {
        graphicalLoadGame = new GraphicalLoadGame((Controller) controller, this);

        graphicalLoadGame.cancelButton.addActionListener(new ChangePageAdapter(this, graphicalMainMenu));

        stopGameAnimationTimer();

        frame.setContentPane(graphicalLoadGame);
        frame.revalidate();
    }

    public void startGameAnimationTimer() {

        gameAnimationTimer.start();
    }

    public void stopGameAnimationTimer() {
        if(gameAnimationTimer.isRunning()) {
             
            gameAnimationTimer.stop();
        }
    }

    public void startGame() {
        graphicalGame = new GraphicalGame(game, controller);

        PopUpAdapter pua;

        pua = new PopUpAdapter(frame, controller, 3, "Voulez-vous sauvegarder la partie en cours ?", "");
        graphicalGame.gameControlBar.saveBt.addActionListener(pua);
        pua.setActionButton(0, "Annuler", true);
        pua.setButtonLabel(0, "Annuler");
        pua.setActionButton(2, "Save", true);
        pua.setButtonLabel(2, "Sauvegarder");
        pua.setButtonVisibility(1, false);

        pua = new PopUpAdapter(frame, controller, 3, "Voulez-vous quittez la partie en cours ?", "Attention les données non sauvegardées seront supprimées !");
        graphicalGame.gameControlBar.quitGameButton.addActionListener(pua);
        pua.setActionButton(0, "Annuler", true);
        pua.setButtonLabel(0, "Annuler");
        pua.setActionButton(2, this, graphicalMainMenu);
        pua.setButtonLabel(2, "Quitter");
        pua.setButtonVisibility(1, false);

        pua = new PopUpAdapter(frame, controller, 3, "Voulez-vous abandonner la partie en cours ?","");
        graphicalGame.forfeitBt.addActionListener(pua);
        pua.setActionButton(0, "Annuler", true);
        pua.setButtonLabel(0, "Annuler");
        pua.setActionButton(2,"GiveUp", true);
        pua.setButtonLabel(2, "Abandonner");
        pua.setButtonVisibility(1, false);

        MouseAdapter mouseAdapter = new MouseAdapter(controller, graphicalGame);
        graphicalGame.gamePanel.addMouseListener(mouseAdapter);
        graphicalGame.gamePanel.addMouseMotionListener(mouseAdapter);

         
        frame.setContentPane(graphicalGame);
        frame.revalidate();

        setUpTooltipSettings();

        startGameAnimationTimer();

        graphicalGame.updateGUI();
    }

    private static void setUpTooltipSettings() {
        ToolTipManager ttm = ToolTipManager.sharedInstance();

        ttm.setInitialDelay(200);  // Temps d'attente avant apparition (en millisecondes)
        ttm.setDismissDelay(5000); // Temps avant que l'infobulle ne disparaisse (5 secondes)
        ttm.setReshowDelay(100);   // Temps d'attente si on passe d'un bouton à un autre
    }

    @Override
    public void update() {
        if (graphicalGame == null) return;
        graphicalGame.updateGUI();
    }

    @Override
    public void run() {
        frame = new JFrame("Circle of life");
        Configuration.initSettings();
        frame.setSize(Configuration.readInt("WindowWidth"), Configuration.readInt("WindowHeight"));
        frame.setMinimumSize(new Dimension(800, 600));
        game.addUpdateObserver(this);

        graphicalMainMenu = new GraphicalMainMenu(frame);
        graphicalNewGame = new GraphicalNewGame(frame);
        graphicalTutorial = new GraphicalTutorial(this);

        graphicalMainMenu.newGameButton.addActionListener(new ChangePageAdapter(this, graphicalNewGame));
        graphicalMainMenu.continueButton.addActionListener(new ContinueGameAdapter(controller, this));
        graphicalMainMenu.loadButton.addActionListener(new LoadGamesAdapter(this));
        graphicalMainMenu.tutorialButton.addActionListener(new TutorialAdapter(this));
        graphicalMainMenu.quitButton.addActionListener(new QuitAdapter());

        graphicalNewGame.startButton.addActionListener(new StartGameAdapter(controller, this, graphicalNewGame));
        graphicalNewGame.startButton.addActionListener(new NewGameAdapter(controller));
        graphicalNewGame.cancelButton.addActionListener(new ChangePageAdapter(this, graphicalMainMenu));

        gameAnimationTimer = new Timer(16, new AnimationAdapter(controller));
        gameAnimationTimer.setCoalesce(true);

        frame.addKeyListener(new KeyboardAdapter(controller));

        frame.setContentPane(graphicalMainMenu);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void errorPopup(String string, Component c) {
        new ErrorPopUpPanel(string, c);
    }

}
