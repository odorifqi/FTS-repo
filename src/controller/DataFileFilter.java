/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Odorifqi
 */
public class DataFileFilter extends FileFilter {
    
    //fungsi untuk memeriksa ekstensi file yang diinput
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        
        String name = file.getName();
        String ext = getExtension(name);
        
        if (ext == null) {
            return false;
        }
        
        if (ext.equals("csv")) {
            return true;
        }
        
        return false;
    }

    //fungsi untuk memberi keterangan ekstensi file yang bisa diinput
    @Override
    public String getDescription() {
        return "csv files (*.csv)";
    }
    
    //fungsi untuk mengambil ekstensi file yang telah diinput
    public static String getExtension(String name) {
        int pointIndex  = name.lastIndexOf('.');

        if (pointIndex == - 1) {
            return null;
        }
        
        if (pointIndex == name.length()-1) {
            return null;
        }
        
        return name.substring(pointIndex+1, name.length());
    }
}
