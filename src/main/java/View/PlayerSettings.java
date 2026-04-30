package View;

import Controller.IA.AILevel;

public class PlayerSettings {
    boolean isAI;
    AILevel aiLevel;

    public PlayerSettings(){
        isAI = false;
        aiLevel = AILevel.HARD;
    }

    public PlayerSettings(AILevel aiLevel) {
        isAI = true;
        this.aiLevel = aiLevel;
    }

    public boolean isAI() {
        return isAI;
    }

    public AILevel getAiLevel() {
        return aiLevel;
    }
}
