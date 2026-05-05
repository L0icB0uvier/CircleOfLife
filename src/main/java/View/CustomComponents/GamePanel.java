package View.CustomComponents;

import Global.Configuration;
import Model.Game;
import Model.Match;
import Patterns.Observateur;
import View.Utils.UIColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class GamePanel extends JComponent implements Observateur {
        Game game;
        Image imgWaffle;
        Match match;
        private int scaleX;
        private int scaleY;
        private int nbLines;
        private int nbColumns;
        private int mouseX, mouseY;
        private int tintAlpha = 160;
        private boolean previewEnabled = false;

        public GamePanel(Game game){
            this.game = game;
            this.game.ajouteObservateur(this);

            imgWaffle=readImage("waffle");

            match = game.getMatch();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

        }

        private void drawLines(Graphics2D g2d){

        }

        private void drawBorders(Graphics2D g2, Color c, int width, int height) {
            Stroke tempStroke = g2.getStroke();
            Color tempColor = g2.getColor();

            g2.setStroke(new BasicStroke(20));
            g2.setColor(c);

            g2.drawLine(0, 0, width, 0);//haut
            g2.drawLine(0, 0, 0, height);//gauche
            g2.drawLine(0, height, width, height);//bas
            g2.drawLine(width, 0, width, height);//droite

            g2.setStroke(tempStroke);
            g2.setColor(tempColor);
        }

        @Override
        public void update() {
            repaint();
        }

        public void updateMousePosition(int x, int y){
            if(!previewEnabled) return;
            mouseX = x;
            mouseY = y;
            repaint();
        }

        public int yToNbLine(int y) { //pour convertir des coordonnees aux indices
            int offset = this.getY();
            if(y <= offset ||y>= offset+this.getHeight()) return -1;
            return (y-offset) / scaleY;
        }

        public int xToNbColumn(int x) {
            int offset = this.getX();
            return (x - offset) / scaleX;
        }

        public void togglePreview(){
            previewEnabled = !previewEnabled;
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
