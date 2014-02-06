/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.AttackData.RFCOMM;

import bluepot.pkgModel.AttackData.Attack;
import java.util.Date;

/**
 *
 * @author andrew
 */
public class AttackCommand extends Attack {

    String command;
    String sourceAddress;


    public AttackCommand(Date time, String command, String sourceAddress) {

        super(time,sourceAddress);

        this.command = command;
        this.sourceAddress = sourceAddress;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }



    public String getCommand()
    {
        return command;
    }


}
