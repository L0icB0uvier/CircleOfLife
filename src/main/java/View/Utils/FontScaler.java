package View.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class FontScaler extends ComponentAdapter {
    private static final float ratio = 0.3f;

    public FontScaler(){
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Component c = e.getComponent();
        float size = c.getParent().getHeight() * ratio;
        c.setFont(c.getFont().deriveFont(size));
    }
}
