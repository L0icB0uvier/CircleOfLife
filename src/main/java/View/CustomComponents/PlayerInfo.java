package View.CustomComponents;

import View.Utils.UIColor;
import View.Utils.UIFont;

import javax.swing.*;
import java.awt.*;

public class PlayerInfo extends JPanel {

    private final String name;
    int nPlayer;
    Color color;
    JLabel scoreLabel, nameLabel;

    PlayerInfo(String name, int nPlayer){
        this.name = name;
        this.nPlayer = nPlayer;
        init();
    }

    private void init(){
        Font sizedFont = UIFont.getFont();

        Color color = UIColor.getColor(nPlayer ==  0 ? UIColor.BLUE:UIColor.RED);
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);

        // GridBagConstraints for all components
        GridBagConstraints nameConstraints = new GridBagConstraints();
        nameConstraints.gridx = nPlayer;
        nameConstraints.weighty=1;
        nameConstraints.weightx=95;
        nameConstraints.fill= GridBagConstraints.BOTH;

        GridBagConstraints scoreConstraints = new GridBagConstraints();
        scoreConstraints.gridx = 1 - nPlayer;
        scoreConstraints.weighty=1;
        scoreConstraints.weightx=5;
        scoreConstraints.fill= GridBagConstraints.BOTH;

        // Initialisation of all components
        nameLabel = new JLabel();
        nameLabel.setText(this.name);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setForeground(color);
        nameLabel.setFont(sizedFont);

        scoreLabel = new JLabel();
        scoreLabel.setText("0");
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setForeground(color);
        scoreLabel.setFont(sizedFont);


        // Adding components to the layout
        gbl.setConstraints(nameLabel,nameConstraints);
        this.add(nameLabel);
        gbl.setConstraints(scoreLabel,scoreConstraints);
        this.add(scoreLabel);

        this.setBackground(UIColor.getColor(nPlayer == 0 ? UIColor.LIGHT_BLUE : UIColor.LIGHT_RED));

    }
    @Override
    public void doLayout() {
        super.doLayout();
        float fontSize = nameLabel.getHeight()>0?nameLabel.getHeight() * 0.4f:22;
        nameLabel.setFont(nameLabel.getFont().deriveFont(fontSize));
        scoreLabel.setFont(scoreLabel.getFont().deriveFont(fontSize));
        super.doLayout();
    }

    public void setScore(int score){
        scoreLabel.setText(String.valueOf(score));
        repaint();
    }

}
