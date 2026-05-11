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
    void resetScore(){
        match.updatePlayerScore(0, 5);
        match.resetScores();
        assertEquals(0, match.getPlayerScore(0));

        match.updatePlayerScore(1, 5);
        match.resetScores();
        assertEquals(0, match.getPlayerScore(0));
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

    @Test
    void gameOverByScore(){
        int currentPlayer = match.getCurrentPlayerIndex();
        int opponent = match.getOpponentPlayerIndex();
        match.updatePlayerScore(currentPlayer, 4);
        assertFalse(match.winByScore());
        match.updatePlayerScore(opponent, 3);
        assertFalse(match.winByScore());
        match.updatePlayerScore(currentPlayer, 16);
        assertTrue(match.winByScore());
    }

    @Test
    void gameOverByFillUp(){

    }

    @Test
    void initMatch(){
        Move moveP1_1 = new Move(match, 1, 1);
        match.apply(moveP1_1);
        match.endTurn();

        Move moveP2_1 = new Move(match, 3, 3);
        match.apply(moveP2_1);
        match.endTurn();

        Move moveP1_2 = new Move(match, 2, 2);
        match.apply(moveP1_2);
        match.endTurn();

        Move moveP2_2 = new Move(match, 8, 8);
        match.apply(moveP2_2);
        match.endTurn();

        match.undo();

        match.initMatch();
        assertEquals(61, match.getCurrentPlayerPlayableMoves().size());
        assertEquals(0, match.getPlayerScore(0));
        assertEquals(0, match.getPlayerScore(1));

        assertTrue(match.getCritters().isEmpty());
        assertTrue(match.getPreviouslyEatenCrittersCoordinates().isEmpty());

        assertEquals(0, match.past.size());
        assertEquals(0, match.future.size());
    }
}