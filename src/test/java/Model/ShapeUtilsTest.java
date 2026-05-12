package Model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShapeUtilsTest {
    @Test
    void NormalizeCoordinateCenter(){
        Set<Coordinate> coord = new HashSet<>(
                Set.of(new Coordinate(4, 5), new Coordinate(5, 6), new Coordinate(6, 6))
        );

        var normalizedCoords = ShapeUtils.normalizeCoordinate(coord);
        Set<Coordinate> expected = new HashSet<>(
                Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1))
        );
        assertEquals(normalizedCoords, expected);
    }

    @Test
    void AlreadyNormalized(){
        Set<Coordinate> coord = new HashSet<>(
                Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1))
        );

        var normalizedCoords = ShapeUtils.normalizeCoordinate(coord);
        Set<Coordinate> expected = new HashSet<>(
                Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1))
        );
        assertEquals(normalizedCoords, expected);
    }

    @Test
    void validateShape0(){
        // Valid set
        Set<Coordinate> correctCoords = new HashSet<>(Set.of(new Coordinate(0, 0)));
        assertEquals(0, ShapeUtils.getShapeId(correctCoords));

        // Valid set with normalization
        Set<Coordinate> correctCoordsUnormalized = new HashSet<>(Set.of(new Coordinate(5, 7)));
        assertEquals(0, ShapeUtils.getShapeId(ShapeUtils.normalizeCoordinate(correctCoordsUnormalized)));

        // Invalid set
        Set<Coordinate> incorrectCoords = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1)));
        assertNotEquals(0, ShapeUtils.getShapeId(incorrectCoords));
    }


    @Test
    void validateShape1(){
        // Valid set 1
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3)));
        assertEquals(1, ShapeUtils.getShapeId(validSet1));

        // Valid set 2
        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(3, 0)));
        assertEquals(1, ShapeUtils.getShapeId(validSet2));

        // Valid set 3
        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3)));
        assertEquals(1, ShapeUtils.getShapeId(validSet3));

        // Valid set with normalization
        Set<Coordinate> unormalizedSet = new HashSet<>(Set.of(new Coordinate(4, 3), new Coordinate(5, 4), new Coordinate(6, 5), new Coordinate(7, 6)));
        assertEquals(1, ShapeUtils.getShapeId(ShapeUtils.normalizeCoordinate(unormalizedSet)));

        // Invalid Set 1
        Set<Coordinate> invalidSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(2, 3)));
        assertNotEquals(1, ShapeUtils.getShapeId(invalidSet1));

        // Invalid Set 1
        Set<Coordinate> invalidSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2)));
        assertNotEquals(1, ShapeUtils.getShapeId(invalidSet2));
    }

    @Test
    void validateShape2(){
        // Valid set 1
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(1, 2)));
        assertEquals(2, ShapeUtils.getShapeId(validSet1));

        // Valid set 2
        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(1, 2)));
        assertEquals(2, ShapeUtils.getShapeId(validSet2));

        // Invalid Set 1
        Set<Coordinate> invalidSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1)));
        assertNotEquals(2, ShapeUtils.getShapeId(invalidSet1));

        // Invalid Set 1
        Set<Coordinate> invalidSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(1, 3)));
        assertNotEquals(2, ShapeUtils.getShapeId(invalidSet2));
    }

    @Test
    void validateShape3(){
        // Valid set 1
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(1, 1)));
        assertEquals(3, ShapeUtils.getShapeId(validSet1));

        // Valid set 2
        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(1, 2)));
        assertEquals(3, ShapeUtils.getShapeId(validSet2));

        // Valid set 3
        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 1)));
        assertEquals(3, ShapeUtils.getShapeId(validSet3));

        // Invalid Set 1
        Set<Coordinate> invalidSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(2, 3)));
        assertNotEquals(3, ShapeUtils.getShapeId(invalidSet1));

        // Invalid Set 1
        Set<Coordinate> invalidSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1)));
        assertNotEquals(3, ShapeUtils.getShapeId(invalidSet2));
    }

    @Test
    void validateShape4(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(5, 4), new Coordinate(6, 5), new Coordinate(7, 5))); // 1
        assertEquals(4, ShapeUtils.getShapeId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(5, 4), new Coordinate(4, 5), new Coordinate(3, 5))); // 2
        assertEquals(4, ShapeUtils.getShapeId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(5, 5), new Coordinate(5, 6), new Coordinate(6, 7))); // 3
        assertEquals(4, ShapeUtils.getShapeId(validSet3));

        Set<Coordinate> validSet4 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(4, 5), new Coordinate(5, 6), new Coordinate(5, 7))); // 4
        assertEquals(4, ShapeUtils.getShapeId(validSet4));

        Set<Coordinate> validSet5 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(4, 5), new Coordinate(3, 5), new Coordinate(3, 6))); // 5
        assertEquals(4, ShapeUtils.getShapeId(validSet5));

        Set<Coordinate> validSet6 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(5, 5), new Coordinate(6, 5), new Coordinate(7, 6))); // 6
        assertEquals(4, ShapeUtils.getShapeId(validSet6));

        Set<Coordinate> invalidSet1 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(5, 5), new Coordinate(3, 4), new Coordinate(7, 6))); // 6
        assertNotEquals(4, ShapeUtils.getShapeId(invalidSet1));

    }

    @Test
    void validateShape5(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 4), new Coordinate(5, 4), new Coordinate(5, 3)));
        assertEquals(5, ShapeUtils.getShapeId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 3), new Coordinate(3, 4), new Coordinate(5, 4)));
        assertEquals(5, ShapeUtils.getShapeId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(3, 4), new Coordinate(4, 5), new Coordinate(5, 5)));
        assertEquals(5, ShapeUtils.getShapeId(validSet3));

        Set<Coordinate> validSet4 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 4), new Coordinate(4, 5), new Coordinate(3, 5)));
        assertEquals(5, ShapeUtils.getShapeId(validSet4));

        Set<Coordinate> validSet5 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 3), new Coordinate(3, 4), new Coordinate(4, 5)));
        assertEquals(5, ShapeUtils.getShapeId(validSet5));

        Set<Coordinate> validSet6 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 3), new Coordinate(5, 4), new Coordinate(5, 5)));
        assertEquals(5, ShapeUtils.getShapeId(validSet6));

    }

    @Test
    void validateShape6(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(0, 1)));
        assertEquals(6, ShapeUtils.getShapeId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(-2, 1)));
        assertEquals(6, ShapeUtils.getShapeId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(3, 1)));
        assertEquals(6, ShapeUtils.getShapeId(validSet3));

        Set<Coordinate> validSet4 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(3, 1)));
        assertEquals(6, ShapeUtils.getShapeId(validSet4));

        Set<Coordinate> validSet5 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1), new Coordinate(3, 2)));
        assertEquals(6, ShapeUtils.getShapeId(validSet5));

        Set<Coordinate> validSet6 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 2)));
        assertEquals(6, ShapeUtils.getShapeId(validSet6));

        Set<Coordinate> validSet7 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(2, 3)));
        assertEquals(6, ShapeUtils.getShapeId(validSet7));

        Set<Coordinate> validSet8 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(2, 3)));
        assertEquals(6, ShapeUtils.getShapeId(validSet8));

        Set<Coordinate> validSet9 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(1, 3)));
        assertEquals(6, ShapeUtils.getShapeId(validSet9));

        Set<Coordinate> validSet10 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(-1, 2)));
        assertEquals(6, ShapeUtils.getShapeId(validSet10));

        Set<Coordinate> validSet11 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(0, 2)));
        assertEquals(6, ShapeUtils.getShapeId(validSet11));

        Set<Coordinate> validSet12 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(1, 3)));
        assertEquals(6, ShapeUtils.getShapeId(validSet12));

    }

    @Test
    void validateShape7(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(2, 1)));
        assertEquals(7, ShapeUtils.getShapeId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(1, 1)));
        assertEquals(7, ShapeUtils.getShapeId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(1, 1)));
        assertEquals(7, ShapeUtils.getShapeId(validSet3));

        Set<Coordinate> validSet4 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(2, 1)));
        assertEquals(7, ShapeUtils.getShapeId(validSet4));

        Set<Coordinate> validSet5 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 2)));
        assertEquals(7, ShapeUtils.getShapeId(validSet5));

        Set<Coordinate> validSet6 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(2, 2)));
        assertEquals(7, ShapeUtils.getShapeId(validSet6));

        Set<Coordinate> validSet7 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(2, 2)));
        assertEquals(7, ShapeUtils.getShapeId(validSet7));

        Set<Coordinate> validSet8 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(2, 2)));
        assertEquals(7, ShapeUtils.getShapeId(validSet8));

        Set<Coordinate> validSet9 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(1, 2)));
        assertEquals(7, ShapeUtils.getShapeId(validSet9));

        Set<Coordinate> validSet10 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(0, 2)));
        assertEquals(7, ShapeUtils.getShapeId(validSet10));

        Set<Coordinate> validSet11 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 2)));
        assertEquals(7, ShapeUtils.getShapeId(validSet11));

        Set<Coordinate> validSet12 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(1, 2)));
        assertEquals(7, ShapeUtils.getShapeId(validSet12));
    }

    @Test
    void validateShape8(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1)));
        assertEquals(8, ShapeUtils.getShapeId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1)));
        assertEquals(8, ShapeUtils.getShapeId(validSet2));
    }

    @Test
    void validateShape9(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2)));
        assertEquals(9, ShapeUtils.getShapeId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0)));
        assertEquals(9, ShapeUtils.getShapeId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2)));
        assertEquals(9, ShapeUtils.getShapeId(validSet3));
    }

    @Test
    void validateShape10(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 3), new Coordinate(5, 4)));
        assertEquals(10, ShapeUtils.getShapeId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(5, 3), new Coordinate(5, 4), new Coordinate(4, 4)));
        assertEquals(10, ShapeUtils.getShapeId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 4), new Coordinate(5, 4)));
        assertEquals(10, ShapeUtils.getShapeId(validSet3));

        Set<Coordinate> validSet4 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 3), new Coordinate(3, 4)));
        assertEquals(10, ShapeUtils.getShapeId(validSet4));

        Set<Coordinate> validSet5 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(3, 4), new Coordinate(4, 5)));
        assertEquals(10, ShapeUtils.getShapeId(validSet5));

        Set<Coordinate> validSet6 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 4), new Coordinate(4, 5)));
        assertEquals(10, ShapeUtils.getShapeId(validSet6));
    }

    @Test
    void validateShape11(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0)));
        assertEquals(11, ShapeUtils.getShapeId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1)));
        assertEquals(11, ShapeUtils.getShapeId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1)));
        assertEquals(11, ShapeUtils.getShapeId(validSet3));
    }

    @Test
    void getShapeCoordinatesForId() {
        Set<Coordinate> expected_1_2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,1), new Coordinate(2, 2), new Coordinate(3, 3)));
        assertEquals(expected_1_2, ShapeUtils.getShapeCoordinatesForId(1, 2));

        Set<Coordinate> expected_2_1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,1), new Coordinate(2, 1), new Coordinate(1, 2)));
        assertEquals(expected_2_1, ShapeUtils.getShapeCoordinatesForId(2, 1));

        Set<Coordinate> expected_3_1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,0), new Coordinate(0, 1), new Coordinate(1, 1)));
        assertEquals(expected_3_1, ShapeUtils.getShapeCoordinatesForId(3, 1));

        Set<Coordinate> expected_4_1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,0), new Coordinate(-1, 1), new Coordinate(0, 1)));
        assertEquals(expected_4_1, ShapeUtils.getShapeCoordinatesForId(4, 1));

        Set<Coordinate> expected_5_0 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,1), new Coordinate(2, 1), new Coordinate(2, 0)));
        assertEquals(expected_5_0, ShapeUtils.getShapeCoordinatesForId(5, 0));

        Set<Coordinate> expected_6_3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,1), new Coordinate(2, 1), new Coordinate(3, 1)));
        assertEquals(expected_6_3, ShapeUtils.getShapeCoordinatesForId(6, 3));

        Set<Coordinate> expected_7_6 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,1), new Coordinate(2, 1), new Coordinate(2, 2)));
        assertEquals(expected_7_6, ShapeUtils.getShapeCoordinatesForId(7, 6));

        Set<Coordinate> expected_8_1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,0), new Coordinate(1, 1)));
        assertEquals(expected_8_1, ShapeUtils.getShapeCoordinatesForId(8, 1));

        Set<Coordinate> expected_9_2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0,1), new Coordinate(0, 2)));
        assertEquals(expected_9_2, ShapeUtils.getShapeCoordinatesForId(9, 2));

        Set<Coordinate> expected_10_3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,0), new Coordinate(0, 1)));
        assertEquals(expected_10_3, ShapeUtils.getShapeCoordinatesForId(10, 3));

        Set<Coordinate> expected_11_1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0,1)));
        assertEquals(expected_11_1, ShapeUtils.getShapeCoordinatesForId(11, 1));
    }
}