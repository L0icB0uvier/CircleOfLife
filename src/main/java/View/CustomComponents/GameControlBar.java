package View.CustomComponents;

import View.Utils.UIColor;

import javax.swing.*;
import java.awt.*;


public class GameControlBar extends JPanel {

    public ImageButton saveBt, previewBt,undoBt,redoBt,forfeitBt,leaveBt;

    public GameControlBar(){
        init();
    }

    private void init(){
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);

        //TODO: ajouter les sprites des bouttons disabled

        // Initialisation of all components
        saveBt = new ImageButton("res/Images/Sauvegarde.png", null);
        undoBt = new ImageButton("res/Images/Undo.png", null);
        redoBt = new ImageButton("res/Images/Redo.png", null);
        forfeitBt = new ImageButton("res/Images/Abandonner.png", null);

        // GridBagConstraints for all components
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.anchor = GridBagConstraints.CENTER;
        buttonConstraints.gridx = 0;
        buttonConstraints.weighty=1;
        buttonConstraints.weightx=1;
        buttonConstraints.fill= GridBagConstraints.BOTH;


        // Adding components to the layout
        gbl.setConstraints(saveBt,buttonConstraints);
        this.add(saveBt);

        buttonConstraints.gridx ++;
        gbl.setConstraints(undoBt,buttonConstraints);
        this.add(undoBt);

        buttonConstraints.gridx ++;
        gbl.setConstraints(redoBt,buttonConstraints);
        this.add(redoBt);

        buttonConstraints.gridx ++;
        gbl.setConstraints(forfeitBt,buttonConstraints);
        this.add(forfeitBt);

    }
}

