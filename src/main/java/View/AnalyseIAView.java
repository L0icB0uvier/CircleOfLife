package View;

import Controller.IA.AILevel;
import Global.Configuration;
import Global.PathValidator;
import Global.PlayerNumber;
import Model.Game;
import Patterns.Observer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

public class AnalyseIAView implements Observer, UserInterface {

    Game game;
    EventCollector control;

    private AILevel aiLevel1, aiLevel2;
    private int numberOfGames;
    private int gamePlayedCounts;
    private final String analyseFileDir = "analyses/";
    private Path outputPath;
    private final gameStats[] gameStats = new gameStats[2];
    private boolean sampleTime = false;
    private int currentPlayer;
    Instant toolStartTime;
    double analyseTime = 0;

    long startTime;

    public AnalyseIAView(Game game, EventCollector control, String[] args){
        this.game = game;
        this.control = control;

        if (parseArgs(args) == false) {
            System.err.println("Erreur : Mauvais arguments.");
            showUsage();
            System.exit(1); // Arrête le programme avec un code d'erreur
        }

        toolStartTime = Instant.now();

        gameStats[0] = new gameStats();
        gameStats[1] = new gameStats();

        Configuration.config("Arguments parsés avec succès.");
        //Configuration.setLoggerLevel(Level.WARNING);
        Configuration.initSettings();

        game.addObserver(this);
        control.addUserInterface(this);

        startNewGame();
    }

    private void startNewGame(){
        System.out.println("Début d'une nouvelle partie.");
        control.performAction("NewGame");
        if(sampleTime){
            startTime = System.nanoTime();
            currentPlayer = game.getCurrentPlayerIndex();
        }
    }

    private boolean parseArgs(String[] args){
        if (args.length < 4) return false;

        aiLevel1 = parseAIDifficulty(args[0]);
        if(aiLevel1 == null) return false;

        aiLevel2 = parseAIDifficulty(args[1]);
        if(aiLevel2 == null) return false;

        if(parseGameCount(args[2]) == false) return false;

        try {
            outputPath = PathValidator.validateAndPrepareOutputPath(analyseFileDir + args[3]);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        }

        if(args.length > 4){
            for (int i = 4; i < args.length; i++) {
                switch (args[i]) {
                    case "-t":
                        sampleTime = true;
                        break;
                    default:
                        System.err.println("Erreur : Option inconnue : " + args[i]);
                        return false;
                }
            }
        }

        return true;
    }

    private AILevel parseAIDifficulty(String AIDifficultyArg){
        return switch (AIDifficultyArg.toUpperCase()) {
            case "F" -> AILevel.EASY;
            case "M" -> AILevel.MEDIUM;
            case "D" -> AILevel.HARD;
            default -> null;
        };
    }

    private boolean parseGameCount(String gameCountArg){
        if(isNumeric(gameCountArg) == false) return false;
        numberOfGames = Integer.parseInt(gameCountArg);
        return numberOfGames > 1 && numberOfGames <= 1000;
    }

    public boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Centralise et affiche le format attendu par le programme.
     */
    private static void showUsage() {
        System.out.println();
        System.out.println("=== MODE D'EMPLOI ===");
        System.out.println("Usage : java AnaylyseIA <Difficulté IA 1> <Difficulté IA 2> <Nombre de parties> <Chemin fichier de sortie> <options>");
        System.out.println();
        System.out.println("Arguments attendus :");
        System.out.println("  <Difficulté IA 1>             : La difficulté de la première IA. Choisir entre 'F' 'M' et 'D' pour \"Facile\", \"Moyen\" et \"Difficile\"");
        System.out.println("  <Difficulté IA 1>             : La difficulté de la deuxième IA. Choisir entre 'F' 'M' et 'D' pour \"Facile\", \"Moyen\" et \"Difficile\"");
        System.out.println("  <Nombre de parties>           : Le nombre de parties que doivent jouer les IA. Maximum 1000 parties.");
        System.out.println("  <Chemin fichier de sortie>    : Le chemin du fichier d'analyse des parties");
        System.out.println("  <options>                     : -t pour avoir des statistiques sur le temps moyen d'un tour pour chaque IA");
        System.out.println();
        System.out.println("=====================");
    }

    @Override
    public void update() {
        if(game.isGameOver()){
            System.out.println("Partie terminée");
            extractGameData();
            gamePlayedCounts++;
            System.out.printf("Nombre de parties jouées: %d%n", gamePlayedCounts);
            if(gamePlayedCounts < numberOfGames)
                startNewGame();

            else{
                Instant end = Instant.now();
                Duration duration = Duration.between(toolStartTime, end);
                analyseTime = duration.toMillis() / 1000.0;
                writeGameStats();
                System.exit(0);
            }
        }
        else if(sampleTime){
            long endTime = System.nanoTime();
            long durationNano = endTime - startTime;
            gameStats[currentPlayer].gameDuration += durationNano;
            gameStats[currentPlayer].turnPlayed++;
            startTime = endTime;
            currentPlayer = game.getCurrentPlayerIndex();
        }
    }

