package View;

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
import java.util.ArrayList;

public class GraphicalGame extends JPanel {
    Game game;
    EventCollector controller;

    GamePanel gamePanel;
    ArrayList<PlayerInfo> playerInfos;
    GameControlBar gameControlBar;
    CustomLabel gameInfo;

    public JButton undoBt, redoBt, replayBt, reviewBt;

    public GraphicalGame(Game game, EventCollector controller){
        this.game = game;
        this.controller = controller;

        playerInfos = new ArrayList<>();

        MigLayout layout = new MigLayout("fill, insets 1.5% 1.5% 1.5% 1.5%", "[grow]1%[58%]1%[grow]1%[14%]","[10%]1%[10%][grow][10%]1%[10%][grow][10%]" );
        this.setLayout(layout);
        this.setBackground(UIColor.BACKGROUND);

        undoBt = new ImageButton("undoIcon.png");
        JPanel undoPanel = new JPanel();
        undoPanel.setOpaque(false);
        undoPanel.setLayout(new GridLayout());
        undoPanel.add(undoBt);
        undoBt.addActionListener(new ControlButtonAdapter(controller, "Undo"));

        redoBt = new ImageButton("redoIcon.png");
        JPanel redoPanel = new JPanel();
        redoPanel.setOpaque(false);
        redoPanel.setLayout(new GridLayout());
        redoPanel.add(redoBt);
        redoBt.addActionListener(new ControlButtonAdapter(controller, "Redo"));

        replayBt = new CustomButton("Rejouer",UIColor.WHITE);
        replayBt.setOpaque(false);
        JPanel replayPanel = new JPanel();
        replayPanel.setOpaque(false);
        replayPanel.setLayout(new GridLayout());
        replayPanel.add(replayBt);
        replayPanel.setBackground(UIColor.WHITE);
        replayPanel.addComponentListener(new FontScaler(replayBt));
        replayPanel.setVisible(false);
        replayBt.addActionListener(new ControlButtonAdapter(controller, "Replay"));

        reviewBt = new CustomButton("Revoir",UIColor.WHITE);
        reviewBt.setOpaque(false);
        JPanel reviewPanel = new JPanel();
        reviewPanel.setOpaque(false);
        reviewPanel.setLayout(new GridLayout());
        reviewPanel.add(reviewBt);
        reviewPanel.setBackground(UIColor.WHITE);
        reviewPanel.addComponentListener(new FontScaler(reviewBt));
        reviewPanel.setVisible(false);
        reviewBt.addActionListener(new ControlButtonAdapter(controller, "Review"));

        gameInfo = new CustomLabel(game);

        gameControlBar = new GameControlBar();

        gamePanel = new GamePanel(game);
        gamePanel.setBorder(new RoundedBorder(15,Color.BLACK,5));
        gamePanel.setBackground(UIColor.WHITE);

        playerInfos.add(new PlayerInfo(game.getMatch().getPlayerData()[0].getName(),0));
        PlayerInfo player1Info = playerInfos.get(0);
        playerInfos.add(new PlayerInfo(game.getMatch().getPlayerData()[1].getName(),1));
        PlayerInfo player2Info = playerInfos.get(1);

        gameControlBar = new GameControlBar();
        gameControlBar.setBackground(UIColor.BACKGROUND);

        reviewPanel.setMinimumSize(new Dimension(0, 0));
        reviewPanel.setMinimumSize(new Dimension(0, 0));
        redoBt.setMinimumSize(new Dimension(0, 0));
        undoPanel.setMinimumSize(new Dimension(0, 0));
        gameInfo.setMinimumSize(new Dimension(0, 0));
        gamePanel.setMinimumSize(new Dimension(0, 0));
        player1Info.setMinimumSize(new Dimension(0, 0));
        player2Info.setMinimumSize(new Dimension(0, 0));
        gameControlBar.setMinimumSize(new Dimension(0, 0));

        this.add(gameInfo,"cell 1 0, grow, sg top");
        this.add(player1Info,"cell 3 0, grow, sg top");
        this.add(player2Info,"cell 3 1, grow, sg top");
        this.add(gamePanel,"cell 0 1, span 3 6, grow");
        this.add(gameControlBar,"cell 3 6, grow, sg top");
        this.add(undoPanel,"cell 0 0,  grow");
        this.add(redoPanel,"cell 2 0, grow");
        this.add(reviewPanel,"cell 3 3, grow, sg top");
        this.add(replayPanel,"cell 3 4, grow, sg top");

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
    }

    public void updateEndGameButtonsVisibility(){
        boolean replayBtnVisible = game.isGameOver() && game.isReviewModeActive() == false;
        boolean reviewBtnVisible = game.isGameOver() && game.isReviewModeActive() == false;

        replayBt.getParent().setVisible(replayBtnVisible);
        reviewBt.getParent().setVisible(reviewBtnVisible);
    }

    public void updateGameInfo(){
        if (game.isGameOver()) {
            if(game.isReviewModeActive()){
                gameInfo.updateMessage(game.getMatch().wonByScore? game.getCurrentPlayerIndex() : game.getOpponentPlayerIndex(), " a joué.");
            }
            else{
                gameInfo.updateMessage(game.getWinningPlayer(), " a gagné la partie.");
            }
        } else {
            gameInfo.updateMessage(game.getCurrentPlayerIndex(), " prépare son coup.");
        }

        updateScore(game.getMatch().getPlayerData());
    }

    public void updateUndoRedoEnabled() {
        //TODO : ajouter les images des boutons grisés
        boolean undoRedoEnabled = game.isGameOver() == false || game.isReviewModeActive();

        undoBt.setEnabled(undoRedoEnabled && game.getMatch().canUndo());
        redoBt.setEnabled(undoRedoEnabled && game.getMatch().canRedo());
    }

    public void updateScore(PlayerData[] playerData) {
        playerInfos.get(0).updateScore(playerData[0]);
        playerInfos.get(1).updateScore(playerData[1]);
    }
}
