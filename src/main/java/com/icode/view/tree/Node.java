/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Tree node
 */
public class Node {

    private Icon icon;
    private String text;
    private Node parent;
    private List<Node> items;
    boolean initialized;

    /**
     * Creates a new node rendered with the given icon and text
     * @param icon an icon for the rendering, can be null
     * @param text a text for the rendering, can be null
     */
    protected Node(Icon icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    /**
     * Adds a new child node to this node
     * @param item a new node
     */
    public void add(Node item) {
        if (items == null) {
            items = new ArrayList<Node>();
        }
        item.parent = this;
        items.add(item);
    }

    void render(DefaultTreeCellRenderer renderer) {
        renderer.setIcon(icon);
        renderer.setText(text);
    }

    /**
     * Returns the parent node
     * @return the parent node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Overwrite this method to lazy load child nodes
     * @return true if it (may) has child nodes
     * @see #initialize()
     */
    protected boolean hasItems() {
        return (items != null) && (items.size() > 0);
    }

    /**
     * Overwrite this method to lazy add child nodes to this node
     * @see #hasItems()
     */
    protected void initialize() {
    }

    int getItemCount() {
        if (!initialized) {
            initialized = true;
            initialize();
        }
        return (items != null) ? items.size() : 0;
    }

    Node getItem(int index) {
        return items.get(index);
    }
}
