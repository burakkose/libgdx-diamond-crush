package com.diamondcrush.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.diamondcrush.model.Diamond;
import com.diamondcrush.util.MyColor;
import com.diamondcrush.util.TextWrapper;

import java.util.Iterator;
import java.util.Set;

public class GameBoardRenderer {

    private static final int DEFAULT_TEXT_POINT = 26;
    private static final int DEVELOPMENT_WIDTH = 345;

    private final int groupSize;
    private final float gameHeight;
    private final float gameWidth;

    private final int[] scores;
    private final BitmapFont timeFont;
    private final GameBoard gameBoard;
    private final BitmapFont groupFont;
    private final OrthographicCamera cam;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    private final FreeTypeFontGenerator generator;
    private final Array<Vector3> circle;
    private final TextWrapper timeWrapper;
    private final TextWrapper groupWrapper;

    public GameBoardRenderer(GameBoard gameBoard, OrthographicCamera cam) {
        this.cam = cam;
        this.gameBoard = gameBoard;
        this.circle = new Array<Vector3>();
        this.scores = gameBoard.getScores();
        this.spriteBatch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.gameWidth = gameBoard.getGameWidth();
        this.groupSize = gameBoard.getGroupSize();
        this.gameHeight = gameBoard.getGameHeight();
        this.generator = new FreeTypeFontGenerator(Gdx.files.internal("myfont.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        System.out.println(gameWidth);

        parameter.size = (int) (DEFAULT_TEXT_POINT * gameWidth / DEVELOPMENT_WIDTH);
        timeFont = generator.generateFont(parameter);
        groupFont = generator.generateFont(parameter);

        this.timeWrapper = new TextWrapper(timeFont);
        this.timeWrapper.setPosition(new Vector2(0, 0));
        this.groupWrapper = new TextWrapper(groupFont);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        gameBoard.update(delta);
        shapeRenderer.setProjectionMatrix(cam.combined);

        // y-Down coordinate system for shapeRenderer and spriteBatch
        // Careful for drawing texts and circles

        // Draw diamonds
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Array<Diamond> arr : gameBoard.getDiamonds()) {
            for (Diamond diamond : arr) {
                shapeRenderer.setColor(diamond.getColor());
                float x = diamond.getX();
                float y = diamond.getY();
                shapeRenderer.circle(x, y, diamond.getRadius());
            }
        }
        shapeRenderer.end();

        //Draw texts
        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(cam.combined);
        StringBuilder timeText = new StringBuilder("TIME = ")
                .append((int) gameBoard.getCurrentTime())
                .append(" : ")
                .append((int) gameBoard.getLevelTimeLimit());
        timeWrapper.setText(timeText.toString());
        timeWrapper.draw(spriteBatch, TextWrapper.Align.CENTER);

        float verticalGap = ((gameHeight * gameBoard.getRatioOfText()) - groupSize * timeWrapper.getHeight()) / (groupSize + 1);
        circle.clear();

        // Writing text of groups
        for (int i = 0; i < groupSize; i++) {
            StringBuilder groupText = new StringBuilder("Group")
                    .append(i + 1)
                    .append(" : ")
                    .append(scores[i])
                    .append(" - ")
                    .append(gameBoard.getScoreSumCondition());
            groupWrapper.setText(groupText.toString());

            float yPosText = (verticalGap + (verticalGap + groupWrapper.getHeight() / 3) * i);
            float textWidth = groupWrapper.getWidth();
            float textHeight = groupWrapper.getHeight();
            float radius = textHeight / 2;
            float ballGap = (gameWidth - textHeight * groupSize - textWidth) / (groupSize + 4);
            float x = textWidth + ballGap * 3 + radius;

            for (int j = 0; j < groupSize; j++) {
                // Used Vector3 for storing pos_x, pos_y, radius
                circle.add(new Vector3(x + (textHeight + ballGap) * j, yPosText + radius * 3f, radius));
            }
            groupWrapper.setPosition(new Vector2(ballGap * 2, yPosText));
            groupWrapper.draw(spriteBatch, TextWrapper.Align.LEFT);
        }
        spriteBatch.end();

        // Draw colors in the game
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Iterator<Vector3> circleIterator = circle.iterator();
        for (Set<MyColor> set : gameBoard.getColors()) {
            for (MyColor color : set) {
                Vector3 c = circleIterator.next();
                shapeRenderer.setColor(color);
                shapeRenderer.circle(c.x, c.y, c.z);
            }
        }
        shapeRenderer.end();
    }

    public void dispose() {
        timeFont.dispose();
        generator.dispose();
        groupFont.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
