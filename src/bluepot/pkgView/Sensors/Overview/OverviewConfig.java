/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Sensors.Overview;

import bluepot.pkgModel.Model;
import bluepot.pkgView.View;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTitledPanel;

/**
 *
 * @author andrew
 */
public class OverviewConfig extends JPanel {

    int sensorId;
    View view;
    Model model;



    private DevicesTableModel tableModel;

    private final JComboBox cmbMajorClass;
    private RowEditorModel rowEditorModel;
    private JButton btnSave = new JButton("Save & Apply");
    private JButton btnReset = new JButton("Reset");
    private final JComboBox cmbVisibleList;
    private final JTableSensors devicesTable;




    public OverviewConfig(int sensorId, View view, Model model) {

        this.setLayout(new MigLayout("wrap 1"));
        this.sensorId = sensorId;
        this.view = view;
        this.model = model;

        
          Font smallFont = new Font("Arial", Font.PLAIN, 9);

        rowEditorModel = new RowEditorModel(model);

        

        JPanel deviceJPanelContent = new JPanel(new MigLayout("wrap 3"));


  


        /* Devices Table */
        tableModel = new DevicesTableModel(model, view);
        devicesTable = new JTableSensors(view, tableModel,rowEditorModel);
        devicesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        devicesTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        devicesTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        devicesTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        devicesTable.getColumnModel().getColumn(5).setPreferredWidth(5);


        devicesTable.setColumnSelectionAllowed(false);
        devicesTable.setDragEnabled(false);
        devicesTable.setCellSelectionEnabled(false);
        devicesTable.setRowSelectionAllowed(true);

        JScrollPane scrollPane = new JScrollPane(devicesTable);
        
        devicesTable.setMinimumSize(new Dimension(900, 20 + model.getNoOfDevices() * 19));
        devicesTable.setMaximumSize(new Dimension(900, 20 + model.getNoOfDevices() * 19));
        scrollPane.setMaximumSize(new Dimension(900, 20 + model.getNoOfDevices() * 19));
        scrollPane.setMinimumSize(new Dimension(900, 20 + model.getNoOfDevices() * 19));

        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));


        //Drop down for visibility
        TableColumn visibilityColumn = devicesTable.getColumnModel().getColumn(DevicesTableModel.VISIBLE);
        String[] visibleList = { "Full", "Limited", "Invisible" };
        cmbVisibleList = new JComboBox(visibleList);
        visibilityColumn.setCellEditor(new DefaultCellEditor(cmbVisibleList));


        //Major + Minor shit
        TableColumn majorColumn = devicesTable.getColumnModel().getColumn(DevicesTableModel.MAJOR);

        String[] majorClassesList = model.getMajorClassesList();
        cmbMajorClass = new JComboBox(majorClassesList);
        cmbMajorClass.addActionListener(new ChangeMajorClassListener(model,this));
        majorColumn.setCellEditor(new DefaultCellEditor(cmbMajorClass));


        deviceJPanelContent.add(scrollPane, "span 3, w 100%");
       

        deviceJPanelContent.add(btnSave);
        deviceJPanelContent.add(btnReset);


        JXTitledPanel configureSensors = new JXTitledPanel("Configure Sensors");
        configureSensors.add(deviceJPanelContent);

         this.add(configureSensors, "width 100%");

    }


    public DevicesTableModel getTableModel() {
        return tableModel;
    }

    public void setMinorClasses(String[] minorClasses)
    {

        TableColumn minorColumn = devicesTable.getColumnModel().getColumn(DevicesTableModel.MINOR);

    }


    public JButton getBtnReset() {
        return btnReset;
    }

     

    public JButton getBtnSave() {
        return btnSave;
    }


}
