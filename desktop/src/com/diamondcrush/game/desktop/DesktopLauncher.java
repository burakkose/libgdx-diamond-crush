package com.diamondcrush.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.diamondcrush.game.DiamondCrush;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Diamond Crush";
        config.width = 345;
        config.height = 640;
        new LwjglApplication(new DiamondCrush(), config);
    }
}
