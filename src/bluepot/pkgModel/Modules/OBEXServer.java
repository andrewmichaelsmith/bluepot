/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.Modules;

/**
 *
 * @author root
 */
import bluepot.pkgController.EmailFile;
import bluepot.pkgModel.AttackData.AttackModule;
import bluepot.pkgModel.AttackData.AttackConnect;
import bluepot.pkgModel.AttackData.OBEX.AttackFileRcvd;
import bluepot.pkgModel.AttackData.OBEX.AttackOBEXFile;
import bluepot.pkgModel.Model;
import bluepot.pkgModel.ObserverMessage;
import com.intel.bluetooth.BlueCoveImpl;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;
import javax.bluetooth.UUID;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;
import javax.obex.ServerRequestHandler;
import javax.obex.SessionNotifier;
import org.apache.log4j.Level;

public class OBEXServer extends Observable implements Runnable  {

    private SessionNotifier serverConnection;
    public static final String SERVER_NAME = "OBEX Object Push";
    final UUID OBEX_OBJECT_PUSH = new UUID(0x1105);
    boolean isRunning = false;
    boolean isStopped = false;//FIXME: w
    int id;
    private Object dongleID;
    LocalDevice localDevice;
    AttackModule attackModule;

    Calendar cal;

    Model model;

    private boolean running = false;
 public boolean isRunning() {
        return running;
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
            }
            connection = keepOpeningConnection(url);
        } catch (IOException ex) {
            System.out.println("serious error");
            return null;
        }

        return connection;


    }

    private void keepUpdatingSDP(ServiceRecord record)
    {
        try {
            localDevice.updateRecord(record);
        } catch (ServiceRegistrationException ex) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex1) {
            }
            keepUpdatingSDP(record);
        }
    }


    public OBEXServer(Model model, int id, Object dongleID, AttackModule attackModule) {
        this.dongleID = dongleID;
        this.id = id;
        this.attackModule = attackModule;

        this.model = model;

    }



    public void run()   {

      BlueCoveImpl.useThreadLocalBluetoothStack();
      BlueCoveImpl.setThreadBluetoothStackID(dongleID);
   
        try {
            localDevice = LocalDevice.getLocalDevice();
        } catch (BluetoothStateException ex) {
          }

            setChanged();
            notifyObservers(new ObserverMessage(id, "[OBEX] Server starting.."));


          
            isStopped = false;



            String connectorUrl = "btgoep://localhost:" + OBEX_OBJECT_PUSH + ";name=" + SERVER_NAME;
            serverConnection = (SessionNotifier) keepOpeningConnection(connectorUrl);
   setChanged();
                       notifyObservers(new ObserverMessage(id, "[OBEX] Server started"));
                       running = true;
                try {
                        ServiceRecord record = localDevice.getRecord(serverConnection);
                        String url = record.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);

                    

                        DataElement bluetoothProfileDescriptorList = new DataElement(DataElement.DATSEQ);
                        DataElement obbexPushProfileDescriptor = new DataElement(DataElement.DATSEQ);
                        obbexPushProfileDescriptor.addElement(new DataElement(DataElement.UUID, OBEX_OBJECT_PUSH));
                        obbexPushProfileDescriptor.addElement(new DataElement(DataElement.U_INT_2, 0x100));
                        bluetoothProfileDescriptorList.addElement(obbexPushProfileDescriptor);
                        record.setAttributeValue(0x0009, bluetoothProfileDescriptorList);

                        final short ATTR_SUPPORTED_FORMAT_LIST_LIST = 0x0303;
                        DataElement supportedFormatList = new DataElement(DataElement.DATSEQ);
                        // any type of object.
                        supportedFormatList.addElement(new DataElement(DataElement.U_INT_1, 0xFF));
                        record.setAttributeValue(ATTR_SUPPORTED_FORMAT_LIST_LIST, supportedFormatList);

                        final short UUID_PUBLICBROWSE_GROUP = 0x1002;
                        final short ATTR_BROWSE_GRP_LIST = 0x0005;
                        DataElement browseClassIDList = new DataElement(DataElement.DATSEQ);
                        UUID browseClassUUID = new UUID(UUID_PUBLICBROWSE_GROUP);
                        browseClassIDList.addElement(new DataElement(DataElement.UUID, browseClassUUID));
                        record.setAttributeValue(ATTR_BROWSE_GRP_LIST, browseClassIDList);

                        keepUpdatingSDP(record);
                        
                } catch (Throwable e) {
                       setChanged();
                 
                }


         try {
                        int errorCount = 0;
                        int count = 0;
                        isRunning = true;
                        while (!isStopped) {
                                RequestHandler handler = new RequestHandler();

                                try {
                                        count++;
                                         Connection c = serverConnection.acceptAndOpen(handler);
                                        handler.connectionAccepted(c);


                                } catch (InterruptedIOException e) {
                                        isStopped = true;
                                        break;
                                } catch (Throwable e) {
                                        if ("Stack closed".equals(e.getMessage())) {
                                                isStopped = true;
                                        }
                                        if (isStopped) {
                                                return;
                                        }
                                        errorCount++;
                                      
                                        continue;
                                }
                                errorCount = 0;
                        }
                } finally {

                        setChanged();
                        notifyObservers(new ObserverMessage(id, "OBEX Server finished!"));
                        isRunning = false;
                }




    }




    private class RequestHandler extends ServerRequestHandler {


                Timer notConnectedTimer = new Timer();

                boolean isConnected = false;

                boolean receivedOk = false;

                Connection cconn;

                void connectionAccepted(Connection cconn)  {


                    String attacker;
                    try {
                        attacker = RemoteDevice.getRemoteDevice(cconn).toString();
                    } catch (IOException ex) {
                        attacker="";
                        model.log(Level.ERROR,"Error getting attacker address");
                    }
                    
                        setChanged();
                        notifyObservers(new ObserverMessage(id, "[OBEX] Received connection from " + attacker));
                      

                        this.cconn = cconn;
                        if (!isConnected) {
                                notConnectedTimer.schedule(new TimerTask() {
                                        public void run() {
                                                notConnectedClose();
                                        }
                                }, 1000 * 30);
                        }
                }

                void notConnectedClose() {
                        if (!isConnected) {
                                setChanged();
                                notifyObservers(new ObserverMessage(id, "[OBEX] Connection timed out"));

                                try {
                                        cconn.close();
                                } catch (IOException e) {
                                }
                                if (!receivedOk) {

                                }
                        }
                }

        @Override
                public int onConnect(HeaderSet request, HeaderSet reply) {
                            isConnected = true;

                        notConnectedTimer.cancel();

                        setChanged();
            try {
                notifyObservers(new ObserverMessage(id, "[OBEX] Got connection from " + RemoteDevice.getRemoteDevice(cconn)));
                attackModule.addTo("connect", new AttackConnect(Calendar.getInstance().getTime(), RemoteDevice.getRemoteDevice(cconn).toString()));
                model.addAttack(RemoteDevice.getRemoteDevice(cconn).toString(), "[Device "+id+"] Connected to OBEX");
            } catch (IOException ex) {
                model.log(Level.WARN, "Could not update sensor data");
            }
                        return ResponseCodes.OBEX_HTTP_OK;
                }

        @Override
                public void onDisconnect(HeaderSet request, HeaderSet reply) {
                        setChanged();
            try {
                notifyObservers(new ObserverMessage(id, "[OBEX] Got disconnection from " + RemoteDevice.getRemoteDevice(cconn)));
                model.addAttack(RemoteDevice.getRemoteDevice(cconn).toString(), "[Device "+id+"] Disconnected from OBEX");

            } catch (IOException ex) {
                model.log(Level.WARN, "Could not update sensor data");
            }
                        if (!receivedOk) {

                        }
                }

                //TODO IMPORTANT!!!
                //replace all Calendar.getTime() with Calendar.getInstance().getTime() .. everywhere.
                public int onSetPath(HeaderSet request, HeaderSet reply, boolean backup, boolean create) {
      
                        setChanged();
                      try {

                        String name = (String) request.getHeader(HeaderSet.NAME);
                        setChanged();
                        notifyObservers(new ObserverMessage(id, "[OBEX] Got set path - " + name));
                        attackModule.addTo("delete", new AttackOBEXFile(Calendar.getInstance().getTime(), name, RemoteDevice.getRemoteDevice(cconn).toString()));
                        setChanged();
                        notifyObservers(new ObserverMessage(id, "[OBEX] onDelete"));

                    } catch (IOException ex) {
                        model.log(Level.WARN, "Could not update sensor data");
                    }
                        notifyObservers(new ObserverMessage(id, "OBEX onSetPath"));
                        return super.onSetPath(request, reply, backup, create);
                }

        @Override
                public int onDelete(HeaderSet request, HeaderSet reply) {

                    try {
                        String name = (String) request.getHeader(HeaderSet.NAME);
                        setChanged();
                        notifyObservers(new ObserverMessage(id, "OBEX onGet"));
                        attackModule.addTo("delete", new AttackOBEXFile(Calendar.getInstance().getTime(), name, RemoteDevice.getRemoteDevice(cconn).toString()));
                        setChanged();
                        notifyObservers(new ObserverMessage(id, "OBEX onDelete"));

                    } catch (IOException ex) {
                        model.log(Level.WARN, "Could not update sensor data");
                    }

                      return super.onDelete(request, reply);
                }

        @Override
                public int onPut(Operation op) {
                        setChanged();
                        notifyObservers(new ObserverMessage(id, "[OBEX] Attacker initiated file send"));
                        try {
                            model.addAttack(RemoteDevice.getRemoteDevice(cconn).toString(), "[Device " + id + "] Initiated file send");
                        } catch (IOException ex) {
                            model.log(Level.WARN, "Could not update sensor data");
                        }
                          try {

                                HeaderSet hs = op.getReceivedHeaders();

                                String name = (String) hs.getHeader(HeaderSet.NAME);
                                if (name != null) {
                                    

                                } else {
                                        long milli = Calendar.getInstance().getTimeInMillis();
                                        name = "unnamed_" + Long.toString(milli);
                                }

                                Long len = (Long) hs.getHeader(HeaderSet.LENGTH);
                                if (len != null) {
                                       
                                }


                                /* make folder and save file */
                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                                Date now = new Date();
                                String parsed = format.format(now);

                                String storageFolder = model.getSettings().getSaveFolder();

                                String saveFolder = storageFolder + "/" + parsed;


                                File save = new File(saveFolder);
                                if(!save.exists())
                                   save.mkdirs();

                                 if(save.getFreeSpace()<52428800)
                                 {
                                     System.err.println("Error: Not Writing File. Less than 50mb Free Space");
                                 }

                                 else
                                 {

                                    String saveName = name + "_" + Calendar.getInstance().getTimeInMillis() + ".virus";
                                
                                File f = new File(saveFolder,name);
                                FileOutputStream out = new FileOutputStream(f);

                                InputStream is = op.openInputStream();

                                int received = 0;

                                while (!isStopped) {
                                        int data = is.read();
                                        if (data == -1) {
                                           
                                                break;
                                        }
                                        out.write(data);
                                        received++;
                                        if ((len != null) && (received % 100 == 0)) {

                                        }
                                }
                                op.close();

                                receivedOk = true;
                                       setChanged();
                                 notifyObservers(new ObserverMessage(id, "[OBEX] File recieved"));
                        try {
                            model.addAttack(RemoteDevice.getRemoteDevice(cconn).toString(), "[Device " + id + "] Finished sending file");
                        } catch (IOException ex) {
                            model.log(Level.WARN, "Could not update sensor data");
                        }
                                 attackModule.addTo("filercvd",
                                                    new AttackFileRcvd(Calendar.getInstance().getTime(),
                                                    name,
                                                    len,
                                                    RemoteDevice.getRemoteDevice(cconn).toString())
                                                   );

                                if(model.getSettings().isEmailEnabled())
                                {
                                    EmailFile ef = new EmailFile(model);
                                    ef.sendEmail(saveFolder +"/" + name,model.getSettings());
                                                setChanged();
                                 notifyObservers(new ObserverMessage(id, "[OBEX] File e-mailed to " + model.getSettings().getEmailTo()));

                                }



                                 }

                                return ResponseCodes.OBEX_HTTP_OK;
                        } catch (IOException e) {

                                return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
                        } finally {

                        }
                }

        @Override
                public int onGet(Operation op) {

                        try {
                                HeaderSet hs = op.getReceivedHeaders();
                                String name = (String) hs.getHeader(HeaderSet.NAME);
                                setChanged();
                                notifyObservers(new ObserverMessage(id, "[OBEX] Attacker requested file: " + name));
                                attackModule.addTo("get",
                                                new AttackOBEXFile(Calendar.getInstance().getTime(),
                                                name,
                                                RemoteDevice.getRemoteDevice(cconn).toString())
                                               );

                                model.addAttack(RemoteDevice.getRemoteDevice(cconn).toString(), "[Device "+id+"] Requested file: " + name);
                //TODO: Can return bogus file here
                DataOutputStream output = op.openDataOutputStream();
                     output.write(model.getSettings().getRespOBEXGet().getBytes());
                     output.close();
                                return ResponseCodes.OBEX_HTTP_OK;

                        } catch (IOException e) {

                                return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
                        } finally {
                        }
                }

        @Override
                public void onAuthenticationFailure(byte[] userName) {
                } 



        }




}