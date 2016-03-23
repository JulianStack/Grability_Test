package onix.mytest.SharedPref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import onix.mytest.Adapters.ListItem;

/**
 * Created by JulianStack on 21/03/2016.
 */
public class ListsManager {

    public static String TAG = "ListsManager";
    public static String TYPEDEVICE = "TypeDevice";
    public static String ISTABLET = "IsTablet";


// Serialize object
    public static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }
    // Deserialize object
    public static Object readObject(Context context, String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

    public static void SaveFullList(Context context, ArrayList<ArrayList<ListItem>> lista, ArrayList<String> categories){
        try {
            writeObject(context, "Lista", lista);
            Log.d(TAG, "Writting Object : "+lista.toString());

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }


 public static ArrayList<ArrayList<ListItem>> getFullList( Context context){
     ArrayList<ArrayList<ListItem>> fullList = new ArrayList<>();
     try {
         Object tempList = readObject(context, "Lista");
         fullList = (ArrayList) tempList;
     } catch (IOException e) {
         e.printStackTrace();
     } catch (ClassNotFoundException e) {
         e.printStackTrace();
     }
     return fullList;
 }

    public static ArrayList<ArrayList<ListItem>> replaceList(ArrayList<String> category, ArrayList<ListItem> lista, int indexArrayList, int IndexObject, Context context){
        ArrayList<ArrayList<ListItem>> FullList = new ArrayList<>();
        try {
        Object tempList  = readObject(context, "Lista");
            FullList = (ArrayList) tempList;


            FullList.get(indexArrayList).get(IndexObject).setData(lista.get(IndexObject).getData());
            FullList.get(indexArrayList).get(IndexObject).setDownloaded(lista.get(IndexObject).getDownloaded());

            /*
            for(int i = 0; i<FullList.size(); i++){
                for(int j = 0; j<FullList.get(i).size(); j++){
                    if(FullList.get(i).get(j).getTitle().equals(lista.get(index).getTitle())){
                        FullList.get(i).set(j,lista.get(index));
                    }
                }
            }
            */
            SaveFullList(context,FullList,category);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return FullList;
    }

    public static boolean isTabletDevice(Context activityContext) {
        // Verifies if the Generalized Size of the device is XLARGE to be
        // considered a Tablet
        boolean xlarge = ((activityContext.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE);

        // If XLarge, checks if the Generalized Density is at least MDPI
        // (160dpi)
        if (xlarge) {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) activityContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
            // DENSITY_TV=213, DENSITY_XHIGH=320
            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {

                // Yes, this is a tablet!
                return true;
            }
        }

        // No, this is not a tablet!
        return false;
    }


    public static void SaveTypeDevice(boolean istablet, Context context){
        Log.d(TAG, "SAVE ARRAYLIST");
        SharedPreferences preference = context.getSharedPreferences(TYPEDEVICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preference.edit();
        edit.putBoolean(ISTABLET, istablet);
        edit.apply();
        Log.d(TAG, "ARRAYLIST SAVED");
    }

    public static boolean isTablet(Activity activity){
        boolean istablet;
        Context context = activity.getApplicationContext();
        SharedPreferences preference = context.getSharedPreferences(TYPEDEVICE, Context.MODE_PRIVATE);
        istablet = preference.getBoolean(ISTABLET, false);
        return  istablet;
    }
}
