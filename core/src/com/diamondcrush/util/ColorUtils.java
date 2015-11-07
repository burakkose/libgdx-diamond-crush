package com.diamondcrush.util;

import com.diamondcrush.util.kmeans.Cluster;

import java.util.*;

public class ColorUtils {

    private static final Random rand = new Random();

    private ColorUtils() {
    }

    public static List<Set<MyColor>> getRandomColors(List<Cluster> clusters, int number) {
        List<Set<MyColor>> colorSetList = new ArrayList<Set<MyColor>>();
        for (Cluster cluster : clusters) {
            Set<MyColor> colorSet = new HashSet<MyColor>();
            List<MyColor> colors = cluster.getColors();

            for (int i = 0; i < number; i++) {
                MyColor color = colors.get(rand.nextInt(colors.size()));
                if (colorSet.contains(color))
                    i--;
                colorSet.add(color);
            }

            colorSetList.add(colorSet);
        }
        return colorSetList;
    }

    public static MyColor getRandomColor(List<MyColor> colors) {
        return colors.get(rand.nextInt(colors.size()));
    }

    public static MyColor getColor(String r, String g, String b) {
        float r_ = Integer.valueOf(r, 16);
        float g_ = Integer.valueOf(g, 16);
        float b_ = Integer.valueOf(b, 16);
        return new MyColor(r_ / 255, g_ / 255, b_ / 255);
    }

    public static MyColor getRandomColor() {
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new MyColor(r, g, b);
    }
}
