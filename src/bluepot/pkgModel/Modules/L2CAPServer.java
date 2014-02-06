/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.Modules;

import bluepot.pkgModel.AttackData.AttackConnect;
import bluepot.pkgModel.AttackData.AttackModule;
import bluepot.pkgModel.AttackData.L2CAP.AttackCommand;
import bluepot.pkgModel.Model;
import bluepot.pkgModel.ObserverMessage;
import com.intel.bluetooth.BlueCoveImpl;
import java.io.IOException;
import java.util.Calendar;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.L2CAPConnectionNotifier;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRegistrationException;
import javax.bluetooth.UUID;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;

/**
 *
 * @author andrew
 */
public class L2CAPServer extends Observable implements Runnable  {

    private int id;
    private Object dongleID;
    private Model model;
    private AttackModule attackModule;

    private boolean running = false;

    private int failCount = 0;

    public boolean isRunning() {
        return running;
    }

    private boolean isStopped = false;

    public L2CAPServer(Model model, int id, Object dongleID, AttackModule attackModule) {
        this.id = id;
        this.dongleID = dongleID;
        this.model = model;
        this.attackModule = attackModule;
    }

    private Connection keepOpeningConnection(String url)
    {
        Connection connection;

        try {
            connection = Connector.open(url);
        } catch (ServiceRegistrationException ex) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex1) {
                Logger.getLogger(L2CAPServer.class.getName()).log(Level.SEVERE, null, ex1);
            }
            connection = keepOpeningConnection(url);
        } catch (IOException ex) {
            System.out.println("serious error");
            return null;
        }

        return connection;


    }

    public void run() {


        BlueCoveImpl.useThreadLocalBluetoothStack();
        BlueCoveImpl.setThreadBluetoothStackID(dongleID);

        setChanged();
        notifyObservers(new ObserverMessage(id, "[L2CAP] Server starting.."));

        UUID L2CAP = new UUID(0x0100);
        String url = "btl2cap://localhost:" + L2CAP;
        L2CAPConnectionNotifier service = null;
        service = (L2CAPConnectionNotifier) keepOpeningConnection(url);

        setChanged();
        notifyObservers(new ObserverMessage(id, "[L2CAP] Server started"));

        running = true;
        while(!isStopped)
        {
            L2CAPConnection con = null;
            String attacker = "";
            
            try {
                con = (L2CAPConnection) service.acceptAndOpen();
                attacker = RemoteDevice.getRemoteDevice(con).toString();
            
            setChanged();
            notifyObservers(new ObserverMessage(id, "[L2CAP] Received connection from " + attacker));
            
            attackModule.addTo("connect", new AttackConnect(Calendar.getInstance().getTime(), attacker));
            model.addAttack(RemoteDevice.getRemoteDevice(con).toString(), "[Device "+id+"] Received connection from " + attacker);

            byte[] buffer = new byte[80];
            int bytesread = 0;
            bytesread = con.receive(buffer);
            
            String received = new String(buffer, 0, bytesread);
            attackModule.addTo("connect", new AttackCommand(Calendar.getInstance().getTime(), attacker, received));
            model.addAttack(RemoteDevice.getRemoteDevice(con).toString(), "[Device "+id+"] Received command: " + received);
            
            con.close();

            } catch (IOException ex) {
                    setChanged();
                    if(failCount > 10)
                    {
                       isStopped = true;
                       notifyObservers(new ObserverMessage(id, "[LCAP] Failed to accept connection from remote user after 10 attempts. Something is wrong with this device."));

                    }
                    else
                    {
                        failCount++;
                        notifyObservers(new ObserverMessage(id, "[LCAP] Failed to accept connection from remote user, trying again."));

                    }

            }            
        }
    }
}
