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
public class AttackFileRcvd extends Attack {

    String fileName;
    long fileSize;
    String sourceAddress;

    public AttackFileRcvd(Date time, String fileName, long fileSize, String sourceAddress) {

        super(time, sourceAddress);

        this.fileName = fileName;
        this.fileSize = fileSize;
    }




    public String getFileName()
    {
        return fileName;
    }

}
