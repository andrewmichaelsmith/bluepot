/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Sensors.Overview;

import bluepot.pkgModel.ConfigureSensors.SensorTableRow;
import bluepot.pkgModel.Model;
import bluepot.pkgView.View;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author root
 */
public class DevicesTableModel extends AbstractTableModel {



    Model model;
    View view;

    public static final int ID = 0;
    public static final int NAME = 1;
    public static final int VISIBLE = 2;
    public static final int MAJOR = 3;
    public static final int MINOR = 4;
    public static final int OBEX = 5;
    public static final int RFCOMM = 6;
    public static final int L2CAP = 7;

    public static final int LOCKED = 8;




    public DevicesTableModel(Model model, View view)
    {
        this.model = model;
        this.view = view;

    }

    public int getRowCount() {

        return model.getNoOfDevices();
    }

    public int getColumnCount() {

        return 9;
    }

    @Override
    public Class getColumnClass(int columnIndex)
    {
        if(columnIndex == LOCKED) return Boolean.class;
        else if(columnIndex == OBEX) return Boolean.class;
        else if(columnIndex == RFCOMM) return Boolean.class;
        else if(columnIndex == L2CAP) return Boolean.class;
        else if(columnIndex == MAJOR) return JComboBox.class;
        else if(columnIndex == MINOR) return JComboBox.class;
        else if(columnIndex == VISIBLE) return JComboBox.class;
        else return String.class;
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
                case VISIBLE:
                    rtn = Model.discoverableIntToString(model.getTableRow(rowIndex).getDiscoverable());
                    break;
                case LOCKED:
                    rtn = model.getTableRow(rowIndex).isLocked();
                    break;
                case MAJOR:
                    rtn = model.majorIntToString(model.getTableRow(rowIndex).getMajor());
                    break;
                case MINOR:
                    rtn = model.minorIntToString(model.getTableRow(rowIndex).getMajor(), model.getTableRow(rowIndex).getMinor());
                    break;

                case OBEX:
                    rtn = model.getTableRow(rowIndex).isOBEX();
                    break;
                case L2CAP:
                    rtn = model.getTableRow(rowIndex).isL2CAP();
                    break;
                case RFCOMM:
                    rtn = model.getTableRow(rowIndex).isRFCOMM();
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
        switch(columnIndex) {

            case LOCKED:
            case OBEX:
            case RFCOMM:
            case L2CAP:
            case NAME:
            case MAJOR:
            case MINOR:
            case VISIBLE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        //TODO: Change to SWITCH
        if(columnIndex == LOCKED)
        {
            SensorTableRow tmp = model.getTableRow(rowIndex);
            tmp.setLocked(new Boolean(value.toString()));
            model.setTableRow(rowIndex, tmp);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
        else if(columnIndex == OBEX)
        {
            SensorTableRow tmp = model.getTableRow(rowIndex);
            tmp.setOBEX(new Boolean(value.toString()));
            model.setTableRow(rowIndex, tmp);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
        else if(columnIndex == RFCOMM)
        {
            SensorTableRow tmp = model.getTableRow(rowIndex);
            tmp.setRFCOMM(new Boolean(value.toString()));
            model.setTableRow(rowIndex, tmp);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
        else if(columnIndex == L2CAP)
        {
            SensorTableRow tmp = model.getTableRow(rowIndex);
            tmp.setL2CAP(new Boolean(value.toString()));
            model.setTableRow(rowIndex, tmp);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
        else if(columnIndex == NAME)
        {
            SensorTableRow tmp = model.getTableRow(rowIndex);
            tmp.setFriendlyName(String.valueOf(value));
            model.setTableRow(rowIndex, tmp);

            fireTableCellUpdated(rowIndex, columnIndex);

        }
        else if(columnIndex == MAJOR)
        {
            SensorTableRow tmp = model.getTableRow(rowIndex);
            int major = model.getMajorClassInt(String.valueOf(value));
            tmp.setMajor(major);
            tmp.setMinor(0);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
        else if(columnIndex == MINOR)
        {

            SensorTableRow tmp = model.getTableRow(rowIndex);
            int majorInt = model.getTableRow(rowIndex).getMajor();

            String majorString = model.majorIntToString(majorInt);
            String minorString = String.valueOf(value);

            int minorInt =  model.getMinorClassInt(majorString, minorString);
            tmp.setMinor(minorInt);
            fireTableCellUpdated(rowIndex, columnIndex);


        }
        else if(columnIndex == VISIBLE)
        {

            SensorTableRow tmp = model.getTableRow(rowIndex);

            int visible = Model.discoverableStringToInt(String.valueOf(value));
            ///TODO:how does this work?????????????????///
            tmp.setDiscoverable(visible);

        }



    }
    @Override
    public String getColumnName(int i)
    {
        String columnName = "";

        switch (i) {
            case NAME:  columnName = "Name"; break;
            case VISIBLE:  columnName = "Visibility"; break;
            case MAJOR:  columnName = "Major"; break;
            case MINOR:  columnName = "Minor"; break;
            case OBEX: columnName = "OBEX"; break;
            case L2CAP: columnName = "L2CAP"; break;
            case RFCOMM: columnName = "RFCOMM"; break;
            case LOCKED:  columnName = "Lock"; break;
            default: columnName = "";break;
        }

        return columnName;

    }

}