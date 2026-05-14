package View.Adapter;

import Global.Configuration;
import View.EventCollector;
import View.GraphicalUserInterface;

import java.awt.event.ActionEvent;

public class NewGameAdapter implements java.awt.event.ActionListener {
    EventCollector controller;

    public NewGameAdapter(EventCollector controller){
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Configuration.info("Creating a new game");
        controller.performAction("NewGame");
    }
}
