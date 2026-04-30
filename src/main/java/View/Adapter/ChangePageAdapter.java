package View.Adapter;

import Global.Configuration;
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
        Configuration.info("Changement de page vers " + newPage.getClass());
        userInterface.getFrame().setContentPane(newPage);
        userInterface.getFrame().revalidate();
    }
}