package com.icode.view.table;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.icode.resources.ResourceUtils;

public class TableRenderer extends JComponent implements ListCellRenderer, ListDataListener {

	private static final long serialVersionUID = 1L;
	private Cell[] cells;
    private transient Dimension size;
    private transient int index;
    private transient boolean selected, focused;
    private transient Object value;
    private final Color obg = new Color(0xedf3fe),
            bg = UIManager.getColor("List.background"),
            sbg = UIManager.getColor("List.selectionBackground"),
            fg = UIManager.getColor("List.foreground"),
            sfg = UIManager.getColor("List.selectionForeground");
	private Table<?> table;

    public TableRenderer(Table<?> table, Class<?> clazz, Cell... cells) {
        this.cells = cells;
        table.getModel().addListDataListener(this);
        
        this.table = table;

        for (Cell cell : cells) {
            cell.init(clazz);
        }
    }

    Cell getLast() {
        return (cells.length > 0) ? cells[0] : null;
    }

    private void checkLayout() {
        if (size != null) {
            return;
        }

        int[] columns = new int[cells.length], rows = new int[cells.length];
        int columnCount = 0, rowCount = 0;
        for (int i = 0, x = 0, y = 0; i < cells.length; i++) {
            if (cells[i].isNewLine()) {
                columns[i] = 0;
                x = 1;
                y++;
                rows[i] = y;
            } else {
                columns[i] = x;
                x++;
                rows[i] = y;
            }
            columnCount = Math.max(columnCount, columns[i] + 1);
            rowCount = Math.max(rowCount, rows[i] + 1);
        }

        int[] widths = new int[columnCount], heights = new int[rowCount];
        for (int i = 0; i < cells.length; i++) {
            Dimension d = cells[i].getSize(this.table);
            widths[columns[i]] = Math.max(widths[columns[i]], d.width);
            heights[rows[i]] = Math.max(heights[rows[i]], d.height);
        }

        int[] xs = new int[columnCount], ys = new int[rowCount];
        int w = 0, h = 0;
        for (int i = 0, x = 8; i < xs.length; i++) {
            xs[i] = x;
            x += widths[i] + 8;
            w = x;
        }
        for (int i = 0, y = 4; i < ys.length; i++) {
            ys[i] = y;
            y += heights[i];
            h = y;
        }

        for (int i = 0; i < cells.length; i++) {
            cells[i].setBounds(xs[columns[i]], ys[rows[i]], widths[columns[i]], heights[rows[i]]);
        }

        size = new Dimension(w, h + 4);
    }

    @Override
    public Dimension getPreferredSize() {
        checkLayout();
        return size;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = ResourceUtils.init(g);
        int w = getWidth(), h = getHeight();
        g.setColor(selected ? sbg : (index % 2 == 0) ? bg : obg);
        g.fillRect(0, 0, w, h - 1);
        g.setColor(new Color(0xe0e0e0));
        g.drawLine(0, h - 1, w - 1, h - 1);

        if (focused) {
            g.setColor(Color.WHITE);
            Stroke stroke = g2.getStroke();
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE,
                    BasicStroke.JOIN_MITER, 10, new float[]{1, 2}, 0));
            g.drawRect(1, 1, w - 3, h - 4);
            g2.setStroke(stroke);
        }

        checkLayout();

        g.setColor(new Color(0x20000000, true));

        g.setColor(selected ? sfg : fg);
        for (Cell item : cells) {
            item.render(this, g2, value, selected);
        }
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean selected, boolean focused) {
        this.index = index;
        this.selected = selected;
        this.focused = focused;
        this.value = value;
        return this;
    }

    Cell getRendererItemAt(int x, int y) {
        for (Cell item : cells) {
            if (item.isInside(x, y)) {
                return item;
            }
        }
        return null;
    }

    public void contentsChanged(ListDataEvent e) {
        size = null;
    }

    public void intervalAdded(ListDataEvent e) {
        size = null;
    }

    public void intervalRemoved(ListDataEvent e) {
        size = null;
    }
}