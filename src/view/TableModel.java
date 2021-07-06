/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import model.DataModel;

/**
 *
 * @author Odorifqi
 */
public class TableModel extends AbstractTableModel{
    private ArrayList<DataModel> list;
    private String[] colnames = {"Time", "Price", "Defuzzy"};

    //fungsi set untuk menerima input
    public void setData(ArrayList<DataModel> list) {
        this.list = list;
    }
    
    //fungsi untuk melakukan reset data
    public void resetData() {
        list.clear();
    }
    
    //fungsi untuk mengatur baris dan kolom
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }
    
    //fungsi get untuk mengambil nilai
    @Override
    public Object getValueAt(int row, int col) {
        DataModel dataModel = list.get(row);
        DecimalFormat df = new DecimalFormat("0.00");
        
        switch(col){
           case 0-> {
               return dataModel.getTime();
           }
           case 1-> {
               return df.format(dataModel.getPrice());
           }
           case 2-> {
               return df.format(dataModel.getPredicted());
           }
       }
        
       return null;
    }
    
    //fungsi get untuk mengambil nama kolom
    @Override
    public String getColumnName(int column) {
        return colnames[column];
    }
}
