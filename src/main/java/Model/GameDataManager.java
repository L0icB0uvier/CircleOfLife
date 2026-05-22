package Model;

import Global.Configuration;
import Global.PlayerNumber;
import Global.PlayerSettings;
import Global.Settings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class GameDataManager {
    static String savePath = "./sauvegardes/";

    /**
     * Sauvegarde un match ainsi que ses paramètres.
     * 
     * @param match    Le match à sauvegarder.
     * @param settings Les settings du match à sauvegarder.
     * @throws Exception Exception retournée lors de la sauvegarde du match.
     */
    public static void saveMatch(Match match, Settings settings) throws Exception {
        Files.createDirectories(Paths.get(savePath));
        BufferedWriter writer = new BufferedWriter(new FileWriter(savePath + getFileName(match, settings)));
        String sep = System.lineSeparator();

        // écrit settings
        writer.write(getPlayerType(settings.getPlayer1Settings()));
        if (!settings.getPlayer1Settings().isAI()) { // écrit nom
            writer.write(sep);
            writer.write(match.getPlayerData()[0].getName().replaceAll(" ", "_"));
        }
        writer.write(sep);
        writer.write(getPlayerType(settings.getPlayer2Settings()));
        if (!settings.getPlayer2Settings().isAI()) { // écrit nom
            writer.write(sep);
            writer.write(match.getPlayerData()[1].getName().replaceAll(" ", "_"));
        }
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

        writer.write(match.isReviewModeActive() + sep);

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
     * Converti le format du type d'IA lu dans le fichier vers le format attendu par updateAISettings dans configuration.
     * @param ai Le format du type d'IA lu dans le fichier.
     * @return Le format du type d'IA converti.
     */
    private static String convertAI(String ai){
        return switch (ai) {
            case "E" -> "Facile";
            case "M" -> "Moyen";
            case "H" -> "Difficile";
            default -> "";
        };
    }

    /**
     * Charge les données d'un match et créé un nouveau match avec ces données dans
     * Game.
     * 
     * @param game     L'instance de game dans laquelle charger le match.
     * @param filename Le nom du fichier à partir duquel charger (sans extension
     *                 .save).
     * @throws FileNotFoundException Exception retourné si le fichier n'est pas trouvé.
     */
    public static void loadMatch(Game game, String filename) throws FileNotFoundException {
        File file = new File(savePath + filename + ".save");
        Scanner scanner = new Scanner(file);

        // Read player type
        String[] playerTypes = new String[2];

        // Update Settings Joueur 1
        playerTypes[0] = scanner.next();
        if(playerTypes[0].equals("J"))
            Configuration.setPlayerSettings(PlayerNumber.PLAYER_1, null, scanner.next().replaceAll("_", " "));
        else{
            Configuration.updateAISettings(convertAI(playerTypes[0]), PlayerNumber.PLAYER_1);
        }

        // Update Settings Joueur 2
        playerTypes[1] = scanner.next();
        if(playerTypes[1].equals("J"))
            Configuration.setPlayerSettings(PlayerNumber.PLAYER_2, null, scanner.next().replaceAll("_", " "));
        else
            Configuration.updateAISettings(convertAI(playerTypes[1]), PlayerNumber.PLAYER_2);

        // Update Setting premier joueur
        int currentPlayerIndex = scanner.nextInt();
        Configuration.setStartingPlayerSetting(currentPlayerIndex);

        // Création du match
        game.createMatch(Configuration.getSettings().getPlayer1Settings().getName(),
                Configuration.getSettings().getPlayer2Settings().getName(), Configuration.getSettings().getStartingPlayerSetting());

        Match m = game.getMatch();

        // Reconstruction des moves
        int lenPast = scanner.nextInt();
        int lenFuture = scanner.nextInt();

        // read past et futur
        for (int k = 0; k < lenPast + lenFuture; k++) {
            Move temp;
            try {
                temp = readMove(m, scanner);
                game.playMove(temp);
            } catch (Exception e) {
                Configuration.error("Impossible de lire move");
            }
        }

        // revenir en arriere si on a future
        for (int i = 0; i < lenFuture; i++) {
            game.undo();
        }

        // read reviewMode
        boolean reviewModeActive = false;
        if (scanner.hasNext())
            reviewModeActive = scanner.nextBoolean();

        if (reviewModeActive || m.isGameOver())
            m.enterReviewMode();

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
    private static Move readMove(Match m, Scanner scanner) {
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
        String sepAlt = "-";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(
                "yyyy MM dd HH:mm:ss");
        char joueur1 = getPlayerType(settings.getPlayer1Settings());
        char joueur2 = getPlayerType(settings.getPlayer2Settings());
        PlayerData[] pd = m.getPlayerData();
        String playerDataString = playerDataToString(pd[0]) + sep
                + (joueur1 == 'J' ? "J" + sepAlt + pd[0].getName().replaceAll(" ", sepAlt) : joueur1) + sep
                + playerDataToString(pd[1]) + sep
                + (joueur2 == 'J' ? "J" + sepAlt + pd[1].getName().replaceAll(" ", sepAlt) : joueur2);
        return ZonedDateTime.now().format(fmt).replaceAll(" ", sep).replaceAll(":", sepAlt) + sep
                + playerDataString + ".save";
    }

    /**
     * Vérifie s'il existe des données à charger.
     * 
     * @return Vrai s'il existe des données et faux sinon.
     */
    public static boolean hasSaveFile() {
        return !getSaveFiles().isEmpty();
    }

    /**
     * Renvoie la liste de tous les fichiers ayant l'extension ".save" dans le
     * répertoire courant (l'extension "".save" étant supprimée).
     * 
     * @return Une Liste des Strings de nom de fichier avec l'extension ".save"
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
            Configuration.error("Pas de fichiers de sauvegardes trouvées");
            return new ArrayList<>();
        }
        return res;
    }

    private static String parsePlayer(String s, char numPlayer) {
        if (s.length() != 1)
            return s.substring(2).replaceAll("-", " ");
        String res = numPlayer == '1' ? "Joueur 1 " : "Joueur 2 ";
        char playerType = s.charAt(0);
        return switch (playerType) {
            case 'E' -> res + "(IA facile)";
            case 'M' -> res + "(IA moyenne)";
            case 'H' -> res + "(IA difficile)";
            default -> "";
        };
    }

    public static String[] parseFileName(String filename) {
        String[] res = new String[5];
        String sep = "_";
        filename = filename.replaceAll(".save", "");

        String[] arr = filename.split(sep);
        if (hasName(filename)) {
            arr = Arrays.copyOfRange(arr, 1, arr.length);
        } 
        String[] date = Arrays.copyOfRange(arr, 0, arr.length - 4);
        String[] game = Arrays.copyOfRange(arr, arr.length - 4, arr.length);

        // parse date
        date[date.length - 1] = date[date.length - 1].replaceAll("-", ":");
        res[0] = String.join(" ", date);

        // parse player 1
        res[1] = parsePlayer(game[1], '1');

        // parse player 2
        res[2] = parsePlayer(game[3], '2');

        // parse score (joueur 1 score - joueur 2 score)
        res[3] = game[0] + " - " + game[2];

        //name game
        res[4] = getName(filename);

        return res;
    }

    public static boolean saveFileExists(String filename) {
        filename = filename.replaceAll(".save", "");
        List<String> temp = getSaveFiles();

        return temp.contains(filename);

    }

    public static boolean deleteMatch(String fileName) {
        File fileToDelete = new File(savePath + fileName + ".save");
        return fileToDelete.delete();
    }

    //on suppose que le jeu est nommé si le premier élément de son nom n'est pas un nombre (cad l'année)
    private static boolean hasName(String filename) {
        String sep = "_";
        try {
            String[] temp = filename.split(sep);
            if (temp.length == 0)
                return false;
            Integer.parseInt(temp[0]);
        } catch (NumberFormatException e)  {
            return true;
        }

        return false;
    }

    private static String getName(String filename) {
        if (filename == null)
            return "";
        if (!hasName(filename))
            return "";
        String sep = "_";
        String sepAlt = "-";
        return filename.split(sep)[0].replaceAll(sepAlt, " ");
    }

    private static String constNewName(String filename, String newName) {
        if (!filename.endsWith(".save"))
            filename += ".save";
        String sep = "_";
        String sepAlt = "-";
        if (!hasName(filename))
            return newName.replaceAll(" ", sepAlt) + sep + filename;
        else {
            String[] temp = filename.split(sep);
            return newName.replaceAll(" ", sepAlt) + sep + String.join(sep, Arrays.copyOfRange(temp, 1, temp.length));
        }
    }

    public static String renameMatch(String filename, String newName) {
        if (!saveFileExists(filename))
            return "";
        if (!filename.endsWith(".save"))
            filename += ".save";
        Path source = Paths.get(savePath + filename);
        String newNameRes = constNewName(filename, newName);
        try {
            Files.move(source,
                    source.resolveSibling(newNameRes));
        } catch (IOException e) {
            Configuration.error("Erreur lors de rennomage du fichier");
            return filename;
        }
        return newNameRes.replaceAll(".save", "");
    }
}
