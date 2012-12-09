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
