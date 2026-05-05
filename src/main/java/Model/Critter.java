package Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Critter {
    Set<Coordinate> hexagons;          // list of couples (l, c)
    int type;                           // ranges from 0 to 11
    int player;                         // player to whom the tiles belong
    ArrayList<Critter> neighbors;       // list of shapes adjacent to this one

    public Critter(int l, int c, int playerIndex){
        hexagons = new HashSet<>();
        hexagons.add(new Coordinate(l, c));
        type = 0;
        player = playerIndex + 1;
        findNeighbors();
    }

    public Critter(Set<Coordinate> stoneCoordinate, int playerIndex){
        hexagons = stoneCoordinate;
        var normalizedCoordinates = ShapeUtils.normalizeCoordinate(stoneCoordinate);
        type = ShapeUtils.getShapeId(normalizedCoordinates);
        player = playerIndex + 1;
        findNeighbors();
    }

    public boolean canEvolve(int l, int c){
        // TODO : list all possible evolution cases
        return false;
    }

    private void findNeighbors(){
        // TODO : find neighbors for new critter and update neighbors' ArrayLists
    }

}
