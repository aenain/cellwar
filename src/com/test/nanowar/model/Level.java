/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import com.test.nanowar.utils.JSONReader;
import com.test.nanowar.utils.JSONWriter;
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
    public static final int COUNT = 5;

    protected String levelName;
    protected int number;
    protected int bestScore = 0, bestTime = Integer.MAX_VALUE, currentScore, currentTime;
    protected int timeRequirements[] = new int[3];
    protected MainGamePanel panel;
    protected JSONObject data;

    public Level(int number) {
        this.number = number;
    }

    public void setPanel(MainGamePanel panel) {
        this.panel = panel;
    }

    public void saveUserData(Context context) {
        JSONWriter writer = new JSONWriter(context);
        JSONObject userData = new JSONObject();

        try {
            userData.put("time", bestTime);
            userData.put("score", bestScore);
            userData.put("name", levelName);
            writer.writeUserLevelData(number, userData);
            Log.d("LEVEL", "writing user data:" + userData);
        } catch (JSONException ex) {
            Log.e("Level", "writing user data error", ex);
        }
    }

    public void readUserData(Context context) {
        JSONReader reader = new JSONReader(context);
        JSONObject userData = reader.getUserLevelData(number);
        if (userData != null) {
            Log.d("LEVEL", "readUserData:" + userData.toString());
            try {
                bestTime = userData.getInt("time");
                bestScore = userData.optInt("score", 0);
                levelName = userData.optString("name", "NO NAME");
            } catch (JSONException ex) {
                Log.e("Level", "reading user data error", ex);
            }
        }
        else {
            Log.d("LEVEL", "readUserData:null");
        }
    }

    public void readData(Context context) {
        JSONReader reader = new JSONReader(context);
        data = reader.getLevelData(number);

        if (data != null) {
            Log.d("LEVEL", "readData:" + data.toString());
            try {
                // level name
                levelName = data.optString("name", "NO NAME");

                // time requirements
                JSONArray rawRequirements = data.getJSONArray("timeRequirements");
                for (int i = 0; i < timeRequirements.length; i++) {
                    timeRequirements[i] = rawRequirements.optInt(i, 0);
                }
            } catch (JSONException ex) {
                Log.e("Level", "reading level data error", ex);
            }
        }
        else {
            Log.d("LEVEL", "readData:null");
        }
    }

    // wczytuje dane z jsona i inicjalizuje wszystkie pola (poza bestScore i bestTime)
    public void readDataAndInitTowers(Context context) {
        readData(context);
        readUserData(context);
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

    public void setScore(int score) {
        this.currentScore = score;
    }

    public int getScore() {
        return currentScore;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getLevelNumber() {
        return number;
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

    public void setTimeAndScore(int currentTime) {
        this.currentTime = currentTime;
        for (int i = 2; i >= 0; i--) {
            if (currentTime < timeRequirements[i]) {
                this.currentScore = i + 1;
                return;
            }
        }
        this.currentScore = 0;
    }

    public void setBestTime(int bestTime) {
        this.bestTime = bestTime;
    }

    public void saveResults(Context context) {
        if (currentScore > bestScore || (currentScore == bestScore && currentTime < bestTime)) {
            bestScore = currentScore;
            bestTime = currentTime;
            saveUserData(context);
        }
    }
}
