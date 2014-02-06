/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.AttackData;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author andrew
 */
public class AttackModule implements AttackGroupInterface {

    HashMap<String, AttackList> attackGroup;

    int totalAttacks = 0;
    

    public AttackModule()
    {
        this.attackGroup = new HashMap<String, AttackList>();
        
    }
    
    public AttackList get(String name)
    {
        return attackGroup.get(name);
    }

    public void add(String name, AttackList attackList)
    {
        attackGroup.put(name, attackList);
    }

    public void addTo(String name, Attack attack)
    {
        AttackList tmp = attackGroup.get(name);
        tmp.add(attack);
        //FIXME: is this needed? will the above just do it? hope latter..
        attackGroup.put(name, tmp);

        
    }

    public String[] getListOfAttacks() {

        Set tmp = attackGroup.keySet();
        Object[] a = tmp.toArray();
        String[] rtn = new String[tmp.size()];

        for(int i = 0; i < tmp.size(); i++)
        {
            rtn[i] = (String) a[i];
        }

        return rtn;
    }

    public int getTotalAttackNumber() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getAttackNumberForAttack(String attack) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String[] getAttackers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String[] getAttackersForAttack(String attack) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
