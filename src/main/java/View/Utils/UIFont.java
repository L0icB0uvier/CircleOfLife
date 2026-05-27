package View.Utils;

import View.CustomComponents.PlayerInfo;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class UIFont {
    static Font font;
    static boolean isCharged = false;

    public static Font getFont(){
        if(!isCharged) chargeFont();
        return font;
    }

    static void chargeFont(){
        InputStream is = PlayerInfo.class.getResourceAsStream("/Fonts/The Bomb Sound.ttf");
        try {
            assert is != null;
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(12f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        isCharged = true;
    }
}
