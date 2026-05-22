package View.CustomComponents;

import Model.Game;
import View.GraphicalGame;
import View.Utils.FontScaler;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CustomLabel extends JPanel {
    JLabel turn;
    JLabel playerName;
    JLabel text;
    Game game;

    public CustomLabel(Game game) {
        this.setLayout(new GridBagLayout());
        this.game = game;

        turn = new JLabel(String.format("Tour %d - ", 1));
        playerName = new JLabel(game.getMatch().getPlayerData()[0].getName());
        text = new JLabel(" prepare son coup...");

        turn.setForeground(Color.BLACK);
        turn.setVerticalAlignment(JLabel.CENTER);
        turn.setHorizontalAlignment(JLabel.LEFT);

        playerName.setForeground(UIColor.BLUE);
        playerName.setVerticalAlignment(JLabel.CENTER);
        playerName.setHorizontalAlignment(JLabel.LEFT);

        text.setForeground(Color.BLACK);
        text.setVerticalAlignment(JLabel.CENTER);
        text.setHorizontalAlignment(JLabel.LEFT);

        Border padding = new EmptyBorder(0,0,RoundedBorder.SHADOW_SIZE_BOTTOM,0);

        turn.setBorder(padding);
        text.setBorder(padding);
        playerName.setBorder(padding);

        this.add(turn);
        this.add(playerName);
        this.add(text);
        this.setOpaque(false);
        this.setBackground(UIColor.LIGHT_BLUE);
        setBorder(new RoundedBorder(15,true));

        this.addComponentListener(new FontScaler(turn, text, playerName));
    }

    public void updateMessage(int currentPlayer, String message){
        updatePlayerTurn(currentPlayer);
        text.setText(message);
    }

    public void updatePlayerTurn(int currentPlayer){
        turn.setText(String.format("Tour %d - ", game.getMatch().getPastCount() + 1));
        playerName.setText(currentPlayer == 0? game.getMatch().getPlayerData()[0].getName() : game.getMatch().getPlayerData()[1].getName());
        playerName.setForeground(currentPlayer == 0? UIColor.BLUE : UIColor.RED);
        this.setBackground(currentPlayer == 0 ? UIColor.LIGHT_BLUE : UIColor.LIGHT_RED);
    }
}

