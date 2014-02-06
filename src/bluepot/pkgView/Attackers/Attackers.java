/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.Attackers;

import bluepot.pkgModel.Model;
import bluepot.pkgView.View;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTitledPanel;

/**
 *
 * @author andrew
 */
public class Attackers extends JPanel {

    Model model;
    View view;

    private String[] listOfAttackers;
    private JComboBox selectAttacker;
    private JTextArea txtLog;

    private boolean noAttackers = true;
    private final JPanel mnPanel;
    private final JPanel lgPanel;
    private final JXTitledPanel mainPanel;
    private final JXTitledPanel logPanel;
    private final JLabel hits;
    private final JLabel firstSeen;
    private final JLabel lastSeen;
    private final JLabel txtHits;
    private final JLabel txtFirstSeen;
    private final JLabel txtlastSeen;
    private final JPanel infoPanel;
    private final JLabel choose;
    private final JLabel none;
    private final TitledBorder infoBorder;
    private final Font smFont;

    private String lastSelected = "";
    private final JScrollPane txtScroll;
    
    public Attackers(Model model, View view)
    {

        super(new CardLayout());
        this.setLayout(new GridBagLayout());
        this.model = model;
        this.view = view;

        none = new JLabel("Currently no attack data.",JLabel.CENTER);
        none.setVerticalAlignment(JLabel.CENTER);
        Font txtFont = new Font("Arial", Font.BOLD, 25);
        smFont = new Font("Arial", Font.BOLD, 1);

        none.setFont(txtFont);
        none.setForeground(Color.gray);

        this.add(none);

        //
        choose = new JLabel("Choose an attacker: ");

        mnPanel = new JPanel(new MigLayout("wrap 2"));
        lgPanel = new JPanel(new MigLayout("wrap 1"));
        mainPanel = new JXTitledPanel("Attacker",mnPanel);
        logPanel = new JXTitledPanel("Log",lgPanel);

        hits = new JLabel("Hits: ");
        firstSeen = new JLabel("First Seen: ");
        lastSeen = new JLabel("Last Seen: ");

        txtHits = new JLabel("0");
        txtFirstSeen = new JLabel("-");
        txtlastSeen = new JLabel("-");


        infoPanel = new JPanel(new MigLayout("wrap 2"));

        selectAttacker = new JComboBox();

        selectAttacker.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if(selectAttacker.getItemCount()>0) {
                   setTextBox(selectAttacker.getSelectedItem().toString());
                    
            }
            }
        });


        txtScroll = new JScrollPane();

        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setBackground(Color.white);
        txtLog.setEditable(false);

        txtLog.setForeground(Color.black);
        txtLog.setAutoscrolls(true);
        txtLog.setDragEnabled(false);

        txtScroll.getViewport().setView(txtLog);
        infoBorder = new TitledBorder("Information");

    }

    public void startPanel()
    {

        none.setVisible(false);
        none.setFont(smFont);
        this.setLayout(new MigLayout("wrap 1"));
        listOfAttackers = (String[]) model.getAttackerList().keySet().toArray(new String[model.getAttackerList().keySet().size()]);

      
        infoPanel.add(hits);
        infoPanel.add(txtHits);
        /*infoPanel.add(firstSeen);
        infoPanel.add(txtFirstSeen);
        infoPanel.add(lastSeen);
        infoPanel.add(txtlastSeen);*/

        infoPanel.setBorder(infoBorder);

        mnPanel.add(choose);
        mnPanel.add(selectAttacker);
        mnPanel.add(infoPanel,"span 2");

        lgPanel.add(txtScroll,"w 100%, h 100%");

        this.add(mainPanel,"w 100%, h 30%");
        this.add(logPanel,"w 100%, h 70%");

        
    }

    public void newAttackers() {

        if(noAttackers)
        {
            noAttackers = false;
            startPanel();

        }
        
        listOfAttackers = (String[]) model.getAttackerList().keySet().toArray(new String[model.getAttackerList().keySet().size()]);
        selectAttacker.removeAllItems();
        for(int i = 0; i < listOfAttackers.length; i++)
        {
            selectAttacker.addItem(listOfAttackers[i]);
        }
        
        setTextBox(selectAttacker.getSelectedItem().toString());
    

    }

    public void setTextBox(String attacker)
    {
        txtLog.setText("");
        Stack tmp = (Stack) model.getAttackerList().get(attacker);

        Stack cloned = (Stack) tmp.clone();

        int size = Integer.parseInt(txtHits.getText());

        txtHits.setText(Integer.toString(cloned.size()));

        while(!cloned.empty())
        {
            txtLog.setText((String) cloned.pop() + "\n\r" + txtLog.getText());
        }
    }

}
