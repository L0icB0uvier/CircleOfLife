package View.CustomComponents;

import View.Utils.RoundedBorder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomButton extends JButton  {

    Color bgColor;
    RoundedBorder border;
    String text;

    public CustomButton(String text,Color bgColor, boolean toggleable){
        this.bgColor = bgColor;
        this.text = text;
        this.setBackground(this.bgColor);
        this.setFocusable(false);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.border = new RoundedBorder(15,true, this.text);
        this.setBorder(border);
        this.addChangeListener(e -> {
            CustomButton button =  (CustomButton)e.getSource();
            if(button.isEnabled()){
                button.setBackground(bgColor);
            }else{
                button.setBackground(Color.LIGHT_GRAY);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                border.toggle();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(!toggleable) border.toggle();
                border.toggle();
                repaint();
            }


        });
    }

    public void updateText(String text){
        this.text = text;
        this.border = new RoundedBorder(15,true,this.text);
        this.setBorder(border);
    }

    @Override
    public String getText() {
        return this.text;
    }

}
