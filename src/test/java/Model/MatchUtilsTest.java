package Model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.*;

import static Model.MatchUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class MatchUtilsTest {
    Coordinate center = new Coordinate(4, 4);
    static Set<Coordinate> coordinates = new HashSet<>();

    /**
     * Initialise la liste des tuiles sur lesquelles les tests seront effectués (contient le plateau).
     */
    @BeforeAll
    public static void initialize() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Coordinate tileA = new Coordinate(i, j);

                coordinates.add(tileA);
            }
        }
    }

    /**
     * Vérifie que hexagonalManhattanDistance() renvoie bien une distance au sens mathématique.
      */
    @Test
    void testDistanceDefinition() {
        // construction de table des distances
        int[][][][] distances = new int[9][9][9][9];
        for (Coordinate tileA : coordinates) {
                for (Coordinate tileB : coordinates) {
                        distances[tileA.line()][tileA.col()][tileB.line()][tileB.col()] = hexagonalManhattanDistance(tileA, tileB);
                }
        }

        // parcours de la table et vérification de la définition d'une distance
        for (Coordinate tileA : coordinates) {
            for (Coordinate tileB : coordinates) {
                        assertAll("Tests de la définition mathématique d'une distance",
                                () -> assertTrue(distances[tileA.line()][tileA.col()][tileB.line()][tileB.col()] >= 0,
                                        "Une distance est positive"),
                                () -> assertEquals(distances[tileA.line()][tileA.col()][tileB.line()][tileB.col()] == 0,
                                        (tileA.line() == tileB.line() && tileA.col() == tileB.col()),
                                        "Une distance est nulle en seul cas d'égalité"),
                                () -> assertEquals(distances[tileA.line()][tileA.col()][tileB.line()][tileB.col()],
                                        distances[tileB.line()][tileB.col()][tileA.line()][tileA.col()],
                                        "Une distance est symétrique"));
                        for (Coordinate tileC : coordinates) {
                                assertTrue(distances[tileA.line()][tileA.col()][tileC.line()][tileC.col()]
                                                <= distances[tileA.line()][tileA.col()][tileB.line()][tileB.col()]
                                                + distances[tileB.line()][tileB.col()][tileC.line()][tileC.col()],
                                        "Une distance vérifie l'inégalité triangulaire");
                        }
            }
        }
    }


    /**
     * Vérifie que isInsideBoard() suit sa spécification.
     */
    @Test
    void testIsInsideBoard() {
        for (int col = 0; col < 9; col++) {
            for (int line = 0; line < 9; line++) {
                Coordinate tile = new Coordinate(col, line);

                assertEquals(hexagonalManhattanDistance(tile, center)<=4,
                         col - line >= -4 && col - line <= 4);
            }
        }
    }

    /**
     * Vérifie que isNeighbor() suit sa spécification.
     */
    @Test
    void checkFirstNeighbors() {
        for (Coordinate tileA : coordinates) {
                if (isInsideBoard(tileA)) {
                    Set<Coordinate> computedFirstNeighbors = new HashSet<>();

                    for (Coordinate tileB : coordinates) {
                            if (MatchUtils.isNeighbor(tileA, tileB) && MatchUtils.isInsideBoard(tileB)) {
                                computedFirstNeighbors.add(tileB);
                            }
                    }

                    assertEquals(expectedFirstNeighbors(tileA), computedFirstNeighbors,
                            "Mauvaise estimation du 1-voisinage en " + tileA);
                }
        }
    }

    /**
     * Calcule à la main les premiers voisins dans le plateau d'une case de coordonnées données.
     * @param tile un Coordinate contenant les coordonnées de la case.
     * @return l'ensemble des premiers voisins de la case donnée en argument.
     */
    private static Set<Coordinate> expectedFirstNeighbors(Coordinate tile) {
        int col = tile.col();
        int line = tile.line();
        Set<Coordinate> expectedNeighbors = new HashSet<>();

        if (col > 0 && line > 0) {          // côtés haut et haut-gauche
            expectedNeighbors.add(new Coordinate(col - 1, line - 1));        // case haut-gauche
        }
        if (col > 0 && col - line > -4) {   // côtés haut-gauche et bas-gauche
            expectedNeighbors.add(new Coordinate(col - 1, line));                // case gauche
        }
        if (col - line > -4 && line < 8) {  // côtés bas-gauche et bas
            expectedNeighbors.add(new Coordinate(col, line + 1));               // case bas-gauche
        }
        if (line < 8 && col < 8) {           // côtés bas et bas-droit
            expectedNeighbors.add(new Coordinate(col + 1, line + 1));      // case bas-droite
        }
        if (col < 8 && col - line < 4) {    // côtés bas-droit et haut-droit
            expectedNeighbors.add(new Coordinate(col + 1, line));               // case droite
        }
        if (col - line < 4 && line > 0) {   // côtés haut-droit et haut
            expectedNeighbors.add(new Coordinate(col, line - 1));              // case haut-droite
        }
        return expectedNeighbors;
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

        assertTrue(MatchUtils.isNeighbor(coord1, coord2));
        assertTrue(MatchUtils.isNeighbor(coord1, coord3));
        assertTrue(MatchUtils.isNeighbor(coord1, coord4));
        assertTrue(MatchUtils.isNeighbor(coord1, coord5));
        assertTrue(MatchUtils.isNeighbor(coord1, coord6));
        assertTrue(MatchUtils.isNeighbor(coord1, coord7));
        assertFalse(MatchUtils.isNeighbor(coord1, coord8));
        assertFalse(MatchUtils.isNeighbor(coord1, coord9));
        assertFalse(MatchUtils.isNeighbor(coord1, coord10));
    }

    @Test
    void calculatePointEarned() {
        Set<Critter> critters1 = new HashSet<>();
        critters1.add(CritterUtils.critterFromId(11, 1, 1));
        critters1.add(CritterUtils.critterFromId(10, 1, 1));
        assertEquals(5, MatchUtils.calculatePointEarned(critters1));

        Set<Critter> critters2 = new HashSet<>();
        critters2.add(CritterUtils.critterFromId(0, 0, 1));
        critters2.add(CritterUtils.critterFromId(1, 0, 1));
        assertEquals(5, MatchUtils.calculatePointEarned(critters2));

        Set<Critter> critters3 = new HashSet<>();
        critters3.add(CritterUtils.critterFromId(1, 0, 1));
        critters3.add(CritterUtils.critterFromId(2, 0, 1));
        critters3.add(CritterUtils.critterFromId(3, 0, 1));
        assertEquals(12, MatchUtils.calculatePointEarned(critters3));

        Set<Critter> critters4 = new HashSet<>();
        critters4.add(CritterUtils.critterFromId(1, 0, 1));
        critters4.add(CritterUtils.critterFromId(2, 0, 1));
        critters4.add(CritterUtils.critterFromId(12, 0, 1));
        assertEquals(8, MatchUtils.calculatePointEarned(critters4));

        assertEquals(0, MatchUtils.calculatePointEarned(null));
        assertEquals(0, MatchUtils.calculatePointEarned(new HashSet<>()));
    }
}
