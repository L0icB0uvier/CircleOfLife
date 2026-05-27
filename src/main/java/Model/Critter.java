package Model;

import java.util.Set;

/**
 * Représente un critter avec ses coordonnées, son type et le joueur auquel il appartient.
 * @param stonesCoordinates Les coordonnées composant le critter.
 * @param type Le type du critter.
 * @param player Le joueur auquel appartient le critter.
 */
public record Critter (Set<Coordinate> stonesCoordinates, int type, int player) {

    public Critter(Set<Coordinate> coordinates, int player){
        this(coordinates, CritterUtils.getCritterId(coordinates), player);
    }
}