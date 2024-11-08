package src.utils;

import src.screens.Menu;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FontUtility {
    private static FontUtility instance;

    private final Font prstart, arialbd;

    private FontUtility() {
        prstart = loadFont("assets/fonts/prstart.ttf");
        arialbd = loadFont("assets/fonts/arialbd.ttf");
    }

    public static FontUtility getInstance() {
        if (instance == null) {
            return new FontUtility();
        }
        return instance;
    }

    public Font getPrstart() {
        return prstart;
    }

    public Font getArialbd() {
        return arialbd;
    }

    private static Font loadFont(String fontName) {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT,
                    new File(fontName));
            font = font.deriveFont(java.awt.Font.PLAIN, 15);
            GraphicsEnvironment ge
                    = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return font;
    }
}
