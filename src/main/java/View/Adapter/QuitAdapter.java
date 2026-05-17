package View.Adapter;

import Global.Configuration;

import java.awt.event.ActionEvent;

public class QuitAdapter implements java.awt.event.ActionListener {

    public QuitAdapter(){
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Configuration.info("Fermeture de l'application");
        System.exit(0);
    }
}
