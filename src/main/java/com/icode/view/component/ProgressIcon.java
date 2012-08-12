package com.icode.view.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

import com.icode.resources.ResourceUtils;

public class ProgressIcon
        implements Icon {

    private float value;

    public ProgressIcon(float paramFloat) {
        this.value = paramFloat;
    }

    public int getIconWidth() {
        return 16;
    }

    public int getIconHeight() {
        return 16;
    }

    public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
        ResourceUtils.init(paramGraphics);
        paramGraphics.setColor(new Color(3563428));
        paramGraphics.fillOval(paramInt1, paramInt2, 16, 16);
        paramGraphics.setColor(new Color(10664935));
        paramGraphics.fillOval(paramInt1 + 1, paramInt2 + 1, 14, 14);
        paramGraphics.setColor(new Color(3563428));
        paramGraphics.fillArc(paramInt1, paramInt2, 16, 16, 90, (int) (-360.0F * this.value + 0.5D));
    }
}

/* Location:           C:\Users\Nes\Desktop\Thinet\flient.jar
 * Qualified Name:     thinlet.ui.ProgressIcon
 * JD-Core Version:    0.5.4
 */
