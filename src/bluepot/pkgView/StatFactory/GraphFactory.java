/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.StatFactory;

import bluepot.pkgModel.AttackData.Attack;
import bluepot.pkgModel.Model;
import bluepot.pkgModel.AttackData.AttackData;
import bluepot.pkgModel.AttackData.AttackList;
import bluepot.pkgModel.AttackData.AttackModule;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

/**
 *
 * @author andrew
 */
public class GraphFactory {

        Model model;

         
        private AttackData a;
        private AttackModule attackModule;
        private AttackList attackList;

        private Minute startTime;

        private TimeSeries data;


        private int sensor;

        private String module;

        private String list;

        private String title;
        private String x;
        private String y;

        private int type;

        public static int TYPE_BAR = 1;
        public static int TYPE_TIME = 0;

        Millisecond biggestTime;


        private ListManager listManager;

        //TODO: may not always want a listmanager, method override bitch
    public GraphFactory(Model model, int sensor, String module, String list, String title, String x, String y, int type, ListManager listManager) {


        startTime = new Minute(Calendar.getInstance().getTime());
        this.model = model;
        this.listManager = listManager;
        this.sensor = sensor;
        this.module = module;
        this.list = list;
        this.x = x;
        this.y = y;
        this.title = title;
        this.type = type;

        biggestTime = new Millisecond(new Date(0));
        
        data = new TimeSeries("");
        data.add(startTime,0);



    }

    public void updateData()
    {
        AttackData[] attackDataStore;


        Millisecond tmpBiggestTime = biggestTime;
        
        if(sensor >= 0)
        {
            attackDataStore = new AttackData[1];
            
            attackDataStore[0] = model.readAttackData(sensor);
        }

        else {
            attackDataStore = new AttackData[model.getNoOfDevices()];
            for(int i = 0; i < model.getNoOfDevices(); i++)
            {
                attackDataStore[i] = model.readAttackData(i);
            }

        }


        for(int i = 0; i < attackDataStore.length; i++)
        {
            AttackModule attackModules[];
            String[] listOfModules = null;

            //if they've specified a module we only want to consider the one module
            if(!module.isEmpty())
            {
                listOfModules = new String[1];
                listOfModules[0] = module;
            }
            //otherwise we want to loop throguh every module
            else
            {
                listOfModules = new String[attackDataStore[i].getListOfAttacks().length];
                listOfModules = attackDataStore[i].getListOfAttacks();
            }
                

            for(int j = 0; j < listOfModules.length; j++)
            {

                String listOfAttacks[] = null;
           
                attackModule = attackDataStore[i].getModule(listOfModules[j]);

                if(!list.isEmpty())
                {
                    listOfAttacks = new String[1];
                    listOfAttacks[0] = list;
                }
                else
                {
                     listOfAttacks = new String[attackModule.getListOfAttacks().length];
                     listOfAttacks = attackModule.getListOfAttacks();
                }

                if(listOfAttacks.length>0)
                {
                    for(int k = 0; k < listOfAttacks.length; k++)
                    {
                        attackList = attackModule.get(listOfAttacks[k]);

                       // ArrayList tmpArrayList = attackList.getArrayList();
                        List tmpArrayList = Collections.synchronizedList(attackList.getArrayList());
                        Iterator tmpIterator = tmpArrayList.iterator();

                        while(tmpIterator.hasNext())
                        {

                            Attack attack =  (Attack) tmpIterator.next();
                            Date d = attack.getTime();
                            Minute rtp = new Minute(d);
                            Millisecond comp = new Millisecond(d);

                            //Running count, biggest time for this update
                            if(comp.compareTo(tmpBiggestTime) > 0)
                            {
                                tmpBiggestTime = comp;
                            }

                            if(comp.compareTo(biggestTime) > 0)
                            {

                                listManager.addAttack(attack);

                                //Here we actually are adding or updating points on the graph
                                if(data.getDataItem(rtp)==null)
                                {
                                    data.add(rtp,1);
                                }
                                else
                                {
                                    TimeSeriesDataItem tmp = data.getDataItem(rtp);
                                    int plusone = tmp.getValue().intValue() + 1;
                                    data.addOrUpdate(rtp, plusone);

                                }

                               

                            }
                        }

                    }

                }
            }

         }


        Minute endTime = new Minute(Calendar.getInstance().getTime());


        if(data.getDataItem(endTime)==null)
        {
            data.add(endTime,0);
        }

        biggestTime = tmpBiggestTime;


    }

    public JFreeChart getChart() {
        
        TimeSeriesCollection dataset = new TimeSeriesCollection(data);

        JFreeChart rtn = null;


        switch(type)
        {
            case 0:
                rtn = ChartFactory.createTimeSeriesChart(
                    title, // title
                    x,             // x-axis label
                    y,   // y-axis label
                    dataset,            // data
                    false,               // create legend?
                    false,               // generate tooltips?
                    false               // generate URLs?
                ); break;
            default:
                rtn = ChartFactory.createXYBarChart(
                title,
                x,
                true,
                y,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
                ); break;
        }

        XYPlot plot = (XYPlot) rtn.getPlot();
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        /*
        rangeAxis.setAutoRangeMinimumSize(1);
        rangeAxis.setLowerBound(0);
*/
        rtn.setBorderVisible(false);
        rtn.setBackgroundPaint(null);
        rtn.setAntiAlias(true);

        return rtn;
    }

}
