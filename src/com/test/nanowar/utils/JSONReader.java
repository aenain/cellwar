/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author artur
 */
public class JSONReader {
    protected File directory;
    protected String filename, rawJSON;
    protected JSONObject json;
    protected Context context;

    public JSONReader(Context context) {
        ContextWrapper wrapper = new ContextWrapper(context);
        directory = wrapper.getDir("media", Context.MODE_PRIVATE);
        this.context = context;
        rawJSON = null;
        json = null;
    }

    public JSONObject getJSON(String filename) {
        if (rawJSON == null) {
            readInternalFile(filename);
        }
        if (json == null) {
            parse();
        }

        return json;
    }

    public JSONObject getLevelData(int levelNumber) {
        
        String rawLevelData = readLevelData(levelNumber); 
        try {
            return new JSONObject(rawLevelData);
        } catch (JSONException ex) {
            Log.e("JSONReader", "json parsing error", ex);
            return null;
        }
    }

    protected String readLevelData(Integer levelNumber) {
        Integer resourceId = ResourceResolver.raw("level" + levelNumber.toString(2));
        if (resourceId == null) { return ""; }

        InputStream stream = context.getResources().openRawResource(resourceId);
        java.util.Scanner scanner = new java.util.Scanner(stream, "UTF-8").useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    protected void readInternalFile(String filename) {
        File file = new File(directory, filename);
        StringBuilder raw = new StringBuilder();
        FileReader freader = null;
        BufferedReader breader = null;

        try {
            freader = new FileReader(file);
            breader = new BufferedReader(freader);
            String line;
            while ((line = breader.readLine()) != null) {
                raw.append(line);
            }
        } catch (IOException ex) {
            Log.e("JSONReader", "io exception", ex);
        } finally {
            if (freader != null) {
                try {
                    freader.close();
                } catch (IOException ex) {}
            }
            if (breader != null) {
                try {
                    breader.close();
                } catch (IOException ex) {}
            }
        }

        rawJSON = raw.toString();
    }

    protected JSONObject parse() {
        try {
            json = new JSONObject(rawJSON);
            return json;
        } catch (JSONException ex) {
            Log.e("JSONReader", "parsing json exception", ex);
        }
        return null;
    }
}
