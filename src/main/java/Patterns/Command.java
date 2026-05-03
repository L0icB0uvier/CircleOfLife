package Patterns;

public interface Command {
    /**
     * Execute la commande associée.
     */
    public void execute();

    /**
     * Annule la commande associée.
     */
    public void desexecute();
}
