package View;

import View.Utils.FontScaler;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class GraphicalMainMenu extends JPanel {
    Box menuBox;
    JFrame parent;
    JButton newGameButton, continueButton, loadButton, tutorialButton, quitButton;
    Font titleFont;
    Font buttonFont;

    public GraphicalMainMenu(JFrame parent){
        super(new BorderLayout());
        menuBox = Box.createVerticalBox();
        this.parent = parent;

        MigLayout layout = new MigLayout("fill, insets 10 10 10 10", "20%[align left]",
                "[30%][10%][10%][10%][10%][10%]push" );
        this.setLayout(layout);

        titleFont = new Font("Arial", Font.BOLD, this.getFont().getSize());
        JLabel titleLabel = new JLabel("Circle of life");

        newGameButton = createButton("> Nouvelle partie");
        continueButton = createButton("> Continuer");
        loadButton = createButton("> Charger une partie");
        tutorialButton = createButton("> Tutoriel");
        quitButton = createButton("> Quitter");

        buttonFont = new Font("Arial", Font.BOLD, this.getFont().getSize());
        newGameButton.setFont(buttonFont);
        continueButton.setFont(buttonFont);
        loadButton.setFont(buttonFont);
        tutorialButton.setFont(buttonFont);
        quitButton.setFont(buttonFont);

        this.add(titleLabel, "cell 0 0, growy");

        this.add(newGameButton, "cell 0 1, growy");
        this.add(continueButton, "cell 0 2, growy");
        this.add(loadButton, "cell 0 3, growy");
        this.add(tutorialButton, "cell 0 4, growy");
        this.add(quitButton, "cell 0 5, growy");

        titleLabel.addComponentListener(new FontScaler(0.5f, titleLabel));
        newGameButton.addComponentListener(new FontScaler(0.5f, newGameButton, continueButton, loadButton, tutorialButton, quitButton));

    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        button.setContentAreaFilled(false);
        return button;
    }
}
