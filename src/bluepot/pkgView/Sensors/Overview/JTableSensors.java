/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Sensors.Overview;

import bluepot.pkgView.View;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author root
 */
public class JTableSensors extends JTable {


    protected RowEditorModel rm = null;
    View view;

    JTableSensors(View view, DevicesTableModel tableModel, RowEditorModel rowEditorModel) {
        super(tableModel);
        this.rm = rowEditorModel;
        this.view = view;

    }
    

    public void setRowEditorModel(RowEditorModel rm)
     {
         this.rm = rm;
     }

     public RowEditorModel getRowEditorModel()
     {
         return rm;
     }


    @Override
     public TableCellEditor getCellEditor(int row, int col)
     {
         TableCellEditor tmpEditor = null;
         tmpEditor = rm.getEditor(row,col);

         if (tmpEditor!=null)
             return tmpEditor;
         else
             return super.getCellEditor(row,col);
     }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

    }

    @Override
      public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
        Component returnComp = super.prepareRenderer(renderer, row, column);
        Color alternateColor = new Color(235,235,235);
        Color whiteColor = Color.WHITE;
        
        if (!returnComp.getBackground().equals(getSelectionBackground())){
            Color bg = (row % 2 == 0 ? alternateColor : whiteColor);
            returnComp .setBackground(bg);
            bg = null;
        }
        return returnComp;
      }

}