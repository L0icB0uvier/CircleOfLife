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
import View.Utils.UIColor;
import View.Utils.imageRatio;

import static java.util.Map.entry;

public class GamePanel extends JComponent implements Observer {
    public static final float OUTER_TO_INNER_RADIUS_RATIO = 0.866025404f;
    Game game;
    Match match;

    BufferedImage
            imgPlateau,
            imgStonePlayer1,
            imgStonePlayer2,
            imgStonePlayer1Preview,
            imgStonePlayer2Preview,
            imgStoneDisabled,
            imgStoneHover;

    int imgSrcHeight, imgSrcWidth;
    float imageWidth, imageHeight;
    int mouseX, mouseY;
    int nSelected, mSelected;

    boolean requireCalculation = true;
    boolean previewEnabled = false;
    boolean drawCenter = false;

    float alpha = 1;
    float oneMinusAlpha;

    private final imageRatio boardOriginRatio = new imageRatio(0.35505f, 0.24893f);
    private final float ratioDistanceX = 0.07248f;
    private final float boardHexagonHeightRatio = 0.08461f;
    float distance;
    int boardStoneImageSize, circleStoneImageSize;
    int boardX0, boardY0;
    int x0, y0;
    float boardHexagonInnerRadius, boardHexagonOuterRadius;
    float circleHexagonInnerRadius, circleHexagonOuterRadius;

    private boolean showCircleHoverHighlight = true;
    private boolean showBoardHoverHighlight = true;
    private boolean useNeutralStoneImageForHoverInCircle = false;
    private boolean showBoardHightlightEffect = true;

    private final float lastMoveThicknessRatio = 0.005f;
    private final float boardEvolveThicknessRatio = 0.006f;
    private final float boardEatenThicknessRatio = 0.003f;
    private final float circleThicknessRatio = 0.0035f;

    private float lastMoveHighlighThickness;
    private float boardEvolveHighlightThickness;
    private float boardEatenHighlightThickness;
    private float circleHighlightThickness;

    float dotedLineDashPatternRatio = 0.0085f;
    float dotedLineSpaceRatio = 0.5f;
    float dotedLinePhaseRatio = 1f;

    float dotedLineMitterLimit = 1f;
    float dotedLinePhase = 0.5f;

    BasicStroke lastMoveStroke;
    BasicStroke evolveBoardStroke;
    BasicStroke circleHighlightStroke;
    BasicStroke eatenBoardStroke;

    // Dessin des formes dans le cercle
    private final float circleHexagonHeightRatio = 0.04708f;
    private final float circleInterHexagonDistance = 0.04077f;
    private float circleStoneDistance;
    float circleStoneOffset;

    private final Point2D.Float[] shapeOriginPoints;

    private final imageRatio[] shapePositionRatios = new imageRatio[]{
            new imageRatio(0.48825f, 0.07045f), // 0
            new imageRatio(0.68074f, 0.07300f), // 1
            new imageRatio(0.84497f, 0.25152f), // 2
            new imageRatio(0.91086f, 0.47923f), // 3
            new imageRatio(0.87220f, 0.69573f), // 4
            new imageRatio(0.65734f, 0.84806f), // 5
            new imageRatio(0.43349f, 0.88752f), // 6
            new imageRatio(0.22379f, 0.80887f), // 7
            new imageRatio(0.09705f, 0.68408f), // 8
            new imageRatio(0.08924f, 0.46216f), // 9
            new imageRatio(0.11971f, 0.25825f), // 10
            new imageRatio(0.29032f, 0.11066f), // 11
    };

