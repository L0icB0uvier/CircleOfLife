package View.Utils;

import Global.Configuration;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class RoundedBorder implements Border {

    public static final int SHADOW_SIZE_BOTTOM = 6;
    public static final int SHADOW_SIZE_HORIZONTAL = 3;

    private final int radius;
    private Color color;
    private final int thickness ;
    private final boolean shadow ;
    private Image image = null;
    private boolean toogle = false;
    private boolean isToogled = true;

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
    public RoundedBorder(int radius,boolean shadow,Image image) {
        this.radius = radius;
        this.thickness = 0;
        this.shadow = shadow;
        this.image = image;
    }


    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        if(thickness > 0) {
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawRoundRect(x + thickness / 2, y + thickness / 2, width - thickness, height - thickness, radius, radius);
        }else {
            if(shadow){
                if(toogle && isToogled) {
                    g2d.setColor(c.getBackground());
                    g2d.fillRoundRect(x + SHADOW_SIZE_HORIZONTAL, y, width - SHADOW_SIZE_HORIZONTAL * 2, height - SHADOW_SIZE_BOTTOM, radius, radius);
                    paintShadow(g2d, x, y, width, height);
                }else{
                    paintShadow(g2d, x, y, width, height);
                    g2d.setColor(c.getBackground());
                    g2d.fillRoundRect(x + SHADOW_SIZE_HORIZONTAL, y, width - SHADOW_SIZE_HORIZONTAL * 2, height - SHADOW_SIZE_BOTTOM, radius, radius);

                }
            }else {
                g2d.setColor(c.getBackground());
                g2d.fillRoundRect(x, y, width - 1, height - 1, radius, radius);
            }
        }
        if(image != null){
            int widthContainer,heightContainer;
            if(c.getParent().getWidth() == 0){
                widthContainer= c.getWidth()-(shadow?2*SHADOW_SIZE_HORIZONTAL:0);
            }else {
                widthContainer = c.getWidth()-(shadow?2*SHADOW_SIZE_HORIZONTAL:0);
            }
            if(c.getParent().getHeight() == 0){
                heightContainer = c.getHeight()-(shadow?SHADOW_SIZE_BOTTOM:0);
            }else {
                heightContainer= c.getParent().getHeight()-(shadow?SHADOW_SIZE_BOTTOM:0);
            }
            int size = (int) (Math.min(heightContainer,widthContainer)*0.8);
            int imageX = (widthContainer - size) / 2 + (shadow?SHADOW_SIZE_HORIZONTAL:0);
            int imageY = (heightContainer - size) / 2;

            g2d.drawImage(image, imageX, imageY, size, size, c);


        }

    }

    private void paintShadow(Graphics2D g2d, int x, int y, int width, int height) {
        Color prevColor = g2d.getColor();
        for (int i = SHADOW_SIZE_BOTTOM; i >= 1; i--) {
            if(toogle && isToogled) {
                int alpha = (int)(50 - Math.pow((double)(SHADOW_SIZE_BOTTOM - i) / (SHADOW_SIZE_BOTTOM - 1), 0.9) * 40);
                g2d.setColor(new Color(0, 0, 0,alpha ));

                int shadowX = x + SHADOW_SIZE_HORIZONTAL + (SHADOW_SIZE_BOTTOM - i);
                int shadowY = y + (SHADOW_SIZE_BOTTOM - i);
                int shadowWidth = width - SHADOW_SIZE_HORIZONTAL *2 - (SHADOW_SIZE_BOTTOM - i);
                int shadowHeight = height - SHADOW_SIZE_BOTTOM - (SHADOW_SIZE_BOTTOM - i);

                g2d.fillRoundRect(shadowX, shadowY, shadowWidth, shadowHeight, radius, radius);
                g2d.setColor(prevColor);
                g2d.fillRoundRect(shadowX+1, shadowY+1, shadowWidth-1, shadowHeight-1, radius , radius);
            }else{
                int alpha = (int) (30 - (SHADOW_SIZE_BOTTOM * i / 1.5));
                g2d.setColor(new Color(0, 0, 0, alpha));

                int shadowX = x + (SHADOW_SIZE_BOTTOM - i);
                int shadowY = y + (SHADOW_SIZE_BOTTOM - i <= SHADOW_SIZE_HORIZONTAL ? SHADOW_SIZE_BOTTOM - i : SHADOW_SIZE_HORIZONTAL + 1) + 3;
                int shadowWidth = width - (SHADOW_SIZE_BOTTOM - i) * 2;
                int shadowHeight = height - (SHADOW_SIZE_BOTTOM - i) - ((SHADOW_SIZE_BOTTOM - i <= SHADOW_SIZE_HORIZONTAL ? SHADOW_SIZE_BOTTOM - i : SHADOW_SIZE_HORIZONTAL + 1) + 3);

                g2d.fillRoundRect(shadowX, shadowY, shadowWidth, shadowHeight, radius + i, radius + i);
            }
        }
        g2d.setColor(prevColor);

    }

    public void toogle(){
        toogle = !toogle;
    }

    public void setToggled(boolean b){
        isToogled = b;
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
