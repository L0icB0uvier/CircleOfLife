package Global;

import Controller.IA.AILevel;
import View.Settings;

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

    public static String readString(String nom) {
        return instance().read(nom);
    }

    public static int readInt(String nom) {
        return Integer.parseInt(instance().read(nom));
    }

    public static double readDouble(String nom) {
        return Double.parseDouble(instance().read(nom));
    }

    public static boolean readBool(String nom) {
        return Boolean.parseBoolean(instance().read(nom));
    }

    public static void loadProperties(Properties p, InputStream in, String nom) {
        try {
            p.load(in);
        } catch (IOException e) {
            // Le logger n'est pas encore en place à ce moment là
            System.err.println("Impossible de charger " + nom);
            System.err.println(e.toString());
            System.exit(1);
        }
    }

    public String read(String nom) {
        String value = prop.getProperty(nom);
        if (value != null) {
            return value;
        } else {
            throw new NoSuchElementException("Propriété " + nom + " manquante");
        }
    }

    public Logger logger() {
        if (logger == null) {
            System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s : %5$s%n");
            logger = Logger.getLogger("Sokoban.Logger");
            logger.setLevel(Level.parse(read("LogLevel")));
        }
        return logger;
    }

    public static void info(String s) {
        instance().logger().info(s);
    }

    public static void warning(String s) {
        instance().logger().warning(s);
    }

    public static void error(String s) {
        instance().logger().severe(s);
        System.exit(1);
    }

    public static void initSettings(){
        instance().matchSettings = new Settings();
    }

    public static Settings getSettings(){
        return instance().matchSettings;
    }

    public static void setPlayer1Settings(AILevel aiLevel){
        instance().matchSettings.setPlayer1Settings(aiLevel);
    }

    public static void setPlayer2Settings(AILevel aiLevel){
        instance().matchSettings.setPlayer2Settings(aiLevel);
    }
}
