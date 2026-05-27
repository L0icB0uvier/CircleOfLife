package View.CustomComponents;

import javax.swing.JDialog;

import java.awt.Component;
import java.awt.event.ActionListener;

public class ErrorPopUpPanel {
    PopUpPanel popUpPanel;

    public ErrorPopUpPanel(String string, Component c) {
        JDialog jDialog = new JDialog();
        jDialog.setSize(800, 300);
        jDialog.setResizable(false);
        jDialog.setLocationRelativeTo(c);
        PopUpPanel popUpPanel = new PopUpPanel(null, jDialog, 1);
        popUpPanel.setMainLabel(string);
        popUpPanel.setButtonLabel(0, "Ok");
        popUpPanel.setActionButton(0, (ActionListener)null, true);
        jDialog.add(popUpPanel);
        jDialog.setVisible(true);
    }
}
