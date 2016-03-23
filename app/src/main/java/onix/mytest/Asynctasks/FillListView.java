package onix.mytest.Asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;

import onix.mytest.Adapters.ListItem;

/**
 * Created by JulianStack on 21/03/2016.
 */
public class FillListView extends AsyncTask<ArrayList<ListItem>, Void, ListItem> {

    int Pos;
    ArrayList<ListItem> Lista;
    ListItem item;
    private MyListCallBack mRespCallback;

    public interface MyListCallBack {
        void call(ListItem item);
    }
    public FillListView(MyListCallBack callback, int pos, ArrayList<ListItem> lista) {
        this.Pos = pos;
        this.mRespCallback = callback;
        this.Lista = lista;
    }

    @Override
    protected ListItem doInBackground(ArrayList<ListItem>... arrayLists) {
        item = Lista.get(Pos);
        return item;
    }
    protected void onPostExecute(ListItem result) {
        mRespCallback.call(item);
    }

}
