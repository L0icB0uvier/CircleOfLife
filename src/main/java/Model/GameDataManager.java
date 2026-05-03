package Model;

import Global.Configuration;
import Global.Settings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class GameDataManager {
    static String savePath = "./save.dat";

    /**
     * Sauvegarde un match ainsi que ses paramètres.
     * @param match Le match à sauvegarder.
     * @param settings Les settings du match à sauvegarder.
     * @throws Exception
     */
    public static void saveMatch(Match match, Settings settings)  throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(savePath));

        writer.close();
    }

    /**
     * Charge les données d'un match et créé un mnouveau match avec ces données dans Game.
     * @param game L'instance de game dans laquelle charger le match.
     * @throws FileNotFoundException
     */
    public static void loadMatch(Game game) throws FileNotFoundException {
        File file = new File(savePath);
        Scanner scanner = new Scanner(file);

        scanner.close();
    }

    /**
     * Vérifie s'il existe des données à charger.
     * @return Vrai s'il existe des données et faux sinon.
     */
    public static boolean hasSaveFile(){
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
