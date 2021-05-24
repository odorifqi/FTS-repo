/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.DataModel;

/**
 *
 * @author Odorifqi
 */
public class Controller {
    private ArrayList<DataModel> TableData = new ArrayList<DataModel>();
    private List<Date> listDateChart = new ArrayList<Date>();
    private List<Double> listActualChart = new ArrayList<Double>();
    private List<String> listDate = new ArrayList<String>();
    private List<Double> listActual = new ArrayList<Double>();
    private List<Double> listDefuzzy = new ArrayList<Double>();
    private List<Double> list_12 = new ArrayList<Double>();
    private List<Double> listWorking = new ArrayList<Double>();
    private List<Integer> listFuzzify = new ArrayList<Integer>();
    private int intvl;
    private double maxVal;
    private double minVal;
    private double jumpVal;
    private double[] intvlPart;
    private int[][] flr;
    private double[][] matrix;
    private double[] temp;
    private double[] medianIntvl;
      
    public Controller(){}
    public List<Date> getListDateChart() {return listDateChart;}
    public List<Double> getListActualChart() {return listActualChart;}
    public List<Double> getListDefuzzy() {return listDefuzzy;}
    public ArrayList<DataModel> getTableData(){return TableData;}
    
    public void inputFile(File file) throws ParseException, IOException {
        String line = "";
        String csvSplit = ";";
        int iter = 0;
        int x = 0;

        TableData.clear();
        listDateChart.clear();
        listActual.clear();
        
        listWorking.clear();
        
        listActualChart.clear();
        listDefuzzy.clear();
        listDate.clear();
        
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {      
            while ((line = br.readLine()) != null) {
                if (iter == 0) {
                    iter++;
                    continue;
                }
                String[] text = line.split(csvSplit);  
                
                DateFormat parser = new SimpleDateFormat("MM/yy");
                Date date = parser.parse(text[0]);
                double tempInt = (double) Double.parseDouble(text[1]);

                listDate.add(text[0]);
                listActual.add(tempInt);
                listWorking.add(tempInt);
                listDateChart.add(date);
                listActualChart.add(tempInt); 
                x++;
            }  

        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } br.close();
        
        for (int i = 0; i < 12; i++) {
            listDateChart.remove(x-12);
            listActualChart.remove(x-12);
        }
            listDateChart.remove(0); 
            listActualChart.remove(0);

        int y = x-12;
        for (int i = 0; i < y; i++) {
                TableData.add(new DataModel(listDate.get(i), listActual.get(i)));
        }
    }  

    public void prediksi(int interval){  
        this.intvl = interval;
        listDefuzzy.clear();
        listFuzzify.clear();
        
        for (int i = 0; i < 12; i++) {
             listWorking.remove(listActual.size()-12);
        }
        
        this.maxVal = getMaxValue();
        this.minVal = getMinValue();
        this.jumpVal = getLompatan();
        this.intvlPart = intervalPartition();
        this.medianIntvl = getMedianInterval();
        
        this.listFuzzify = fuzzifikasi();
        this.flr = fuzzyLogRel();
        this.matrix = matrixProb();
        defuzzifikasi();  
        predict_12();
    }
   
    public double getMaxValue(){
        double max = Integer.MIN_VALUE;
        for (int i = 0; i < (listWorking.size()); i++) {
            if (listWorking.get(i) >= max) {
                max = listWorking.get(i);
            }
        }

        double rem = max%25;
        if (rem < 12.5 || rem > 12.5) {
            maxVal = max + (25 - (rem));
         }else{
             maxVal = max + 12.5;
        }
            
        return maxVal;
    }
    
    public double getMinValue(){
        double min = Integer.MAX_VALUE;
        for (int i = 0; i < (listWorking.size()); i++) {
            if (listWorking.get(i) <= min) {
                min = listWorking.get(i);
            }
        }
        
        if (intvl == 20) {
            double rem = min%50;
            if (rem < 25 || rem > 25) {
                minVal = min - rem;
            }else{
                minVal = min - 25;
            }
        }else{
            double rem = min%25; 
           if (rem < 12.5 || rem > 12.5) {
               minVal = min - rem;
            }else{
               minVal = min - 12.5;
           }
        }
        
        return minVal;
    }
    
    public double getLompatan(){
        jumpVal = (maxVal - minVal)/intvl;
        
        return jumpVal;
    }
    
    private double[] intervalPartition(){
        intvlPart = new double[intvl];
        intvlPart[0] = minVal;
        
        for (int i = 1; i < intvl; i++) {
            intvlPart[i] = intvlPart[i-1] +  jumpVal;
        }
        
        return intvlPart;
    }
    
