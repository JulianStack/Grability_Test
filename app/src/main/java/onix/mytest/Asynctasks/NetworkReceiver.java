package onix.mytest.Asynctasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import onix.mytest.Adapters.ListItem;
import onix.mytest.Interfaces.Interfaces;
import onix.mytest.MainActivity;
import onix.mytest.SharedPref.CategoryManager;
import onix.mytest.SharedPref.ListsManager;

import static onix.mytest.Interfaces.Interfaces.*;

/**
 * Created by JulianStack on 22/03/2016.
 */
public abstract class NetworkReceiver extends BroadcastReceiver
{


    @Override
    public void onReceive( Context context, Intent intent )
    {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(     ConnectivityManager.TYPE_MOBILE );
        if ( activeNetInfo != null )
        {
            new UpdateMessage().execute();
            //sendSnackBarMessage();
           // Toast.makeText( context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
        }
        if( mobNetInfo != null )
        {
            new UpdateMessage().execute();
           // sendSnackBarMessage();
            //.makeText( context, "Mobile Network Type : " + mobNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
        }
    }

    public void sendSnackBarMessage(){
        if(!hostAvailable("www.google.com",80)){
            showSnack("Network Status OffLine", false);
        }else{

            showSnack("Network Status OnLine", true);
        }
    }

    public abstract void showSnack(String message, boolean active);



    public boolean hostAvailable(String host, int port) {

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 1500);
            return true;
        } catch (IOException e) {
            //showSnack("No Internet Access", true);
            System.out.println(e);
            return false;
        }
    }

    public class UpdateMessage extends AsyncTask<Void, Void, Void> {
        boolean isactive = false;
        protected Void doInBackground(Void... values) {

            isactive = hostAvailable("www.google.com",80);

            return null;
        }
        protected void onPostExecute(Void value) {
            if(!isactive){
                showSnack("Network Status OffLine", false);
            }else{
                showSnack("Network Status OnLine", true);
            }

        }
    }
}
