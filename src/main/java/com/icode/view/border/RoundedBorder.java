package com.icode.view.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.border.Border;

import com.icode.resources.ResourceUtils;

public class RoundedBorder implements Border {

    public Insets getBorderInsets(Component c) {
        return new Insets(9, 9, 9, 9);
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = ResourceUtils.init(g);
        g2.setPaint(new GradientPaint(x, y + 1, Color.white, x, y + height - 3, new Color(0xe0e0e0)));
        g.fillRoundRect(x, y, width, height, 18, 18);
        g.setColor(Color.lightGray);
        g.drawRoundRect(x, y, width - 1, height - 1, 18, 18);
    }
}
