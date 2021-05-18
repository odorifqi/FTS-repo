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
    private JRadioButton int5, int10, int15, int20;
    private ButtonGroup intervalBG;
    private JButton okButton, cancelButton;
    
    private int interval;
    
    public PredictDialog(JFrame parent) {
        super(parent, "Set interval", true);
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        
        int5 = new JRadioButton("5");
        int10 = new JRadioButton("10");
        int15 = new JRadioButton("15");
        int20 = new JRadioButton("20");
        intervalBG = new ButtonGroup();
        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");
        
        int5.setActionCommand("5");
        int10.setActionCommand("10");
        int15.setActionCommand("15");
        int20.setActionCommand("20");
        
        intervalBG.add(int5);
        intervalBG.add(int10);
        intervalBG.add(int15);
        intervalBG.add(int20);
        int10.setSelected(true);
        
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interval = Integer.parseInt(intervalBG.getSelection().getActionCommand());          
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
        Border title = BorderFactory.createTitledBorder("Prediksi");
        Border space = BorderFactory.createEmptyBorder(x,x,x,x);
        
        controlsPanel.setBorder(BorderFactory.createCompoundBorder(space, title));
        
        controlsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        Insets right = new Insets(0, 0, 0, 15);
        
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = right;
        controlsPanel.add(new JLabel("Set Interval"), gc);
       
        gc.gridx = 0;
        gc.gridy++;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = right;
        controlsPanel.add(int5);
        
        gc.gridx++;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = right;
        controlsPanel.add(int10);
        
        gc.gridx++;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = right;
        controlsPanel.add(int15);
        
        gc.gridx++;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = right;
        controlsPanel.add(int20);
        
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        Dimension btnsize = cancelButton.getPreferredSize();
        okButton.setPreferredSize(btnsize);
        
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        
        setLayout(new BorderLayout());
        add(controlsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    public int getInterval(){
        return interval;
    }
}