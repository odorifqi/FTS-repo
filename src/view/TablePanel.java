/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import model.DataModel;

/**
 *
 * @author Odorifqi
 */
public class TablePanel extends JPanel{
    private JTable table;
    private TableModel tableModel;
  
    //konstruktor kelas
    public TablePanel() {
        Dimension dim = getSize();
        dim.width = 180;
        setPreferredSize(dim);
        
        tableModel = new TableModel();
        table = new JTable(tableModel);
        
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER); 
    }
    
    //fungsi set untuk menerima input
    public void setData(ArrayList<DataModel> rdm){
        tableModel.setData(rdm);
    }
    
    //fungsi untuk menjalankan reset data
    public void resetData(){
        tableModel.resetData(); 
    }
    
    //fungsi untuk melakukan refresh jika terjadi perubahan data
    public void refresh(){
        tableModel.fireTableDataChanged();  
    }
}
