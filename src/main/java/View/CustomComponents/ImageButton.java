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
    private Color bgColor = UIColor.WHITE;

    RoundedBorder border;
    boolean hovered = false;

    public ImageButton(String path) {
        this.image = Configuration.loadImage(path);
        border = new RoundedBorder(15,true,image);
        init();

    }

    public ImageButton(String path, Color bgColor) {
        this.image = Configuration.loadImage(path);
        this.bgColor = bgColor;
        border = new RoundedBorder(15,true,image);
        init();
    }

    private void init(){
        setContentAreaFilled(false);
        setBackground(bgColor);
        setBorder(border);
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

    @Override
    public void setEnabled(boolean e){
        super.setEnabled(e);

        if(!e){
            setBackground(Color.LIGHT_GRAY);
            border.setToggled(false);
            this.repaint();
        }else{
            setBackground(bgColor);
            border.setToggled(true);
            this.repaint();
        }
    }
}



