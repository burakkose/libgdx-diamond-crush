package com.diamondcrush.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class MyColor extends Color {

    private int clusterNumber = 0;

    public MyColor(float r, float g, float b) {
        super(r, g, b, 0);
    }

    //Calculates the distance between three points.
    public static double distance(MyColor c, MyColor centroid) {

        return Math.sqrt(Math.pow(centroid.r - c.r, 2) + Math.pow(centroid.g - c.g, 2)
                + Math.pow(centroid.b - c.b, 2));
    }

    public static List<MyColor> readFromAssest() {
        List<MyColor> result = new ArrayList<MyColor>();
        FileHandle file = Gdx.files.internal("colors.txt");
        String[] lines = file.readString().split("\n");
        for (String line : lines) {
            String[] RGB = line.split(" ");
            result.add(ColorUtils.getColor(RGB[0], RGB[1], RGB[2]));
        }
        return result;
    }

    public int getClusterNumber() {
        return this.clusterNumber;
    }

    public void setClusterNumber(int n) {
        this.clusterNumber = n;
    }
}