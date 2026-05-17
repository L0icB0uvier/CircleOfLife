package View.Utils;

import View.Adapter.ChoiceButtonAdapter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ChoiceBox extends JComponent {

    private final String [] values;
    public JButton leftBtn, rightBtn;
    private final JLabel label;
    private int currentLabel = 0;

    public ChoiceBox(String... values) {
        this.values = values;

        MigLayout layout = new MigLayout("fill", "[10%, align right][40%, align center][10%, align left]push", "[]");
        this.setLayout(layout);
        leftBtn = createButton("<");
        rightBtn = createButton(">");
        this.label = createLabel(values[0]);
        leftBtn.addActionListener(new ChoiceButtonAdapter(this, -1));
        rightBtn.addActionListener(new ChoiceButtonAdapter(this, 1));

        leftBtn.setMinimumSize(new Dimension(0, 0));
        label.setMinimumSize(new Dimension(0, 0));
        rightBtn.setMinimumSize(new Dimension(0, 0));

        this.add(leftBtn);
        this.add(label);
        this.add(rightBtn);
        this.addComponentListener(new FontScaler(leftBtn, label, rightBtn));
    }

    public String getValue() {
        return this.label.getText();
    }

    public void changeLabel(int direction) {
        this.currentLabel += direction + this.values.length;
        this.label.setText(this.values[this.currentLabel % this.values.length]);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        button.setContentAreaFilled(false);
        return button;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        return label;
    }
}
