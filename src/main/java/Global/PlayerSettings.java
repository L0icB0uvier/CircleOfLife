package Global;

import Controller.IA.AILevel;

public class PlayerSettings {
    boolean isAI;
    AILevel aiLevel;
    String name;

    public PlayerSettings(String name){
        isAI = false;
        aiLevel = AILevel.HARD;
        this.name = name;
    }

    public PlayerSettings(AILevel aiLevel, String name) {
        isAI = true;
        this.aiLevel = aiLevel;
        this.name = name;
    }

    public boolean isAI() {
        return isAI;
    }

    public AILevel getAiLevel() {
        return aiLevel;
    }

    public String getName() { return name; }
}
