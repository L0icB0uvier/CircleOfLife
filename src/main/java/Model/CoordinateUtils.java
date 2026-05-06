package Model;

public class CoordinateUtils {
    /**
     * Vérifie si 2 coordonnées sont voisines.
     * @param first La première coordonnée.
     * @param second La deuxième coordonnée.
     * @return true si les deux coordonnées sont voisines, false sinon.
     */
    public static boolean isNeighbor(Coordinate first, Coordinate second){
        int deltaX = first.line() - second.line();
        int deltaY = first.col() - second.col();
        return Math.abs(deltaX) <= 1 && Math.abs(deltaY) <= 1 && Math.abs(deltaX - deltaY) <= 1;
    }


}
