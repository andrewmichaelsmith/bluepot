/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel;


import bluepot.pkgModel.Sensor.Sensor;
import bluepot.Main;
import bluepot.pkgModel.ConfigureSensors.SensorTableRow;
import bluepot.pkgModel.AttackData.AttackData;
import bluepot.pkgModel.Config.Settings;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Vector;
import com.intel.bluetooth.BlueCoveImpl;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
/**
 *
 * @author root
 */
public class Model extends Observable {

    private int noOfDevices = 0;
    private Sensor[] sensorArray;
    private SensorTableRow[] sensorTable;
    private Logger logger;
    private DeviceClass deviceClass;
    private Settings settings;
    private HashMap<String, Stack> attackerList = new HashMap<String, Stack>();
    private boolean newAttacker = false;
      

    public HashMap getAttackerList()
    {
        return attackerList;
    }
    public boolean getNewAttacker()
    {
        if(newAttacker)
        {
            newAttacker = false;
            return true;
        }
        return false;
    }
    public void addAttack(String attacker, String attack)
    {
        if(attackerList.get(attacker) == null)
        {
            Stack tmp = new Stack();
            tmp.push(Model.getDateTime() + " " + attack);

            attackerList.put(attacker, tmp);

            newAttacker = true;


        }
        else
        {
            Stack tmp = attackerList.get(attacker);
            tmp.push(Model.getDateTime() + " " + attack);

            attackerList.put(attacker,tmp);
        }

        
        setChanged();
        notifyObservers();
    }


   public Model() {

        deviceClass = new DeviceClass();
       
        startLogger();
        
        log(Level.ALL,"Attempting to initialize bluetooth devices");
        initializeSensors();
        log(Level.ALL,"Sensors initialized");
        loadTableData();
        log(Level.ALL,"Loaded sensor configuration");
        if(loadConfiguration() != 1)
            settings = new Settings();
        else
            log(Level.ALL,"Loaded configuration");
    }

    public Settings getSettings()
    {
        return settings;
    }
    public SensorTableRow getTableRow(int i)
    {
        return sensorTable[i];
    }
    public void setTableRow(int i, SensorTableRow str)
    {
        sensorTable[i] = str;
    }
    public int getNoOfDevices() {

        return noOfDevices;
        
    }

    public String[] getMajorClassesList() {

        String[] rtn = deviceClass.getMajorList();
        Arrays.sort(rtn);

        return rtn;
    }

    public String[] getMinorClassesList(String s) {

        String[] rtn = deviceClass.getMinorList(s);
        Arrays.sort(rtn);

        return rtn;

    }

    public int getMajorClassInt(String s)
    {
        return deviceClass.getMajorInt(s);
    }

    public int getMinorClassInt(String s, String t)
    {
        return deviceClass.getMinorInt(s,t);
    }

    public String minorIntToString(int ma, int mi)
    {
        return deviceClass.minorIntToString(ma, mi);
    }

    public String majorIntToString(int ma)
    {
        return deviceClass.majorIntToString(ma);
    }
    
    public Sensor getSensor(int i)
    {
        return sensorArray[i];
    }

    public static String discoverableIntToString(int discoverable)
    {
        //TODO: Switch!!!
        if(discoverable == DiscoveryAgent.GIAC) return "Full";
        else if(discoverable == DiscoveryAgent.LIAC) return "Limited";
        else if(discoverable == DiscoveryAgent.NOT_DISCOVERABLE) return "Invisible";
        else if(discoverable == DiscoveryAgent.PREKNOWN) return "PREKNOWN";
        else if(discoverable == DiscoveryAgent.CACHED) return "CACHED";
        else return "-1";

    }

    public static int discoverableStringToInt(String discoverable)
    {
        if(discoverable.equalsIgnoreCase("Full")) return DiscoveryAgent.GIAC;
        else if(discoverable.equalsIgnoreCase("Limited")) return DiscoveryAgent.LIAC;
        else if(discoverable.equalsIgnoreCase("Invisible")) return DiscoveryAgent.NOT_DISCOVERABLE;
        else if(discoverable.equalsIgnoreCase("PREKNOWN")) return DiscoveryAgent.PREKNOWN;
        else if(discoverable.equalsIgnoreCase("CACHED")) return DiscoveryAgent.CACHED;
        else return -1;

    }

     public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
     

    public void setLocalDeviceName(int i, String s)
    { 
        sensorArray[i].setFriendlyDeviceName(s);
    }

    public void setLocalDeviceClass(int i, int major, int minor)  {
        sensorArray[i].setDeviceClass(major, minor);
    }


    private String logPath()
    {

        File cwd = new File(".");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MMMMM/yyyyMMdd");
        Date now = new Date();
        String parsed = format.format(now);
        try {
            return cwd.getCanonicalPath() + "/logs/" + parsed + ".log";
        } catch (IOException ex) {
            log(Level.ERROR,"Failed getting current working directory");
            return null;
        }

    }

    private String logFolder()
    {

        File cwd = new File(".");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MMMMM/");
        Date now = new Date();
        String parsed = format.format(now);
        try {
            return cwd.getCanonicalPath() + "/logs/" + parsed;
        } catch (IOException ex) {
            log(Level.ERROR,"Could not get current working directory");
            return null;
        }

    }
    private void startLogger()
    {
       logger = Logger.getLogger("Bluepot");

        File logFolder = new File(logFolder());
        
        if(!logFolder.exists())
            logFolder.mkdirs();

        //FIXME: I stole this
        String pattern = "%d{ISO8601} - %C - %m %n";
        PatternLayout pl = new PatternLayout(pattern);

        FileAppender fa = null;
        try {
            fa = new FileAppender(pl, logPath());
        } catch (IOException ex) {
            System.out.println("Error writing log file: " + ex.toString());
            log(Level.ERROR,"Failed to append to log file");
        }

        logger.addAppender(fa);
        logger.setLevel(Level.ALL);

        
    }

