/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Sensors.Sensor;

import bluepot.pkgModel.Model;
import bluepot.pkgView.StatFactory.GraphFactory;
import bluepot.pkgView.StatFactory.ListManager;
import bluepot.pkgView.View;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTitledPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author andrew
 */
public class Status extends JPanel {

    int sensorId;
    View view;
    Model model;
    private final GraphFactory graphCreator;
    private final ChartPanel panel;
    private final JTextArea txtLog;
    private final JScrollPane scrollPane;
    private final JLabel txtConnsRcvd;


    private ImageIcon icnTick = new ImageIcon("icons/tick.png");
    private ImageIcon icnAttacker = new ImageIcon("icons/user.png");
    private ImageIcon icnConn = new ImageIcon("icons/bug.png");
    private ImageIcon icnFiles = new ImageIcon("icons/folder.png");
    private ImageIcon icnRandom = new ImageIcon("icons/time.png");
    private ImageIcon icnPhone = new ImageIcon("icons/phone.png");
    private ImageIcon icnDev = new ImageIcon("icons/application_form.png");
    private ImageIcon icnTag = new ImageIcon("icons/tag_blue.png");
    private ImageIcon icnZm = new ImageIcon("icons/zoom.png");
    private final JLabel txtUniqueAttackers;
    private final JLabel txtDeviceName;
    private final JLabel txtDeviceClass;
    private final JLabel txtDeviceAddress;
    private final JLabel txtDeviceVisibility;
    private final ListManager listManager;


    public Status(int sensorId, View view, Model model)
    {
        super(new MigLayout("wrap 1"));
        this.view = view;
        this.model = model;
        this.sensorId = sensorId;


        Font txtFont = new Font("Arial", Font.BOLD, 12);

        JLabel title = new JLabel("<html><strong>Sensor " + (sensorId-1) + "</strong></html>");
        
           JLabel connectionsRcvd = new JLabel("Connections Rcvd:",icnConn,SwingConstants.LEFT);
        txtConnsRcvd = new JLabel("0");

        txtConnsRcvd.setFont(txtFont);

        JLabel deviceName = new JLabel("Device Name: ",icnPhone,SwingConstants.LEFT);
        txtDeviceName = new JLabel("-");

        JLabel deviceClass = new JLabel("Device Class: ",icnDev,SwingConstants.LEFT);
        txtDeviceClass = new JLabel("-");

        JLabel deviceAddress = new JLabel("Device Address: ",icnTag,SwingConstants.LEFT);
        txtDeviceAddress = new JLabel("-");

        JLabel deviceVisibility = new JLabel("Device Visibility: ", icnZm, SwingConstants.LEFT);
        txtDeviceVisibility = new JLabel("-");

        JLabel uniqueAttackers = new JLabel("Unique Attackers:",icnAttacker,SwingConstants.LEADING);
        txtUniqueAttackers = new JLabel("0");
        txtUniqueAttackers.setFont(txtFont);

        listManager = new ListManager();

        JXTitledPanel sensorStatus = new JXTitledPanel("Sensor Status");
        JPanel snsrStatus = new JPanel(new MigLayout("wrap 2"));

        JPanel snsrInfo = new JPanel(new MigLayout("wrap 2"));
        snsrInfo.setBorder(new TitledBorder("Sensor Information"));
        snsrInfo.add(title,"span 2");
        snsrInfo.add(deviceName);
        snsrInfo.add(txtDeviceName);
        txtDeviceName.setFont(txtFont);
        snsrInfo.add(deviceClass);
        snsrInfo.add(txtDeviceClass);
        txtDeviceClass.setFont(txtFont);
        snsrInfo.add(deviceAddress);
        snsrInfo.add(txtDeviceAddress);
        txtDeviceAddress.setFont(txtFont);
        snsrInfo.add(deviceVisibility);
        snsrInfo.add(txtDeviceVisibility);
        txtDeviceVisibility.setFont(txtFont);
        snsrInfo.add(connectionsRcvd);
        snsrInfo.add(txtConnsRcvd);
        snsrInfo.add(uniqueAttackers);
        snsrInfo.add(txtUniqueAttackers);



        graphCreator = new GraphFactory(model, (sensorId-1), "", "","Traffic", "Time", "Hits",GraphFactory.TYPE_TIME, listManager);
        graphCreator.updateData();
        JFreeChart chart = graphCreator.getChart();

        panel = new ChartPanel(chart);
        panel.setInitialDelay(0);


        txtLog = new JTextArea(30,100);
        txtLog.setEditable(false);
        txtLog.setBackground(Color.white);
        txtLog.setEditable(false);
        txtLog.setAutoscrolls(true);
        txtLog.setDragEnabled(false);


        txtLog.setForeground(Color.black);
        scrollPane = new JScrollPane();
        scrollPane.getViewport().add(txtLog);

        JXTitledPanel logPanel = new JXTitledPanel("Sensor Log  ");
        JPanel lgPanel = new JPanel(new MigLayout("wrap 1"));
        lgPanel.add(scrollPane, "w 100%");
        logPanel.add(lgPanel);

        snsrStatus.add(snsrInfo,"w 50%, h 100%");
        snsrStatus.add(panel);

        sensorStatus.add(snsrStatus);
       
         

         this.add(sensorStatus, "w 100%, h 40%");
        this.add(logPanel,"w 100%, h 60%");

        deviceChange();


    }
    public void deviceChange()
    {
        int i = (sensorId-1);
        txtDeviceName.setText(model.getSensor(i).getFriendlyName());
        txtDeviceAddress.setText(model.getSensor(i).getAddress());
        txtDeviceVisibility.setText(Model.discoverableIntToString(model.getSensor(i).getDiscoverable()));

        String major = model.majorIntToString(model.getTableRow(i).getMajor());
        String minor =  model.minorIntToString(model.getTableRow(i).getMajor(), model.getTableRow(i).getMinor());
        String classOfDevice = major + " - " + minor;

        txtDeviceClass.setText(classOfDevice);
    }


    public void updateGraphs() {

        graphCreator.updateData();

        txtConnsRcvd.setText(Integer.toString(listManager.getNoOfAttacks()));
        txtUniqueAttackers.setText(Integer.toString(listManager.getNoOfAttackers()));
        
    }


    public void addLog(String s)
    {
        txtLog.setText(Model.getDateTime() + ": " + s + "\n" + txtLog.getText());
    }

}
