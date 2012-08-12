/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icode.view.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Nes
 */
public class DecoratedJTable extends JTable {

    private java.awt.Color rowColors[] = new java.awt.Color[2];
    private boolean drawStripes = false;

    /**
     *
     */
    public DecoratedJTable() {
    }

    /**
     *
     * @param numRows
     * @param numColumns
     */
    public DecoratedJTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    /**
     *
     * @param rowData
     * @param columnNames
     */
    public DecoratedJTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    /**
     *
     * @param dataModel
     */
    public DecoratedJTable(TableModel dataModel) {
        super(dataModel);
    }

    /**
     *
     * @param dataModel
     * @param columnModel
     */
    public DecoratedJTable(TableModel dataModel, TableColumnModel columnModel) {
        super(dataModel, columnModel);
    }

    /**
     *
     * @param dataModel
     * @param columnModel
     * @param selectionModel
     */
    public DecoratedJTable(TableModel dataModel,  TableColumnModel columnModel, ListSelectionModel selectionModel) {
        super(dataModel, columnModel, selectionModel);
    }

    /**
     *
     * @param rowData
     * @param columnNames
     */
    public DecoratedJTable(Vector<?> rowData, Vector<?> columnNames) {
        super(rowData, columnNames);
    }

    /** Add stripes between cells and behind non-opaque cells. */
    @Override
    public void paintComponent(Graphics g) {
        if (!(drawStripes = isOpaque())) {
            super.paintComponent(g);
            return;
        }

        // Paint zebra background stripes
        updateZebraColors();
        final java.awt.Insets insets = getInsets();
        final int w = getWidth() - insets.left - insets.right;
        final int h = getHeight() - insets.top - insets.bottom;
        final int x = insets.left;
        int y = insets.top;
        int rowHeights = 16; // A default for empty tables
        final int nItems = getRowCount();
        for (int i = 0; i < nItems; i++, y += rowHeights) {
            rowHeights = getRowHeight(i);
            g.setColor(rowColors[i & 1]);
            g.fillRect(x, y, w, rowHeights);
        }
        // Use last row height for remainder of table area
        final int nRows = nItems + (insets.top + h - y) / rowHeights;
        for (int i = nItems; i < nRows; i++, y += rowHeights) {
            g.setColor(rowColors[i & 1]);
            g.fillRect(x, y, w, rowHeights);
        }
        final int remainder = insets.top + h - y;
        if (remainder > 0) {
            g.setColor(rowColors[nRows & 1]);
            g.fillRect(x, y, w, remainder);
        }

        // Paint component
        setOpaque(false);
        super.paintComponent(g);
        setOpaque(true);

    }

    /** Add background stripes behind rendered cells.
     * @param col
     */
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
        final Component c = super.prepareRenderer(renderer, row, col);
        if (drawStripes && !isCellSelected(row, col)) {
            c.setBackground(rowColors[row & 1]);
        }
        return c;
    }

    /** Add background stripes behind edited cells.
     * @param col
     */
    @Override
    public Component prepareEditor(TableCellEditor editor, int row, int col) {
        final Component c = super.prepareEditor(editor, row, col);
        if (drawStripes && !isCellSelected(row, col)) {
            c.setBackground(rowColors[row & 1]);
        }
        return c;
    }

    /** Force the table to fill the viewport's height. */
    @Override
    public boolean getScrollableTracksViewportHeight() {
        final Component p = getParent();
        if (!(p instanceof javax.swing.JViewport)) {
            return false;
        }
        return ((javax.swing.JViewport) p).getHeight() > getPreferredSize().height;
    }

    /** Compute zebra background stripe colors. */
    private void updateZebraColors() {
        if ((rowColors[0] = getBackground()) == null) {
            rowColors[0] = rowColors[1] = Color.white;
            return;
        }
        final Color sel = getSelectionBackground();
        if (sel == null) {
            rowColors[1] = rowColors[0];
            return;
        }
        final float[] bgHSB = Color.RGBtoHSB(
                rowColors[0].getRed(),
                rowColors[0].getGreen(),
                rowColors[0].getBlue(),
                null);
        final float[] selHSB = Color.RGBtoHSB(
                sel.getRed(),
                sel.getGreen(),
                sel.getBlue(),
                null);
        rowColors[1] = Color.getHSBColor(
                (selHSB[1] == 0.0 || selHSB[2] == 0.0) ? bgHSB[0] : selHSB[0],
                0.1f * selHSB[1] + 0.9f * bgHSB[1],
                bgHSB[2] + ((bgHSB[2] < 0.5f) ? 0.05f : -0.05f));
    }
}
