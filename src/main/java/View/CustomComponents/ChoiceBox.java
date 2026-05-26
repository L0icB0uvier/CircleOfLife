package View.CustomComponents;

import View.Adapter.ChoiceButtonAdapter;
import View.CustomComponents.ImageButton;
import View.Utils.FontScaler;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ChoiceBox extends JComponent {

    private final String [] values;
    public JButton leftBtn, rightBtn;
    Box labelPanel;
    private final JLabel label;
    private int currentLabel = 0;

    public ChoiceBox(String... values) {
        this.values = values;

        MigLayout layout = new MigLayout("fill", "[15%][70%][15%]", "[align center, grow]");
        this.setLayout(layout);
        leftBtn = createButton("undoIcon.png");
        rightBtn = createButton("redoIcon.png");
        this.labelPanel = Box.createHorizontalBox();
        labelPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.label = createLabel(values[0]);
        labelPanel.add(Box.createGlue());
        labelPanel.add(label);
        labelPanel.add(Box.createGlue());
        leftBtn.addActionListener(new ChoiceButtonAdapter(this, -1));
        rightBtn.addActionListener(new ChoiceButtonAdapter(this, 1));

        this.add(leftBtn, "grow");
        this.add(labelPanel, "grow");
        this.add(rightBtn, "grow");
        leftBtn.addComponentListener(new FontScaler(leftBtn, rightBtn));
    }

    public String getValue() {
        return this.label.getText();
    }

    public JLabel getLabel() {
        return label;
    }

    public Box getLabelPanel() {
        return labelPanel;
    }

    public String getMaxText() {
        String text = "";
        for(String t: values) {
            if (t.length() > text.length()) text = t;
        }
        return text;
    }

    public void changeLabel(int direction) {
        this.currentLabel += direction + this.values.length;
        this.label.setText(this.values[this.currentLabel % this.values.length]);
    }

    private JButton createButton(String text) {
        JButton button = new ImageButton(text,false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
        //button.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        button.setContentAreaFilled(false);
        return button;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }
}
