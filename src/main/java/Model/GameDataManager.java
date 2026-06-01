package Model;

import Global.Configuration;
import Global.PlayerNumber;
import Global.PlayerSettings;
import Global.Settings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class GameDataManager {
    private static final String savePath = "./sauvegardes/";
    private static final String testPath = "./test_sauvegardes/";
    static boolean testMode = false;

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

        // écrit le joueur qui a commencé la partie
        writer.write(match.startingPlayer + sep);

        // écrit le nombre de coups joué dans le passé et le futur
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

        writer.write(match.winType.name() + sep);

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
     * Converti le format du type d'IA lu dans le fichier vers le format attendu par
     * updateAISettings dans Configuration.
     * 
     * @param ai Le format du type d'IA lu dans le fichier.
     * @return Le format du type d'IA converti.
     */
    private static String convertAI(String ai) {
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
     * @return true si la fonction a réussi du load, false sinon
     * @throws FileNotFoundException Exception retournée si le fichier n'est pas
     *                               trouvé.
     */
    public static boolean loadMatch(Game game, String filename) throws FileNotFoundException {
        if (filename == null || game == null) {
            Configuration.warning("game et ou filename null");
            return false;
        }

        if (!filename.endsWith(".save"))
            filename += ".save";

        File file;

        if (!testMode)
            file = new File(savePath + filename);
        else {
            try {
                Files.createDirectories(Paths.get(testPath));
            } catch (IOException e) {
                Configuration.warning("impossible de créer le dossier des tests");
                return false;
            }
            file = new File(testPath + filename);
        }

        Scanner scanner = new Scanner(file);

        // Read player type
        String[] playerTypes = new String[2];

        // Update Settings Joueur 1
        if (scanner.hasNext())
            playerTypes[0] = scanner.next();
        else {
            Configuration.warning("Impossible de lire le type de joueur 1 dans le fichier " + filename);
            return false;
        }
        if (playerTypes[0].equals("J")) {
            if (scanner.hasNext())
                Configuration.setPlayerSettings(PlayerNumber.PLAYER_1, null, scanner.next().replaceAll("_", " "));
            else {
                Configuration.warning("Nom manquantes pour le joueur 1 dans le fichier " + filename);
                return false;
            }
        } else if (playerTypes[0].equals("E") || playerTypes[0].equals("M") || playerTypes[0].equals("H")) {
            Configuration.updateAISettings(convertAI(playerTypes[0]), PlayerNumber.PLAYER_1);
        } else {
            Configuration.warning("Le type de joueur incorrecte pour le joueur 1 dans le fichier " + filename);
            return false;
        }

        // Update Settings Joueur 2
        if (scanner.hasNext())
            playerTypes[1] = scanner.next();
        else {
            Configuration.warning("Impossible de lire le type de joueur 2 dans le fichier " + filename);
            return false;
        }

        if (playerTypes[1].equals("J")) {
            if (scanner.hasNext())
                Configuration.setPlayerSettings(PlayerNumber.PLAYER_2, null, scanner.next().replaceAll("_", " "));
            else {
                Configuration.warning("Nom manquantes pour le joueur 2 dans le fichier " + filename);
                return false;
            }
        } else if (playerTypes[1].equals("E") || playerTypes[1].equals("M") || playerTypes[1].equals("H"))
            Configuration.updateAISettings(convertAI(playerTypes[1]), PlayerNumber.PLAYER_2);
        else {
            Configuration.warning("Le type de joueur incorrecte pour le joueur 2 dans le fichier " + filename);
            return false;
        }

        // Update Setting premier joueur
        int startingPlayerIndex;
        if (scanner.hasNextInt())
            startingPlayerIndex = scanner.nextInt();
        else {
            Configuration.warning("Impossible de lire le joueur commençant la partie dans le fichier " + filename);
            return false;
        }

        if (startingPlayerIndex!=0 && startingPlayerIndex !=1) {
            Configuration.warning("L'indice de joueur débutant est incorrect dans le fichier " + filename);
            return false;
        }

        Configuration.setStartingPlayerSetting(startingPlayerIndex);

        // Création du match
        game.createMatch(Configuration.getSettings().getPlayer1Settings().getName(),
                Configuration.getSettings().getPlayer2Settings().getName(),
                Configuration.getSettings().getStartingPlayerSetting());

        Match m = game.getMatch();

        // Reconstruction des moves
        int lenPast, lenFuture;
        if (scanner.hasNextInt())
            lenPast = scanner.nextInt();
        else {
            Configuration.warning("Impossible de lire le nombre du coup joué dans le fichier " + filename);
            return false;
        }

        if (scanner.hasNextInt())
            lenFuture = scanner.nextInt();
        else {
            Configuration.warning("Impossible de lire le nombre de undo dans le fichier " + filename);
            return false;
        }

        // read past et futur
        for (int k = 0; k < lenPast + lenFuture; k++) {
            Move temp;
            try {
                temp = readMove(m, scanner);
                game.playMove(temp);
            } catch (Exception e) {
                Configuration.warning("Impossible de lire move");
                return false;
            }
        }

        // revenir en arriere si on a future
        for (int i = 0; i < lenFuture; i++) {
            if (!game.canUndo()) {
                Configuration.warning("Plus de undo a faire que des moves totales dans le fichier " + filename);
                return false;
            }
            game.undo();
        }

        if (scanner.hasNextInt()) {
            Configuration.warning("Pas tous les moves lus dans le fichier " + filename);
            return false;
        }

        if (scanner.hasNext()) {
            String winTypeText = scanner.next();
            WinType winType = WinType.valueOf(winTypeText);
            if(winType == WinType.GIVE_UP)
                game.giveUp();
        }

        scanner.close();
        return true;
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
     * avec des traits de soulignement comme séparateurs.
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
     *         supprimée triée en ordre décroissante par rapport leurs champs de date.
     */
    public static List<String> getSaveFiles() {
        Path dirPath;
        if (!testMode)
            dirPath = Paths.get(savePath);
        else
            dirPath = Paths.get(testPath);
        List<String> res = new ArrayList<>();

        // On déclare le Stream dans le try(...) pour qu'il soit fermé automatiquement
        try (Stream<Path> stream = Files.list(dirPath)) {
            stream.filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .filter(filename -> filename.endsWith(".save"))
                    .sorted((a, b) -> { // trier par les dates
                        String s1 = removeName(a);
                        String s2 = removeName(b);
                        return s2.compareTo(s1);
                    })
                    .forEach(filename -> res.add(filename.replaceAll(".save", "")));
        } catch (IOException e) {
            Configuration.info("Pas de fichiers de sauvegardes trouvées");
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

    private static boolean validDate(String date) {
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern(
                "yyyy MM dd HH:mm:ss");
                LocalDateTime.parse(date, fmt);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    
    /**
     * Analyser un nom de fichier et le convertir en un tableau de 5 éléments : 
     * -0 la date 
     * -1 le nom du premier joueur 
     * -2 le nom du deuxième joueur
     * -3 le score du match, 
     * -4 le nom du match (s'il existe, sinon une chaîne vide "").
     * 
     * @param filename nom du fichier.
     * 
     * @return Un tableau de string en cas du succès, null sinon.
     */
    public static String[] parseFileName(String filename) {
        if (filename == null)
            return null;
        String[] res = new String[5];
        String sep = "_";
        filename = filename.replaceAll(".save", "");

        String[] arr = filename.split(sep);
        if (hasName(filename)) {
            arr = Arrays.copyOfRange(arr, 1, arr.length);
        }

        String[] date, game;

        try {
            date = Arrays.copyOfRange(arr, 0, arr.length - 4);
            game = Arrays.copyOfRange(arr, arr.length - 4, arr.length);
        } catch (Exception e) {
            Configuration.warning("Impossible de lire le nom du fichier");
            return null;
        }

        if (date.length != 4) { // année, mois, jour, temps
            Configuration.warning("Détails concernant le date manquants");
            return null;
        }
        // parse date
        date[date.length - 1] = date[date.length - 1].replaceAll("-", ":");
        res[0] = String.join(" ", date);
        if (!validDate(res[0])) {
            Configuration.warning("Date invalide");
            return null;
        }

        if (game.length != 4) { // deux joueurs avec leurs propres scores
            Configuration.warning("Détails concernant le jeu manquants");
            return null;
        }
        // parse player 1
        res[1] = parsePlayer(game[1], '1');

        // parse player 2
        res[2] = parsePlayer(game[3], '2');

        // parse score (joueur 1 score - joueur 2 score)
        res[3] = game[0] + " - " + game[2];

        // name game
        res[4] = getName(filename);

        return res;
    }

    /**
     * Vérifie si un fichier existe dans le répertoire des saves
     * 
     * @param filename nom du fichier
     * @return true si le fichier existe dans le répertoire des saves, false sinon
     */
    public static boolean saveFileExists(String filename) {
        filename = filename.replaceAll(".save", "");
        List<String> temp = getSaveFiles();

        return temp.contains(filename);

    }

    /**
     * Supprime un fichier de save dans le répertoire des saves
     * 
     * @param fileName nom du fichier
     * @return true si le fichier a été supprimé avec succès, false sinon
     */
    public static boolean deleteMatch(String fileName) {
        File fileToDelete = new File(savePath + fileName + ".save");
        return fileToDelete.delete();
    }

    
    private static boolean hasName(String filename) {
        String sep = "_";
        return filename.split(sep).length == 9;
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

    private static String removeName(String filename) {
        if (!hasName(filename)) return filename;
        else {
            String sep = "_";
            String[] temp = filename.split(sep);
            return String.join(sep, Arrays.copyOfRange(temp, 1, temp.length));
        }
    }

    private static String constNewName(String filename, String newName) {
        if (!filename.endsWith(".save"))
            filename += ".save";
        if (newName.isEmpty())
            return removeName(filename);
        String sep = "_";
        String sepAlt = "-";
        if (!hasName(filename))
            return newName.replaceAll(" ", sepAlt) + sep + filename;
        else {
            return newName.replaceAll(" ", sepAlt) + sep + removeName(filename);
        }
    }


    public static boolean nameTooLongPlayer(String name) {
        return name.length()>10;
    }

    public static boolean nameTooLongMatch(String name) {
        return name.length()>20;
    }

    private static boolean invalidFileName(String newName) {
        if (newName==null || newName.equals(""))
            return false;
        try {
            Files.createFile(Paths.get(newName));
            Files.delete(Paths.get(newName));
        } catch (IOException e) {
            return true;
        }
        return false;
    }

    /**
     * Vérifie si le string donné contient des caractères illégaux (des séparateurs des différents champs des noms c.-à-d. underscore)
     * 
     * @param newName string
     * @return true si le fichier contient des caractères illégaux, false sinon
     */
    public static boolean newNameContainsSeparator(String newName) {
        String sep = "_";
        return newName.contains(sep) || invalidFileName(newName);
    }

    /**
     * Renomme le fichier de sauvegarde correspondant à filename en lui attribuant
     * le nouveau nom « newName » (ajoute « newName » comme suffixe si c'est un nom
     * valide, ou supprime le fichier si « newName » est une chaîne vide).
     * 
     * @param filename nom du fichier de sauvegarde
     * @param newName nouvel nom candidat pour ce fichier
     * @return le nouvel nom du fichier de la sauvegarde en cas de succès (sans extension .save), chaine vide sinon
     */
    public static String renameMatch(String filename, String newName) {
        if (!saveFileExists(filename))
            return "";
        if (!filename.endsWith(".save"))
            filename += ".save";
        if (newNameContainsSeparator(newName))
            return "";
        Path source = Paths.get((!testMode ? savePath : testPath) + filename);
        String newNameRes = constNewName(filename, newName);
        try {
            Files.move(source,
                    source.resolveSibling(newNameRes));
        } catch (IOException e) {
            Configuration.warning("Erreur lors de renommage du fichier");
            return filename;
        }
        return newNameRes.replaceAll(".save", "");
    }
}
