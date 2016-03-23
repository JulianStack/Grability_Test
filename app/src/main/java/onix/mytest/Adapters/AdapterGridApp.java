package onix.mytest.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import onix.mytest.R;

/**
 * Created by JulianStack on 20/03/2016.
 */
public class AdapterGridApp extends BaseAdapter {


    protected Activity activity;
    private ArrayList<ListItem> AdapterList;

    public AdapterGridApp(Activity activity, ArrayList<ListItem> AdapterList) {
        this.activity = activity;
        this.AdapterList = AdapterList;
    }

    @Override
    public int getCount() {
        return AdapterList.size();
    }

    @Override
    public Object getItem(int position) {
        return AdapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.grid_template, null);
        }

        ListItem itm = AdapterList.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.titulo);
        title.setText(itm.getTitle());

        TextView category = (TextView) convertView.findViewById(R.id.categoria);
        category.setText(itm.getCategory());

        TextView autor = (TextView) convertView.findViewById(R.id.autor);
        autor.setText(itm.getAutor());

        ImageView image = (ImageView) convertView.findViewById(R.id.Preview);
        Bitmap bitmap = BitmapFactory.decodeByteArray(itm.getImage(), 0, itm.getImage().length);
        image.setImageBitmap(bitmap);

        return convertView;
    }





}
