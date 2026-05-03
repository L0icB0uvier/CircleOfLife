package View.CustomComponents;

import Global.Configuration;
import Model.Game;
import Model.Match;
import Patterns.Observer;
import View.Utils.UIColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class GamePanel extends JComponent implements Observer {
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
            this.game.addObserver(this);

            imgWaffle=readImage("waffle");

            match = game.getMatch();

            nbLines = match.getNbLines();
            nbColumns = match.getNbCol();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d=(Graphics2D) g;

            int width = getWidth();
            int height = getHeight();

            Configuration.info(String.valueOf(width));
            Configuration.info(String.valueOf(height));

            scaleX=Math.max(width/nbColumns, 1); //scale 0 entraine une erreur lors d'appel a drawImage
            scaleY=Math.max(height/nbLines, 1);

            int posX=0;
            int posY=0;

            int nbCurrLines;
            for (int i = 0; i < nbColumns; i++) {
                posY=0;
                nbCurrLines = match.getColumnNumber(i);

                if (nbCurrLines<=0)
                    break;

                for (int j = 0; j < nbCurrLines; j++) {
                    g2d.drawImage(imgWaffle,posX, posY, scaleX, scaleY, null);

                    if (i==0 && j==0) { //la case empoisonnée
                        Color temp=g2d.getColor();
                        g2d.setColor(new Color(0,0,0,160));
                        g2d.fillRect(posX, posY, scaleX, scaleY);
                        g2d.setColor(temp);
                    }

                    if(previewEnabled){
                        int columnMouse = xToNbColumn(mouseX);
                        int lineMouse = yToNbLine(mouseY);

                        if(columnMouse != -1 && lineMouse != -1 && i >= xToNbColumn(mouseX) && j >= yToNbLine(mouseY)){
                            g2d.setComposite(AlphaComposite.SrcAtop);
                            Color temp= g2d.getColor();
                            Color playerColor = UIColor.getColor(game.getMatch().getCurrentPlayerIndex());
                            Color tintColor = new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), tintAlpha);
                            g2d.setColor(tintColor);
                            g2d.fillRect(posX, posY, scaleX, scaleY);
                            g2d.setColor(temp);
                        }
                    }
                    posY+=scaleY;
                }
                posX+=scaleX;
            }
        }

        private void drawLines(Graphics2D g2d){
            int posX=0, posY=0, nbCurrLines = 0;

            //lignes droites separants les cases
            g2d.setStroke(new BasicStroke(5));

            for (int i = 0; i < nbColumns; i++) {
                posY=0;
                nbCurrLines= match.getColumnNumber(i);

                if (nbCurrLines<=0)
                    break;

                posX+=scaleX;

                for (int j = 0; j < nbCurrLines; j++) {
                    posY+=scaleY;
                    if (i == nbColumns-1 || match.getColumnNumber(i+1) <= j) //dernier colonne ou la case prochaine dans ce ligne deja mange
                        //g2d.drawLine(posX, 0, posX, posY); //ligne droite horizontale
                        g2d.drawLine(0, posY, posX, posY);
                }

                g2d.drawLine(posX, 0, posX, nbCurrLines * scaleY); //ligne droite verticale
            }
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
