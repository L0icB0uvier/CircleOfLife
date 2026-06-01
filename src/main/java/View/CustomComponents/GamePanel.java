package View.CustomComponents;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.awt.geom.Path2D;
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
            imgBackgroundPlateau,
            imgStonePlayer1,
            imgStonePlayer2,
            imgStonePlayer1LastMove,
            imgStonePlayer2LastMove,
            imgStonePlayer1Preview,
            imgStonePlayer2Preview,
            imgDisabledStone;

    int imgSrcHeight, imgSrcWidth;
    float imageWidth, imageHeight;
    int mouseX, mouseY;
    int nSelected, mSelected;

    boolean requireCalculation = true;
    boolean drawCenter = false;

    float alpha = 1;
    float oneMinusAlpha;

    private final imageRatio boardOriginRatio = new imageRatio(0.35505f, 0.24893f);
    float distance;
    int boardStoneImageSize, circleStoneImageSize;
    int boardX0, boardY0;
    int x0, y0;
    float boardHexagonInnerRadius, boardHexagonOuterRadius;
    float circleHexagonInnerRadius, circleHexagonOuterRadius;

    private boolean showHoverHighlight = true;
    private boolean showFeedforwardHighlight = true;
    private boolean showUnplayableMoves = true;
    private boolean showBlockingCrittersHighlight = true;
    private boolean showEatenCrittersFeedback = true;
    private boolean showScoreAnimation = true;

    float dotedLineDashPatternRatio = 0.0085f;

    float circleDotedLineDashPatternRatio = 0.006f;

    float dotedLineSpaceRatio = 0.5f;
    float circleDotedLineSpaceRatio = 0.35f;

    float dotedLinePhaseRatio = 1f;

    float dotedLineMitterLimit = 1f;
    float dotedLinePhase = 0.5f;

    float animationTextHighlightRatio = 0.01f;
    float animationTextHighlightStroke;

    float scoreAnimationTravelRatio = 0.2f;
    float scoreAnimationTravelDistance;

    float moveImpossibleAnimationTravelRatio = 0.1f;
    float moveImpossibleAnimationTravelDistance;

    BasicStroke dotedStroke,circleDotedStroke ;
    BasicStroke boardHighlightStroke;
    BasicStroke circleHighlightStroke;

    private float circleStoneDistance;
    private float circleStoneOffset;

    private final Point2D.Float[] shapeOriginPoints;

    private final Map<Set<Coordinate>, ScoreAnimation> scoreAnimations;
    private float animationOffset = 0;
    private Font scoreAnimationFont;
    private final Map<String, ImpossibleMoveAnimation> impossibleMoveAnimations;

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

        game.addUpdateObserver(this);
        match = game.getMatch();

        scoreAnimations = new HashMap<>();
        impossibleMoveAnimations = new HashMap<>();

        shapeOriginPoints = new Point2D.Float[12];

        nSelected = -1;
        mSelected = -1;
        oneMinusAlpha = 1 - alpha;

        loadImages();

        imgSrcHeight = imgPlateau.getHeight();
        imgSrcWidth = imgPlateau.getWidth();
    }

    /**
     * Charge toutes les images nécessaires pour dessiner le niveau.
     */
    private void loadImages() {
        imgPlateau= (BufferedImage) Configuration.loadImage("Plateau_fleches.png");
        imgBackgroundPlateau = (BufferedImage) Configuration.loadImage("Board_Background.png");
        imgStonePlayer1 = (BufferedImage) Configuration.loadImage("Blue_Stone.png");
        imgStonePlayer2 = (BufferedImage) Configuration.loadImage("Red_Stone.png");
        imgStonePlayer1LastMove = (BufferedImage) Configuration.loadImage("Blue_Stone_Last_Move.png");
        imgStonePlayer2LastMove = (BufferedImage) Configuration.loadImage("Red_Stone_Last_Move.png");
        imgStonePlayer1Preview = (BufferedImage) Configuration.loadImage("Blue_Stone_transparent.png");
        imgStonePlayer2Preview = (BufferedImage) Configuration.loadImage("Red_Stone_transparent.png");
        imgDisabledStone = (BufferedImage) Configuration.loadImage("Disabled_Stone.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(this.getBackground());
        g2d.fillRoundRect(5, 5, getWidth()-10, getHeight()-10, 15, 15);

        if(requireCalculation)
            recalculate();

        drawBoardBackground(g2d);
        drawBoard(g2d);
        drawStones(g2d);

        if(showEatenCrittersFeedback && (!match.isGameOver() || match.isReviewModeActive()))
            drawEaten(g2d);

        if(!match.isGameOver())
        {
            if(drawSelected(g2d))
                drawFeedforward(g2d);
        }

        drawScoreAnimations(g2d);
        drawImpossibleMoveAnimation(g2d);

        super.paintBorder(g2d);

        g2d.dispose();
    }

    /**
     * Recalcule les informations nécessaires pour pouvoir dessiner les différents éléments du plateau.
     */
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
        x0 = Math.round(boardX0 + (boardOriginRatio.xRatio() * imageWidth));
        y0 = Math.round(boardY0 + (boardOriginRatio.yRatio() * imageHeight));
        float ratioDistanceX = 0.07248f;
        distance = ratioDistanceX * imageWidth;

        // Calcule taille de l'image des pierres du cercle
        float boardHexagonHeightRatio = 0.08461f;
        boardStoneImageSize = Math.round(boardHexagonHeightRatio * imageHeight);

        // Dessin des formes dans le cercle
        float circleHexagonHeightRatio = 0.04708f;
        circleStoneImageSize = Math.round(circleHexagonHeightRatio * imageHeight);

        boardHexagonOuterRadius = (float) boardStoneImageSize / 2;
        boardHexagonInnerRadius = boardHexagonOuterRadius * OUTER_TO_INNER_RADIUS_RATIO;

        // Calcule taille de l'image des pierres du cercle
        float circleInterHexagonDistance = 0.04077f;
        circleStoneDistance = circleInterHexagonDistance * imageWidth;
        circleStoneImageSize = Math.round(circleHexagonHeightRatio * imageHeight);

        circleHexagonOuterRadius = (float) circleStoneImageSize / 2;
        circleHexagonInnerRadius = circleHexagonOuterRadius * OUTER_TO_INNER_RADIUS_RATIO;

        circleStoneOffset = (float) circleStoneImageSize / 2;
        for (int i = 0; i < 12; i++) {
            var ratio = shapePositionRatios[i];
            shapeOriginPoints[i] = new Point2D.Float(boardX0 + imageWidth * ratio.xRatio(), boardY0 + imageHeight * ratio.yRatio());
        }

        float dotedThicknessRatio = 0.005f;
        float dotedHighlightThickness = imageWidth * dotedThicknessRatio;
        float boardThicknessRatio = 0.005f;
        float boardHighlightThickness = imageWidth * boardThicknessRatio;
        float circleThicknessRatio = 0.0035f;
        float circleHighlightThickness = imageWidth * circleThicknessRatio;

        float[] dotedLigneDashPattern = {imageWidth * dotedLineDashPatternRatio, imageWidth * dotedLineDashPatternRatio * dotedLineSpaceRatio};
        if(dotedLigneDashPattern[0] == 0.0f && dotedLigneDashPattern[1] == 0.0f){
            dotedLigneDashPattern[0] = 10.0f;
            dotedLigneDashPattern[1] = 30.0f;
        }
        float[] circleDotedLigneDashPattern = {imageWidth * circleDotedLineDashPatternRatio, imageWidth * circleDotedLineDashPatternRatio * circleDotedLineSpaceRatio};
        if(circleDotedLigneDashPattern[0] == 0.0f && circleDotedLigneDashPattern[1] == 0.0f){
            dotedLigneDashPattern[0] = 7.0f;
            dotedLigneDashPattern[1] = 21.0f;
        }

        dotedLinePhase = dotedLigneDashPattern[0] * dotedLinePhaseRatio;

        dotedStroke = new BasicStroke(
                dotedHighlightThickness,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                dotedLineMitterLimit,
                dotedLigneDashPattern,
                dotedLinePhase);

        circleDotedStroke = new BasicStroke(
                circleHighlightThickness,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                dotedLineMitterLimit,
                circleDotedLigneDashPattern,
                dotedLinePhase);

        boardHighlightStroke = new BasicStroke(boardHighlightThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        circleHighlightStroke = new BasicStroke(circleHighlightThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        scoreAnimationTravelDistance = height * scoreAnimationTravelRatio;
        moveImpossibleAnimationTravelDistance = height * moveImpossibleAnimationTravelRatio;
        scoreAnimationFont = new Font("Arial", Font.BOLD, (int) (0.05 * getHeight()));
        animationOffset = (float)boardStoneImageSize / 2;
        animationTextHighlightStroke = imageWidth * animationTextHighlightRatio;

        requireCalculation = false;
    }

    private void drawBoardBackground(Graphics2D g2d) {
        g2d.drawImage(imgBackgroundPlateau,3,3, this.getWidth()-6, this.getHeight()-6, null);
    }

    /**
     * Dessine le plateau de jeu.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
     */
    private void drawBoard(Graphics2D g2d){
        g2d.drawImage(imgPlateau, boardX0, boardY0, Math.round(imageWidth), Math.round(imageHeight), null);
    }

    /**
     * Dessine les pierres sur le plateau.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
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
                        drawStone(g2d, isLastMove(n, m)? imgStonePlayer1LastMove : imgStonePlayer1, drawPos.x, drawPos.y, boardStoneImageSize);
                        break;
                    case 2:
                        drawStone(g2d, isLastMove(n, m)? imgStonePlayer2LastMove : imgStonePlayer2, drawPos.x, drawPos.y, boardStoneImageSize);
                        break;
                    case -1:
                        if(match.isGameOver() || showUnplayableMoves == false || match.getCurrentPlayerIndex() == 1)
                            continue;
                        drawStone(g2d, imgDisabledStone, drawPos.x, drawPos.y, boardStoneImageSize);
                        break;
                    case -2:
                        if(match.isGameOver() || showUnplayableMoves == false || match.getCurrentPlayerIndex() == 0)
                            continue;
                        drawStone(g2d, imgDisabledStone, drawPos.x, drawPos.y, boardStoneImageSize);
                        break;
                    case -3:
                        if(match.isGameOver() || showUnplayableMoves == false)
                            continue;
                        drawStone(g2d, imgDisabledStone, drawPos.x, drawPos.y, boardStoneImageSize);
                        break;
                }
            }
        }
    }

    /**
     * Retourne si les coordonnées correspondent au dernier coup joué.
     * @param n La colonne du coup joué.
     * @param m La ligne du coup joué.
     * @return true s'il s'agit du dernier coup joué, false sinon.
     */
    private boolean isLastMove(int n, int m){
        Move lastMove = match.getLastMove();
        return lastMove.getColumn() == n && lastMove.getLine() == m;
    }

    /**
     * Dessine un point au centre d'une case du plateau. Utilisé pour debugger.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
     * @param n La colonne de la case.
     * @param m La ligne de la case.
     */
    private void drawTileCenter(Graphics2D g2d, int n, int m){
        int x = nToX(n, m);
        int y = mToY(m);
        g2d.drawRect(x - 1, y - 1, 2, 2);
    }

    /**
     * Dessine la pierre sous le curseur du joueur actif.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
     * @return true si a dessiné une pierre, faux sinon
     */
    private boolean drawSelected(Graphics2D g2d){
        int m = getMSelected();
        int n = getNSelected();
        if (n == -1 || m == -1) return false;

        Coordinate selectedCoordinate = new Coordinate(n, m);

        int contentType = match.getContentAt(m, n);
        if(contentType > 0 || contentType == -3)
        {
            if(contentType > 0){
                Critter critter = match.getCritterAtCoord(selectedCoordinate);

                Set<Coordinate> coords = CritterUtils.getCritterTypeCoordinates(critter.type(), circleShapeTypeIds.get(critter.type()));
                drawCircleShape(g2d, critter.type(), getPlayerImage(critter.player()));

                if(showHoverHighlight){
                    drawBoardCoordinatesHighlight(g2d, critter.stonesCoordinates(), UIColor.HOVER_COLOR, boardHighlightStroke);

                    drawHighlight(g2d, coords, shapeOriginPoints[critter.type()], circleHexagonInnerRadius, circleHexagonOuterRadius, 0, UIColor.HOVER_COLOR, circleHighlightStroke);
                }
            }
            if(contentType == -3){
                drawBlockingCrittersHighlight(g2d, selectedCoordinate);
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
                {
                    drawBlockingCrittersHighlight(g2d, selectedCoordinate);
                    return false;
                }
                drawStone(g2d, match.getCurrentPlayerIndex() == 0? imgStonePlayer1Preview : imgStonePlayer2Preview, drawPos.x, drawPos.y, boardStoneImageSize);
                break;
            case -2:
                if(match.getCurrentPlayerIndex() == 1){
                    drawBlockingCrittersHighlight(g2d, selectedCoordinate);
                    return false;
                }
                drawStone(g2d, match.getCurrentPlayerIndex() == 0? imgStonePlayer1Preview : imgStonePlayer2Preview, drawPos.x, drawPos.y, boardStoneImageSize);
                break;
        }

        return true;
    }

    /**
     * Dessine les contours des critters empêchant une pose de pierre sur une case du plateau.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
     * @param selectedCoordinate Les coordonnées de la case du plateau pour laquelle on veut afficher le feedback.
     */
    private void drawBlockingCrittersHighlight(Graphics2D g2d, Coordinate selectedCoordinate) {
        if(!showBlockingCrittersHighlight) return;
        var neighborCritters = match.getPlayerNeighborsCritters(match.getCurrentPlayerIndex(), selectedCoordinate);
        for (Critter critter : neighborCritters) {
            drawBoardCoordinatesHighlight(g2d, critter.stonesCoordinates(), UIColor.BLOCKING_CRITTER_COLOR, boardHighlightStroke);
        }
    }

    /**
     * Dessine les feedforward permettant au joueur de visualiser les conséquences d'une pose de pierre sur une case.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
     */
    private void drawFeedforward(Graphics2D g2d){
        Coordinate selectedCoordinate = new Coordinate(getNSelected(), getMSelected());
        Set<Critter> playerNeighbors = match.getPlayerNeighborsCritters(match.getCurrentPlayerIndex(), selectedCoordinate);
        int evolveInto = 0;
        Set<Coordinate> evolveCoords = new HashSet<>();
        evolveCoords.add(selectedCoordinate);

        // Est-ce qu'il y a des voisins à faire évoluer
        if(!playerNeighbors.isEmpty()) {
            for (Critter critter : playerNeighbors) {
                evolveCoords.addAll(critter.stonesCoordinates());
            }

            evolveInto = CritterUtils.getCritterId(evolveCoords);
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
                        if(showFeedforwardHighlight)
                            drawBoardCoordinatesHighlight(g2d, critter.stonesCoordinates(), UIColor.EATEN_COLOR, dotedStroke);
                        eatenShape = critter.type();
                    }
                }
            }
            if(eatenShape >= 0)
                drawCircleShapeWithHighlight(g2d, eatenShape, UIColor.EATEN_COLOR, getPlayerImage(match.getOpponentPlayerIndex()));
        }

        if(showFeedforwardHighlight)
            drawBoardCoordinatesHighlight(g2d, evolveCoords, UIColor.EVOLVE_COLOR, boardHighlightStroke);

        drawCircleShapeWithHighlight(g2d, evolveInto, UIColor.EVOLVE_COLOR, getPlayerImage(match.getCurrentPlayerIndex()));
    }

    /**
     * Dessine le critter sur le cercle autour du plateau avec un contour.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
     * @param shapeType Le type de critter à utiliser.
     * @param color La couleur du contour.
     * @param img L'image à utiliser pour dessiner les pierres du critter.
     */
    private void drawCircleShapeWithHighlight(Graphics2D g2d, int shapeType, Color color, Image img){
        drawCircleShape(g2d, shapeType, img);
        Set<Coordinate> coords = CritterUtils.getCritterTypeCoordinates(shapeType, circleShapeTypeIds.get(shapeType));
        if(showFeedforwardHighlight)
            if(color == UIColor.EATEN_COLOR) {
                drawHighlight(g2d, coords, shapeOriginPoints[shapeType], circleHexagonInnerRadius, circleHexagonOuterRadius,0, color, circleDotedStroke);

            }else {
                drawHighlight(g2d, coords, shapeOriginPoints[shapeType], circleHexagonInnerRadius, circleHexagonOuterRadius,0, color, circleHighlightStroke);

            }
    }

    /**
     * Dessine le critter sur le cercle autour du plateau.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
     * @param shapeType Le type de critter à utiliser.
     * @param img L'image à utiliser pour dessiner les pierres du critter.
     */
    private void drawCircleShape(Graphics2D g2d, int shapeType, Image img){
        Set<Coordinate> coords = CritterUtils.getCritterTypeCoordinates(shapeType, circleShapeTypeIds.get(shapeType));

        for(Coordinate coord : coords){
            Point2D.Float stoneCenter = getShapeStoneCenterPosition(shapeOriginPoints[shapeType], coord, circleStoneDistance);
            int x = Math.round(stoneCenter.x - circleStoneOffset);
            int y = Math.round(stoneCenter.y - circleStoneOffset);
            drawStone(g2d, img, x, y, circleStoneImageSize);
        }
    }

    /**
     * Récupère le centre d'une pierre dans le référentiel Swing par rapport à une position de référence.
     * @param shapeOrigin L'origine du critter dans le référentiel Swing.
     * @param relativeOffset Les coordonnées relatives à l'origine du critter.
     * @param distance La distance entre les pierres.
     * @return le centre de la pierre dans le référentiel Swing.
     */
    private Point2D.Float getShapeStoneCenterPosition(Point2D.Float shapeOrigin, Coordinate relativeOffset, float distance){
        float x = shapeOrigin.x + (relativeOffset.col() * distance - relativeOffset.line() * distance / 2);
        float y = (shapeOrigin.y + ((float)(Math.sqrt(3) * distance * relativeOffset.line()) / 2));
        return new Point2D.Float(x, y);
    }

    /**
     * Dessine le contour d'un critter sur le plateau.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
     * @param coordinates Les coordonnées des pierres pour lesquelles dessiner le contour.
     * @param color La couleur du contour.
     * @param stroke Le stroke du contour.
     */
    private void drawBoardCoordinatesHighlight(Graphics2D g2d, Set<Coordinate> coordinates, Color color, BasicStroke stroke){
        Set<Coordinate> normalizedCoordinates = CritterUtils.normalizeCoordinate(coordinates);
        var shapeOriginCoordinates = CritterUtils.getTopLeftCoordinate(coordinates);
        Point2D.Float shapeOriginPos = new Point2D.Float(
                nToX(shapeOriginCoordinates.col(), shapeOriginCoordinates.line()),
                mToY(shapeOriginCoordinates.line())
        );

        drawHighlight(g2d, normalizedCoordinates, shapeOriginPos, boardHexagonInnerRadius, boardHexagonOuterRadius, stroke.getLineWidth() / 2 ,color, stroke);
    }

    /**
     * Retourne l'image associée à un joueur.
     * @param player Le joueur pour lequel on veut récupérer l'image.
     * @return L'image correspondant au joueur ou null si le joueur n'est pas valide.
     */
    private Image getPlayerImage(int player){
        if(player < 0 || player > 1)
            return null;
        return player == 0? imgStonePlayer1 : imgStonePlayer2;
    }

    /**
     * Dessine un contour autour d'un critter.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
     * @param coordinates Les coordonnées du critter normalisées.
     * @param shapeOrigin L'origine en pixels du critter.
     * @param innerRadius La taille du rayon intérieur de l'hexagone.
     * @param outerRadius La taille du rayon extérieur de l'hexagone.
     * @param offset L'offset à appliquer au contour. >0 décale le contour vers l'intérieur de l'hexagone.
     * @param highlightColor La couleur du contour.
     * @param stroke Le Stroke du contour.
     */
    private void drawHighlight(
            Graphics2D g2d,
            Set<Coordinate> coordinates,
            Point2D.Float shapeOrigin,
            float innerRadius,
            float outerRadius,
            float offset,
            Color highlightColor,
            BasicStroke stroke)
    {
        var previousColor = g2d.getColor();
        var previousStroke = g2d.getStroke();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(stroke);
        g2d.setColor(highlightColor);

        float cornerOffset = offset * 1.1547005f;
        float slideDistance = offset * 1.1547005f;

        Path2D.Float path = new Path2D.Float();

        for (Coordinate coord : coordinates) {
            Coordinate[] neighborCoordinate = new Coordinate[]{
                    new Coordinate(coord.col() - 1, coord.line()),
                    new Coordinate(coord.col() - 1, coord.line() - 1),
                    new Coordinate(coord.col(),     coord.line() - 1),
                    new Coordinate(coord.col() + 1, coord.line()),
                    new Coordinate(coord.col() + 1, coord.line() + 1),
                    new Coordinate(coord.col(),     coord.line() + 1),
            };

            Point2D.Float center = getShapeStoneCenterPosition(shapeOrigin, coord, innerRadius * 2);

            Point2D.Float[] originalCorners = new Point2D.Float[] {
                    new Point2D.Float(center.x - innerRadius, center.y + 0.5f * outerRadius), // 0
                    new Point2D.Float(center.x - innerRadius, center.y - 0.5f * outerRadius), // 1
                    new Point2D.Float(center.x, center.y - outerRadius),        // 2
                    new Point2D.Float(center.x + innerRadius, center.y - 0.5f * outerRadius), // 3
                    new Point2D.Float(center.x + innerRadius, center.y + 0.5f * outerRadius), // 4
                    new Point2D.Float(center.x,center.y + outerRadius)         // 5
            };

            Point2D.Float[] insetCorners = new Point2D.Float[6];

            for (int i = 0; i < 6; i++) {
                // Pas d'offset, on skip les calculs
                if (offset == 0) {
                    insetCorners[i] = originalCorners[i];
                }

                boolean hasNeighborLeft = coordinates.contains(neighborCoordinate[(i + 5) % 6]);
                boolean hasNeighborRight = coordinates.contains(neighborCoordinate[i]);

                // Le corner est entouré de voisins, on ne le dessinera pas
                if (hasNeighborLeft && hasNeighborRight)
                    insetCorners[i] = originalCorners[i];

                //Le corner n'a aucun voisin, on le dessin avec un inset vers l'intérieur
                else if (!hasNeighborLeft && !hasNeighborRight) {
                    float dx = center.x - originalCorners[i].x;
                    float dy = center.y - originalCorners[i].y;
                    float length = (float) Math.sqrt(dx * dx + dy * dy);

                    if (length > 0) {
                        float ux = dx / length;
                        float uy = dy / length;
                        insetCorners[i] = new Point2D.Float(
                                originalCorners[i].x + ux * cornerOffset,
                                originalCorners[i].y + uy * cornerOffset
                        );
                    } else {
                        insetCorners[i] = originalCorners[i];
                    }
                // Le corner a un voisin, on doit le slider dans la bonne direction
                } else {
                    int targetIndex = hasNeighborLeft ? (i + 5) % 6 : (i + 1) % 6;

                    Point2D.Float currentCorner = originalCorners[i];
                    Point2D.Float targetCorner = originalCorners[targetIndex];

                    float dx = targetCorner.x - currentCorner.x;
                    float dy = targetCorner.y - currentCorner.y;
                    float edgeLength = (float) Math.sqrt(dx * dx + dy * dy);

                    if (edgeLength > 0) {
                        float ux = dx / edgeLength;
                        float uy = dy / edgeLength;

                        insetCorners[i] = new Point2D.Float(
                                currentCorner.x + ux * slideDistance,
                                currentCorner.y + uy * slideDistance
                        );
                    } else {
                        insetCorners[i] = currentCorner;
                    }
                }
            }

            for (int j = 0; j < 6; j++) {
                Coordinate neighborCoord = neighborCoordinate[j];
                if (!coordinates.contains(neighborCoord)) {
                    path.moveTo(insetCorners[j].x, insetCorners[j].y);
                    path.lineTo(insetCorners[(j + 1) % 6].x, insetCorners[(j + 1) % 6].y);
                }
            }
        }

        g2d.draw(path);

        g2d.setColor(previousColor);
        g2d.setStroke(previousStroke);
    }

    /**
     * Récupère les coordonnées en pixels correspondantes au coin supérieur gauche de l'image d'une pierre du plateau.
     * @param n La colonne du plateau.
     * @param m La ligne du plateau
     * @return Les coordonnées en pixels où dessiner l'image.
     */
    private Point getStoneDrawPositions(int n, int m){
        Point pixel = tileToPixel(new Coordinate(n, m));
        int x = (int) pixel.getX() - Math.round((float) boardStoneImageSize / 2);
        int y = (int) pixel.getY() - Math.round((float) boardStoneImageSize / 2);
        return new Point(x, y);
    }

    /**
     * Draw the stones eaten at the previous turn
     * @param g2d Le Graphic à utiliser pour dessiner.
     */
    private void drawEaten(Graphics2D g2d){
        List<Coordinate> coordinates = match.getPreviouslyEatenCrittersCoordinates();
        if(coordinates.isEmpty()) return;
        Color col = getEatenStonesColor();
        drawBoardCoordinatesHighlight(g2d, new HashSet<>(coordinates), col, dotedStroke);
    }

    /**
     * Récupère la bonne couleur pour l'affichage du contour des pierres mangées.
     * @return La couleur correspondant à l'état du jeu.
     */
    private Color getEatenStonesColor() {
        Color col;
        if(match.isReviewModeActive()){
            col = match.getOpponentPlayerIndex() == 0? UIColor.BLUE : UIColor.RED;
        }
        else
            col = match.getOpponentPlayerIndex() == 0? UIColor.RED : UIColor.BLUE;
        return col;
    }

    /**
     * Dessine une pierre.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
     * @param img L'image à dessiner.
     * @param x La position x dans le référentiel Swing.
     * @param y La position y dans le référentiel Swing.
     * @param size La taille de l'image en pixels.
     */
    private void drawStone(Graphics2D g2d, Image img, int x, int y, int size){
        g2d.drawImage(img, x, y, size, size, null);
    }

    private void drawImpossibleMoveAnimation(Graphics2D g2d) {
        if(impossibleMoveAnimations.isEmpty())
            return;

        Font prevFont = g2d.getFont();
        g2d.setFont(scoreAnimationFont);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (var entry : impossibleMoveAnimations.entrySet()) {
            Point pixel = tileToPixel(new Coordinate(entry.getValue().c, entry.getValue().l));
            int yPos = Math.round(pixel.y - animationOffset - (entry.getValue().progress * moveImpossibleAnimationTravelDistance));
            pixel.setLocation(pixel.x, yPos);

            String text = "Impossible de jouer ici";
            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
            int x = pixel.x - (metrics.stringWidth(text) / 2);
            int y = Math.round(pixel.y - (entry.getValue().progress * moveImpossibleAnimationTravelDistance));

            printScoreGainedText(g2d, text, x, y, Color.BLACK ,1 - entry.getValue().progress);
        }
        g2d.setFont(prevFont);
    }


    /**
     * Dessine les animations de score.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
     */
    private void drawScoreAnimations(Graphics2D g2d) {
        if(!showScoreAnimation || scoreAnimations.isEmpty())
            return;

        Font prevFont = g2d.getFont();
        g2d.setFont(scoreAnimationFont);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (Map.Entry<Set<Coordinate>, ScoreAnimation> entry : scoreAnimations.entrySet()) {

            String text = String.format("+%d", entry.getValue().scoreGained);

            Point pixelPos = calculateAverageStonePosition(entry.getKey());
            int yPos = Math.round(pixelPos.y - animationOffset - (entry.getValue().progress * scoreAnimationTravelDistance));
            pixelPos.setLocation(pixelPos.x, yPos);

            FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
            int x = pixelPos.x - (metrics.stringWidth(text) / 2);
            int y = Math.round(pixelPos.y - (entry.getValue().progress * scoreAnimationTravelDistance));
            printScoreGainedText(g2d, text, x, y, entry.getValue().player == 0 ? UIColor.BLUE : UIColor.RED ,1 - entry.getValue().progress);
        }

        g2d.setFont(prevFont);
    }

    /**
     * Calcule la position moyenne en pixel d'un ensemble de pierre du plateau.
     * @param coords Les coordonnées des pierres sur plateau.
     * @return La position moyenne en pixels.
     */
    private Point calculateAverageStonePosition(Set<Coordinate> coords){
        int sumX = 0;
        int sumY = 0;

        for (Coordinate coord : coords) {
            Point position = tileToPixel(coord);
            sumX += position.x;
            sumY += position.y;
        }

        int averageX = sumX / coords.size();
        int averageY = sumY / coords.size();

        return new Point(averageX, averageY);
    }

    /**
     * Dessine un texte aux positions données.
     * @param g2d Le composant Graphic à utiliser pour dessiner.
     * @param text Le texte à dessiner.
     * @param x La position x où dessiner dans le référentiel Swing.
     * @param y La position y où dessiner dans le référentiel Swing.
     * @param textColor La couleur à utiliser pour dessiner.
     * @param alpha L'alpha à appliquer à la couleur.
     */
    private void printScoreGainedText(Graphics2D g2d, String text, int x, int y, Color textColor, float alpha) {
        if (text == null || text.isEmpty()) return;

        Color prevCol = g2d.getColor();
        Stroke prevStroke = g2d.getStroke();

        int alphaInt = Math.round(255 * alpha);
        Color fillColor = new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), alphaInt);
        Color outlineBaseColor = UIColor.SCORE_ANIMATION_HIGHLIGHT_COLOR;
        Color outlineColor = new Color(outlineBaseColor.getRed(), outlineBaseColor.getGreen(), outlineBaseColor.getBlue(), alphaInt); // Contour noir (0, 0, 0)

        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout textLayout = new TextLayout(text, g2d.getFont(), frc);

        g2d.translate(x, y);
        Shape textShape = textLayout.getOutline(null);

        g2d.setColor(outlineColor);

        g2d.setStroke(new BasicStroke(animationTextHighlightStroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(textShape);

        g2d.setColor(fillColor);
        g2d.fill(textShape);

        // 5. Restauration de l'état d'origine du Graphics2D
        g2d.translate(-x, -y); // Remise à zéro du repère
        g2d.setColor(prevCol);
        g2d.setStroke(prevStroke);
    }

    @Override
    public void update() {
        repaint();
    }

    /**
     * Met à jour le position du curseur de la souris et converti cette position en ligne et colonnes sur le plateau.
     * @param x La position x de la souris dans le référentiel Swing.
     * @param y La position y de la souris dans le référentiel Swing.
     */
    public void updateMousePosition(int x, int y) {
        mouseX = x;
        mouseY = y;
        Coordinate mouseToTile = pixelToTile(new Point(mouseX, mouseY));
        int n = mouseToTile.col();
        int m = mouseToTile.line();

        if (MatchUtils.isInsideBoard(new Coordinate(m, n))) {
            if((n != nSelected || m != mSelected)) {
                nSelected = n;
                mSelected = m;
                repaint();
            } 
        } else {
            nSelected = -1;
            mSelected = -1;
            repaint();
        }
    }

    /**
     * Converti une colonne en sa position x dans le référentiel Swing.
     * @param n La colonne de la tuile dans le référentiel hexagonale.
     * @param m La ligne de la tuile dans le référentiel hexagonale.
     * @return La position horizontale en pixels dans le référentiel Swing.
     */
    public int nToX(int n, int m) {
        return (int) Math.round(x0 + (distance * ((double) n - ((double) m / 2))));
    }

    /**
     * Converti une ligne en sa position y dans le référentiel Swing.
     * @param m La ligne de la tuile dans le référentiel hexagonale.
     * @return La position verticale en pixels dans le référentiel Swing.
     */
    public int mToY(int m) {
        return (int) Math.round(y0 + ((m * Math.sqrt(3) * distance) / 2));
    }

    /**
     * Converti la position d'une case en coordonnées en pixels.
     * @param tile La case du plateau.
     * @return La position de case en coordonnées en pixels.
     */
    public Point tileToPixel(Coordinate tile){
        int n = tile.col();
        int m = tile.line();
        return new Point((int) Math.round(x0 + (distance * ((double) n - ((double) m / 2)))),
                (int) Math.round(y0 + ((m * Math.sqrt(3) * distance) / 2)));
    }

    /**
     * Converti une coordonnée en pixels en case du plateau.
     * @param pixels Les coordonnées en pixels.
     * @return La case du plateau correspondante.
     */
    public Coordinate pixelToTile(Point pixels){
        Set<Coordinate> tiles = getCoordinates(pixels);
        Coordinate closestTile = null;
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (Coordinate tile : tiles){
            double distance = MatchUtils.euclideanDistance(tileToPixel(tile), pixels);
            if (distance < shortestDistance){
                closestTile = tile;
                shortestDistance = distance;
            }
        }
        return closestTile;
    }

    /**
     * Retourne les coordonnées correspondantes au pixel.
     * @param pixels Le pixel qu'on cherche à traduire en Coordonnées.
     * @return Les coordonnées du pixel.
     */
    private Set<Coordinate> getCoordinates(Point pixels) {
        int x = (int) pixels.getX();
        int y = (int) pixels.getY();
        int c = (int) (Math.round(((double) (x - x0) / distance) + ((1 / Math.sqrt(3) * ((double) (y - y0) / distance)))));
        int l = (int) (Math.round((2 * (y - y0) / (distance * Math.sqrt(3)))));
        return new HashSet<>(Set.of(new Coordinate(c,l), new Coordinate(c-1, l), new Coordinate(c+1, l),
                new Coordinate(c, l-1), new Coordinate(c, l+1),
                new Coordinate(c-1, l-1), new Coordinate(c+1, l+1)));
    }

    public void animateScore(Set<Coordinate> groupCoords, int scoreGained, int player, float progress) {
        if(!match.isPlaying()) return;
        if(scoreAnimations.containsKey(groupCoords)){
            if(progress >= 1){
                 
                scoreAnimations.remove(groupCoords);
            }
            else
                scoreAnimations.get(groupCoords).updateProgress(progress);
        }
        else{
            scoreAnimations.put(groupCoords, new ScoreAnimation(scoreGained, player, progress));
        }
        repaint();
    }

    public void animateImpossibleMove(String id, int l, int c, float progress) {
        if(!match.isPlaying()) return;
        if(impossibleMoveAnimations.containsKey(id)){
            if(progress >= 1){
                Configuration.info("Removing Impossible move animation in GamePanel");
                impossibleMoveAnimations.remove(id);
            }
            else
                impossibleMoveAnimations.get(id).updateProgress(progress);
        }
        else{
            impossibleMoveAnimations.put(id, new ImpossibleMoveAnimation(progress, c, l));
        }
        repaint();
    }

    static abstract class ViewAnimation {
        float progress;

        public ViewAnimation(float progress) {
            this.progress = progress;
        }

        public void updateProgress(float newProgress){
            progress = newProgress;
        }
    }

    static class ScoreAnimation extends ViewAnimation {
        int scoreGained;
        int player;

        public ScoreAnimation(int scoreGained, int player, float progress){
            super(progress);
            this.scoreGained = scoreGained;
            this.player = player;
        }
    }

    static class ImpossibleMoveAnimation extends ViewAnimation{
        int l, c;

        public ImpossibleMoveAnimation(float progress, int c, int l) {
            super(progress);
            this.c = c;
            this.l = l;
        }
    }

    public int getNSelected() {
        return nSelected;
    }

    public int getMSelected() {
        return mSelected;
    }

    public boolean isMovePlayable(int m, int n){
        return match.isBoardTilePlayableForPlayer(match.getCurrentPlayerIndex(), m, n);
    }

    public boolean getShowHoverHighlight() {
        return showHoverHighlight;
    }

    public boolean getShowUnplayableMoves() {
        return showUnplayableMoves;
    }

    public boolean getShowBlockingCrittersHighlight() {
        return showBlockingCrittersHighlight;
    }

    public boolean getShowFeedforwardHighlight() {
        return showFeedforwardHighlight;
    }

    public boolean getShowEatenCrittersFeedback() {
        return showEatenCrittersFeedback;
    }

    public boolean getShowScoreAnimation() {
        return showScoreAnimation;
    }

    public void setShowUnplayableMoves(boolean showUnplayableMoves) {
        this.showUnplayableMoves = showUnplayableMoves;
    }

    public void setShowEatenCrittersFeedback(boolean showEatenCrittersFeedback) {
        this.showEatenCrittersFeedback = showEatenCrittersFeedback;
    }

    public void setShowHoverHighlight(boolean showHoverHighlight) {
        this.showHoverHighlight = showHoverHighlight;
    }

    public void setShowFeedforwardHighlight(boolean showFeedforwardHighlight) {
        this.showFeedforwardHighlight = showFeedforwardHighlight;
    }

    public void setShowBlockingCrittersHighlight(boolean showBlockingCrittersHighlight) {
        this.showBlockingCrittersHighlight = showBlockingCrittersHighlight;
    }

    public void setShowScoreAnimation(boolean showScoreAnimation) {
        this.showScoreAnimation = showScoreAnimation;
    }
}
