/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

/**
 *
 * @author artur
 */
public class JSONWriter {
    protected File directory;
    protected Context context;

    public JSONWriter(Context context) {
        ContextWrapper wrapper = new ContextWrapper(context);
        directory = wrapper.getDir("media", Context.MODE_PRIVATE);
        this.context = context;
    }

    public void writeUserLevelData(Integer levelNumber, JSONObject json) {
        writeInternalFile(json, ResourceResolver.levelFileName(levelNumber) +".json");
    }

    protected void writeInternalFile(JSONObject json, String filename) {
        FileWriter fwriter = null;
        
        try {
            File file = new File(directory, filename);
            fwriter = new FileWriter(file);
            fwriter.write(json.toString());
        } catch (IOException ex) {
            Log.e("JSONWriter", "io exception", ex);
        } finally {
            try {
                if (fwriter != null) {
                    fwriter.close();
                }
            } catch (IOException ex) {}
        }
    }
}
