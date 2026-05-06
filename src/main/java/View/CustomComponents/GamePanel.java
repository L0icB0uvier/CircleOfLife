package View.CustomComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import Global.Configuration;
import Model.Game;
import Model.Match;
import Patterns.Observer;
import View.Utils.UIColor;

public class GamePanel extends JComponent implements Observer {
    Game game;
    BufferedImage imgPlateau;
    Image imgWaffle;
    int imgSrcHeight, imgSrcWidth;
    Match match;
    int scaleX, scaleY;
    int nbLines, nbColumns;
    int mouseX, mouseY;
    int nSelected, mSelected;
    int tintAlpha = 160;
    boolean previewEnabled = false;

    double incX;
    double incY;
    double size;

    double alpha = 0.95;

    public GamePanel(Game game) {
        /*
         * this.game = game;
         * this.game.ajouteObservateur(this);
         * 
         * imgWaffle=readImage("waffle");
         * 
         * match = game.getMatch();
         */

        // nbLines = match.getNbLines();
        // nbColumns = match.getNbCol();
        this.game=game;
        match=game.getMatch();
        nSelected = -1;
        mSelected = -1;
        imgPlateau=(BufferedImage) readImage("Plateau_fleches");
        imgSrcHeight = imgPlateau.getHeight();
        imgSrcWidth = imgPlateau.getWidth();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        int width = getSize().width;
        int height = getSize().height;

        int x, y, imageWidth, imageHeight;
        double oneMinusAlpha = 1 - alpha;

        if(width > height){
            x = (int) ((width - ((alpha * height * imgSrcWidth) / imgSrcHeight))) / 2;
            y = (int) ((oneMinusAlpha) / 2 * height);
            imageWidth = (int) Math.round(alpha * height * imgSrcWidth) / imgSrcHeight;
            imageHeight = (int) Math.round(alpha * height);
            size = imageHeight * (89 / (double)imgSrcHeight);
        }
        else{
            x = (int) ((oneMinusAlpha) / 2) * width;
            y = (int) ((height - ((alpha * width * imgSrcHeight) / imgSrcWidth)) / 2);
            imageWidth = (int) Math.round(alpha * width);
            imageHeight = (int) Math.round((alpha * width * imgSrcHeight) / imgSrcWidth);
            size = imageWidth * (89. / (double)imgSrcWidth);
        }
        
        incX = 2 * (int) Math.round(Math.cos((Math.PI / 6)) * size); // permet d'aller au prochain coin horizontalement
        incY = (int) Math.round(Math.sin((Math.PI / 6)) * size) + size; // permet d'aller au prochain coin verticalement

        g2D.drawImage(imgPlateau, x, y, imageWidth, imageHeight, null);
        drawPlateau(g2D);
    }

