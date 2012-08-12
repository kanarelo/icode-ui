package com.icode.view.container;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import com.icode.resources.ResourceUtils;
import com.icode.util.Commons;

/**
 * The wrapper component to draw the bubble background and to block mouse
 * events for a modal dialog
 */
class Bubble extends JComponent implements ActionListener, ComponentListener, MouseListener {

    private JComponent invoker;
    private JButton previousDefaultButton;
    private Component previousFocused;
    private boolean submitted;
    private GeneralPath path;
	private JButton okButton;
	private JButton cancelButton;
	private final PopupDialog popupDialog;
	private boolean translucent;

    Bubble(JComponent invoker, PopupDialog popupDialog, JButton okButton, JButton cancelButton, boolean translucent) {
        this.invoker = invoker; // TODO invoker may be invisible for non
		this.popupDialog = popupDialog;
		this.okButton = okButton;
		this.cancelButton = cancelButton;
		this.translucent = translucent;
        // modal
        RootPaneContainer rootPaneContainer = (RootPaneContainer) SwingUtilities.windowForComponent(invoker);

        JRootPane rootPane = rootPaneContainer.getRootPane();
        previousDefaultButton = rootPane.getDefaultButton();
        previousFocused = ((Window) rootPaneContainer).getFocusOwner();

        setLayout(null);
        add(popupDialog);
        setOpaque(false);
        JLayeredPane layeredPane = rootPaneContainer.getLayeredPane();
        layeredPane.add(this, JLayeredPane.MODAL_LAYER, 0);
        layeredPane.addComponentListener(this);
        updateBounds();

        for (int i = 0, n = getComponentCount(); i < n; i++) {
            setTransparent(getComponent(i));
        }

        if (okButton != null) {
            okButton.addActionListener(this);
            rootPane.setDefaultButton(okButton);
        }
        if (cancelButton != null) {
            cancelButton.addActionListener(this);
            cancelButton.registerKeyboardAction(this,
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                    JComponent.WHEN_IN_FOCUSED_WINDOW);
        }

        if (okButton != null) {
            okButton.requestFocus();
        } else if (cancelButton != null) {
            cancelButton.requestFocus();
        }
        // else first focusable
    }

    public boolean waitForClose() {
        setFocusCycleRoot(true);
        addMouseListener(this);
        try {
            if (EventQueue.isDispatchThread()) { // SwingUtilities.isEventDispatchThread()
                EventQueue queue = getToolkit().getSystemEventQueue();
                while (isVisible()) {
                    AWTEvent event = queue.getNextEvent();
                    if (event instanceof ComponentEvent) {
                        ((ComponentEvent) event).getComponent().dispatchEvent(event);
                    } else if (event instanceof ActiveEvent) {
                        ((ActiveEvent) event).dispatch();
                    } else {
                        throw new RuntimeException(event.toString());
                    }
                }
            } else {
                while (isVisible()) {
                    Thread.sleep(250); // TODO current thread not owner
                }
            }
        } catch (InterruptedException ie) {
        }
        return submitted;
    }

    private void setTransparent(Component component) {
        if (component instanceof JPanel) {
            ((JPanel) component).setOpaque(false);
        }
        if (component instanceof Container) {
            Container container = (Container) component;
            for (int i = 0, n = container.getComponentCount(); i < n; i++) {
                setTransparent(container.getComponent(i));
            }
        }
    }

