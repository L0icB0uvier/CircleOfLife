package View.Adapter;

import View.Utils.ChoiceBox;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AIPlayerAdapter implements java.awt.event.ActionListener {
    JTextField player;
    ChoiceBox AI;
    JLabel playerLabel, AILabel;
    ChoiceBox choiceBox;

    public AIPlayerAdapter(JLabel playerLabel, ChoiceBox AI, JTextField player, JLabel AILabel, ChoiceBox choiceBox) {
        this.playerLabel = playerLabel;
        this.AI = AI;
        this.player = player;
        this.AILabel = AILabel;
        this.choiceBox = choiceBox;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean isAI = choiceBox.getValue().equals("IA");
        this.player.setVisible(isAI);
        this.playerLabel.setVisible(isAI);
        this.AI.setVisible(!isAI);
        this.AILabel.setVisible(!isAI);
        if(isAI) {
           player.setText("");
       }
    }
}
