
import javax.swing.SwingUtilities;
import view.Home;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Odorifqi
 */
public class Teskripsi {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // fungsi untuk memulai menjalankan perangkat lunak
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Home();
            }
        });
    }
    
}
