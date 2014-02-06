/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.Sensor;

import bluepot.pkgModel.ConfigureSensors.SensorTableRow;
import bluepot.pkgModel.Model;
import bluepot.pkgModel.Modules.OBEXServer;
import bluepot.pkgModel.AttackData.AttackData;
import bluepot.pkgModel.AttackData.AttackList;
import bluepot.pkgModel.AttackData.AttackModule;
import bluepot.pkgModel.Modules.L2CAPServer;
import bluepot.pkgModel.Modules.RFCOMMServer;
import com.intel.bluetooth.BlueCoveImpl;
import java.io.IOException;
import java.util.Observer;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import org.apache.log4j.Level;

/**
 *
 * @author root
 */

/*
 *
 *  This is a thread that manages individual bluetooth devices
 *  It spawns other 
 *
 */

public class Sensor implements Runnable {

    private int id;
    private String deviceName;
    private Object dongleID;
    private Thread obexThread;
    private OBEXServer obexRunnable;
    private boolean running = false;
    private RFCOMMServer rfcommRunnable;
    private Thread rfcommThread;
    private L2CAPServer l2capRunnable;
    private Thread l2capThread;
    private boolean locked;

    private String address;

    
    private boolean OBEX = true;
    private boolean L2CAP = true;
    private boolean RFCOMM = true;

    private int major;

    private int minor;

    
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    public boolean isRunning() {
        return running;
    }



    public String getDeviceName() {
        return deviceName;
    }

