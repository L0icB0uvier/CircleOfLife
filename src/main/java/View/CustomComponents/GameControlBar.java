package View.CustomComponents;

import javax.swing.*;
import java.awt.*;


public class GameControlBar extends JPanel {

    public ImageButton saveBt,forfeitBt;

    public GameControlBar(){
        init();
    }

    private void init(){
        this.setLayout(new GridLayout());

        //TODO: ajouter les sprites des bouttons disabled

        saveBt = new ImageButton("saveIcon.png");
        saveBt.setToolTipText("<html><b>Sauvegarder la partie.<b></html>");
        forfeitBt = new ImageButton("logout.png");
        forfeitBt.setToolTipText("<html><b>Abandonner la partie.<b></html>");

        this.add(saveBt);
        this.add(forfeitBt);

    }
}

