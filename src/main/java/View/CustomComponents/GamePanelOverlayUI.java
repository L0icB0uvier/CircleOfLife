package View.CustomComponents;


import javax.swing.*;
import java.awt.*;

public class GamePanelOverlayUI extends javax.swing.plaf.LayerUI<GamePanel> {
    GamePanel gamePanel;

    private final Image icon;

    private final int MARGIN = 20;

    private Rectangle buttonBounds = new Rectangle(20, 20, 20, 20);

    private boolean isMenuOpen = false;

    private boolean showHoverHighlight;
    private boolean showFeedforwardHighlight;
    private boolean showBlockingCrittersHighlight;
    private boolean showEatenCrittersFeedback;
    private boolean showAnimations;

    private Rectangle menuBounds = new Rectangle();

    private Rectangle showHoverHighlightBounds = new Rectangle();
    private Rectangle showFeedforwardHighlightBounds = new Rectangle();
    private Rectangle showBlockingCrittersHighlightBounds = new Rectangle();
    private Rectangle showEatenCrittersFeedbackBounds = new Rectangle();
    private Rectangle showAnimationBounds = new Rectangle();

    Font categoryFont = new Font("Arial", Font.BOLD, 18);
    Font toggleLabelFont = new Font("Arial", Font.BOLD, 15);

    public GamePanelOverlayUI(GamePanel gamePanel, Image icon) {
        this.gamePanel = gamePanel;
        this.icon = icon;

        showHoverHighlight = gamePanel.getShowHoverHighlight();
        showFeedforwardHighlight =  gamePanel.getShowFeedforwardHighlight();
        showBlockingCrittersHighlight = gamePanel.getShowBlockingCrittersHighlight();
        showEatenCrittersFeedback = gamePanel.getShowEatenCrittersFeedback();
        showAnimations = gamePanel.getShowScoreAnimation();
    }

