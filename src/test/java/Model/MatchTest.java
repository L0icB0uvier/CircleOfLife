package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    Match match;

    @BeforeEach
    void init(){
        match = new Match();
    }

    @Test
    void evolve() {
        Coordinate firstCoord = new Coordinate(5, 5);
        match.updateCritters(firstCoord);
        assertEquals(0 ,match.getCritterAtCoord(firstCoord).type);
        assertEquals(1, match.getNumberOfCritters());

        Coordinate secondCoord = new Coordinate(6, 5);
        match.updateCritters(secondCoord);
        assertEquals(11, match.getCritterAtCoord(firstCoord).type);
        assertEquals(11 , match.getCritterAtCoord(secondCoord).type);
        assertEquals(1, match.getNumberOfCritters());

        Coordinate thirdCoord = new Coordinate(6, 6);
        match.updateCritters(thirdCoord);
        assertEquals(8, match.getCritterAtCoord(firstCoord).type);
        assertEquals(8, match.getCritterAtCoord(secondCoord).type);
        assertEquals(8, match.getCritterAtCoord(thirdCoord).type);
        assertEquals(1, match.getNumberOfCritters());

        Coordinate fourthCoord = new Coordinate(7, 5);
        match.updateCritters(fourthCoord);
        assertEquals(7, match.getCritterAtCoord(firstCoord).type);
        assertEquals(7, match.getCritterAtCoord(secondCoord).type);
        assertEquals(7, match.getCritterAtCoord(thirdCoord).type);
        assertEquals(7, match.getCritterAtCoord(fourthCoord).type);
        assertEquals(1, match.getNumberOfCritters());
    }

    @Test
    void feed() {
    }

    @Test
    void isNeighbor() {
        Coordinate coord1 = new Coordinate(2, 2);
        Coordinate coord2 = new Coordinate(1, 1);
        Coordinate coord3 = new Coordinate(1, 2);
        Coordinate coord4 = new Coordinate(2, 3);
        Coordinate coord5 = new Coordinate(3, 3);
        Coordinate coord6 = new Coordinate(3, 2);
        Coordinate coord7 = new Coordinate(2, 1);
        Coordinate coord8 = new Coordinate(4, 4);
        Coordinate coord9 = new Coordinate(0, 0);
        Coordinate coord10 = new Coordinate(0, 2);

        assertTrue(match.isNeighbor(coord1, coord2));
        assertTrue(match.isNeighbor(coord1, coord3));
        assertTrue(match.isNeighbor(coord1, coord4));
        assertTrue(match.isNeighbor(coord1, coord5));
        assertTrue(match.isNeighbor(coord1, coord6));
        assertTrue(match.isNeighbor(coord1, coord7));
        assertFalse(match.isNeighbor(coord1, coord8));
        assertFalse(match.isNeighbor(coord1, coord9));
        assertFalse(match.isNeighbor(coord1, coord10));
    }
}