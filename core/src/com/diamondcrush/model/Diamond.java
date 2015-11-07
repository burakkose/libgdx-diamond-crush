package com.diamondcrush.model;

import com.badlogic.gdx.math.Circle;
import com.diamondcrush.util.MyColor;

public class Diamond {

    private final Circle circle;
    private MyColor color;
    private Diamond rightNeighbor;
    private Diamond leftNeighbor;
    private Diamond aboveNeighbor;
    private Diamond belowNeighbor;

    public Diamond(float x, float y, float radius) {
        this.circle = new Circle(x, y, radius);
    }

    public float getX() {
        return circle.x;
    }

    public float getY() {
        return circle.y;
    }

    public float getRadius() {
        return circle.radius;
    }

    public MyColor getColor() {
        return color;
    }

    public void setColor(MyColor color) {
        this.color = color;
    }

    public Diamond getRightNeighbor() {
        return rightNeighbor;
    }

    public void setRightNeighbor(Diamond rightNeighbor) {
        this.rightNeighbor = rightNeighbor;
    }

    public Diamond getLeftNeighbor() {
        return leftNeighbor;
    }

    public void setLeftNeighbor(Diamond leftNeighbor) {
        this.leftNeighbor = leftNeighbor;
    }

    public Diamond getBelowNeighbor() {
        return belowNeighbor;
    }

    public void setBelowNeighbor(Diamond belowNeighbor) {
        this.belowNeighbor = belowNeighbor;
    }

    public Diamond getAboveNeighbor() {
        return aboveNeighbor;
    }

    public void setAboveNeighbor(Diamond aboveNeighbor) {
        this.aboveNeighbor = aboveNeighbor;
    }

    public void swapColor(Diamond diamond) {
        MyColor temp = diamond.color;
        diamond.color = color;
        color = temp;
    }

    public boolean equalsByCluster(Diamond diamond) {
        return this.color.getClusterNumber() == diamond.getColor().getClusterNumber();
    }

    public int getClusterID() {
        return this.color.getClusterNumber();
    }

    public boolean isNeighbor(Diamond diamond) {
        return this.getAboveNeighbor() == diamond
                || this.getBelowNeighbor() == diamond
                || this.getRightNeighbor() == diamond
                || this.getLeftNeighbor() == diamond;
    }

    public boolean checkContain(float x, float y) {
        return circle.contains(x, y);
    }


}
