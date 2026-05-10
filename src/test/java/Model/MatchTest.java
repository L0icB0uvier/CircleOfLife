package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

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
        assertEquals(0 ,match.getCritterAtCoord(firstCoord).type());
        assertEquals(1, match.getNumberOfCritters());

        Coordinate secondCoord = new Coordinate(6, 5);
        match.updateCritters(secondCoord);
        assertEquals(11, match.getCritterAtCoord(firstCoord).type());
        assertEquals(11 , match.getCritterAtCoord(secondCoord).type());
        assertEquals(1, match.getNumberOfCritters());

        Coordinate thirdCoord = new Coordinate(6, 6);
        match.updateCritters(thirdCoord);
        assertEquals(8, match.getCritterAtCoord(firstCoord).type());
        assertEquals(8, match.getCritterAtCoord(secondCoord).type());
        assertEquals(8, match.getCritterAtCoord(thirdCoord).type());
        assertEquals(1, match.getNumberOfCritters());

        Coordinate fourthCoord = new Coordinate(7, 5);
        match.updateCritters(fourthCoord);
        assertEquals(7, match.getCritterAtCoord(firstCoord).type());
        assertEquals(7, match.getCritterAtCoord(secondCoord).type());
        assertEquals(7, match.getCritterAtCoord(thirdCoord).type());
        assertEquals(7, match.getCritterAtCoord(fourthCoord).type());
        assertEquals(1, match.getNumberOfCritters());
    }

    @Test
    void feed() {

    }


    @Test
    void updatePlayerScore(){
        assertEquals(0, match.getPlayerScore(0));
        assertEquals(0, match.getPlayerScore(1));
        match.updatePlayerScore(0, 5);
        assertEquals(5, match.getPlayerScore(0));
        assertEquals(0, match.getPlayerScore(1));
        match.updatePlayerScore(1, 2);
        assertEquals(5, match.getPlayerScore(0));
        assertEquals(2, match.getPlayerScore(1));
        match.updatePlayerScore(1, 4);
        assertEquals(5, match.getPlayerScore(0));
        assertEquals(6, match.getPlayerScore(1));

    }
}