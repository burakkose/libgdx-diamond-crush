package com.diamondcrush.util.kmeans;

import com.diamondcrush.util.MyColor;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private final int id;
    private final List<MyColor> colors;
    private MyColor centroid;

    public Cluster(int id) {
        this.id = id;
        this.colors = new ArrayList<MyColor>();
        this.centroid = null;
    }

    public List<MyColor> getColors() {
        return colors;
    }

    public void addPoint(MyColor MyColor) {
        colors.add(MyColor);
    }

    public MyColor getCentroid() {
        return centroid;
    }

    public void setCentroid(MyColor centroid) {
        this.centroid = centroid;
    }

    public void clear() {
        colors.clear();
    }

}