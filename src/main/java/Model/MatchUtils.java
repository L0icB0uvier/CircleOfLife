package Model;

import Global.Configuration;

import java.awt.*;
import java.util.Set;

public class MatchUtils {
    /**
     * Vérifie si deux coordonnées sont voisines.
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

    /**
     * Crée une copie profonde des PlayerData.
     * @param players Les PlayerData à copier.
     * @return Une copie profonde des PlayerData.
     */
    public static PlayerData[] copyPlayerData(PlayerData[] players)  {
        PlayerData[] temp = new PlayerData[players.length];
        for (int i = 0; i < players.length; i++) {
            try {
                temp[i] = players[i].clone();
            } catch (CloneNotSupportedException e) {
                Configuration.error("Erreur lors du clonage des PlayerData");
            }
        }
        return temp;
    }

    /**
     * Calcule la distance Euclidienne entre deux points de coordonnées données.
     * @param pointA Le premier point.
     * @param pointB Le deuxième point.
     * @return La distance Euclidienne entre pointA et pointB.
     */
    public static double euclideanDistance(Point pointA, Point pointB){
        return Math.sqrt(Math.pow(pointA.getX()- pointB.getX(), 2) + Math.pow(pointA.getY()- pointB.getY(), 2));
    }


    /**
     * Réalise une copie profonde d'un match.
     * @param match Le match à copier.
     * @return Un match ayant un état, des joueurs, des critters et un joueur actif égaux à ceux de celui donné en argument
     */
    public static Match copy(Match match){
        return match.clone();
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

    /**
     * Compte le nombre de critters d'une taille appartenant à un joueur dans un match.
     * @param match Le match dont la configuration actuelle sera mesurée.
     * @param playerID L'index du joueur dont on doit compter les critters.
     * @param size La taille de critter à compter.
     * @return Le nombre des critters mesuré.
     */
    public static int countPlayerCritterSize(Match match, int playerID, int size){
        int result = 0;
        for (Critter critter : match.critters){
            if (critter.stonesCoordinates().size() == size && critter.player() == playerID){
                result++;
            }
        }
        return result;
    }

    /**
     * Détermine si l'adversaire du joueur actif peut jouer dans une case donnée.
     * @param match Le match sur lequel la mesure est faite.
     * @param coordinate La position de la case.
     * @return true si l'adversaire ne peut PAS jouer sur cette case, false sinon.
     */
    public static boolean isOpponentGreyTile(Match match, Coordinate coordinate) {
        return (match.boardState[coordinate.line()][coordinate.col()] == -match.getOpponentPlayerIndex());
    }

    /**
     * Détermine si jouer dans une case donnée case mènera à une évolution pour le joueur actif.
     * @param match Le match sur lequel la mesure est faite.
     * @param coordinate La position de la case.
     * @return true si jouer sur cette case mène à une évolution, false sinon.
     */
    public static boolean isEvolutionTile(Match match, Coordinate coordinate) {
        Set<Critter> neighbors = match.getPlayerNeighborsCritters(match.getCurrentPlayerIndex(), coordinate);
        for (Critter critter : neighbors){
            if (critter.stonesCoordinates().size() < 4){
                return true;
            }
        }
        return false;
    }
    /**
     * Retourne l'index de l'autre joueur.
     * @param playerIndex l'index du joueur pour lequel on veut l'adversaire.
     * @return L'index du joueur adverse.
     */
    public static int getOtherPlayerIndex(int playerIndex){
        return (playerIndex + 1) % 2;
    }

}
