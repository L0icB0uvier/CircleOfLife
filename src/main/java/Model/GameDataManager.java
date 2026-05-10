package Model;

import Global.Configuration;
import Global.PlayerSettings;
import Global.Settings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import Controller.IA.AILevel;

public class GameDataManager {
    static String savePath = "./";

    /**
     * Sauvegarde un match ainsi que ses paramètres.
     * 
     * @param match    Le match à sauvegarder.
     * @param settings Les settings du match à sauvegarder.
     * @throws Exception
     */
    public static void saveMatch(Match match, Settings settings) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName(match)));
        String sep = System.lineSeparator();

        // écrit le passé
        writer.write(match.past.size() + sep);
        Iterator<Move> it = match.pastIterator();
        while (it.hasNext()) {
            Move m = it.next();
            writer.write(moveToString(m) + sep);
        }

        // écrit le futur
        writer.write(match.future.size() + sep);
        it = match.futurIterator();
        while (it.hasNext()) {
            Move m = it.next();
            writer.write(moveToString(m) + sep);

        }

        // écrit settings
        writer.write(getPlayerType(settings.getPlayer1Settings()));
        writer.write(' ');
        writer.write(getPlayerType(settings.getPlayer2Settings()));

        writer.close();
    }

    private static String moveToString(Move m) {
        String res = "";
        String sep = System.lineSeparator();
        res += m.getLine() + " " + m.getColumn() + sep;
        // convertir previousState
        for (byte[] arr : m.previousState) {
            res += arrToString(arr) + sep;
        }

        // convertir critters
        res += m.critters.size() + "";
        res += sep;

        for (Critter critter : m.critters) {
            res += critterToString(critter) + sep;
        }

        // convertir previousScore

        res += playerDataToString(m.previousScore[0]) + sep;
        res += playerDataToString(m.previousScore[1]);

        return res;
    }

    private static String critterToString(Critter c) {
        String res = "";
        String sep = System.lineSeparator();
        Set<Coordinate> coordinates = c.stonesCoordinates();
        int player = c.player();

        res += coordinates.size() + sep;
        for (Coordinate coordinate : coordinates) {
            res += coordinate.col() + " " + coordinate.line() + sep;
        }
        res += player;
        return res;
    }

    private static String playerDataToString(PlayerData pd) {
        return pd.score + "";
    }

    private static char getPlayerType(PlayerSettings settings) {
        if (!settings.isAI())
            return 'J';
        else {
            switch (settings.getAiLevel()) {
                case EASY -> {
                    return 'E';
                }
                case MEDIUM -> {
                    return 'M';
                }
                case HARD -> {
                    return 'H';
                }
            }
        }
        return Character.MAX_HIGH_SURROGATE;
    }

    private static String arrToString(byte[] arr) {
        String res = "";
        String sep = " ";
        int t = arr.length - 1;
        for (int i = 0; i < t; i++) {
            res += Byte.toUnsignedInt(arr[i]) + sep;
        }
        res += Byte.toUnsignedInt(arr[t]);
        return res;
    }

    /**
     * Charge les données d'un match et créé un mnouveau match avec ces données dans
     * Game.
     * 
     * @param game L'instance de game dans laquelle charger le match.
     * @throws FileNotFoundException
     */
    public static void loadMatch(Game game) throws FileNotFoundException {
        File file = new File(savePath);
        Scanner scanner = new Scanner(file);
        Match m = new Match();
        m.initMatch();

        // read past
        int lenPast = scanner.nextInt();
        for (int k = 0; k < lenPast; k++) {
            Move temp;
            try {
                temp = readMove(m, scanner);
                m.apply(temp);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // read future
        int lenFuture = scanner.nextInt();

        for (int k = 0; k < lenFuture; k++) {
            Move temp;
            try {
                temp = readMove(m, scanner);
                m.future.addFirst(temp);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        // Read player type
        String playerTypesLine = scanner.nextLine();
        String[] playerTypes = playerTypesLine.split(" ");
        if (Objects.equals(playerTypes[0], "J")) {
            Configuration.setPlayer1Settings(null);
        } else {
            if (Objects.equals(playerTypes[0], "E")) {
                Configuration.setPlayer1Settings(AILevel.EASY);
            } else if (Objects.equals(playerTypes[0], "M")) {
                Configuration.setPlayer1Settings(AILevel.MEDIUM);
            } else if (Objects.equals(playerTypes[0], "H")) {
                Configuration.setPlayer1Settings(AILevel.HARD);
            }
        }

        if (Objects.equals(playerTypes[1], "J")) {
            Configuration.setPlayer2Settings(null);
        } else {
            if (Objects.equals(playerTypes[1], "E")) {
                Configuration.setPlayer2Settings(AILevel.EASY);
            } else if (Objects.equals(playerTypes[1], "M")) {
                Configuration.setPlayer2Settings(AILevel.MEDIUM);
            } else if (Objects.equals(playerTypes[1], "H")) {
                Configuration.setPlayer2Settings(AILevel.HARD);
            }
        }

        scanner.close();
    }

    private static Move readMove(Match m, Scanner scanner) throws Exception {

        // lire ligne/colonne

        int l = scanner.nextInt();
        int c = scanner.nextInt();

        // lire previousState
        for (int i = 0; i < m.boardState.length; i++) {
            for (int j = 0; j < m.boardState[i].length; j++) {
                m.boardState[i][j] = (byte) scanner.nextInt();
            }
        }

        // lire critters
        int nbCritters = scanner.nextInt();
        m.critters = new HashSet<>(nbCritters);

        for (int i = 0; i < nbCritters; i++) {
            m.critters.add(readCritter(scanner));
        }

        // lire players
        for (int i = 0; i < m.players.length; i++) {
            m.players[i] = readPlayerData(scanner);
        }

        return new Move(m, l, c);
    }

    private static Critter readCritter(Scanner scanner) throws Exception {

        int line, col;
        int nbCoordinates = scanner.nextInt();
        Set<Coordinate> coordinates = new HashSet<>(nbCoordinates);
        for (int i = 0; i < nbCoordinates; i++) {
            col = scanner.nextInt();
            line = scanner.nextInt();
            coordinates.add(new Coordinate(col, line));
        }

        int player = scanner.nextInt();
        return new Critter(coordinates, player);
    }

    private static PlayerData readPlayerData(Scanner s) throws Exception {
        int score = s.nextInt();
        return new PlayerData(score);
    }

    public static String getFileName(Match m) {
        Date d = new Date();
        PlayerData[] pd = m.getPlayerData();
        String playerDataString = playerDataToString(pd[0]) + " " + playerDataToString(pd[1]);
        return d.toString().replaceAll(" ", "_") + "_" + playerDataString.replaceAll(" ", "_") + ".save";

    }

    /**
     * Vérifie s'il existe des données à charger.
     * 
     * @return Vrai s'il existe des données et faux sinon.
     */
    public static boolean hasSaveFile() {
        Path path = Paths.get(savePath);

        if (Files.exists(path)) {
            Configuration.info("Le fichier existe !");
            return true;
        } else {
            Configuration.info("Le fichier est introuvable.");
            return false;
        }
    }
}
