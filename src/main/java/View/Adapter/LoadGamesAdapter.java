package View.Adapter;

import Global.Configuration;
import View.GraphicalUserInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LoadGamesAdapter implements java.awt.event.ActionListener {
    GraphicalUserInterface userInterface;

    public LoadGamesAdapter(GraphicalUserInterface userInterface){
        this.userInterface = userInterface;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         
        userInterface.startLoadPage();
    }
}
