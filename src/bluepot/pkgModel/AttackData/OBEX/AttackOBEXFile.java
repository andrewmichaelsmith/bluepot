/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.AttackData.OBEX;

import bluepot.pkgModel.AttackData.Attack;
import java.util.Date;

/**
 *
 * @author andrew
 */
public class AttackOBEXFile extends Attack {

    String getRequest;
    String sourceAddress;

    
    public AttackOBEXFile(Date time, String getRequest, String sourceAddress) {

        super(time,sourceAddress);

        this.getRequest = getRequest;
        this.sourceAddress = sourceAddress;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }



    public String getFileName()
    {
        return getRequest;
    }

}