    private final Map<Integer, Integer> circleShapeTypeIds = Map.ofEntries(
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

        shapeOriginPoints = new Point2D.Float[12];

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
        imgStoneHover = (BufferedImage) readImage("stone_hover");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if(requireCalculation)
            recalculate();

        drawBoard(g2d);
        drawStones(g2d);

        if(match.isReviewModeActive() && match.canUndo()){
            drawLastMoveHighlight(g2d);
        }

        else
        {
            if(match.isGameOver())
                return;

            if(match.canUndo())
                drawLastMoveHighlight(g2d);

            if(drawSelected(g2d))
                drawFeedforward(g2d);
        }

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

        // Calcule taille de l'image des pierres du cercle
        boardStoneImageSize = Math.round(boardHexagonHeightRatio * imageHeight);
        circleStoneImageSize = Math.round(circleHexagonHeightRatio * imageHeight);

        boardHexagonOuterRadius = (float) boardStoneImageSize / 2;
        boardHexagonInnerRadius = boardHexagonOuterRadius * OUTER_TO_INNER_RADIUS_RATIO;

        // Calcule taille de l'image des pierres du cercle
        circleStoneDistance = circleInterHexagonDistance * imageWidth;
        circleStoneImageSize = Math.round(circleHexagonHeightRatio * imageHeight);

        circleHexagonOuterRadius = (float) circleStoneImageSize / 2;
        circleHexagonInnerRadius = circleHexagonOuterRadius * OUTER_TO_INNER_RADIUS_RATIO;

        circleStoneOffset = (float) circleStoneImageSize / 2;
        for (int i = 0; i < 12; i++) {
            var ratio = shapePositionRatios[i];
            shapeOriginPoints[i] = new Point2D.Float(boardX0 + imageWidth * ratio.xRatio(), boardY0 + imageHeight * ratio.yRatio());
        }

        lastMoveHighlighThickness = imageWidth * lastMoveThicknessRatio;
        boardEvolveHighlightThickness = imageWidth * boardEvolveThicknessRatio;
        boardEatenHighlightThickness = imageWidth * boardEatenThicknessRatio;
        circleHighlightThickness = imageWidth * circleThicknessRatio;

        float[] dotedLigneDashPattern = {imageWidth * dotedLineDashPatternRatio, imageWidth * dotedLineDashPatternRatio * dotedLineSpaceRatio};
        if(dotedLigneDashPattern[0] == 0.0f && dotedLigneDashPattern[1] == 0.0f){
            dotedLigneDashPattern[0] = 10.0f;
            dotedLigneDashPattern[1] = 30.0f;
        }

        dotedLinePhase = dotedLigneDashPattern[0] * dotedLinePhaseRatio;

        lastMoveStroke = new BasicStroke(
                lastMoveHighlighThickness,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                dotedLineMitterLimit,
                dotedLigneDashPattern,
                dotedLinePhase);

        evolveBoardStroke = new BasicStroke(boardEvolveHighlightThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        eatenBoardStroke = new BasicStroke(boardEatenHighlightThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        circleHighlightStroke = new BasicStroke(circleHighlightThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        requireCalculation = false;
    }


    private void drawBoard(Graphics2D g2d){
        g2d.drawImage(imgPlateau, boardX0, boardY0, Math.round(imageWidth), Math.round(imageHeight), null);
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
                        drawStone(g2d, imgStonePlayer1, drawPos.x, drawPos.y, boardStoneImageSize);
                        break;
                    case 2:
                        drawStone(g2d, imgStonePlayer2, drawPos.x, drawPos.y, boardStoneImageSize);
                        break;
                    case -1:
                        if(match.getCurrentPlayerIndex() == 1)
                            continue;
                        drawStone(g2d, imgStoneDisabled, drawPos.x, drawPos.y, boardStoneImageSize);
                        break;
                    case -2:
                        if(match.getCurrentPlayerIndex() == 0)
                            continue;
                        drawStone(g2d, imgStoneDisabled, drawPos.x, drawPos.y, boardStoneImageSize);
                    case -3:
                        drawStone(g2d, imgStoneDisabled, drawPos.x, drawPos.y, boardStoneImageSize);
                }
            }
        }
    }

    private void drawTileCenter(Graphics2D g2d, int n, int m){
        int x = nToX(n, m);
        int y = mToY(m);
        g2d.drawRect(x - 1, y - 1, 2, 2);
    }

