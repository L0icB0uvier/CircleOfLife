package View.CustomComponents;

import Global.Configuration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

public class  ImageButton extends JButton {
    private Image imageEnabled, imageDisabled;
    boolean hovered = false;

    public ImageButton(String pathEnabled, String pathDisabled) {
        if(pathEnabled != null)
            this.imageEnabled = Configuration.loadImage(pathEnabled);

        if(pathDisabled != null)
            this.imageDisabled = Configuration.loadImage(pathDisabled);

        setContentAreaFilled(false);
        setBorderPainted(false);

        addMouseListener(new MouseAdapter() {
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
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width,height;

        if(getParent().getWidth() == 0){
            width = getWidth();
        }else {
            width = getWidth();
        }
        if(getParent().getHeight() == 0){
            height = getHeight();
        }else {
            height = getParent().getHeight();
        }


        int size = Math.min(width,height);
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        g.drawImage(isEnabled()? imageEnabled : imageDisabled, x, y, size, size, this);

    }

}



