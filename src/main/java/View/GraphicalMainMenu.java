package View;

import Model.GameDataManager;
import View.CustomComponents.ErrorPopUpPanel;
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

        MigLayout layout = new MigLayout("fill, insets 10 10 10 10", "[20%][grow,align left][20%]",
                "[25%][15%][15%][15%][15%][15%]" );
        this.setLayout(layout);

        titleFont = new Font("Arial", Font.BOLD, this.getFont().getSize());
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Circle of life");
        titlePanel.add(titleLabel);

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

        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);

        JPanel newGamePanel,continuePanel,loadPanel,tutorialPanel,quitPanel;
        newGamePanel = new JPanel();
        newGamePanel.setLayout(fl);
        newGamePanel.add(newGameButton);

        continuePanel = new JPanel();
        continuePanel.setLayout(fl);
        continuePanel.add(continueButton);

        loadPanel = new JPanel();
        loadPanel.setLayout(fl);
        loadPanel.add(loadButton);

        tutorialPanel =new JPanel();
        tutorialPanel.setLayout(fl);
        tutorialPanel.add(tutorialButton);

        quitPanel = new JPanel();
        quitPanel.setLayout(fl);
        quitPanel.add(quitButton);

        newGamePanel.addComponentListener(new FontScaler(0.5f, newGameButton, continueButton, loadButton, tutorialButton, quitButton));
        titlePanel.addComponentListener(new FontScaler(0.5f, 0.9f, titleLabel));

        this.add(titlePanel, "cell 0 0, span 3 0, grow");
        this.add(newGamePanel, "cell 1 1, grow");
        this.add(continuePanel, "cell 1 2, grow");
        this.add(loadPanel, "cell 1 3, grow");
        this.add(tutorialPanel, "cell 1 4, grow");
        this.add(quitPanel, "cell 1 5, grow");

    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
       // button.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    public void errorPopup(String string) {
        new ErrorPopUpPanel(string, this);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        continueButton.setEnabled(GameDataManager.hasSaveFile());
        loadButton.setEnabled(GameDataManager.hasSaveFile());
    }
}
