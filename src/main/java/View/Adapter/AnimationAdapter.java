package View.Adapter;

import View.EventCollector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimationAdapter  implements ActionListener {
    EventCollector control;

    public AnimationAdapter(EventCollector control) {
        this.control = control;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        control.animTic();
    }
}
