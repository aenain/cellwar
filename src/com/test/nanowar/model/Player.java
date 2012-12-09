/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

import com.test.nanowar.utils.Mathematics;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author artur
 */
public class Player {
    public static final int MIN_SELECTION_DURATION = 200; // milliseconds
    public static final int MAX_SELECTION_DURATION = 1200; // milliseconds

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

    public void selectTower(Tower tower, long touchDuration) {
        synchronized (tower) {
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

            gamePanel.sendTroops(Player.convertSelectionTimeToPercentageShareOfTroops(touchDuration), this, tower);
        }
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

    public static int convertSelectionTimeToPercentageShareOfTroops(long selectionTimeInMilliseconds) {
        int duration = Mathematics.inBounds((int)selectionTimeInMilliseconds, MIN_SELECTION_DURATION, MAX_SELECTION_DURATION);
        int share = (int)Mathematics.linearConvertion(duration, MIN_SELECTION_DURATION, MAX_SELECTION_DURATION, Tower.MIN_TROOPS_SHARE, Tower.MAX_TROOPS_SHARE);
        return Mathematics.inBounds(share, Tower.MIN_TROOPS_SHARE, Tower.MAX_TROOPS_SHARE);
    }
}
