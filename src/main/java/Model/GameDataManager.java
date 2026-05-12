package Model;

import Global.Configuration;
import Global.PlayerSettings;
import Global.Settings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

import Controller.IA.AILevel;

public class GameDataManager {
    static String savePath = "./sauvegardes/";

    /**
     * Sauvegarde un match ainsi que ses paramètres.
     * 
     * @param match    Le match à sauvegarder.
     * @param settings Les settings du match à sauvegarder.
     * @throws Exception
     */
    public static void saveMatch(Match match, Settings settings) throws Exception {
        Files.createDirectories(Paths.get(savePath));
        BufferedWriter writer = new BufferedWriter(new FileWriter(savePath + getFileName(match, settings)));
        String sep = System.lineSeparator();

        // écrit settings
        writer.write(getPlayerType(settings.getPlayer1Settings()));
        writer.write(' ');
        writer.write(getPlayerType(settings.getPlayer2Settings()));
        writer.write(sep);

        // écrit le joueur courant
        writer.write(match.getCurrentPlayerIndex() + sep);

        // écrit le nombre de coup joué dans le passé et le futur
        writer.write(match.past.size() + sep);
        writer.write(match.future.size() + sep);

        // écrit le passé
        Iterator<Move> it = match.pastIterator();
        while (it.hasNext()) {
            Move m = it.next();
            writer.write(moveToLineColumn(m) + sep);
        }

        // écrit le futur
        it = match.futurIterator();
        while (it.hasNext()) {
            Move m = it.next();
            writer.write(moveToLineColumn(m) + sep);

        }

        writer.close();
    }

    /**
     * Retourne la représentation textuelle d'un Move.
     * 
     * @param m Move.
     * @return Un String correspondant à la représentation textuelle de m.
     */
    private static String moveToLineColumn(Move m) {
        String res = "";
        String sep = " ";
        res += m.getLine() + sep + m.getColumn();
        return res;
    }

    /**
     * Retourne la représentation textuelle d'un PlayerSettings.
     * 
     * @param settings PlayerSettings.
     * @return Un char correspondant à la représentation textuelle de settings.
     */
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

    /**
     * Charge les données d'un match et créé un nouveau match avec ces données dans
     * Game.
     * 
     * @param game     L'instance de game dans laquelle charger le match.
     * @param filename Le nom du fichier à partir duquel charger (sans extension
     *                 .save).
     * @throws FileNotFoundException
     */
    public static void loadMatch(Game game, String filename) throws FileNotFoundException {
        File file = new File(savePath + filename + ".save");
        Scanner scanner = new Scanner(file);

        // Read player type
        String[] playerTypes = new String[2];
        playerTypes[0] = scanner.next();
        playerTypes[1] = scanner.next();
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

        game.createMatch();
        Match m = game.getMatch();
        m.initMatch();

        int currentPlayerIndex = scanner.nextInt();

        int lenPast = scanner.nextInt();
        int lenFuture = scanner.nextInt();

        // calculé le currentPlayerIndex
        if (lenPast % 2 != 0) {
            currentPlayerIndex = currentPlayerIndex == 0 ? 1 : 0;
        }
        if (m.currentPlayerIndex != currentPlayerIndex)
            game.update();
        m.currentPlayerIndex = currentPlayerIndex;

        // read past et futur
        for (int k = 0; k < lenPast + lenFuture; k++) {
            Move temp;
            try {
                temp = readMove(m, scanner);
                game.playMove(temp);
            } catch (Exception e) {
                e.printStackTrace();
                Configuration.error("Impossible de lire move");
            }
        }

        // revenir en arriere si on a future
        for (int i = 0; i < lenFuture; i++) {
            game.undo();
        }

        scanner.close();
    }

    /**
     * Lit un déplacement à partir d'un scanner en supposant un format identique à
     * celui de moveToLineColumn.
     * 
     * @param m       Match sur lequel le move va s'appliquer.
     * @param scanner Scanner.
     * @return Un Move qui correspond à ce qui a été lu.
     */
    private static Move readMove(Match m, Scanner scanner) throws Exception {
        int l = scanner.nextInt();
        int c = scanner.nextInt();
        return new Move(m, l, c);
    }

    /**
     * Retourne la représentation textuelle d'un PlayerData.
     * 
     * @param pd PlayerData.
     * @return Un String correspondant à la représentation textuelle de pd.
     */
    private static String playerDataToString(PlayerData pd) {
        return pd.getScore() + "";
    }

    /**
     * Retourne un nom de fichier de format "<date_courant>_<infos_joueurs>.save"
     * avec avec des traits de soulignement comme séparateurs.
     * 
     * @param m        Un Match.
     * @param settings Les settings du match.
     * @return Vrai s'il existe des données et faux sinon.
     */
    public static String getFileName(Match m, Settings settings) {
        String sep = "_";
        String sepTime = "-";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(
                "EEEE d MMMM yyyy HH:mm:ss", Locale.FRENCH);
        PlayerData[] pd = m.getPlayerData();
        String playerDataString = playerDataToString(pd[0]) + sep + getPlayerType(settings.getPlayer1Settings()) + sep
                + playerDataToString(pd[1]) + sep + getPlayerType(settings.getPlayer2Settings());
        return ZonedDateTime.now().format(fmt).replaceAll(" ", sep).replaceAll(":", sepTime) + sep
                + playerDataString.toLowerCase() + ".save";
    }

    /**
     * Vérifie s'il existe des données à charger.
     * 
     * @return Vrai s'il existe des données et faux sinon.
     */
    public static boolean hasSaveFile() {
        return getSaveFiles().size() != 0;
    }

    /**
     * Renvoie la liste de tous les fichiers ayant l'extension ".save" dans le
     * répertoire courant (l'extension "".save" étant supprimée).
     * 
     * @return Une Liste des Strings de nom de fichier avec l'enxtension ".save"
     *         supprimée.
     */
    public static List<String> getSaveFiles() {
        Path dirPath = Paths.get(savePath);
        List<String> res = new ArrayList<>();

        try {
            Files.list(dirPath)
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .filter(filename -> filename.endsWith(".save"))
                    .forEach(filename -> res.add(filename.replaceAll(".save", "")));
        } catch (IOException e) {
            Configuration.error("Erreur lors d'acces a des fichiers de sauvegardes");
        }
        return res;
    }

    private static String parsePlayerType(String s) {
        char playerType = s.charAt(0);
        switch (playerType) {

            case 'e':
                return "(IA facile)";

            case 'm':
                return "(IA moyenne)";

            case 'h':
                return "(IA difficile)";

            default:
                return "";
        }
    }

    public static String[] parseFileName(String filename) {
        String[] res = new String[4];
        String sep = "_";
        if (filename.endsWith(".save"))
            filename.replaceAll(".save", "");

        String arr[] = filename.split(sep);

        String date[] = Arrays.copyOfRange(arr, 0, arr.length - 4);
        String game[] = Arrays.copyOfRange(arr, arr.length - 4, arr.length);

        // parse date
        date[date.length - 1] = date[date.length - 1].replaceAll("-", ":");
        res[0] = String.join(" ", date);

        // parse player 1
        res[1] = "Joueur 1 " + parsePlayerType(game[1]);

        // parse player 2
        res[2] = "Joueur 2 " + parsePlayerType(game[3]);

        // parse score (joueur 1 score - joueur 2 score)
        res[3] = game[0] + " - " + game[2];

        return res;
    }
}