    public void log(Level level, String msg)
    {
         logger.log(level, msg);
         if(Main.DEBUG == 1)
             System.out.println(level.toString() + ":" + msg);
         
    }

    private void initializeSensors()  {

        BlueCoveImpl.setConfigProperty("bluecove.deviceID", "0");
        Vector localDeviceList = null;
        try {
            localDeviceList = BlueCoveImpl.getLocalDevicesID();
        } catch (BluetoothStateException ex) {
            log(Level.FATAL,"Bluetooth State Exception when getting list of local devices");
            log(Level.FATAL, ex.toString());
            log(Level.FATAL, "System exiting");
            BlueCoveImpl.shutdown();
                        System.out.println("boo3");

            System.exit(1);
        }

        noOfDevices = localDeviceList.size();

        sensorArray = new Sensor[noOfDevices];
        sensorTable = new SensorTableRow[noOfDevices];

           for(int i = 0; i < noOfDevices; i++)
            {

              
                if(i == 0) {
                try {
                    Object id = BlueCoveImpl.getThreadBluetoothStackID();
                    sensorArray[i] = new Sensor(id, (String) localDeviceList.get(i), this);
                } catch (BluetoothStateException ex) {
                    log(Level.FATAL,"Bluetooth State Exception when starting a device manager");
                    log(Level.FATAL, ex.toString());
                    log(Level.FATAL, "System exiting");
                    BlueCoveImpl.shutdown();
                                System.out.println("boo4");

                    System.exit(1);
                }
                }

                else
                {

                    sensorArray[i] = new Sensor(i, (String) localDeviceList.get(i), this);
                
                }

                new Thread(sensorArray[i]).start();
                while(!sensorArray[i].isRunning())
                {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
                }

            }
        
    }



    public void startServers(Observer view) {

        for(int i = 0; i < getNoOfDevices(); i++)
        {
            sensorArray[i].startListeners(view);
        }
    }
    public void updateTableRow(int id) {
        sensorTable[id] = new SensorTableRow(sensorArray[id]);
    }

    private int loadConfiguration()
    {
       File config = new File("data/config.ser");
        if(config.exists())
        {
            InputStream file = null;
            try {
                    file = new FileInputStream("data/config.ser");
                    InputStream buffer = new BufferedInputStream(file);
                    ObjectInput input = new ObjectInputStream(buffer);

                    settings = (Settings) input.readObject();
                    } catch (InvalidClassException ex) {
                     config.delete();
                     return -1;
                    } catch (ClassNotFoundException ex) {
                    config.delete();
                     return -1;
                 } catch (IOException ex) {
                    config.delete();
                     return -1;
                } finally {
                    return 1;
                }
        }
        else
        {
            return -1;
        }
    }

    private void loadTableData()
    {
        for(int j = 0; j < noOfDevices; j++)
        {
            File test = new File("data/hci" + j + ".ser");
            if(test.exists())
            {
              InputStream file = null;
                try {
                    file = new FileInputStream("data/hci" + j + ".ser");
                    InputStream buffer = new BufferedInputStream(file);
                    ObjectInput input = new ObjectInputStream(buffer);
                    
                    SensorTableRow str = (SensorTableRow) input.readObject();

                   
                } catch (ClassNotFoundException ex) {
                    test.delete();
                    sensorTable[j] = new SensorTableRow(sensorArray[j]);
                } catch (IOException ex) {
                    test.delete();
                    sensorTable[j] = new SensorTableRow(sensorArray[j]);
                }

            }
            else
            {
                //use defaults
                sensorTable[j] = new SensorTableRow(sensorArray[j]);
            }

            
        }

    }
    public void resetTableData()
    {
        for(int j = 0; j < noOfDevices; j++)
        {
            sensorTable[j] = new SensorTableRow(sensorArray[j]);

        }
        
    }
    public void saveConfiguration()
    {
        try {
                OutputStream file = null;
                ObjectOutput output = null;

                try {
                    file = new FileOutputStream("data/config.ser");
                } catch (FileNotFoundException ex) {
                    java.util.logging.Logger.getLogger(Model.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }

                OutputStream buffer = new BufferedOutputStream(file);
                output = new ObjectOutputStream(buffer);
                output.writeObject(settings);

                output.close();
            } catch (IOException ex) {
                log(Level.ERROR, "Error saving configuration file: " + ex.toString());
            }


    }
    public void applyTableData()
    {
        for(int j = 0; j < noOfDevices; j++)
        {
            try {
                OutputStream file = null;
                ObjectOutput output = null;

                try {
                    file = new FileOutputStream("data/hci" + j + ".ser");
                } catch (FileNotFoundException ex) {
                    java.util.logging.Logger.getLogger(Model.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }

                OutputStream buffer = new BufferedOutputStream(file);
                output = new ObjectOutputStream(buffer);
               
                sensorArray[j].updateSettings(sensorTable[j]);
                output.writeObject(sensorTable[j]);
                
                output.close();
            } catch (IOException ex) {
                log(Level.ERROR, "Error saving configuration file: " + ex.toString());
            }
        }

        

    }

    public AttackData readAttackData(int j) {
        return sensorArray[j].getAttackData();
        }

}
