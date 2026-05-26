package View.CustomComponents;

import Model.Game;

import javax.swing.*;
import java.awt.*;


public class GameControlBar extends JPanel {
    private final Game game;
    public ImageButton saveBt, forfeitBt, quitGameButton;

    public GameControlBar(Game game){
        this.game = game;
        init();
    }

    private void init(){
        this.setLayout(new GridLayout());

        //TODO: ajouter les sprites des bouttons disabled

        saveBt = new ImageButton("saveIcon.png");
        saveBt.setToolTipText("<html><b>Sauvegarder la partie.<b></html>");
        quitGameButton = new ImageButton("logout.png");
        quitGameButton.setToolTipText("<html><b>Quitter la partie.<b></html>");

        this.add(saveBt);
        this.add(quitGameButton);
    }
}

