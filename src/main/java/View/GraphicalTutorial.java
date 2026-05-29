package View;

import Global.Configuration;
import View.Adapter.ChangePageAdapter;
import View.CustomComponents.ImageButton;
import View.CustomComponents.ImagePanel;
import View.Utils.FontScaler;
import View.Utils.TutorialPages;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class GraphicalTutorial extends JComponent {
    int pageNumber = 0;
    JLabel titleLabel, textLabel;
    JPanel textPanel;
    BufferedImage[] imagesTuto = new BufferedImage[4];
    ImagePanel image;
    ImageButton buttonNext, buttonPrev, buttonQuit;

    public GraphicalTutorial(GraphicalUserInterface userInterface) {
        MigLayout layout = new MigLayout("fill, insets 10 10 20 10", "[grow, align center]", "[15%][25%][45%][15%]");
        this.setLayout(layout);

        for(int i = 0; i < 4; i++) {
            imagesTuto[i] = (BufferedImage) Configuration.loadImage(TutorialPages.pages[i].getImageFile());
        }

        JPanel titlePanel = new JPanel();
        this.titleLabel = new JLabel(TutorialPages.pages[pageNumber].getTitle());
        titlePanel.add(titleLabel);

        textPanel = new JPanel();
        this.textLabel = new JLabel(TutorialPages.pages[pageNumber].getText());
        textPanel.add(textLabel);

        this.image = new ImagePanel(imagesTuto[pageNumber]);

        this.buttonPrev = new ImageButton("undoIcon.png");
        buttonPrev.addActionListener(e -> previousPage());

        this.buttonNext = new ImageButton("redoIcon.png");
        buttonNext.addActionListener(e -> nextPage());

        this.buttonQuit = new ImageButton("Close.png");
        buttonQuit.addActionListener(new ChangePageAdapter(userInterface, userInterface.graphicalMainMenu));


        MigLayout layoutButtons = new MigLayout("fill, insets 10 10 10 10, hidemode 3", "push[15%, sg]push[15%, sg]push", "[]");
        JComponent buttonsComp = new JPanel(layoutButtons);

        buttonsComp.add(buttonPrev, "cell 0 0, grow");
        buttonsComp.add(buttonNext, "cell 1 0, grow");
        buttonsComp.add(buttonQuit, "cell 1 0, grow");
        buttonQuit.setVisible(false);
        buttonPrev.setEnabled(false);

        this.add(titlePanel, "cell 0 0, grow");
        this.add(textPanel, "cell 0 1, grow");
        this.add(image, "cell 0 2, grow");
        this.add(buttonsComp, "cell 0 3, grow");

        titlePanel.addComponentListener(new FontScaler(0.4f, titleLabel));
        textPanel.addComponentListener(new FontScaler(0.5f, 0.95f, textLabel));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private void updatePageContent() {
        titleLabel.setText(TutorialPages.pages[pageNumber].getTitle());
        textLabel.setText("<html>" + TutorialPages.pages[pageNumber].getText() + "</html>");
        image.setImage(imagesTuto[pageNumber]);

        // On force la mise à jour du FontScaler et de l'affichage
        revalidate();
        repaint();
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new ComponentEvent(textPanel, ComponentEvent.COMPONENT_RESIZED));
    }

    public void nextPage() {
        pageNumber++;
        boolean isLastPage = (pageNumber == imagesTuto.length - 1);
        buttonNext.setVisible(!isLastPage);
        buttonQuit.setVisible(isLastPage);
        buttonPrev.setEnabled(true);
        updatePageContent();
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new ComponentEvent(textPanel, ComponentEvent.COMPONENT_RESIZED));
    }

    public void previousPage() {
        pageNumber = Math.max(0, pageNumber - 1);
        buttonNext.setVisible(true);
        buttonQuit.setVisible(false);
        buttonPrev.setEnabled(pageNumber != 0);
        updatePageContent();
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new ComponentEvent(textPanel, ComponentEvent.COMPONENT_RESIZED));
    }

    public void resetPage() {
        pageNumber = 0;
        buttonNext.setVisible(true);
        buttonQuit.setVisible(false);
        buttonPrev.setEnabled(false);
        updatePageContent();
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new ComponentEvent(textPanel, ComponentEvent.COMPONENT_RESIZED));
    }
}
