/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Sensors.Overview;

import bluepot.pkgModel.Model;
import bluepot.pkgView.StatFactory.GraphFactory;
import bluepot.pkgView.StatFactory.GraphOverall;
import bluepot.pkgView.StatFactory.GraphSensors;
import bluepot.pkgView.StatFactory.ListManager;
import bluepot.pkgView.StatFactory.ListManagerOBEX;
import bluepot.pkgView.View;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTitledPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author andrew
 */
public class OverviewStatus  extends JPanel{

    int sensorId;
    View view;
    Model model;
    private final GraphFactory obexFilesRecievedGraph;
    private final ChartPanel panel1;
    private final ChartPanel panel2;
    private final SmallDevicesTableModel tableModel;
    private final ListManagerOBEX obexFilesRecievedListManager;
    private final ListManager allListManager;
    private final GraphOverall overallGraph;
    private final ChartPanel panel4;
    private final JLabel last10Attackers;
    private final JLabel top10Attackers;
    private final JLabel last10Files;
    private JLabel txtSensorsOnline;
    private JLabel txtConnsRcvd;
    private JLabel txtUniqueAttackers;
    private JLabel txtFilesRcvd;
    private JLabel txtRandomiserEnabled;

    private ImageIcon icnTick = new ImageIcon("icons/tick.png");
    private ImageIcon icnAttacker = new ImageIcon("icons/user.png");
    private ImageIcon icnConn = new ImageIcon("icons/bug.png");
    private ImageIcon icnFiles = new ImageIcon("icons/folder.png");
    private ImageIcon icnRandom = new ImageIcon("icons/time.png");
    private final ListManager sensorsOverallManager;
    private final GraphSensors sensorsOverallGraph;
    
    





