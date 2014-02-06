/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.AttackData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author andrew
 */
public class AttackData implements AttackGroupInterface {

    
    private HashMap<String,AttackModule> moduleGroup;

    public AttackData()
    {
        moduleGroup = new HashMap<String,AttackModule>();
    }

    public AttackModule getModule(String moduleName)
    {
        return moduleGroup.get(moduleName);
    }
    
    public void addModule(String moduleName, AttackModule attackModule)
    {
        moduleGroup.put(moduleName, attackModule);
    }



    public String[] getListOfAttacks() {

        Set tmp = moduleGroup.keySet();
        Object[] a = tmp.toArray();
        String[] rtn = new String[tmp.size()];

        for(int i = 0; i < tmp.size(); i++)
        {
            rtn[i] = (String) a[i];
        }
        
        return rtn;

    }
    public int getTotalAttackNumber() {
        //not implemented
        return -1;
    }

    public int getAttackNumberForAttack(String attack)
    {
        //not implemented
        return -1;
    }

    public String[] getAttackers() {

        Collection c = moduleGroup.values();
        Iterator itr = c.iterator();

        Vector v = new Vector();

        while(itr.hasNext())
        {

            AttackModule attackModule = (AttackModule) itr.next();

            v.add(attackModule.getAttackers());
            
        }

        return new String[1];
    }

    public String[] getAttackersForAttack(String attack)
    {

        //not implemented
        return new String[1];
   }

}
