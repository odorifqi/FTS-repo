/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Odorifqi
 */
public class MapePanel extends JPanel{
    private JTextField mape;
    
    public MapePanel(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());
        mape = new JTextField(10);
        mape.setEditable(false);
        mape.setFont(new Font("Helvetica", Font.PLAIN, 14));
        add(new JLabel("  Tingkat Error(%):  "), BorderLayout.WEST);
        add(mape);
    }
    
    public void setText(String mapeVal){
        mape.setText(" " + mapeVal);
    }
}
