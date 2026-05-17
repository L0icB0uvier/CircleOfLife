package View.Adapter;

import Global.Configuration;
import View.GraphicalUserInterface;
import View.Utils.ChoiceBox;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ChoiceButtonAdapter implements java.awt.event.ActionListener {
    ChoiceBox choiceBox;
    int direction;

    public ChoiceButtonAdapter(ChoiceBox choiceBox, int direction) {
        this.choiceBox = choiceBox;
        this.direction = direction;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        this.choiceBox.changeLabel(this.direction);
    }
}
