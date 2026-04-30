package Model;

import java.util.ArrayList;

public class Critter {
    ArrayList<int[]> hexagons;          // list of couples (l, c)
    int type;                           // ranges from 0 to 11
    int player;                         // player to whom the tiles belong
    ArrayList<Critter> neighbors;       // list of shapes adjacent to this one

    public Critter(int l, int c, int playerIndex){
        hexagons = new ArrayList<>();
        hexagons.add(new int[]{l, c});
        type = 0;
        player = playerIndex + 1;
        findNeighbors();
    }

    public boolean canEvolve(int l, int c){
        // TODO : list all possible evolution cases
    }

    private void findNeighbors(){
        // TODO : find neighbors for new critter and update neighbors' ArrayLists
    }

}
