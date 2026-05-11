package Model;

import Global.Configuration;
import Global.PlayerSettings;
import Global.Settings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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
        BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName(match, settings)));
        String sep = System.lineSeparator();

        // écrit settings
        writer.write(getPlayerType(settings.getPlayer1Settings()));
        writer.write(' ');
        writer.write(getPlayerType(settings.getPlayer2Settings()));
        writer.write(sep);

        // écrit le joueur courant
        writer.write(match.getCurrentPlayerIndex() + sep);


        //écrit le nombre de coup joué dans le passé et le futur (passé + futur = nb total coups)
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

    private static String moveToLineColumn(Move m) {
        String res = "";
        String sep = " ";
        res += m.getLine() + sep + m.getColumn();
        return res;
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

    /**
     * Charge les données d'un match et créé un mnouveau match avec ces données dans
     * Game.
     * 
     * @param game L'instance de game dans laquelle charger le match.
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
            System.out.println("in here");
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
        int nbMove = lenPast==0?lenFuture:lenPast; //si il n'y a pas de passé, on derive currentPlayerIndex depuis le futur
        // read past

        if (nbMove % 2 != 0) // calculé le currentPlayerIndex
            currentPlayerIndex = currentPlayerIndex == 0 ? 1 : 0;
        m.currentPlayerIndex = currentPlayerIndex;

        for (int k = 0; k < lenPast; k++) {
            Move temp;
            try {
                temp = readMove(m, scanner);
                System.out.println(temp.getColumn() + " " + temp.getLine());
                m.apply(temp);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // read future
        for (int k = 0; k < lenFuture; k++) {
            Move temp;
            try {
                temp = readMove(m, scanner);
                System.out.println(temp.getColumn() + " " + temp.getLine());
                m.apply(temp);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        for (int i = 0; i < lenFuture; i++) { // revenir en arriere si on a future
            m.undo();
        }

        scanner.close();
    }

    private static Move readMove(Match m, Scanner scanner) throws Exception {
        int l = scanner.nextInt();
        int c = scanner.nextInt();
        return new Move(m, l, c);
    }

    private static String playerDataToString(PlayerData pd) {
        return pd.getScore()+ "";
    }

    public static String getFileName(Match m, Settings settings) {
        String sep = "_";
        Date d = new Date();
        PlayerData[] pd = m.getPlayerData();
        String playerDataString = playerDataToString(pd[0]) + sep + getPlayerType(settings.getPlayer1Settings()) + sep
                + playerDataToString(pd[1]) + sep + getPlayerType(settings.getPlayer2Settings());
        return d.toString().replaceAll(" ", "_") + "_" + playerDataString + ".save";

    }

    /**
     * Vérifie s'il existe des données à charger.
     * 
     * @return Vrai s'il existe des données et faux sinon.
     */
    public static boolean hasSaveFile() {
        return getSaveFiles().size() != 0;
    }

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
}
