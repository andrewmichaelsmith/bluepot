/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.AttackData;

import java.util.Date;


/**
 *
 * @author andrew
 */
public abstract class Attack {

    Date time;
    String attacker;

    public Attack(Date time, String attacker) {

        this.time = time;
        this.attacker = attacker;
        
    }

    public String getAttacker() {
        return attacker;
    }
    public Date getTime() {
        return  time;
    }

    
    
}