    private void drawPlateau(Graphics2D g2D) {

        int width = getWidth();
        int height = getHeight();

        int n = 0;
        int m = 0;
        int xSelected = -1, ySelected = -1;
        Color colorSelected = UIColor.getColor(game.getCurrentPlayerIndex() == 0 ? UIColor.BLUE: UIColor.RED);
        int posX = (int) (width / 2.0 - 2 * incX);
        int posXInit = posX;
        int posY = (int) (height / 2.0 - (5 * incY - size / 2));

        int nb_elem = 5;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < nb_elem; j++) {
                if(n == nSelected && m == mSelected) {
                    xSelected = posX;
                    ySelected = posY;
                }
                switch (match.getCase(n, m)) {
                    case 1:
                        drawHexagon(g2D, posX, posY, (int) size, UIColor.getColor(UIColor.BLUE), n, m);
                        break;

                    case 2:
                        drawHexagon(g2D, posX, posY, (int) size, UIColor.getColor(UIColor.RED), n, m);
                        break;

                    default:
                        drawHexagon(g2D, posX, posY, (int) size, Color.WHITE, n, m);
                        break;
                }
               // drawHexagon(g2D, posX, posY, (int) size, UIColor.getColor(UIColor.RED), n, m);
                n++;
                posX += incX;
            }
            m++;
            n = 0;
            nb_elem++;
            posX = posXInit;
            posX -= incX / 2;
            posY += incY;
            posXInit = posX;
        }

        for (int j = 0; j < nb_elem; j++) {
            if(n == nSelected && m == mSelected) {
                xSelected = posX;
                ySelected = posY;
            }
            switch (match.getCase(n, m)) {
                    case 1:
                        drawHexagon(g2D, posX, posY, (int) size, UIColor.getColor(UIColor.BLUE), n, m);
                        break;

                    case 2:
                        drawHexagon(g2D, posX, posY, (int) size, UIColor.getColor(UIColor.RED), n, m);
                        break;

                    default:
                        drawHexagon(g2D, posX, posY, (int) size, Color.WHITE, n, m);
                        break;
            }
            posX += incX;
            n++;
        }
        n = 0;
        m++;
        nb_elem--;
        posX = posXInit;
        posX += incX / 2;
        posY += incY;
        posXInit = posX;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < nb_elem; j++) {
                if(n == nSelected && m == mSelected) {
                    xSelected = posX;
                    ySelected = posY;
                }
                switch (match.getCase(n, m)) {
                    case 1:
                        drawHexagon(g2D, posX, posY, (int) size, UIColor.getColor(UIColor.BLUE), n, m);
                        break;

                    case 2:
                        drawHexagon(g2D, posX, posY, (int) size, UIColor.getColor(UIColor.RED), n, m);
                        break;
                    
                    default:
                        drawHexagon(g2D, posX, posY, (int) size, Color.WHITE, n, m);
                        break;
                }
                posX += incX;
                n++;
            }
            nb_elem--;
            n = 9 - nb_elem;
            m++;
            posX = posXInit;
            posX += incX / 2;
            posY += incY;
            posXInit = posX;
        }
        if(xSelected != -1) drawHexagon(g2D, xSelected, ySelected, (int) size, colorSelected, nSelected, mSelected);
    }

    /*
     * dessine un hexagone allant du coin haut et y revenant
     */
    private void drawHexagon(Graphics2D g2D, int x, int y, int size, Color color, int n, int m) {
        double angle, cos, sin, vx, rx, ry;
        int new_x = x;
        int new_y = y;
        int xs[] = new int[6];
        int ys[] = new int[6];
        for (int i = 0; i < 6; i++) {
            xs[i] = x;
            ys[i] = y;

            angle = ((i) * Math.PI / 3 + Math.PI / 6);// on commence en dessinant depuis le coin haut (donc angle de
                                                      // rotation 30°)
            cos = Math.cos(angle);
            sin = Math.sin(angle);
            vx = size;

            // matrice de rotation
            rx = cos * vx;
            ry = sin * vx;
            new_x += (int) Math.round(rx);
            new_y += (int) Math.round(ry);

            x = new_x;
            y = new_y;
        }
        Color tempColor = g2D.getColor();
        Stroke tempStroke = g2D.getStroke();

        g2D.setColor(color);
        g2D.fillPolygon(xs, ys, 6);

        if(n == nSelected && m == mSelected) g2D.setColor(UIColor.getColor(UIColor.WAFFLE));
        else g2D.setColor(Color.BLACK);
        g2D.setStroke(new BasicStroke(size/9.0f));
        g2D.drawPolygon(xs, ys, 6);

        g2D.setColor(tempColor);
        g2D.setStroke(tempStroke);
    }

    private void drawBorders(Graphics2D g2, Color c, int width, int height) {
        Stroke tempStroke = g2.getStroke();
        Color tempColor = g2.getColor();

        g2.setStroke(new BasicStroke(20));
        g2.setColor(c);

        g2.drawLine(0, 0, width, 0);// haut
        g2.drawLine(0, 0, 0, height);// gauche
        g2.drawLine(0, height, width, height);// bas
        g2.drawLine(width, 0, width, height);// droite

        g2.setStroke(tempStroke);
        g2.setColor(tempColor);
    }

    @Override
    public void update() {
        repaint();
    }

    public void updateMousePosition(int x, int y) {
        mouseX = x;
        mouseY = y;
        int n = xToN(mouseX, mouseY);
        int m = yToM(mouseY);
        if(Math.max(Math.abs(n-4), Math.abs(m-4)) <= 4 && (n != nSelected || m != mSelected)) {
            nSelected = n;
            mSelected = m;
            //Configuration.info("Focus sur " + n + ", " + m);
        }
        repaint();
    }

    public int getnSelected() {
        return nSelected;
    }

    public int getmSelected() {
        return mSelected;
    }

    public int nToX(int n, int y) {
        double x0 = getWidth() / 2 - 2 * incX;
        double y0 = getHeight() / 2 - 4 * incY;
        double scaleX = incX;
        double scaleY = incX;
        return (int) (scaleX*(n-(y-y0)/(Math.sqrt(3)*scaleY))+x0);
    }

    public int mToY(int m) {
        double y0 = getHeight() / 2 - 4 * incY;
        double scaleY = incX; 
        return (int) Math.round(m*scaleY*Math.sqrt(3)/2-y0);
    }

    public int xToN(int x, int y) {
        double x0 = getWidth() / 2 - 2 * incX;
        double y0 = getHeight() / 2 - 4 * incY;
        double scaleX = incX;
        double scaleY = incX; // ou bien 2*Math.sin(2*Math.PI/3)*size
//        System.out.println("calculs: \n x0: " + x0 + "\n y0: " + y0);
//        System.out.println("x: " + (x) + "\ny: " + y);
        // System.out.println("scaling: " + scaleX + " " + scaleY);
//        System.out.println(((x - x0) / scaleX + (y - y0) / (scaleY * Math.sqrt(3))));
//        System.out.println((x - x0) / scaleX);
        return (int) (Math.round(((x - x0) / scaleX + (y - y0) / (scaleY * Math.sqrt(3)))));
    }

    public int yToM(int y) {
        double y0 = getHeight() / 2 - 4 * incY;
        double scaleY = incX;
//        System.out.println((2 * (y - y0) / (scaleY * Math.sqrt(3))));
        return (int) (Math.round((2 * (y - y0) / (scaleY * Math.sqrt(3)))));
    }

    public void togglePreview() {
        previewEnabled = !previewEnabled;
    }

    private Image readImage(String nom) {
        InputStream in = Configuration.open("Images/" + nom + ".png");
        Configuration.info("Chargement de l'image " + nom);
        try {
            // Chargement d'une image utilisable dans Swing
            return ImageIO.read(in);
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image " + nom);
        }
        return null;
    }
}
