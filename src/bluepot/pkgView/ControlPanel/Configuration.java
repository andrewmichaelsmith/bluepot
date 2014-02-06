/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.ControlPanel;

import bluepot.pkgModel.Config.Settings;
import bluepot.pkgModel.Model;
import bluepot.pkgView.View;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author andrew
 */
public class Configuration extends JPanel {

    Model model;
    View view;

    private final JFileChooser randomNameFileChooser;
    private final JFileChooser rcvdFolderChooser;
    private final JTextField randomTxtNameLocation;
    private final JTextField rcvdTxtDirectory;
    private final JCheckBox randomChckEnabled;
    private final JTextField randomTxtEvery;
    private final JCheckBox rcvdChkShouldEmail;
    private final JTextField rcvdTxtTo;
    private final JTextField rcvdTxtFrom;
    private final JTextField rcvdTxtSubject;
    private final JTextField emailTxtHost;
    private final JTextField emailTxtUser;
    private final JPasswordField emailTxtPassword;
    private final JTextField emailTxtPort;
    private final JCheckBox randomChckDeviceClass;
    private final JTextArea txtObexGetResponse;
    private final JTextField txtRfcommIdResponse;
    private final JTextField txtRfcommPhoneBookResponse;
    private final JTextField txtRfcommSMSResponse;

    public Configuration(View view, Model model) {

        super(new MigLayout("wrap 1"));

        this.model = model;

        this.view = view;
        
        Settings settings = model.getSettings();

   
    JPanel randomPanel = new JPanel(new MigLayout("wrap 2"));
    randomPanel.setBorder(new TitledBorder("Randomiser"));

    JLabel randomDesc = new JLabel("The randomiser will randomly alter a sensors state when the user specifies. Device name, class and other features will change, unless they are \"locked\" to the user's specification");

    JLabel randomEnable = new JLabel("Enable randomiser");
    JLabel randomDeviceClass = new JLabel("Randomise Device Classess");

    JLabel randomNameLocation = new JLabel("Random Name List File");
    JLabel randomEvery = new JLabel("Randomise sensors every ");
    JLabel randomMinutes = new JLabel(" minutes");

    randomChckEnabled = new JCheckBox();
    randomChckEnabled.setSelected(settings.isRandomEnabled());

    randomChckDeviceClass = new JCheckBox();
    randomChckDeviceClass.setSelected(settings.isRandomDeviceClass());

    randomTxtNameLocation = new JTextField(35);
    randomTxtNameLocation.setText(settings.getRandomNameFolder());
    randomTxtNameLocation.setEditable(false);

    JButton randomLoadName = new JButton("Load..");
    randomLoadName.addActionListener(new loadActionListener());
    
    
    randomTxtEvery = new JTextField(5);
    randomTxtEvery.setText(Integer.toString(settings.getRandomMinutes()));

    randomNameFileChooser = new JFileChooser(settings.getRandomNameFolder());
    

    
    

    randomPanel.add(randomDesc,"span 2");

    randomPanel.add(randomEnable);
    randomPanel.add(randomChckEnabled);

    randomPanel.add(randomDeviceClass);
    randomPanel.add(randomChckDeviceClass);
    
    randomPanel.add(randomNameLocation);
    randomPanel.add(randomTxtNameLocation, "split 2");
    randomPanel.add(randomLoadName);

    randomPanel.add(randomEvery);
    randomPanel.add(randomTxtEvery, "split 2");
    randomPanel.add(randomMinutes);

    this.add(randomPanel);

    JPanel rcvdFiles = new JPanel(new MigLayout("wrap 2"));
    rcvdFiles.setBorder(new TitledBorder("System Settings"));

    JLabel rcvdDesc = new JLabel("Set up how files are dealt with when they are recieved");
    JLabel rcvdDirectory = new JLabel("Recieved Files Directory");
    JLabel rcvdMaxFileSize = new JLabel("Maximum File Size");
    JLabel rcvdKbytes = new JLabel("Kilobytes");
    JLabel rcvdShouldEmailFiles = new JLabel("Forward files recieved via email?");
    JLabel rcvdEmailTo = new JLabel("To");
    JLabel rcvdEmailFrom = new JLabel("From");
    JLabel rcvdEmailSubject = new JLabel("Subject");

    rcvdTxtDirectory = new JTextField(35);
    rcvdTxtDirectory.setText(settings.getSaveFolder());
    rcvdTxtDirectory.setEditable(false);

    rcvdFolderChooser = new JFileChooser(settings.getSaveFolder());
    rcvdFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    JButton rcvdBtnDirectory = new JButton("Choose..");
    rcvdBtnDirectory.addActionListener(new chooseActionListener());
    
    rcvdChkShouldEmail = new JCheckBox();
    rcvdChkShouldEmail.setSelected(settings.isEmailEnabled());

    rcvdTxtTo = new JTextField(20);
    rcvdTxtTo.setText(settings.getEmailTo());

    rcvdTxtFrom = new JTextField(20);
    rcvdTxtFrom.setText(settings.getEmailFrom());

    rcvdTxtSubject = new JTextField(25);
    rcvdTxtSubject.setText(settings.getEmailSubject());

    ImageIcon icnExclam = new ImageIcon("icons/exclamation.png");
    ImageIcon icnTick = new ImageIcon("icons/tick.png");

    JLabel rcvdIsEmailConfigured;

    rcvdFiles.add(rcvdDesc,"span 2");
    rcvdFiles.add(rcvdDirectory);
    rcvdFiles.add(rcvdTxtDirectory,"split 2");
    rcvdFiles.add(rcvdBtnDirectory);
    rcvdFiles.add(rcvdShouldEmailFiles);
    rcvdFiles.add(rcvdChkShouldEmail);
    rcvdFiles.add(rcvdEmailTo);
    rcvdFiles.add(rcvdTxtTo);
    rcvdFiles.add(rcvdEmailFrom);
    rcvdFiles.add(rcvdTxtFrom);
    rcvdFiles.add(rcvdEmailSubject);
    rcvdFiles.add(rcvdTxtSubject);


    this.add(rcvdFiles);

    JPanel emailConfig = new JPanel(new MigLayout("wrap 2"));
    emailConfig.setBorder(new TitledBorder("Email Server"));

    JLabel emailDesc = new JLabel("Configure SMTP server to send emails with. Required only to send attachments by email");
    JLabel emailHost = new JLabel("SMTP Host");
    JLabel emailPort = new JLabel(":");
    
    JLabel emailUsername = new JLabel("SMTP Username");
    JLabel emailPassword = new JLabel("SMTP Password");

    emailTxtHost = new JTextField(20);
    emailTxtHost.setText(settings.getSmtpHost());

    emailTxtUser = new JTextField(15);
    emailTxtUser.setText(settings.getSmtpUser());

    emailTxtPassword = new JPasswordField(15);
    emailTxtPassword.setText(settings.getSmtpPassword());
   
    emailTxtPort = new JTextField(4);
    emailTxtPort.setText(Integer.toString(settings.getSmtpPort()));

    emailConfig.add(emailDesc,"span 2");
    emailConfig.add(emailHost);
    emailConfig.add(emailTxtHost,"split 3");
    emailConfig.add(emailPort);
    emailConfig.add(emailTxtPort);
    emailConfig.add(emailUsername);
    emailConfig.add(emailTxtUser);
    emailConfig.add(emailPassword);
    emailConfig.add(emailTxtPassword);

    this.add(emailConfig);

    JPanel otherConfig = new JPanel(new MigLayout("wrap 2"));

    otherConfig.setBorder(new TitledBorder("Exploit Responses"));

    JLabel obexGetResponse = new JLabel("OBEX Get Response");
    txtObexGetResponse = new JTextArea(5,60);
    txtObexGetResponse.setText(model.getSettings().getRespOBEXGet());

    otherConfig.add(obexGetResponse,"wrap");
    otherConfig.add(txtObexGetResponse,"wrap");

    JLabel rfcommIdResponse= new JLabel("RFCOMM Manufacturer Response: ");
    txtRfcommIdResponse = new JTextField(30);
    txtRfcommIdResponse.setText(model.getSettings().getRespID());

    otherConfig.add(rfcommIdResponse);
    otherConfig.add(txtRfcommIdResponse);

    JLabel rfcommPhonebookResponse = new JLabel("RFCOMM Model Response: ");
    txtRfcommPhoneBookResponse = new JTextField(30);
    txtRfcommPhoneBookResponse.setText(model.getSettings().getRespPhone());

    otherConfig.add(rfcommPhonebookResponse);
    otherConfig.add(txtRfcommPhoneBookResponse);


    JLabel rfcommSMSResponse = new JLabel("RFCOMM Revision Response: ");
    txtRfcommSMSResponse = new JTextField(30);
    txtRfcommSMSResponse.setText(model.getSettings().getRespSMS());

    otherConfig.add(rfcommSMSResponse);
    otherConfig.add(txtRfcommSMSResponse);


    this.add(otherConfig);
    
    JButton saveSetting = new JButton("Save Settings");
    saveSetting.addActionListener(new saveActionListener());
    JButton resetSetting = new JButton("Reset Settings");
    this.add(saveSetting,"split 2");
   // this.add(resetSetting);


    }

