package Model;

import java.util.*;

public class CritterUtils {
    private static final Map<Set<Coordinate>, Integer> critters;

    /**
     * Représente la distance entre les 2 pierres les plus éloignées d'un critter.
     */
    public static final Map<Integer, Integer> critterEvolutionMaxDistance = Map.ofEntries(
            Map.entry(0, 0),
            Map.entry(1, 3),
            Map.entry(2, 2),
            Map.entry(3, 2),
            Map.entry(4, 3),
            Map.entry(5, 3),
            Map.entry(6, 3),
            Map.entry(7, 2),
            Map.entry(8, 1),
            Map.entry(9, 2),
            Map.entry(10, 2),
            Map.entry(11, 1)
    );

    static {
        Map<Set<Coordinate>, Integer> tempShapes = new LinkedHashMap<>();

        // ID 0
        tempShapes.put(Set.of(new Coordinate(0, 0)), 0);

        // ID 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3)), 1); // 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(3, 0)), 1); // 2
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3)), 1); // 3

        // ID 2
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(1, 2)), 2); // 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(1, 2)), 2); // 2

        // ID 3
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(1, 2)), 3); // 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(1, 1)), 3); // 2
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 1)), 3); // 3

        // ID 4
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1), new Coordinate(3, 1)), 4); // 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(-1, 1)), 4); // 2
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(2, 3)), 4); // 3
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(1, 3)), 4); // 4
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(-1, 2)), 4); // 5
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(3, 2)), 4); // 6

        // ID 5
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(2, 0)), 5); // 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(2, 1)), 5); // 2
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(2, 2)), 5); // 3
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(0, 2)), 5); // 4
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(1, 2)), 5); // 5
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1), new Coordinate(2, 2)), 5); // 6

        // ID 6
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(0, 1)), 6); // 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(-2, 1)), 6); // 2
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(3, 1)), 6); // 3
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(3, 1)), 6); // 4
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1), new Coordinate(3, 2)), 6); // 5
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 2)), 6); // 6
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(2, 3)), 6); // 7
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(2, 3)), 6); // 8
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(1, 3)), 6); // 9
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(-1, 2)), 6); // 10
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(0, 2)), 6); // 11
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(1, 3)), 6); // 12

        // ID 7
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(2, 1)), 7); // 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(1, 1)), 7); // 2
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(1, 1)), 7); // 3
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(2, 1)), 7); // 4
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 2)), 7); // 5
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(2, 2)), 7); // 6
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(2, 2)), 7); // 7
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(2, 2)), 7); // 8
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(1, 2)), 7); // 9
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(0, 2)), 7); // 10
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 2)), 7); // 11
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(1, 2)), 7); // 12

        // ID 8
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1)), 8); // 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1)), 8); // 1

        // ID 9
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2)), 9); // 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0)), 9); // 2
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2)), 9); // 3

        // ID 10
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1)), 10); // 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1)), 10); // 2
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1)), 10); // 3
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1)), 10); // 4
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2)), 10); // 5
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2)), 10); // 6

        // ID 11
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0)), 11); // 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1)), 11); // 2
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1)), 11); // 3

        critters = Collections.unmodifiableMap(tempShapes);
    }

    /**
     * Récupère le type de forme.
     * @param boardCoordinates Un Set de coordonnées sur le plateau décrivant la forme.
     * @return Le type de forme.
     */
    public static Integer getCritterId(Set<Coordinate> boardCoordinates) {
        var normalizedCoord = normalizeCoordinate(boardCoordinates);
        return critters.getOrDefault(normalizedCoord, -1);
    }

    /**
     * Normalize un Set de coordonnées.
     * @param boardCoordinate Un Set de coordonnées relative à leur position sur le plateau.
     * @return Une normalization du Set d'entré.
     */
    public static Set<Coordinate> normalizeCoordinate(Set<Coordinate> boardCoordinate) {
        if(boardCoordinate == null || boardCoordinate.isEmpty()) return null;

        Coordinate topLeft = getTopLeftCoordinate(boardCoordinate);

        Set<Coordinate> normalized = new HashSet<>();
        for(Coordinate coord : boardCoordinate){
            int normalizedLine = coord.line() - topLeft.line();
            int normalizedCol = coord.col() - topLeft.col();
            normalized.add(new Coordinate(normalizedCol, normalizedLine));
        }

        return normalized;
    }

    /**
     * Récupère la coordonnées la plus en haut à gauche du critter.
     * @param boardCoordinate La liste de coordonnées dans laquel trouver la coordonnées la plus haut/gauche.
     * @return La coordonnées la plus haut/gauche.
     */
    public static Coordinate getTopLeftCoordinate(Set<Coordinate> boardCoordinate){
        int minLine = Integer.MAX_VALUE;
        int minCol = Integer.MAX_VALUE;

        for (Coordinate coord : boardCoordinate) {
            if (coord.line() < minLine) {
                minLine = coord.line();
                minCol = coord.col();
            }
            else if(coord.line() == minLine && coord.col() < minCol)
                minCol = coord.col();
        }

        return new Coordinate(minCol, minLine);
    }

    /**
     * Retourne les un critter du type et de l'id donnée.
     * @param type Le type du critter.
     * @param id L'id de la variation du critter.
     * @param player Le type de joueur à assigner au critter.
     * @return Le critter correspondant.
     */
    public static Critter critterFromId(int type, int id, int player){
        List<Set<Coordinate>> keys = critters.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), type))
                .map(Map.Entry::getKey)
                .toList();

        if(id >= keys.size()) return null;

        return new Critter(keys.get(id), player);
    }

    /**
     * Retourne les coordonnées d'un critter du type et de l'id donné.
     * @param type Le type du critter.
     * @param id L'id de la variation du critter.
     * @return Les coordonnées correspondantes.
     */
    public static Set<Coordinate> getCritterTypeCoordinates(int type, int id){
        List<Set<Coordinate>> keys = critters.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), type))
                .map(Map.Entry::getKey)
                .toList();

        if(id >= keys.size())
            return Collections.emptySet();

        return keys.get(id);
    }

    /**
     * Calcule la coordonnée moyenne d'un ensemble de coordonnées.
     * @param coordinates Le set de coordonnées à analyser.
     * @return La coordonnée moyenne, ou null si l'ensemble est vide ou null.
     */
    public static Coordinate getAverageCoordinate(Set<Coordinate> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            return null;
        }

        double sumLine = 0;
        double sumCol = 0;

        for (Coordinate coord : coordinates) {
            sumLine += coord.line();
            sumCol += coord.col();
        }

        // On arrondit au plus proche pour obtenir des coordonnées entières
        int avgLine = (int) Math.round(sumLine / coordinates.size());
        int avgCol = (int) Math.round(sumCol / coordinates.size());

        return new Coordinate(avgCol, avgLine); // Attention à l'ordre (col, line) utilisé dans votre méthode normalizeCoordinate
    }
}