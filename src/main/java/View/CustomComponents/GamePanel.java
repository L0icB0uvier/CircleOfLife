package View.CustomComponents;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import Global.Configuration;
import Model.*;
import Patterns.Observer;
import View.Utils.imageRatio;

import static java.util.Map.entry;

public class GamePanel extends JComponent implements Observer {
    Game game;
    Match match;

    BufferedImage
            imgPlateau,
            imgStonePlayer1,
            imgStonePlayer2,
            imgStonePlayer1Preview,
            imgStonePlayer2Preview,
            imgStoneDisabled;

    int imgSrcHeight, imgSrcWidth;
    double imageWidth, imageHeight;
    int mouseX, mouseY;
    int nSelected, mSelected;

    boolean previewEnabled = false;
    boolean drawCenter = false;

    double alpha = 1;
    double oneMinusAlpha;

    private final imageRatio boardOriginRatio = new imageRatio(0.35505, 0.24893);
    private final double ratioDistanceX = 0.07248;
    private final double hexagonHeightRatio = 0.08461;
    double distance;
    int stoneImageSize;
    int boardX0, boardY0;
    int x0, y0;
    double innerRadius, outerRadius;

    boolean requireCalculation = true;

    private final float evolveHighlightThickness = 6.0f;
    private final float feedHighlightThickness = 6.0f;

    private final imageRatio[] shapePositionRatios = new imageRatio[]{
            new imageRatio(0.48825, 0.07045), // 0
            new imageRatio(0.68074, 0.07300), // 1
            new imageRatio(0.84497, 0.25152), // 2
            new imageRatio(0.91086, 0.47923), // 3
            new imageRatio(0.87220, 0.69573), // 4
            new imageRatio(0.65734, 0.84806), // 5
            new imageRatio(0.43349, 0.88752), // 6
            new imageRatio(0.22379, 0.80887), // 7
            new imageRatio(0.09705, 0.68408), // 8
            new imageRatio(0.08924, 0.46216), // 9
            new imageRatio(0.11971, 0.25825), // 10
            new imageRatio(0.29032, 0.11066), // 11
    };

    private Point2D.Double[] shapeOriginPoints;

    private final double circleHexagonHeight = 0.04708;
    private final double circleInterHexagonDistance = 0.04077;
    private double circleStoneDistance;
    int circleStoneImageSize;
    double circleStoneOffset;

    private final Map<Integer, Integer> circleShapeType = Map.ofEntries(
            entry(0, 0),
            entry(1, 2),
            entry(2, 1),
            entry(3, 1),
            entry(4, 1),
            entry(5, 0),
            entry(6, 3),
            entry(7, 6),
            entry(8, 1),
            entry(9, 2),
            entry(10, 3),
            entry(11, 1)
    );

    public GamePanel(Game game) {
        this.game = game;

        this.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsAdapter() {
            @Override
            public void ancestorResized(java.awt.event.HierarchyEvent e) {
                requireCalculation = true;
            }
        });

        game.addObserver(this);
        match = game.getMatch();

        shapeOriginPoints = new Point2D.Double[12];

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
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(requireCalculation)
            recalculate();

        drawBoard(g2d);
        drawStones(g2d);
        if(match.isGameOver()){
            return;
        }
        if(drawSelected(g2d))
            drawFeedforward(g2d);

