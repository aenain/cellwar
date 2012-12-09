/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.utils;

/**
 *
 * @author artur
 */
public class Mathematics {
    /*
     * zwraca wartosc przeksztalcenia liniowego y = ax+b, gdzie x nalezy do <minX, maxX>, y do <minY, maxY>
     */ 
    public static double linearConvertion(double x, double minX, double maxX, double minY, double maxY) {
        double a = (maxY - minY) / (maxX - minX);
        double b = minY - a * minX;
        return a*x + b;
    }

    public static double inBounds(double value, double min, double max) {
        return Math.min(max, Math.max(value, min));
    }

    public static int inBounds(int value, int min, int max) {
        return Math.min(max, Math.max(value, min));
    }
}