    private double[] getMedianInterval(){ 
       medianIntvl = new double[intvl];
        
        for (int i = 0; i < intvl; i++) {
            medianIntvl[i] = intvlPart[i] + (jumpVal/2);
        }
        
        return medianIntvl;
    }
    
    private int getFuzzy(double value){
        int fuzLing = 0;
        
        for (int j = 0; j < intvl; j++) {
            if (value >= intvlPart[j] && value <= intvlPart[j] + jumpVal){
               fuzLing = j+1;
            }
        }
       
        return fuzLing;
    }
    
    private List fuzzifikasi(){
        
        for (int i = 0; i < (listWorking.size()); i++) {
            for (int j = 0; j < intvl; j++) {
                if (listWorking.get(i) >= intvlPart[j] && listWorking.get(i) <= intvlPart[j] + jumpVal){
                    listFuzzify.add(j+1);
                }
            }
        }
        return listFuzzify;
    }
     
    private int[][] fuzzyLogRel(){
        flr = new int[intvl][intvl];
        
        for (int i = 0; i < listFuzzify.size() - 1; i++) {
            flr[listFuzzify.get(i) - 1][listFuzzify.get(i+1) - 1]= flr[listFuzzify.get(i) - 1][listFuzzify.get(i+1) - 1] + 1;
        }
        
        return flr;
    }
     
    private double[][] matrixProb(){
        matrix = new double[intvl][intvl];
        temp = new double[intvl];
        
        for (int i = 0; i < intvl; i++) {
            for (int j = 0; j < intvl; j++) {
                temp[i] += flr[i][j];  
            }
        }
        
        for (int i = 0; i < intvl; i++) {
            for (int j = 0; j < intvl; j++) {
                if (temp[i] == 0) {
                    matrix[i][j] = 0;
                }else{
                    matrix[i][j] = flr[i][j]/temp[i];
                }
            }
        }
        
        return matrix;
    } 
    
    private boolean checkMatrix(int x) {
        for (int i = 0; i < intvl; i++) {
            if (matrix[x-1][i] == 1) {
                return true;
            }
        }
       
        return false;
    }
    
    private double adjust(double temp, int i) {
        double dt1 = 0;
        double dt2 = (jumpVal/2)*(listFuzzify.get(i) - listFuzzify.get(i-1));

            if (matrix[listFuzzify.get(i-1)-1][listFuzzify.get(i-1)-1]  > 0 && listFuzzify.get(i) != listFuzzify.get(i-1)) {
                if (listFuzzify.get(i) > listFuzzify.get(i-1)) {
                    dt1 = jumpVal/2;
                }
               else {  
                    dt1 = -(jumpVal/2);
                }
            }

            temp = temp + dt1 + dt2;
        return temp;
    }
    
    private double adjust_12(double temp, int i) {
        double dt1 = 0;
        double dt2 = (jumpVal/2)*(i - listFuzzify.get(listFuzzify.size()-1));
        int x = listFuzzify.size()-1;
       
        if (i != listFuzzify.get(x) && matrix[listFuzzify.get(x)-1][listFuzzify.get(x)-1]  > 0) {
            if (i > listFuzzify.get(x)) {
                dt1 = jumpVal/2;
            }
           else {  
                dt1 = -(jumpVal/2);
            }
        }
        temp = temp + dt1 + dt2;
        
        return temp;
    }
    
    private void defuzzifikasi() {
        double tempHasil = 0;
        listDefuzzy.clear();
        
        TableData.add(0, new DataModel(listDate.get(0), listWorking.get(0), 0.0));
        listDefuzzy.add(0, 0.0);
        
        for (int i = 1; i < listFuzzify.size(); i++) {
            if (checkMatrix(listFuzzify.get(i-1)) == true) {
                tempHasil = medianIntvl[listFuzzify.get(i)-1];
                TableData.add(i, new DataModel(listDate.get(i), listWorking.get(i), tempHasil));
                listDefuzzy.add(i, tempHasil);
                tempHasil = 0;
            }
            else{
                double center = matrix[listFuzzify.get(i-1)-1][listFuzzify.get(i-1)-1]*listWorking.get(i-1);
                
                for (int j = 0; j < intvl; j++) {
                    tempHasil = (matrix[listFuzzify.get(i-1)-1][j]*medianIntvl[j]);
                    if (listFuzzify.get(i-1)-1 != j && tempHasil != 0) {
                        center += tempHasil;
                    }
                }
                TableData.add(i, new DataModel(listDate.get(i), listWorking.get(i),adjust(center, i)));
                listDefuzzy.add(i, adjust(center, i));
                tempHasil = 0;
                center = 0;
            } 
        }
        listDefuzzy.remove(0);
    }
    
