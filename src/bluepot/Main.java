/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot;

/**
 *
 * @author csugdn
 */

import bluepot.pkgController.Controller;
import bluepot.pkgModel.Model;
import bluepot.pkgView.View;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.pushingpixels.substance.api.skin.*;


public class Main extends JFrame {

     public static final int DEBUG = 1;
     private static final int PORT = 61234;
     private static ServerSocket socket;


      public static void main(String[] args)  {

      //Only run on Linux
      if(!System.getProperty("os.name").contains("Linux"))
      {
          System.out.println("Detected :" + System.getProperty("os.name"));
          System.out.println("Closing, " + System.getProperty("os.name") + " not supported. Linux only, try using a virutal machine!");

          System.exit(1);
      }

       try {
        socket = new ServerSocket(PORT,0,InetAddress.getByAddress(new byte[] {127,0,0,1}));
      }
      catch (BindException e) {
        System.err.println("Bluepot is already running.");
        System.exit(1); 
      }
      catch (IOException e) {
        System.err.println("Unexpected error.");
        e.printStackTrace();
        System.exit(2);
      }

      try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    try {
                        UIManager.setLookAndFeel(new SubstanceModerateLookAndFeel());
                    } catch (UnsupportedLookAndFeelException ex) {
                        System.out.println("Unsupported look and feel error: " + ex.toString());
                    }
                    Model model = new Model();
                    View view = new View(model);
                    Controller controller = new Controller(model, view);
                }
            });
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (InvocationTargetException ex) {
            System.err.println("InvocationTargetException.");
            System.exit(1); 
        }
      }

}
