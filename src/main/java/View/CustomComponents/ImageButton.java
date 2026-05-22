package View.CustomComponents;

import Global.Configuration;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class  ImageButton extends JButton {
    private final Image image;
    static private final Color BG_COLOR = UIColor.WHITE;
    boolean hovered = false;

    public ImageButton(String path) {
        this.image = Configuration.loadImage(path);
        setContentAreaFilled(false);
        setBackground(BG_COLOR);
        setBorder(new RoundedBorder(15,true,image));

        /*addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });*/
    }


}