        //drawEaten(g2d);
    }

    public void recalculate(){
        Configuration.config("Recalculate Game Panel");

        int width = getSize().width;
        int height = getSize().height;

        if(width > height){
            boardX0 = (int) ((width - ((alpha * height * imgSrcWidth) / imgSrcHeight))) / 2;
            boardY0 = (int) ((oneMinusAlpha) / 2 * height);
            imageWidth = (alpha * height * imgSrcWidth) / imgSrcHeight;
            imageHeight = alpha * height;
        }
        else{
            boardX0 = (int) (((oneMinusAlpha) / 2) * width);
            boardY0 = (int) ((height - ((alpha * width * imgSrcHeight) / imgSrcWidth)) / 2);
            imageWidth = alpha * width;
            imageHeight = (alpha * width * imgSrcHeight) / imgSrcWidth;
        }

        // Calcul du centre de la case 0:0 du plateau
        x0 = (int) Math.round(boardX0 + (boardOriginRatio.xRatio() * imageWidth));
        y0 = (int) Math.round(boardY0 + (boardOriginRatio.yRatio() * imageHeight));
        distance = ratioDistanceX * imageWidth;

        // Calcule taille de l'image des pierres
        stoneImageSize = (int) Math.round(hexagonHeightRatio * imageHeight);

        outerRadius = (double) stoneImageSize / 2;
        innerRadius = outerRadius * 0.866025404f;

        circleStoneDistance = circleInterHexagonDistance * imageWidth;
        circleStoneImageSize = (int) Math.round(circleHexagonHeight * imageHeight);

        circleStoneOffset = (double) circleStoneImageSize / 2;
        for (int i = 0; i < 12; i++) {
            var ratio = shapePositionRatios[i];
            shapeOriginPoints[i] = new Point2D.Double(boardX0 + imageWidth * ratio.xRatio(), boardY0 + imageHeight * ratio.yRatio());
        }

        requireCalculation = false;
    }


    private void drawBoard(Graphics2D g2d){
        g2d.drawImage(imgPlateau, boardX0, boardY0, (int) Math.round(imageWidth), (int) Math.round(imageHeight), null);
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

                if(drawCenter && contentType != Integer.MAX_VALUE)
                    drawTileCenter(g2d, n, m);

                if(contentType == 0 || contentType == Integer.MAX_VALUE)
                    continue;

                Point drawPos = getStoneDrawPositions(n, m);

                switch (contentType){
                    case 1:
                        drawStone(g2d, imgStonePlayer1, drawPos.x, drawPos.y, stoneImageSize);
                        break;
                    case 2:
                        drawStone(g2d, imgStonePlayer2, drawPos.x, drawPos.y, stoneImageSize);
                        break;
                    case -1:
                        if(match.getCurrentPlayerIndex() == 1)
                            continue;
                        drawStone(g2d, imgStoneDisabled, drawPos.x, drawPos.y, stoneImageSize);
                        break;
                    case -2:
                        if(match.getCurrentPlayerIndex() == 0)
                            continue;
                        drawStone(g2d, imgStoneDisabled, drawPos.x, drawPos.y, stoneImageSize);
                    case -3:
                        drawStone(g2d, imgStoneDisabled, drawPos.x, drawPos.y, stoneImageSize);
                }
            }
        }
    }

    private void drawTileCenter(Graphics2D g2d, int n, int m){
        int x = nToX(n, m);
        int y = mToY(m);
        g2d.drawRect(x - 1, y - 1, 2, 2);
    }

    /**
     * Dessine la pierre sous le curseur du joueur actif.
     * @param g2d Le Graphic à utiliser pour dessiner.
     */
    private boolean drawSelected(Graphics2D g2d){
        int m = getmSelected();
        int n = getnSelected();
        if (n==-1 || m==-1) return false;
        int contentType = match.getContentAt(m, n);
        if(contentType > 0 || contentType == -3)
            return false;

        Point drawPos = getStoneDrawPositions(n, m);

        switch (contentType){
            case 0:
                drawStone(g2d, match.getCurrentPlayerIndex() == 0? imgStonePlayer1Preview : imgStonePlayer2Preview, drawPos.x, drawPos.y, stoneImageSize);
                break;
            case -1:
                if(match.getCurrentPlayerIndex() == 0)
                    return false;
                drawStone(g2d, match.getCurrentPlayerIndex() == 0? imgStonePlayer1Preview : imgStonePlayer2Preview, drawPos.x, drawPos.y, stoneImageSize);
                break;
            case -2:
                if(match.getCurrentPlayerIndex() == 1)
                    return false;
                drawStone(g2d, match.getCurrentPlayerIndex() == 0? imgStonePlayer1Preview : imgStonePlayer2Preview, drawPos.x, drawPos.y, stoneImageSize);
                break;
        }

        return true;
    }

    private void drawCircleShape(Graphics2D g2d, int shapeType, int playerIndex){
        Set<Coordinate> coords = ShapeUtils.getShapeCoordinatesForId(shapeType, circleShapeType.get(shapeType));

        for(Coordinate coord : coords){
            int x = (int) Math.round(shapeOriginPoints[shapeType].x + (coord.col() * circleStoneDistance - coord.line() * circleStoneDistance / 2) - circleStoneOffset) ;
            int y = (int) Math.round((shapeOriginPoints[shapeType].y + ((Math.sqrt(3) * circleStoneDistance * coord.line()) / 2)) - circleStoneOffset);
            drawStone(g2d, playerIndex == 0? imgStonePlayer1 : imgStonePlayer2, x, y, circleStoneImageSize);
        }
    }

    private void drawFeedforward(Graphics2D g2d){
        boolean showEvolveFeedback = false;
        Coordinate selectedCoordinate = new Coordinate(getnSelected(), getmSelected());
        Set<Critter> playerNeighbors = match.getPlayerNeighborsCritters(match.getCurrentPlayerIndex(), selectedCoordinate);
        int evolveInto = 0;
        Set<Coordinate> evolveCoords = new HashSet<>();
        evolveCoords.add(selectedCoordinate);
        if(!playerNeighbors.isEmpty()) {
            for (Critter critter : playerNeighbors) {
                evolveCoords.addAll(critter.stonesCoordinates());
            }

            evolveInto = ShapeUtils.getShapeId(evolveCoords);
            showEvolveFeedback = true;
        }

        if(evolveInto >= 0){
            Set<Critter> opponentsNeighbors = new HashSet<>();
            for (Coordinate coord : evolveCoords){
                opponentsNeighbors.addAll(match.getPlayerNeighborsCritters(match.getOpponentPlayerIndex(), coord));
            }

            int eatenShape = -1;
            if(!opponentsNeighbors.isEmpty()){
                for (Critter critter : opponentsNeighbors){
                    if(match.canEat(evolveInto, critter.type())) {
                        drawHighlight(g2d, critter.stonesCoordinates(), Color.orange, feedHighlightThickness);
                        eatenShape = critter.type();
                    }
                }
            }
            if(eatenShape >= 0)
                drawCircleShape(g2d, eatenShape, match.getOpponentPlayerIndex());
        }

        if(showEvolveFeedback)
            drawHighlight(g2d, evolveCoords,  Color.yellow, evolveHighlightThickness);

        drawCircleShape(g2d, evolveInto, match.getCurrentPlayerIndex());
    }


    private void drawHighlight(Graphics2D g2d, Set<Coordinate> coordinates, Color highlightColor, float strokeThickness){
        var previousColor = g2d.getColor();
        var previousStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(strokeThickness));
        g2d.setColor(highlightColor);

        for (Coordinate coord : coordinates){
            int x = nToX(coord.col(), coord.line());
            int y = mToY(coord.line());

            Coordinate[] neighborCoordinate = new Coordinate[]{
                    new Coordinate(coord.col() - 1, coord.line()),
                    new Coordinate(coord.col() - 1, coord.line() - 1),
                    new Coordinate(coord.col(), coord.line() - 1),
                    new Coordinate(coord.col() + 1, coord.line()),
                    new Coordinate(coord.col() + 1, coord.line() + 1),
                    new Coordinate(coord.col(), coord.line() + 1),
            };

            Point[] corners = new Point[] {
                    new Point((int) Math.round(x - innerRadius), y + (int) Math.round(0.5f * outerRadius)),
                    new Point((int) Math.round(x - innerRadius), y - (int) Math.round(0.5f * outerRadius)),
                    new Point(x, y - (int) Math.round(outerRadius)),
                    new Point((int) Math.round(x + innerRadius), y - (int) Math.round(0.5f * outerRadius)),
                    new Point((int) Math.round(x + innerRadius), y + (int) Math.round(0.5f * outerRadius)),
                    new Point(x, y + (int) Math.round(outerRadius))
            };

            for (int i = 0; i < 6; i++) {
                Coordinate neighborCoord = neighborCoordinate[i];
                if(!coordinates.contains(neighborCoord)){
                    g2d.drawLine(corners[i].x, corners[i].y, corners[(i + 1) % 6].x, corners[(i + 1) % 6].y);
                }
            }
        }

        g2d.setColor(previousColor);
        g2d.setStroke(previousStroke);
    }

    private Point getStoneDrawPositions(int n, int m){
        Coordinate pixel = tileToPixel(new Coordinate(n, m));
        int x = pixel.col() - Math.round((float) stoneImageSize / 2);
        int y = pixel.line() - Math.round((float) stoneImageSize / 2);
        return new Point(x, y);
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

            Point drawPos = getStoneDrawPositions(coord.col(), coord.line());

            BufferedImage imgEatenStone = match.getCurrentPlayerIndex() == 0? imgStonePlayer1Preview : imgStonePlayer2Preview;
            drawStone(g2d, imgEatenStone, drawPos.x, drawPos.y, stoneImageSize);
        }
    }

    private void drawStone(Graphics2D g2d, Image img, int x, int y, int size){
        g2d.drawImage(img, x, y, size, size, null);
    }

    @Override
    public void update() {
        repaint();
    }

    public void updateMousePosition(int x, int y) {
        mouseX = x;
        mouseY = y;
        Coordinate mouseToTile = pixelToTile(new Coordinate(mouseX, mouseY));
        int n = mouseToTile.col();
        int m = mouseToTile.line();

        if (MatchUtils.isInsideBoard(new Coordinate(m, n))) {
        //Configuration.info(String.format("Mouse at %d:%d", x, y));
            if((n != nSelected || m != mSelected)) {
                nSelected = n;
                mSelected = m;
                //Configuration.info("Focus sur " + n + ", " + m);
            } 
        } else {
            nSelected = -1;
            mSelected = -1;
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
        return (int) Math.round(x0 + (distance * ((double) n - ((double) m / 2))));
    }

    public int mToY(int m) {
        return (int) Math.round(y0 + ((m * Math.sqrt(3) * distance) / 2));
    }

    public Coordinate tileToPixel(Coordinate tile){
        int n = tile.col();
        int m = tile.line();
        return new Coordinate((int) Math.round(x0 + (distance * ((double) n - ((double) m / 2)))),
                (int) Math.round(y0 + ((m * Math.sqrt(3) * distance) / 2)));
    }

    public int xToN(int x, int y) {
        return (int) (Math.round(((double) (x - x0) / distance) + ((1 / Math.sqrt(3) * ((double) (y - y0) / distance)))));
    }

    public int yToM(int y) {
        return (int) (Math.round((2 * (y - y0) / (distance * Math.sqrt(3)))));
    }

    public Coordinate pixelToTile(Coordinate pixels){
        int x = pixels.col();
        int y = pixels.line();
        int c = (int) (Math.round(((double) (x - x0) / distance) + ((1 / Math.sqrt(3) * ((double) (y - y0) / distance)))));
        int l = (int) (Math.round((2 * (y - y0) / (distance * Math.sqrt(3)))));
        Set<Coordinate> tiles = new HashSet<>(Set.of(new Coordinate(c,l), new Coordinate(c-1, l), new Coordinate(c+1, l),
                new Coordinate(c, l-1), new Coordinate(c, l+1),
                new Coordinate(c-1, l-1), new Coordinate(c+1, l+1)));
        Coordinate closestTile = null;
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (Coordinate tile : tiles){
            double distance = MatchUtils.euclidianDistance(tileToPixel(tile), pixels);
            if (distance < shortestDistance){
                closestTile = tile;
                shortestDistance = distance;
            }
        }
        return closestTile;
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
