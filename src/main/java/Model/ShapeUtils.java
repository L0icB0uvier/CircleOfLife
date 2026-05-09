package Model;

import java.util.*;

public class ShapeUtils {
    private static final Map<Set<Coordinate>, Integer> shapes;
    private static final Map<Set<Coordinate>, ArrayList<Boolean>> borders;

    static {
        Map<Set<Coordinate>, Integer> tempShapes = new HashMap<>();
        Map<Set<Coordinate>, ArrayList<Boolean>> shapesBorders = new HashMap<>();

        // ID 0
        tempShapes.put(Set.of(new Coordinate(0, 0)), 0);
        shapesBorders.put(Set.of(new Coordinate(0, 0)), new ArrayList<>(Arrays.asList(true, true, true, true, true)));

        // ID 1
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3)), 1); // 1
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3)),
                new ArrayList<>(Arrays.asList(true, true, true, false, true, false, true, false, true, true, true, true, false, true, false, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(3, 0)), 1); // 2
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(2, 0), new Coordinate(3, 0)),
                new ArrayList<>(Arrays.asList(true, false, true, false, true, false, true, true, true, true, false, true, false, true, false, true, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3)), 1); // 3
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3)),
                new ArrayList<>(Arrays.asList(true, true, false, true, false, true, false, true, true, true, true, false, true, false, true, false, true)));
        // ID 2
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(1, 2)), 2); // 1
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(1, 2)),
                new ArrayList<>(Arrays.asList(true, true, true, false, false, true, true, true, true, false, false, true, true, true, true, false, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(1, 2)), 2); // 2
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(1, 2)),
                new ArrayList<>(Arrays.asList(true, true, false, false, true, true, true, true, false, false, true, true, true, true, false, false, true)));

        // ID 3
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(1, 1)), 3); // 2
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(1, 1)),
                new ArrayList<>(Arrays.asList(true, true, false, true, true, false, true, true, true, false, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(1, 2)), 3); // 1
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(1, 2)),
                new ArrayList<>(Arrays.asList(true, false, true, true, true, false, true, true, false, true, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 1)), 3); // 3
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 1)),
                new ArrayList<>(Arrays.asList(true, false, true, true, false, true, true, false, true, true, true, false, true)));

        // ID 4
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1), new Coordinate(3, 1)), 4); // 1
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1), new Coordinate(3, 1)),
                new ArrayList<>(Arrays.asList(true, false, true, true, false, false, true, true, true, true, false, true, true, false, false, true, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(-1, 1)), 4); // 2
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(-1, 1)),
                new ArrayList<>(Arrays.asList(true, false, true, true, true, true, false, false, true, true, false, true, true, true, true, false, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(2, 3)), 4); // 3
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(2, 3)),
                new ArrayList<>(Arrays.asList(true, true, false, true, true, false, false, true, true, true, true, false, true, true, false, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(1, 3)), 4); // 4
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(1, 3)),
                new ArrayList<>(Arrays.asList(true, true, true, false, false, true, true, false, true, true, true, true, false, false, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(-1, 2)), 4); // 5
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(-1, 2)),
                new ArrayList<>(Arrays.asList(true, true, true, false, true, true, false, false, true, true, true, true, false, true, true, false, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(3, 2)), 4); // 6
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(3, 2)),
                new ArrayList<>(Arrays.asList(true, true, false, false, true, true, false, true, true, true, true, false, false, true, true, false, true)));

        // ID 5
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(2, 0)), 5); // 1
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(2, 0)),
                new ArrayList<>(Arrays.asList(true, true, false, false, false, true, true, true, true, false, true, true, false, true, true, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(2, 1)), 5); // 2
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(2, 1)),
                new ArrayList<>(Arrays.asList(true, false, true, true, false, true, true, true, true, false, false, false, true, true, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(2, 2)), 5); // 3
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(2, 2)),
                new ArrayList<>(Arrays.asList(true, true, true, false, false, false, true, true, true, true, false, true, true, false, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(0, 2)), 5); // 4
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(0, 2)),
                new ArrayList<>(Arrays.asList(true, true, false, true, true, false, true, true, false, true, true, true, true, false, false, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(1, 2)), 5); // 5
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(2, 2)),
                new ArrayList<>(Arrays.asList(true, true, true, false, false, false, true, true, true, true, false, true, true, false, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1), new Coordinate(2, 2)), 5); // 6
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(2, 2)),
                new ArrayList<>(Arrays.asList(true, false, true, true, false, true, true, false, true, true, true, true, false, false, false, true, true)));

        // ID 6
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(0, 1)), 6); // 1
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(0, 1)),
                new ArrayList<>(Arrays.asList(true, false, true, false, true, true, true, true, false, true, false, false, true, true, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(-2, 1)), 6); // 2
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(-2, 1)),
                new ArrayList<>(Arrays.asList(true, true, true, false, true, true, false, true, false, true, true, true, true, false, true, false, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(3, 1)), 6); // 3
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(3, 1)),
                new ArrayList<>(Arrays.asList(true, false, true, false, true, true, false, true, true, true, true, false, false, true, false, true, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(3, 1)), 6); // 4
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(3, 1)),
                new ArrayList<>(Arrays.asList(true, true, false, false, true, false, true, true, true, true, false, true, false, true, true, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1), new Coordinate(2, 2)), 6); // 5
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1), new Coordinate(2, 2)),
                new ArrayList<>(Arrays.asList(true, false, true, true, false, true, false, true, true, true, true, false, true, false, false, true, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 2)), 6); // 6
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 2)),
                new ArrayList<>(Arrays.asList(true, true, false, true, false, false, true, true, true, true, false, true, true, false, true, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(2, 3)), 6); // 7
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(2, 3)),
                new ArrayList<>(Arrays.asList(true, true, true, false, false, true, false, true, true, true, true, false, true, false, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(2, 3)), 6); // 8
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(2, 3)),
                new ArrayList<>(Arrays.asList(true, true, false, true, false, true, true, false, true, true, true, true, false, false, true, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(1, 3)), 6); // 9
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(1, 3)),
                new ArrayList<>(Arrays.asList(true, true, true, false, true, false, false, true, true, true, true, false, true, true, false, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(-1, 2)), 6); // 10
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(-1, 2)),
                new ArrayList<>(Arrays.asList(true, true, true, false, true, false, true, true, false, true, true, true, true, false, false, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(0, 2)), 6); // 11
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(0, 2)),
                new ArrayList<>(Arrays.asList(true, false, true, true, true, true, false, false, true, false, true, true, true, true, false, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(1, 3)), 6); // 12
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(1, 3)),
                new ArrayList<>(Arrays.asList(true, true, false, true, true, false, true, false, true, true, true, true, false, true, false, false, true)));

        // ID 7
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(2, 1)), 7); // 1
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(1, 3)),
                new ArrayList<>(Arrays.asList(true, true, false, false, true, true, true, true, false, true, false, true, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(1, 1)), 7); // 2
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(1, 1)),
                new ArrayList<>(Arrays.asList(true, false, true, false, true, true, true, true, false, false, true, true, true, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(1, 1)), 7); // 3
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(1, 1)),
                new ArrayList<>(Arrays.asList(true, true, false, true, true, true, false, true, false, true, true, true, true, false, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(2, 1)), 7); // 4
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(2, 1)),
                new ArrayList<>(Arrays.asList(true, false, true, false, true, true, true, false, true, true, true, true, false, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 2)), 7); // 5
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 2)),
                new ArrayList<>(Arrays.asList(true, false, true, true, true, false, false, true, true, true, true, false, true, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(2, 2)), 7); // 6
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(2, 2)),
                new ArrayList<>(Arrays.asList(true, true, false, true, false, true, true, true, true, false, false, true, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(2, 2)), 7); // 7
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(2, 2)),
                new ArrayList<>(Arrays.asList(true, true, false, false, true, true, true, false, true, true, true, false, true, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(2, 2)), 7); // 8
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(2, 2)),
                new ArrayList<>(Arrays.asList(true, true, false, true, false, true, true, true, false, true, true, true, false, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(1, 2)), 7); // 9
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(1, 2)),
                new ArrayList<>(Arrays.asList(true, false, true, true, true, false, true, false, true, true, true, true, false, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(0, 2)), 7); // 10
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(0, 2)),
                new ArrayList<>(Arrays.asList(true, true, false, true, true, true, false, false, true, true, true, true, false, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 2)), 7); // 11
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 2)),
                new ArrayList<>(Arrays.asList(true, true, true, false, true, false, true, true, true, false, true, true, true, false, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(1, 2)), 7); // 12
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(1, 2)),
                new ArrayList<>(Arrays.asList(true, true, true, false, false, true, true, true, false, true, true, true, false, true, false)));

        // ID 8
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1)), 8); // 1
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1)),
                new ArrayList<>(Arrays.asList(true, true, false, true, true, true, false, true, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1)), 8); // 1
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1)),
                new ArrayList<>(Arrays.asList(true, false, true, true, true, false, true, true, true, false, true)));

        // ID 9
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2)), 9); // 1
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2)),
                new ArrayList<>(Arrays.asList(true, true, false, true, false, true, true, true, true, false, true, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0)), 9); // 2
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0)),
                new ArrayList<>(Arrays.asList(true, false, true, false, true, true, true, true, false, true, false, true, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2)), 9); // 3
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2)),
                new ArrayList<>(Arrays.asList(true, true, true, false, true, false, true, true, true, true, false, true, false)));

        // ID 10
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1)), 10); // 1
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1)),
                new ArrayList<>(Arrays.asList(true, false, true, true, false, true, true, true, true, false, false, true, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1)), 10); // 2
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1)),
                new ArrayList<>(Arrays.asList(true, true, true, false, true, true, false, true, true, true, true, false, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1)), 10); // 3
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1)),
                new ArrayList<>(Arrays.asList(true, true, false, false, true, true, true, true, false, true, true, false, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1)), 10); // 4
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1)),
                new ArrayList<>(Arrays.asList(true, false, true, true, true, true, false, false, true, true, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2)), 10); // 5
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2)),
                new ArrayList<>(Arrays.asList(true, true, true, false, false, true, true, true, true, false, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2)), 10); // 6
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2)),
                new ArrayList<>(Arrays.asList(true, true, false, true, true, false, true, true, true, true, false, false, true)));

        // ID 11
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0)), 11); // 1
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 0)),
                new ArrayList<>(Arrays.asList(true, false, true, true, true, true, false, true, true)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1)), 11); // 2
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(0, 1)),
                new ArrayList<>(Arrays.asList(true, true, true, false, true, true, true, true, false)));
        tempShapes.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1)), 11); // 3
        shapesBorders.put(Set.of(new Coordinate(0, 0), new Coordinate(1, 1)),
                new ArrayList<>(Arrays.asList(true, true, false, true, true, true, true, false, true)));

        shapes = Collections.unmodifiableMap(tempShapes);
        borders= Collections.unmodifiableMap(shapesBorders);
    }

    /**
     * Récupère le type de forme.
     * @param boardCoordinates Un Set de coordonnées sur le plateau décrivant la forme.
     * @return Le type de forme.
     */
    public static Integer getShapeId(Set<Coordinate> boardCoordinates) {
        var normalizedCoord = normalizeCoordinate(boardCoordinates);
        return shapes.getOrDefault(normalizedCoord, -1);
    }

    /**
     * Récupère le type de forme.
     * @param boardCoordinates Un Set de coordonnées sur le plateau décrivant la forme.
     * @return Le type de forme.
     */
    public static ArrayList<Boolean> getShapeBorders(Set<Coordinate> boardCoordinates) {
        var normalizedCoord = normalizeCoordinate(boardCoordinates);
        return borders.getOrDefault(normalizedCoord, new ArrayList<>());
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
            if (coord.col() <= minCol && coord.line() <= minLine) {
                minLine = coord.line();
                minCol = coord.col();
            }
        }

        Set<Coordinate> normalized = new HashSet<>();
        for(Coordinate coord : boardCoordinate){
            int normalizedLine = coord.line() - minLine;
            int normalizedCol = coord.col() - minCol;
            normalized.add(new Coordinate(normalizedCol, normalizedLine));
        }

        return normalized;
    }
}