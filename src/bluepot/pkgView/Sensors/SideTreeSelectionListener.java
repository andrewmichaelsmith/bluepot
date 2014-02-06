/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Sensors;

import bluepot.pkgView.View;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author andrew
 */
public class SideTreeSelectionListener implements TreeSelectionListener {

    View view;

        public SideTreeSelectionListener(View view)
        {
           this.view = view;
        }
        
        public void valueChanged(TreeSelectionEvent e) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode)  view.getLastSelectedPathComponent();

            if (node == null)
            //Nothing is selected.
            return;

            //FIXME: nasty, could be nicer if you have the time
            if(node.getLevel()==1)
            {
                view.setCard(node.toString() + "." + "Status");
            }
            {
                view.setCard(node.getParent().toString() + "." + node.toString());
            }
        }

}
