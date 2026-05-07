package Model;

public class CoordinateUtils {
    /**
     * Vérifie si 2 coordonnées sont voisines.
     * @param first La première coordonnée.
     * @param second La deuxième coordonnée.
     * @return true si les deux coordonnées sont voisines, false sinon.
     */
    public static boolean isNeighbor(Coordinate first, Coordinate second){
        return hexagonalManhattanDistance(first, second) == 1;
    }

    public static boolean isInsideBoard(Coordinate coord) {
        return CoordinateUtils.hexagonalManhattanDistance(coord, new Coordinate(4, 4)) <= 4;
    }

    /**
     * Calcule la distance Manhattan hexagonale entre deux Cases.
     * @param first La coordonnée de la première case.
     * @param second La coordonnée de la deuxième case.
     * @return la distance de Manhattan hexagonale entre les deux coordonnées.
     */
    public static int hexagonalManhattanDistance(Coordinate first, Coordinate second){
        int deltaX = second.line() - first.line();
        int deltaY =  second.col() - first.col();
        int deltaZ = deltaX - deltaY;
        return Math.max(Math.max(Math.abs(deltaX), Math.abs(deltaY)), Math.abs(deltaZ));
    }
}
