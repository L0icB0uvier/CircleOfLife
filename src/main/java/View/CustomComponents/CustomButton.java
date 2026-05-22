package View.CustomComponents;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class CustomButton extends JButton {

    Color bgColor;

    public CustomButton(String text,Color bgColor){
        super(text);
        this.setBackground(bgColor);
        this.setBorderPainted(false);
        this.setFocusable(false);
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
    }
}
