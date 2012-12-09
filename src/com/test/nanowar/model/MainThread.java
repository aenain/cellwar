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
public class MainThread extends Thread {

    private boolean running;
    private Long startTime, stopTime;
    final protected MainGamePanel gamePanel;
    protected AI computer;

    public MainThread(MainGamePanel gamePanel) {
        super();
        this.gamePanel = gamePanel;
    }
    
    public MainThread(MainGamePanel gamePanel, AI comp) {
        super();
        this.gamePanel = gamePanel;
        this.computer = comp;
    }

    public void setRunning(boolean running) {
        this.running = running;
        if (running) {
            startTime = System.currentTimeMillis();
        }
        else {
            stopTime = System.currentTimeMillis();
        }
    }

    // zwraca czas rozgrywki w sekundach
    public Integer getTime() {
        Integer time;
        long duration;

        if (stopTime != null) {
            duration = stopTime - startTime;
        } else {
            duration = System.currentTimeMillis() - startTime;
        }

        time = (int)(duration / 1000.0);
        return time;
    }

    @Override
    public void run() {
        int ile = 0;
        while (running) {
            computer.doMove();
            gamePanel.update();
            /*if(ile == 50) {
                List<Tower> towers = gamePanel.getPlayerTowers(gamePanel.getUserPlayer());
                
                gamePanel.sendTroops(80, towers, gamePanel.getPlayerTowers(gamePanel.getComputerPlayer()).iterator().next());
                
            }
            ile++;*/
             try {
               sleep(40);
           } catch (InterruptedException ex) {}
        }
    }
}
