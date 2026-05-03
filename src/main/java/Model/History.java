package Model;

import Patterns.Command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class History<E extends Command> {
    Deque<E> past, future;

    public History() {
        reset();
    }

    void reset() {
        past = new ArrayDeque<>();
        future = new ArrayDeque<>();
    }

    /**
     * Vérifie s'il est possible d'annuler une action.
     * @return true s'il existe des actions annulables, false sinon.
     */
    public boolean canUndo() {
        return past.isEmpty() == false;
    }

    /**
     * Vérifie s'il est possible de refaire une action annulée.
     * @return true s'il existe des actions refaisable, false sinon.
     */
    public boolean canRedo() {
        return future.isEmpty() == false;
    }

    /**
     * Transfert une action de la queue source vers la queue destination.
     * @param source Queue source
     * @param destination Queue destination
     * @return L'action transférée.
     */
    public E transfer(Deque<E> source, Deque<E> destination) {
        var res = source.removeFirst();
        destination.addFirst(res);
        return res;
    }

    /**
     * Annule la dernière commande.
     * @return La commande annulé.
     */
    public E undo() {
        var cmd = transfer(past, future);
        cmd.desexecute();
        return cmd;
    }

    /**
     * Refait la dernière commande annulée.
     * @return La commande refaite.
     */
    public E redo() {
        var cmd = transfer(future, past);
        cmd.execute();
        return cmd;
    }

    /**
     * Applique une nouvelle commande. Supprime les redo s'il y en a.
     * @param newMove Le Move à appliquer.
     */
    public void apply(E newMove) {
        newMove.execute();
        past.addFirst(newMove);
        while (!future.isEmpty()) {
            future.removeFirst();
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        History resultat = (History) super.clone();
        resultat.reset();
        return resultat;
    }

    /**
     * Récupère un itérateur des actions passées en ordre descendant.
     * @return Un itérateur des actions passées en ordre descendant.
     */
    public Iterator<E> pastIterator(){
        return past.descendingIterator();
    }

    /**
     * Récupère un itérateur des actions annulées.
     * @return Un itérateur des actions annulées.
     */
    public Iterator<E> futurIterator(){
        return future.iterator();
    }
}
