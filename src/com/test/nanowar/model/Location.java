/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.model;

/**
 *
 * @author artur
 */
public class Location {
    protected Integer x, y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
