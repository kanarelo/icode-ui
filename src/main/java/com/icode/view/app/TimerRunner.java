/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.app;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JComponent;

import com.icode.resources.ResourceUtils;
import com.icode.util.Commons;

/**
 * An indeterminate circle progress bar animated while the added task executed
 */
public class TimerRunner extends JComponent {

    private TimerRunner timerRunner;
    private javax.swing.Timer swingTimer;
    private java.util.Timer taskTimer;
    private boolean running;
    private int angle;
    private TimerRunner thisTimerR;
    private Icon[] busyIcons = null;

    private final Icon idleIcon = ResourceUtils.getIcon("/icons/busyicons/idle-icon.png");
    private int busyIconIndex;

    public TimerRunner() {
        thisTimerR = this;
    }

    /**
     * Creates a single component and returns it
     * @return the single progress component
     */
    public TimerRunner get() {
        if (timerRunner == null) {
            timerRunner = new TimerRunner();
        }
        return timerRunner;
    }

    /**
     * Executes the given task not on the event thread
     * @param runnable the task to execute
     */
    public void execute(Runnable runnable) {
        get().executeImpl(runnable);
    }

    private void executeImpl(Runnable runnable) {
        if (taskTimer == null) {
            taskTimer = new java.util.Timer(getClass().getName(), false);
        }
        taskTimer.schedule(new TaskRunner(runnable), 100);
    }

    private class TaskRunner extends TimerTask implements ActionListener {

        private Runnable runnable;

        private TaskRunner(Runnable runnable) {
            this.runnable = runnable;
        }

        //messageTimer.setRepeats(false);
        @Override
		public void run() {
            if (swingTimer == null) {
                swingTimer = new javax.swing.Timer(15, this);
            }
            running = true;
            swingTimer.start();
            Commons.repaintAncestry(thisTimerR);

            runnable.run();

            swingTimer.stop();
            running = false;
            Commons.repaintAncestry(thisTimerR);
        }

        public void actionPerformed(ActionEvent e) {
            angle++;
            Commons.repaintAncestry(thisTimerR);
        }
    }
    private static final int size = 32;

    /**
     * Overwritten to paint the component
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = ResourceUtils.init(g);
        if (!running) {
            idleIcon.paintIcon(this, g2, 8, 8);
            return;
        }
        
        if (busyIcons == null){
        	busyIcons = new Icon[15];
            for (int q = 0; q < busyIcons.length; q++) {
                busyIcons[q] = ResourceUtils.getIcon("/icons/busyicons/busy-icon" + q + ".png");
            }
        }
        busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
        busyIcons[busyIconIndex].paintIcon(this, g2, 8, 8);
    }

    /**
     * Overwritten to return the fixed size
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size, size);
    }

    /** Overwritten to return preferred size for box layout */
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /** Overwritten to return preferred size for box layout */
    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
    private static final Logger LOG = Logger.getLogger(TimerRunner.class.getName());
}
