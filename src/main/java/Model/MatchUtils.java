package Model;

public class MatchUtils {
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
        return MatchUtils.hexagonalManhattanDistance(coord, new Coordinate(4, 4)) <= 4;
    }

    /**
     * Calcule la distance Manhattan hexagonale entre deux Cases.
     * @param first La coordonnée de la première case.
     * @param second La coordonnée de la deuxième case.
     * @return la distance de Manhattan hexagonale entre les deux coordonnées.
     */
    public static int hexagonalManhattanDistance(Coordinate first, Coordinate second){
        int deltaX = second.col() - first.col();
        int deltaY =  second.line() - first.line();
        int deltaZ = deltaX - deltaY;
        return Math.max(Math.max(Math.abs(deltaX), Math.abs(deltaY)), Math.abs(deltaZ));
    }

    public static byte[][] copyBoard(byte[][] board){
        if (board == null) return null;

        // Création du tableau de premier niveau
        byte[][] copy = new byte[board.length][];

        for (int i = 0; i < board.length; i++) {
            // .clone() sur un tableau de primitives (byte) effectue une copie profonde de la ligne
            copy[i] = board[i].clone();
        }

        return copy;
    }

    public static PlayerData[] copyPlayerData(PlayerData[] players)  {
        PlayerData[] temp = new PlayerData[players.length];
        for (int i = 0; i < players.length; i++) {
            temp[i] = players[i].clone();
        }
        return temp;
    }
}
