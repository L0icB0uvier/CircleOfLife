package Global;

import java.util.logging.*;

public class ColorFormatter extends Formatter {
    // Codes ANSI pour les couleurs
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    @Override
    public String format(LogRecord record) {
        String color;
        int val = record.getLevel().intValue();

        // On compare les valeurs numériques pour être certain de matcher
        if (val >= Level.SEVERE.intValue()) {
            color = RED;
        } else if (val >= Level.WARNING.intValue()) {
            color = YELLOW;
        } else if (val >= Level.INFO.intValue()) {
            color = CYAN;
        } else {
            color = WHITE;
        }

        // Utilisation de formatMessage pour supporter les paramètres (ex : %s)
        return color + "[" + record.getLevel() + "] "
                + formatMessage(record) + RESET + "\n";
    }
}