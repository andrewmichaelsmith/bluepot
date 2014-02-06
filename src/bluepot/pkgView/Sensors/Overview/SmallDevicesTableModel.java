/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Sensors.Overview;

import bluepot.pkgModel.Model;
import bluepot.pkgView.View;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author root
 */
public class SmallDevicesTableModel extends AbstractTableModel {

    Model model;
    View view;

    public static final int ID = 0;
    public static final int NAME = 1;
    public static final int TYPE = 2;




    public SmallDevicesTableModel(Model model, View view)
    {
        this.model = model;
        this.view = view;

    }

    public int getRowCount() {

        return model.getNoOfDevices();
    }

    public int getColumnCount() {

        return 3;
    }

    @Override
    public Class getColumnClass(int columnIndex)
    {
       return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        //FIXME: how often is this updated?
        Object rtn = null;

            switch (columnIndex) {
                case ID:
                    rtn = model.getTableRow(rowIndex).getId();
                    break;
                case NAME:
                    rtn = model.getTableRow(rowIndex).getFriendlyName();
                    break;
                case TYPE:
                    String major = model.majorIntToString(model.getTableRow(rowIndex).getMajor());
                    String minor =  model.minorIntToString(model.getTableRow(rowIndex).getMajor(), model.getTableRow(rowIndex).getMinor());
                    rtn = major + " - " + minor;
                    break;
                default:
                    rtn = "";
                    break;


            }

        return rtn;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        
      return false;
    }

   
    @Override
    public String getColumnName(int i)
    {
        String columnName = "";

        switch (i) {
            case ID:  columnName = ""; break;
            case NAME:  columnName = "Name"; break;
            case TYPE:  columnName = "Type"; break;
            default: columnName = "Invalid Col. No.";break;
        }

        return columnName;

    }

}