    public class loadActionListener implements ActionListener {

       
        public void actionPerformed(ActionEvent ae) {
            int returnVal = randomNameFileChooser.showOpenDialog(Configuration.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {


                randomTxtNameLocation.setText(randomNameFileChooser.getSelectedFile().toString());

            }
        }

     }

    public class chooseActionListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {

            int returnVal = rcvdFolderChooser.showOpenDialog(Configuration.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                rcvdTxtDirectory.setText(rcvdFolderChooser.getSelectedFile().toString());

            }
        }

     }
     public class saveActionListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {

            int number = Integer.parseInt(randomTxtEvery.getText());

            //TODO: Validate emails, hosts, folder, file
            model.getSettings().setEmailEnabled(rcvdChkShouldEmail.isSelected());
            model.getSettings().setEmailFrom(rcvdTxtFrom.getText());
            model.getSettings().setEmailSubject(rcvdTxtSubject.getText());
            model.getSettings().setEmailTo(rcvdTxtTo.getText());
            model.getSettings().setRandomEnabled(randomChckEnabled.isEnabled());
            model.getSettings().setRandomMinutes(number);
            model.getSettings().setRandomNameFolder(randomTxtNameLocation.getText());
            model.getSettings().setSaveFolder(rcvdTxtDirectory.getText());
            model.getSettings().setSmtpHost(emailTxtHost.getText());
            model.getSettings().setSmtpPassword(new String(emailTxtPassword.getPassword()));
            model.getSettings().setSmtpUser(emailTxtUser.getText());
            model.getSettings().setRandomDeviceClass(randomChckDeviceClass.isSelected());
            model.getSettings().setSmtpPort(Integer.parseInt(emailTxtPort.getText()));

            model.getSettings().setRespID(txtRfcommIdResponse.getText());
            model.getSettings().setRespOBEXGet(txtObexGetResponse.getText());
            model.getSettings().setRespPhone(txtRfcommPhoneBookResponse.getText());
            model.getSettings().setRespSMS(txtRfcommSMSResponse.getText());
            model.saveConfiguration();
            

            //TODO: on close / on tab change save config
        }

     }



}
