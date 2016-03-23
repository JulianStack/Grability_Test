package onix.mytest.Splash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import onix.mytest.MainActivity;
import onix.mytest.R;
import onix.mytest.SharedPref.ListsManager;

import static android.content.pm.ActivityInfo.*;
import static onix.mytest.SharedPref.ListsManager.*;

/**
 * Created by julian on 19/03/2016
 */
public class MySplash extends Activity {


    static final String TAG = "Splash";

    public static final int segundos =7;
    public static final int milisegundos = segundos*1000;
    public static final int delay=2;
    private ProgressBar pbprogreso;



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        LinearLayout Lsplash = (LinearLayout)findViewById(R.id.LinearSplash);
        pbprogreso = (ProgressBar)findViewById(R.id.progressBar);

        empezaranimacion();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_splash);
        // Scale it to screen size
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, width,height, true));

        Lsplash.setBackground(d);

        SaveTypeDevice(isTabletDevice(getApplicationContext()), getApplicationContext());

        if(isTablet(this)){
            setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
        }

    }


    private void empezaranimacion() {

        new CountDownTimer(milisegundos, 1000){

            @Override
            public void onTick(long millisUntilFinished) {

                pbprogreso.setProgress(progreso(millisUntilFinished));
                pbprogreso.setMax(max_progress());
            }

            @Override
            public void onFinish() {

                    Intent newform = new Intent(MySplash.this, MainActivity.class);
                    startActivity(newform);
                    finish();
            }
        }.start();
    }

    public int max_progress(){

        return segundos-delay;
    }

    public int progreso(long miliseconds){


        return (int) ((milisegundos-miliseconds)/1000);
    }



}
