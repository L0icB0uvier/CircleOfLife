package Model;

public class CoordinateUtils {
    /**
     * Vérifie si 2 coordonnées sont voisines.
     * @param first La première coordonnée.
     * @param second La deuxième coordonnée.
     * @return true si les deux coordonnées sont voisines, false sinon.
     */
    public static boolean isNeighbor(Coordinate first, Coordinate second){
        int deltaX = Math.abs(first.line() - second.line());
        int deltaY = Math.abs(first.col() - second.col());
        return deltaX <= 1 && deltaY <= 1;
    }
}
