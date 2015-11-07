package com.diamondcrush.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.diamondcrush.controller.PlayInputController;
import com.diamondcrush.world.GameBoard;
import com.diamondcrush.world.GameBoardRenderer;

class PlayScreen implements Screen {

    private final GameBoardRenderer gameBoardRenderer;
    private final PlayInputController playInputController;

    public PlayScreen(int level) {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float gameWidth = screenHeight * 7f / 13f;
        float gameHeight = screenHeight / (screenWidth / gameWidth);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true, gameWidth, gameHeight);
        camera.update();

        float widthFactor = screenWidth / gameWidth;
        float heightFactor = screenHeight / gameHeight;

        GameBoard gameBoard = new GameBoard(level, gameWidth, gameHeight);
        gameBoardRenderer = new GameBoardRenderer(gameBoard, camera);
        playInputController = new PlayInputController(gameBoard, camera,
                widthFactor, heightFactor);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(playInputController);
    }

    @Override
    public void render(float delta) {
        gameBoardRenderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameBoardRenderer.dispose();
    }
}
