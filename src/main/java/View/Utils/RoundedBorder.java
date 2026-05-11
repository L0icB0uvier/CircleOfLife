package View.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class RoundedBorder implements Border {
    private int radius;
    private Color color;
    private int thickness;

    public RoundedBorder(int radius, Color color, int thickness) {
        this.radius = radius;
        this.color = color;
        this.thickness =thickness;
    }
    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(color!=null){
            g.setColor(color);
            g2d.fillRoundRect(x, y, width -1, height - 1, (int) (radius*1.5), (int) (radius*1.5));

        }
        g.setColor(c.getBackground());
        g2d.fillRoundRect(x+thickness, y+thickness, width -(thickness*2+1), height - (thickness*2+1), radius, radius);

    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius, radius, radius, radius);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
