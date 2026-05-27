package View.Adapter;

import Global.Configuration;
import View.GraphicalGame;
import View.GraphicalUserInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ChangePageAdapter implements java.awt.event.ActionListener {
    GraphicalUserInterface userInterface;
    JComponent newPage;

    public ChangePageAdapter(GraphicalUserInterface userInterface, JComponent newPage){
        this.userInterface = userInterface;
        this.newPage = newPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         

        if(newPage.getClass() != GraphicalGame.class)
            userInterface.stopGameAnimationTimer();
        else
            userInterface.startGameAnimationTimer();

        userInterface.getFrame().setContentPane(newPage);
        userInterface.getFrame().revalidate();
    }
}