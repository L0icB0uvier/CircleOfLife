package View.Adapter;

import Global.Configuration;
import View.EventCollector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlButtonAdapter implements ActionListener {
    EventCollector controller;
    String action;

    public ControlButtonAdapter(EventCollector controller, String action){
        this.controller = controller;
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Configuration.info(action);
        controller.performAction(action);
    }
}
