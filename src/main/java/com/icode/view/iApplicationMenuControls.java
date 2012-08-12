/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icode.view;
/**
 *
 * @author Nes
 */
public interface iApplicationMenuControls {
    /**
     *
     */
    void menu();

    /**
     *
     */
    void open();

    /**
     *
     */
    void newHomeScreen();

    /**
     *
     */
    void exportData();

    /**
     *
     */
    void importData();

    /**
     *
     */
    void lockScreen();

    /**
     *
     */
    void closeApplication();

    /**
     *
     */
    void newHomeWindow();

    /**
     *
     */
    void closeWindow();

    /**
     * Overwrite it, called when the window is opened
     */
    void init();

    /**
     * Overwrite it, called when the window is closing, or for the quit system menu on Mac
     * @return true to allow to exit
     */
    boolean close();

    /**
     * Overwrite it, called for the about system menu on Mac
     */
    void showAbout();

    /**
     * Overwrite it, called for the preferences system menu on Mac
     */
    void showPreferences();
}
