/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.Modules;

import bluepot.pkgModel.AttackData.AttackConnect;
import bluepot.pkgModel.AttackData.AttackModule;
import bluepot.pkgModel.AttackData.RFCOMM.AttackCommand;
import bluepot.pkgModel.Model;
import bluepot.pkgModel.ObserverMessage;
import com.intel.bluetooth.BlueCoveImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRegistrationException;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 *
 * @author andrew
 */
public class RFCOMMServer extends Observable implements Runnable  {
    private String SERVER_NAME;
//    private UUID HEADSET_PROFILE = new UUID(0x110A);
    private UUID HEADSET_PROFILE = new UUID(0x0314);

    private StreamConnectionNotifier serverConnection;

    private boolean isStopped = false;
    private int id;
    private Object dongleID;

    private Model model;
    private AttackModule attackModule;

    private boolean running = false;
    
    private Process specialBluetoothDaemon;

    public boolean isRunning() {
            return running;
        }

    private StreamConnectionNotifier keepOpeningConnection(String url)
    {
        StreamConnectionNotifier connection;

        try {
            connection =  (StreamConnectionNotifier) Connector.open(url);
        } catch (ServiceRegistrationException ex) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex1) {
                Logger.getLogger(L2CAPServer.class.getName()).log(Level.SEVERE, null, ex1);
            }
            connection = keepOpeningConnection(url);
        } catch (IOException ex) {
            //FIXME: urgent, this should never really ever be thrown unless the bt devices are FUCKED
            //but it just happened and i have no ide awhy
            System.out.println("serious error - ");
            ex.toString();
            return null;
        }

        return connection;


    }


    public RFCOMMServer(Model model, int id, Object dongleID,  AttackModule attackModule) {
        this.id = id;
        this.dongleID = dongleID;
        this.model = model;
        this.attackModule = attackModule;
    }

    public void run() {
        // This application uses an outdated API from bluetoothd. We need to
        // kill it and restart it with some extra parameters to enable
        // compabillity
        try {
            Process process = new ProcessBuilder("systemctl","stop","bluetooth").start();
            process.waitFor();
            this.specialBluetoothDaemon = new ProcessBuilder("bluetoothd","-C","--noplugin=sap").start();
        } catch (IOException ex) {
            System.out.println("failed to deal with bluetoothd - ");
            System.out.println(ex.toString());
        }  catch (InterruptedException ex) {
            System.out.println("failed to deal with bluetoothd - ");
            System.out.println(ex.toString());
        }
        
        BlueCoveImpl.useThreadLocalBluetoothStack();
        BlueCoveImpl.setThreadBluetoothStackID(dongleID);
        setChanged();
        notifyObservers(new ObserverMessage(id, "[RFCOMM] Server starting.."));
        String url = "btspp://localhost:" + HEADSET_PROFILE + ";name=Server";
        serverConnection = (StreamConnectionNotifier) keepOpeningConnection(url);
        setChanged();
        notifyObservers(new ObserverMessage(id, "[RFCOMM] Server started"));


      running = true;

      while(!isStopped)
                    {
        try {

            String line;
            StreamConnection con = (StreamConnection) serverConnection.acceptAndOpen();
            OutputStream os = con.openOutputStream();
            InputStream is = con.openInputStream();
            setChanged();
            notifyObservers(new ObserverMessage(id, "[RFCOMM] Received connection from: " + RemoteDevice.getRemoteDevice(con).toString()));
            attackModule.addTo("connect", new AttackConnect(Calendar.getInstance().getTime(), RemoteDevice.getRemoteDevice(con).toString()));
            model.addAttack(RemoteDevice.getRemoteDevice(con).toString(), "[Device "+id+"] Connected on RFCOMM");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int count = 0;

             while ((line = reader.readLine()) != null) {

                 count++;

                 if(count >= 50)
                    break;

                 if(line.equals("AT+GMI")) //Manufacturer
                    os.write(new String(model.getSettings().getRespID() + "\r\n").getBytes());
                 else if(line.equals("AT+GMM")) //Model
                    os.write(new String(model.getSettings().getRespPhone() + "\r\n").getBytes());
                 else if(line.equals("AT+GMR")) //Revision
                    os.write(new String(model.getSettings().getRespSMS() + "\r\n").getBytes());
                 else if(line.equals("AT+CGSN")) //PSN
                    os.write(new String("psn\r\n").getBytes());
                 else if(line.equals("AT+GCAP")) //Capability
                    os.write(new String("Model\r\n").getBytes());
                 else if(line.equals("AT+CPBS=?")) //Address book
                    os.write(new String("+CPBS=Tom,000000002\r\n").getBytes());
                 else if(line.equals("AT+CMGF=1"))//Messages
                   os.write(new String("+CPMS=Hi\r\n").getBytes());
                 else
                   os.write(new String("garbage\r\n").getBytes());
                            
                 os.write(new String("OK\r\n").getBytes());
                            
                 setChanged();
                 notifyObservers(new ObserverMessage(id, "[RFCOMM] Received command: '"+ line +"' from: " + RemoteDevice.getRemoteDevice(con).toString()));
                 attackModule.addTo("command", new AttackCommand(Calendar.getInstance().getTime(), line, RemoteDevice.getRemoteDevice(con).toString()));
                 model.addAttack(RemoteDevice.getRemoteDevice(con).toString(), "[Device "+id+"] Received command: '" + line + "'");

             }
            
            count = 0;
            os.close();
            is.close();
            con.close();

                } catch (IOException ex) {
                    setChanged();
                    notifyObservers(new ObserverMessage(id, "[RFCOMM] Connected client timed out"));
                    
                }
             }
    }
}
