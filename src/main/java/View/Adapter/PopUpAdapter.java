package View.Adapter;

import View.CustomComponents.PopUpPanel;
import View.EventCollector;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopUpAdapter implements ActionListener {
    EventCollector controller;
    JFrame parent;
    String mainText,secondaryText,leftButtonText,rightButtonText;
    PopUpPanel popup;
    public JDialog dialog;

    public PopUpAdapter(JFrame parent,EventCollector controller, String mainText,String secondaryText,String leftButtonText,String rightButtonText){
        this.parent = parent;
        this.controller = controller;
        this.mainText = mainText;
        this.secondaryText = secondaryText;
        this.leftButtonText = leftButtonText;
        this.rightButtonText = rightButtonText;
        init();
    }
    private void init(){
        dialog = new JDialog(parent,"",true);
        dialog.setSize(800, 300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        popup = new PopUpPanel(controller,dialog);
        popup.setLeftButton(leftButtonText);
        popup.setRightButton(rightButtonText);
        popup.setMainLabel(mainText);
        popup.setSecondaryLabel(secondaryText);

        dialog.add(popup);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.setVisible(true);
    }

    public void setActionLeftButton(String action){
        popup.setActionLeftButton(action);
    }

    public void setActionRightButton(String action){
        popup.setActionRightButton(action);
    }
    public void setActionRightButton(ActionListener al){
        popup.setActionRightButton(al);
    }

    public void setActionMiddleButton(String action, String label){
        popup.setMiddleButton(label);
        popup.setActionMiddleButton(action);
    }
}
