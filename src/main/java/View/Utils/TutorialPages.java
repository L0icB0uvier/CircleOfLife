package View.Utils;

public class TutorialPages {
    public static final TutorialPage[] pages = new TutorialPage[]{new TutorialPage("Tutoriel Circle of life", """
                    Bienvenue dans le jeu “Circle of Life” (ou Jeu de la Vie), un jeu de plateau à 2 joueurs.<br>
                    Pour gagner, deux conditions sont remplissables : Manger 20 pierres adverses, ou faire en sorte que l’autre joueur ne puisse plus jouer.""", "Tuto_1"),
        new TutorialPage("Déroulement d'un coup", """
                Au début de la partie, le plateau est vide.<br>
                Chaque joueur possède des pierres. Un tour se déroule de cette manière :<br>
                Un joueur pose une pierre de telle sorte à former un «organisme», c’est-à-dire une formation d’au maximum 4 pierres.""", "Tuto_2"),
        new TutorialPage("Manger une pierre", """
                Autour du plateau se trouve un cercle, démontrant quel organisme peut manger lequel, indiqué par les flèches.<br>
                Si pendant son tour, l’organisme crée se trouve à côté d’un organisme qu’il peut manger,<br>
                alors le joueur les pierres adverses et les ajoute à son total afin de l’approcher de l’objectif de victoire.""", "Tuto_3"),
        new TutorialPage("Remplissage du plateau", """
                Comme un organisme est constitué d’au maximum 4 pierres adjacentes, comme indiqué par le cercle de la vie,<br>
                il est impossible au joueur de poser une pierre afin de créer un organisme de 5 pierres.<br>
                Si le joueur est dans l’incapacité de poser une pierre sans briser cette règle, alors il a gagné!""", "Tuto_4")};


}
