/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.StatFactory;

import bluepot.pkgModel.AttackData.Attack;
import bluepot.pkgModel.AttackData.OBEX.AttackFileRcvd;
import java.util.Stack;

/**
 *
 * @author andrew
 */
public class ListManagerOBEX extends ListManager {

    Stack recentFiles = new Stack();

    public ListManagerOBEX()
    {
        super();
    }
    
    @Override
    public void addAttack(Attack attack)
    {
        super.addAttack(attack);

        AttackFileRcvd obexAttack = (AttackFileRcvd) attack;


        recentFiles.push(obexAttack.getFileName());
    }

    public int getNoOfFiles()
    {
        return recentFiles.size();
    }
    public String getRecentFiles(int count)
    {



        Stack temp = (Stack) recentFiles.clone();



        int i = 0;

        String rtn = "<html><ol>";

        while( (i < count) && (temp.size()>0) )
        {
            String filename = (String) temp.pop();

            rtn = rtn + "<li>" + filename + "</li>";

            i++;
        }

        while( (i < count))
        {
            rtn = rtn + "<li>-               </li>";
            i++;
        }
        rtn = rtn + "</ol></html>";


        return rtn;
    }

}
