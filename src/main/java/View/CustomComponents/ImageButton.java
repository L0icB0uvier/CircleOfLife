package View.CustomComponents;

import Global.Configuration;
import View.Utils.RoundedBorder;
import View.Utils.UIColor;

import javax.swing.*;
import java.awt.*;

public class  ImageButton extends JButton {
    private final Image image;
    private Color bgColor = UIColor.WHITE;
    boolean hovered = false;

    public ImageButton(String path) {
        this.image = Configuration.loadImage(path);
        setContentAreaFilled(false);
        setBackground(bgColor);
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

    public ImageButton(String path, Color bgColor) {
        this.image = Configuration.loadImage(path);
        this.bgColor = bgColor;
        setContentAreaFilled(false);
        setBackground(this.bgColor);
        setBorder(new RoundedBorder(15, true, image));
    }


}