    private void drawLastMoveHighlight(Graphics2D g2d){
        var lastMove = match.getLastMove();
        Point2D.Float origin = new Point2D.Float(nToX(lastMove.getColumn(), lastMove.getLine()), mToY(lastMove.getLine()));
        drawHighlight(g2d, Set.of(new Coordinate(0, 0)), origin, boardHexagonInnerRadius, boardHexagonOuterRadius, UIColor.lastMoveColor, lastMoveStroke);
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
        {
            if(contentType > 0){
                Critter critter = match.getCritterAtCoord(new Coordinate(n, m));

                if(showBoardHoverHighlight)
                    drawBoardCritterHighlight(g2d, critter.stonesCoordinates(), UIColor.hoverColor, evolveBoardStroke);

                Set<Coordinate> coords = ShapeUtils.getShapeCoordinatesForId(critter.type(), circleShapeTypeIds.get(critter.type()));
                drawCircleShape(g2d, critter.type(),useNeutralStoneImageForHoverInCircle? imgStoneDisabled : getPlayerImage(critter.player()));

                if(showCircleHoverHighlight)
                    drawHighlight(g2d, coords, shapeOriginPoints[critter.type()], circleHexagonInnerRadius, circleHexagonOuterRadius, UIColor.hoverColor, circleHighlightStroke);
            }
            return false;
        }

        Point drawPos = getStoneDrawPositions(n, m);

        switch (contentType){
            case 0:
                drawStone(g2d, match.getCurrentPlayerIndex() == 0? imgStonePlayer1Preview : imgStonePlayer2Preview, drawPos.x, drawPos.y, boardStoneImageSize);
                break;
            case -1:
                if(match.getCurrentPlayerIndex() == 0)
                    return false;
                drawStone(g2d, match.getCurrentPlayerIndex() == 0? imgStonePlayer1Preview : imgStonePlayer2Preview, drawPos.x, drawPos.y, boardStoneImageSize);
                break;
            case -2:
                if(match.getCurrentPlayerIndex() == 1)
                    return false;
                drawStone(g2d, match.getCurrentPlayerIndex() == 0? imgStonePlayer1Preview : imgStonePlayer2Preview, drawPos.x, drawPos.y, boardStoneImageSize);
                break;
        }

        return true;
    }

