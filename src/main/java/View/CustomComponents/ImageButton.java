package View.CustomComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class  ImageButton extends JButton {
    private Image imageEnabled, imageDisabled;
    boolean hovered = false;

    public ImageButton(String pathEnabled, String pathDisabled) {
        this.imageEnabled = new ImageIcon(pathEnabled).getImage();
        if(pathDisabled != null)
            this.imageDisabled = new ImageIcon(pathDisabled).getImage();
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
            width = getParent().getWidth();
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