    private void extractGameData(){
        System.out.println("Extraction des données.");
        gameStats[game.getWinningPlayer()].winCount++;
        if(game.getMatch().winByScore()){
            gameStats[game.getWinningPlayer()].winByScoreCount++;
            gameStats[game.getWinningPlayer()].numberOfMoveWinByScore += game.getNumberOfMovePlayed();
        }
        else{
            gameStats[game.getWinningPlayer()].numberOfMoveWinByFill += game.getNumberOfMovePlayed();
        }
    }

    private void writeGameStats() {
        System.out.println("Écriture des statistiques...");

        // 1. ÉCRITURE DANS LE FICHIER
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputPath.toFile()))) {
            printTableToDestination(fileWriter);
            fileWriter.flush();
            System.out.printf("Statistiques des matchs écrites dans le fichier %s\n%n", outputPath.getFileName());
        } catch (IOException e) {
            System.err.println("Erreur critique lors de l'écriture du fichier : " + e.getMessage());
        }

        // 2. AFFICHAGE DANS LA CONSOLE
        System.out.println("========================== RÉSULTATS DE L'ANALYSE ==========================");

        try (java.io.PrintWriter consoleWriter = new java.io.PrintWriter(System.out)) {
            printTableToDestination(consoleWriter);
            consoleWriter.flush(); // Très important pour forcer l'affichage dans la console
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("============================================================================");
    }

    private void printTableToDestination(java.io.Writer writer) throws IOException {
        String formatLigneStr = "%-30s\t%-18s\t%-18s\n";
        String formatLigneInt = "%-30s\t%-18d\t%-18d\n";

        int remplissage0 = gameStats[0].winCount - gameStats[0].winByScoreCount;
        int remplissage1 = gameStats[1].winCount - gameStats[1].winByScoreCount;

        // On utilise writer.write(String.format(...)) pour tout le monde
        writer.write(String.format(formatLigneStr, "Métrique", "IA Joueur 1", "IA Joueur 2"));
        writer.write(String.format(formatLigneStr, "Difficulté IA", convertAILevelToText(aiLevel1), convertAILevelToText(aiLevel2)));
        writer.write(String.format(formatLigneStr, "Nb victoires",
                String.format("%d (%d%%)", gameStats[0].winCount, (gameStats[0].winCount * 100) / numberOfGames),
                String.format("%d (%d%%)", gameStats[1].winCount, (gameStats[1].winCount * 100) / numberOfGames)));
        writer.write(String.format(formatLigneStr, "Victoires par score",
                String.format("%d (%d%%)", gameStats[0].winByScoreCount, gameStats[0].winCount > 0? (gameStats[0].winByScoreCount * 100) / gameStats[0].winCount : 0),
                String.format("%d (%d%%)", gameStats[1].winByScoreCount, gameStats[1].winCount > 0? (gameStats[1].winByScoreCount * 100) / gameStats[1].winCount : 0)));
        writer.write(String.format(formatLigneInt, "   Nombre moyen de coups",
                gameStats[0].winByScoreCount > 0? gameStats[0].numberOfMoveWinByScore / gameStats[0].winByScoreCount : 0,
                gameStats[1].winByScoreCount > 0? gameStats[1].numberOfMoveWinByScore / gameStats[1].winByScoreCount : 0));
        writer.write(String.format(formatLigneStr, "Victoires par remplissage",
                String.format("%d (%d%%)", remplissage0, gameStats[0].winCount > 0? (remplissage0 * 100) / gameStats[0].winCount : 0),
                String.format("%d (%d%%)", remplissage1, gameStats[1].winCount > 0? (remplissage1 * 100) / gameStats[1].winCount : 0)));
        writer.write(String.format(formatLigneInt, "   Nombre moyen de coups",
                remplissage0 > 0? gameStats[0].numberOfMoveWinByFill / remplissage0 : 0,
                remplissage1 > 0? gameStats[1].numberOfMoveWinByFill / remplissage1 : 0));
        if(sampleTime){
            writer.write(String.format(formatLigneStr, "Temps moyen",
                    String.format("%f ms", (float) (gameStats[0].gameDuration / gameStats[0].turnPlayed) / 1_000_000),
                    String.format("%f ms", (float) (gameStats[1].gameDuration / gameStats[1].turnPlayed) / 1_000_000)));
        }
        writer.write("\n");
        writer.write(String.format("Analyse effectuée en %.3f secondes.", analyseTime));
    }

    private String convertAILevelToText(AILevel aiLevel){
        switch (aiLevel){
            case EASY: return "Facile";
            case MEDIUM: return "Moyenne";
            case HARD: return "Difficile";
            default: return "Non reconnu";
        }
    }

    @Override
    public void toggleFullscreen() {

    }

    @Override
    public void updateSettings() {
        Configuration.setPlayerSettings(PlayerNumber.PLAYER_1, aiLevel1, "AI1");
        Configuration.setPlayerSettings(PlayerNumber.PLAYER_2, aiLevel2, "AI2");
    }

    public class gameStats {
        public int turnPlayed;
        public int winCount;
        public int winByScoreCount;
        public int numberOfMoveWinByScore;
        public int numberOfMoveWinByFill;
        public long gameDuration;
    }
}
