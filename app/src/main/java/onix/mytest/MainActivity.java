package onix.mytest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Telephony;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import onix.mytest.Adapters.ListItem;
import onix.mytest.Adapters.NavDrawerAdapter;
import onix.mytest.Adapters.NavDrawerItem;
import onix.mytest.Asynctasks.GetJson;
import onix.mytest.Asynctasks.NetworkReceiver;
import onix.mytest.Fragments.Home;

import onix.mytest.Interfaces.Interfaces;
import onix.mytest.Interfaces.Interfaces.ActivityCommunicator;
import onix.mytest.Interfaces.Interfaces.ActivitySnacksbars;
import onix.mytest.Interfaces.Interfaces.FragmentCommunicator;
import onix.mytest.SharedPref.CategoryManager;
import onix.mytest.SharedPref.ListsManager;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static onix.mytest.SharedPref.ListsManager.isTablet;


public class MainActivity extends AppCompatActivity implements ActivityCommunicator {

    volatile Bitmap preImage;
    public int InterfaceIntValue =1;


    public FragmentCommunicator fragmentcommunicator;
    public static Fragment fragment = new Home();

    ActionBarDrawerToggle mDrawerToggle;


    private ArrayList navDrawerItems;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    public NavDrawerAdapter adapter;

    Toolbar toolbar;
    TextView mTitle;
    ArrayList<String> Category = new ArrayList<String>();

    public ArrayList<ListItem> GeneralList = new ArrayList<>();


    public boolean isRunning = false;
    public boolean success = false;

    ArrayList<ArrayList<ListItem>> group =new ArrayList<>();

    Snackbar snackbar;

    CoordinatorLayout Clayout;

    NetworkReceiver mReceiver;
    volatile boolean registered = false;
    Snackbar snack = null;
    public int LastPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // change screen otientation
        if(isTablet(this)){
            setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        }else{
            setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        Drawable drawable = getResources().getDrawable(R.drawable.preview);
        // Load preview image as bitmap im memory
        preImage = ((BitmapDrawable)drawable).getBitmap();

        // set custom toolbar as actionbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        adapter = new NavDrawerAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        if (savedInstanceState == null) {
            displayView(0);
        }
    }

    // Get and Parse JSON
    public void GetJson(){

            final GetJson obj = new GetJson(new GetJson.MyResponseCallback() {
                public void call(JSONArray json) {

                    if(!isRunning){

                        isRunning = true;
                        ParseJSON parser = new ParseJSON(json);
                        LoadListMenu LoadLists = new LoadListMenu();

                        parser.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                        LoadLists.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

                    }
                }
                @Override
                public void canceled(boolean canceled) {
                    Log.d("JSON ASYNCTASK ", "CANCELLED CALLED");
                    fragmentcommunicator.passMessageToFragment("JSON ERROR");
                    setTitle("Connection Error");
                    setStatus(false);
                    isRunning = false;
                }
            }, "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json");
            obj.execute();
    }


// check if there is connection
    public void CheckConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo!= null && networkInfo.isConnected()) {
            if(!getStatus()){GetJson();
                setStatus(true);}
        } else {
            Log.d("CHECK CONNECTION  :", String.valueOf(networkInfo));
            fragmentcommunicator.passMessageToFragment("there is not internet connection");
        }
    }


    // try retieve data before to use connection
    public void Init(){

        Log.d("INIT", "INIT LISTS");
        Category = new ArrayList<>(CategoryManager.getList(getApplicationContext()));
        // if lists is empty try get data from internet
        Log.d("INIT list", String.valueOf(Category.size()));
        if(Category.isEmpty() || Category.size()==0){
            if(!getStatus()){CheckConnection();}
        }else{
            // use data
            if(!getStatus()){
                setStatus(true);
                isRunning = true;
                group = new ArrayList<>(ListsManager.getFullList(getApplicationContext()));
                FillList(Category);
                fragmentcommunicator.passDataToFragment(group.get(0), "LISTS RETRIEVED Successfully");
                setTitle(Category.get(0));
            }

        }
    }

    public boolean getStatus(){ return success; }
    public void setStatus(boolean status){ this.success = status;}

    public void RegisterMyReceiver(){

        if(!registered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

            mReceiver = new NetworkReceiver() {
                @Override
                public void showSnack(String message, boolean active) {
                    Log.d("Snack STATUS LISTS ", String.valueOf(getStatus()));
                    snack = Snackbar.make(mDrawerLayout, message, Snackbar.LENGTH_INDEFINITE);
                    snack.setAction("Hide", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snack.dismiss();
                        }
                    }).show();
                    Log.d("Snack", message);
                    // if there is connection and lists are emptys try retrieve it again.
                        if (!getStatus() && active) {
                            Init();
                        }else if(!getStatus() && !active){
                            fragmentcommunicator.passMessageToFragment("No Connection");
                    }
                }
            };
            getApplicationContext().registerReceiver(mReceiver, intentFilter);
            registered = true;
        }
    }




    @Override
    public void passDataToActivity(ArrayList<ListItem> list, int Pos,String someValue) {
        group = ListsManager.replaceList(Category,list,LastPos,Pos,getApplicationContext());
    }


    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }

    private void displayView(int position) {

        mDrawerLayout.closeDrawer(mDrawerList);

        fragment = new Home();

        if(!Category.isEmpty()){

            // add arraylist to fragment
            Bundle bundle = new Bundle();
            ArrayList<ListItem> temp = group.get(position);
            bundle.putParcelableArrayList("PARCELABLE_LIST",temp);
            fragment.setArguments(bundle);
            setTitle(Category.get(position));
        }
        if (fragment != null) {

                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.pop_enter,
                                R.anim.pop_exit, 0, 0)
                        .replace(R.id.frame_container, fragment).commit();
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                mDrawerLayout.closeDrawer(mDrawerList);
                LastPos = position;

        } else{
            Log.e("JSON", "ERROR CREATING FRAGMENT");
        }

        LastPos = position;
    }

    public void FillList(ArrayList<String> lista){
        Log.d("JSON LIST : ", String.valueOf(lista));
        for (int j = 0; j < lista.size(); j++) {
            navDrawerItems.add(new NavDrawerItem(lista.get(j)));
            adapter.notifyDataSetChanged();
        }
    }

