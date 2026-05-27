package View.CustomComponents;

import Global.Configuration;
import Model.Coordinate;
import Model.Game;
import Model.Move;
import Model.WinType;
import View.Utils.FontScaler;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.io.ObjectInputFilter;

public class GameInfo extends JPanel {
    JLabel turn;
    JLabel playerName;
    JLabel mainMessage;
    JLabel winMessage;
    JLabel textLabel;
    Game game;

    public GameInfo(Game game) {
        this.setLayout(new GridBagLayout());
        this.game = game;

        turn = new JLabel(String.format("Tour %d - ", 1));
        playerName = new JLabel(game.getMatch().getPlayerData()[0].getName());
        mainMessage = new JLabel(" prepare son coup...");
        winMessage = new JLabel();

        turn.setForeground(Color.BLACK);
        turn.setVerticalAlignment(JLabel.CENTER);
        turn.setHorizontalAlignment(JLabel.LEFT);

        playerName.setForeground(UIColor.BLUE);
        playerName.setVerticalAlignment(JLabel.CENTER);
        playerName.setHorizontalAlignment(JLabel.LEFT);

        mainMessage.setForeground(Color.BLACK);
        mainMessage.setVerticalAlignment(JLabel.CENTER);
        mainMessage.setHorizontalAlignment(JLabel.LEFT);

        winMessage.setForeground(Color.BLACK);
        winMessage.setVerticalAlignment(JLabel.CENTER);
        winMessage.setHorizontalAlignment(JLabel.LEFT);

        Border padding = new EmptyBorder(0,0,RoundedBorder.SHADOW_SIZE_BOTTOM,0);

        turn.setBorder(padding);
        mainMessage.setBorder(padding);
        playerName.setBorder(padding);
        winMessage.setBorder(padding);

        this.add(turn);
        this.add(playerName);
        this.add(mainMessage);
        this.setOpaque(false);
        this.setBackground(UIColor.LIGHT_BLUE);
        setBorder(new RoundedBorder(15,true));

        textLabel = new JLabel(turn.getText() + playerName.getText() + mainMessage.getText() + winMessage.getText());
        this.add(textLabel);
        textLabel.setVisible(false);

        this.addComponentListener(new FontScaler(0.4f, turn, mainMessage, playerName, winMessage, textLabel));
    }

    public void update(){
        updateTurn();
        updateMessage();
        updateWinMessage();

        if(game.isGameOver()){
            if(game.isReviewModeActive()){
                if(game.canUndo()){
                    updatePlayerTurn(playerName, game.getMatch().winType == WinType.SCORE ? game.getCurrentPlayerIndex() : game.getOpponentPlayerIndex());
                }
                else{
                    updatePlayerTurn(playerName, game.getMatch().winType == WinType.SCORE ? game.getOpponentPlayerIndex() : game.getCurrentPlayerIndex());
                }
                textLabel.setText(turn.getText() + playerName.getText() + mainMessage.getText() + (!game.canRedo() ? winMessage.getText(): ""));
            }
            else{
                updatePlayerTurn(playerName, game.getWinningPlayer());
                textLabel.setText(turn.getText() + playerName.getText() + mainMessage.getText() + winMessage.getText());
            }
        }
        else {
            updatePlayerTurn(playerName, game.getCurrentPlayerIndex());
            textLabel.setText(turn.getText() + playerName.getText() + mainMessage.getText());
        }
        Configuration.info(textLabel.getText());
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
        repaint();
    }

    private void updateMessage(){
        if(game.isGameOver()){
            if(game.isReviewModeActive()){
                if(game.canUndo()){
                    Move lastMove = game.getMatch().getLastMove();
                    mainMessage.setText(String.format(" joue en %s", new Coordinate(lastMove.getColumn(), lastMove.getLine())));
                }
                else
                    mainMessage.setText(" a commencé");
            }
            else{
                String winType = getWinTypeString();
                mainMessage.setText(String.format(" gagne %s", winType));
            }
        }
        else{
            mainMessage.setText(" prépare son coup");
        }
    }

    private void updateWinMessage() {
        if(game.isGameOver() && game.isReviewModeActive() && game.canRedo() == false){
            switch (game.getMatch().winType){
                case SCORE -> {
                    winMessage.setText(String.format(" et gagne %s", getWinTypeString()));
                }
                case FILL -> {
                    if(game.getWinningPlayer() != game.getOpponentPlayerIndex()){
                        winMessage.setText(String.format(" et perd %s", getWinTypeString()));
                    }
                    else{
                        winMessage.setText(String.format(" et gagne %s", getWinTypeString()));
                    }
                }
                case GIVE_UP ->{
                    if(game.canUndo() == false){
                        winMessage.setText(String.format(" et perd %s", getWinTypeString()));
                    }
                    else{
                        if(game.getWinningPlayer() != game.getOpponentPlayerIndex()){
                            winMessage.setText(String.format(" et perd %s", getWinTypeString()));
                        }
                        else{
                            winMessage.setText(String.format(" et gagne %s", getWinTypeString()));
                        }
                    }
                }
            }

            this.add(winMessage);
        }
        else{
            this.remove(winMessage);
        }
    }

    private String getWinTypeString() {
        String winType;
        switch (game.getMatch().winType){
            case SCORE -> winType = "au score";
            case FILL -> winType = "par remplissage";
            case GIVE_UP -> winType = "par abandon";
            default -> winType = "";
        }
        return winType;
    }

    private void updateTurn(){
        if(game.isGameOver() && game.isReviewModeActive())
            turn.setVisible(game.canUndo());

        turn.setText(String.format("Tour %d - ", game.getMatch().isGameOver()?  game.getMatch().getPastCount() : game.getMatch().getPastCount() + 1));
    }

    public void updatePlayerTurn(JLabel playerLabel, int currentPlayer){
        playerLabel.setText(currentPlayer == 0? game.getMatch().getPlayerData()[0].getName() : game.getMatch().getPlayerData()[1].getName());
        playerLabel.setForeground(currentPlayer == 0? UIColor.BLUE : UIColor.RED);
        this.setBackground(currentPlayer == 0 ? UIColor.LIGHT_BLUE : UIColor.LIGHT_RED);
    }
}

