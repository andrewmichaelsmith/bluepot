/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.ConfigureSensors;

import bluepot.pkgModel.Sensor.Sensor;
import java.io.Serializable;

/**
 *
 * @author andrew
 */
public class SensorTableRow implements Serializable {

    private int id;
    private String deviceName;
    private int major;
    private int minor;
    private boolean enabled = false;
    private int discoverable;
    private String friendlyName;
    private boolean locked = false;
    private boolean OBEX = true;
    private boolean RFCOMM = true;
    private boolean L2CAP = true;

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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public SensorTableRow(Sensor s)
    {
       this.id = s.getIdNumber();
       this.deviceName = s.getDeviceName();
       this.major = s.getMajor();
       this.minor = s.getMinor();
       this.enabled = s.isEnabled();
       this.discoverable = s.getDiscoverable();
       this.friendlyName = s.getFriendlyName();
       this.locked = s.isLocked();
       this.OBEX = s.isOBEX();
       this.L2CAP = s.isL2CAP();
       this.RFCOMM = s.isRFCOMM();

    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDiscoverable() {
        return discoverable;
    }

    public void setDiscoverable(int discoverable) {
        this.discoverable = discoverable;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
