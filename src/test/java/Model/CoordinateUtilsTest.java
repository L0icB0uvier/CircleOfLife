package Model;

import org.junit.jupiter.api.Test;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoordinateUtilsTest {

    @Test
    void checkFirstNeighbors(){
        int col = 5;
        int line = 3;
        Coordinate origin = new Coordinate(col,line);
        Set<Coordinate> computedNeighbors = new HashSet<>();
        for (int i = col-5; i<col+5; i++){
            for (int j = line-5; j<line+5; j++){
                Coordinate point = new Coordinate(i, j);

                if (CoordinateUtils.hexagonalManhattanDistance(origin, point) == 1){
                    computedNeighbors.add(point);
                }
            }
        }
        Set<Coordinate> expectedNeighbors = new HashSet<>(Set.of(new Coordinate(col, line + 1),
                new Coordinate(col, line - 1), new Coordinate(col + 1, line),
                new Coordinate(col - 1, line), new Coordinate(col - 1, line - 1),
                new Coordinate(col + 1, line + 1)));

        assertEquals(computedNeighbors, expectedNeighbors);

    }
}
