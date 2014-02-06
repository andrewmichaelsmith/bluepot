/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.StatFactory;

import bluepot.pkgModel.AttackData.Attack;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author andrew
 */
public class ListManager {

        HashMap<String,Integer> attackerInstances = new HashMap<String, Integer>();


        Stack recentAttackers = new Stack();

    public void addAttack(Attack attack)
    {
        recentAttackers.push(new AttackerAndTime(attack.getAttacker(), attack.getTime().getTime()));

        String attacker = attack.getAttacker();

        if(attackerInstances.get(attacker) == null){
            attackerInstances.put(attack.getAttacker(),1);
        }
        else
        {
            int attackerCount = attackerInstances.get(attack.getAttacker());
            attackerCount++;
            attackerInstances.put(attack.getAttacker(),attackerCount);
        }

        
       
        
    }

    public int getNoOfAttackers()
    {
        return attackerInstances.size();
    }
    public int getNoOfAttacks()
    {
        return recentAttackers.size();
    }

    public String getRecentAttackers(int count)
    {

        
        Stack temp = (Stack) recentAttackers.clone();



        int i = 0;

        String rtn = "<html><ol>";
        
        while( (i < count) && (temp.size()>0) )
        {
            AttackerAndTime data = (AttackerAndTime) temp.pop();

            rtn = rtn + "<li>" + data.getAttacker() + "</li>";

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

    public String getTopVolumeAttackers(int count)
    {
//        SortedMap sortedData = new TreeMap(new ValueComparer(attackerInstances));
//        sortedData.putAll(attackerInstances);

        LinkedHashMap sortedData = new LinkedHashMap<String, Integer>();
        sortedData =  (LinkedHashMap) sortByValue(attackerInstances);

        Iterator it = sortedData.keySet().iterator();
        int i = 0;
        String rtn = "<html><ol>";

        
        while((it.hasNext()) && (i < count))
        {
           String attacker = (String) it.next();
           rtn = rtn + "<li>" + attacker + "</li>";

           i++;
        }

        while(i < count)
        {
            rtn = rtn + "<li>-               </li>";
            i++;
        }

        rtn = rtn + "</ol></html>";


        return rtn;
    }

    private class AttackerAndTime {
        String attacker;
        Long time;
        public AttackerAndTime(String attacker, Long time)
        {
            this.attacker = attacker;
            this.time = time;
        }

        public String getAttacker() {
            return attacker;
        }



        public Long getTime() {
            return time;
        }

    }
    //TODO:
    //FIXME:
    //!!!!!!!!!!
    //Following methods are taken from: http://paaloliver.wordpress.com/2006/01/24/sorting-maps-in-java/
    private static class ValueComparer implements Comparator {
		private Map  _data = null;
		public ValueComparer (Map data){
			super();
			_data = data;
		}

         public int compare(Object o1, Object o2) {
        	 String e1 = (String) _data.get(o1);
             String e2 = (String) _data.get(o2);
             return e1.compareTo(e2);
         }
	}


    static Map sortByValue(Map map) {
     List list = new LinkedList(map.entrySet());
     Collections.sort(list, new Comparator() {
          public int compare(Object o1, Object o2) {
               return ((Comparable) ((Map.Entry) (o1)).getValue())
              .compareTo(((Map.Entry) (o2)).getValue());
          }
     });
// logger.info(list);
Map result = new LinkedHashMap();
for (Iterator it = list.iterator(); it.hasNext();) {
     Map.Entry entry = (Map.Entry)it.next();
     result.put(entry.getKey(), entry.getValue());
     }
return result;
}

 


}
