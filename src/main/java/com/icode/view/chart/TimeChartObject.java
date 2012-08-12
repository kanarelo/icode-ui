/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icode.view.chart;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

/**
 *
 * @author Nes
 */
public class TimeChartObject {

    private String graphTitle = "";
    private String y_AxisText = "";
    private String x_AxisText = "";
    private int size = 0;
    private int gap = 0;
    //Wait, order matters here, not just any list
    //FIFO is the policy here; Hence Queue.
    private Queue<Path> chartPaths = null;

    public TimeChartObject() {
        chartPaths = new LinkedList<Path>();
    }

    public TimeChartObject(int size) {
        chartPaths = new LinkedList<Path>();
        this.size = size;
    }

    public TimeChartObject(String graphTitle, String x_AxisText, String y_AxisText) {
        this.graphTitle = graphTitle;
        this.x_AxisText = x_AxisText;
        this.y_AxisText = y_AxisText;
        this.chartPaths = new LinkedList<Path>();
    }

    public TimeChartObject(String graphTitle, String x_AxisText, String y_AxisText, int size) {
        this(graphTitle, x_AxisText, y_AxisText);
        this.size = size;
    }

    public TimeChartObject(String graphTitle, String x_AxisText, String y_AxisText, Queue<Path> chartPaths) {
        this(graphTitle, x_AxisText, y_AxisText);
        this.chartPaths = chartPaths;
    }

    /**
     * @return the graphTitle
     */
    public String getGraphTitle() {
        return graphTitle;
    }

    /**
     * @return the x_AxisText
     */
    public String getX_AxisText() {
        return x_AxisText;
    }

    /**
     * @return the y_AxisText
     */
    public String getY_AxisText() {
        return y_AxisText;
    }

    /**
     * @param graphTitle the graphTitle to set
     */
    public void setGraphTitle(String graphTitle) {
        this.graphTitle = graphTitle;
    }

    /**
     * @param x_AxisText the x_AxisText to set
     */
    public void setX_AxisText(String x_AxisText) {
        this.x_AxisText = x_AxisText;
    }

    /**
     * @param y_AxisText the y_AxisText to set
     */
    public void setY_AxisText(String y_AxisText) {
        this.y_AxisText = y_AxisText;
    }

    /**
     * @return the chartPaths
     */
    public Queue<Path> getChartPaths() {
        return chartPaths;
    }

    /**
     * @param chartPaths the chartPaths to set
     */
    public void setChartPaths(Queue<Path> chartPaths) {
        this.chartPaths = chartPaths;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }
    private static final Logger LOG = Logger.getLogger(TimeChartObject.class.getName());

    /**
     * @return the gap
     */
    public int getGap() {
        return gap;
    }

    /**
     * @param gap the gap to set
     */
    public void setGap(int gap) {
        this.gap = gap;
    }

    /**
     * 
     */
    public void drawPathFromPoints() {
        Iterator<Path> it = this.chartPaths.iterator();
        while (it.hasNext()) {
            Path path = it.next();
            Iterator<Point> pointsIT = path.getPoints().iterator();
            int i = 0;
            while (pointsIT.hasNext()) {
                if (i == 0) {
                    Point p = pointsIT.next();
                    path.getPath().moveTo(p.x, p.y);
                } else {
                    Point p = pointsIT.next();
                    path.getPath().lineTo(p.x, p.y);
                }
                i++;
            }
        }
    }
}
