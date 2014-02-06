/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.AttackData.L2CAP;

import bluepot.pkgModel.AttackData.Attack;
import java.util.Date;

/**
 *
 * @author Lesley
 */
public class AttackCommand extends Attack {

    Date time;
    String attacker;
    String command;

    public AttackCommand(Date time, String attacker, String command)
    {
        super(time, attacker);
        
        this.time = time;
        this.attacker = attacker;
        this.command = command;
    }

    public String getCommand()
    {
        return command;  
    }

}
