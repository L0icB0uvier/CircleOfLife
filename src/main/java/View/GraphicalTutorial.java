package View;

import Global.Configuration;
import View.Adapter.ChangePageAdapter;
import View.CustomComponents.ImageButton;
import View.CustomComponents.ImagePanel;
import View.Utils.FontScaler;
import View.Utils.RoundedBorder;
import View.Utils.TutorialPages;
import View.Utils.UIColor;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class GraphicalTutorial extends JComponent {
    int pageNumber = 0;
    JLabel titleLabel, textLabel;
    JPanel textPanel;
    BufferedImage[] imagesTuto = new BufferedImage[4];
    ImagePanel image;
    ImageButton buttonNext, buttonPrev, buttonQuit, buttonPrevDisabled;

    public GraphicalTutorial(GraphicalUserInterface userInterface) {
        MigLayout layout = new MigLayout("fill, insets 10 10 20 10, debug", "[grow, align center]", "[15%][25%][45%][15%]");
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

        titleLabel.setText(TutorialPages.pages[pageNumber].getTitle());
        textLabel.setText("<html>" + TutorialPages.pages[pageNumber].getText() + "</html>");
        image.setImage(imagesTuto[pageNumber]);
    }

    public void nextPage() {
        pageNumber++;
        boolean isLastPage = (pageNumber == imagesTuto.length - 1);
        buttonNext.setVisible(!isLastPage);
        buttonQuit.setVisible(isLastPage);
        buttonPrev.setEnabled(true);
        repaint();
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new ComponentEvent(textPanel, ComponentEvent.COMPONENT_RESIZED));
    }

    public void previousPage() {
        pageNumber = Math.max(0, pageNumber - 1);
        buttonNext.setVisible(true);
        buttonQuit.setVisible(false);
        buttonPrev.setEnabled(pageNumber != 0);
        repaint();
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new ComponentEvent(textPanel, ComponentEvent.COMPONENT_RESIZED));
    }

    public void resetPage() {
        pageNumber = 0;
        buttonNext.setVisible(true);
        buttonQuit.setVisible(false);
        buttonPrev.setEnabled(false);
        repaint();
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new ComponentEvent(textPanel, ComponentEvent.COMPONENT_RESIZED));
    }
}
