package View;

import View.Adapter.ToggleButtonAdapter;
import View.Utils.UIColor;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GraphicalNewGame extends JPanel {

    class MyUI extends javax.swing.plaf.metal.MetalToggleButtonUI
    {
        Color selected;
        public MyUI(Color selected) {
            super();
            this.selected = selected;
        }

        public Color getSelectColor(){
            return selected;
        }
    }

    public class CustonToggleButton extends JToggleButton {
        String text;
        Color selected, notSelected;
        public CustonToggleButton(String text, Color selected, Color notSelected) {
            super(text);
            this.text = text;
            this.selected = selected;
            this.notSelected = notSelected;
            setUI(new MyUI(selected));
        }

        public void paintComponent(Graphics g) {
            if(!isSelected()) {
                setBackground(notSelected);
            }
            super.paintComponent(g);
        }
    }
    Box newGameBox;
    JFrame parent;
    JToggleButton player1Button, AI1Button, player2Button, AI2Button;
    ButtonGroup player1Group, player2Group;
    JButton cancelButton, startButton;
    JComboBox<String> AI1ComboBox, AI2ComboBox;
    final int SIZE_TEXT = 35;
    final int SIZE_TITLE = 23;

    public GraphicalNewGame(JFrame parent){
        super(new BorderLayout());
        newGameBox = Box.createVerticalBox();
        this.parent = parent;
        Map<JComponent, Integer> sizes = new HashMap<>();

        newGameBox.add(Box.createGlue());
        JLabel titleLabel = createLabel("Choix de la grille");
        sizes.put(titleLabel, SIZE_TITLE);
        newGameBox.add(titleLabel);
        newGameBox.add(new Box.Filler(new Dimension(getWidth(), 30), new Dimension(getWidth(), 40), new Dimension(getWidth(), 75)));

        Box sliderLabelsBox = Box.createHorizontalBox();
        sliderLabelsBox.add(Box.createGlue());
        JLabel columnsLabel = createLabel("Nombre de colonnes");
        sizes.put(columnsLabel, SIZE_TEXT);
        sliderLabelsBox.add(columnsLabel);
        sliderLabelsBox.add(Box.createGlue());

        JLabel linesLabel = createLabel("Nombre de lignes");
        sizes.put(linesLabel, SIZE_TEXT);
        sliderLabelsBox.add(linesLabel);
        sliderLabelsBox.add(Box.createGlue());
        newGameBox.add(sliderLabelsBox);

        JLabel playersLabel = createLabel("Choix joueurs");
        sizes.put(playersLabel, SIZE_TEXT);
        newGameBox.add(playersLabel);

        Box player1Box = Box.createHorizontalBox();
        player1Box.add(Box.createGlue());

        JLabel player1Label = createLabel("Joueur 1 :");
        player1Label.setForeground(UIColor.getColor(UIColor.RED));
        sizes.put(player1Label, SIZE_TEXT);
        player1Box.add(player1Label);
        player1Box.add(new Box.Filler(new Dimension(50, getHeight()), new Dimension(100, getHeight()), new Dimension(150, getHeight())));
        player1Button = createToggleButton("J1", UIColor.getColor(UIColor.RED), UIColor.getColor(UIColor.WHITE));
        player1Button.setBackground(UIColor.getColor(UIColor.RED));
        player1Button.setForeground(Color.BLACK);
        player1Button.setSelected(true);
        sizes.put(player1Button, SIZE_TEXT);
        player1Box.add(player1Button);
        AI1Button = createToggleButton("IA", UIColor.getColor(UIColor.RED), UIColor.getColor(UIColor.WHITE));
        AI1Button.setBackground(UIColor.getColor(UIColor.RED));
        AI1Button.setForeground(Color.DARK_GRAY);

        player1Group = new ButtonGroup();
        player1Group.add(player1Button);
        player1Group.add(AI1Button);
        sizes.put(AI1Button, SIZE_TEXT);
        player1Box.add(AI1Button);

        player1Box.add(new Box.Filler(new Dimension(50, getHeight()), new Dimension(100, getHeight()), new Dimension(150, getHeight())));

        JPanel card1 = new JPanel(new GridLayout(1, 1));

        AI1ComboBox = createComboBox(new String[]{"Facile", "Moyen", "Difficile"});
        AI1ComboBox.setBackground(UIColor.getColor(UIColor.RED));
        AI1ComboBox.setBorder(BorderFactory.createLineBorder(UIColor.getColor(UIColor.RED)));
        sizes.put(AI1ComboBox, SIZE_TEXT);

        card1.add(AI1ComboBox);
        card1.setMaximumSize((new Dimension(150, 40)));
        AI1ComboBox.setVisible(false);

        player1Box.add(card1);
        player1Button.addActionListener(new ToggleButtonAdapter(player1Group, AI1Button.getModel(), AI1ComboBox, player1Box));
        AI1Button.addActionListener(new ToggleButtonAdapter(player1Group, AI1Button.getModel(), AI1ComboBox, player1Box));
        player1Group.setSelected(player1Button.getModel(), true);
        player1Box.add(Box.createGlue());
        newGameBox.add(player1Box);

        Box player2Box = Box.createHorizontalBox();

        player2Box.add(Box.createGlue());

        JLabel player2Label = createLabel("Joueur 2 :");
        player2Label.setForeground(UIColor.getColor(UIColor.ALT_BLUE));
        sizes.put(player2Label, SIZE_TEXT);
        player2Box.add(player2Label);
        player2Box.add(new Box.Filler(new Dimension(50, getHeight()), new Dimension(100, getHeight()), new Dimension(150, getHeight())));

        player2Group = new ButtonGroup();
        player2Button = createToggleButton("J2", UIColor.getColor(UIColor.ALT_BLUE), UIColor.getColor(UIColor.WHITE));
        player2Button.setBackground(UIColor.getColor(UIColor.ALT_BLUE));
        player2Button.setForeground(Color.BLACK);
        player2Button.setSelected(true);
        sizes.put(player2Button, SIZE_TEXT);
        player2Box.add(player2Button);
        AI2Button = createToggleButton("IA", UIColor.getColor(UIColor.ALT_BLUE), UIColor.getColor(UIColor.WHITE));
        AI2Button.setBackground(UIColor.getColor(UIColor.ALT_BLUE));
        AI2Button.setForeground(Color.BLACK);
        sizes.put(AI2Button, SIZE_TEXT);

        player2Group.add(player2Button);
        player2Group.add(AI2Button);

        player2Box.add(AI2Button);
        player2Box.add(new Box.Filler(new Dimension(50, getHeight()), new Dimension(100, getHeight()), new Dimension(150, getHeight())));

        JPanel card2 = new JPanel(new GridLayout(1, 1));

        AI2ComboBox = createComboBox(new String[]{"Facile", "Moyen", "Difficile"});
        AI2ComboBox.setBackground(UIColor.getColor(UIColor.ALT_BLUE));
        AI2ComboBox.setBorder(BorderFactory.createLineBorder(UIColor.getColor(UIColor.ALT_BLUE)));

        sizes.put(AI2ComboBox, SIZE_TEXT);
        card2.add(AI2ComboBox);
        card2.setMaximumSize((new Dimension(150, 40)));
        AI2ComboBox.setVisible(false);

        player2Button.addActionListener(new ToggleButtonAdapter(player2Group, AI2Button.getModel(), AI2ComboBox, player2Box));
        AI2Button.addActionListener(new ToggleButtonAdapter(player2Group, AI2Button.getModel(), AI2ComboBox, player2Box));
        player2Group.setSelected(player2Button.getModel(), true);

        player2Box.add(card2);
        player2Box.add(Box.createGlue());
        newGameBox.add(player2Box);

        Box buttonsBox = Box.createHorizontalBox();

        cancelButton = createBorderedButton("Annuler");
        cancelButton.setBackground(UIColor.getColor(UIColor.RED));
        sizes.put(cancelButton, SIZE_TEXT);
        buttonsBox.add(cancelButton);
        buttonsBox.add(new Box.Filler(new Dimension(50, getHeight()), new Dimension(100, getHeight()), new Dimension(150, getHeight())));

        startButton = createBorderedButton("Démarrer");
        startButton.setBackground(UIColor.getColor(UIColor.WAFFLE));
        sizes.put(startButton, SIZE_TEXT);
        buttonsBox.add(startButton);
        newGameBox.add(buttonsBox);
        newGameBox.add(Box.createGlue());

        this.add(newGameBox);
        this.addComponentListener(new ResizedWindow(sizes, parent));
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JToggleButton createToggleButton(String text, Color selected, Color notSelected) {
        JToggleButton button = new CustonToggleButton(text, selected, notSelected);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
        return button;
    }

    private JButton createBorderedButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusable(false);
        return button;
    }

    private JSlider createSlider(int min, int max) {
        JSlider slider = new JSlider(min, max);
        slider.setMajorTickSpacing(3);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider.setFocusable(false);
        return slider;
    }

    private JComboBox<String> createComboBox(String[] strings) {
        JComboBox<String> comboBox = new JComboBox<>(strings);
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox.setFocusable(false);
        comboBox.setPreferredSize(new Dimension(150, 40));
        comboBox.setMaximumSize(new Dimension(150, 40));
        return comboBox;
    }
}
