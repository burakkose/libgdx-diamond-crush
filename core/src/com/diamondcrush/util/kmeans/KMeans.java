package com.diamondcrush.util.kmeans;

import com.diamondcrush.util.ColorUtils;
import com.diamondcrush.util.MyColor;

import java.util.ArrayList;
import java.util.List;

public class KMeans {

    private static final int NUM_CLUSTER = 5;
    private final List<Cluster> clusters;
    private List<MyColor> colors;

    public KMeans() {
        this.colors = new ArrayList<MyColor>();
        this.clusters = new ArrayList<Cluster>();
    }

    public void init() {
        colors = MyColor.readFromAssest();

        for (int i = 0; i < NUM_CLUSTER; i++) {
            Cluster cluster = new Cluster(i);
            MyColor centroid = ColorUtils.getRandomColor();
            cluster.setCentroid(centroid);
            clusters.add(cluster);
        }
    }

    public List<Cluster> calculate() {
        boolean finish = false;

        while (!finish) {
            clearClusters();

            List<MyColor> lastCentroids = getCentroids();

            assignCluster();
            calculateCentroids();

            List<MyColor> currentCentroids = getCentroids();

            double distance = 0;
            for (int i = 0; i < lastCentroids.size(); i++) {
                distance += MyColor.distance(lastCentroids.get(i), currentCentroids.get(i));
            }

            if (distance == 0) {
                finish = true;
            }
        }
        return clusters;
    }

    private void clearClusters() {
        for (Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    private List<MyColor> getCentroids() {
        List<MyColor> centroids = new ArrayList<MyColor>(NUM_CLUSTER);
        for (Cluster cluster : clusters) {
            MyColor aux = cluster.getCentroid();
            MyColor point = new MyColor(aux.r, aux.g, aux.b);
            centroids.add(point);
        }
        return centroids;
    }

    private void assignCluster() {
        double max = Double.MAX_VALUE;
        double min;
        int cluster = 0;
        double distance;

        for (MyColor point : colors) {
            min = max;
            for (int i = 0; i < NUM_CLUSTER; i++) {
                Cluster c = clusters.get(i);
                distance = MyColor.distance(point, c.getCentroid());
                if (distance < min) {
                    min = distance;
                    cluster = i;
                }
            }
            point.setClusterNumber(cluster);
            clusters.get(cluster).addPoint(point);
        }
    }

    private void calculateCentroids() {
        for (Cluster cluster : clusters) {
            float sumR = 0;
            float sumG = 0;
            float sumB = 0;
            List<MyColor> list = cluster.getColors();
            int n_points = list.size();

            for (MyColor point : list) {
                sumR += point.r;
                sumG += point.g;
                sumB += point.b;
            }

            MyColor centroid = cluster.getCentroid();
            if (n_points > 0) {
                float newR = sumR / n_points;
                float newG = sumG / n_points;
                float newB = sumB / n_points;
                centroid.r = newR;
                centroid.g = newG;
                centroid.b = newB;
            }
        }
    }
}