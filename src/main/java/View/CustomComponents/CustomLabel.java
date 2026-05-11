package View.CustomComponents;

import Model.Game;
import View.ResizedWindow;
import View.Utils.FontScaler;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;

import javax.swing.*;
import java.awt.*;

public class CustomLabel extends JPanel {
    JLabel playerName;
    JLabel text;
    public CustomLabel(Game game) {
        //TODO : changer le texte en fonction du joueur courant
        playerName = new JLabel("Joueur1");
        text = new JLabel(" prepare son coup...");
        FontScaler fs = new FontScaler();

        playerName.setForeground(UIColor.BLUE);
        playerName.setVerticalAlignment(JLabel.CENTER);
        playerName.setHorizontalAlignment(JLabel.LEFT);
        playerName.addComponentListener(fs);

        text.setForeground(Color.BLACK);
        text.setVerticalAlignment(JLabel.CENTER);
        text.setHorizontalAlignment(JLabel.LEFT);
        text.addComponentListener(fs);

        this.add(playerName);
        this.add(text);
        this.setOpaque(false);
        this.setBackground(UIColor.LIGHT_BLUE);
        this.setBorder(new RoundedBorder(15));


    }
}

