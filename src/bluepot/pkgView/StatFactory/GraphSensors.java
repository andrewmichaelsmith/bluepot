/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgView.StatFactory;

import bluepot.pkgModel.AttackData.Attack;
import bluepot.pkgModel.AttackData.AttackData;
import bluepot.pkgModel.AttackData.AttackList;
import bluepot.pkgModel.AttackData.AttackModule;
import bluepot.pkgModel.Model;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

/**
 *
 * @author andrew
 */
public class GraphSensors {
    private  Minute startTime;
    private Model model;
    private Millisecond biggestTime;
    private TimeSeries[]  sensors;
    private ListManager listManager;

    public GraphSensors(Model model, ListManager listManager) {

        startTime = new Minute(Calendar.getInstance().getTime());
        this.model = model;
        this.listManager = listManager;

        sensors = new TimeSeries[model.getNoOfDevices()];
        for(int i = 0; i < model.getNoOfDevices(); i++)
        {
            sensors[i] = new TimeSeries("" + i);
        }


        biggestTime = new Millisecond(new Date(0));


    }

     public void updateData()
    {
        AttackData[] attackDataStore;


        Millisecond tmpBiggestTime = biggestTime;


            attackDataStore = new AttackData[model.getNoOfDevices()];
            for(int i = 0; i < model.getNoOfDevices(); i++)
            {
                attackDataStore[i] = model.readAttackData(i);
            }
        AttackModule attackModule;
        AttackList attackList;


        for(int i = 0; i < attackDataStore.length; i++)
        {
            AttackModule attackModules[];
            String[] listOfModules = null;

            listOfModules = new String[attackDataStore[i].getListOfAttacks().length];
            listOfModules = attackDataStore[i].getListOfAttacks();



            for(int j = 0; j < listOfModules.length; j++)
            {
               
                String listOfAttacks[] = null;

                attackModule = attackDataStore[i].getModule(listOfModules[j]);

                listOfAttacks = new String[attackModule.getListOfAttacks().length];
                listOfAttacks = attackModule.getListOfAttacks();


                if(listOfAttacks.length>0)
                {
                    for(int k = 0; k < listOfAttacks.length; k++)
                    {
                        attackList = attackModule.get(listOfAttacks[k]);

                        //ArrayList tmpArrayList = attackList.getArrayList();
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
                                if(sensors[i].getDataItem(rtp)==null)
                                {
                                    sensors[i].add(rtp,1);
                                }
                                else
                                {
                                    TimeSeriesDataItem tmp = sensors[i].getDataItem(rtp);
                                    int plusone = tmp.getValue().intValue() + 1;
                                    sensors[i].addOrUpdate(rtp, plusone);

                                }





                            }
                        }

                    }

                }
            }

         }


        Minute endTime = new Minute(Calendar.getInstance().getTime());


        for(int i = 0; i < model.getNoOfDevices(); i++)
        {
            if(sensors[i].getDataItem(endTime)==null)
            {
                sensors[i].add(endTime,0);
            }
        }

        biggestTime = tmpBiggestTime;



     }

     public JFreeChart getChart() {

        TimeSeriesCollection dataset = new TimeSeriesCollection();


        for(int i = 0; i < model.getNoOfDevices(); i++)
        {
            dataset.addSeries(sensors[i]);
        }

        JFreeChart rtn = ChartFactory.createTimeSeriesChart(
            "Sensor Activity", // title
            "Time",             // x-axis label
            "Hits",   // y-axis label
            dataset,            // data
            true,               // create legend?
            false,               // generate tooltips?
            false               // generate URLs?
        );

         XYPlot plot = (XYPlot) rtn.getPlot();
        XYItemRenderer r = plot.getRenderer();

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        /*rangeAxis.setAutoRangeMinimumSize(1);
        rangeAxis.setLowerBound(0);*/
        rtn.setBorderVisible(false);
        rtn.setBackgroundPaint(null);
        rtn.setAntiAlias(true);
        

        return rtn;
     }



}
