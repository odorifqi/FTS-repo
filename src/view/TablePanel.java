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
    private TableModel tableModel = new TableModel();
  
    public TablePanel() {
        Dimension dim = getSize();
        dim.width = 180;
        setPreferredSize(dim);
        
        tableModel = new TableModel();
        table = new JTable(tableModel);
        
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER); 
    }
    
    public void setData(ArrayList<DataModel> rdm){
        tableModel.setData(rdm);
    }
    
    public void resetData(){
        tableModel.resetData(); 
    }
    
    public void refresh(){
        tableModel.fireTableDataChanged();  
    }
}
