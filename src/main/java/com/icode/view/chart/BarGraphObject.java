/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icode.view.chart;

import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @param <A> A for "Axis", 
 * @param <V>
 * @author Nes
 */
public class BarGraphObject<A extends Axis, V> {

    /**
     * In a JSON/Python Dictionary context, The data would be like:
     * e.g:
     *      "Items":{
     *          "2010":{
     *              "Beans":{"x":34.3,"y":4.3},
     *              "Maize":{"x":36.3,"y":5.5},
     *          },
     *          "2011":{
     *              "Beans":{"x":43.3,"y":4.3},
     *              "Maize":{"x":12.9,"y":8.5},
     *          },
     *      }
     */
    private Map<String, Map<String, Map<A, V>>> items;
    private int width;
    private String graphTitle = "";
    private String y_AxisText = "";
    private String x_AxisText = "";
    private int size = 0;
    private int gap = 0;

    public BarGraphObject(Map<String, Map<String, Map<A, V>>> items) {
        this.items = items;
    }

    public BarGraphObject(String graphTitle, String x_AxisText, String y_AxisText) {
        this.graphTitle = graphTitle;
        this.x_AxisText = x_AxisText;
        this.y_AxisText = y_AxisText;
        this.items = new java.util.LinkedHashMap<String, Map<String, Map<A, V>>>(2);
    }

    public BarGraphObject(String graphTitle, String x_AxisText, String y_AxisText, int size) {
        this(graphTitle, x_AxisText, y_AxisText);
        this.size = size;
    }

    /**
     * @return the items
     */
    public Map<String, Map<String, Map<A, V>>> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(Map<String, Map<String, Map<A, V>>> items) {
        this.items = items;
    }

    public void setItem(String key, Map<String, Map<A, V>> item) {
        this.items.put(key, item);
    }
    
    private static final Logger LOG = Logger.getLogger(BarGraphObject.class.getName());

    /**
     * @return the graphTitle
     */
    public String getGraphTitle() {
        return graphTitle;
    }

    /**
     * @param graphTitle the graphTitle to set
     */
    public void setGraphTitle(String graphTitle) {
        this.graphTitle = graphTitle;
    }

    /**
     * @return the y_AxisText
     */
    public String getY_AxisText() {
        return y_AxisText;
    }

    /**
     * @param y_AxisText the y_AxisText to set
     */
    public void setY_AxisText(String y_AxisText) {
        this.y_AxisText = y_AxisText;
    }

    /**
     * @return the x_AxisText
     */
    public String getX_AxisText() {
        return x_AxisText;
    }

    /**
     * @param x_AxisText the x_AxisText to set
     */
    public void setX_AxisText(String x_AxisText) {
        this.x_AxisText = x_AxisText;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

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
}
