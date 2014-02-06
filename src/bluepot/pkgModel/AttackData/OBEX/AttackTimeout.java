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
public class AttackTimeout  extends Attack {

    
    public AttackTimeout(Date time, String attacker) {

        super(time,attacker);

    }

}