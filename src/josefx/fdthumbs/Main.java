/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package josefx.fdthumbs;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Displays the thumbnail viewer.
 * This application is based on the freedesktop.org specifikation for
 * thumbnails.
 * @author josefx
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }
            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                MainFrame mf = new MainFrame();
                mf.setVisible(true);
                mf.updateTable();

            }
        });
    }

}
