package View.Adapter;

import View.CustomComponents.ChoiceBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

public class OptionalVisibilityAdapter implements java.awt.event.ActionListener {
    JComponent defaultComp;
    ChoiceBox optionalComp;
    JLabel defaultLabel, optionalLabel;
    ChoiceBox choiceBox;
    List<String> defaultValues;
    int direction;

    public OptionalVisibilityAdapter(JLabel defaultLabel, ChoiceBox optionalComp, JComponent defaultComp, JLabel optionalLabel, ChoiceBox choiceBox, int dir, String... defaultValues) {
        this.defaultLabel = defaultLabel;
        this.optionalComp = optionalComp;
        this.defaultComp = defaultComp;
        this.optionalLabel = optionalLabel;
        this.choiceBox = choiceBox;
        this.direction = dir;
        this.defaultValues = Arrays.stream(defaultValues).toList();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        choiceBox.changeLabel(direction);
        boolean showOptional = defaultValues.contains(choiceBox.getValue());
        choiceBox.changeLabel(-direction);
        this.optionalComp.setVisible(!showOptional);
        this.optionalLabel.setVisible(!showOptional);
        if(defaultLabel == null) return;
        this.defaultLabel.setVisible(showOptional);
        if(defaultComp == null) return;
        this.defaultComp.setVisible(showOptional);
        if(showOptional && defaultComp instanceof JTextField) {
            ((JTextField) defaultComp).setText("");
       }
    }
}
