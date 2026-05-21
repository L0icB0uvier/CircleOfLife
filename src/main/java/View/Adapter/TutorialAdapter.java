package View.Adapter;

import Global.Configuration;
import View.GraphicalUserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TutorialAdapter implements ActionListener {
    GraphicalUserInterface userInterface;

    public TutorialAdapter(GraphicalUserInterface userInterface) {
        this.userInterface = userInterface;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Configuration.info("Changement de page vers GraphicalTutorial");
        userInterface.getGraphicalTutorial().resetPage();
        userInterface.getFrame().setContentPane(userInterface.getGraphicalTutorial());
        userInterface.getFrame().revalidate();
    }
}
