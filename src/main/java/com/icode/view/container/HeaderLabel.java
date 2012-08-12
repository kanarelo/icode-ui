/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.icode.view.container;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.Icon;
import javax.swing.JLabel;

import com.icode.resources.ResourceUtils;

/**
 *
 * @author Nes
 */
public class HeaderLabel extends JLabel{
    private boolean seamless = true;
    private Color seamColor = Color.DARK_GRAY;
    public HeaderLabel(){
        super();
    }

    public HeaderLabel(String str){
        super(str);
    }

    public HeaderLabel(String str, boolean seamless){
        this(str);
        this.seamless = seamless;
    }
    public HeaderLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }
    
    public HeaderLabel(String text, int horizontalAlignment) {
        this(text, null, horizontalAlignment);
    }
    
    public HeaderLabel(Icon image, int horizontalAlignment) {
        this(null, image, horizontalAlignment);
    }

    public HeaderLabel(Icon image) {
        this(null, image, CENTER);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setOpaque(true);
        if (seamless) {
            Graphics2D g2 = ResourceUtils.init(g);
            int w = this.getWidth(), h = this.getHeight();
            g.setColor(this.getBackground().brighter());
            Stroke stroke = g2.getStroke();
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE,
                    BasicStroke.JOIN_MITER, 10, new float[]{1, 2}, 0));
            g.drawLine(1, 1, w - 1, 1);
            g.drawLine(1, h - 2, w - 1, h - 2);
            g2.setStroke(stroke);
        }
    }

    private Color getSeamColor() {
        return seamColor;
    }

    public void setSeamColor(Color seamColor) {
        this.seamColor = seamColor;
    }

}
