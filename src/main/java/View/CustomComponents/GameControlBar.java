package View.CustomComponents;

import javax.swing.*;
import java.awt.*;


public class GameControlBar extends JPanel {
    public ImageButton saveBt, quitGameButton;

    public GameControlBar(){
        init();
    }

    private void init(){
        this.setLayout(new GridLayout());

        //TODO: ajouter les sprites des boutons disabled

        saveBt = new ImageButton("saveIcon.png");
        saveBt.setToolTipText("<html><b>Sauvegarder la partie.<b></html>");
        quitGameButton = new ImageButton("logout.png");
        quitGameButton.setToolTipText("<html><b>Quitter la partie.<b></html>");

        this.add(saveBt);
        this.add(quitGameButton);
    }
}

