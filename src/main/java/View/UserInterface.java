package View;

import Model.PlayerData;

public interface UserInterface {
    void toggleFullscreen();
    void updateSettings();
    void playerTurn(int nPlayer);
    void updateScore(PlayerData[] playerData);
}