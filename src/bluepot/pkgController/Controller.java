/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluepot.pkgController;

import bluepot.pkgModel.Model;
import bluepot.pkgView.View;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.log4j.Level;

/**
 *
 * @author root
 */
public class Controller {

    private View view;
    private Model model;
    private GraphUpdater graphUpdater;
    private final Randomiser randomiser;
    public Controller(Model model, View view)  {

        this.view = view;
        this.model = model;

        model.log(Level.ALL, "Starting system..");

        view.setBtnSaveActionListener(new SaveSettingsActionListener());
        view.setBtnResetActionListener(new ResetActionListener());

        graphUpdater = new GraphUpdater(view, model);
        new Thread(graphUpdater).start();

        randomiser = new Randomiser(model, view);
        new Thread(randomiser).start();

        model.startServers(view);
        model.addObserver(view);


    }

    public class SaveSettingsActionListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {

            model.applyTableData();
            view.updateSensors();
        }
    }

    public class ResetActionListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {

            model.resetTableData();
        }
    }
 
}