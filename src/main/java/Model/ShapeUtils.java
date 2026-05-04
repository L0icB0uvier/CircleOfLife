package Model;

import java.util.*;

public class ShapeUtils {
    private static final Map<Set<Coordinate>, Integer> shapes;

    static {
        Map<Set<Coordinate>, Integer> tempShapes = new HashMap<>();

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
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1), new Coordinate(2, 2)), 6); // 5
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

        shapes = Collections.unmodifiableMap(tempShapes);
    }

    /**
     * Récupère le type de forme.
     * @param normalizedGroup Un Set de coordonnées normalisées décrivant la forme.
     * @return Le type de forme.
     */
    public static Integer getShapeId(Set<Coordinate> normalizedGroup) {
        return shapes.getOrDefault(normalizedGroup, -1);
    }

    /**
     * Normalize un Set de coordonnées.
     * @param boardCoordinate Un Set de coordonnées relative à leur position sur le plateau.
     * @return Une normalization du Set d'entré.
     */
    public static Set<Coordinate> normalizeCoordinate(Set<Coordinate> boardCoordinate) {
        if(boardCoordinate == null || boardCoordinate.isEmpty()) return null;

        int minLine = Integer.MAX_VALUE;
        int minCol = Integer.MAX_VALUE;

        for (Coordinate coord : boardCoordinate) {
            if (coord.col() < minCol && coord.line() < minLine) {
                minLine = coord.line();
                minCol = coord.col();
            }
        }

        Set<Coordinate> normalized = new HashSet<>();
        for(Coordinate coord : boardCoordinate){
            int normalizedLine = coord.line() - minLine;
            int normalizedCol = coord.col() - minCol;
            normalized.add(new Coordinate(normalizedLine, normalizedCol));
        }

        return normalized;
    }
}