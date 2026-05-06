package View.Utils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class UIColor {
    static public int BLUE = 0;
    static public int RED = 1;
    static public int BROWN = 2;
    static public int WHITE = 3;
    static public int ORANGE = 4;
    static public int GREEN = 5;
    static public int LIGHT_BLUE = 6;
    static public int LIGHT_RED = 7;
    static public int ALT_BLUE = 8;
    static public int WAFFLE = 9;

    static Map<Integer, Color> colors = new HashMap<>();
    static {
        colors.put(0,new Color(15,0,225));
        colors.put(1,new Color(225,0,15));
        colors.put(2,new Color(181,126,63));
        colors.put(3,new Color(255,249,240));
        colors.put(4,new Color(244,126,82));
        colors.put(5,new Color(0,155,78));
        colors.put(6,new Color(0,0,145,25));
        colors.put(7,new Color(255,0,15,25));
        colors.put(8,new Color(77, 178, 255));
        colors.put(9,new Color(181, 126, 63));
    }

    public static Color getColor(int i){
        if(colors.containsKey(i)) return colors.get(i);
        else return null;
    }
}
