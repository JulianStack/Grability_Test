package onix.mytest.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import onix.mytest.Adapters.AdapterGridApp;
import onix.mytest.Adapters.AdapterListApp;
import onix.mytest.Adapters.ListItem;
import onix.mytest.Asynctasks.FillListView;
import onix.mytest.Asynctasks.FillListView.MyListCallBack;
import onix.mytest.Asynctasks.GetImage;
import onix.mytest.Asynctasks.GetJson;
import onix.mytest.Asynctasks.NetworkReceiver;
import onix.mytest.Interfaces.Interfaces;
import onix.mytest.Interfaces.Interfaces.FragmentCommunicator;
import onix.mytest.MainActivity;
import onix.mytest.R;
import onix.mytest.SharedPref.CategoryManager;
import onix.mytest.SharedPref.ListsManager;
import onix.mytest.Splash.ResumeAppDialog;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static onix.mytest.Interfaces.Interfaces.*;
import static onix.mytest.SharedPref.ListsManager.isTablet;


/**
 * Created by JulianStack on 19/03/2016.
 */
public class Home extends Fragment implements FragmentCommunicator {


    Context context;
    GridView grid;
    AdapterListApp adapterList;
    AdapterGridApp adapterGrid;
    ProgressBar progress;
    ActivityCommunicator activitycommunicator;
    public boolean sent = false;
    Bundle bundle;
    boolean GridEnabled = false;
    ImageView errorImage;
    TextView errorText;

