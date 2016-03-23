package onix.mytest.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by JulianStack on 20/03/2016.
 */
public class CategoryManager {

    public static String TAG = "CategoryManager";
    public static String JSONDATA = "JsonData";
    public static String CATEGORYLIST = "CategoryList";


    public static void SaveCategoryList(List<String> lista, Context context){

        Log.d(TAG, "SAVE ARRAYLIST");
        SharedPreferences preference = context.getSharedPreferences(JSONDATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = preference.edit();
        Log.d(TAG, "LIST TO BE SAVED : " + String.valueOf(lista));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++) {
            sb.append(lista.get(i)).append(",");
        }

            edit.putString(CATEGORYLIST, sb.toString());
            edit.apply();
            Log.d(TAG, "ARRAYLIST SAVED");
    }

    public static ArrayList<String> getList(Context context){

        SharedPreferences preference = context.getSharedPreferences(JSONDATA, Context.MODE_PRIVATE);
        String achievedList = preference.getString(CATEGORYLIST, null);

        String[] Lista = new String[0];
        if(achievedList!=null){
            Lista = achievedList.split(",");
        }

        ArrayList<String> wordList = new ArrayList<>();
        for(int i = 0; i<Lista.length; i++){
            wordList.add(Lista[i]);
        }
        Log.d(TAG, "RETRIEVED LIST : " + String.valueOf(achievedList));
        return wordList;
    }
}
