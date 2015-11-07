package com.diamondcrush.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen implements Screen {

    private static final float WIDTH_RATIO = 0.7F;
    private static final float HEIGHT_RATIo = 0.24f;

    private final Game game;
    private final Skin skin;
    private final Stage stage;
    private final SpriteBatch batch;

    public MenuScreen(Game game) {
        this.batch = new SpriteBatch();
        this.stage = new Stage();
        this.game = game;
        this.skin = new Skin();
        init();
        Gdx.input.setInputProcessor(stage);
    }

    private void init() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float density = Gdx.graphics.getDensity();

        Pixmap pixmap = new Pixmap((int) (w * WIDTH_RATIO), (int) (h * HEIGHT_RATIo), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();

        skin.add("white", new Texture(pixmap));

        BitmapFont font = new BitmapFont();
        font.getData().scale(density);
        skin.add("default", font);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);

        textButtonStyle.font = skin.getFont("default");

        skin.add("default", textButtonStyle);

        final TextButton textButton1 = new TextButton("LEVEL 1", textButtonStyle);
        final TextButton textButton2 = new TextButton("LEVEL 2", textButtonStyle);
        final TextButton textButton3 = new TextButton("LEVEL 3", textButtonStyle);

        float k = (h - textButton1.getHeight() * 3) / 8;
        float x = w / 2 - textButton1.getWidth() / 2;

        textButton3.setPosition(x, 3 * k);
        stage.addActor(textButton3);
        textButton2.setPosition(x, 4 * k + textButton3.getHeight());
        stage.addActor(textButton2);
        textButton1.setPosition(x, 5 * k + textButton2.getHeight() + textButton3.getHeight());
        stage.addActor(textButton1);

        textButton1.addListener(new ChangeListener() { // level 1
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PlayScreen(1));
            }
        });

        textButton2.addListener(new ChangeListener() { // level 2
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PlayScreen(2));
            }
        });

        textButton3.addListener(new ChangeListener() { // level 3
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PlayScreen(3));
            }
        });

    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}

