package View;

public interface EventCollector {
    /**
     * Gestion d'un clic dans la vue sur une case spécifique du plateau de jeu.
     * @param l La ligne du plateau sur laquelle le joueur a cliqué.
     * @param c La colonne du plateau sur laquelle le joueur a cliqué.
     */
    void handleClick(int l, int c);

    /**
     * Gestion d'une demande d'action reçu depuis la vue.
     * @param t Le nom de l'action à exécuter.
     */
    void performAction(String t);

    /**
     * Enregistre une interface utilisateur.
     * @param i L'interface utilisateur à enregistrer.
     */
    void addUserInterface(UserInterface i);
}
