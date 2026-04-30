package View.CustomComponents;

import View.Adapter.ChangePageAdapter;
import View.EventCollector;
import View.GraphicalUserInterface;
import View.Utils.UIColor;
import View.Utils.UIFont;

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
    private EventCollector controller;

    public PopUpPanel(EventCollector controller){

        this.controller = controller;
        init();
    }

    private void init(){
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);

        // Initialisation of all components
        rightButton = new JButton();
        leftButton = new JButton();

        JPanel buttonContainer = new JPanel();

        mainLabel   = new JLabel();
        secondaryLabel = new JLabel();

        mainLabel.setFont(UIFont.getFont().deriveFont(50f));
        secondaryLabel.setFont(UIFont.getFont().deriveFont(20f));
        rightButton.setFont(UIFont.getFont().deriveFont(25f));
        leftButton.setFont(UIFont.getFont().deriveFont(25f));

        //Adding Buttons to the ButtonContainer
        buttonContainer.setLayout(new GridLayout(1,3,20,20));
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonContainer.add(leftButton);
        buttonContainer.add(new JPanel());

        buttonContainer.add(rightButton);

        // Adding components to the layout
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridy = 0;
        buttonConstraints.weighty=40;
        buttonConstraints.weightx=1;
        buttonConstraints.fill= GridBagConstraints.BOTH;
        gbl.setConstraints(mainLabel,buttonConstraints);
        this.add(mainLabel);

        buttonConstraints.gridy++;
        buttonConstraints.weighty=30;
        gbl.setConstraints(secondaryLabel,buttonConstraints);
        this.add(secondaryLabel);

        buttonConstraints.gridy ++;
        buttonConstraints.weighty=30;
        gbl.setConstraints(buttonContainer,buttonConstraints);
        this.add(buttonContainer);
    }

    public void setLeftButton(String leftButton) {
        this.leftButton.setText(leftButton);
    }


    public void setMainLabel(String mainLabel) {
        this.mainLabel.setText("<html><div style='text-align:start;width:610px;padding-left :10px;padding-right :10px'>" + mainLabel + "</div></html>");
    }

    public void setRightButton(String rightButton) {
        this.rightButton.setText(rightButton);
    }

    public void setSecondaryLabel(String secondaryLabel) {
        this.secondaryLabel.setText("<html><div style='text-align:start;width:610px;padding-left :10px;padding-right :10px'>" + secondaryLabel + "</div></html>");
        this.secondaryLabel.setForeground(UIColor.getColor(UIColor.RED));
        this.secondaryLabel.setVerticalAlignment(SwingConstants.NORTH);
    }

    public void setActionLeftButton(String action,JDialog dialog){
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
    }

    public void setActionRightButton(String action,JDialog dialog){
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                controller.performAction(action);
            }
        });

    }


}
