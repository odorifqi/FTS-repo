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
    private ArrayList<DataModel> arrayData = new ArrayList<DataModel>();
    private ArrayList<DataModel> arrayPre = new ArrayList<DataModel>();
    private List<Date> listDateChart = new ArrayList<Date>();
    private List<Double> listRaw = new ArrayList<Double>();
    private List<Double> listRawChart = new ArrayList<Double>();
    private List<Double> listPredict = new ArrayList<Double>();
    
    private ArrayList<DataModel> list_12 = new ArrayList<DataModel>();
    
    private List<String> listStringDate = new ArrayList<String>();
    private int intvl;
    private double maxVal;
    private double minVal;
    private double jumpVal;
    private double[] intvlPart;
    private List<DataModel> dataFuzzifikasi;
    private int[][] fuzzyLogRel;
    private double[][] matrix;
    private double[] adj;
    private double[] temp;
    
    private double[] medianInterval;
      
    public Controller(){}
    public List<Date> getListDateChart() {return listDateChart;}
    public List<Double> getListRaw() {return listRaw;}
    public List<Double> getListRawChart() {return listRawChart;}
    public List<Double> getListPredict() {return listPredict;}
    public ArrayList<DataModel> getDataTable(){return arrayData;}
    
    public void inputFile(File file) throws ParseException, IOException {
        String line = "";
        String csvSplit = ";";
        int iter = 0;

        arrayData.clear();
        arrayPre.clear();
        
        listDateChart.clear();
        listRaw.clear();
        listRawChart.clear();
        listPredict.clear();
        listStringDate.clear();
        
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
                
                arrayData.add(new DataModel(text[0], tempInt));
                listStringDate.add(text[0]);
                listRaw.add(tempInt);
                listDateChart.add(date);
                listRawChart.add(tempInt); 
            }  
            listDateChart.remove(0); 
            listRawChart.remove(0);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } br.close();
    }  

    public void prediksi(int interval){  
        this.intvl = interval;
        listPredict.clear();
        arrayPre.clear();
        
        this.maxVal = getMaxValue();
        this.minVal = getMinValue();
        this.jumpVal = getLompatan();
        this.intvlPart = intervalPartition();
        this.medianInterval = getMedianInterval();
        this.dataFuzzifikasi = fuzzifikasi();
        this.fuzzyLogRel = fuzzyLogRel();
        this.matrix = matrixProb();
        
        defuzzifikasi();  
        predict_12();
    }
   
    public double getMaxValue(){
        double max = Integer.MIN_VALUE;
        for (int i = 0; i < listRaw.size(); i++) {
            if (listRaw.get(i) >= max) {
                max = listRaw.get(i);
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
        for (int i = 0; i < listRaw.size(); i++) {
            if (listRaw.get(i) <= min) {
                min = listRaw.get(i);
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
    
    //NEW
    private double[] getMedianInterval(){ 
       medianInterval = new double[intvl];
        
        for (int i = 0; i < intvl; i++) {
            medianInterval[i] = intvlPart[i] + (jumpVal/2);
        }
        
        return medianInterval;
    }
    
    //NEW
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
        dataFuzzifikasi = new ArrayList<>();
        
        for (int i = 0; i < listRaw.size(); i++) {
            for (int j = 0; j < intvl; j++) {
                if (listRaw.get(i) >= intvlPart[j] && listRaw.get(i) <= intvlPart[j] + jumpVal){
                    dataFuzzifikasi.add(new DataModel(listStringDate.get(i), listRaw.get(i), j+1));
                }
            }
        }
        
        return dataFuzzifikasi;
    }
     
    private int[][] fuzzyLogRel(){
        fuzzyLogRel = new int[intvl][intvl];
        
        for (int i = 0; i < dataFuzzifikasi.size() - 1; i++) {
            fuzzyLogRel[dataFuzzifikasi.get(i).getIndex() - 1][dataFuzzifikasi.get(i+1).getIndex() - 1]= fuzzyLogRel[dataFuzzifikasi.get(i).getIndex() - 1][dataFuzzifikasi.get(i+1).getIndex() - 1] + 1;
        }
        
        return fuzzyLogRel;
    }
     
    private double[][] matrixProb(){
        matrix = new double[intvl][intvl];
        temp = new double[intvl];
        
        for (int i = 0; i < intvl; i++) {
            for (int j = 0; j < intvl; j++) {
                temp[i] += fuzzyLogRel[i][j];  
            }
        }
        
        for (int i = 0; i < intvl; i++) {
            for (int j = 0; j < intvl; j++) {
                if (temp[i] == 0) {
                    matrix[i][j] = 0;
                }else{
                    matrix[i][j] = fuzzyLogRel[i][j]/temp[i];
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
        double dt2 = (jumpVal/2)*(dataFuzzifikasi.get(i).getIndex() - dataFuzzifikasi.get(i-1).getIndex());
        if (dataFuzzifikasi.get(i).getIndex() != dataFuzzifikasi.get(i-1).getIndex()) {
            if (matrix[dataFuzzifikasi.get(i-1).getIndex()-1][dataFuzzifikasi.get(i-1).getIndex()-1]  > 0) {
                dt1 = jumpVal/2;
            }
            if (dataFuzzifikasi.get(i).getIndex() < dataFuzzifikasi.get(i-1).getIndex() && matrix[dataFuzzifikasi.get(i-1).getIndex()-1][dataFuzzifikasi.get(i-1).getIndex()-1]  > 0) {  
                dt1 = -(jumpVal/2);
            }
            temp = temp + dt1 + dt2;
            
            adj[i] = (dt1 + dt2);
        }
        return temp;
    }
    
    private void defuzzifikasi() {
        adj = new double[dataFuzzifikasi.size()];
        adj[0] = 0;
        double tempHasil = 0;

        listPredict.clear();
        
            arrayData.add(0, new DataModel(listStringDate.get(0), listRaw.get(0), 0.0));
            arrayPre.add(0, new DataModel(listRaw.get(0), listStringDate.get(0), 0.0));
            listPredict.add(0, 0.0);
        
        for (int i = 1; i < dataFuzzifikasi.size(); i++) {
            if (checkMatrix(dataFuzzifikasi.get(i-1).getIndex()) == true) {
                tempHasil = medianInterval[dataFuzzifikasi.get(i).getIndex()-1];
                arrayData.add(i, new DataModel(listStringDate.get(i), listRaw.get(i),tempHasil));
                arrayPre.add(i, new DataModel(listRaw.get(i), listStringDate.get(i), tempHasil));
                listPredict.add(i, tempHasil);
                
                tempHasil = 0;
            }
            else{
                double center = matrix[dataFuzzifikasi.get(i-1).getIndex()-1][dataFuzzifikasi.get(i-1).getIndex()-1]*dataFuzzifikasi.get(i-1).getPrice();
                
                for (int j = 0; j < intvl; j++) {
                    tempHasil = (matrix[dataFuzzifikasi.get(i-1).getIndex()-1][j]*medianInterval[j]);
                    if (dataFuzzifikasi.get(i-1).getIndex()-1 != j && tempHasil != 0) {
                        center += tempHasil;
                    }
                }
                arrayData.add(i, new DataModel(listStringDate.get(i), listRaw.get(i),adjust(center, i)));
                arrayPre.add(i, new DataModel(listRaw.get(i), listStringDate.get(i), center));
                listPredict.add(i, adjust(center, i));
                tempHasil = 0;
                center = 0;
            } 
        }
        listPredict.remove(0);
    }
    
    public void predict_12(){
        double tempHasil = 0;
        int fuzLing = 0;
        int index = dataFuzzifikasi.size()-1;
       
        if (temp[dataFuzzifikasi.get(index).getIndex()-1] == 0) {
            tempHasil = medianInterval[dataFuzzifikasi.get(index).getIndex()-1];
            list_12.add(0, new DataModel(0, tempHasil));
            fuzLing = getFuzzy(tempHasil);
            tempHasil = 0;
        }

         if (checkMatrix(dataFuzzifikasi.get(index).getIndex()) == true) {
             for (int j = 0; j < intvl; j++) {
                    if (matrix[dataFuzzifikasi.get(index).getIndex()-1][j] == 1) {
                         tempHasil = medianInterval[j];
                    }
                }
               
                list_12.add(0, new DataModel(0, tempHasil));
                fuzLing = getFuzzy(tempHasil);
                tempHasil = 0;
            }
         
         if (checkMatrix(dataFuzzifikasi.get(index).getIndex()) == false && temp[dataFuzzifikasi.get(index).getIndex()-1] != 0){
                double center = matrix[dataFuzzifikasi.get(index).getIndex()-1][dataFuzzifikasi.get(index).getIndex()-1]*dataFuzzifikasi.get(index).getPrice();
                
                for (int j = 0; j < intvl; j++) {
                    tempHasil = (matrix[dataFuzzifikasi.get(index).getIndex()-1][j]*medianInterval[j]);

                    if (dataFuzzifikasi.get(index).getIndex()-1 != j && tempHasil != 0) {
                        center += tempHasil;
                    }
                }
                fuzLing = getFuzzy(center);

                list_12.add(0, new DataModel(0, adjust(center, fuzLing)));
                
                tempHasil = 0;
                center = 0;
                
            } 
         
         for (int i = 1; i < 12; i++) {
                if (temp[fuzLing-1] == 0) {
                    tempHasil = medianInterval[fuzLing-1];
                    list_12.add(i, new DataModel(0, tempHasil));
                    fuzLing = getFuzzy(tempHasil);
                    tempHasil = 0;
                 }

                 if (checkMatrix(fuzLing) == true) {
                    for (int j = 0; j < intvl; j++) {
                           if (matrix[fuzLing-1][j] == 1) {
                                tempHasil = medianInterval[j];
                           }
                       }
               
                    list_12.add(i, new DataModel(0, tempHasil));
                    fuzLing = getFuzzy(tempHasil);
                    tempHasil = 0;
                }
         
            if (checkMatrix(fuzLing) == false && temp[fuzLing-1] != 0){
                   double center = matrix[fuzLing-1][fuzLing-1]*list_12.get(i-1).getPrice();

                   for (int j = 0; j < intvl; j++) {
                       tempHasil = (matrix[fuzLing-1][j]*medianInterval[j]);

                       if (fuzLing-1 != j && tempHasil != 0) {
                           center += tempHasil;
                       }
                   }

                   fuzLing = 0;

                    fuzLing = getFuzzy(center);

                   list_12.add(i, new DataModel(0, adjust(center, fuzLing)));

                   tempHasil = 0;
                   center = 0;

           }    
        }  
        
    }
    
    
    public String getMape(){
        double temp = 0;
        String mape;
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        for (int i = 0; i < listPredict.size(); i++) {
            temp+= (double) Math.abs((listRawChart.get(i) - listPredict.get(i))/listRawChart.get(i));
        }
        
        mape = decimalFormat.format(temp/listPredict.size()*100);
        return mape;
    }
    
    
    public String get_12(){
        String listOut = "";
        for (int i = 0; i < 12; i++) {
            listOut = listOut + "\n" + (i+1) + ")  " + list_12.get(i).getPrice();
        }

         return listOut;
    }
    
    public String getAdjust(){
        String preOut = "";
        for (int i = 0; i < dataFuzzifikasi.size(); i++) {
            preOut = preOut + "\n" + adj[i];
        }
        return preOut;
    }
}