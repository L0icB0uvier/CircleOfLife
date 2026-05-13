package View.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class FontScaler extends ComponentAdapter {
    private float RATIO = 0.3f;
    private final JComponent[] jComponents;

    public FontScaler(JComponent... jComponents){
        this.jComponents = jComponents;
    }

    public FontScaler(float ratio, JComponent... jComponents){
        this.RATIO = ratio;
        this.jComponents = jComponents;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        float size = e.getComponent().getHeight() * RATIO;
        for (JComponent label : jComponents) {
            label.setFont(label.getFont().deriveFont(size));
        }
        e.getComponent().revalidate();
    }
}
