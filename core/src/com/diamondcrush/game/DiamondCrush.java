package com.diamondcrush.game;

import com.badlogic.gdx.Game;
import com.diamondcrush.screen.MenuScreen;

public class DiamondCrush extends Game {


    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }

}
