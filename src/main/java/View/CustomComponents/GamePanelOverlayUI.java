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

    private final Rectangle showHoverHighlightBounds = new Rectangle();
    private final Rectangle showFeedforwardHighlightBounds = new Rectangle();
    private final Rectangle showBlockingCrittersHighlightBounds = new Rectangle();
    private final Rectangle showEatenCrittersFeedbackBounds = new Rectangle();
    private final Rectangle showAnimationBounds = new Rectangle();

    Font categoryFont = new Font("Arial", Font.BOLD, 18);
    Font toggleLabelFont = new Font("Arial", Font.BOLD, 16);

    int menuWidth;
    int menuHeight;
    int menuX;
    int menuY;
    int categoryX;
    int itemX;
    int switchWidth;
    int switchHeight;
    int switchX;

    public GamePanelOverlayUI(GamePanel gamePanel, Image icon) {
        this.gamePanel = gamePanel;
        this.icon = icon;

        showHoverHighlight = gamePanel.getShowHoverHighlight();
        showFeedforwardHighlight =  gamePanel.getShowFeedforwardHighlight();
        showBlockingCrittersHighlight = gamePanel.getShowBlockingCrittersHighlight();
        showEatenCrittersFeedback = gamePanel.getShowEatenCrittersFeedback();
        showAnimations = gamePanel.getShowScoreAnimation();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!isMenuOpen) {
            drawOpenMenuButton(c, g2);
        } else {
            drawOptionMenu(c, g2);
        }

        g2.dispose();
    }

    private void drawOpenMenuButton(JComponent c, Graphics2D g2) {
        Rectangle bounds = getButtonBounds(c);
        if (icon != null) {
            g2.setColor(new Color(0, 0, 0, 130));
            g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);

            int padding = 6;

            g2.drawImage(icon, bounds.x + padding, bounds.y + padding, bounds.width - (padding * 2), bounds.height - (padding * 2), c);
        }
    }

    private Rectangle getButtonBounds(JComponent container) {
        float sizeRatio = 0.07f;
        int buttonSize = Math.round(container.getHeight() * sizeRatio);
        int x = container.getWidth() - buttonSize - MARGIN;
        buttonBounds = new Rectangle(x, MARGIN, buttonSize, buttonSize);

        return buttonBounds;
    }

    private void drawOptionMenu(JComponent c, Graphics2D g2) {
        recalculate(c);

        drawOptionMenuFrame(g2);
        DrawTitle(g2);

        // Options contouts
        drawCategory(g2, "Contours des pierres", 90);
        drawLabelledToggle(g2, 100, "Survol", showHoverHighlight, showHoverHighlightBounds);
        drawLabelledToggle(g2, 140, "Evolution/prédation", showFeedforwardHighlight, showFeedforwardHighlightBounds);

        // Options feedbacks
        drawCategory(g2, "Feedback", 200);
        drawLabelledToggle(g2, 210, "Critters bloquants", showBlockingCrittersHighlight, showBlockingCrittersHighlightBounds);
        drawLabelledToggle(g2, 250, "Critters mangés", showEatenCrittersFeedback, showEatenCrittersFeedbackBounds);

        // Options Animations
        drawCategory(g2, "Animation", 310);
        drawLabelledToggle(g2, 320, "Scores", showAnimations, showAnimationBounds);
    }

    private void recalculate(JComponent c) {
        menuWidth = 280;
        menuHeight = 360;
        menuX = (c.getWidth() - menuWidth) - MARGIN;
        menuY = MARGIN;
        categoryX = menuX + 15;
        itemX = menuX + 25;
        switchWidth = 50;
        switchHeight = 26;
        switchX = menuX + menuWidth - switchWidth - 20;

        menuBounds = new Rectangle(menuX, menuY, menuWidth, menuHeight);
    }

    private void drawOptionMenuFrame(Graphics2D g2) {
        g2.setColor(new Color(45, 45, 45, 150));
        g2.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
    }

    private void DrawTitle(Graphics2D g2) {
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        String title = "Options d'affichage";
        g2.drawString(title, menuX + (menuWidth - fm.stringWidth(title)) / 2, menuY + 45);
    }

    private void drawCategory(Graphics2D g2, String categoryName, int x) {
        g2.setFont(categoryFont);
        g2.drawString(categoryName, categoryX, menuY + x);
    }

    private void drawLabelledToggle(Graphics2D g2, int y, String labelText, boolean toggleValue, Rectangle toggleBounds) {
        g2.setFont(toggleLabelFont);
        g2.setColor(Color.WHITE);
        int yShowHoverHighlight = menuY + y;
        g2.drawString(labelText, itemX, yShowHoverHighlight + 18);
        toggleBounds.setBounds(switchX, yShowHoverHighlight, switchWidth, switchHeight);
        drawToggleSwitch(g2, toggleBounds, toggleValue);
    }

    private void drawToggleSwitch(Graphics2D g2, Rectangle bounds, boolean isOn) {

        if (isOn) {
            g2.setColor(new Color(40, 167, 69));
        } else {
            g2.setColor(new Color(100, 100, 100));
        }
        g2.fillRoundRect(bounds.x,
                bounds.y,
                bounds.width,
                bounds.height,
                bounds.height,
                bounds.height);

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
}