/////
    public void AddtoListCategory(String jt){

            if(!existInList(jt)){
                Category.add(jt);
            }
    }

////////
    public boolean existInList(String jt){
        boolean exists = false;
        for (int j = 0; j < Category.size(); j++) {
            if(Category.get(j).equals(jt)){
                exists = true;
            }
        }
        return exists;
    }

    @Override
    public void setTitle(CharSequence title) {
         mTitle.setText(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public String[] convertJsonArray(JSONArray json) throws JSONException {
        String[] stringArray = new String[json.length()];
        for (int i = 0; i < json.length(); i++) {
            stringArray[i]= json.getJSONObject(i).getString("label");
        }
        return stringArray;
    }

    public static ArrayList<ArrayList<ListItem>> CreateArrayLists(ArrayList<String> categories, ArrayList<ListItem> List){
        ArrayList<ArrayList<ListItem>> ArrayLists = new ArrayList<>(categories.size());
        for(int i = 0; i<categories.size(); i++){
            ArrayList<ListItem> tempList = new ArrayList<>();
            for(int j = 0; j<List.size(); j++){
                if(categories.get(i).equals(List.get(j).getCategory())){
                    tempList.add(List.get(j));
                }
            }
            ArrayLists.add(tempList);
        }
        Log.d("LISTAS ", String.valueOf(ArrayLists));
        Log.d("LISTAS recogidas", String.valueOf(List.size()));
        return ArrayLists;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        RegisterMyReceiver();
    }

    public class ParseJSON extends AsyncTask<Void, Void, Void> {
        JSONArray json;
        public ParseJSON(JSONArray Json){this.json = Json;}
        protected Void doInBackground(Void... values) {

            try {
            Log.d("JSON ASYNCTASK", String.valueOf(json));
            //  if (json.length() != 0) {
            JSONObject object = null;
            for (int i = 0; i < json.length(); i++) {


                object = json.getJSONObject(i);
                JSONObject jc = object.getJSONObject("category");
                JSONObject ja = jc.getJSONObject("attributes");
                String jt = ja.getString("term");
                JSONArray imurl = object.getJSONArray("im:image");
                JSONObject jtitle = object.getJSONObject("im:name");
                String title = jtitle.getString("label");
                JSONObject jautor = object.getJSONObject("im:artist");
                String autor = jautor.getString("label");
                JSONObject jdesc = object.getJSONObject("summary");
                String desc = jdesc.getString("label");
                GeneralList.add(new ListItem(preImage, title, jt, autor, convertJsonArray(imurl), desc, 0));
                Log.d("JSON CATEGORY : ", jt);
                if (Category != null) {
                    AddtoListCategory(jt);
                } else {
                    Category.add(jt);
                }
            }
                } catch (JSONException e) {
                    isRunning = false;
                    setStatus(false);
                    e.printStackTrace();
                    Log.e("ERROR PARSING ARRAY", String.valueOf(e));
                }



            return null;
        }
        protected void onPostExecute(Void value) {
            setStatus(true);

        }
    }

    public class LoadListMenu extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... values) {

            // create Arrays
            group = CreateArrayLists(Category, GeneralList);
            CategoryManager.SaveCategoryList(Category, getApplicationContext());
            ListsManager.SaveFullList(getApplicationContext(), group, Category);
            return null;
        }
        protected void onPostExecute(Void value) {
            // add categories to Slide menu
            FillList(Category);
            ArrayList<ListItem> temp = group.get(0);
            fragmentcommunicator.passDataToFragment(temp, "JSON Successfully");
            setTitle(Category.get(0));
            setStatus(true);
            isRunning = true;
            // back in UI thread after task is done
        }
    }

}