    /**
     * Updates the bounds of the glass pane, the bubble shape, and the
     * dialog content
     */
    private void updateBounds() {
        JLayeredPane layeredPane = (JLayeredPane) getParent();
        Point p = SwingUtilities.convertPoint(invoker, 0, 0, layeredPane);
        Dimension d = popupDialog.getPreferredSize();
        int ih = invoker.getHeight(), iw = invoker.getWidth(), lw = layeredPane.getWidth(), lh = layeredPane.getHeight();
        int w = Math.min(d.width, lw - 16), h = Math.min(d.height, lh - 16), m = (w > lw - 32) ? 8
                : 16, x = Math.max(m, Math.min(p.x + (iw - w) / 2, lw - w - m)), y = 0;
        int ax = p.x + iw / 2, ay = 0; // if ((grip < 8) || (grip > w - 8))
        // { grip = 0; }
        if (p.y + ih + h + 24 <= lh) {
            y = p.y + ih + 16;
            ay = -1;
        } // bellow
        else if (h + 24 <= p.y) {
            y = p.y - 16 - h;
            ay = 1;
        } // above
        else {
            y = (lh - h) / 2;
        }

        popupDialog.setBounds(x, y, w, h);
        setBounds(0, 0, lw, lh);

        path = new GeneralPath();
        int x1 = x - 8, x2 = x + w + 8, y1 = y - 8, y2 = y + h + 8;
        path.moveTo(x1 + 8, y1);
        if (ay == -1) {
            path.lineTo(ax - 8, y1);
            path.lineTo(ax, y1 - 8);
            path.lineTo(ax + 8, y1);
        } // top arrow

        path.lineTo(x2 - 8, y1); // top
        path.quadTo(x2, y1, x2, y1 + 8);
        path.lineTo(x2, y2 - 8); // right
        path.quadTo(x2, y2, x2 - 8, y2);
        if (ay == 1) {
            path.lineTo(ax + 8, y2);
            path.lineTo(ax, y2 + 8);
            path.lineTo(ax - 8, y2);
        } // bottom arrow
        path.lineTo(x1 + 8, y2); // bottom
        path.quadTo(x1, y2, x1, y2 - 8);
        path.lineTo(x1, y1 + 8); // left
        path.quadTo(x1, y1, x1 + 8, y1);

        layeredPane.revalidate();
    }

    protected GradientPaint getGradient(Rectangle bounds) {
    	return new GradientPaint(0, bounds.y + 1, upperGradientColor(), 0, bounds.y + bounds.height - 3, lowerGradientColor());
    }

    protected Color lowerGradientColor() {
		return new Color(0xdddddd);
	}

	protected Color upperGradientColor() {
		return new Color(240, 242, 246);
	}

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = ResourceUtils.init(g);
        if (path == null) {
        	updateBounds();//At times, a null pointer exception is exhibited
        }

//        int size = 2;
//        int tx = -size, ty = 1-size;
//        g.translate(tx + 3 , ty + 3);
//        final float opacity = 0.8f; // Effect "darkness".
//        final Composite oldComposite = g2.getComposite();
//        final Color oldColor = g.getColor();
//        
//        float preAlpha = 0.4f;
//        if (oldComposite instanceof AlphaComposite
//        		&& ((AlphaComposite) oldComposite).getRule() == AlphaComposite.SRC_OVER) {
//        	preAlpha = Math.min(((AlphaComposite) oldComposite).getAlpha(), preAlpha);
//        }
//        
//        boolean isShadow = true; 
//        int maxSize = isShadow ? size - 1 : size;
//        
//        g.setColor(Color.BLACK);
//        for (int i = -size; i <= maxSize; i++) {
//        	for (int j = -size; j <= maxSize; j++) {
//        		double distance = i * i + j * j;
//        		float alpha = opacity;
//        		if (distance > 0.0d) {
//        			alpha = (float) (1.0f / ((distance * size) * opacity));
//        		}
//        		alpha *= preAlpha;
//        		if (alpha > 1.0f) {
//        			alpha = 1.0f;
//        		}
//        		g2.setComposite(AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha));
//        		g2.draw(path);
//        	}
//        }

        if (translucent){
        	g2.setColor(Color.BLACK);
        	g2.draw(path);
        	g2.setColor(new Color(0xbb000000, true));
        	g2.fill(path);
        }else{
        	g2.setPaint(getGradient(path.getBounds()));
        	g2.fill(path);
        	g2.setColor(Color.gray);
        	g2.draw(path);
        }
    }

    public void dismiss() {
        JLayeredPane layeredPane = (JLayeredPane) getParent();
        layeredPane.removeComponentListener(this);
        Commons.repaintAncestry(layeredPane);
        layeredPane.remove(this);
        setVisible(false);

        if (previousDefaultButton != null) {
            layeredPane.getRootPane().setDefaultButton(
                    previousDefaultButton);
        }
        if (previousFocused != null) {
            previousFocused.requestFocus();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            submitted = true;
            popupDialog.close();
        } else if (e.getSource() == cancelButton) {
        	popupDialog.close();
        }
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        updateBounds();
    }

    public void componentShown(ComponentEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (!path.contains(e.getX(), e.getY())) {
            getToolkit().beep();
        }
    }

    public void mouseReleased(MouseEvent e) {
    }
}