package View.Adapter;

import View.CustomComponents.PopUpPanel;
import View.EventCollector;
import View.GraphicalGame;
import View.GraphicalUserInterface;
import View.UserInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopUpAdapter implements ActionListener {
    EventCollector controller;
    JFrame parent;
    String mainText, secondaryText;
    PopUpPanel popup;
    int nButtons;
    public JDialog dialog;

    public PopUpAdapter(JFrame parent, EventCollector controller, int nButtons, String mainText, String secondaryText) {
        this.parent = parent;
        this.controller = controller;
        this.mainText = mainText;
        this.secondaryText = secondaryText;
        this.nButtons = nButtons;

        init();
    }

    private void init() {
        dialog = new JDialog(parent, "", true);
        dialog.setSize(800, 300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);

        popup = new PopUpPanel(controller, dialog, nButtons);
        popup.setMainLabel(mainText);
        popup.setSecondaryLabel(secondaryText);

        dialog.add(popup);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.setVisible(true);
    }

    public void setButtonLabel(int button, String label) {
        popup.setButtonLabel(button, label);
    }

    public void setActionButton(int button, String action, boolean dispose) {
        popup.setActionButton(button, action,dispose);
    }

    public void setActionButton(int button, GraphicalUserInterface gui, JComponent nPage) {
        popup.setActionButton(button, gui, nPage);
    }

    public void setButtonVisibility(int button, boolean b) {
        popup.setButtonVisibility(button, b);
    }


    public void show(){
        dialog.setVisible(true);
    }
}
