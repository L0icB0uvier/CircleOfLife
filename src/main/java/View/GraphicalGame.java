package View;

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
    CustomLabel gameInfo;
    public GameHistory gameHistory;
    GameControlBar gameControlBar;
    Game game;

    public GraphicalGame(Game game){
        this.game=game;
        playerInfos = new ArrayList<>();

        MigLayout layout = new MigLayout("fill, insets 10 10 10 10, debug", "[70%][30%]","[10%][10%][70%][10%]" );
        this.setLayout(layout);
        this.setBackground(UIColor.BACKGROUND);

        gameInfo = new CustomLabel();

        gamePanel = new GamePanel(game);
        gamePanel.recalculate();
        //JPanel p = new JPanel();
        //p.setBorder(new RoundedBorder(15,Color.BLACK,3));

        playerInfos.add(new PlayerInfo("Player1",0));
        PlayerInfo player1Info = playerInfos.get(0);
        playerInfos.add(new PlayerInfo("Player2",1));
        PlayerInfo player2Info = playerInfos.get(1);

        gameHistory = new GameHistory(game);
        gameHistory.setBackground(Color.WHITE);
        gameHistory.setOpaque(false);
        //gameHistory.setBorder(new RoundedBorder(15, Color.BLACK,3));

        gameControlBar = new GameControlBar();
        gameControlBar.setBackground(UIColor.BACKGROUND);

        gameInfo.setMinimumSize(new Dimension(0, 0));
        gamePanel.setMinimumSize(new Dimension(0, 0));
        player1Info.setMinimumSize(new Dimension(0, 0));
        player2Info.setMinimumSize(new Dimension(0, 0));
        gameHistory.setMinimumSize(new Dimension(0, 0));
        gameControlBar.setMinimumSize(new Dimension(0, 0));

        this.add(gameInfo,"cell 0 0, grow, sg top");
        this.add(player1Info,"cell 1 0, grow, sg top");
        this.add(player2Info,"cell 1 1, grow, sg top");
        this.add(gamePanel,"cell 0 1, span 1 3, grow");
        this.add(gameHistory,"cell 1 2, grow");
        this.add(gameControlBar,"cell 1 3, grow, sg top");

        this.setVisible(true);

        updateScore(game.getMatch().getPlayerData());
    }



    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void playerTurn(int nPlayer){
        //TODO: changer playerInfo et gameInfo
        gameInfo.updatePlayerTurn(nPlayer);
        //PlayerInfo.playerTurn(nPlayer);
    }

    public void updateScore(PlayerData[] playerData) {
        playerInfos.get(0).updateScore(playerData[0]);
        playerInfos.get(1).updateScore(playerData[1]);
    }

    public void gameOver(){
        gameInfo.gameOver(0);
    }

}
