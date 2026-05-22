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

        // Initialisation of all components
        saveBt = new ImageButton("Sauvegarde.png", null);
        forfeitBt = new ImageButton("Abandonner.png", null);

        this.add(saveBt);
        this.add(forfeitBt);

    }
}

