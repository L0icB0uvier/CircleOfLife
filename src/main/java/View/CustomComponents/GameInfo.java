package View.CustomComponents;

import Model.PlayerData;
import View.Utils.UIColor;

import javax.swing.*;
import java.awt.*;


public class GameInfo extends JPanel {
    private PlayerInfo player1;
    private PlayerInfo player2;

    public GameInfo(){
        init();
    }

    private void init(){
        GridBagLayout gbl = new GridBagLayout();

        this.setLayout(gbl);

        GridBagConstraints player1Constraints = new GridBagConstraints();
        player1Constraints.gridx = 0;
        player1Constraints.weighty=1;
        player1Constraints.weightx=50;
        player1Constraints.fill= GridBagConstraints.BOTH;
        player1 = new PlayerInfo("Joueur1",0);

        GridBagConstraints player2Constraints = new GridBagConstraints();
        player2Constraints.gridx = 1;
        player2Constraints.weighty=1;
        player2Constraints.weightx=50;
        player2Constraints.fill= GridBagConstraints.BOTH;
        player2 = new PlayerInfo("Joueur2",1);

        gbl.setConstraints(player1,player1Constraints);
        this.add(player1);
        gbl.setConstraints(player2,player2Constraints);
        this.add(player2);

    }

    public void playerTurn(int nPlayer){
        Color c;
        if(nPlayer == 0){
            c = UIColor.getColor(UIColor.BLUE);
            player1.setBorder(BorderFactory.createMatteBorder(7,7,0,7,c));
            player2.setBorder(BorderFactory.createEmptyBorder(7,7,0,7));
        }else{
            c = UIColor.getColor(UIColor.RED);
            player1.setBorder(BorderFactory.createEmptyBorder(7,7,0,7));
            player2.setBorder(BorderFactory.createMatteBorder(7,7,0,7,c));
        }
        repaint();
    }

    public void updateScore(PlayerData[] playerData) {
        player1.setScore(playerData[0].getScore());
        player2.setScore(playerData[1].getScore());
    }


}
