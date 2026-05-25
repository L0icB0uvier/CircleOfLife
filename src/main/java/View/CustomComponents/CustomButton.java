package View.CustomComponents;

import View.Utils.RoundedBorder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomButton extends JButton {

    Color bgColor;
    RoundedBorder border;
    String text;

    public CustomButton(String text,Color bgColor){
        this.bgColor = bgColor;
        this.text=text;
        this.setBackground(this.bgColor);
        this.setFocusable(false);
        this.setOpaque(false);
        this.border = new RoundedBorder(15,true,this.text);
        this.setBorder(border);
        setContentAreaFilled(false);
        this.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                CustomButton button =  (CustomButton)e.getSource();
                if(button.isEnabled()){
                    button.setBackground(bgColor);
                }else{
                    button.setBackground(Color.LIGHT_GRAY);
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                border.toogle();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                border.toogle();
                repaint();
            }


        });
    }
}
