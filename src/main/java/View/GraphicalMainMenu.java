package View;

import View.Utils.UIColor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GraphicalMainMenu extends JPanel {
    Box menuBox;
    JFrame parent;
    JButton newGameButton;
    JButton continueButton;
    Font titleFont;
    Font buttonFont;

    public GraphicalMainMenu(JFrame parent){
        super(new BorderLayout());
        menuBox = Box.createVerticalBox();
        this.parent = parent;
        Map<JComponent, Integer> sizes = new HashMap<>();

        MigLayout layout = new MigLayout("fill, insets 10 10 10 10", "[left][center][left][center][center][]","[10%][10%][grow][10%]" );
        this.setLayout(layout);

        titleFont = new Font("Arial", Font.BOLD, Math.min(parent.getHeight(), parent.getWidth())/10);
        menuBox.add(Box.createGlue());
        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Box titlePanel = Box.createHorizontalBox();
        JLabel part1Title = new JLabel("La ");
        part1Title.setFont(titleFont);
        sizes.put(part1Title, 10);
        titlePanel.add(part1Title);
        JLabel part2Title = new JLabel("Gauffre ");
        part2Title.setFont(titleFont);
        sizes.put(part2Title, 10);
        part2Title.setForeground(UIColor.BACKGROUND);
        titlePanel.add(part2Title);
        JLabel part3Title = new JLabel("Empoisonnée");
        part3Title.setFont(titleFont);
        sizes.put(part2Title, 10);
        part3Title.setForeground(UIColor.GREEN);
        titlePanel.add(part3Title);
        titlePanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, UIColor.BACKGROUND));
        titlePanel.setBorder(new CompoundBorder(titlePanel.getBorder(), BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        textPanel.add(titlePanel);


        menuBox.add(textPanel);
        menuBox.add(new Box.Filler(new Dimension(getWidth(), 30), new Dimension(getWidth(), 150), new Dimension(getWidth(), 150)));

        newGameButton = createButton("– Nouvelle partie");
        sizes.put(newGameButton, 25);
        menuBox.add(newGameButton);
        menuBox.add(new Box.Filler(new Dimension(getWidth(), 10), new Dimension(getWidth(), 40), new Dimension(getWidth(), 100)));

        continueButton = createButton("– Continuer");
        sizes.put(continueButton, 25);
        menuBox.add(continueButton);
        menuBox.add(Box.createGlue());

        buttonFont = new Font("Arial", Font.BOLD, Math.min(parent.getHeight(), parent.getWidth())/25);
        newGameButton.setFont(buttonFont);
        continueButton.setFont(buttonFont);

        this.add(menuBox);
        this.addComponentListener(new ResizedWindow(sizes, parent));

    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        button.setContentAreaFilled(false);
        return button;
    }
}
