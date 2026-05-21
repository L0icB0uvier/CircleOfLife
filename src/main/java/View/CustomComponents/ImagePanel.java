package View.CustomComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JComponent {
    BufferedImage image;
    public ImagePanel(BufferedImage image) {
        this.image = image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        int imgSrcHeight = image.getHeight();
        int imgSrcWidth = image.getWidth();
        int width = this.getWidth();
        int height = this.getHeight();
        int boardX0, boardY0, imageWidth, imageHeight;
        int alpha = 1;
        int oneMinusAlpha = 1 - alpha;

        if(width > height){
            boardX0 = (int) ((width - ((alpha * height * imgSrcWidth) / imgSrcHeight))) / 2;
            boardY0 = (int) (0 * height);
            imageWidth = (alpha * height * imgSrcWidth) / imgSrcHeight;
            imageHeight = alpha * height;
        }
        else{
            boardX0 = (int) (((oneMinusAlpha) / 2) * width);
            boardY0 = (int) ((height - ((alpha * width * imgSrcHeight) / imgSrcWidth)) / 2);
            imageWidth = alpha * width;
            imageHeight = (alpha * width * imgSrcHeight) / imgSrcWidth;
        }


        g2D.drawImage(image, boardX0, boardY0, imageWidth, imageHeight, null);
    }
}
