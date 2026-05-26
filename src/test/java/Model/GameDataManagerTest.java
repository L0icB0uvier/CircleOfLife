package Model;

import Global.Configuration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameDataManagerTest {

    @BeforeAll
    public static void initialize() {
        Configuration.instance();
        Configuration.initSettings();
        GameDataManager.testMode = true;
    }

    // parser tests
    @Test
    void testParserCorrectInput1() {
        String filename = "2026_05_20_10-33-38_20_J-komacchin_17_J-Desperis";
        String[] res = GameDataManager.parseFileName(filename);
        assertEquals(res[0], "2026 05 20 10:33:38");
        assertEquals(res[1], "komacchin");
        assertEquals(res[2], "Desperis");
        assertEquals(res[3], "20 - 17");
        assertEquals(res[4], "");
    }

    @Test
    void testParserCorrectInput2() {
        String filename = "match-du-siecle_2026_05_20_10-33-38_0_J-komacchin_0_J-Desperis";
        String[] res = GameDataManager.parseFileName(filename);
        assertEquals(res[0], "2026 05 20 10:33:38");
        assertEquals(res[1], "komacchin");
        assertEquals(res[2], "Desperis");
        assertEquals(res[3], "0 - 0");
        assertEquals(res[4], "match du siecle");
    }

    @Test
    void testParserCorrectInput3() {
        String filename = "robot-wars_2026_05_20_10-33-38_0_M_0_H";
        String[] res = GameDataManager.parseFileName(filename);
        assertEquals(res[0], "2026 05 20 10:33:38");
        assertEquals(res[1], "Joueur 1 (IA moyenne)");
        assertEquals(res[2], "Joueur 2 (IA difficile)");
        assertEquals(res[3], "0 - 0");
        assertEquals(res[4], "robot wars");
    }

    @Test
    void testParserIncorrectInputDateMissing() {
        String filename = "robot-wars_0_M_0_H";
        String[] res = GameDataManager.parseFileName(filename);
        assertNull(res);
    }

    @Test
    void testParserIncorrectInputPlayer1ScoreMissing() {
        String filename = "robot-wars_2026_05_20_10-33-38_M_0_H";
        String[] res = GameDataManager.parseFileName(filename);
        assertNull(res);
    }

    @Test
    void testParserIncorrectInputPlayer2ScoreMissing() {
        String filename = "robot-wars_2026_05_20_10-33-38_0_M_H";
        String[] res = GameDataManager.parseFileName(filename);
        assertNull(res);
    }

    @Test
    void testParserIncorrectInputPlayer1TypeMissing() {
        String filename = "robot-wars_2026_05_20_10-33-38_0_0_H";
        String[] res = GameDataManager.parseFileName(filename);
        assertNull(res);
    }

    @Test
    void testParserIncorrectInputPlayer2TypeMissing() {
        String filename = "robot-wars_2026_05_20_10-33-38_0_M_0";
        String[] res = GameDataManager.parseFileName(filename);
        assertNull(res);
    }

    @Test
    void testParserIncorrectInputIncorrectFileNames() {
        String filename = "klsadaldlk129231ö93(/=/%%!%=()Ö(=/ZUIK?OIUZTZUnsajlSúSL89X,Sosjhqsnauim??";
        String[] res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = "i-Uj%qri*c";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = ")b:a=!+,fE";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = ":]Z_!Fjx8F";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);
    }

    // parse name containing numbers

    @Test
    void testParserNameContainingNumbers() {
        String filename = "match-du-2026_2026_05_20_10-33-38_0_J-komacchin_0_J-Desperis";
        String[] res = GameDataManager.parseFileName(filename);
        assertEquals(res[0], "2026 05 20 10:33:38");
        assertEquals(res[1], "komacchin");
        assertEquals(res[2], "Desperis");
        assertEquals(res[3], "0 - 0");
        assertEquals(res[4], "match du 2026");
    }

    // parse incorrect dates
    @Test
    void testParserIncorrectDate() {
        String filename = "match-du-siecle_-5_05_20_10-33-38_0_J-komacchin_0_J-Desperis";
        String[] res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = "match-du-siecle_2026_-5_20_10-33-38_0_J-komacchin_0_J-Desperis";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = "match-du-siecle_2026_05_-5_10-33-38_0_J-komacchin_0_J-Desperis";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = "match-du-siecle_2026_0_20_10-33-38_0_J-komacchin_0_J-Desperis";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = "match-du-siecle_2026_05_0_10-33-38_0_J-komacchin_0_J-Desperis";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = "match-du-siecle_2026_05_20_32-33-38_0_J-komacchin_0_J-Desperis";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = "match-du-siecle_2026_05_20_10-70-38_0_J-komacchin_0_J-Desperis";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = "match-du-siecle_2026_-5_20_10-33-70_0_J-komacchin_0_J-Desperis";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = "match-du-siecle_2026_15_20_-10-33-38_0_J-komacchin_0_J-Desperis";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = "match-du-siecle_2026_15_20_10--33-38_0_J-komacchin_0_J-Desperis";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);

        filename = "match-du-siecle_2026_15_20_-10-33--38_0_J-komacchin_0_J-Desperis";
        res = GameDataManager.parseFileName(filename);
        assertNull(res);
    }

    // parse incorrect scores (negative)

    @Test
    public void testLoadMatchFichierVide() {
        try {
            Game g = new Game();
            assertFalse(GameDataManager.loadMatch(g, "fichier_vide"));

        } catch (FileNotFoundException e) {
        }

    }

    @Test
    public void testLoadMatchGameNomFichierNull() {
        try {
            Game g = new Game();
            assertFalse(GameDataManager.loadMatch(g, null));
            String s = "whatever";
            assertFalse(GameDataManager.loadMatch(null, s));

        } catch (FileNotFoundException e) {
        }

    }

    @Test
    public void testLoadMatchFichierIncorrecteTypesJoueursManquantes() {
        try {
            Game g = new Game();
            assertFalse(GameDataManager.loadMatch(g, "type_joueur1_manquante"));
            assertFalse(GameDataManager.loadMatch(g, "type_joueur2_manquante"));

        } catch (FileNotFoundException e) {
        }

    }

    // on exige que si le joueur est humain, il a un nom et si le joueur est une IA,
    // il n'a pas de nom
    @Test
    public void testLoadMatchFichierIncorrecteNomsJoueurs() {
        try {
            Game g = new Game();
            assertFalse(GameDataManager.loadMatch(g, "nom_joueur1_manquante"));
            assertFalse(GameDataManager.loadMatch(g, "nom_joueur2_manquante"));
            assertFalse(GameDataManager.loadMatch(g, "nom_donné_a_ia1"));
            assertFalse(GameDataManager.loadMatch(g, "nom_donné_a_ia2"));

        } catch (FileNotFoundException e) {
        }

    }

    @Test
    public void testLoadMatchFichierIndiceJoueurManquante() {
        try {
            Game g = new Game();
            assertFalse(GameDataManager.loadMatch(g, "indice_joueur_debutant_manquante"));

        } catch (FileNotFoundException e) {
        }

    }

    @Test
    public void testLoadMatchFichierIncorrecteMoves() {
        try {
            Game g = new Game();
            assertFalse(GameDataManager.loadMatch(g, "len_passe_manquante"));
            assertFalse(GameDataManager.loadMatch(g, "len_futur_manquante"));
            assertFalse(GameDataManager.loadMatch(g, "moins_de_move_que_len_futur"));
            assertFalse(GameDataManager.loadMatch(g, "moins_de_move_que_len_passe"));
            assertFalse(GameDataManager.loadMatch(g, "plus_de_move_que_len_passe"));
            assertFalse(GameDataManager.loadMatch(g, "plus_de_move_que_len_futur"));

        } catch (FileNotFoundException e) {
        }
    }

    @Test
    public void testLoadMatchCorrecte() {
        try {
            Game g = new Game();
            assertTrue(GameDataManager.loadMatch(g, "sauvegarde_correcte"));

        } catch (FileNotFoundException e) {
        }
    }

    // test renameMatch

    @Test
    public void testRenameMatchNoPreviousName() {
        String filename = "2026_05_20_10-33-38_20_J-komacchin_17_J-Desperis";
        String newName = "match du siecle";
        String newFileName = GameDataManager.renameMatch(filename, newName);
        assertEquals(newName.replaceAll(" ", "-") + "_" + filename, newFileName);
        GameDataManager.renameMatch(newFileName, "");
    }

    @Test
    public void testRenameMatchHasPreviousName() {
        String prefix = "test";
        String filename = "2026_05_20_10-33-38_20_J-komacchin_17_J-Desperis";
        String newName = "match du siecle";
        String newFileName = GameDataManager.renameMatch(prefix + "_" + filename, newName);
        assertEquals(newName.replaceAll(" ", "-") + "_" + filename, newFileName);
        GameDataManager.renameMatch(newFileName, prefix);
    }
}