    public int getDiscoverable() {
        return discoverable;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public int getIdNumber() {
        return id;
    }

    private boolean enabled = false;

    private int discoverable;
    private String friendlyName;

    private Model model;

    private AttackData attackData = new AttackData();

    public String getAddress() {
        return address;
    }

    public boolean isL2CAP() {
        return L2CAP;
    }

    public void setL2CAP(boolean L2CAP) {
        this.L2CAP = L2CAP;
    }

    public boolean isOBEX() {
        return OBEX;
    }

    public void setOBEX(boolean OBEX) {
        this.OBEX = OBEX;
    }

    public boolean isRFCOMM() {
        return RFCOMM;
    }

    public void setRFCOMM(boolean RFCOMM) {
        this.RFCOMM = RFCOMM;
    }


    public AttackData getAttackData() {
        return attackData;
    }

    public Sensor(int id, String deviceName, Model model)  {
         super();
         this.id = id;
         this.deviceName = deviceName;
         this.model = model;
         constructor();
     }



    public Sensor(Object dongleID, String deviceName, Model model)  {
        super();
        this.id = 0;
        this.dongleID = dongleID;
        this.deviceName = deviceName;
        this.model = model;
        constructor();
    }

    private void constructor()
    {

        AttackModule obexAttackModule = new AttackModule();
        obexAttackModule.add("connect", new AttackList());
        obexAttackModule.add("filercvd", new AttackList());
        obexAttackModule.add("timeout", new AttackList());
        obexAttackModule.add("get", new AttackList());
        obexAttackModule.add("setpath", new AttackList());
        obexAttackModule.add("delete", new AttackList());
        obexAttackModule.add("timeout", new AttackList());

        AttackModule l2capAttackModule = new AttackModule();

        AttackModule rfcommAttackModule = new AttackModule();
        rfcommAttackModule.add("connect", new AttackList());
        rfcommAttackModule.add("command", new AttackList());

        attackData.addModule("obexpush", obexAttackModule);
        attackData.addModule("rfcomm", rfcommAttackModule);
        attackData.addModule("l2cap", l2capAttackModule);

        
    }

    @Override
    public void run() {


        if(id > 0)
        {
            
            BlueCoveImpl.setConfigProperty("bluecove.deviceID", Integer.toString(id));
            BlueCoveImpl.useThreadLocalBluetoothStack();

            try {
                dongleID = BlueCoveImpl.getThreadBluetoothStackID();
            } catch (BluetoothStateException ex) {
              }
        }
        
        BlueCoveImpl.useThreadLocalBluetoothStack();
        BlueCoveImpl.setThreadBluetoothStackID(dongleID);
        
        try {
            BlueCoveImpl.useThreadLocalBluetoothStack();
            
            friendlyName = LocalDevice.getLocalDevice().getFriendlyName();
            discoverable = LocalDevice.getLocalDevice().getDiscoverable();
            major = LocalDevice.getLocalDevice().getDeviceClass().getMajorDeviceClass();
            minor = LocalDevice.getLocalDevice().getDeviceClass().getMinorDeviceClass();

        } catch (BluetoothStateException ex) {
            model.log(Level.FATAL, "BluetoothStateException id: " + id + "\n" + "deviceName: " + deviceName + "\n" + "dongleId: " + dongleID);
            
            BlueCoveImpl.shutdownThreadBluetoothStack();
            BlueCoveImpl.shutdown();

            System.exit(1);
        } catch (Exception ex) {
            model. log(Level.FATAL,"something terrible happened");
            BlueCoveImpl.shutdownThreadBluetoothStack();
            BlueCoveImpl.shutdown();
            System.exit(1);
        }
        try {
            address = LocalDevice.getLocalDevice().getBluetoothAddress();
        } catch (BluetoothStateException ex) {
            Logger.getLogger(Sensor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        model.updateTableRow(id);
        
        running = true;

    }

    
    public void applySettings(SensorTableRow sensorTableRow)
    {
        setFriendlyDeviceName(sensorTableRow.getFriendlyName());
        setMajor(sensorTableRow.getMajor());
        setMinor(sensorTableRow.getMinor());
        setDeviceClass(major, minor);
        setDiscoverable(sensorTableRow.getDiscoverable());
        setLocked(sensorTableRow.isLocked());
        setOBEX(sensorTableRow.isOBEX());
        setRFCOMM(sensorTableRow.isRFCOMM());
        setL2CAP(sensorTableRow.isL2CAP());
        model.updateTableRow(id);
    }
    public void updateSettings(SensorTableRow sensorTableRow)
    {
       if( (friendlyName.isEmpty()) || (!friendlyName.equals(sensorTableRow.getFriendlyName())));
            setFriendlyDeviceName(sensorTableRow.getFriendlyName());
        //TODO: why do we have this here...setMajor/setMinor when setDeviceClass does it

            locked = sensorTableRow.isLocked();
            OBEX = sensorTableRow.isOBEX();
            RFCOMM = sensorTableRow.isRFCOMM();
            L2CAP = sensorTableRow.isL2CAP();
            
        boolean majorminor = false;

        if(major != sensorTableRow.getMajor())
        {
            setMajor(sensorTableRow.getMajor());
            majorminor = true;
        }

        if(minor != sensorTableRow.getMinor())
        {
            setMinor(sensorTableRow.getMinor());
            majorminor = true;
        }

        if(majorminor)
        {
            setDeviceClass(major, minor);
            majorminor = false;
        }

        if(discoverable != sensorTableRow.getDiscoverable())
        {
            setDiscoverable(sensorTableRow.getDiscoverable());
        }
        model.updateTableRow(id);
    }
    public void startListeners(Observer view)
    {
        if(RFCOMM)
        {
            startRFCOMMServer(view);
            while(!rfcommRunnable.isRunning())
            {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sensor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }

        }
        if(L2CAP)
        {
            startL2CAP(view);
            while(!l2capRunnable.isRunning())
            {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sensor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        if(OBEX)
        {
            startOBEXServer(view);
            while(!obexRunnable.isRunning())
            {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sensor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
            
    }
    public void startRFCOMMServer(Observer view)
    {
        rfcommRunnable = new RFCOMMServer(model, id, dongleID, attackData.getModule("rfcomm"));
        rfcommRunnable.addObserver(view);
        rfcommThread = new Thread(rfcommRunnable);
        rfcommThread.start();

    }

    public void startL2CAP(Observer view)
    {
        l2capRunnable = new L2CAPServer(model, id, dongleID, attackData.getModule("l2cap"));
        l2capRunnable.addObserver(view);
        l2capThread = new Thread(l2capRunnable);
        l2capThread.start();


    }
    public void startOBEXServer(Observer view) {

        obexRunnable = new OBEXServer(model, id, dongleID, attackData.getModule("obexpush"));
        obexRunnable.addObserver(view);
        obexThread = new Thread(obexRunnable);
        obexThread.start();

  
    }



    public String getLocalDeviceID()
    {
        return Integer.toString(id);
    }
    public String getLocalDeviceName() 
    {
      return friendlyName;

    }
    public String getLocalDeviceVisibility()
    {
        BlueCoveImpl.useThreadLocalBluetoothStack();
      BlueCoveImpl.setThreadBluetoothStackID(dongleID);
        try {
            return Model.discoverableIntToString(LocalDevice.getLocalDevice().getDiscoverable());
        } catch (BluetoothStateException ex) {
            model.log(Level.ERROR, "Error retrieving device visibility");
            return "device error";

        }
    }

     public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public void setDiscoverable(int i)
    {
      BlueCoveImpl.useThreadLocalBluetoothStack();
      BlueCoveImpl.setThreadBluetoothStackID(dongleID);

         discoverable = i;

       
        try {
             LocalDevice localDevice = LocalDevice.getLocalDevice();
             if(!localDevice.setDiscoverable(DiscoveryAgent.GIAC))
             {
                 System.out.println("weird error - please explain");
             }
        } catch (BluetoothStateException ex) {
            System.out.println("TRYING TO SET DISCOVERABLE ERROR - " + ex.toString());
            Logger.getLogger(Sensor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }   

    public void setFriendlyDeviceName(String s) 
    {
        try {
            //TODO: External execution as root, SERIOUS SERIOUS error checking here
            //TODO: This doesn't work, for spaces in name need to turn all " " in to "\ "
            s = s.replaceAll("\\s", "\\ ");
            String cmd = "hciconfig hci" + deviceName + " name " + s;
            Process result = Runtime.getRuntime().exec(cmd);
            try {
                int fff = result.waitFor();
             } catch (InterruptedException ex) {
                Logger.getLogger(Sensor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            friendlyName = s;
        } catch (IOException ex) {
            model.log(Level.ERROR, "IOException Failed changing device " + id + "'s name to " + s);
        }
        model.log(Level.INFO, "Changed device " + id + "'s name to " + s);

    }
    
    public void setDeviceClass(int major, int minor)
    {
        try {
            setMajor(major);
            setMinor(minor);
            int deviceClass = major + minor;
            String cmd = "hciconfig hci" + deviceName + " class 0x" + Integer.toString(deviceClass, 16).toUpperCase();
            Process result = Runtime.getRuntime().exec(cmd);
           try {
                int ffff =  result.waitFor();//fixme
            } catch (InterruptedException ex) {
                Logger.getLogger(Sensor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            model.log(Level.INFO, "Changed device " + id + "'s class to " + Integer.toString(deviceClass));
        } catch (IOException ex) {
            Logger.getLogger(Sensor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
    }

}
