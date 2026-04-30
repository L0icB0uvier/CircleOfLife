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

    public boolean canUndo() {
        return past.isEmpty() == false;
    }

    public boolean canRedo() {
        return future.isEmpty() == false;
    }

    public E transfer(Deque<E> source, Deque<E> destination) {
        var res = source.removeFirst();
        destination.addFirst(res);
        return res;
    }

    public E undo() {
        var cmd = transfer(past, future);
        cmd.desexecute();
        return cmd;
    }

    public E redo() {
        var cmd = transfer(future, past);
        cmd.execute();
        return cmd;
    }

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

    public Iterator<E> pastIterator(){
        return past.descendingIterator();
    }

    public Iterator<E> futurIterator(){
        return future.iterator();
    }
}
