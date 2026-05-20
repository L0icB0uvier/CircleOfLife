package View;

import Controller.IA.AILevel;
import Global.Configuration;
import Global.PathValidator;
import Model.Game;
import Patterns.Observer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class AnalyseIAView implements Observer, UserInterface {

    Game game;
    EventCollector control;

    private AILevel aiLevel1, aiLevel2;
    private int numberOfGames;
    private int gamePlayedCounts;
    private String analyseFileDir = "analyses/";
    private Path outputPath;
    private winStat[] winCount = new winStat[2];

    public AnalyseIAView(Game game, EventCollector control, String[] args){
        this.game = game;
        this.control = control;

        if (parseArgs(args) == false) {
            System.err.println("Erreur : Mauvais arguments.");
            showUsage();
            System.exit(1); // Arrête le programme avec un code d'erreur
        }

        winCount[0] = new winStat();
        winCount[1] = new winStat();

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
        System.out.println("Usage : java AnaylyseIA <Difficulté IA 1> <Difficulté IA 2> <Nombre de parties> <Chemin fichier de sortie>");
        System.out.println();
        System.out.println("Arguments attendus :");
        System.out.println("  <Difficulté IA 1>             : La difficulté de la première IA. Choisir entre 'F' 'M' et 'D' pour \"Facile\", \"Moyen\" et \"Difficile\"");
        System.out.println("  <Difficulté IA 1>             : La difficulté de la deuxième IA. Choisir entre 'F' 'M' et 'D' pour \"Facile\", \"Moyen\" et \"Difficile\"");
        System.out.println("  <Nombre de parties>           : Le nombre de parties que doivent jouer les IA. Maximum 1000 parties.");
        System.out.println("  <Chemin fichier de sortie>    : Le chemin du fichier d'analyse des parties");
        System.out.println();
        System.out.println("Exemple valide : java GestionArguments document.txt 5");
        System.out.println("=====================");
    }

    @Override
    public void update() {
        if(game.isGameOver()){
            System.out.println("Partie terminée");
            extractGameData();
            gamePlayedCounts++;
            if(gamePlayedCounts < numberOfGames)
                startNewGame();

            else{
                writeGameStats();
                System.out.println("Fin de l'analyse. Fermeture du programme.");
                System.exit(0);
            }
        }
    }

    private void extractGameData(){
        System.out.println("Extraction des données.");
        winCount[game.getWinningPlayer()].winCount++;
        if(game.getMatch().winByScore())
            winCount[game.getWinningPlayer()].winByScoreCount++;
    }

    private void writeGameStats() {
        System.out.println("Écriture des statistiques...");

        // 1. ÉCRITURE DANS LE FICHIER
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputPath.toFile()))) {
            printTableToDestination(fileWriter);
            fileWriter.flush();
            System.out.println(String.format("Statistiques des matchs écrites dans le fichier %s\n", outputPath.getFileName()));
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

        // 3. FERMETURE PROPRE
        System.out.println("\nFin de l'analyse. Fermeture du programme.");
        System.exit(0);
    }

    private void printTableToDestination(java.io.Writer writer) throws IOException {
        String formatLigneStr = "%-30s\t%-18s\t%-18s\n";
        String formatLigneInt = "%-30s\t%-18d\t%-18d\n";

        int remplissage0 = winCount[0].winCount - winCount[0].winByScoreCount;
        int remplissage1 = winCount[1].winCount - winCount[1].winByScoreCount;

        // On utilise writer.write(String.format(...)) pour tout le monde
        writer.write(String.format(formatLigneStr, "Métrique", "IA Joueur 1", "IA Joueur 2"));
        writer.write(String.format(formatLigneStr, "Difficulté IA", convertAILevelToText(aiLevel1), convertAILevelToText(aiLevel2)));
        writer.write(String.format(formatLigneInt, "Nb victoires", winCount[0].winCount, winCount[1].winCount));
        writer.write(String.format(formatLigneInt, "Victoires par score", winCount[0].winByScoreCount, winCount[1].winByScoreCount));
        writer.write(String.format(formatLigneInt, "Victoires par remplissage", remplissage0, remplissage1));
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
        Configuration.setPlayer1Settings(aiLevel1, "AI1");
        Configuration.setPlayer2Settings(aiLevel2, "AI2");
    }

    public class winStat{
        public int winCount;
        public int winByScoreCount;
    }
}
