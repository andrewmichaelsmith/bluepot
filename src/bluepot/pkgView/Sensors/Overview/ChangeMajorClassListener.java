/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Sensors.Overview;

import bluepot.pkgModel.Model;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
 *
 * @author andrew
 */
//TODO: Maybe move this to controller for CONSISTENCY!!!!!!!!
class ChangeMajorClassListener implements ActionListener {

    //DevicePanel dp;
    JPanel panel;
    Model model;



    ChangeMajorClassListener(Model model, JPanel panel) {
        this.model = model;
        this.panel = panel;
    }



    public void actionPerformed(ActionEvent ae) {
      //  panel.setMinorClasses(model.getMinorClassesList(panel.getSelectedMajor()));
    }

}
