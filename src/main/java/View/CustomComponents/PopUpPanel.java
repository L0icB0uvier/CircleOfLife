package View.CustomComponents;

import View.Adapter.ChangePageAdapter;
import View.EventCollector;
import View.GraphicalUserInterface;
import View.Utils.UIColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PopUpPanel extends JPanel {

    private JLabel mainLabel;
    private JLabel secondaryLabel;
    private ArrayList<JButton> listButton;
    private EventCollector controller;

    private Dialog dialog;

    public PopUpPanel(EventCollector controller, JDialog dialog,int nButtons){

        this.controller = controller;
        this.dialog = dialog;
        this.listButton = new ArrayList<>();
        for(int i = 0;i<nButtons;i++){
            this.listButton.add(new JButton());
        }
        init();
    }

    private void init(){
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);

        JPanel buttonContainer = new JPanel();

        mainLabel   = new JLabel();
        secondaryLabel = new JLabel();

        mainLabel.setFont(getFont().deriveFont(30f));
        secondaryLabel.setFont(getFont().deriveFont(30f));


        //Adding Buttons to the ButtonContainer
        buttonContainer.setLayout(new GridLayout(1,listButton.size(),20,20));
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for(JButton button : listButton){
            button.setFont(getFont().deriveFont(15f));
            buttonContainer.add(button);
        }

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

    public void setButtonLabel(int button,String text) {
        this.listButton.get(button).setText(text);
    }



    public void setMainLabel(String mainLabel) {
        this.mainLabel.setText("<html><div style='text-align:start;width:610px;padding-left :10px;padding-right :10px'>" + mainLabel + "</div></html>");
    }


    public void setSecondaryLabel(String secondaryLabel) {
        this.secondaryLabel.setText("<html><div style='text-align:start;width:610px;padding-left :10px;padding-right :10px'>" + secondaryLabel + "</div></html>");
        this.secondaryLabel.setForeground(UIColor.RED);
        this.secondaryLabel.setVerticalAlignment(SwingConstants.NORTH);
    }


    public void setButtonVisibility(int button,boolean b){
        listButton.get(button).setVisible(b);
    }

    public void setActionButton(int button,String action, boolean dispose){
        this.listButton.get(button).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(action.equals("Save") && !dispose) {
                        ((JButton) (e.getSource())).setEnabled(false);
                    }else{
                        dialog.dispose();
                        for(JButton button : listButton){
                            button.setEnabled(true);
                        }
                    }
                    if (!action.equals("Annuler")) {
                        controller.performAction(action);
                    }
                }
            });
    }


    public void setActionButton(int button,GraphicalUserInterface gui,JComponent nPage){
        this.listButton.get(button).addActionListener(new ChangePageAdapter(gui,nPage));
        this.listButton.get(button).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }




}
