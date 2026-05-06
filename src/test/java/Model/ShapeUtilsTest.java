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

    }

    @Test
    void validateShape5(){

    }

    @Test
    void validateShape6(){}

    @Test
    void validateShape7(){}

    @Test
    void validateShape8(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1)));
        assertEquals(8, ShapeUtils.getShapeId(validSet1));
    }

    @Test
    void validateShape9(){}

    @Test
    void validateShape10(){}

    @Test
    void validateShape11(){}
}