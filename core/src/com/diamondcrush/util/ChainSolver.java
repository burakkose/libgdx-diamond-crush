package com.diamondcrush.util;

import com.badlogic.gdx.utils.Array;
import com.diamondcrush.model.Diamond;

import java.util.List;

public class ChainSolver {

    private final Array<Array<Diamond>> diamonds;

    public ChainSolver(Array<Array<Diamond>> diamonds) {
        this.diamonds = diamonds;
    }

    public boolean isChainOccur() {
        return findChain() != null;
    }

    public Array<Diamond> findChain() {
        Array<Diamond> currentChain = new Array<Diamond>();
        Array<Diamond> usedDiamonds = new Array<Diamond>();

        for (Array<Diamond> arr : diamonds) {
            for (Diamond currentDiamond : arr) {
                if (!usedDiamonds.contains(currentDiamond, true)) {
                    currentChain.clear();
                    buildChain(usedDiamonds, currentChain, currentDiamond);
                    currentChain = checkChain(currentChain);

                    if (currentChain.size >= 3) {
                        return currentChain;
                    }
                }
            }
        }

        return null;
    }

    public void deleteChain(Array<Diamond> chain, List<MyColor> colorsForGame, int[] scores) {
        for (Diamond currentBlock : chain) {
            Diamond point = currentBlock;

            if (scores != null)
                scores[point.getClusterID()]++;

            while (point.getAboveNeighbor() != null) {
                point.swapColor(point.getAboveNeighbor());
                point = point.getAboveNeighbor();
            }

            point.setColor(ColorUtils.getRandomColor(colorsForGame));
        }
    }

    private Array<Diamond> checkChain(Array<Diamond> chain) {
        Array<Diamond> checkedDiamondList = new Array<Diamond>();

        for (Diamond currentDiamond : chain) {
            if (!checkedDiamondList.contains(currentDiamond, true)) {
                Array<Diamond> currentRow = new Array<Diamond>();
                Diamond point = currentDiamond;

                while (point.getAboveNeighbor() != null) {
                    point = point.getAboveNeighbor();
                }

                // Vertical check
                while (point != null) {
                    if (chain.contains(point, true)) {
                        currentRow.add(point);
                    } else {
                        if (currentRow.size >= 3) {
                            checkedDiamondList.addAll(currentRow);
                        }
                        currentRow.clear();
                    }
                    point = point.getBelowNeighbor();
                }

                if (currentRow.size >= 3) {
                    checkedDiamondList.addAll(currentRow);
                }

                currentRow.clear();

                // Horizontal check
                point = currentDiamond;
                while (point.getLeftNeighbor() != null) {
                    point = point.getLeftNeighbor();
                }

                while (point != null) {
                    if (chain.contains(point, true)) {
                        currentRow.add(point);
                    } else {
                        if (currentRow.size >= 3) {
                            checkedDiamondList.addAll(currentRow);
                        }
                        currentRow.clear();
                    }
                    point = point.getRightNeighbor();
                }

                if (currentRow.size >= 3) {
                    checkedDiamondList.addAll(currentRow);

                }
                currentRow.clear();
            }
        }

        return checkedDiamondList;
    }

    private void buildChain(Array<Diamond> usedDiamonds, Array<Diamond> currentChain, Diamond diamond) {
        if (diamond == null)
            return;

        usedDiamonds.add(diamond);

        Diamond temp = getNeighborForChain(currentChain, diamond, diamond.getRightNeighbor());
        buildChain(usedDiamonds, currentChain, temp);

        temp = getNeighborForChain(currentChain, diamond, diamond.getLeftNeighbor());
        buildChain(usedDiamonds, currentChain, temp);

        temp = getNeighborForChain(currentChain, diamond, diamond.getAboveNeighbor());
        buildChain(usedDiamonds, currentChain, temp);

        temp = getNeighborForChain(currentChain, diamond, diamond.getBelowNeighbor());
        buildChain(usedDiamonds, currentChain, temp);
    }

    private Diamond getNeighborForChain(Array<Diamond> currentChain, Diamond currentDiamond, Diamond neighborDiamond) {

        if (neighborDiamond != null
                && currentDiamond.equalsByCluster(neighborDiamond)
                && !currentChain.contains(neighborDiamond, true)) {
            currentChain.add(neighborDiamond);
            return neighborDiamond;
        }

        return null;
    }
}
