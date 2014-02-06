/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author root
 */
public class DeviceClass {

 

  

  
    private static HashMap<String, BiMap> majorToMinor = new HashMap<String, BiMap>();


    private static BiMap<String, Integer> major = HashBiMap.create();

    private static BiMap<String, Integer> computer = HashBiMap.create();
    
    private static BiMap<String, Integer> phone = HashBiMap.create();
    private static BiMap<String, Integer> network = HashBiMap.create();
    private static BiMap<String, Integer> audiovideo = HashBiMap.create();
    private static BiMap<String, Integer> peripheral = HashBiMap.create();
    private static BiMap<String, Integer> imaging = HashBiMap.create();
    private static BiMap<String, Integer> wearable = HashBiMap.create();
    private static BiMap<String, Integer> toy = HashBiMap.create();
    private static BiMap<String, Integer> health = HashBiMap.create();
    private static BiMap<String, Integer> misc = HashBiMap.create();
    //TODO: uncat
    private static BiMap<String, Integer> uncategorized = HashBiMap.create();

    private String[][] majorMinorClasses;



    public DeviceClass()
    {
       
          major.put("Misc", 0);
          major.put("Computer", 256);
          major.put("Phone", 512);
          major.put("Network", 768);
          major.put("Audio/Video", 1024);
          major.put("Peripheral", 1280);
          major.put("Imaging", 1536);
          major.put("Wearable", 1792);
          major.put("Toy", 2048);
          major.put("Health", 2304);
          //major.put("Uncategorized", 7936);

          misc.put("Misc",0);
          
          computer.put("Uncategorized", 0);
          computer.put("Desktop", 4);
          computer.put("Server", 8);
          computer.put("Laptop", 12);
          computer.put("Handheld", 16);
          computer.put("Palm", 20);
          computer.put("Wearable", 24);

          phone.put("Uncategorized",0);
          phone.put("Cellular",4);
          phone.put("Cordless",8);
          phone.put("Smartphone",12);
          phone.put("Modem",16);
          phone.put("ISDN",20);

          network.put("Full Available",0);
          network.put("1 - 17% utilized",32);
          network.put("17 - 33% utilized",64);
          network.put("33 - 50% utilized",96);
          network.put("50 - 67% utilized",128);
          network.put("67 - 83% utilized",160);
          network.put("83 - 99% utilized",192);
          network.put("No service available",224);

          audiovideo.put("Uncategorized",0);
          audiovideo.put("Wearable",4);
          audiovideo.put("Hands-free",8);
          audiovideo.put("Microphone",16);
          audiovideo.put("Loudspeaker",20);
          audiovideo.put("Headphones",24);
          audiovideo.put("Portable Audio",28);
          audiovideo.put("Car Audio",32);
          audiovideo.put("Set-top Box",36);
          audiovideo.put("HiFi Audio Device",40);
          audiovideo.put("VCR",44);
          audiovideo.put("Video Camera",48);
          audiovideo.put("Camcorder",52);
          audiovideo.put("Video Monitor",56);
          audiovideo.put("Video Display and Loudspeaker",60);
          audiovideo.put("Video Conferencing",64);
          audiovideo.put("Gaming/Toy",72);


           peripheral.put("Keyboard - Uncategorized",0);
           peripheral.put( "Keyboard - Joystick",4);
           peripheral.put("Keyboard - Gamepad",8);
           peripheral.put("Keyboard - Remote Control",12);
           peripheral.put("Keyboard - Sensing Device",16);
           peripheral.put("Keyboard - Digitizer Tablet",20);
           peripheral.put("Keyboard - Card Reader",24);
           peripheral.put("Pointing Device - Uncategorized",28);//FIXME
/*           peripheral.put("Pointing Device - Joystick",28);//FIXME
           peripheral.put("Pointing Device - Gamepad",28);//FIXME
           peripheral.put("Pointing Device - Remote Control",28);//FIXME
           peripheral.put("Pointing Device - Sensing Device",28);//FIXME
           peripheral.put("Pointing Device - Digitizer Tablet",28);//FIXME
           peripheral.put("Pointing Device - Card Reader",28); //FIXME*/


           imaging.put("Display",16);
           imaging.put("Camera",32);
           imaging.put("Scanner",54);
           imaging.put("Printer",128);

           wearable.put("Wrist Watch",4);
           wearable.put("Pager",8);
           wearable.put("Jacket",12);
           wearable.put("Helmet",16);
           wearable.put("Glasses",20);


           toy.put("Robot",4);
           toy.put("Vehicle",8);
           toy.put("Doll / Action Figure",12);
           toy.put("Controller",16);
           toy.put("Game",20);

           health.put("Undefined",0);
           health.put("Blood Pressure Monitor",4);
           health.put("Thermometer",8);
           health.put("Weighing Scale",12);
           health.put("Glucose Meter",16);
           health.put("Pulse Oximeter",20);
           health.put("Heart/Pulse Rate Monitor",24);
           health.put("Health Data Display",28);


            //majorToMinor.put("Miscellaneous",null);
            majorToMinor.put("Computer",computer);
            majorToMinor.put("Phone",phone);
            majorToMinor.put("Network",network);
            majorToMinor.put("Audio/Video", audiovideo);
            majorToMinor.put("Peripheral", peripheral);
            majorToMinor.put("Imaging", imaging);
            majorToMinor.put("Wearable", wearable);
            majorToMinor.put("Toy", toy);
            majorToMinor.put("Health",health);
            //majorToMinor.put("Uncategorized",null);

            majorToMinor.put("Misc",misc);



    }

    public String minorIntToString(int ma, int mi)
    {
        String majorString = major.inverse().get(ma);
        BiMap<String, Integer> majorMap = majorToMinor.get(majorString);
        return majorMap.inverse().get(mi);
    }

    public String majorIntToString(int ma)
    {
        return major.inverse().get(ma);
    }
    public int getMajorInt(String s)
    {
        return  Integer.parseInt(major.get(s).toString());
    }

    public int getMinorInt(String ma, String mi)
    {
        BiMap tmp = majorToMinor.get(ma);
        return  Integer.parseInt(tmp.get(mi).toString());
    }
    
  

    public String[] getMinorList(String majorClass)
    {
        BiMap minor = majorToMinor.get(majorClass);
        Set tmp = minor.keySet();
        Object[] a = tmp.toArray();
        String[] rtn = new String[tmp.size()];

        for(int i = 0; i < tmp.size(); i++)
        {
            rtn[i] = (String) a[i];
        }

        return rtn;


    }

    public String[] getMajorList() {

        Set tmp = major.keySet();
        Object[] a = tmp.toArray();
        String[] rtn = new String[tmp.size()];

        for(int i = 0; i < tmp.size(); i++)
        {
            rtn[i] = (String) a[i];
        }

        return rtn;

    }


}
