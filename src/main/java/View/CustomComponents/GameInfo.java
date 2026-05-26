package View.CustomComponents;

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

public class GameInfo extends JPanel {
    JLabel turn;
    JLabel playerName;
    JLabel mainMessage;
    JLabel winMessage;
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

        this.addComponentListener(new FontScaler(turn, mainMessage, playerName, winMessage));
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
            }
            else{
                updatePlayerTurn(playerName, game.getWinningPlayer());
            }
        }
        else {
            updatePlayerTurn(playerName, game.getCurrentPlayerIndex());
        }

        repaint();
    }

    private void updateMessage(){
        if(game.isGameOver()){
            if(game.isReviewModeActive()){
                if(game.canUndo()){
                    Move lastMove = game.getMatch().getLastMove();
                    String and = game.canRedo()? "" : " et ";
                    mainMessage.setText(String.format(" joue en %s%s", new Coordinate(lastMove.getColumn(), lastMove.getLine()), and));
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
            if(game.getWinningPlayer() != game.getOpponentPlayerIndex()){
                winMessage.setText(String.format("perd %s", getWinTypeString()));
            }
            else{
                winMessage.setText(String.format("gagne %s", getWinTypeString()));
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

