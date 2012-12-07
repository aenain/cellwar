/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author artur
 */


public class Player {
    
    public enum PlayerType {
        USER, COMPUTER
    }
    
    protected PlayerType playerType;
    protected ArrayList<Tower> towers;
    protected ArrayList<Troops> troops;

    public Player(PlayerType playerType, ArrayList<Tower> towers, ArrayList<Troops> troops) {
        this.playerType = playerType;
        this.towers = (towers == null ? new ArrayList<Tower>() : towers);
        this.troops = (troops == null ? new ArrayList<Troops>() : troops);
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public ArrayList<Tower> getTowers() {
        return towers;
    }

    public void setTowers(ArrayList<Tower> towers) {
        this.towers = towers;
    }

    public ArrayList<Troops> getTroops() {
        return troops;
    }

    public void setTroops(ArrayList<Troops> troops) {
        this.troops = troops;
    }
    
    public void sendTroops(List<Tower> selectedTowers, Tower destination, Integer percentageShare) {
        for(Tower tower : selectedTowers) {
            if(tower.getOwner() == this) {
                tower.sendTroops(percentageShare, destination);
            }
        }
    }
    
    public void addTower(Tower tower) {
        if(tower.getOwner() == this) {
            towers.add(tower);
        }
    }
    
    public void deleteTower(Tower tower) {
        towers.remove(tower);
    }
    
    public boolean isComputer() {
        return this.playerType == PlayerType.COMPUTER;
    }
    
    public boolean isUser() {
        return this.playerType == PlayerType.USER;
    }
    
    public void clearObjects() {
        towers.clear();
        troops.clear();
    }
    
}
