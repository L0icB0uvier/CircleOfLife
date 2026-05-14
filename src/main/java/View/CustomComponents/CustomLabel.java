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
    Game game;

    public CustomLabel(Game game) {
        this.setLayout(new GridBagLayout());
        //TODO : changer le texte en fonction du joueur courant
        this.game = game;
        playerName = new JLabel(game.getMatch().getPlayerData()[0].getName());
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

    public void updatePlayerTurn(int currentPlayer){
        playerName.setText(currentPlayer == 0? game.getMatch().getPlayerData()[0].getName() : game.getMatch().getPlayerData()[1].getName());
        playerName.setForeground(currentPlayer == 0? UIColor.BLUE : UIColor.RED);
    }

    public void gameOver(int winner){
        text.setText(" a gagné la partie");
    }
}

