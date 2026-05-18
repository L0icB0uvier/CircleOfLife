package View.CustomComponents;

import View.Adapter.ChangePageAdapter;
import View.EventCollector;
import View.GraphicalUserInterface;
import View.Utils.UIColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class PopUpPanel extends JPanel {

    private JLabel mainLabel;
    private JLabel secondaryLabel;
    private JButton leftButton;
    private JButton rightButton;
    private JButton middleButton;
    private EventCollector controller;

    private Dialog dialog;

    public PopUpPanel(EventCollector controller, JDialog dialog){

        this.controller = controller;
        this.dialog = dialog;
        init();
    }

    private void init(){
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);

        // Initialisation of all components
        rightButton = new JButton();
        leftButton = new JButton();
        middleButton = new JButton();

        JPanel buttonContainer = new JPanel();

        mainLabel   = new JLabel();
        secondaryLabel = new JLabel();

        mainLabel.setFont(getFont().deriveFont(30f));
        secondaryLabel.setFont(getFont().deriveFont(30f));
        rightButton.setFont(getFont().deriveFont(15f));
        leftButton.setFont(getFont().deriveFont(15f));
        middleButton.setFont(getFont().deriveFont(15f));

        //Adding Buttons to the ButtonContainer
        buttonContainer.setLayout(new GridLayout(1,3,20,20));
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonContainer.add(leftButton);
        buttonContainer.add(middleButton);
        buttonContainer.add(rightButton);

        middleButton.setVisible(false);

        // Adding components to the layout
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridy = 0;
        labelConstraints.weighty=50;
        labelConstraints.weightx=1;
        labelConstraints.fill= GridBagConstraints.BOTH;
        gbl.setConstraints(mainLabel,labelConstraints);
        this.add(mainLabel);

        labelConstraints.gridy++;
        labelConstraints.weighty=40;
        gbl.setConstraints(secondaryLabel,labelConstraints);
        this.add(secondaryLabel);

        labelConstraints.gridy ++;
        labelConstraints.weighty=10;
        gbl.setConstraints(buttonContainer,labelConstraints);
        this.add(buttonContainer);
    }

    public void setLeftButton(String text) {
        this.leftButton.setText(text);
    }


    public void setMainLabel(String mainLabel) {
        this.mainLabel.setText("<html><div style='text-align:start;width:610px;padding-left :10px;padding-right :10px'>" + mainLabel + "</div></html>");
    }

    public void setRightButton(String text) {
        this.rightButton.setText(text);

    }

    public void setMiddleButton(String text) {
        middleButton.setVisible(true);
        this.middleButton.setText(text);
    }

    public void setSecondaryLabel(String secondaryLabel) {
        this.secondaryLabel.setText("<html><div style='text-align:start;width:610px;padding-left :10px;padding-right :10px'>" + secondaryLabel + "</div></html>");
        this.secondaryLabel.setForeground(UIColor.RED);
        this.secondaryLabel.setVerticalAlignment(SwingConstants.NORTH);
    }

    public void setActionLeftButton(String action){
            leftButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                    if (!Objects.equals(action, "Annuler")) {
                        controller.performAction(action);
                    }
                }
            });
    }

    public void setActionLeftButton(GraphicalUserInterface gui,JComponent nPage){
        leftButton.addActionListener(new ChangePageAdapter(gui,nPage));
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });


    }

    public void setActionRightButton(String action){
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                controller.performAction(action);
            }
        });

    }

    public void setActionRightButton(ActionListener al) {
        rightButton.addActionListener(al);

    }


        public void setActionMiddleButton(String action){
        if(middleButton.getClass() == JButton.class){
            middleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!action.equals("Save")){
                        dialog.dispose();
                    }
                    controller.performAction(action);
                }
            });
        }
    }


}
