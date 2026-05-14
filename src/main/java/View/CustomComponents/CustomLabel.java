package View.CustomComponents;

import Model.Game;
import View.GraphicalGame;
import View.Utils.FontScaler;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;

import javax.swing.*;
import java.awt.*;

public class CustomLabel extends JPanel {
    JLabel playerName;
    JLabel text;

    public CustomLabel() {
        this.setLayout(new GridBagLayout());
        //TODO : changer le texte en fonction du joueur courant
        playerName = new JLabel("Player1");
        text = new JLabel(" prepare son coup...");

        playerName.setForeground(UIColor.BLUE);
        playerName.setVerticalAlignment(JLabel.CENTER);
        playerName.setHorizontalAlignment(JLabel.LEFT);
        text.setForeground(Color.BLACK);
        text.setVerticalAlignment(JLabel.CENTER);
        text.setHorizontalAlignment(JLabel.LEFT);

        this.add(playerName);
        this.add(text);
        this.setOpaque(false);
        this.setBackground(UIColor.LIGHT_BLUE);
        this.setBorder(new RoundedBorder(15));

        this.addComponentListener(new FontScaler(text,playerName));
    }
    public CustomLabel(int currentPlayer,String msg) {
        this.setLayout(new GridBagLayout());
        //TODO : changer le texte en fonction du joueur courant
        String label = (currentPlayer == 0 ? "Player1 ":"Player2 ");
        playerName = new JLabel(label);
        text = new JLabel(msg);
        Color color = (currentPlayer == 0 ? UIColor.BLUE:UIColor.RED);
        playerName.setForeground(color);
        playerName.setVerticalAlignment(JLabel.CENTER);
        playerName.setHorizontalAlignment(JLabel.LEFT);
        text.setForeground(Color.BLACK);
        text.setVerticalAlignment(JLabel.CENTER);
        text.setHorizontalAlignment(JLabel.LEFT);

        this.add(playerName);
        this.add(text);
        this.setOpaque(false);
        this.setBackground(UIColor.LIGHT_BLUE);
        this.setBorder(new RoundedBorder(15));

        this.addComponentListener(new FontScaler(text,playerName));
    }



    public void updatePlayerTurn(int currentPlayer){
        playerName.setText(currentPlayer == 0? "Player1" : "Player2");
        playerName.setForeground(currentPlayer == 0? UIColor.BLUE : UIColor.RED);
    }

    public void gameOver(int winner){
        text.setText(" a gagné la partie");
    }
}

