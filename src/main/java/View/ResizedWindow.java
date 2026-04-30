package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Map;

public class ResizedWindow implements ComponentListener {

    Map<JComponent, Integer> sizes;
    JFrame frame;

    public ResizedWindow(Map<JComponent, Integer> sizes, JFrame frame) {
        this.sizes = sizes;
        this.frame = frame;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        for(JComponent component: sizes.keySet()) {
            Font font = component.getFont();
            component.setFont(font.deriveFont((float) Math.min(frame.getHeight(), frame.getWidth()) / sizes.get(component)));
            if(component instanceof JButton) {
                component.setSize(component.getFont().getSize() * ((JButton) component).getText().length(), component.getHeight());
            }
            if(component instanceof JComboBox) {
                component.setPreferredSize(new Dimension(component.getFont().getSize() * ((JComboBox<String>) component).getItemAt(0).length(), 40));
                component.setMaximumSize(new Dimension(component.getFont().getSize() * ((JComboBox<String>) component).getItemAt(0).length(), 40));
            }
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
