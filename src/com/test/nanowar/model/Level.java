/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import com.test.nanowar.utils.JSONReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author artur klasa reprezentujaca dany poziom: zawiera informacje o
 * wymaganiach czasowych na kolejne gwiazdki, bestScore (ile gwiazdek), nazwa
 * lvl-u, thumbnail odpowiada za wczytanie informacji o levelu zapisanych w
 * json-ie do panelu.
 */
public class Level {
    public static final int COUNT = 3;

    protected String levelName;
    protected int number;
    protected int bestScore, bestTime;
    protected int timeRequirements[] = new int[3];
    protected MainGamePanel panel;
    protected JSONObject data;

    public Level(int number) {
        this.number = number;
    }

    public void setPanel(MainGamePanel panel) {
        this.panel = panel;
    }

    public void readData(Context context) {
        JSONReader reader = new JSONReader(context);
        data = reader.getLevelData(number);
        if (data != null) {
            try {
                // level name
                setLevelName(data.getString("name"));

                // time requirements
                JSONArray rawRequirements = data.getJSONArray("timeRequirements");
                for (int i = 0; i < timeRequirements.length; i++) {
                    timeRequirements[i] = rawRequirements.optInt(i, 0);
                }
            } catch (JSONException ex) {
                Log.e("Level", "reading level data error", ex);
            }
        }
    }

    // wczytuje dane z jsona i inicjalizuje wszystkie pola (poza bestScore i bestTime)
    public void readDataAndInitTowers(Context context) {
        readData(context);
        if (panel == null || data == null) { return; }
        try {
            // towers
            JSONArray rawTowers = data.getJSONArray("towers");
            JSONObject raw;
            com.test.nanowar.model.Tower tower;
            JSONArray coordinates;
            Point center;
            String ownerName;
            for (int i = 0; i < rawTowers.length(); i++) {
                raw = rawTowers.getJSONObject(i);
                tower = new com.test.nanowar.model.Tower(panel);

                // relative location of the center point
                coordinates = raw.getJSONArray("location");
                center = new Point(coordinates.getInt(0), coordinates.getInt(1));
                tower.setRelativeCenter(center);

                // capacity & troopsCount
                tower.setCapacity(raw.getInt("capacity"));
                tower.setInitTroopsCount(raw.getInt("initTroopsCount"));

                // owner name
                ownerName = raw.optString("player", "none");

                if (ownerName.equals("user")) {
                    tower.setOwner(panel.userPlayer);
                    panel.getPlayerTowers(panel.userPlayer).add(tower);
                } else if (ownerName.equals("computer")) {
                    tower.setOwner(panel.computerPlayer);
                    panel.getPlayerTowers(panel.computerPlayer).add(tower);
                } else {
                    tower.setOwner(panel.nonePlayer);
                    panel.getPlayerTowers(panel.nonePlayer).add(tower);
                }
            }
        } catch (JSONException ex) {
            Log.e("Level", "reading level data error", ex);
        }
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int[] getTimeRequirements() {
        return timeRequirements;
    }

    public void setTimeRequirements(int[] timeRequirements) {
        this.timeRequirements = timeRequirements;
    }

    public int getBestTime() {
        return bestTime;
    }

    public void setBestTime(int bestTime) {
        this.bestTime = bestTime;
    }
}
