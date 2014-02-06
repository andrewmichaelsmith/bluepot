/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Sensors;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.jfree.chart.urls.CustomCategoryURLGenerator;

/**
 *
 * @author andrew
 */
public class SideTreeCellRenderer extends DefaultTreeCellRenderer {

    

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        
        String comp = value.toString();

        ImageIcon theIcon = new ImageIcon("icons/phone.png");
        
        if(comp.equals("Overview"))
            theIcon = new ImageIcon("icons/table.png");
        else if(comp.equals("Config"))
            theIcon = new ImageIcon("icons/cog.png");
        /*else if(comp.equals("Status"))
            theIcon = new ImageIcon("icons/application_view_detail.png");*/
        else if(comp.equals("Log"))
            theIcon = new ImageIcon("icons/text_align_left.png");

        setIcon(theIcon);

        return this;
    }

}
