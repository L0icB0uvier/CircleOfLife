package View.Adapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ToggleButtonAdapter implements java.awt.event.ActionListener {
    ButtonGroup buttonGroup;
    JComboBox<String> comboBox;
    ButtonModel model;
    Box box;
    Component rigid;

    public ToggleButtonAdapter(ButtonGroup buttonGroup, ButtonModel model, JComboBox<String> comboBox, Box box){
        this.buttonGroup = buttonGroup;
        this.comboBox = comboBox;
        this.model = model;
        this.box = box;
        rigid = Box.createRigidArea(new Dimension(comboBox.getWidth(), comboBox.getHeight()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.comboBox.setVisible(buttonGroup.getSelection() == model);
    }
}