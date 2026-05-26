package Patterns;

import Model.Coordinate;

import java.util.Map;

public interface ScoreEventObserver {
    void onScoreUpdated(Map<Coordinate, Integer> eatenInfo, int player);
}
