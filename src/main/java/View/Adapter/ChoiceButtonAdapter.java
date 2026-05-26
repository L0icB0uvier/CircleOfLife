package View.Adapter;

import View.CustomComponents.ChoiceBox;

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
