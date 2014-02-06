/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Sensors.Overview;

/**
 *
 * @author andrew
 */
import bluepot.pkgModel.Model;
 import javax.swing.table.*;
 import java.util.*;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
//TODO: THIS IS SERIOUSLY PLAGARISED CODE ( !! )

 public class RowEditorModel
 {
      private Hashtable data;
      private Model model;
      public RowEditorModel(Model model)
      {
          data = new Hashtable();
          this.model = model;

      }

     public void addEditorForRow(int row, TableCellEditor e )
     {
         data.put(new Integer(row), e);
     }

     public void removeEditorForRow(int row)
     {
         data.remove(new Integer(row));
     }

     public TableCellEditor getEditor(int row, int col)
     {
         if(col == DevicesTableModel.MINOR)
         {
            int tmpI = model.getTableRow(row).getMajor();
            String tmpS = model.majorIntToString(tmpI);
            String[] minorClassesList = model.getMinorClassesList(tmpS);
            JComboBox cmbMinorClass = new JComboBox(minorClassesList);
            addEditorForRow(row, new DefaultCellEditor(cmbMinorClass));

            return (TableCellEditor)data.get(new Integer(row));
         }
         else
         {
             return null;
         }
         
     }

 }