    View CurrentView;
    ViewGroup Container;
    ArrayList<ListItem> retrieved = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.homepage, container, false);
        CurrentView = rootView;
        Container = container;

        Log.d("FRAGMENT","ONCREATE");
        bundle = this.getArguments();

        grid = (GridView)rootView.findViewById(R.id.gridView);
        errorImage = (ImageView)rootView.findViewById(R.id.connection);
        errorText = (TextView)rootView.findViewById(R.id.textconnection);

        progress = (ProgressBar)rootView.findViewById(R.id.progress);

        SetGrid();
        disableError();


        registerClickCallback();
        setHasOptionsMenu(true);



        return rootView;
    }


    public View getMyCurrentView(){
        return CurrentView;
    }

    public ViewGroup getViewParent(){
        return Container;
    }


    private void SetGrid() {
        if(isTablet(this.getActivity())){
          //  getActivity().setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
            GridEnabled = true;
            grid.setNumColumns(3);
        }else{
            //getActivity().setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
            GridEnabled = false;
        }
       // initList();
    }


    private void initList() {
        if (bundle != null) {
            /*
            retrieved =  bundle.getParcelableArrayList("PARCELABLE_LIST");
            //adapterList = new AdapterListApp(getActivity(), retrieved);


            Log.d("FRAGMENT","LIST RETRIEVED");

            setCurrentAdapter(getActivity(), retrieved);
            grid.setAdapter(getAdapter());

            LoadImages(retrieved);

            progress.setVisibility(View.GONE);
            */

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    new LoadListView().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            };

            handler.postDelayed(runnable, 100);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // return the current adapter
    public BaseAdapter getAdapter(){
        BaseAdapter temporary;
        if(GridEnabled){
            temporary = this.adapterGrid;
        }else{
            temporary = this.adapterList;
        }
        return temporary;
    }

    // set adapter we'll use
    public void setCurrentAdapter(Activity activity, ArrayList<ListItem> list){
        if(GridEnabled){
            adapterGrid = new AdapterGridApp(activity, list);
        }else{
            adapterList = new AdapterListApp(activity, list);
        }
    }

    public void disableError(){
        errorText.setText(null);
        errorText.setEnabled(false);
        errorImage.setImageBitmap(null);
        errorImage.setEnabled(false);
        }

        public void enableError(){
            errorText.setEnabled(true);
            errorText.setText("Internet Broken");

        errorImage.setEnabled(true);
        errorImage.setImageResource(R.drawable.dinosaurio);
    }

    // receive messages from activity
    @Override
    public void passDataToFragment(ArrayList<ListItem> list,String someValue) {

        retrieved = list;

        /*
        Log.d("FRAGMENT LIST ", String.valueOf(retrieved));
        setCurrentAdapter(getActivity(), retrieved);
        grid.setAdapter(getAdapter());
        LoadImages(retrieved);
        disableError();
        */
        new LoadListView2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        Log.d("FRAGMENT", someValue);

    }

    @Override
    public void passMessageToFragment(String someValue) {
        enableError();
        Log.d("FRAGMENT", "ERROR CONNECTION");
    }


    // register both interfaces (API 23)
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Context mycontext = getActivity();
        Log.d("FRAGMENT", "on Attach");
        ((MainActivity)mycontext).fragmentcommunicator = this;
        activitycommunicator =(ActivityCommunicator)mycontext;

    }

    // register both interfaces
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Context mycontext = getActivity();
        Log.d("FRAGMENT", "on Attach activity");

        context = getActivity();
     //   if(activity instanceof FragmentCommunicator){
        ((MainActivity)context).fragmentcommunicator = this;

      /*  }else
        {
            throw new ClassCastException();
        }
        */
        activitycommunicator =(ActivityCommunicator)context;

       // ((MainActivity)context).fragmentcommunicator = this;



    }

    @Override
    public void onResume(){
        super.onResume();
        int activityValue =((MainActivity)context).InterfaceIntValue;
        if(activityValue == 0){
            //show one view
        }else{
            // show other view
        }
        initList();
    }


    // launch an Asynctask to load images and process it inside this callback
    public void LoadImages(final ArrayList<ListItem> list) {

        Log.d("FRAGMENT", "Loading Images");


        for (int i = 0; i < list.size(); i++) {
            int ready = list.get(i).getDownloaded();

            Log.d("FRAGMENT Image_value",String.valueOf(ready));
            if (ready == 0) {

                new GetImage(new GetImage.MyBitmapCallback() {
                    @Override
                    public void call(Bitmap bitmap, int Pos) {
                        retrieved.get(Pos).setImagen(bitmap);
                        retrieved.get(Pos).setDownloaded(1);
                        getAdapter().notifyDataSetChanged();

                        activitycommunicator.passDataToActivity(retrieved,Pos, "Replace Object In List");
                    }

                },list.get(i).getUrl()[2],i, list).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

            }
        }


    }

    ////////////////   /////////////////
    private void registerClickCallback() {
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
               // Toast.makeText(getActivity(), retrieved.get(position).getTitle(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), ResumeAppDialog.class);
                Bundle bundle = new Bundle();
                ListItem temp = retrieved.get(position);
                bundle.putParcelable("PARCELABLE_ITEM", temp);
                intent.putExtras(bundle);
                startActivity(intent);

                /*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(), getAdapter().getView(position,getMyCurrentView(),getViewParent()).findViewById(R.id.Preview), "preview");
                    startActivity(intent, options.toBundle());

                }
                else {
                    startActivity(intent);
                }

                */


            }
        });
    }


    public class LoadListView extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... values) {
                retrieved =  bundle.getParcelableArrayList("PARCELABLE_LIST");
                Log.d("FRAGMENT","LIST RETRIEVED");
                setCurrentAdapter(getActivity(), retrieved);
            return null;
        }
        protected void onPostExecute(Void value) {

            grid.setAdapter(getAdapter());
            LoadImages(retrieved);
            progress.setVisibility(View.GONE);
            // back in UI thread after task is done
        }
    }

    public class LoadListView2 extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... values) {
            Log.d("FRAGMENT", "LIST RETRIEVED");
            setCurrentAdapter(getActivity(), retrieved);
            return null;
        }
        protected void onPostExecute(Void value) {

            grid.setAdapter(getAdapter());
            LoadImages(retrieved);
            progress.setVisibility(View.GONE);
            disableError();
            // back in UI thread after task is done
        }
    }


}
