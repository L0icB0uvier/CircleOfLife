package Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Critter {
    Set<Coordinate> hexagons;          // list of couples (l, c)
    int type;                           // ranges from 0 to 11
    int player;                         // player to whom the tiles belong
    ArrayList<Critter> neighbors;       // list of shapes adjacent to this one

    public Critter(Coordinate stoneCoordinate, int playerIndex){
        hexagons = new HashSet<>();
        hexagons.add(stoneCoordinate);
        type = 0;
        player = playerIndex;
        findNeighbors();
    }

    public Critter(Set<Coordinate> stoneCoordinates, int playerIndex){
        hexagons = stoneCoordinates;
        type = ShapeUtils.getShapeId(stoneCoordinates);
        player = playerIndex;
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
