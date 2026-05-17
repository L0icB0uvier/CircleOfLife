package View;

import View.Adapter.AIPlayerAdapter;
import View.Utils.ChoiceBox;
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
    ChoiceBox player1Choice, player2Choice, AI1LevelChoice, AI2LevelChoice;
    JTextField player1NameTextField, player2NameTextField;

    public GraphicalNewGame(JFrame parent){
        super(new BorderLayout());
        this.parent = parent;
        MigLayout layoutPage = new MigLayout("fillx, insets 10 10 10 10", "[left]","[20%][15%, align top][15%, align top]push[10%]" );
        this.setLayout(layoutPage);
        JLabel titleLabel = createLabel("Paramètres de la partie");

        player1Choice = new ChoiceBox("Joueur", "IA");
        player2Choice = new ChoiceBox("Joueur", "IA");
        player1Choice.setVisible(true);
        player2Choice.setVisible(true);
        AI1LevelChoice = new ChoiceBox("Facile", "Moyen", "Difficile");
        AI2LevelChoice = new ChoiceBox("Facile", "Moyen", "Difficile");
        JLabel player1NameLabel = createLabel("Nom :");
        JLabel player2NameLabel = createLabel("Nom :");
        JLabel AI1LevelLabel = createLabel("Difficulté : ");
        JLabel AI2LevelLabel = createLabel("Difficulté : ");

        
        String playerLayout = "fill, insets 0 10 0 10, hidemode 1";
        String playerLayoutCol = "[5%, align right][20%][10%, align right][20%]";
        String playerLayoutRow = "[align center, fill]";
        JComponent player1Comp = new JPanel(new MigLayout(playerLayout, playerLayoutCol, playerLayoutRow));

        JLabel player1Label = createLabel("Joueur 1 :");
        player1Label.setForeground(UIColor.RED);
        AI1LevelLabel.setVisible(false);

        player1NameTextField = createJTextField("Joueur 1");
        AI1LevelChoice.setVisible(false);

        JComponent player2Comp = new JPanel(new MigLayout(playerLayout, playerLayoutCol, playerLayoutRow));

        JLabel player2Label = createLabel("Joueur 2 :");
        player2Label.setForeground(UIColor.ALT_BLUE);
        AI2LevelLabel.setVisible(false);

        player2NameTextField = createJTextField("Joueur 2");
        AI2LevelChoice.setVisible(false);

        MigLayout layoutButtons = new MigLayout("fill, insets 10 10 10 10", "[sg]push[sg]", "[]");
        JComponent buttonsComp = new JPanel(layoutButtons);

        cancelButton = createBorderedButton("Annuler");
        cancelButton.setBackground(UIColor.RED);
        startButton = createBorderedButton("Démarrer");
        startButton.setBackground(UIColor.BACKGROUND);

        player1Choice.setMinimumSize(new Dimension(0, 0));
        player2Choice.setMinimumSize(new Dimension(0, 0));
        AI1LevelChoice.setMinimumSize(new Dimension(0, 0));
        AI2LevelChoice.setMinimumSize(new Dimension(0, 0));
        player1NameLabel.setMinimumSize(new Dimension(0, 0));
        player2NameLabel.setMinimumSize(new Dimension(0, 0));
        AI1LevelLabel.setMinimumSize(new Dimension(0, 0));
        AI2LevelLabel.setMinimumSize(new Dimension(0, 0));
        player1Label.setMinimumSize(new Dimension(0, 0));
        player2Label.setMinimumSize(new Dimension(0, 0));
        player1NameTextField.setMinimumSize(new Dimension(0, 0));
        player2NameTextField.setMinimumSize(new Dimension(0, 0));
        player1Comp.setMinimumSize(new Dimension(0, 0));
        player2Comp.setMinimumSize(new Dimension(0, 0));

        player1Comp.add(player1Label, "cell 0 0");
        player1Comp.add(player1Choice, "cell 1 0, grow");
        player1Comp.add(player1NameLabel, "cell 2 0");
        player1Comp.add(AI1LevelLabel, "cell 2 0");
        player1Comp.add(player1NameTextField, "cell 3 0, growx, hmax 50%");
        player1Comp.add(AI1LevelChoice, "cell 3 0, growx, height 100%");

        player2Comp.add(player2Label, "cell 0 0");
        player2Comp.add(player2Choice, "cell 1 0, grow");
        player2Comp.add(player2NameLabel, "cell 2 0");
        player2Comp.add(AI2LevelLabel, "cell 2 0");
        player2Comp.add(player2NameTextField, "cell 3 0, growx, hmax 50%");
        player2Comp.add(AI2LevelChoice, "cell 3 0, growx, height 100%");

        buttonsComp.add(cancelButton, "cell 0 0, height 25%");
        buttonsComp.add(startButton, "cell 1 0");

        buttonsComp.setMinimumSize(new Dimension(0, 0));

        this.add(titleLabel, "cell 0 0, growy");
        this.add(player1Comp, "cell 0 1, grow");
        this.add(player2Comp, "cell 0 2, grow");
        this.add(buttonsComp, "cell 0 3, grow");
        titleLabel.addComponentListener(new FontScaler(titleLabel));
        player1Comp.addComponentListener(new FontScaler(player1Label, player2Label, player1NameLabel, player2NameLabel, AI1LevelLabel, AI2LevelLabel));
        player1NameTextField.addComponentListener(new FontScaler(0.5f, player1NameTextField));
        player2NameTextField.addComponentListener(new FontScaler(0.5f, player2NameTextField));
        buttonsComp.addComponentListener(new FontScaler(cancelButton, startButton));

        player1Choice.leftBtn.addActionListener(new AIPlayerAdapter(player1NameLabel, AI1LevelChoice, player1NameTextField, AI1LevelLabel, player1Choice));
        player1Choice.rightBtn.addActionListener(new AIPlayerAdapter(player1NameLabel, AI1LevelChoice, player1NameTextField, AI1LevelLabel, player1Choice));
        player2Choice.leftBtn.addActionListener(new AIPlayerAdapter(player2NameLabel, AI2LevelChoice, player2NameTextField, AI2LevelLabel, player2Choice));
        player2Choice.rightBtn.addActionListener(new AIPlayerAdapter(player2NameLabel, AI2LevelChoice, player2NameTextField, AI2LevelLabel, player2Choice));

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
}
