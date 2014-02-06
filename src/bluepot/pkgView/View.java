/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluepot.pkgView;

import bluepot.pkgView.Sensors.ConfigureSensors;
import bluepot.pkgModel.Model;
import bluepot.pkgModel.ObserverMessage;
import bluepot.pkgView.ControlPanel.Configuration;
import bluepot.pkgView.Attackers.Attackers;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import net.miginfocom.swing.MigLayout;


/**
 *
 * @author root
 */
public class View extends JFrame implements Observer {
   
    private final Configuration configuration;

  

    private JTabbedPane mainTabbedPane = new JTabbedPane();


    //FIXME: just move these around, we have methods referencing them!
    private ImageIcon icnMonitor = new ImageIcon("icons/monitor.png");
    private ImageIcon icnPhone = new ImageIcon("icons/phone.png");
    private ImageIcon icnUp = new ImageIcon("icons/up.png");
    private ImageIcon icnDown = new ImageIcon("icons/down.png");
    private ImageIcon icnSensors = new ImageIcon("icons/plugin_edit.png");
    private ImageIcon icnCP = new ImageIcon("icons/application_edit.png");
    private ImageIcon icnAttacker = new ImageIcon("icons/user.png");
    


    ConfigureSensors panelConfigureSensors;
    
    private Model model;


    private boolean[] selectedDeviceRows;

    private final Attackers panelAttackers;
    

    public View(Model model)  {



        super("Bluetooth Honeypot");
       

       
        Image im = Toolkit.getDefaultToolkit().getImage("icons/logo.gif");
        this.setIconImage(im);

        this.model = model;
        this.setLayout(new MigLayout("wrap 1"));


        //FIXME: MOVE THESE OUT OF HERE!!!!!
        selectedDeviceRows = new boolean[model.getNoOfDevices()];

        for (int i = 0; i < model.getNoOfDevices(); i++) {
            selectedDeviceRows[i] = false;
        }

        

        /* Adding to Main Panel */

        configuration = new Configuration(this,model);

        panelConfigureSensors = new ConfigureSensors(this, model);

        panelAttackers = new Attackers(model,this);

        mainTabbedPane.addTab("Sensors",icnMonitor ,panelConfigureSensors);
        JScrollPane configurationScrollPane = new JScrollPane();
        configurationScrollPane.getViewport().setView(configuration);
        mainTabbedPane.addTab("System", icnCP, configurationScrollPane);
        mainTabbedPane.addTab("Attackers", icnAttacker, panelAttackers);
        
        mainTabbedPane.setFont(new Font("Arial",Font.PLAIN, 15));
        this.add(mainTabbedPane, "w 100%, h 100%");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(new Dimension(800,600));
        this.setVisible(true);

        this.setExtendedState(this.getExtendedState() | View.MAXIMIZED_BOTH);
       



    }

   
    public ImageIcon getIcnDown() {
        return icnDown;
    }

    public ImageIcon getIcnMonitor() {
        return icnMonitor;
    }

    public ImageIcon getIcnPhone() {
        return icnPhone;
    }

    public ImageIcon getIcnUp() {
        return icnUp;
    }

    public boolean[] getSelectedDeviceRows() {
        return selectedDeviceRows;
    }

   
    public void setTabTitle(int i, String s) {
        mainTabbedPane.setTitleAt(i, s);

    }

    public void fireMainTableDataChanged() {
         panelConfigureSensors.getTableModel().fireTableDataChanged();
    }

    public void addLogToDevice(int i, String s) {
//        pnlArrayLocalDevices[i].addLog(s);
        panelConfigureSensors.addLog(i,s);


    }

    


   public void setBtnSaveActionListener(ActionListener al) {
         panelConfigureSensors.getBtnSave().addActionListener(al);
    }

   public void setBtnResetActionListener(ActionListener al) {
       panelConfigureSensors.getBtnReset().addActionListener(al);
   }

    public void update(Observable o, Object arg) {

        if(arg==null)
            panelAttackers.newAttackers();
        else
        {
        ObserverMessage barg = (ObserverMessage) arg;
        //barg.printMsg();
        this.addLogToDevice(barg.getID(), barg.getMsg());

        }


    }

    public void updateOBEXChart() {
        panelConfigureSensors.updateOBEXChart();

    }

    public DefaultMutableTreeNode getLastSelectedPathComponent() {

        return panelConfigureSensors.getLastSelectedPathComponent();

    }

    public void setCard(String string) {
        panelConfigureSensors.setCard(string);
    }

    public void fireSmallTableDataChanged() {

     panelConfigureSensors.fireSmallTableDataChanged();
    }

    public void updateSensors() {
        panelConfigureSensors.updateSensors();
    }
    
}

