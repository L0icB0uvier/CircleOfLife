package View.CustomComponents;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import Global.Configuration;
import Model.Coordinate;
import Model.Game;
import Model.Match;
import Patterns.Observer;
import View.Utils.UIColor;

public class GamePanel extends JComponent implements Observer {
    Game game;
    BufferedImage imgPlateau, imgStonePlayer1, imgStonePlayer2, imgEatenStone;
    Match match;

    int imgSrcHeight, imgSrcWidth;
    int mouseX, mouseY;
    int nSelected, mSelected;

    boolean previewEnabled = false;

    double incX;
    double incY;
    double size;
    int stoneImageSize;

    int previewAlpha = 150, defaultAlpha = 255;

    double alpha = 1;
    double oneMinusAlpha;
    double magicNumber = 92;

    double x0Ratio = 0.35505, y0Ratio = 0.24893;
    double ratioDistanceX = 0.07329;
    double hexagonHeightRatio = 0.08461;

    int x0, y0, distance;

    public GamePanel(Game game) {
        this.game = game;
        game.addObserver(this);
        match=game.getMatch();
        nSelected = -1;
        mSelected = -1;
        oneMinusAlpha = 1 - alpha;

        imgPlateau=(BufferedImage) readImage("Plateau_fleches");
        imgStonePlayer1 = (BufferedImage) readImage("Blue_Stone");
        imgStonePlayer2 = (BufferedImage) readImage("Red_Stone");

        imgSrcHeight = imgPlateau.getHeight();
        imgSrcWidth = imgPlateau.getWidth();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        drawBoard(g2d);
        drawStones(g2d);
        drawSelected(g2d);
        //drawEaten(g2d);
    }

    private void drawBoard(Graphics2D g2d){
        int width = getSize().width;
        int height = getSize().height;

        int x, y, imageWidth, imageHeight;

        if(width > height){
            x = (int) ((width - ((alpha * height * imgSrcWidth) / imgSrcHeight))) / 2;
            y = (int) ((oneMinusAlpha) / 2 * height);
            imageWidth = (int) Math.round(alpha * height * imgSrcWidth) / imgSrcHeight;
            imageHeight = (int) Math.round(alpha * height);
            size = imageHeight * (magicNumber / (double)imgSrcHeight);
        }
        else{
            x = (int) (((oneMinusAlpha) / 2) * width);
            y = (int) ((height - ((alpha * width * imgSrcHeight) / imgSrcWidth)) / 2);
            imageWidth = (int) Math.round(alpha * width);
            imageHeight = (int) Math.round((alpha * width * imgSrcHeight) / imgSrcWidth);
            size = imageHeight * (magicNumber / (double)imgSrcHeight);
        }

        // Calcul du centre de la case 0:0 du plateau
        x0 = (int) Math.round(x + (x0Ratio * imageWidth));
        y0 = (int) Math.round(y + (y0Ratio * imageHeight));
        distance = (int) Math.round(ratioDistanceX * imageWidth);

        // Calcule taille de l'image des pierres
        stoneImageSize = (int) Math.round(hexagonHeightRatio * imageHeight);

        incX = 2 * (int) Math.round(Math.cos((Math.PI / 6)) * size); // permet d'aller au prochain coin horizontalement
        incY = (int) Math.round(Math.sin((Math.PI / 6)) * size) + size; // permet d'aller au prochain coin verticalement

        g2d.drawImage(imgPlateau, x, y, imageWidth, imageHeight, null);
    }

    /**
     * Dessine les pierres sur le plateau.
     * @param g2d Le Graphic à utiliser pour dessiner.
     */
    private void drawStones(Graphics2D g2d){
        int boardSize = match.getBoardSize();
        for (int m = 0; m < boardSize; m++) {
            for (int n = 0; n < boardSize; n++) {

                int contentType = match.getContentAt(m, n);
                if(contentType == 0 || contentType == Integer.MAX_VALUE)
                    continue;

                int x = nToX(n, m) - (stoneImageSize / 2);
                int y = mToY(m) - (stoneImageSize / 2);

                switch (contentType){
                    case 1:
                        drawStone(g2d, imgStonePlayer1, 255, x, y, stoneImageSize);
                        break;
                    case 2:
                        drawStone(g2d, imgStonePlayer2, defaultAlpha, x, y, stoneImageSize);
                        break;
                    case -1:

                }
            }
        }
    }

    /**
     * Dessine la pierre sous le curseur du joueur actif.
     * @param g2d
     */
    private void drawSelected(Graphics2D g2d){
        int m = getmSelected();
        int n = getnSelected();
        int contentType = match.getContentAt(m, n);
        switch (contentType){
            case 0:
                int x = nToX(n, m) - (stoneImageSize / 2);
                int y = mToY(m) - (stoneImageSize / 2);
                drawStone(g2d, match.getCurrentPlayerIndex() == 0? imgStonePlayer1 : imgStonePlayer2, previewAlpha, x, y, stoneImageSize);
                break;
            case 1:
                break;
            case 2:
                break;
            case -1:
                break;
            case -2:
                break;

        }
    }

    private void drawEaten(Graphics2D g2d){
        List<Coordinate> coordinates = match.getPreviouslyEatenCrittersCoordinates();
        for (Coordinate coord : coordinates){
            if(coord.col() == nSelected && coord.line() == mSelected)
                continue;

            int x = nToX(coord.col(), coord.line());
            int y = mToY(coord.line());

            drawStone(g2d, imgEatenStone, previewAlpha, x, y, stoneImageSize);
        }
    }

    private void drawStone(Graphics2D g2d, Image img, int alpha, int x, int y, int size){
        Color previousColor = g2d.getColor();
        g2d.setColor(new Color(previousColor.getRed(), previousColor.getGreen() ,previousColor.getBlue() , alpha));
        g2d.drawImage(img, x, y, size, size, null);
        g2d.setColor(previousColor);
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
                switch (match.getContentAt(m, n)) {
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
            switch (match.getContentAt(m, n)) {
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
                switch (match.getContentAt(m, n)) {
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

        Configuration.info(String.format("Mouse at %d:%d", x, y));
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

    public int nToX(int n, int m) {
        return (x0 + distance * (n - (m / 2)));
    }

    public int mToY(int m) {
        return (int) (y0 + ((m * Math.sqrt(3) * distance) / 2));
    }

    public int xToN(int x, int y) {
        return (int) (Math.round(((double) (x - x0) / distance) + ((1 / Math.sqrt(3) * ((double) (y - y0) / distance)))));
    }

    public int yToM(int y) {
        return (int) (Math.round((2 * (y - y0) / (distance * Math.sqrt(3)))));
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
