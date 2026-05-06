package View.CustomComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Stroke;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import Global.Configuration;
import Model.Game;
import Model.Match;
import Patterns.Observer;
import View.Utils.UIColor;

public class GamePanel extends JComponent implements Observer {
    BufferedImage imgPlateau;
    Game game;
    Image imgWaffle;
    Match match;
    private int scaleX;
    private int scaleY;
    private int nbLines;
    private int nbColumns;
    private int mouseX, mouseY;
    private int nSelected, mSelected;
    private int tintAlpha = 160;
    private boolean previewEnabled = false;

    double incX;
    double incY;
    double size;

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
        nSelected = -1;
        mSelected = -1;
        imgPlateau=(BufferedImage) readImage("Plateau_fleches");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        int width = getSize().width;
        int height = getSize().height;

        /* 
        double sizeYProp = (0.9 * height / (10 * Math.sin(Math.PI / 6) + 9)); // dérivée depuis
                                                                              // hauteur_totale_plateau=0.9*height cad
                                                                              // 9*incY + incY - size=0.9*height cad
                                                                              // 10*incY-size=0-9*height
        
        double sizeXProp = (int) (0.9 * width / (18 * Math.cos(Math.PI / 6))); // dérivée depuis
        */                                                                       // longeur_totale_plateau=0.9*width cad
                                                         // 9*incX=0.9*width
        int heightImg=imgPlateau.getHeight();
        int heightOriginImage=heightImg;
        int widthImg=imgPlateau.getWidth();
        int widthOriginImage=widthImg;
        double scale=widthImg/heightImg;
        double globalScale=0.95;

        

        if (width<height) {
            widthImg=(int) (globalScale*width);
            heightImg=(int) (globalScale*(width/scale));
            g2D.drawImage(imgPlateau, (int)((1-globalScale)/2*width), height/2-heightImg/2, widthImg, heightImg, null);
        } else {
            heightImg=(int) (globalScale*height);
            widthImg=(int) (globalScale*(height*scale));
            g2D.drawImage(imgPlateau, width/2-widthImg/2, (int)((1-globalScale)/2*height), widthImg, heightImg, null);
        }
        double sizeYProp=heightImg*(86./(double)heightOriginImage);
        
        
        
        
        size = (int) sizeYProp;

        //g2D.drawLine(width/2, height/2, width/2+10 ,height/2);

        incX = 2 * (int) Math.round(Math.cos((Math.PI / 6)) * size); // permet d'aller au prochain coin horizontalement
        incY = (int) Math.round(Math.sin((Math.PI / 6)) * size) + size; // permet d'aller au prochain coin verticalement

        
        
        
        drawPlateau(g2D);
    }

    private void drawPlateau(Graphics2D g2D) {

        int width = getWidth();
        int height = getHeight();

        int n = 0;
        int m = 0;
        int xSelected = -1, ySelected = -1;
        Color colorSelected = null;
        int posX = (int) ((int) width / 2 - 2 * incX);
        int posXInit = posX;
        int posY = (int) (height / 2 - (5 * incY - size / 2));

        int nb_elem = 5;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < nb_elem; j++) {
                if(n == nSelected && m == mSelected) {
                    xSelected = posX;
                    ySelected = posY;
                    colorSelected = UIColor.getColor(UIColor.RED);
                }
                drawHexagon(g2D, posX, posY, (int) size, UIColor.getColor(UIColor.RED), n, m);
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
                colorSelected = Color.WHITE;
            }
            drawHexagon(g2D, posX, posY, (int) size, Color.WHITE, n, m);
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
                if(n + 1 == nSelected && m == mSelected) {
                    xSelected = posX;
                    ySelected = posY;
                    colorSelected = UIColor.getColor(UIColor.BLUE);
                }
                drawHexagon(g2D, posX, posY, (int) size, UIColor.getColor(UIColor.BLUE), n + 1, m);
                posX += incX;
                n++;
            }
            nb_elem--;
            n = 8 - nb_elem;
            m++;
            posX = posXInit;
            posX += incX / 2;
            posY += incY;
            posXInit = posX;
        }
        if(xSelected != -1) drawHexagon(g2D, xSelected, ySelected, (int) size, colorSelected, nSelected, mSelected);
    }

    /*
     * dessine une hexagone allant du coin haut et y revenant
     */
    private void drawHexagon(Graphics2D g2D, int x, int y, int size) {

        Stroke tempStroke = g2D.getStroke();
        g2D.setStroke(new BasicStroke(5));

        double angle, cos, sin, vx, rx, ry;
        int new_x = x;
        int new_y = y;
        for (int i = 0; i < 6; i++) {
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

            g2D.drawLine(x, y, new_x, new_y);
            x = new_x;
            y = new_y;
        }
        g2D.setStroke(tempStroke);
    }

    /*
     * dessine une hexagone allant du coin haut et y revenant
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
        g2D.setStroke(new BasicStroke(5));
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
