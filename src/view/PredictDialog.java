/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

/**
 *
 * @author Odorifqi
 */
public class PredictDialog extends JDialog {
    private JRadioButton dbl, abl, int15, int20;
    private ButtonGroup intervalBG;
    private JButton okButton, cancelButton;
    
    private String choice;
    
    public PredictDialog(JFrame parent) {
        super(parent, "Set interval", true);
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        
        dbl = new JRadioButton("Distribution-Based");
        abl = new JRadioButton("Average-Based");
        intervalBG = new ButtonGroup();
        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");
        
        dbl.setActionCommand("Distribution-Based");
        abl.setActionCommand("Average-Based");
        
        intervalBG.add(dbl);
        intervalBG.add(abl);
        abl.setSelected(true);
        
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choice = intervalBG.getSelection().getActionCommand();          
                setVisible(false);
            }
        });
        
         cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
         
        layoutControl();
        
        setSize(340, 250);
        setLocationRelativeTo(parent);
    }
    
    private void layoutControl(){
        JPanel controlsPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        
        int x = 15;
        Border title = BorderFactory.createTitledBorder("Choose Interval method");
        Border space = BorderFactory.createEmptyBorder(x,x,x,x);
        
        controlsPanel.setBorder(BorderFactory.createCompoundBorder(space, title));
        
        controlsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        Insets right = new Insets(0, 0, 0, 15);
       
        gc.gridx = 0;
        gc.gridy++;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = right;
        controlsPanel.add(dbl);
        
        gc.gridx++;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = right;
        controlsPanel.add(abl);
        
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        Dimension btnsize = cancelButton.getPreferredSize();
        okButton.setPreferredSize(btnsize);
        
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        
        setLayout(new BorderLayout());
        add(controlsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    public String getInterval(){
        return choice;
    }
}