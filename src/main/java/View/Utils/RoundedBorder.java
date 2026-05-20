package View.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class RoundedBorder implements Border {

    public static final int SHADOW_SIZE_BOTTOM = 6;
    public static final int SHADOW_SIZE_HORIZONTAL = 3;

    private int radius;
    private Color color;
    private int thickness ;
    private boolean shadow ;

    public RoundedBorder(int radius, Color color, int thickness) {
        this.radius = radius;
        this.color = color;
        this.thickness = thickness;
        this.shadow = false;
    }
    public RoundedBorder(int radius,boolean shadow) {
        this.radius = radius;
        this.thickness = 0;
        this.shadow = shadow;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(thickness > 0) {
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawRoundRect(x + thickness / 2, y + thickness / 2, width - thickness, height - thickness, radius, radius);
        }else {
            if(shadow){
                paintShadow(g2d,x,y,width,height);
                g2d.setColor(c.getBackground());
                g2d.fillRoundRect(x+SHADOW_SIZE_HORIZONTAL, y, width -SHADOW_SIZE_HORIZONTAL*2 , height - SHADOW_SIZE_BOTTOM, radius, radius);
            }else {
                g2d.setColor(c.getBackground());
                g2d.fillRoundRect(x, y, width - 1, height - 1, radius, radius);
            }
        }

        g2d.dispose();
    }

    private void paintShadow(Graphics2D g2d, int x, int y, int width, int height) {
        for (int i = SHADOW_SIZE_BOTTOM; i >=1; i--){

            int alpha = (int) (30 - (SHADOW_SIZE_BOTTOM*i/1.5));
            g2d.setColor(new Color(0, 0, 0, alpha));

            int shadowX = x + (SHADOW_SIZE_BOTTOM-i);
            int shadowY = y + (SHADOW_SIZE_BOTTOM-i<=SHADOW_SIZE_HORIZONTAL?SHADOW_SIZE_BOTTOM-i:SHADOW_SIZE_HORIZONTAL+1)+3;
            int shadowWidth = width - (SHADOW_SIZE_BOTTOM-i)*2;
            int shadowHeight = height - (SHADOW_SIZE_BOTTOM-i) - ((SHADOW_SIZE_BOTTOM-i<=SHADOW_SIZE_HORIZONTAL?SHADOW_SIZE_BOTTOM-i:SHADOW_SIZE_HORIZONTAL+1)+3);

            g2d.fillRoundRect(shadowX, shadowY, shadowWidth, shadowHeight, radius+i, radius+i);
        }
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
