package View;

import Global.Configuration;
import Model.Coordinate;
import Model.Game;
import Model.PlayerData;
import View.Adapter.ControlButtonAdapter;
import View.CustomComponents.*;
import View.Utils.FontScaler;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Set;

public class GraphicalGame extends JPanel {
    Game game;
    EventCollector controller;

    GamePanel gamePanel;
    ArrayList<PlayerInfo> playerInfos;
    GameControlBar gameControlBar;
    GameInfo gameInfo;
    Image crown, overlayIcon;

    JLayer<GamePanel> gameLayer;

    public JButton undoBt, redoBt, allUndoBt, allRedoBt;
    public CustomButton forfeitBt, replayBt, reviewBt;

    public JPanel undoPanel, redoPanel, allUndoPanel, allRedoPanel, forfeitPanel, reviewPanel, replayPanel;

    public GraphicalGame(Game game, EventCollector controller){
        this.game = game;
        this.controller = controller;
        this.crown = Configuration.loadImage("crown.png");
        this.overlayIcon = Configuration.loadImage("display_options_button.png");

        playerInfos = new ArrayList<>();

        MigLayout layout = new MigLayout("fill, insets 2% 2% 2% 2%", "[grow]1.5%[grow]1.5%[58%]1.5%[grow]1.5%[grow]1.5%[14%]","[10%]1.5%[10%][grow][10%]1.5%[10%][grow][10%]" );
        this.setLayout(layout);
        this.setBackground(UIColor.BACKGROUND);

        undoBt = new ImageButton("undoIcon.png");
        undoBt.addActionListener(new ControlButtonAdapter(controller, "Undo"));
        undoPanel = new JPanel();
        undoPanel.setLayout(new GridLayout());
        undoPanel.setOpaque(false);
        undoPanel.add(undoBt);
        undoBt.setToolTipText("<html><b>Annuler la dernière action</b></html>");

        redoBt = new ImageButton("redoIcon.png");
        redoBt.addActionListener(new ControlButtonAdapter(controller, "Redo"));
        redoPanel = new JPanel();
        redoPanel.setOpaque(false);
        redoPanel.setLayout(new GridLayout());
        redoPanel.add(redoBt);
        redoBt.setToolTipText("<html><b>Refaire la dernière action.<b></html>");

        allUndoBt = new ImageButton("undoAllIcon.png");
        allUndoBt.addActionListener(new ControlButtonAdapter(controller, "UndoAll"));
        allUndoPanel = new JPanel();
        allUndoPanel.setOpaque(false);
        allUndoPanel.setLayout(new GridLayout());
        allUndoPanel.add(allUndoBt);
        allUndoBt.setToolTipText("<html><b>Renvenir au début de la partie</b></html>");

        allRedoBt = new ImageButton("redoAllIcon.png");
        allRedoBt.addActionListener(new ControlButtonAdapter(controller, "RedoAll"));
        allRedoPanel = new JPanel();
        allRedoPanel.setOpaque(false);
        allRedoPanel.setLayout(new GridLayout());
        allRedoPanel.add(allRedoBt);
        allRedoBt.setToolTipText("<html><b>Refaire la dernière action.<b></html>");

        forfeitBt = new CustomButton("Abandonner", UIColor.WHITE, true);
        forfeitBt.setOpaque(false);
        forfeitPanel = new JPanel();
        forfeitPanel.setOpaque(false);
        forfeitPanel.setLayout(new GridLayout());
        forfeitPanel.add(forfeitBt);
        forfeitPanel.setBackground(UIColor.WHITE);
        forfeitPanel.addComponentListener(new FontScaler(forfeitBt));
        forfeitBt.setVisible(true);
        forfeitBt.addActionListener(new ControlButtonAdapter(controller, "GiveUp"));
        forfeitBt.setToolTipText("<html><b>Abandonner la partie.<b></html>");

        replayBt = new CustomButton("Rejouer",UIColor.WHITE, true);
        replayBt.setOpaque(false);
        replayPanel = new JPanel();
        replayPanel.setOpaque(false);
        replayPanel.setLayout(new GridLayout());
        replayPanel.add(replayBt);
        replayPanel.setBackground(UIColor.WHITE);
        replayPanel.addComponentListener(new FontScaler(replayBt));
        replayPanel.setVisible(false);
        replayBt.addActionListener(new ControlButtonAdapter(controller, "Replay"));
        replayBt.setToolTipText("<html><b>Rejouer une partie.<b></html>");

        reviewBt = new CustomButton("Revoir", UIColor.WHITE, false);
        reviewBt.setOpaque(false);
        reviewPanel = new JPanel();
        reviewPanel.setOpaque(false);
        reviewPanel.setLayout(new GridLayout());
        reviewPanel.add(reviewBt);
        reviewPanel.setBackground(UIColor.WHITE);
        reviewPanel.addComponentListener(new FontScaler(reviewBt));
        reviewPanel.setVisible(false);
        reviewBt.addActionListener(new ControlButtonAdapter(controller, "ToggleReviewMode"));
        reviewBt.setToolTipText("<html><b>Revoir la partie.<b></html>");

        gameInfo = new GameInfo(game);

        gamePanel = new GamePanel(game);
        gamePanel.setBorder(new RoundedBorder(15, Color.BLACK, 5));
        gamePanel.setBackground(new Color(0, 0, 0, 0));

        GamePanelOverlayUI overlayUI = new GamePanelOverlayUI(gamePanel, overlayIcon);
        gameLayer = new JLayer<>(gamePanel, overlayUI);

        gameLayer.setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        gameLayer.addMouseMotionListener(new MouseMotionAdapter() {});
        gameLayer.setMinimumSize(new Dimension(0, 0));

        playerInfos.add(new PlayerInfo(game.getMatch().getPlayerData()[0].getName(),0));
        PlayerInfo player1Info = playerInfos.get(0);
        playerInfos.add(new PlayerInfo(game.getMatch().getPlayerData()[1].getName(),1));
        PlayerInfo player2Info = playerInfos.get(1);

        gameControlBar = new GameControlBar(game);
        gameControlBar.setBackground(UIColor.BACKGROUND);

        reviewPanel.setMinimumSize(new Dimension(0, 0));
        reviewPanel.setMinimumSize(new Dimension(0, 0));
        allRedoPanel.setMinimumSize(new Dimension(0, 0));
        allUndoPanel.setMinimumSize(new Dimension(0, 0));
        redoPanel.setMinimumSize(new Dimension(0, 0));
        undoPanel.setMinimumSize(new Dimension(0, 0));
        gameInfo.setMinimumSize(new Dimension(0, 0));
        gamePanel.setMinimumSize(new Dimension(0, 0));
        player1Info.setMinimumSize(new Dimension(0, 0));
        player2Info.setMinimumSize(new Dimension(0, 0));
        gameControlBar.setMinimumSize(new Dimension(0, 0));
        forfeitPanel.setMinimumSize(new Dimension(0,0));

        this.add(gameInfo,"cell 2 0, grow, sg top");
        this.add(player1Info,"cell 5 0, grow, sg top");
        this.add(player2Info,"cell 5 1, grow, sg top");
        this.add(gameLayer,"cell 0 1, span 5 6, grow");
        this.add(gameControlBar,"cell 5 6, grow, sg top");

        this.setVisible(true);

        updateScore(game.getMatch().getPlayerData());
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void updateGUI(){
        updateGameInfo();
        updateEndGameButtonsVisibility();
        updateUndoRedoEnabled();
        updateGameControlBarVisibility();
        updateScore(game.getMatch().getPlayerData());
    }

    public void updateEndGameButtonsVisibility(){
        boolean replayBtnVisible = game.isGameOver() && game.isReviewModeActive() == false;
        boolean reviewBtnVisible = game.isGameOver();

        reviewBt.updateText(game.getMatch().isReviewModeActive()? "Retour" : "Revoir");
        reviewBt.setToolTipText(game.getMatch().isReviewModeActive()?
                "<html><b>Retour au menu de fin de partie.<b></html>" :
                "<html><b>Revoir la partie.<b></html>"
        );

        replayBt.getParent().setVisible(replayBtnVisible);
        reviewBt.getParent().setVisible(reviewBtnVisible);
    }

    public void updateGameInfo(){
        gameInfo.update();
        if (game.isGameOver()) {
            showEndGameButtons();
            if(game.isReviewModeActive()){
                enableReviewMode();
            }
            else{
                disableReviewMode();
            }
        } else {
            hideEndGameButtons();
            disableReviewMode();
        }
    }

    private void updateUndoRedoEnabled() {
        boolean undoRedoEnabled = game.isGameOver() == false || game.isReviewModeActive();

        undoBt.setEnabled(undoRedoEnabled && game.canUndo());
        allUndoBt.setEnabled(undoRedoEnabled && game.canUndo());
        redoBt.setEnabled(undoRedoEnabled && game.getMatch().canRedo());
        allRedoBt.setEnabled(undoRedoEnabled && game.getMatch().canRedo());

    }

    public void enableReviewMode(){
            this.remove(undoPanel);
            this.remove(redoPanel);

            this.add(undoPanel,"cell 1 0, grow");
            this.add(redoPanel,"cell 3 0, grow");
            this.add(allUndoPanel,"cell 0 0, grow");
            this.add(allRedoPanel,"cell 4 0, grow");
            this.revalidate();
            repaint();
    }

    public void disableReviewMode(){
        this.remove(undoPanel);
        this.remove(redoPanel);
        this.remove(allUndoPanel);
        this.remove(allRedoPanel);

        this.add(undoPanel,"cell 0 0,span 2, grow");
        this.add(redoPanel,"cell 3 0,span 2, grow");
        this.revalidate();
        repaint();

    }

    public void showEndGameButtons(){
        this.remove(forfeitPanel);
        this.add(reviewPanel,"cell 5 3, grow, sg top");
        this.add(replayPanel,"cell 5 4, grow, sg top");
        this.revalidate();
        repaint();

    }

    public void hideEndGameButtons(){
        this.remove(reviewPanel);
        this.remove(replayPanel);
        this.add(forfeitPanel,"cell 5 3, grow, sg top");
        this.revalidate();
        repaint();
    }

    private void updateScore(PlayerData[] playerData) {
        playerInfos.get(0).updateScore(playerData[0]);
        playerInfos.get(1).updateScore(playerData[1]);
    }

    private void updateGameControlBarVisibility(){
        boolean gameControlBarVisible = game.getMatch().isReviewModeActive() == false;
        gameControlBar.setVisible(gameControlBarVisible);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if(game.isGameOver() && crown != null){
            PlayerInfo player = playerInfos.get(game.getWinningPlayer());
            int min_size = Math.min((int)(this.getWidth()*0.03),(int)(this.getHeight()*0.03));
            int x = player.getWidth() + player.getX()  - min_size;
            int y = player.getY()  - min_size;
            int size = 2*min_size;
            g.drawImage(crown, x ,y, size, size, this);
        }
    }

    public void animateScore(Set<Coordinate> groupCoords, int scoreGained, int player, float progress) {
        gamePanel.animateScore(groupCoords, scoreGained, player, progress);
    }
}
