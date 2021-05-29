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
    private double predicted;

    public DataModel(String time, double price) {
        this.price = price;
        this.time = time;
    }
    
    public DataModel(String time, double price, double predicted) {
        this.price = price;
        this.time = time;
        this.predicted = predicted;
    }

    public double getPredicted() {return predicted;}
    public double getPrice() {return price;}
    public String getTime() {return time;}
}