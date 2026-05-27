package Patterns;

import Model.Coordinate;

import java.util.Map;
import java.util.Set;

public interface ScoreEventObserver {
    void onScoreUpdated(Map<Set<Coordinate>, Integer> eatenInfo, int player);
}