    private Rectangle getButtonBounds(JComponent container) {
        float sizeRatio = 0.07f;
        int buttonSize = Math.round(container.getHeight() * sizeRatio);
        int x = container.getWidth() - buttonSize - MARGIN;
        buttonBounds = new Rectangle(x, MARGIN, buttonSize, buttonSize);

        return buttonBounds;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!isMenuOpen) {
            Rectangle bounds = getButtonBounds(c);
            if (icon != null) {
                g2.setColor(new Color(0, 0, 0, 130));
                g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);

                int padding = 6;

                g2.drawImage(icon, bounds.x + padding, bounds.y + padding, bounds.width - (padding * 2), bounds.height - (padding * 2), c);
            }
        } else {
            int menuWidth = 280;
            int menuHeight = 360;
            int menuX = (c.getWidth() - menuWidth) - MARGIN;
            int menuY = MARGIN;
            int itemX = menuX + 20;
            int switchWidth = 50;
            int switchHeight = 26;
            int switchX = menuX + menuWidth - switchWidth - 20;

            menuBounds = new Rectangle(menuX, menuY, menuWidth, menuHeight);

            // Dessin du cadre du menu
            g2.setColor(new Color(45, 45, 45));
            g2.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);

            // Titre du menu
            g2.setFont(new Font("SansSerif", Font.BOLD, 22));
            FontMetrics fm = g2.getFontMetrics();
            String title = "Options d'affichage";
            g2.drawString(title, menuX + (menuWidth - fm.stringWidth(title)) / 2, menuY + 45);

            g2.setFont(categoryFont);
            String contours = "Contours des pierres";
            g2.drawString(contours, menuX + (menuWidth - fm.stringWidth(title)) / 2, menuY + 90);

            g2.setFont(toggleLabelFont);

            int yShowHoverHighlight = menuY + 100;
            g2.setColor(Color.WHITE);
            g2.drawString("Survol", itemX, yShowHoverHighlight + 18);
            showHoverHighlightBounds = new Rectangle(switchX, yShowHoverHighlight, switchWidth, switchHeight);
            drawToggleSwitch(g2, showHoverHighlightBounds, showHoverHighlight);

            int yShowFeedforwardHighlight = menuY + 140;
            g2.setColor(Color.WHITE);
            g2.drawString("Evolution/prédation", itemX, yShowFeedforwardHighlight + 18);
            showFeedforwardHighlightBounds = new Rectangle(switchX, yShowFeedforwardHighlight, switchWidth, switchHeight);
            drawToggleSwitch(g2, showFeedforwardHighlightBounds, showFeedforwardHighlight);

            g2.setFont(categoryFont);
            String feedback = "Feedback";
            g2.drawString(feedback, menuX + (menuWidth - fm.stringWidth(title)) / 2, menuY + 200);
            g2.setFont(toggleLabelFont);

            int yBlockingCrittersHighlight = menuY + 210;
            g2.setColor(Color.WHITE);
            g2.drawString("Critters bloquants", itemX, yBlockingCrittersHighlight + 18);
            showBlockingCrittersHighlightBounds = new Rectangle(switchX, yBlockingCrittersHighlight, switchWidth, switchHeight);
            drawToggleSwitch(g2, showBlockingCrittersHighlightBounds, showBlockingCrittersHighlight);

            int yShowEatenCrittersFeedback = menuY + 250;
            g2.setColor(Color.WHITE);
            g2.drawString("Critters mangés", itemX, yShowEatenCrittersFeedback + 18);
            showEatenCrittersFeedbackBounds = new Rectangle(switchX, yShowEatenCrittersFeedback, switchWidth, switchHeight);
            drawToggleSwitch(g2, showEatenCrittersFeedbackBounds, showEatenCrittersFeedback);

            g2.setFont(categoryFont);
            String animation = "Animation";
            g2.drawString(animation, menuX + (menuWidth - fm.stringWidth(title)) / 2, menuY + 310);
            g2.setFont(toggleLabelFont);

            int yShowAnimation = menuY + 320;
            g2.setColor(Color.WHITE);
            g2.drawString("Scores", itemX, yShowAnimation + 18);
            showAnimationBounds = new Rectangle(switchX, yShowAnimation, switchWidth, switchHeight);
            drawToggleSwitch(g2, showAnimationBounds, showAnimations);
        }

        g2.dispose();
    }

    @Override
    protected void processMouseEvent(java.awt.event.MouseEvent e, JLayer<? extends GamePanel> l) {
        if (e.getID() == java.awt.event.MouseEvent.MOUSE_CLICKED) {
            Point clickPoint = e.getPoint();

            if (!isMenuOpen) {
                getButtonBounds(l);
                if (buttonBounds.contains(clickPoint)) {
                    isMenuOpen = true;
                    l.repaint();
                    e.consume();
                }
            } else {
                e.consume();

                if (showHoverHighlightBounds.contains(clickPoint)) {
                    showHoverHighlight = !showHoverHighlight;
                    gamePanel.setShowHoverHighlight(showHoverHighlight);
                    l.repaint();
                }

                if(showFeedforwardHighlightBounds.contains(clickPoint)){
                    showFeedforwardHighlight = !showFeedforwardHighlight;
                    gamePanel.setShowFeedforwardHighlight(showFeedforwardHighlight);
                    l.repaint();
                }

                if(showBlockingCrittersHighlightBounds.contains(clickPoint)){
                    showBlockingCrittersHighlight = !showBlockingCrittersHighlight;
                    gamePanel.setShowBlockingCrittersHighlight(showBlockingCrittersHighlight);
                    l.repaint();
                }

                if(showEatenCrittersFeedbackBounds.contains(clickPoint)){
                    showEatenCrittersFeedback = !showEatenCrittersFeedback;
                    gamePanel.setShowEatenCrittersFeedback(showEatenCrittersFeedback);
                    l.repaint();
                }

                if(showAnimationBounds.contains(clickPoint)){
                    showAnimations = !showAnimations;
                    gamePanel.setShowScoreAnimation(showAnimations);
                    l.repaint();
                }

                if(!menuBounds.contains(clickPoint)){
                    isMenuOpen = false;
                    l.repaint();
                }
            }
        }

        if (!isMenuOpen) {
            super.processMouseEvent(e, l);
        }
    }

    private void drawToggleSwitch(Graphics2D g2, Rectangle bounds, boolean isOn) {

        if (isOn) {
            g2.setColor(new Color(40, 167, 69));
        } else {
            g2.setColor(new Color(100, 100, 100));
        }
        g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, bounds.height, bounds.height);

        g2.setColor(Color.WHITE);
        int padding = 3;
        int circleSize = bounds.height - (padding * 2);
        int circleX;

        if (isOn) {
            circleX = bounds.x + bounds.width - circleSize - padding;
        } else {
            circleX = bounds.x + padding;
        }

        g2.fillOval(circleX, bounds.y + padding, circleSize, circleSize);
    }
}