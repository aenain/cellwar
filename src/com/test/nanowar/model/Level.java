/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author artur
 * klasa reprezentujaca dany poziom: zawiera informacje o wielkosci i rozmieszczeniu wiez,
 * wymagania czasowe na gwiazdki, bestScore (ile gwiazdek), nazwa lvl-u, thumbnail
 * odpowiada za wczytanie informacji o levelu zapisanych w json-ie czy czymś.
 */
public class Level {
    
    protected String levelName;
    protected int bestScore, bestTime;
    protected int timeRequirement[] = new int[3];
    protected ArrayList<Tower> userTowers;
    protected ArrayList<Tower> computerTowers;

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

    public int[] getTimeRequirement() {
        return timeRequirement;
    }

    public void setTimeRequirement(int[] timeRequirement) {
        this.timeRequirement = timeRequirement;
    }

    public int getBestTime() {
        return bestTime;
    }

    public void setBestTime(int bestTime) {
        this.bestTime = bestTime;
    }

    public ArrayList<Tower> getComputerTowers() {
        return computerTowers;
    }

    public void setComputerTowers(ArrayList<Tower> computerTowers) {
        this.computerTowers = computerTowers;
    }

    public ArrayList<Tower> getUserTowers() {
        return userTowers;
    }

    public void setUserTowers(ArrayList<Tower> userTowers) {
        this.userTowers = userTowers;
    }
    
    public void setPlayersTowers(Player user, Player comp) {
        if(user.isUser()) {
            user.clearObjects();
            user.setTowers(userTowers);
        }
        if(comp.isComputer()) {
            comp.clearObjects();
            comp.setTowers(computerTowers);
        }
    }
}
