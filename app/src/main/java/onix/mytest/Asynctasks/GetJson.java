package onix.mytest.Asynctasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Handler;

import onix.mytest.Interfaces.Interfaces;

import static onix.mytest.Interfaces.Interfaces.*;

/**
 * Created by JulianStack on 19/03/2016.
 */
public class GetJson extends AsyncTask<String, String, JSONArray> {

    HttpURLConnection urlConnection;
    String Myurl;
    JSONArray jsonObject = new JSONArray();

    private Boolean result;

    private MyResponseCallback mRespCallback;

    public interface MyResponseCallback {
        void call(JSONArray jsonarray);
        void canceled (boolean canceled);
    }

    public GetJson(MyResponseCallback callback, String url) {
        this.mRespCallback = callback;
        this.Myurl = url;
    }

    @Override
    protected JSONArray doInBackground(String... args) {

        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(Myurl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String aux = "";
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
                aux += line;
            }
            JSONObject json = new JSONObject(aux);
            JSONObject json2 = json.getJSONObject("feed");
            jsonObject = json2.getJSONArray("entry");

        }catch( Exception e) {
            onCancelled();
            cancel(true);
            e.printStackTrace();
            Log.d("JSON RESPONSE ERROR  ", String.valueOf(e));
        }
        finally {
            urlConnection.disconnect();
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        mRespCallback.call(jsonObject);
    }

    /*
    @Override
    protected void onCancelled() {
        mRespCallback.canceled(true);
        Log.d("JSON CANCELLED", "CANCELLED");
    }
    */

    @Override
    protected void onCancelled(JSONArray result){
        System.out.println("ON CANCELLED (args)!");
        mRespCallback.canceled(true);
        Log.d("JSON CANCELLED", "CANCELLED");
    }


}