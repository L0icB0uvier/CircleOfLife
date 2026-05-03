package Global;

import Controller.IA.AILevel;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuration {

    private static Configuration instance = null;
    Properties prop;
    Settings matchSettings;
    Logger logger;

    /**
     * Permet de récupérer l'instance du Singleton de la classe Configuration.
     * @return Instance unique de Configuration
     */
    public static Configuration instance() {
        if (instance == null)
            instance = new Configuration();
        return instance;
    }

    protected Configuration(){
        InputStream in = open("defaut.cfg");
        Properties defaut = new Properties();
        loadProperties(defaut, in, "defaut.cfg");

        String message = "Fichier de propriétés defaut.cfg chargé";
        String name = System.getProperty("user.home") + File.separator + ".sokoban";
        try {
            in = new FileInputStream(name);
            prop = new Properties(defaut);
            loadProperties(prop, in, name);
            logger().info(message);
            logger().info("Fichier de propriétés " + name + " chargé");
        } catch (FileNotFoundException e) {
            prop = defaut;
            logger().info(message);
        }
    }

    public static InputStream open(String s) {
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(s);
        if (in == null) {
            System.err.println("Impossible de charger la ressource " + s);
            System.exit(1);
        }
        return in;
    }

    /**
     * Charge les propriétées depuis un fichier cfg
     * @param p Variable dans laquelle charger les propriétées.
     * @param in InputStream à partir duquel charger les propriétées.
     * @param fileName Nom du fichier cfg que l'on cherche à charger.
     */
    public static void loadProperties(Properties p, InputStream in, String fileName) {
        try {
            p.load(in);
        } catch (IOException e) {
            // Le logger n'est pas encore en place à ce moment là
            System.err.println("Impossible de charger " + fileName);
            System.err.println(e.toString());
            System.exit(1);
        }
    }


    /**
     * Lit un string dans les Properties.
     * @param propertyName Nom de la propriété à lire dans Properties
     * @return La chaîne de caractère correspondant à la propriété.
     */
    public static String readString(String propertyName) {
        return instance().read(propertyName);
    }

    /**
     * Lit un Int dans les Properties.
     * @param propertyName Nom de la propriété à lire dans Properties
     * @return La valeur entière correspondant à la propriété.
     */
    public static int readInt(String propertyName) {
        return Integer.parseInt(instance().read(propertyName));
    }

    /**
     * Lit un Double dans les Properties.
     * @param propertyName Nom de la propriété à lire dans Properties
     * @return La valeur décimale correspondant à la propriété.
     */
    public static double readDouble(String propertyName) {
        return Double.parseDouble(instance().read(propertyName));
    }

    /**
     * Lit un booléen dans les Properties.
     * @param propertyName Nom de la propriété à lire dans Properties
     * @return La valeur booléenne correspondant à la propriété.
     */
    public static boolean readBool(String propertyName) {
        return Boolean.parseBoolean(instance().read(propertyName));
    }

    /**
     * Lit la valeur d'une propriété.
     * @param propertyName Le nom de la propriété à lire.
     * @return Retourne la valeur de la propriété sous forme de chaîne de caractère.
     */
    private String read(String propertyName) {
        String value = prop.getProperty(propertyName);
        if (value != null) {
            return value;
        } else {
            throw new NoSuchElementException("Propriété " + propertyName + " manquante");
        }
    }

    /**
     * Crée un logger s'il n'existe pas et retourne une instance.
     * @return Une instance de Logger prête à être utilisée.
     */
    public Logger logger() {
        if (logger == null) {
            System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s : %5$s%n");
            logger = Logger.getLogger("Sokoban.Logger");
            logger.setLevel(Level.parse(read("LogLevel")));
        }
        return logger;
    }

    /**
     * Log une info dans la console en utilisant un Logger.
      * @param s Message à afficher.
     */
    public static void info(String s) {
        instance().logger().info(s);
    }

    /**
     * Log un warning dans la console en utilisant un Logger.
     * @param s Message à afficher.
     */
    public static void warning(String s) {
        instance().logger().warning(s);
    }

    /**
     * Log une erreur dans la console en utilisant un Logger.
     * @param s Message à afficher.
     */
    public static void error(String s) {
        instance().logger().severe(s);
        System.exit(1);
    }

    /**
     * Crée une nouvelle instance des Settings avec des valeurs par défault.
     */
    public static void initSettings(){
        instance().matchSettings = new Settings();
    }

    /**
     * Retourne une référence aux Settings.
     * @return Référence aux Settings.
     */
    public static Settings getSettings(){
        return instance().matchSettings;
    }

    /**
     * Met à jour les settings relatif au premier joueur.
     * @param aiLevel Le niveau de difficulté de l'IA. Si null, le joueur est considéré comme humain.
     */
    public static void setPlayer1Settings(AILevel aiLevel){
        instance().matchSettings.setPlayer1Settings(aiLevel);
    }

    /**
     * Met à jour les settings relatif au deuxième joueur.
     * @param aiLevel Le niveau de difficulté de l'IA. Si null, le joueur est considéré comme humain.
     */
    public static void setPlayer2Settings(AILevel aiLevel){
        instance().matchSettings.setPlayer2Settings(aiLevel);
    }
}
