package View;

import Global.Configuration;
import Model.Game;
import Model.PlayerData;
import View.CustomComponents.*;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphicalGame extends JPanel {
    GamePanel gamePanel;
    ArrayList<PlayerInfo> playerInfos;
    GameControlBar gameControlBar;
    CustomLabel gameInfo;

    public JButton undoBt,redoBt;
    Game game;

    public GraphicalGame(Game game){
        this.game=game;
        playerInfos = new ArrayList<>();

        MigLayout layout = new MigLayout("fill, insets 1.5% 1.5% 1.5% 1.5%", "[grow][60%][grow][15%]","[10%][10%][grow][10%]" );
        this.setLayout(layout);
        this.setBackground(UIColor.BACKGROUND);

        undoBt = new ImageButton("Undo.png", null);
        JPanel undoPanel = new JPanel();
        undoPanel.setOpaque(false);
        undoPanel.setLayout(new GridLayout());
        undoPanel.add(undoBt);
        redoBt = new ImageButton("Redo.png", null);
        JPanel redoPanel = new JPanel();
        redoPanel.setOpaque(false);
        redoPanel.setLayout(new GridLayout());
        redoPanel.add(redoBt);

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
        this.add(gamePanel,"cell 0 1, span 3 3, grow");
        this.add(gameControlBar,"cell 3 3, grow, sg top, sg button");
        this.add(undoPanel,"cell 0 0,  grow");
        this.add(redoPanel,"cell 2 0, grow");

        this.setVisible(true);

        updateScore(game.getMatch().getPlayerData());
    }

    public GamePanel getGamePanel() {
        return gamePanel;
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

    public void updateScore(PlayerData[] playerData) {
        playerInfos.get(0).updateScore(playerData[0]);
        playerInfos.get(1).updateScore(playerData[1]);
    }
}
