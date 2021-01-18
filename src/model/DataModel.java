/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author Odorifqi
 */
public class DataModel implements Serializable {
    private double price;
    private String time;
    private double prePredicted;
    private double predicted;
    private double adjust;
    private int index;

    public DataModel(String time, double price) {
        this.price = price;
        this.time = time;
    }

    public DataModel(double price, String time, double prePredicted, double adjust) {
        this.price = price;
        this.time = time;
        this.prePredicted = prePredicted;
        this.adjust = adjust;
    }
    
    public DataModel(String time, double price, double predicted) {
        this.price = price;
        this.time = time;
        this.predicted = predicted;
    }
    
    public DataModel(String time, double price, int index) {
        this.price = price;
        this.time = time;
        this.index = index;
    }

    public double getPrePredicted() {return prePredicted;}
    public int getIndex() {return index;}
    public double getPredicted() {return predicted;}
    public double getPrice() {return price;}
    public String getTime() {return time;}
    public double getAdjust() {return adjust;}

}
