/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.component.fields;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.icode.resources.ResourceUtils;

/**
 * Field to type or choose a directory from the file system
 */
public class DirectoryField extends AbstractField {

    private String[] extensions;
    private Chooser chooser;

    /**
     * Creates an empty directory field
     */
    public DirectoryField() {
        setColumns(18);
        Listener listener = new Listener();
    }

    /**
     * Creates a directory field
     * @param text a path to a directory
     */
    public DirectoryField(String text) { //, String... extensions
        this();
        setText(text);
//		this.extensions = extensions;
    }

    /**
     * Overwrites the method to paint an arrow to pop up a chooser
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ResourceUtils.init(g);

        g.setColor(Color.darkGray);
        Insets is = getInsets();
        int x = getWidth() - 8 - is.right, y = is.top + (getHeight() - 8 - is.top - is.bottom) / 2;
        g.fillPolygon(new int[]{x, x + 8, x + 4}, (chooser == null)
                ? new int[]{y, y, y + 8} : new int[]{y + 8, y + 8, y}, 3);
    }

    private void popup() {
        chooser = new Chooser();
        Component popup = new JScrollPane(chooser);
        popup.setPreferredSize(new Dimension(getWidth(), 320));

        Window window = SwingUtilities.windowForComponent(this);
        JRootPane rootpane = getRootPane();
        JLayeredPane layeredpane = rootpane.getLayeredPane();

        Point t = SwingUtilities.convertPoint(this, 0, getHeight(), layeredpane);
        Dimension d = popup.getPreferredSize();
        popup.setBounds(t.x, t.y, d.width, Math.min(d.height, layeredpane.getHeight() - t.y));
        layeredpane.add(popup, JLayeredPane.POPUP_LAYER, 0);
        selectAll();
        requestFocus();

        layeredpane.addMouseListener(chooser);
        rootpane.addComponentListener(chooser);
        window.addWindowFocusListener(chooser);
        repaint();
    }

    private class Chooser extends JTree
            implements TreeModel, TreeCellRenderer, TreeSelectionListener, Runnable,
            ComponentListener, WindowFocusListener, MouseListener {

        private FileSystemView view;
        private Node root;
        private DefaultTreeCellRenderer renderer;

        Chooser() {
            view = FileSystemView.getFileSystemView();
            root = new Node();

            setRootVisible(false);
            setShowsRootHandles(true);
            setModel(this);
            renderer = new DefaultTreeCellRenderer();
            setCellRenderer(this);

            setFocusable(false);
            SwingUtilities.invokeLater(this);
        }

        public void run() {
            expand();
            getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            addTreeSelectionListener(this);
        }

        private class Node {

            private File file;
            private Icon icon;
            private String text;
            private Node[] nodes;

            private Node() {
                nodes = getNodes(view.getRoots());
            }

            private Node(File file) {
                this.file = file;
            }

            private int getChildCount() {
                if (nodes == null) {
                    nodes = getNodes(view.getFiles(file, true));
                }
                return nodes.length;
            }

            private Node getChild(int index) {
                return nodes[index];
            }

            private boolean isLeaf() {
                // getChildCount(node) == 0
                return false; //((extensions == null) || (file == null)) ? false : !file.isDirectory();
            }

            private void render() {
                if (file != null) {
                    if (icon == null) {
                        icon = view.getSystemIcon(file);
                    }
                    if (text == null) {
                        text = view.getSystemDisplayName(file);
                    }
                }
                renderer.setIcon(icon);
                renderer.setText(text);
            }

            private Node getChild(File subfile) {
                for (int i = 0, n = getChildCount(); i < n; i++) {
                    Node node = getChild(i);
                    if (node.file.equals(subfile)) {
                        return node;
                    }
                }
                return null;
            }
        }

        private Node[] getNodes(File[] files) {
            ArrayList<Node> list = new ArrayList<Node>();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    list.add(new Node(files[i]));
                }
                /*else if (extensions != null) {
                for (int j = 0; j < extensions.length; j++) {
                if (files[i].getName().endsWith(extensions[j])) {
                list.add(new Node(files[i])); break;
                }
                }
                }*/
            }
            return list.<Node>toArray(new Node[list.size()]);
        }

        private void expand() {
            File file = new File(getText());
            if (file.exists()) {
                ArrayList<Node> path = new ArrayList<Node>();
                expand(path, file, true);
            }
        }

        private Node expand(ArrayList<Node> path, File file, boolean last) {
            Node node = root;
            if (file != null) {
                Node parentnode = expand(path, view.getParentDirectory(file), false);
                node = parentnode.getChild(file);

            }
            path.add(node);
            TreePath treepath = new TreePath(path.toArray());
            expandPath(treepath);
            if (last) {
                setSelectionPath(treepath);
                scrollPathToVisible(treepath);
            }
            return node;
        }

        public Object getRoot() {
            return root;
        }

        public Object getChild(Object parent, int index) {
            return ((Node) parent).getChild(index);
        }

        public int getChildCount(Object parent) {
            return ((Node) parent).getChildCount();
        }

        public boolean isLeaf(Object node) {
            return ((Node) node).isLeaf();
        }

        public int getIndexOfChild(Object parent, Object child) {
            for (int i = getChildCount(parent) - 1; i >= 0; i--) {
                if (getChild(parent, i) == child) {
                    return i;
                }
            }
            return -1;
        }

        public void addTreeModelListener(TreeModelListener l) {
            listenerList.add(TreeModelListener.class, l);
        }

        public void removeTreeModelListener(TreeModelListener l) {
            listenerList.remove(TreeModelListener.class, l);
        }

        public void valueForPathChanged(TreePath path, Object value) {
        }

        public Component getTreeCellRendererComponent(JTree tree,
                Object value, boolean selected, boolean expanded, boolean leaf,
                int row, boolean focused) {
            if (value != this) {
                renderer.getTreeCellRendererComponent(tree,
                        null, selected, expanded, leaf, row, focused);
                ((Node) value).render();
            }
            return renderer;
        }

        public void valueChanged(TreeSelectionEvent e) {
            Node node = (Node) e.getPath().getLastPathComponent();
            if ((node != null) && ((extensions == null) == node.file.isDirectory())
                    && view.isFileSystem(node.file)) {
                setText(node.file.getAbsolutePath());
                close();
            }
        }

        public void componentResized(ComponentEvent e) {
        }

        public void componentMoved(ComponentEvent e) {
            close();
        }

        public void componentShown(ComponentEvent e) {
        }

        public void componentHidden(ComponentEvent e) {
        }

        public void windowGainedFocus(WindowEvent e) {
        }

        public void windowLostFocus(WindowEvent e) {
            close();
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            close();
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        private void close() {
            Container scrollpane = getParent();
            if (scrollpane == null) {
                return;
            }
            while (!(scrollpane instanceof JScrollPane)) {
                scrollpane = scrollpane.getParent();
            }
            JLayeredPane layerepane = (JLayeredPane) scrollpane.getParent();
            JFrame rootpane = (JFrame) SwingUtilities.windowForComponent(this);

            layerepane.removeMouseListener(this);
            rootpane.removeComponentListener(this);
            rootpane.removeWindowFocusListener(this);
            layerepane.repaint(scrollpane.getBounds());
            layerepane.remove(scrollpane);
            chooser = null;
            DirectoryField.this.repaint();
        }
    }

    private class Listener implements FocusListener, MouseListener {

        private Listener() {
            addFocusListener(this);
            addMouseListener(this);
        }

        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
            if (chooser != null) {
                chooser.close();
            }
        }

        public void mousePressed(MouseEvent e) {
            if (e.getX() > getWidth() - getInsets().right - 8) {
                if (chooser == null) {
                    popup();
                } else {
                    chooser.close();
                }
            }
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
}
