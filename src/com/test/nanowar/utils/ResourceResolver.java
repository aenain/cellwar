/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.utils;

import android.util.Log;
import com.test.nanowar.R;

/**
 *
 * @author artur
 */
public class ResourceResolver {
    public static Integer raw(String resourceName) {
        Integer levelResourceId = null;

        try {
            levelResourceId = R.raw.class.getField(resourceName).getInt(null);
            
        } catch (NoSuchFieldException ex) {
            Log.e("ResourceResolver", "no such resource", ex);
        } catch (IllegalAccessException ex) {
            Log.e("ResourceResolver", "illegal access", ex);
        } catch (IllegalArgumentException ex) {
            Log.e("ResourceResolver", "illegal argument", ex);
        }

        return levelResourceId;
    }
}