    public void predict_12(){
        list_12.clear();
        double tempHasil = 0;
        double center = 0;

        for (int i = 0; i < 12; i++) {
            
            if (temp[listFuzzify.get( listFuzzify.size()-1)-1] == 0){
                tempHasil = medianIntvl[listFuzzify.get( listFuzzify.size()-1)-1];

                list_12.add(i, tempHasil);
                listWorking.add(tempHasil);
                listFuzzify.add(getFuzzy(tempHasil));   
            }else  if (checkMatrix(listFuzzify.get( listFuzzify.size()-1)) == true){
                for (int j = 0; j < intvl; j++) {
                    if (matrix[listFuzzify.get( listFuzzify.size()-1)-1][j] == 1) {
                        tempHasil = medianIntvl[j];
                    }
                }

                list_12.add(i, tempHasil);
                listWorking.add(tempHasil);
                listFuzzify.add(getFuzzy(tempHasil)); 
            }else{
                center = matrix[listFuzzify.get( listFuzzify.size()-1)-1][listFuzzify.get( listFuzzify.size()-1)-1]*listWorking.get(listWorking.size()-1);

                for (int j = 0; j < intvl; j++) {
                    tempHasil = (matrix[listFuzzify.get( listFuzzify.size()-1)-1][j]*medianIntvl[j]);

                    if (listFuzzify.get( listFuzzify.size()-1)-1 != j && tempHasil != 0) {
                        center += tempHasil;
                    }
                 }

                double val =  adjust_12(center, getFuzzy(center));
                
                if (center > maxVal || center < minVal) {
                    listFuzzify.clear();
                    listWorking.add(listWorking.size(), val);
                    this.maxVal = getMaxValue();
                    this.minVal = getMinValue();
                    this.jumpVal = getLompatan();
                    this.intvlPart = intervalPartition();
                    this.medianIntvl = getMedianInterval();
                    this.listFuzzify = fuzzifikasi();
                    
                    this.flr = fuzzyLogRel();
                    this.matrix = matrixProb();

                    listWorking.remove(listWorking.size()-1);
                    val =  adjust_12(center, listFuzzify.get( listFuzzify.size()-1));
                 }
                
                     list_12.add(i, val);
                     listWorking.add(val);
                     listFuzzify.add(getFuzzy(val));
            }    
            
            tempHasil = 0;
            center = 0;
            flr[listFuzzify.get( listFuzzify.size()-2) - 1][ listFuzzify.get( listFuzzify.size()-1) - 1]= 
                    flr[listFuzzify.get( listFuzzify.size()-2) - 1][ listFuzzify.get(listFuzzify.size()-1) - 1] + 1;
            
            temp[listFuzzify.get( listFuzzify.size()-2) - 1] += 1;
               
            for (int j = 0; j < intvl; j++) {
                    matrix[listFuzzify.get( listFuzzify.size()-2) - 1][j] = 
                            flr[listFuzzify.get( listFuzzify.size()-2) - 1][j]/temp[listFuzzify.get( listFuzzify.size()-2) - 1]; 
            }
        }
    }
    
    public String mapeDefuzzy(){
        double temp = 0;
        String mape;
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        
        for (int i = 0; i < listDefuzzy.size(); i++) {
            temp+= (double) Math.abs((listActual.get(i+1) - listDefuzzy.get(i))/listActual.get(i+1));
        }

        mape = decimalFormat.format(temp/listDefuzzy.size()*100);
        return mape;
    }

    public String mapePredict(){
        double temp = 0;
        String mape;
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        
        for (int i = 0; i < list_12.size(); i++) {
            temp+= (double) Math.abs((listActual.get(listActual.size()-(12-i)) - list_12.get(i))/listActual.get(listActual.size()-(12-i))) ;
        }
        
        mape = decimalFormat.format(temp/list_12.size()*100);
        return mape;
    }
    
    public String getPredict(){
        String listOut = "";
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        
        for (int i = 0; i < 12; i++) {
                        listOut = listOut + "\n" + listDate.get(listDate.size()-(12-i)) + ") " + decimalFormat.format(list_12.get(i)) + " || " + listActual.get(listActual.size()-(12-i));
        }

         return listOut;
    }
}