/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

/**
 *
 * @author artur
 */
public class MainThread extends Thread {

    private boolean running;
    protected MainGamePanel gamePanel;

    public MainThread(MainGamePanel gamePanel) {
        super();
        this.gamePanel = gamePanel;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {
            gamePanel.update();
        }
    }
}
