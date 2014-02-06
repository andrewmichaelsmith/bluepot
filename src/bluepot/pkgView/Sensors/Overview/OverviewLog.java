/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Sensors.Overview;

import bluepot.pkgModel.Model;
import bluepot.pkgView.View;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTitledPanel;

/**
 *
 * @author andrew
 */
public class OverviewLog  extends JPanel{

    int sensorId;
    View view;
    Model model;
    private final JTextArea txtLog;
    private final JScrollPane scrollPane;


    public OverviewLog(int sensorId, View view, Model model) {

        super(new MigLayout("wrap 1"));
        this.sensorId = sensorId;
        this.view = view;
        this.model = model;

        txtLog = new JTextArea(30,100);
        txtLog.setEditable(false);
        txtLog.setBackground(Color.white);
        txtLog.setEditable(false);
        txtLog.setForeground(Color.black);
        txtLog.setAutoscrolls(true);
        txtLog.setDragEnabled(false);

        
        JXTitledPanel allLog = new JXTitledPanel("Sensor Logs");
        JPanel alLog = new JPanel(new MigLayout("wrap 1"));

        scrollPane = new JScrollPane();
        scrollPane.getViewport().add(txtLog);
        alLog.add(scrollPane);
        allLog.add(alLog);

        this.add(allLog,"w 100%, h 100%");

    }

    public void addLog(String s) {
         txtLog.setText(Model.getDateTime() + ": " + s + "\n" + txtLog.getText());
    }



}
