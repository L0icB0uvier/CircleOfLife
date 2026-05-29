package View.Utils;

public class TutorialPages {
    public static final TutorialPage[] pages = new TutorialPage[]{new TutorialPage("Tutoriel Circle of life",
                    "Bienvenue dans le jeu “Circle of Life” (ou Jeu de la Vie), un jeu de plateau à 2 joueurs.<br>" +
                    "Pour gagner, un joueur doit soit manger 20 pierres adverses,<br>" +
                        "soit ne plus pouvoir jouer à son tour.", "Tuto_1.png"),
        new TutorialPage("Déroulement d'un coup",
                "Au début de la partie, le plateau est vide." +
                    "Chaque joueur possède des pierres.<br>Un tour se déroule de cette manière :<br>" +
                    "Un joueur pose une pierre de telle sorte à former un «organisme»,<br>"+
                    "c’est-à-dire une formation d’au maximum 4 pierres.", "Tuto_2.png"),
        new TutorialPage("Manger une pierre",
                "Autour du plateau se trouve un cercle, démontrant quel organisme peut manger lequel, indiqué par les flèches.<br>" +
                    "Si pendant son tour, l’organisme crée se trouve à côté d’un organisme qu’il peut manger,<br>" +
                    "alors le joueur mange les pierres adverses et les ajoute à son score<br>afin de l’approcher de l’objectif de victoire.", "Tuto_3.png"),
        new TutorialPage("Remplissage du plateau",
                "Comme un organisme est constitué d’au maximum 4 pierres adjacentes,<br>visible grâce au cercle de la vie, " +
                "il est impossible au joueur de poser une pierre<br>afin de créer un organisme de plus de 4 pierres. " +
                "Si le joueur est<br>dans l’incapacité de poser une pierre sans briser cette règle, alors il a gagné!", "Tuto_4.png")};


}
