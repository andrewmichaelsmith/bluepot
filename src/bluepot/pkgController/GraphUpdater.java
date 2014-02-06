/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgController;

import bluepot.pkgModel.Model;
import bluepot.pkgView.View;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andrew
 */
public class GraphUpdater implements Runnable {

    View view;
    Model model;

    public GraphUpdater(View view, Model model)
    {
       this.view = view;
       this.model = model;

    }
    public void run() {

        //FIXME: Not good
        while(true)
        {
        try {
            view.updateOBEXChart();
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        
        }

        }
    }

}
