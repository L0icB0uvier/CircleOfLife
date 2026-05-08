package Model;

import java.util.Set;

public record Critter (Set<Coordinate> stonesCoordinates, int type, int player) {

    public Critter(Set<Coordinate> coordinates, int player){
        this(coordinates, ShapeUtils.getShapeId(coordinates), player);
    }
}
