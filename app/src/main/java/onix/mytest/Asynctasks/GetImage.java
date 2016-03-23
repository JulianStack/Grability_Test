package onix.mytest.Asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;

import onix.mytest.Adapters.ListItem;

/**
 * Created by JulianStack on 21/03/2016.
 */
public class GetImage extends AsyncTask<String, Void, Bitmap> {

    String Url;
    int Pos;
    ArrayList<ListItem> Lista;
    ArrayList<ListItem> Lista2;
    Bitmap mIcon11 = null;

    public GetImage(MyBitmapCallback callback,String url,int pos, ArrayList<ListItem> lista) {
        this.mRespCallback = callback;
        this.Url = url;
        this.Lista = lista;
        this.Pos = pos;
    }


    private MyBitmapCallback mRespCallback;

    public interface MyBitmapCallback {
        void call(Bitmap bitmap, int Pos);
    }

    protected Bitmap doInBackground(String... urls) {
        try {
                    InputStream in = new java.net.URL(Url).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            this.cancel(true);
            Log.e("Error in bitmap", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        mRespCallback.call(mIcon11,Pos);
    }
}