/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.AttackData;

/**
 *
 * @author andrew
 */
public interface AttackGroupInterface {

    //private HashMap<String,AttackModule>;

    //public void AttackGroupInterface();

    public String[] getListOfAttacks();
    public int getTotalAttackNumber();
    public int getAttackNumberForAttack(String attackName);
    public String[] getAttackers();
    public String[] getAttackersForAttack(String attackName);

}
