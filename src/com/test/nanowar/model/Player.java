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

        USER, COMPUTER, NONE
    }

    public enum SelectionMode {

        SELECT, TRANSPORT
    }
    protected PlayerType playerType;
    protected MainGamePanel gamePanel;
    protected SelectionMode selectionMode = SelectionMode.SELECT;

    public Player(PlayerType type) {
        this.playerType = type;
        /*this.towers = new ArrayList<Tower>();
         this.troops = new ArrayList<Troops>();*/
    }

    public Player(PlayerType type, MainGamePanel gamePanel) {
        this.playerType = type;
        this.gamePanel = gamePanel;
    }

    public Player(PlayerType playerType, ArrayList<Tower> towers, ArrayList<Troops> troops) {
        this.playerType = playerType;
        /*this.towers = (towers == null ? new ArrayList<Tower>() : towers);
         this.troops = (troops == null ? new ArrayList<Troops>() : troops);*/
    }

    public void setGamePanel(MainGamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public List<Tower> getTowers() {
        return gamePanel.getPlayerTowers(this);
    }

    public List<Troops> getTroops() {
        return gamePanel.getPlayerTroops(this);
    }

    public void selectTower(Tower tower) {
        // synchronized (tower) {
            if (tower.getOwner().equals(this)) {
                if (isSelecting()) {
                    if (tower.isSelected()) {
                        gamePanel.selectTower(tower, Tower.Selection.NONE);
                    } else {
                        gamePanel.selectTower(tower, Tower.Selection.SELECTED);
                    }
                    return;
                }
            }

            gamePanel.sendTroops(50, this, tower);
        // }
    }

    public void sendTroops(List<Tower> selectedTowers, Tower destination, Integer percentageShare) {
        gamePanel.sendTroops(percentageShare, selectedTowers, destination);
    }

    public boolean isComputer() {
        return this.playerType == PlayerType.COMPUTER;
    }

    public boolean isUser() {
        return this.playerType == PlayerType.USER;
    }

    public boolean isNone() {
        return this.playerType == PlayerType.NONE;
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    public boolean isSelecting() {
        return selectionMode == SelectionMode.SELECT;
    }
}
