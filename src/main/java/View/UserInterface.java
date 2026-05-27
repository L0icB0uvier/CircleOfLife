package View;

import Model.Coordinate;

import java.util.Set;

public interface UserInterface {
    /**
     * Demande à la vue de passer en plein écran.
     */
    void toggleFullscreen();

    /**
     * Demande à la vue de mettre à jour les settings.
     */
    void updateSettings();

    /**
     * Anime un gain de score.
     * @param groupCoords
     * @param scoreGained La quantité de points gagnés.
     * @param player Le joueur ayant gagné les points.
     * @param progress Le stage de l'animation entre 0 et 1.
     */
    void animateScore(Set<Coordinate> groupCoords, int scoreGained, int player, float progress);
}