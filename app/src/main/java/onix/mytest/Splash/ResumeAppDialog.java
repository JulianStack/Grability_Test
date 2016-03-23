package onix.mytest.Splash;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import onix.mytest.Adapters.ListItem;
import onix.mytest.R;

/**
 * Created by JulianStack on 22/03/2016.
 */
public class ResumeAppDialog extends Activity {


    Bundle bundle;
    ImageView image;
    TextView title;
    TextView category;
    TextView autor;
    TextView desc;

    ImageView close;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_resume_detail);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        int width = getWindow().getAttributes().width/5;
        params.width = getWindow().getAttributes().width-width;

        this.getWindow().setAttributes(params);

        image = (ImageView)findViewById(R.id.Preview);
        title = (TextView)findViewById(R.id.Titulo);
        category = (TextView)findViewById(R.id.Categoria);
        autor = (TextView)findViewById(R.id.Autor);
        desc = (TextView)findViewById(R.id.Desc);
        close = (ImageView)findViewById(R.id.close_button);

        close.setImageResource(R.drawable.close_icon);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bundle = getIntent().getExtras();

        if(bundle!=null){

            ListItem item = bundle.getParcelable("PARCELABLE_ITEM");
            Bitmap bitmap = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
            image.setImageBitmap(bitmap);

            title.setText(item.getTitle());
            category.setText(item.getCategory());
            autor.setText(item.getAutor());
            desc.setText(item.getDesc());

        }

        /*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image.setTransitionName("preview");
        }
*/

    }

    @Override
    protected void onPause() {

        super.onPause();
    }


}
