package Model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CritterUtilsTest {
    @Test
    void NormalizeCoordinateCenter(){
        Set<Coordinate> coord = new HashSet<>(
                Set.of(new Coordinate(4, 5), new Coordinate(5, 6), new Coordinate(6, 6))
        );

        var normalizedCoords = CritterUtils.normalizeCoordinate(coord);
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

        var normalizedCoords = CritterUtils.normalizeCoordinate(coord);
        Set<Coordinate> expected = new HashSet<>(
                Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1))
        );
        assertEquals(normalizedCoords, expected);
    }

    @Test
    void validateShape0(){
        // Valid set
        Set<Coordinate> correctCoords = new HashSet<>(Set.of(new Coordinate(0, 0)));
        assertEquals(0, CritterUtils.getCritterId(correctCoords));

        // Valid set with normalization
        Set<Coordinate> correctCoordsUnormalized = new HashSet<>(Set.of(new Coordinate(5, 7)));
        assertEquals(0, CritterUtils.getCritterId(CritterUtils.normalizeCoordinate(correctCoordsUnormalized)));

        // Invalid set
        Set<Coordinate> incorrectCoords = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1)));
        assertNotEquals(0, CritterUtils.getCritterId(incorrectCoords));
    }


    @Test
    void validateShape1(){
        // Valid set 1
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3)));
        assertEquals(1, CritterUtils.getCritterId(validSet1));

        // Valid set 2
        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(3, 0)));
        assertEquals(1, CritterUtils.getCritterId(validSet2));

        // Valid set 3
        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3)));
        assertEquals(1, CritterUtils.getCritterId(validSet3));

        // Valid set with normalization
        Set<Coordinate> unormalizedSet = new HashSet<>(Set.of(new Coordinate(4, 3), new Coordinate(5, 4), new Coordinate(6, 5), new Coordinate(7, 6)));
        assertEquals(1, CritterUtils.getCritterId(CritterUtils.normalizeCoordinate(unormalizedSet)));

        // Invalid Set 1
        Set<Coordinate> invalidSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(2, 3)));
        assertNotEquals(1, CritterUtils.getCritterId(invalidSet1));

        // Invalid Set 1
        Set<Coordinate> invalidSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2)));
        assertNotEquals(1, CritterUtils.getCritterId(invalidSet2));
    }

    @Test
    void validateShape2(){
        // Valid set 1
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(1, 2)));
        assertEquals(2, CritterUtils.getCritterId(validSet1));

        // Valid set 2
        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(1, 2)));
        assertEquals(2, CritterUtils.getCritterId(validSet2));

        // Invalid Set 1
        Set<Coordinate> invalidSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1)));
        assertNotEquals(2, CritterUtils.getCritterId(invalidSet1));

        // Invalid Set 1
        Set<Coordinate> invalidSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(1, 3)));
        assertNotEquals(2, CritterUtils.getCritterId(invalidSet2));
    }

    @Test
    void validateShape3(){
        // Valid set 1
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(1, 1)));
        assertEquals(3, CritterUtils.getCritterId(validSet1));

        // Valid set 2
        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(1, 2)));
        assertEquals(3, CritterUtils.getCritterId(validSet2));

        // Valid set 3
        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 1)));
        assertEquals(3, CritterUtils.getCritterId(validSet3));

        // Invalid Set 1
        Set<Coordinate> invalidSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(2, 3)));
        assertNotEquals(3, CritterUtils.getCritterId(invalidSet1));

        // Invalid Set 1
        Set<Coordinate> invalidSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1)));
        assertNotEquals(3, CritterUtils.getCritterId(invalidSet2));
    }

    @Test
    void validateShape4(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(5, 4), new Coordinate(6, 5), new Coordinate(7, 5))); // 1
        assertEquals(4, CritterUtils.getCritterId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(5, 4), new Coordinate(4, 5), new Coordinate(3, 5))); // 2
        assertEquals(4, CritterUtils.getCritterId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(5, 5), new Coordinate(5, 6), new Coordinate(6, 7))); // 3
        assertEquals(4, CritterUtils.getCritterId(validSet3));

        Set<Coordinate> validSet4 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(4, 5), new Coordinate(5, 6), new Coordinate(5, 7))); // 4
        assertEquals(4, CritterUtils.getCritterId(validSet4));

        Set<Coordinate> validSet5 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(4, 5), new Coordinate(3, 5), new Coordinate(3, 6))); // 5
        assertEquals(4, CritterUtils.getCritterId(validSet5));

        Set<Coordinate> validSet6 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(5, 5), new Coordinate(6, 5), new Coordinate(7, 6))); // 6
        assertEquals(4, CritterUtils.getCritterId(validSet6));

        Set<Coordinate> invalidSet1 = new HashSet<>(Set.of(new Coordinate(4, 4), new Coordinate(5, 5), new Coordinate(3, 4), new Coordinate(7, 6))); // 6
        assertNotEquals(4, CritterUtils.getCritterId(invalidSet1));

    }

    @Test
    void validateShape5(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 4), new Coordinate(5, 4), new Coordinate(5, 3)));
        assertEquals(5, CritterUtils.getCritterId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 3), new Coordinate(3, 4), new Coordinate(5, 4)));
        assertEquals(5, CritterUtils.getCritterId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(3, 4), new Coordinate(4, 5), new Coordinate(5, 5)));
        assertEquals(5, CritterUtils.getCritterId(validSet3));

        Set<Coordinate> validSet4 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 4), new Coordinate(4, 5), new Coordinate(3, 5)));
        assertEquals(5, CritterUtils.getCritterId(validSet4));

        Set<Coordinate> validSet5 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 3), new Coordinate(3, 4), new Coordinate(4, 5)));
        assertEquals(5, CritterUtils.getCritterId(validSet5));

        Set<Coordinate> validSet6 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 3), new Coordinate(5, 4), new Coordinate(5, 5)));
        assertEquals(5, CritterUtils.getCritterId(validSet6));

    }

    @Test
    void validateShape6(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(0, 1)));
        assertEquals(6, CritterUtils.getCritterId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(-1, 1), new Coordinate(-2, 1)));
        assertEquals(6, CritterUtils.getCritterId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(3, 1)));
        assertEquals(6, CritterUtils.getCritterId(validSet3));

        Set<Coordinate> validSet4 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(3, 1)));
        assertEquals(6, CritterUtils.getCritterId(validSet4));

        Set<Coordinate> validSet5 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 1), new Coordinate(3, 2)));
        assertEquals(6, CritterUtils.getCritterId(validSet5));

        Set<Coordinate> validSet6 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 2)));
        assertEquals(6, CritterUtils.getCritterId(validSet6));

        Set<Coordinate> validSet7 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 2), new Coordinate(2, 3)));
        assertEquals(6, CritterUtils.getCritterId(validSet7));

        Set<Coordinate> validSet8 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(2, 3)));
        assertEquals(6, CritterUtils.getCritterId(validSet8));

        Set<Coordinate> validSet9 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(1, 3)));
        assertEquals(6, CritterUtils.getCritterId(validSet9));

        Set<Coordinate> validSet10 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(-1, 2)));
        assertEquals(6, CritterUtils.getCritterId(validSet10));

        Set<Coordinate> validSet11 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(0, 2)));
        assertEquals(6, CritterUtils.getCritterId(validSet11));

        Set<Coordinate> validSet12 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(1, 3)));
        assertEquals(6, CritterUtils.getCritterId(validSet12));

    }

    @Test
    void validateShape7(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(2, 1)));
        assertEquals(7, CritterUtils.getCritterId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(1, 1)));
        assertEquals(7, CritterUtils.getCritterId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(1, 1)));
        assertEquals(7, CritterUtils.getCritterId(validSet3));

        Set<Coordinate> validSet4 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0), new Coordinate(2, 1)));
        assertEquals(7, CritterUtils.getCritterId(validSet4));

        Set<Coordinate> validSet5 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 2)));
        assertEquals(7, CritterUtils.getCritterId(validSet5));

        Set<Coordinate> validSet6 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(2, 2)));
        assertEquals(7, CritterUtils.getCritterId(validSet6));

        Set<Coordinate> validSet7 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1), new Coordinate(2, 2)));
        assertEquals(7, CritterUtils.getCritterId(validSet7));

        Set<Coordinate> validSet8 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(2, 2)));
        assertEquals(7, CritterUtils.getCritterId(validSet8));

        Set<Coordinate> validSet9 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(1, 2)));
        assertEquals(7, CritterUtils.getCritterId(validSet9));

        Set<Coordinate> validSet10 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1), new Coordinate(0, 2)));
        assertEquals(7, CritterUtils.getCritterId(validSet10));

        Set<Coordinate> validSet11 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(-1, 1), new Coordinate(0, 1), new Coordinate(0, 2)));
        assertEquals(7, CritterUtils.getCritterId(validSet11));

        Set<Coordinate> validSet12 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(1, 2)));
        assertEquals(7, CritterUtils.getCritterId(validSet12));
    }

    @Test
    void validateShape8(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1)));
        assertEquals(8, CritterUtils.getCritterId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(1, 1)));
        assertEquals(8, CritterUtils.getCritterId(validSet2));
    }

    @Test
    void validateShape9(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2)));
        assertEquals(9, CritterUtils.getCritterId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0)));
        assertEquals(9, CritterUtils.getCritterId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2)));
        assertEquals(9, CritterUtils.getCritterId(validSet3));
    }

    @Test
    void validateShape10(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 3), new Coordinate(5, 4)));
        assertEquals(10, CritterUtils.getCritterId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(5, 3), new Coordinate(5, 4), new Coordinate(4, 4)));
        assertEquals(10, CritterUtils.getCritterId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 4), new Coordinate(5, 4)));
        assertEquals(10, CritterUtils.getCritterId(validSet3));

        Set<Coordinate> validSet4 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 3), new Coordinate(3, 4)));
        assertEquals(10, CritterUtils.getCritterId(validSet4));

        Set<Coordinate> validSet5 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(3, 4), new Coordinate(4, 5)));
        assertEquals(10, CritterUtils.getCritterId(validSet5));

        Set<Coordinate> validSet6 = new HashSet<>(Set.of(new Coordinate(3, 3), new Coordinate(4, 4), new Coordinate(4, 5)));
        assertEquals(10, CritterUtils.getCritterId(validSet6));
    }

    @Test
    void validateShape11(){
        Set<Coordinate> validSet1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 0)));
        assertEquals(11, CritterUtils.getCritterId(validSet1));

        Set<Coordinate> validSet2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0, 1)));
        assertEquals(11, CritterUtils.getCritterId(validSet2));

        Set<Coordinate> validSet3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1, 1)));
        assertEquals(11, CritterUtils.getCritterId(validSet3));
    }

    @Test
    void getCritterTypeCoordinates() {
        Set<Coordinate> expected_1_2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,1), new Coordinate(2, 2), new Coordinate(3, 3)));
        assertEquals(expected_1_2, CritterUtils.getCritterTypeCoordinates(1, 2));

        Set<Coordinate> expected_2_1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,1), new Coordinate(2, 1), new Coordinate(1, 2)));
        assertEquals(expected_2_1, CritterUtils.getCritterTypeCoordinates(2, 1));

        Set<Coordinate> expected_3_1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,0), new Coordinate(0, 1), new Coordinate(1, 1)));
        assertEquals(expected_3_1, CritterUtils.getCritterTypeCoordinates(3, 1));

        Set<Coordinate> expected_4_1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,0), new Coordinate(-1, 1), new Coordinate(0, 1)));
        assertEquals(expected_4_1, CritterUtils.getCritterTypeCoordinates(4, 1));

        Set<Coordinate> expected_5_0 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,1), new Coordinate(2, 1), new Coordinate(2, 0)));
        assertEquals(expected_5_0, CritterUtils.getCritterTypeCoordinates(5, 0));

        Set<Coordinate> expected_6_3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,1), new Coordinate(2, 1), new Coordinate(3, 1)));
        assertEquals(expected_6_3, CritterUtils.getCritterTypeCoordinates(6, 3));

        Set<Coordinate> expected_7_6 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,1), new Coordinate(2, 1), new Coordinate(2, 2)));
        assertEquals(expected_7_6, CritterUtils.getCritterTypeCoordinates(7, 6));

        Set<Coordinate> expected_8_1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,0), new Coordinate(1, 1)));
        assertEquals(expected_8_1, CritterUtils.getCritterTypeCoordinates(8, 1));

        Set<Coordinate> expected_9_2 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0,1), new Coordinate(0, 2)));
        assertEquals(expected_9_2, CritterUtils.getCritterTypeCoordinates(9, 2));

        Set<Coordinate> expected_10_3 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(1,0), new Coordinate(0, 1)));
        assertEquals(expected_10_3, CritterUtils.getCritterTypeCoordinates(10, 3));

        Set<Coordinate> expected_11_1 = new HashSet<>(Set.of(new Coordinate(0, 0), new Coordinate(0,1)));
        assertEquals(expected_11_1, CritterUtils.getCritterTypeCoordinates(11, 1));
    }
}