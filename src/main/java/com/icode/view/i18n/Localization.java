/* Copyright 2008-2009 Robert Bajzat. All rights reserved. Use is subject to license terms. */
package com.icode.view.i18n;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Loads resource bundle files given by a class or classes
 */
public class Localization {

    private Map<String, String> bundle;

    /**
     * Loads resource bundles for the given classes, e.g. for the package.Class class
     * searches for the package/resource/Class[_fr].properties file
     * @param classes an array of classes to search resource bundles for
     */
    public Localization(Class<?>... classes) {
        bundle = new HashMap<String, String>();
        for (int i = classes.length - 1; i >= 0; i--) {
            ResourceBundle rb = ResourceBundle.getBundle(
                    classes[i].getPackage().getName() + ".resource." + classes[i].getSimpleName());
            Enumeration<String> keys = rb.getKeys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                bundle.put(key, rb.getString(key));
            }
        }
    }

    /**
     * Returns a string for the given key from a resource bundle
     * @param key the key for the desired string
     * @return the string for the given key
     */
    public String get(String key) {
        return bundle.get(key);
    }

    /**
     * Formats the arguments and appends the pattern text given by the key
     * @param key the key for the desired string
     * @param arguments an array of objects to be formatted and substituted
     * @return the string for the given key including the substituted arguments
     */
    public String get(String key, Object... arguments) {
        return MessageFormat.format(bundle.get(key), arguments);
    }
    private static final Logger LOG = Logger.getLogger(Localization.class.getName());
}
