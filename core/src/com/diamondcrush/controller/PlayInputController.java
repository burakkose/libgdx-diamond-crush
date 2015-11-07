package com.diamondcrush.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.diamondcrush.model.Diamond;
import com.diamondcrush.world.GameBoard;


public class PlayInputController extends InputAdapter {

    private static final Vector3 tmp = new Vector3();

    private final GameBoard gameGameBoard;
    private final OrthographicCamera cam;
    private final float scaleFactorX;
    private final float scaleFactorY;
    private Diamond touchDownDiamond;
    private boolean dragging;


    public PlayInputController(GameBoard gameGameBoard, OrthographicCamera cam,
                               float scaleFactorX, float scaleFactorY) {
        this.cam = cam;
        this.gameGameBoard = gameGameBoard;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        cam.unproject(tmp.set(screenX, screenY, 0));
        touchDownDiamond = gameGameBoard.findDiamond(screenX, screenY);
        dragging = true;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!dragging) return false;
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        cam.unproject(tmp.set(screenX, screenY, 0));
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        Diamond touchUpDiamond = gameGameBoard.findDiamond(screenX, screenY);
        if (touchUpDiamond != null && touchDownDiamond != null
                && touchDownDiamond != touchUpDiamond
                && touchDownDiamond.isNeighbor(touchUpDiamond)) {
            gameGameBoard.actIfPossible(touchUpDiamond, touchDownDiamond);
        }
        cam.unproject(tmp.set(screenX, screenY, 0));
        dragging = false;
        touchDownDiamond = null;
        tmp.setZero();
        return true;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }
}
