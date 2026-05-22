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
        saveBt = new ImageButton("res/Images/saveIcon.png");
        forfeitBt = new ImageButton("res/Images/logout.png");

        this.add(saveBt);
        this.add(forfeitBt);

    }
}