    private void drawFeedforward(Graphics2D g2d){
        Coordinate selectedCoordinate = new Coordinate(getnSelected(), getmSelected());
        Set<Critter> playerNeighbors = match.getPlayerNeighborsCritters(match.getCurrentPlayerIndex(), selectedCoordinate);
        int evolveInto = 0;
        Set<Coordinate> evolveCoords = new HashSet<>();
        evolveCoords.add(selectedCoordinate);

        // Est-ce qu'il y a des voisins a faire évoluer
        if(!playerNeighbors.isEmpty()) {
            for (Critter critter : playerNeighbors) {
                evolveCoords.addAll(critter.stonesCoordinates());
            }

            evolveInto = ShapeUtils.getShapeId(evolveCoords);
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
                        if(showBoardHightlightEffect)
                            drawBoardCritterHighlight(g2d, critter.stonesCoordinates(), UIColor.eatenColor, eatenBoardStroke);
                        eatenShape = critter.type();
                    }
                }
            }
            if(eatenShape >= 0)
                drawCircleShapeWithHighlight(g2d, eatenShape, UIColor.eatenColor, circleHighlightThickness, getPlayerImage(match.getOpponentPlayerIndex()));
        }

        if(showBoardHightlightEffect)
            drawBoardCritterHighlight(g2d, evolveCoords, UIColor.evolveColor, evolveBoardStroke);
        drawCircleShapeWithHighlight(g2d, evolveInto, UIColor.evolveColor, circleHighlightThickness, getPlayerImage(match.getCurrentPlayerIndex()));
    }

    private void drawCircleShape(Graphics2D g2d, int shapeType, Image img){
        Set<Coordinate> coords = ShapeUtils.getShapeCoordinatesForId(shapeType, circleShapeTypeIds.get(shapeType));

        for(Coordinate coord : coords){
            Point2D.Float stoneCenter = getShapeStoneCenterPosition(shapeOriginPoints[shapeType], coord, circleStoneDistance);
            int x = Math.round(stoneCenter.x - circleStoneOffset);
            int y = Math.round(stoneCenter.y - circleStoneOffset);
            drawStone(g2d, img, x, y, circleStoneImageSize);
        }
    }

    private void drawCircleShapeWithHighlight(Graphics2D g2d, int shapeType, Color color, float thickness, Image img){
        drawCircleShape(g2d, shapeType, img);
        Set<Coordinate> coords = ShapeUtils.getShapeCoordinatesForId(shapeType, circleShapeTypeIds.get(shapeType));
        drawHighlight(g2d, coords, shapeOriginPoints[shapeType], circleHexagonInnerRadius, circleHexagonOuterRadius, color, circleHighlightStroke);
    }

    private Point2D.Float getShapeStoneCenterPosition(Point2D.Float shapeOrigin, Coordinate relativeOffset, float distance){
        float x = shapeOrigin.x + (relativeOffset.col() * distance - relativeOffset.line() * distance / 2);
        float y = (shapeOrigin.y + ((float)(Math.sqrt(3) * distance * relativeOffset.line()) / 2));
        return new Point2D.Float(x, y);
    }

    private void drawBoardCritterHighlight(Graphics2D g2d, Set<Coordinate> coordinates, Color color, Stroke stroke){
        Set<Coordinate> normalizedCoordinates = ShapeUtils.normalizeCoordinate(coordinates);
        var shapeOriginCoordinates = ShapeUtils.getTopLeftCoordinate(coordinates);
        Point2D.Float shapeOriginPos = new Point2D.Float(
                nToX(shapeOriginCoordinates.col(), shapeOriginCoordinates.line()),
                mToY(shapeOriginCoordinates.line())
        );

        drawHighlight(g2d, normalizedCoordinates, shapeOriginPos, boardHexagonInnerRadius, boardHexagonOuterRadius, color, stroke);
    }

    private Image getPlayerImage(int player){
        return player == 0? imgStonePlayer1 : imgStonePlayer2;
    }

    private void drawHighlight(
            Graphics2D g2d,
            Set<Coordinate> coordinates,
            Point2D.Float shapeOrigin,
            float innerRadius,
            float outerRadius,
            Color highlightColor,
            Stroke stroke)
    {
        var previousColor = g2d.getColor();
        var previousStroke = g2d.getStroke();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(stroke);
        g2d.setColor(highlightColor);

        for (Coordinate coord : coordinates) {
            Coordinate[] neighborCoordinate = new Coordinate[]{
                    new Coordinate(coord.col() - 1, coord.line()),
                    new Coordinate(coord.col() - 1, coord.line() - 1),
                    new Coordinate(coord.col(), coord.line() - 1),
                    new Coordinate(coord.col() + 1, coord.line()),
                    new Coordinate(coord.col() + 1, coord.line() + 1),
                    new Coordinate(coord.col(), coord.line() + 1),
            };

            Point2D.Float center = getShapeStoneCenterPosition(shapeOrigin, coord, innerRadius * 2);

            Point2D.Float[] corners = new Point2D.Float[] {
                    new Point2D.Float((center.x - innerRadius), center.y + 0.5f * outerRadius),
                    new Point2D.Float(center.x - innerRadius, center.y - 0.5f * outerRadius),
                    new Point2D.Float(center.x, center.y - outerRadius),
                    new Point2D.Float(center.x + innerRadius, center.y - 0.5f * outerRadius),
                    new Point2D.Float(center.x + innerRadius, center.y + 0.5f * outerRadius),
                    new Point2D.Float(center.x, center.y + outerRadius)
            };

            for (int j = 0; j < 6; j++) {
                Coordinate neighborCoord = neighborCoordinate[j];
                if(!coordinates.contains(neighborCoord)){
                    g2d.drawLine(Math.round(corners[j].x), Math.round(corners[j].y), Math.round(corners[(j + 1) % 6].x), Math.round(corners[(j + 1) % 6].y));
                }
            }
        }

        g2d.setColor(previousColor);
        g2d.setStroke(previousStroke);
    }

    private Point getStoneDrawPositions(int n, int m){
        Coordinate pixel = tileToPixel(new Coordinate(n, m));
        int x = pixel.col() - Math.round((float) boardStoneImageSize / 2);
        int y = pixel.line() - Math.round((float) boardStoneImageSize / 2);
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
            drawStone(g2d, imgEatenStone, drawPos.x, drawPos.y, boardStoneImageSize);
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
