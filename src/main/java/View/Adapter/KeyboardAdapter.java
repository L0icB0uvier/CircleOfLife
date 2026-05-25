package View.Adapter;

import Global.Configuration;
import View.EventCollector;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardAdapter extends KeyAdapter {
    EventCollector control;

    public KeyboardAdapter(EventCollector control){
        this.control = control;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Configuration.info("Keyboard pressed");
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT -> control.performAction("Undo");
            case KeyEvent.VK_RIGHT -> control.performAction("Redo");
            case KeyEvent.VK_S -> control.performAction("Save");
            case KeyEvent.VK_O -> control.performAction("UndoAll");
            case KeyEvent.VK_P -> control.performAction("RedoAll");
        }
    }
}
