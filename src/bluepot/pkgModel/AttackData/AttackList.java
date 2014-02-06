/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.AttackData;

import java.util.ArrayList;

/**
 *
 * @author andrew
 */
public class AttackList {

    private ArrayList<Attack> attackList = new ArrayList<Attack>();

    public AttackList() {
        //attackList = new ArrayList<Attack>();
    }

    public ArrayList getArrayList()
    {
        return attackList;
    }
    public void add(Attack attack)
    {
        attackList.add(attack);
    }

    public int getNumberOfAttacks()
    {
        return attackList.size();
    }
  
}
