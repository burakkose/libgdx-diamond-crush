package com.diamondcrush.world;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.diamondcrush.model.Diamond;
import com.diamondcrush.util.ChainSolver;
import com.diamondcrush.util.ColorUtils;
import com.diamondcrush.util.MyColor;
import com.diamondcrush.util.kmeans.Cluster;
import com.diamondcrush.util.kmeans.KMeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameBoard {
    private static final float LEVEL1TIME = 60f * 30;
    private static final float LEVEL2TIME = 60f * 40;
    private static final float LEVEL3TIME = 60f * 50;
    private static final float RATIO_OF_DIAMOND = 0.7f;
    private static final float DELAY_TIMER_INTERVAL = 0.7f;

    private final int[] scores;
    private final float gameHeight;
    private final float gameWidth;
    private final Timer delayTimer;
    private final ChainSolver chainSolver;
    private final Array<Array<Diamond>> diamonds;
    private final List<Cluster> clusters;
    private final List<MyColor> colorsForGame;

    private GameState gameState;
    private List<Set<MyColor>> colors;

    private int scoreSumCondition;
    private int boardRow;
    private int groupSize;
    private float currentTime = 0f;
    private float levelTimeLimit = 0f;
    private float ratioOfText;
    private boolean isLocked;


    public GameBoard(int level, float gameWidth, float gameHeight) {
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
        this.diamonds = new Array<Array<Diamond>>();

        gameState = GameState.RUNNING;
        chainSolver = new ChainSolver(diamonds);

        switch (level) {
            case 1:
                boardRow = 6;
                groupSize = 3;
                scoreSumCondition = 10;
                levelTimeLimit = LEVEL1TIME;
                break;
            case 2:
                boardRow = 9;
                groupSize = 4;
                scoreSumCondition = 15;
                levelTimeLimit = LEVEL2TIME;
                break;
            case 3:
                boardRow = 12;
                groupSize = 5;
                scoreSumCondition = 20;
                levelTimeLimit = LEVEL3TIME;
                break;
        }


        KMeans kmeans = new KMeans();
        kmeans.init();
        clusters = kmeans.calculate().subList(0, groupSize);

        colors = ColorUtils.getRandomColors(clusters, groupSize);
        colorsForGame = new ArrayList<MyColor>();
        for (Set<MyColor> set : colors)
            colorsForGame.addAll(set);

        scores = new int[groupSize];

        initBoard(); // Create diamonds and handle with chains at the beginning

        delayTimer = new Timer();
        delayTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                isLocked = false;
                if (act()) {
                    if (chainSolver.isChainOccur()) {
                        isLocked = true;
                    }
                }
            }
        }, 0, DELAY_TIMER_INTERVAL);

    }

    private boolean act() {
        boolean isUpdated = false;
        Array<Diamond> chain = chainSolver.findChain();

        if (chain != null) {
            colors = ColorUtils.getRandomColors(clusters, groupSize);
            for (Set<MyColor> set : colors)
                colorsForGame.addAll(set);
            chainSolver.deleteChain(chain, colorsForGame, scores);
            isUpdated = true;
        }

        return isUpdated;
    }

    public float getGameWidth() {
        return gameWidth;
    }

    public float getGameHeight() {
        return gameHeight;
    }

    public void update(float delta) {
        boolean isFinish = true;

        for (Integer i : scores) {
            if (i < scoreSumCondition) {
                isFinish = false;
                break;
            }
        }

        if (isFinish)
            gameState = GameState.FINISH;

        switch (gameState) {
            case RUNNING:
                currentTime += delta;
                if (currentTime >= levelTimeLimit) {
                    gameState = GameState.GAME_OVER;
                }
                break;
            case GAME_OVER:
                // Todo here
                break;
            case FINISH:
                // Todo here
                Gdx.app.exit();
                break;
        }
    }

    public Array<Array<Diamond>> getDiamonds() {
        return diamonds;
    }

    public int getScoreSumCondition() {
        return scoreSumCondition;
    }

    public void actIfPossible(Diamond diamond1, Diamond diamond2) {
        if (isLocked) return;

        diamond1.swapColor(diamond2);

        Array<Diamond> chain = chainSolver.findChain();

        if (chain == null) {
            diamond1.swapColor(diamond2);
        } else {
            isLocked = true;
            delayTimer.stop();
            delayTimer.start();
        }
    }

    private void initBoard() {

        // 5 * k is the R of diamond
        // k is a gap of between diamonds
        // g is a distance of the first or the last diamond to edge
        // minimum of g is 5 px

        float ratioOfDiamonds = RATIO_OF_DIAMOND;
        float g = 0;
        float k = 0;
        while (!(g >= 5)) {
            k = (ratioOfDiamonds * gameHeight) / (6 * boardRow - 1);
            g = (gameWidth - 35 * k) / 2f;
            ratioOfDiamonds -= 0.05f;
        }

        ratioOfText = 1 - ratioOfDiamonds;

        for (int i = 0; i < boardRow; i++) {
            Array<Diamond> row = new Array<Diamond>();
            int boardColumn = 6;
            for (int j = 0; j < boardColumn; j++) {
                float x = g + 2.5f * k + 6 * k * j;
                float y = gameHeight * (0.97f - ratioOfDiamonds) + 6 * k * i;

                Diamond diamond = new Diamond(x, y, 2.5f * k);
                diamond.setColor(ColorUtils.getRandomColor(colorsForGame));

                if (j != 0) {
                    diamond.setLeftNeighbor(row.get(j - 1));
                    row.get(j - 1).setRightNeighbor(diamond);
                    // Check two same color in the horizontal order
                    while (diamond.equalsByCluster(diamond.getLeftNeighbor())) {
                        diamond.setColor(ColorUtils.getRandomColor(colorsForGame));
                    }
                }

                if (i != 0) {
                    diamond.setAboveNeighbor(diamonds.get(i - 1).get(j));
                    diamonds.get(i - 1).get(j).setBelowNeighbor(diamond);
                }
                row.add(diamond);
            }
            diamonds.add(row);
        }

        Array<Diamond> chain = chainSolver.findChain();
        while (chain != null) {
            chainSolver.deleteChain(chain, colorsForGame, null); // We don't need to calculate scores here
            chain = chainSolver.findChain();
        }
    }

    public Diamond findDiamond(int screenX, int screenY) {
        for (Array<Diamond> row : diamonds) {
            for (Diamond diamond : row)
                if (diamond.checkContain(screenX, screenY)) {
                    return diamond;
                }
        }
        return null;
    }

    public List<Set<MyColor>> getColors() {
        return colors;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public float getLevelTimeLimit() {
        return levelTimeLimit;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public float getRatioOfText() {
        return ratioOfText;
    }

    public int[] getScores() {
        return scores;
    }

    public enum GameState {
        RUNNING, GAME_OVER, FINISH
    }
}