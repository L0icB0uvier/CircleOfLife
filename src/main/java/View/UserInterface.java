package View;

import Model.Coordinate;

public interface UserInterface {
    void toggleFullscreen();
    void updateSettings();
    void animateScore(Coordinate groupCoords, int scoreGained, int player, double progress);
}