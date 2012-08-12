/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icode.view;

import java.awt.event.ActionEvent;

/**
 *
 * @author ian
 */
public interface iToolBarMenuControls {

    /**
     * 
     * @param e 
     */
    public void newEntry(ActionEvent e);

    /**
     *
     * @param e
     */
    public void saveEntry(ActionEvent e);

    /**
     *
     * @param e
     */
    public void deleteEntry(ActionEvent e);

    /**
     *
     * @param e
     * @param modelSearch
     */
    public void findEntry(ActionEvent e);

    /**
     *
     * @param e 
     * @param ID
     */
    public void goToResourceID(ActionEvent e, Integer ID);

    /**
     *
     * @param e
     */
    public void previewInPDF(ActionEvent e);

    /**
     *
     * @param e
     */
    public void previewInEditor(ActionEvent e);

    /**
     *
     * @param e
     */
    public void previousEntry(ActionEvent e);

    /**
     *
     * @param e
     */
    public void nextEntry(ActionEvent e);

    /**
     *
     * @param e
     */
    public void formAsList(ActionEvent e);

    /**
     *
     * @param e
     */
    public void listAsForm(ActionEvent e);

    /**
     *
     * @param e
     */
    public void printEntry(ActionEvent e);

    /**
     *
     * @param e
     */
    public void actionsOnEntry(ActionEvent e);

    /**
     *
     * @param e
     */
    public void reloadEntry(ActionEvent e);

    public void firstEntry(ActionEvent e);

    public void lastEntry(ActionEvent e);
}
