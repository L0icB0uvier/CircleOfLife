package View.CustomComponents;

import Global.Configuration;
import Model.PlayerData;
import View.Utils.FontScaler;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;
import View.Utils.UIFont;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
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
        nameConstraints.gridx = 0;
        nameConstraints.gridy = 0;
        nameConstraints.anchor = GridBagConstraints.CENTER;
        nameConstraints.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints scoreConstraints = new GridBagConstraints();
        scoreConstraints.gridx = 0;
        scoreConstraints.gridy = 1;
        scoreConstraints.anchor = GridBagConstraints.CENTER;
        scoreConstraints.fill = GridBagConstraints.HORIZONTAL;

        // Initialisation of all components
        nameLabel = new JLabel();
        nameLabel.setText(this.name);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setVerticalAlignment(JLabel.CENTER);
        nameLabel.setForeground(color);

        scoreLabel = new JLabel();
        scoreLabel.setText("0/20");
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setVerticalAlignment(JLabel.CENTER);
        scoreLabel.setForeground(color);

        Border padding = new EmptyBorder(0,0,RoundedBorder.SHADOW_SIZE_BOTTOM,0);
        scoreLabel.setBorder(padding);

        // Adding components to the layout
        gbl.setConstraints(nameLabel,nameConstraints);
        this.add(nameLabel);
        gbl.setConstraints(scoreLabel,scoreConstraints);
        this.add(scoreLabel);

        this.setBackground(nPlayer == 0 ? UIColor.LIGHT_BLUE : UIColor.LIGHT_RED);
        this.setOpaque(false);
        this.setBorder(new RoundedBorder(15,true));

        this.addComponentListener(new FontScaler(nameLabel,scoreLabel));

    }

    public void updateScore(PlayerData data){
        scoreLabel.setText(String.valueOf(data.getScore()) + "/" + winScore);
        repaint();
    }

}
