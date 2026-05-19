package View.CustomComponents;

import Global.Configuration;
import Model.PlayerData;
import View.Utils.FontScaler;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;
import View.Utils.UIFont;

import javax.swing.*;
import java.awt.*;

public class PlayerInfo extends JPanel {

    private final String name;
    int nPlayer;
    JLabel scoreLabel, nameLabel;
    private int winScore;

    public PlayerInfo(String name, int nPlayer){
        this.name = name;
        this.nPlayer = nPlayer;
        init();
    }

    private void init(){
        Font sizedFont = UIFont.getFont();
        //TODO modifier la font du projet

        winScore = Configuration.readInt("WinScore");
        Color color = nPlayer ==  0 ? UIColor.BLUE:UIColor.RED;
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);

        // GridBagConstraints for all components
        GridBagConstraints nameConstraints = new GridBagConstraints();
        nameConstraints.weighty=0.1;
        nameConstraints.weightx=0.1;
        nameConstraints.insets = new Insets(0,20,0,0);
        nameConstraints.fill= GridBagConstraints.BOTH;
        nameConstraints.anchor = GridBagConstraints.WEST;

        GridBagConstraints scoreConstraints = new GridBagConstraints();
        scoreConstraints.weighty=0.1;
        scoreConstraints.weightx=0.1;
        scoreConstraints.insets = new Insets(0,0,0,20);
        scoreConstraints.fill= GridBagConstraints.BOTH;
        scoreConstraints.anchor = GridBagConstraints.EAST;


        // Initialisation of all components
        nameLabel = new JLabel();
        nameLabel.setText(this.name);
        nameLabel.setHorizontalAlignment(JLabel.LEFT);
        nameLabel.setVerticalAlignment(JLabel.CENTER);
        nameLabel.setForeground(color);

        scoreLabel = new JLabel();
        scoreLabel.setText("0/20");
        scoreLabel.setHorizontalAlignment(JLabel.RIGHT);
        nameLabel.setVerticalAlignment(JLabel.CENTER);
        scoreLabel.setForeground(color);

        // Adding components to the layout
        gbl.setConstraints(nameLabel,nameConstraints);
        this.add(nameLabel);
        gbl.setConstraints(scoreLabel,scoreConstraints);
        this.add(scoreLabel);

        this.setBackground(nPlayer == 0 ? UIColor.LIGHT_BLUE : UIColor.LIGHT_RED);
        this.setOpaque(false);
        this.setBorder(new RoundedBorder(15));

        this.addComponentListener(new FontScaler(nameLabel,scoreLabel));

    }

    public void updateScore(PlayerData data){
        scoreLabel.setText(String.valueOf(data.getScore()) + "/" + winScore);
        repaint();
    }

}
