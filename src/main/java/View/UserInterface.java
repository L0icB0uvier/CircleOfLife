package View;

import Model.PlayerData;

public interface UserInterface {
    void toggleFullscreen();
    void updateSettings();
    void updateScore(PlayerData[] playerData);
}