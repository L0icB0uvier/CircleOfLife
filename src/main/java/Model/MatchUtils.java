package Model;

import java.util.Set;

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

    /**
     * Vérifie que la case de coordonnées données est bien dans le plateau
     * @param coord Les coordonnées de la case à tester.
     * @return true si la case est dans le plateau, false sinon
     */
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

    /**
     * Calcule la distance Euclidienne entre deux points de coordonnées données.
     * @param pointA Le premier point.
     * @param pointB Le deuxième point.
     * @return La distance Euclidienne entre pointA et pointB.
     */
    public static double euclidianDistance(Coordinate pointA, Coordinate pointB){
        return Math.sqrt(Math.pow(pointA.col()- pointB.col(), 2) + Math.pow(pointA.line()- pointB.line(), 2));
    }


    /**
     * Réalise une copie profonde d'un match.
     * @param match Le match à copier.
     * @return Un match ayant un état, des joueurs, des critters et un joueur actif égaux à celui donné en argument
     */
    public static Match copy(Match match){
        Match newMatch = new Match();
        newMatch.boardState = match.getBoardState();
        newMatch.players = match.getPlayerData();
        for (Critter critter : match.critters){
            newMatch.critters.add(new Critter(critter));
        }
        newMatch.currentPlayerIndex = match.currentPlayerIndex;
        for (Critter critter : match.previouslyEatenCritters){
            newMatch.previouslyEatenCritters.add(new Critter(critter));
        }
        return newMatch;
    }

    /**
     * Calcule le nombre de points gagné en mangeant des critters.
     * @param eatenCritters La liste des critters mangés.
     * @return Le nombre de points gagnés.
     */
    public static int calculatePointEarned(Set<Critter> eatenCritters){
        if(eatenCritters == null || eatenCritters.isEmpty()) return 0;

        int score = 0;
        for (Critter critter : eatenCritters){
            if(critter == null)
                continue;
            score += critter.stonesCoordinates().size();
        }

        return score;
    }
}
