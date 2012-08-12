package com.icode.view.chart;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The class defining the Path points
 */
public class Path {

    private GeneralPath path = null;
    private int max = 0;
    private int min = 0;
    private Queue<Point> points = null;

    public Path() {
        this.points = new LinkedList<Point>();
    }

    public Path(GeneralPath path, int min, int max) {
        this();
        this.path = path;
        this.max = max;
        this.min = min;
    }

    public Path(GeneralPath path, int min, int max, Point points) {
        this(path, min, max);
        this.points.add(points);
    }

    public Path(GeneralPath path, int min, int max, Queue<Point> points) {
        this(path, min, max);
        this.points = (points);
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public GeneralPath getPath() {
        return path;
    }

    /**
     * @return the points
     */
    public Queue<Point> getPoints() {
        return points;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @param path the path to set
     */
    public void setPath(GeneralPath path) {
        this.path = path;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(Queue<Point> points) {
        this.points = points;
    }    
}
