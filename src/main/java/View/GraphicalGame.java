package View;

import Model.Game;
import Model.PlayerData;
import View.CustomComponents.GameControlBar;
import View.CustomComponents.GameInfo;
import View.CustomComponents.GamePanel;
import View.Utils.UIColor;

import javax.swing.*;
import java.awt.*;

public class GraphicalGame extends JPanel {
    GamePanel gamePanel;
    GameInfo gameInfo;
    GameControlBar gameControlBar;
    Game game;
    public GraphicalGame(Game game){
        this.game=game;
        GridBagLayout gbl = new GridBagLayout();

        this.setLayout(gbl);

        gamePanel = new GamePanel(game);
        GridBagConstraints graphicalLevelConstraints = new GridBagConstraints();
        graphicalLevelConstraints.gridy = 1;
        graphicalLevelConstraints.weighty=80;
        graphicalLevelConstraints.weightx=1;
        graphicalLevelConstraints.fill= GridBagConstraints.BOTH;

        gameInfo = new GameInfo();
        GridBagConstraints gameInfoConstraints = new GridBagConstraints();
        gameInfoConstraints.gridy = 0;
        gameInfoConstraints.weighty=10;
        gameInfoConstraints.weightx=1;
        gameInfoConstraints.fill = GridBagConstraints.BOTH;

        gameControlBar = new GameControlBar();
        GridBagConstraints gameControlBarConstraints = new GridBagConstraints();
        gameControlBarConstraints.gridy = 2;
        gameControlBarConstraints.weighty= 10;
        gameControlBarConstraints.weightx= 1;
        gameControlBarConstraints.fill= GridBagConstraints.BOTH;

        gameControlBar.setBackground(UIColor.getColor(UIColor.WHITE));

        gbl.setConstraints(gameInfo,gameInfoConstraints);
        this.add(gameInfo);
        gbl.setConstraints(gamePanel,graphicalLevelConstraints);
        this.add(gamePanel);
        gbl.setConstraints(gameControlBar,gameControlBarConstraints);
        this.add(gameControlBar);
        this.setVisible(true);

        updateScore(game.getMatch().getPlayerData());

        gameControlBar.previewBt.addActionListener(e -> gamePanel.togglePreview());
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void playerTurn(int nPlayer){

        gameInfo.playerTurn(nPlayer);
        Color c;
        if(nPlayer == 0){
            c = UIColor.getColor(UIColor.BLUE);
        }else{
            c = UIColor.getColor(UIColor.RED);
        }
        gamePanel.setBorder(BorderFactory.createMatteBorder(7,7,7,7,c));
        repaint();

    }

    public void updateScore(PlayerData[] playerData) {
        gameInfo.updateScore(playerData);
    }

}
