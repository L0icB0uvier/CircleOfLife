package Model;

/**
 * Représente les coordonnées d'une tile d'un critter.
 * @param col La colonne de la coordonnée.
 * @param line La ligne de la coordonnée.
 */
public record Coordinate(int col, int line) {
    @Override
    public String toString() {
//        return "(" + col + ", " + line + ")";
        char col = (char) (65 + col());
        return "(" + (line + 1) + ", " + col + ")";
    }
}
