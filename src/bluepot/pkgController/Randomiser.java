/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgController;

import bluepot.pkgModel.Model;
import bluepot.pkgView.View;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;
import org.apache.log4j.Level;

/**
 *
 * @author andrew
 */
public class Randomiser implements Runnable {

    //TODO: one issue with the randomiser is that when changing names they may not be unique because
    //it doesnt do them all at once..
    View view;
    Model model;

    Vector names;

    public Randomiser(Model model, View view)
    {
        this.model = model;
        this.view = view;
        names = new Vector();
        loadNameList();

    }


    public void loadNameList()
    {
        names.clear();
        
        File nameList = new File("data/config.ser");
        if(nameList.exists())
        {

            try {
                //TODO: nicked code, refactor
                    FileInputStream fstream = new FileInputStream(model.getSettings().getRandomNameFolder());
                     DataInputStream in = new DataInputStream(fstream);
                     BufferedReader br = new BufferedReader(new InputStreamReader(in));
                     String strLine;
                      while ((strLine = br.readLine()) != null)   {
                          // Print the content on the console
                          names.add(strLine);
                        }
                        //Close the input stream
                        in.close();

                  
                 } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Model.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);

                }
        }
    }
    
    public void run() {


           long sleepLength = model.getSettings().getRandomMinutes() * 1000 * 60;

           //on start we don't want to change straight away
            try {
            Thread.sleep(sleepLength);
        } catch (InterruptedException ex) {
        }

           
       while(true) {

        if(model.getSettings().isRandomEnabled())

        {


            loadNameList();
            String[] namesList = randomNames();

            if( (namesList.length>0) && (namesList.length > model.getNoOfDevices()))
            {
            Vector[] classesList = null;
            if(model.getSettings().isRandomDeviceClass())
            {
                classesList = randomDeviceClass();
            }
            
            for(int i = 0; i < namesList.length; i++)
            {
                if(!model.getSensor(i).isLocked())
                {
                int major = 0;
                int minor = 0;
                
                if(model.getSettings().isRandomDeviceClass())
                {
                   major =  (Integer) classesList[0].get(i);
                   minor =  (Integer) classesList[1].get(i);
                }
                         if(model.getSettings().isRandomDeviceClass())
                         {
                            model.setLocalDeviceClass(i, major, minor);
                         }
                        model.setLocalDeviceName(i, namesList[i]);
                        model.resetTableData();
                        view.fireMainTableDataChanged();
                        model.applyTableData();
                        view.fireSmallTableDataChanged();
                        view.updateSensors();
                        

              }

                    try {
                        Thread.sleep(sleepLength / model.getNoOfDevices());
                    } catch (InterruptedException ex) {

                    }
                
                
            }
            

        }
        else
        {
                model.log(Level.WARN, "Randomiser not using names list because the number of entries is less than the number of devices");
                try {
                    Thread.sleep(sleepLength);
                } catch (InterruptedException ex) {
                   
                }
        }
        }
       }
    }

    public Vector[] randomDeviceClass()
    {

        Vector majorList = new Vector();
        Vector minorList = new Vector();


        for(int i = 0; i < model.getNoOfDevices(); i++)
        {
            Random generator = new Random();
            int r;

            int noOfMajor = model.getMajorClassesList().length;
            r = generator.nextInt(noOfMajor);
            String majorName = model.getMajorClassesList()[r];
            int major = model.getMajorClassInt(majorName);

            int noOfMinor = model.getMinorClassesList(majorName).length;

            r = generator.nextInt(noOfMinor);
            String minorName = model.getMinorClassesList(majorName)[r];
            int minor = model.getMinorClassInt(majorName, minorName);
            
                minorList.add(minor);
                majorList.add(major);
            }

        Vector[] majorMinor= new Vector[2];

        majorMinor[0] = majorList;
        majorMinor[1] = minorList;

        return majorMinor;
        
    }
       

    public String[] randomNames() {

//TODO: infinite loop if list of names < number of devices
        
        
        Random generator = new Random();
        
        Set randomNumbersName = new HashSet();

        for(int i = 0; i < model.getNoOfDevices(); i++)
        {
            Boolean unique = false;

            while(!unique)
            {
                int oldSize = randomNumbersName.size();

                int r = generator.nextInt(names.size());
                randomNumbersName.add(names.get(r));

                int newSize = randomNumbersName.size();

                if(newSize > oldSize)
                {
                    unique = true;
                }
            }

        }

       return (String[]) randomNumbersName.toArray(new String[model.getNoOfDevices()]);
        
    }

 
}


