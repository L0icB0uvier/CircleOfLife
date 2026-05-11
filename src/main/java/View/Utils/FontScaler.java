package View.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class FontScaler extends ComponentAdapter {
    private static final float RATIO = 0.3f;
    private final JLabel[] jLabels;

    public FontScaler(JLabel... jLabels){
        this.jLabels = jLabels;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        float size = e.getComponent().getHeight() * RATIO;
        for (JLabel label : jLabels) {
            label.setFont(label.getFont().deriveFont(size));
        }
        e.getComponent().revalidate();
    }
}
