package View.CustomComponents;

import javax.swing.*;
import java.awt.*;


public class GameControlBar extends JPanel {

    public ImageButton saveBt, previewBt,undoBt,redoBt,forfeitBt,leaveBt;

    public GameControlBar(){
        init();
    }

    private void init(){
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);

        // Initialisation of all components
        saveBt = new ImageButton("res/Images/saveIcon.png", null);
        previewBt = new ImageButton("res/Images/selectIcon.png", null);
        undoBt = new ImageButton("res/Images/undoIcon.png", "res/Images/undoDisabledIcon.png");
        redoBt = new ImageButton("res/Images/redoIcon.png", "res/Images/redoDisabledIcon.png");
        forfeitBt = new ImageButton("res/Images/forfeitIcon.png", null);
        leaveBt = new ImageButton("res/Images/leaveIcon.png", null);

        JPanel leftPanel   = initPanel(FlowLayout.LEFT,saveBt, previewBt);
        JPanel middlePanel = initPanel(FlowLayout.CENTER,undoBt, redoBt);
        JPanel rightPanel  = initPanel(FlowLayout.RIGHT,forfeitBt, leaveBt);

        // Adding Action to all buttons
        saveBt.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

            }
        });

        previewBt.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

            }
        });

        undoBt.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //TODO
            }
        });

        redoBt.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

            }
        });

        forfeitBt.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

            }
        });

        leaveBt.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

            }
        });

        // GridBagConstraints for all components
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.weighty=1;
        buttonConstraints.weightx=1;
        buttonConstraints.fill= GridBagConstraints.BOTH;


        // Adding components to the layout
        buttonConstraints.anchor = GridBagConstraints.WEST;
        buttonConstraints.insets = new Insets(0,20,0,0);
        gbl.setConstraints(leftPanel,buttonConstraints);
        this.add(leftPanel);

        buttonConstraints.anchor = GridBagConstraints.CENTER;
        buttonConstraints.gridx ++;
        buttonConstraints.insets = new Insets(0,0,0,0);

        gbl.setConstraints(middlePanel,buttonConstraints);
        this.add(middlePanel);

        buttonConstraints.anchor = GridBagConstraints.EAST;
        buttonConstraints.insets = new Insets(0,0,0,20);
        buttonConstraints.gridx ++;
        gbl.setConstraints(rightPanel,buttonConstraints);
        this.add(rightPanel);

    }

    private JPanel initPanel(int align,ImageButton... buttons) {
        JPanel p = new JPanel(){
            @Override
            public void doLayout(){
                int h = getHeight();
                int gap = 15;

                int totalWidth = buttons.length * h + (buttons.length - 1) * gap;

                int x = (getWidth() - totalWidth) / 2;

                if (align == FlowLayout.LEFT)
                    x = 0;

                else if (align == FlowLayout.CENTER)
                    x = (getWidth() - totalWidth) / 2;

                else if (align == FlowLayout.RIGHT)
                    x = getWidth() - totalWidth;

                for (ImageButton b : buttons) {
                    b.setBounds(x, 0, h, h);
                    x += h + gap;
                }
            }
        };
        p.setOpaque(false);
        for (ImageButton b : buttons){
            p.add(b);
        }
        return p;
    }
}

