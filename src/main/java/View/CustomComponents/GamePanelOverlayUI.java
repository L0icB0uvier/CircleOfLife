package View.CustomComponents;

import View.EventCollector;

import javax.swing.*;
import java.awt.*;

public class GamePanelOverlayUI extends javax.swing.plaf.LayerUI<GamePanel> {
    private final Image icon;
    private final EventCollector controller;

    private final float sizeRatio = 0.05f;
    private final int MARGIN = 20;

    private Rectangle buttonBounds = new Rectangle(20, 20, 20, 20);

    // --- NOUVELLES VARIABLES POUR LE MENU ---
    private boolean isMenuOpen = false;
    private Rectangle resumeButtonBounds = new Rectangle();
    private Rectangle restartButtonBounds = new Rectangle();

    public GamePanelOverlayUI(EventCollector controller, Image icon) {
        this.controller = controller;
        this.icon = icon;
    }

    private Rectangle getButtonBounds(JComponent container) {
        int buttonSize = Math.round(container.getHeight() * sizeRatio);
        int x = container.getWidth() - buttonSize - MARGIN;
        int y = MARGIN;
        buttonBounds = new Rectangle(x, y, buttonSize, buttonSize);

        return buttonBounds;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c); // Dessine le plateau de jeu (GamePanel) en fond

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!isMenuOpen) {
            // --- MODE NORMAL : On affiche uniquement l'icône ---
            Rectangle bounds = getButtonBounds(c);
            if (icon != null) {
                g2.drawImage(icon, bounds.x, bounds.y, bounds.width, bounds.height, c);
            }
        } else {
            // --- MODE MENU : Voile sombre et options ---

            // 1. Fond semi-transparent sur tout le panneau de jeu
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, c.getWidth(), c.getHeight());

            // 2. Dimensions du panneau central adaptées à l'écran
            int menuWidth = 260;
            int menuHeight = 220;
            int menuX = (c.getWidth() - menuWidth) / 2;
            int menuY = (c.getHeight() - menuHeight) / 2;

            // Dessin du cadre du menu
            g2.setColor(new Color(45, 45, 45));
            g2.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);

            // Titre du menu
            g2.setFont(new Font("SansSerif", Font.BOLD, 22));
            FontMetrics fm = g2.getFontMetrics();
            String title = "PAUSE";
            g2.drawString(title, menuX + (menuWidth - fm.stringWidth(title)) / 2, menuY + 45);

            // 3. Boutons d'options du menu
            g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
            fm = g2.getFontMetrics();

            // Option 1 : Reprendre
            resumeButtonBounds = new Rectangle(menuX + 30, menuY + 80, menuWidth - 60, 40);
            g2.setColor(new Color(70, 70, 70));
            g2.fillRoundRect(resumeButtonBounds.x, resumeButtonBounds.y, resumeButtonBounds.width, resumeButtonBounds.height, 10, 10);
            g2.setColor(Color.WHITE);
            String textResume = "Reprendre";
            g2.drawString(textResume, resumeButtonBounds.x + (resumeButtonBounds.width - fm.stringWidth(textResume)) / 2, resumeButtonBounds.y + 25);

            // Option 2 : Recommencer (ou une action de votre EventCollector)
            restartButtonBounds = new Rectangle(menuX + 30, menuY + 140, menuWidth - 60, 40);
            g2.setColor(new Color(220, 53, 69)); // Rouge
            g2.fillRoundRect(restartButtonBounds.x, restartButtonBounds.y, restartButtonBounds.width, restartButtonBounds.height, 10, 10);
            g2.setColor(Color.WHITE);
            String textRestart = "Recommencer";
            g2.drawString(textRestart, restartButtonBounds.x + (restartButtonBounds.width - fm.stringWidth(textRestart)) / 2, restartButtonBounds.y + 25);
        }

        g2.dispose();
    }

    @Override
    protected void processMouseEvent(java.awt.event.MouseEvent e, JLayer<? extends GamePanel> l) {
        if (e.getID() == java.awt.event.MouseEvent.MOUSE_CLICKED) {
            Point clickPoint = e.getPoint();

            if (!isMenuOpen) {
                // Si le menu est fermé, on met à jour et vérifie le bouton icône de pause
                getButtonBounds(l);
                if (buttonBounds.contains(clickPoint)) {
                    isMenuOpen = true;
                    l.repaint(); // Force le dessin du voile noir
                    e.consume(); // Bloque le clic pour ne pas jouer sur le plateau sous l'icône
                }
            } else {
                // Si le menu est ouvert, on consomme TOUS les clics pour protéger le jeu en dessous
                e.consume();

                // Clic sur "Reprendre"
                if (resumeButtonBounds.contains(clickPoint)) {
                    isMenuOpen = false;
                    l.repaint();
                }
                // Clic sur "Recommencer"
                else if (restartButtonBounds.contains(clickPoint)) {
                    isMenuOpen = false;
                    l.repaint();

                    // Exemple d'action : vous pouvez déclencher un événement via votre contrôleur
                    // controller.action("Restart");
                    System.out.println("Déclenchement : Recommencer la partie");
                }
            }
        }

        // On ne laisse passer l'événement vers le plateau de jeu QUE si le menu est fermé
        if (!isMenuOpen) {
            super.processMouseEvent(e, l);
        }
    }
}