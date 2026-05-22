package Patterns;

public interface Command {
    /**
     * Execute la commande associée.
     */
    void execute();

    /**
     * Annule la commande associée.
     */
    void desexecute();
}