    public OverviewStatus(int sensorId, View view, Model model) {

        super(new MigLayout("wrap 2"));

        this.sensorId = sensorId;
        this.view = view;
        this.model = model;

   
        Font headerFont = new Font("Arial", Font.BOLD, 14);

        //TOP LEFT
        JPanel mainStatus = new JPanel(new MigLayout("", "[][]150[][]"));
        JXTitledPanel mainStatusWithTitle = new JXTitledPanel("System Overview", mainStatus);

        Font txtFont = new Font("Arial", Font.BOLD, 12);

        JLabel sensorsOnline = new JLabel("Sensors Online:",icnTick,SwingConstants.LEFT);
        txtSensorsOnline = new JLabel();
        txtSensorsOnline.setText(Integer.toString(model.getNoOfDevices()));
        txtSensorsOnline.setFont(txtFont);

        JLabel connectionsRcvd = new JLabel("Connections Rcvd:",icnConn,SwingConstants.LEFT);
        txtConnsRcvd = new JLabel("0");
        txtConnsRcvd.setFont(txtFont);

        JLabel uniqueAttackers = new JLabel("Unique Attackers:",icnAttacker,SwingConstants.LEADING);
        txtUniqueAttackers = new JLabel("0");
        txtUniqueAttackers.setFont(txtFont);


        JLabel filesRcvd = new JLabel("Files Recieved",icnFiles,SwingConstants.LEFT);
        txtFilesRcvd = new JLabel("0");
        
        txtFilesRcvd.setFont(txtFont);

        JLabel randomiserEnabled = new JLabel("Randomiser Enabled:",icnRandom,SwingConstants.LEFT);
        txtRandomiserEnabled = new JLabel("True");
        
        txtRandomiserEnabled.setFont(txtFont);

        mainStatus.add(sensorsOnline, "cell 0 0");
        mainStatus.add(txtSensorsOnline,"cell 1 0");


        mainStatus.add(connectionsRcvd, "cell 0 1");
        mainStatus.add(txtConnsRcvd, "cell 1 1");

        mainStatus.add(uniqueAttackers, "cell 0 2");
        mainStatus.add(txtUniqueAttackers,"cell 1 2");

        mainStatus.add(filesRcvd, "cell 0 3");
        mainStatus.add(txtFilesRcvd, "cell 1 3");

        mainStatus.add(randomiserEnabled, "cell 0 4");
        mainStatus.add(txtRandomiserEnabled, "cell 1 4");
        //TOPRIGHT
        JPanel deviceListPnl = new JPanel(new MigLayout("wrap 1"));
        JXTitledPanel deviceListPnlWithTitle = new JXTitledPanel("Available Devices", deviceListPnl);

        tableModel = new SmallDevicesTableModel(model, view);
        JTable smallDevicesTable = new JTable(tableModel);

        smallDevicesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        smallDevicesTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        smallDevicesTable.getColumnModel().getColumn(0).setMaxWidth(20);
        smallDevicesTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        smallDevicesTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        smallDevicesTable.setColumnSelectionAllowed(false);
        smallDevicesTable.setRowSelectionAllowed(false);
        smallDevicesTable.setCellSelectionEnabled(false);
        
        JScrollPane smallJViewport = new JScrollPane(smallDevicesTable);
        smallDevicesTable.setFillsViewportHeight(true);
        
        deviceListPnlWithTitle.add(smallJViewport);


        JXTitledPanel graphsWithTitle = new JXTitledPanel("Graphs");
        JPanel graphs = new JPanel(new MigLayout("wrap 2"));

        obexFilesRecievedListManager = new ListManagerOBEX();
        allListManager = new ListManager();
        sensorsOverallManager = new ListManager();

        sensorsOverallGraph = new GraphSensors(model, sensorsOverallManager);
        overallGraph = new GraphOverall(model,allListManager);


        obexFilesRecievedGraph = new GraphFactory(model, -1, "obexpush", "filercvd","Files Recieved", "Time", "No. Of Files",GraphFactory.TYPE_BAR, obexFilesRecievedListManager);
        //obexFilesRecievedGraph.updateData();

        

       
      

        JFreeChart overallChart = overallGraph.getChart();
        JFreeChart obexFRG = obexFilesRecievedGraph.getChart();

        JFreeChart sensorsChart = sensorsOverallGraph.getChart();
        
        panel1 = new ChartPanel(obexFRG);
        panel1.setInitialDelay(0);

        panel2 = new ChartPanel(sensorsChart);
        panel2.setInitialDelay(0);


        panel4 = new ChartPanel(overallChart);

        JPanel listsPanel = new JPanel(new MigLayout("wrap 3","[center][center][center]","[center][center][center]"));

        JPanel last10Panel = new JPanel(new MigLayout("wrap 1"));
        JPanel top10Panel = new JPanel(new MigLayout("wrap 1"));
        JPanel lastFilesPanel = new JPanel(new MigLayout("wrap 1"));
        
        JLabel last10 = new JLabel("<html><strong><h2>Last 10 Attackers</h2></strong></html>");
        JLabel top10 = new JLabel("<html><strong><h2>Top 10 Attackers</h2></strong></html>");
        JLabel lastFiles = new JLabel("<html><strong><h2>Last 10 Files</h2></strong></html>");

        last10Attackers = new JLabel("<html><ol><li></li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li></ol></html>");
        top10Attackers = new JLabel("<html><ol><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li></ol></html>");
        last10Files = new JLabel("<html><ol><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li><li>-</li></ol></html>");

        last10Panel.add(last10);
        last10Panel.add(last10Attackers,"align left");

        top10Panel.add(top10);
        top10Panel.add(top10Attackers, "align left");

        lastFilesPanel.add(lastFiles);
        lastFilesPanel.add(last10Files, "align left");

        listsPanel.add(last10Panel, "w 33%");
        listsPanel.add(top10Panel, "w 33%");
        listsPanel.add(lastFilesPanel, "w 33%");

        graphs.add(panel4);
        graphs.add(listsPanel,"w 50%, h 33%");
        graphs.add(panel1);
        graphs.add(panel2);
        //graphs.add(panel3,"wrap");
        
        graphsWithTitle.add(graphs);


        this.add(mainStatusWithTitle, "width 50%, growy");
        this.add(deviceListPnlWithTitle, "width 50%");
        this.add(graphsWithTitle,"w 100%, span 2");

    }


    public void updateSmallTable() {
        tableModel.fireTableDataChanged();
    }
    public void updateCharts() {

        obexFilesRecievedGraph.updateData();
        overallGraph.updateData();
        sensorsOverallGraph.updateData();



        last10Attackers.setText(allListManager.getRecentAttackers(10));
        top10Attackers.setText(allListManager.getTopVolumeAttackers(10));
        last10Files.setText(obexFilesRecievedListManager.getRecentFiles(10));

        txtFilesRcvd.setText(Integer.toString(obexFilesRecievedListManager.getNoOfFiles()));

        txtConnsRcvd.setText(Integer.toString(allListManager.getNoOfAttacks()));

        txtUniqueAttackers.setText(Integer.toString(allListManager.getNoOfAttackers()));

        if(model.getSettings().isRandomEnabled())
        {
            txtRandomiserEnabled.setText("True");
        }
        else
        {
            txtRandomiserEnabled.setText("False");
        }
    }



}
