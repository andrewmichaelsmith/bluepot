/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel;

/**
 *
 * @author root
 */
public class ObserverMessage {

    private int deviceId;
    private String message;

    public ObserverMessage(int deviceId, String message)
    {
        this.deviceId = deviceId;
        this.message = message;
    }


    public int getID() {
        return this.deviceId;
    }

    public String getMsg() {
        return this.message;
    }
}
