package View.Utils;

import View.CustomComponents.ChoiceBox;

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
        Font prevFont = jComponents[0].getFont();
        float size = e.getComponent().getHeight() * RATIO;
        for (JComponent comp : jComponents) {
            comp.setFont(comp.getFont().deriveFont(size));
        }

        Graphics g = e.getComponent().getGraphics();
        FontMetrics fontMetrics = g.getFontMetrics(jComponents[0].getFont());
        for(JComponent comp: jComponents) {
            int width = e.getComponent().getWidth();
            String text = null;
            if (comp instanceof JLabel) {
                text = ((JLabel) comp).getText();
            } else if (comp instanceof JButton) {
                text = ((JButton) comp).getText();
            } else if (comp instanceof JTextField) {
                text = ((JTextField) comp).getText();
            } else if (comp instanceof ChoiceBox) {
                text = ((ChoiceBox) comp).getMaxText();
            }
            if (text == null) {
                throw new ClassCastException("Classe incompatible avec fontScaler : " + comp.getClass().getName());
            }
            while (width *0.8 < fontMetrics.stringWidth(text)) {

                float current = comp.getFont().getSize2D();
                comp.setFont(comp.getFont().deriveFont(current * 0.9f));
                fontMetrics = g.getFontMetrics(comp.getFont());
            }
        }
        e.getComponent().revalidate();
    }
}
