/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Sensors;

import bluepot.pkgModel.Model;
import bluepot.pkgView.Sensors.Overview.DevicesTableModel;
import bluepot.pkgView.Sensors.Overview.OverviewConfig;
import bluepot.pkgView.Sensors.Overview.OverviewLog;
import bluepot.pkgView.Sensors.Overview.OverviewStatus;
import bluepot.pkgView.Sensors.Sensor.Status;
import bluepot.pkgView.View;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTitledPanel;

/**
 *
 * @author andrew
 */
public class ConfigureSensors extends JPanel {

    View view;
    Model model;
   
    private JPanel[][] panels;
    private JTree sideTree;
    private JPanel cards;

  
    public ConfigureSensors (View view, Model model) {

        this.view = view;
        this.model = model;

        this.setLayout(new MigLayout("wrap 2"));
        
        panels = new JPanel[model.getNoOfDevices()+1][3];
        cards = new JPanel(new CardLayout());

        int number = model.getNoOfDevices()+1;

        //Status, Config, Loig
     

        for(int i = 0; i < number; i++)
        {
            if(i > 0)
            {
                panels[i][0] = new Status(i, view, model);
                cards.add(panels[i][0],"Sensor " + (i-1) + ".Status");

            }
            else
            {
                panels[i][0] = new OverviewConfig(i, view, model);
                panels[i][1] = new OverviewStatus(i, view, model);
                panels[i][2] = new OverviewLog(i, view, model);
                cards.add(panels[i][1],"Overview.Status");
                cards.add(panels[i][0],"Overview.Config");
                cards.add(panels[i][2],"Overview.Log");
            
            }
        }

        //TREE TREE TREE TREE TREE TREE TREE TREE TREE TREE
        JScrollPane scrollTree = new JScrollPane();
        JXTitledPanel pnlTree = new JXTitledPanel("Sensors", scrollTree);
     
        
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Sensors");

        creatNodes(rootNode);
        UIManager.getDefaults().put("TreeUI", BasicTreeUI.class.getName() );
        sideTree = new JTree(new DefaultTreeModel(rootNode));
       

        

       sideTree.setCellRenderer(new SideTreeCellRenderer());
      
     
       sideTree.setRootVisible(false);

       sideTree.getSelectionModel().setSelectionMode
            (TreeSelectionModel.SINGLE_TREE_SELECTION);

       sideTree.addTreeSelectionListener(new SideTreeSelectionListener(view));

        scrollTree.getViewport().add(sideTree);
        pnlTree.setMinimumSize(new Dimension(150,600));
        

        int row = 0;
        while (row < sideTree.getRowCount()) {
            sideTree.expandRow(row);
            row++;
        }

        //END TREE TREE TREE TREE

        scrollTree.setBorder(new EmptyBorder(0,0,0,0));
        //sideTree.setBorder(new EmptyBorder(0,0,0,0));
         sideTree.putClientProperty("JTree.lineStyle", "Angled");
        


        Font topBarFont = new Font("Arial", 13, Font.LAYOUT_LEFT_TO_RIGHT);
      
        
        this.add(pnlTree, "span 1 2, width 200px, height 100%");
        this.add(cards, "h 100%, w 100%");
        
       

    }


    
    public void addLog(int i, String s)
    {
        Status tmp = (Status) panels[i+1][0];
        tmp.addLog(s);
        OverviewLog tmp2 = (OverviewLog) panels[0][2];
        tmp2.addLog("[Device " + i + "] " + s);

    }


    private void creatNodes(DefaultMutableTreeNode rootNode) {

        DefaultMutableTreeNode sensorNode;

        sensorNode = new DefaultMutableTreeNode("Overview");

        sensorNode.add(new DefaultMutableTreeNode("Config"));
       
        sensorNode.add(new DefaultMutableTreeNode("Log"));

        rootNode.add(sensorNode);

        
        for(int i = 0; i < model.getNoOfDevices(); i++)
        {
            sensorNode = new DefaultMutableTreeNode("Sensor " + i);
            
            rootNode.add(sensorNode);
            

        }

    }

    public DefaultMutableTreeNode getLastSelectedPathComponent() {
        return (DefaultMutableTreeNode) sideTree.getLastSelectedPathComponent();
    }

    public void setCard(String string) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards,string);
        
    }

    public DevicesTableModel getTableModel() {
        OverviewConfig tmp = (OverviewConfig) panels[0][0];
        return tmp.getTableModel();
    }



    public JButton getBtnSave() {
        OverviewConfig tmp = (OverviewConfig) panels[0][0];
        return tmp.getBtnSave();
    }

    public JButton getBtnReset() {
        OverviewConfig tmp = (OverviewConfig) panels[0][0];
        return tmp.getBtnReset();
    }

    public void updateOBEXChart() {
        OverviewStatus tmp = (OverviewStatus) panels[0][1];
        tmp.updateCharts();

        for(int i = 1; i < (model.getNoOfDevices()+1); i++)
        {
            //FIXME: Coommeeoonn
            Status tmp2 = (Status) panels[i][0];
            tmp2.updateGraphs();
        }
    }

    public void fireSmallTableDataChanged() {
        OverviewStatus tmp = (OverviewStatus) panels[model.getNoOfDevices()][0];
        tmp.updateSmallTable();
    }

    public void updateSensors() {
         int number = model.getNoOfDevices()+1;
        for(int i = 1; i < number; i++)
        {
            Status tmp2 = (Status) panels[i][0];
            tmp2.deviceChange();
        }
    }

}
