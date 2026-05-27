package View.Utils;

import Global.Configuration;
import View.CustomComponents.ChoiceBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class FontScaler extends ComponentAdapter {
    private float RATIO = 0.3f;
    private float WIDTH_PADDING = 0.8f;
    private final JComponent[] jComponents;

    public FontScaler(JComponent... jComponents){
        this.jComponents = jComponents;
    }

    public FontScaler(float ratio, JComponent... jComponents){
        this.RATIO = ratio;
        this.jComponents = jComponents;
    }

    public FontScaler(float ratio, float width_padding, JComponent... jComponents){
        this.RATIO = ratio;
        this.WIDTH_PADDING = width_padding;
        this.jComponents = jComponents;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Graphics g = e.getComponent().getGraphics();
        FontMetrics fontMetricsStart = g.getFontMetrics(jComponents[0].getFont());
        float maxSize = getMaxSize(e);

        String maxText = "";
        for(JComponent comp: jComponents) {
            String text = null;
            if (comp instanceof JLabel) {
                if(e.getComponent() instanceof ChoiceBox) {
                    text = ((ChoiceBox) e.getComponent()).getMaxText();
                    if (text.length() > maxText.length()) maxText = text;
                    continue;
                }
                text = ((JLabel) comp).getText();
                String[] lines = text.split("<br>");
                text = "";
                for (String line: lines) {
                    line = line.replace("<html>", "");
                    line = line.replace("</html>", "");
                    if(line.length() > text.length()) text = line;
                }
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
            if (text.length() > maxText.length()) maxText = text;
        }
        if(e.getComponent() instanceof  ChoiceBox) Configuration.info(maxText);
        Font maxFont = new Font(null, Font.PLAIN, 0);
        for(JComponent comp: jComponents) {
            comp.setFont(comp.getFont().deriveFont(maxSize));
            JComponent tempComp = new JPanel();
            tempComp.setFont(comp.getFont());
            FontMetrics fontMetrics = fontMetricsStart;
            int width = e.getComponent().getWidth();
            while (width * WIDTH_PADDING < fontMetrics.stringWidth(maxText)) {

                float current = tempComp.getFont().getSize2D();
                tempComp.setFont(comp.getFont().deriveFont(current * 0.9f));
                fontMetrics = g.getFontMetrics(tempComp.getFont());
            }
            if (tempComp.getFont().getSize2D() > maxFont.getSize2D()) maxFont = tempComp.getFont();
        }
        for(JComponent comp: jComponents) {
            comp.setFont(maxFont);
        }
        e.getComponent().revalidate();
    }

    private float getMaxSize(ComponentEvent e) {
        float size = e.getComponent().getHeight() * RATIO;
        float maxSize = 0.0f;
        for (JComponent comp : jComponents) {
            int lineNb = 1;
            if (comp instanceof JLabel) {
                lineNb = 0;
                String text = ((JLabel) comp).getText();
                String[] lines = text.split("<br>");
                for (String ignored : lines) {
                    lineNb++;
                }
            }
            if (size/lineNb > maxSize) maxSize = size/lineNb;
        }
        return maxSize;
    }
}
