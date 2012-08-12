/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.tree;

import java.awt.Component;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Advanced tree component
 */
public class Tree extends JTree {

    private List<Node> roots;

    /**
     * Creates a tree component
     */
    public Tree() {
        Model model = new Model();
        setModel(model);
        setCellRenderer(model);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setRootVisible(false);
        setShowsRootHandles(true);
    }

    /**
     * Set the root node(s) of the tree
     * @param roots the root node(s)
     */
    public void setRoots(List<Node> roots) {
        this.roots = roots;
        for (TreeModelListener listener : listenerList.getListeners(TreeModelListener.class)) {
            listener.treeStructureChanged(new TreeModelEvent(this, new Object[]{this}));
        }
    }

    /**
     * Collapses all the opened tree nodes
     */
    public void collapseAll() {
        if (roots != null) {
            for (Node root : roots) {
                collapse(new TreePath(new Object[]{this, root}));
            }
        }
    }

    private void collapse(TreePath path) {
        Node node = (Node) path.getLastPathComponent();
        if (node.initialized) {
            TreeModel model = getModel();
            for (int i = 0, n = model.getChildCount(node); i < n; i++) {
                collapse(path.pathByAddingChild(model.getChild(node, i)));
            }
        }
        if (isExpanded(path)) {
            collapsePath(path);
        }
    }

    private class Model implements TreeModel, TreeCellRenderer {

        public Object getRoot() {
            return this;
        }

        public boolean isLeaf(Object node) {
            return (node == this) ? ((roots == null) || (roots.size() == 0))
                    : !((Node) node).hasItems();
        }

        public int getChildCount(Object parent) {
            return (parent == this) ? ((roots != null) ? roots.size() : 0)
                    : ((Node) parent).getItemCount();
        }

        public Object getChild(Object parent, int index) {
            return (parent == this) ? roots.get(index) : ((Node) parent).getItem(index);
        }

        public int getIndexOfChild(Object parent, Object child) {
            for (int i = 0, n = getChildCount(parent); i < n; i++) {
                if (getChild(parent, i) == child) {
                    return i;
                }
            }
            return -1;
        }

        public void valueForPathChanged(TreePath path, Object value) {
        }

        public void addTreeModelListener(TreeModelListener listener) {
            listenerList.add(TreeModelListener.class, listener);
        }

        public void removeTreeModelListener(TreeModelListener listener) {
            listenerList.remove(TreeModelListener.class, listener);
        }
        private DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean selected, boolean expanded, boolean leaf, int row, boolean focused) {
            renderer.getTreeCellRendererComponent(tree,
                    null, selected, expanded, leaf, row, focused);
            renderer.setIcon(null);
            if ((value != null) && (value != this)) {
                ((Node) value).render(renderer);
            }
            return renderer;
        }
    }
}
