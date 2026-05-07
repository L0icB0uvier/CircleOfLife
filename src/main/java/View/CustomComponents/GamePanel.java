package View.CustomComponents;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import Global.Configuration;
import Model.Coordinate;
import Model.CoordinateUtils;
import Model.Game;
import Model.Match;
import Patterns.Observer;

public class GamePanel extends JComponent implements Observer {
    Game game;
    Match match;

    BufferedImage
            imgPlateau,
            imgStonePlayer1,
            imgStonePlayer2,
            imgStonePlayer1Preview,
            imgStonePlayer2Preview,
            imgEatenStone,
            imgStoneDisabled;

    int imgSrcHeight, imgSrcWidth;
    int mouseX, mouseY;
    int nSelected, mSelected;

    boolean previewEnabled = false;

    double alpha = 1;
    double oneMinusAlpha;

    double x0Ratio = 0.35505, y0Ratio = 0.24893;
    double ratioDistanceX = 0.07329;
    double hexagonHeightRatio = 0.08461;
    double distance;
    int stoneImageSize;
    int x0, y0;

    public GamePanel(Game game) {
        this.game = game;
        game.addObserver(this);
        match=game.getMatch();
        nSelected = -1;
        mSelected = -1;
        oneMinusAlpha = 1 - alpha;

        loadImages();

        imgSrcHeight = imgPlateau.getHeight();
        imgSrcWidth = imgPlateau.getWidth();
    }

    private void loadImages() {
        imgPlateau=(BufferedImage) readImage("Plateau_fleches");
        imgStonePlayer1 = (BufferedImage) readImage("Blue_Stone");
        imgStonePlayer2 = (BufferedImage) readImage("Red_Stone");
        imgStonePlayer1Preview = (BufferedImage) readImage("Blue_Stone_transparent");
        imgStonePlayer2Preview = (BufferedImage) readImage("Red_Stone_transparent");
        imgStoneDisabled = (BufferedImage) readImage("Disabled_Stone");
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
        }
        else{
            x = (int) (((oneMinusAlpha) / 2) * width);
            y = (int) ((height - ((alpha * width * imgSrcHeight) / imgSrcWidth)) / 2);
            imageWidth = (int) Math.round(alpha * width);
            imageHeight = (int) Math.round((alpha * width * imgSrcHeight) / imgSrcWidth);
        }

        // Calcul du centre de la case 0:0 du plateau
        x0 = (int) Math.round(x + (x0Ratio * imageWidth));
        y0 = (int) Math.round(y + (y0Ratio * imageHeight));
        distance = ratioDistanceX * imageWidth;

        // Calcule taille de l'image des pierres
        stoneImageSize = (int) Math.round(hexagonHeightRatio * imageHeight);

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

                int x = nToX(n, m) - Math.round((float) stoneImageSize / 2);
                int y = mToY(m) - Math.round((float) stoneImageSize / 2);

                switch (contentType){
                    case 1:
                        drawStone(g2d, imgStonePlayer1, x, y, stoneImageSize);
                        break;
                    case 2:
                        drawStone(g2d, imgStonePlayer2, x, y, stoneImageSize);
                        break;
                    case -1:
                        if(match.getCurrentPlayerIndex() == 1)
                            continue;
                        drawStone(g2d, imgStoneDisabled, x, y, stoneImageSize);
                        break;
                    case -2:
                        if(match.getCurrentPlayerIndex() == 0)
                            continue;
                        drawStone(g2d, imgStoneDisabled, x, y, stoneImageSize);
                    case -3:
                        drawStone(g2d, imgStoneDisabled, x, y, stoneImageSize);
                }
            }
        }
    }

    /**
     * Dessine la pierre sous le curseur du joueur actif.
     * @param g2d Le Graphic à utiliser pour dessiner.
     */
    private void drawSelected(Graphics2D g2d){
        int m = getmSelected();
        int n = getnSelected();
        int contentType = match.getContentAt(m, n);
        if(contentType > 0)
            return;

        int x = nToX(n, m) - (stoneImageSize / 2);
        int y = mToY(m) - (stoneImageSize / 2);

        switch (contentType){
            case 0:
                drawStone(g2d, match.getCurrentPlayerIndex() == 0? imgStonePlayer1Preview : imgStonePlayer2Preview, x, y, stoneImageSize);
                break;
            case -1:
                if(match.getCurrentPlayerIndex() == 0)
                    return;
                drawStone(g2d, match.getCurrentPlayerIndex() == 0? imgStonePlayer1Preview : imgStonePlayer2Preview, x, y, stoneImageSize);
                break;
            case -2:
                if(match.getCurrentPlayerIndex() == 1)
                    return;
                drawStone(g2d, match.getCurrentPlayerIndex() == 0? imgStonePlayer1Preview : imgStonePlayer2Preview, x, y, stoneImageSize);
                break;
        }
    }

    /**
     * Draw the stones eaten at the previous turn
     * @param g2d Le Graphic à utiliser pour dessiner.
     */
    private void drawEaten(Graphics2D g2d){
        List<Coordinate> coordinates = match.getPreviouslyEatenCrittersCoordinates();
        for (Coordinate coord : coordinates){
            if(coord.col() == nSelected && coord.line() == mSelected)
                continue;

            int x = nToX(coord.col(), coord.line());
            int y = mToY(coord.line());

            drawStone(g2d, imgEatenStone, x, y, stoneImageSize);
        }
    }

    private void drawStone(Graphics2D g2d, Image img, int x, int y, int size){
        g2d.drawImage(img, x, y, size, size, null);
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

        //Configuration.info(String.format("Mouse at %d:%d", x, y));
        if(CoordinateUtils.isInsideBoard(new Coordinate(m, n)) && (n != nSelected || m != mSelected)) {
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
        return (int) (x0 + distance * (n - ((double) m / 2)));
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
