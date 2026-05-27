package View;

import View.Adapter.OptionalVisibilityAdapter;
import View.CustomComponents.ChoiceBox;
import View.Utils.FontScaler;
import View.Utils.UIColor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class GraphicalNewGame extends JPanel {
    JFrame parent;
    JButton cancelButton, startButton;
    ChoiceBox player1Choice, player2Choice,  AI1LevelChoice, AI2LevelChoice, startingPlayerChoice;
    JTextField player1NameTextField, player2NameTextField;

    public GraphicalNewGame(JFrame parent){
        super(new BorderLayout());
        this.parent = parent;
        MigLayout layoutPage = new MigLayout("fillx, insets 10 10 10 10, debug", "[center]","[20%][20%, align top][20%, align top][20%, align top][20%]" );
        this.setLayout(layoutPage);
        JPanel titleLabelPanel = new JPanel();
        titleLabelPanel.setLayout(new GridLayout());
        JLabel titleLabel = createLabel("Paramètres de la partie");
        titleLabel.setVerticalAlignment(JLabel.CENTER);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabelPanel.add(titleLabel);

        player1Choice = new ChoiceBox("Joueur", "IA");
        player2Choice = new ChoiceBox("Joueur", "IA");
        player1Choice.setVisible(true);
        player2Choice.setVisible(true);
        AI1LevelChoice = new ChoiceBox("Facile", "Moyen", "Difficile");
        AI2LevelChoice = new ChoiceBox("Facile", "Moyen", "Difficile");
        JLabel player1NameLabel = createLabel("Nom :");
        JLabel player2NameLabel = createLabel("Nom :");
        player1NameLabel.setHorizontalAlignment(JLabel.RIGHT);
        player2NameLabel.setHorizontalAlignment(JLabel.RIGHT);
        JLabel AI1LevelLabel = createLabel("Difficulté : ");
        JLabel AI2LevelLabel = createLabel("Difficulté : ");
        AI1LevelLabel.setHorizontalAlignment(JLabel.RIGHT);
        AI2LevelLabel.setHorizontalAlignment(JLabel.RIGHT);
        JLabel startingPlayerLabel = createLabel("Premier joueur :");
        startingPlayerLabel.setHorizontalAlignment(JLabel.RIGHT);


        startingPlayerChoice = new ChoiceBox("Aléatoire", "Joueur 1", "Joueur 2");

        
        String playerLayout = "fill, insets 0 10 0 10, hidemode 1, debug";
        String playerLayoutCol = "[20%, align right][25%][30%, align right][25%]";
        String playerLayoutRow = "[align center, fill]";
        JComponent player1Comp = new JPanel(new MigLayout(playerLayout, playerLayoutCol, playerLayoutRow));

        JLabel player1Label = createLabel("Joueur 1 :");
        player1Label.setHorizontalAlignment(JLabel.RIGHT);
        JPanel player1LabelPanel = new JPanel();
        player1LabelPanel.setLayout(new GridLayout());
        player1LabelPanel.add(player1Label);
        player1Label.setForeground(UIColor.BLUE);
        AI1LevelLabel.setVisible(false);

        player1NameTextField = createJTextField("Joueur 1");
        AI1LevelChoice.setVisible(false);

        JComponent player2Comp = new JPanel(new MigLayout(playerLayout, playerLayoutCol, playerLayoutRow));

        JLabel player2Label = createLabel("Joueur 2 :");
        player2Label.setHorizontalAlignment(JLabel.RIGHT);
        JPanel player2LabelPanel = new JPanel();
        player2LabelPanel.setLayout(new GridLayout());
        player2LabelPanel.add(player2Label);
        player2Label.setForeground(UIColor.RED);
        AI2LevelLabel.setVisible(false);

        player2NameTextField = createJTextField("Joueur 2");
        AI2LevelChoice.setVisible(false);

        String startingPlayerLayoutCol = "[50%, align right][25%]push";
        JComponent startingPlayerComp = new JPanel(new MigLayout(playerLayout, startingPlayerLayoutCol, playerLayoutRow));

        MigLayout layoutButtons = new MigLayout("fill, insets 10 10 10 10", "[sg]push[sg]", "[]");
        JComponent buttonsComp = new JPanel(layoutButtons);

        cancelButton = createBorderedButton("Annuler");
        cancelButton.setBackground(UIColor.RED);
        startButton = createBorderedButton("Démarrer");
        startButton.setBackground(UIColor.GREEN);

        player1Choice.setMinimumSize(new Dimension(0, 0));
        player2Choice.setMinimumSize(new Dimension(0, 0));
        AI1LevelChoice.setMinimumSize(new Dimension(0, 0));
        AI2LevelChoice.setMinimumSize(new Dimension(0, 0));
        player1NameLabel.setMinimumSize(new Dimension(0, 0));
        player2NameLabel.setMinimumSize(new Dimension(0, 0));
        AI1LevelLabel.setMinimumSize(new Dimension(0, 0));
        AI2LevelLabel.setMinimumSize(new Dimension(0, 0));
        player1LabelPanel.setMinimumSize(new Dimension(0, 0));
        player2LabelPanel.setMinimumSize(new Dimension(0, 0));
        player1NameTextField.setMinimumSize(new Dimension(0, 0));
        player2NameTextField.setMinimumSize(new Dimension(0, 0));
        player1Comp.setMinimumSize(new Dimension(0, 0));
        player2Comp.setMinimumSize(new Dimension(0, 0));
        startingPlayerLabel.setMinimumSize(new Dimension(0, 0));
        startingPlayerChoice.setMinimumSize(new Dimension(0, 0));

        player1Comp.add(player1LabelPanel, "cell 0 0,grow");
        player1Comp.add(player1Choice, "cell 1 0, grow");
        player1Comp.add(player1NameLabel, "cell 2 0, grow");
        player1Comp.add(AI1LevelLabel, "cell 2 0,grow");
        player1Comp.add(player1NameTextField, "cell 3 0, grow, hmax 40%");
        player1Comp.add(AI1LevelChoice, "cell 3 0, grow");

        player2Comp.add(player2LabelPanel, "cell 0 0,grow");
        player2Comp.add(player2Choice, "cell 1 0, grow");
        player2Comp.add(player2NameLabel, "cell 2 0,grow");
        player2Comp.add(AI2LevelLabel, "cell 2 0,grow");
        player2Comp.add(player2NameTextField, "cell 3 0, grow, hmax 40%");
        player2Comp.add(AI2LevelChoice, "cell 3 0, grow");

        startingPlayerComp.add(startingPlayerLabel, "cell 0 0, grow");
        startingPlayerComp.add(startingPlayerChoice, "cell 1 0, grow");

        buttonsComp.add(cancelButton, "cell 0 0, height 25%");
        buttonsComp.add(startButton, "cell 1 0");

        buttonsComp.setMinimumSize(new Dimension(0, 0));

        this.add(titleLabelPanel, "cell 0 0, grow");
        this.add(player1Comp, "cell 0 1, grow");
        this.add(player2Comp, "cell 0 2, grow");
        this.add(startingPlayerComp, "cell 0 3, grow");
        this.add(buttonsComp, "cell 0 4, grow");

        titleLabelPanel.addComponentListener(new FontScaler(0.6f, titleLabel));
        player1LabelPanel.addComponentListener(new FontScaler(0.75f, 1.5f, player1Label, player1NameLabel, AI1LevelLabel, startingPlayerLabel, player2Label, player2NameLabel, AI2LevelLabel));
        player1NameTextField.addComponentListener(new FontScaler(0.5f, player1NameTextField, player2NameTextField));
        buttonsComp.addComponentListener(new FontScaler(cancelButton, startButton));
        player1Choice.getLabelPanel().addComponentListener(new FontScaler(0.7f, 0.95f, player1Choice.getLabel(), player2Choice.getLabel(), AI1LevelChoice.getLabel(), AI2LevelChoice.getLabel(), startingPlayerChoice.getLabel()));

        player1Choice.leftBtn.addActionListener(new OptionalVisibilityAdapter(player1NameLabel, AI1LevelChoice, player1NameTextField, AI1LevelLabel, player1Choice, -1,"Joueur"));
        player1Choice.rightBtn.addActionListener(new OptionalVisibilityAdapter(player1NameLabel, AI1LevelChoice, player1NameTextField, AI1LevelLabel, player1Choice, 1,"Joueur"));
        player2Choice.leftBtn.addActionListener(new OptionalVisibilityAdapter(player2NameLabel, AI2LevelChoice, player2NameTextField, AI2LevelLabel, player2Choice, -1, "Joueur"));
        player2Choice.rightBtn.addActionListener(new OptionalVisibilityAdapter(player2NameLabel, AI2LevelChoice, player2NameTextField, AI2LevelLabel, player2Choice, 1,"Joueur"));


        this.setVisible(true);
        this.requestFocusInWindow();
    }

    private static JTextField createJTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getForeground().equals(Color.LIGHT_GRAY)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.LIGHT_GRAY);
                    textField.setText(placeholder);
                }
            }
        });
        textField.setForeground(Color.LIGHT_GRAY);
        textField.setText(placeholder);
        textField.requestFocus(false);
        return textField;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFocusable(false);
        return label;
    }

    private JButton createBorderedButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        return button;
    }

    public String[] getTextFields() {
        return new String[]{player1NameTextField.getText(), player2NameTextField.getText()};
    }

    
}
