package com.icode.view.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.icode.resources.ResourceUtils;
import com.icode.view.app.TipManager;
import com.icode.view.component.ComponentList.Item;

public class ComponentList<E extends Item> extends JPanel
        implements MouseListener, MouseMotionListener, MouseWheelListener, FocusListener, AdjustmentListener {

    private int size;
    private boolean focused;
    private E lead;
    private E anchor;
    private Rectangle select;
    private JScrollBar scrollBar;
    private String watermark;
    private Rectangle localObject;

    public ComponentList(int paramInt) {
        super(null);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setBackground(Color.white);
        this.size = paramInt;
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addFocusListener(this);
        this.scrollBar = new JScrollBar(1);
        this.scrollBar.addAdjustmentListener(this);
        add(this.scrollBar);
    }

    public void setWatermark(String paramString) {
        this.watermark = paramString;
    }

    @Override
	public void removeAll() {
        super.removeAll();
        add(this.scrollBar);
        this.lead = (this.anchor = null);
        fireListSelectionListener();
        revalidate();
        repaint();
    }

    @Override
	public void doLayout() {
        Insets localInsets = getInsets();
        int i = getWidth();
        int j = getHeight();
        int k = i - localInsets.left - localInsets.right;
        int l = j - localInsets.top - localInsets.bottom;
        int i1 = getComponentCount() - 1;
        int i2 = this.scrollBar.getPreferredSize().width;
        int i3 = Math.max(k / (this.size + 8), 1);
        int i4 = 0;
        if (i1 <= l / (this.size + 8) * i3) {
            this.scrollBar.setVisible(false);
        } else {
            i3 = Math.max((k - i2) / (this.size + 8), 1);
            int i5 = localInsets.top + localInsets.bottom + (i1 + i3 - 1) / i3 * (this.size + 8);
            this.scrollBar.setValues(i4 = Math.max(0, Math.min(this.scrollBar.getValue(), i5 - j)), j, 0, i5);
            this.scrollBar.setBounds(i - i2, 0, i2, j);
            this.scrollBar.setVisible(true);
        }
        for (int i5 = 0; i5 < i1; ++i5) {
            getComponent(i5 + 1).setBounds(localInsets.left + 4 + i5 % i3 * (this.size + 8), localInsets.top + 4 + i5 / i3 * (this.size + 8) - i4, this.size, this.size);
        }
    }

    public boolean isSelectionEmpty() {
        for (int i = 1; i < getComponentCount(); ++i) {
            if (((Item) getComponent(i)).isSelected()) {
                return false;
            }
        }
        return true;
    }

    public List<E> getSelecteds() {
        ArrayList<E> localArrayList = new ArrayList<E>();
        for (int i = 1; i < getComponentCount(); ++i) {
            Item localItem = (Item) getComponent(i);
            if (!localItem.isSelected()) {
                continue;
            }
            localArrayList.add((E) localItem);
        }
        return localArrayList;
    }

    public List<E> getAll() {
        ArrayList<E> localArrayList = new ArrayList<E>(5);
        for (int i = 1; i < getComponentCount(); ++i) {
            localArrayList.add((E) getComponent(i));
        }
        return localArrayList;
    }

    public void remove(E paramE) {
        super.remove(paramE);
        if (this.lead == paramE) {
            this.lead = null;
        }
        if (this.anchor == paramE) {
            this.anchor = null;
        }
        revalidate();
        repaint();
    }

    public void select(E paramE, boolean paramBoolean1, boolean paramBoolean2) {
        int i;
        if (paramBoolean1) {
            if (this.anchor == null) {
                this.anchor = this.lead;
            }
            i = 0;
            for (int j = 1; j < getComponentCount(); ++j) {
                Item localItem2 = (Item) getComponent(j);
                localItem2.setSelected((i != 0) || (localItem2 == this.anchor) || (localItem2 == paramE));
                if (localItem2 == this.anchor) {
                    i = (i == 0) ? 1 : 0;
                }
                if (localItem2 != paramE) {
                    continue;
                }
                i = (i == 0) ? 1 : 0;
            }
        } else if (paramBoolean2) {
            paramE.setSelected(!paramE.isSelected());
            this.anchor = null;
        } else {
            for (i = 1; i < getComponentCount(); ++i) {
                Item localItem1 = (Item) getComponent(i);
                localItem1.setSelected(localItem1 == paramE);
            }
            this.anchor = null;
        }
        this.lead = paramE;
        repaint();
        fireListSelectionListener();
    }

    public void addListSelectionListener(ListSelectionListener paramListSelectionListener) {
        this.listenerList.add(ListSelectionListener.class, paramListSelectionListener);
    }

    public void removeListSelectionListener(ListSelectionListener paramListSelectionListener) {
        this.listenerList.remove(ListSelectionListener.class, paramListSelectionListener);
    }

    private void fireListSelectionListener() {
        for (ListSelectionListener localListSelectionListener : this.listenerList.getListeners(ListSelectionListener.class)) {
            localListSelectionListener.valueChanged(new ListSelectionEvent(this, -1, -1, false));
        }
    }

    public void mouseClicked(MouseEvent paramMouseEvent) {
    }

    public void mouseEntered(MouseEvent paramMouseEvent) {
    }

    public void mouseExited(MouseEvent paramMouseEvent) {
    }

    public void mouseMoved(MouseEvent paramMouseEvent) {
    }

    public void mousePressed(MouseEvent paramMouseEvent) {
        requestFocus();
        this.select = new Rectangle(paramMouseEvent.getX(), paramMouseEvent.getY(), 0, 0);
    }

    public void mouseDragged(MouseEvent paramMouseEvent) {
        this.select.width = (paramMouseEvent.getX() - this.select.x);
        this.select.height = (paramMouseEvent.getY() - this.select.y);
        repaint();
    }

    public void mouseReleased(MouseEvent paramMouseEvent) {
        this.lead = (this.anchor = null);
        int i = ((paramMouseEvent.isControlDown()) || (paramMouseEvent.isMetaDown())) ? 1 : 0;
        if (this.select.width < 0) {
            this.select.x += this.select.width;
            this.select.width = Math.abs(this.select.width);
        }
        if (this.select.height < 0) {
            this.select.y += this.select.height;
            this.select.height = Math.abs(this.select.height);
        }
        for (int j = 1; j < getComponentCount(); ++j) {
            Item localItem = (Item) getComponent(j);
            boolean bool = this.select.intersects(localItem.getBounds());
            if (bool) {
                if (this.lead == null) {
                    this.lead = (E) localItem;
                }
                this.anchor = (E) localItem;
                localItem.setSelected(true);
            } else {
                if (i != 0) {
                    continue;
                }
                localItem.setSelected(false);
            }
        }
        this.select = null;
        repaint();
        fireListSelectionListener();
    }

    public void mouseWheelMoved(MouseWheelEvent paramMouseWheelEvent) {
        if (!this.scrollBar.isVisible()) {
            return;
        }
        this.scrollBar.setValue(this.scrollBar.getValue() + paramMouseWheelEvent.getUnitsToScroll() * 8);
    }

    @Override
	public boolean isFocusable() {
        return true;
    }

    public void focusLost(FocusEvent paramFocusEvent) {
        this.focused = false;
        repaint();
    }

    public void focusGained(FocusEvent paramFocusEvent) {
        this.focused = true;
        repaint();
    }

    public void adjustmentValueChanged(AdjustmentEvent paramAdjustmentEvent) {
        revalidate();
        repaint();
    }

    @Override
	protected void paintComponent(Graphics paramGraphics) {
        super.paintComponent(paramGraphics);
        Graphics2D localGraphics2D = ResourceUtils.init(paramGraphics);
        Color localColor = (this.focused) ? new Color(3700183) : Color.lightGray;
        for (int i = 1; i < getComponentCount(); ++i) {
            Item localItem = (Item) getComponent(i);
            if (localItem.isSelected()) {
                localObject = localItem.getBounds();
                paramGraphics.setColor(localColor);
                paramGraphics.fillRoundRect(localObject.x - 3, localObject.y - 3, localObject.width + 6, localObject.height + 6, 6, 6);
            }
            if ((!this.focused) || (this.lead != localItem)) {
                continue;
            }
            Object localObject = localGraphics2D.getStroke();
            localGraphics2D.setStroke(new BasicStroke(1.0F, 2, 0, 10.0F, new float[]{1.0F, 2.0F}, 0.0F));
            paramGraphics.setColor((localItem.isSelected()) ? Color.white : Color.darkGray);
            Rectangle localRectangle = localItem.getBounds();
            paramGraphics.drawRect(localRectangle.x - 1, localRectangle.y - 1, localRectangle.width + 1, localRectangle.height + 1);
            localGraphics2D.setStroke((Stroke) localObject);
        }
        if ((this.watermark == null) || (getComponentCount() != 1)) {
            return;
        }
        paramGraphics.setColor(Color.lightGray);
        paramGraphics.setFont(UIManager.getFont("Label.boldfont"));
        FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
        paramGraphics.drawString(this.watermark, (getWidth() - localFontMetrics.stringWidth(this.watermark)) / 2, (getHeight() + localFontMetrics.getAscent() - localFontMetrics.getDescent()) / 2);
    }

    @Override
	public void paint(Graphics paramGraphics) {
        super.paint(paramGraphics);
        if (this.select == null) {
            return;
        }
        int i = this.select.x;
        int j = this.select.x + this.select.width;
        int k = this.select.y;
        int l = this.select.y + this.select.height;
        int i1 = Math.min(i, j);
        int i2 = Math.min(k, l);
        int i3 = Math.max(i, j) - i1;
        int i4 = Math.max(k, l) - i2;
        paramGraphics.setColor(Color.white);
        paramGraphics.drawRect(i1, i2, i3 - 1, i4 - 1);
        paramGraphics.setColor(new Color(805306368, true));
        paramGraphics.fillRect(i1 + 1, i2 + 1, i3 - 2, i4 - 2);
    }

    public static abstract class Item extends JComponent
            implements MouseListener {

        private boolean selected;

        protected Item() {
            TipManager.register(this);
            addMouseListener(this);
        }

        public void setSelected(boolean paramBoolean) {
            this.selected = paramBoolean;
        }

        public boolean isSelected() {
            return this.selected;
        }

        @Override
		protected void paintComponent(Graphics paramGraphics) {
            paramGraphics.setColor(Color.gray);
            paramGraphics.fillRect(0, 0, getWidth(), getHeight());
        }

        public void mouseClicked(MouseEvent paramMouseEvent) {
        }

        public void mouseEntered(MouseEvent paramMouseEvent) {
        }

        public void mouseExited(MouseEvent paramMouseEvent) {
        }

        public void mouseReleased(MouseEvent paramMouseEvent) {
        }

        public void mousePressed(MouseEvent paramMouseEvent) {
            ComponentList localComponentList = (ComponentList) getParent();
            localComponentList.requestFocus();
            localComponentList.select(this, paramMouseEvent.isShiftDown(), (paramMouseEvent.isControlDown()) || (paramMouseEvent.isMetaDown()));
        }
    }
}
