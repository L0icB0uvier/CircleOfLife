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
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class GraphicalTutorial extends JComponent {
    int pageNumber = 0;
    JLabel titleLabel, textLabel;
    BufferedImage[] imagesTuto = new BufferedImage[4];
    ImagePanel image;
    ImageButton buttonNext, buttonPrev, buttonQuit;

    public GraphicalTutorial(GraphicalUserInterface userInterface) {
        MigLayout layout = new MigLayout("fill, insets 10", "[align center]", "[8%][17%][grow][15%]");
        this.setLayout(layout);

        for(int i = 0; i < 4; i++) {
            imagesTuto[i] = (BufferedImage) readImage(TutorialPages.pages[i].getImageFile());
        }

        this.titleLabel = new JLabel(TutorialPages.pages[pageNumber].getTitle());
        this.titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.textLabel = new JLabel(TutorialPages.pages[pageNumber].getText());

        this.image = new ImagePanel(imagesTuto[pageNumber]);

        this.buttonPrev = new ImageButton("res/Images/PreviousPage.png");
        buttonPrev.addActionListener(e -> previousPage());

        this.buttonNext = new ImageButton("res/Images/NextPage.png");
        buttonNext.addActionListener(e -> nextPage());

        this.buttonQuit = new ImageButton("res/Images/Quit.png");
        buttonQuit.addActionListener(new ChangePageAdapter(userInterface, userInterface.graphicalMainMenu));


        MigLayout layoutButtons = new MigLayout("fill, insets 10 10 10 10, hidemode 3", "push[sg]push[sg]push", "[]");
        JComponent buttonsComp = new JPanel(layoutButtons);

        buttonsComp.add(buttonPrev, "cell 0 0, grow");
        buttonsComp.add(buttonNext, "cell 1 0, grow");
        buttonsComp.add(buttonQuit, "cell 1 0, grow");
        buttonQuit.setVisible(false);
        buttonPrev.setEnabled(false);

        this.add(titleLabel, "cell 0 0, growy");
        this.add(textLabel, "cell 0 1, growy");
        this.add(image, "cell 0 2, grow");
        this.add(buttonsComp, "cell 0 3, grow");

        titleLabel.addComponentListener(new FontScaler(0.7f, titleLabel));
        textLabel.addComponentListener(new FontScaler(0.15f, textLabel));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.setBackground(UIColor.RED);
        titleLabel.setText(TutorialPages.pages[pageNumber].getTitle());
        textLabel.setText("<html>" + TutorialPages.pages[pageNumber].getText() + "</html>");
        image.setImage(imagesTuto[pageNumber]);
        int size = Math.max(buttonPrev.getWidth(), buttonPrev.getHeight());
        Dimension dim = new Dimension(size, size);
        buttonPrev.setSize(dim);
        buttonNext.setSize(dim);
        buttonQuit.setSize(dim);
    }

    public void nextPage() {
        pageNumber++;
        boolean isLastPage = (pageNumber == imagesTuto.length - 1);
        buttonNext.setVisible(!isLastPage);
        buttonQuit.setVisible(isLastPage);
        buttonPrev.setEnabled(true);
        repaint();
    }

    public void previousPage() {
        pageNumber = Math.max(0, pageNumber - 1);
        buttonNext.setVisible(true);
        buttonQuit.setVisible(false);
        buttonPrev.setEnabled(pageNumber != 0);
        repaint();
    }

    public void resetPage() {
        pageNumber = 0;
        buttonNext.setVisible(true);
        buttonQuit.setVisible(false);
        buttonPrev.setEnabled(false);
        repaint();
    }

    private Image readImage(String nom) {
        InputStream in = Configuration.open("Images/" + nom + ".png");
        Configuration.info("Chargement de l'image " + nom);
        try {
            // Chargement d'une image utilisable dans Swing
            return ImageIO.read(in);
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image " + nom);
        }
        return null;
    }
}
