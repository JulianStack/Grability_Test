package onix.mytest.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * Created by JulianStack on 20/03/2016.
 */
public class ListItem implements Serializable, Parcelable {

    String Title;
    String Category;
    String Autor;
    String[] Url;
    String Desc;
    byte[] Image;
    int Downloaded;

    public ListItem(Bitmap image, String title, String category, String autor, String[] url, String desc, int downloaded){
        super();
        this.Autor = autor;
        this.Category = category;
        this.Title = title;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.Image = stream.toByteArray();
        this.Url = url;
        this.Desc = desc;
        this.Downloaded = downloaded;
    }


    public void setData(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.Image = stream.toByteArray();
    }

    public Bitmap getData(){
        Bitmap bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.length);
        return bitmap;
    }

    public int getDownloaded(){return  Downloaded;}
    public void setDownloaded(int downloaded){Downloaded = downloaded;}

    public String getDesc(){return Desc;}
    public void setDesc(String desc) {Desc = desc;}

    public String[] getUrl(){return Url;}
    public void setUrl(String[] url){this.Url = url;}

    public byte[] getImage(){ return Image; }
    public void setImagen(Bitmap image){
       // Bitmap bitmap = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.Image = stream.toByteArray();
    }

    public String getTitle(){return Title;}
    public void setTitle(String title){this.Title = title;}

    public String getCategory(){return Category;}
    public void setCategory(String category){this.Category = category;}

    public String getAutor(){return Autor;}
    public void setAutor(String autor){this.Autor = autor;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Title);
        dest.writeString(this.Category);
        dest.writeString(this.Autor);
        dest.writeStringArray(this.Url);
        dest.writeString(this.Desc);
        dest.writeByteArray(this.Image);
        dest.writeInt(this.Downloaded);
    }

    protected ListItem(Parcel in) {
        this.Title = in.readString();
        this.Category = in.readString();
        this.Autor = in.readString();
        this.Url = in.createStringArray();
        this.Desc = in.readString();
        this.Image = in.createByteArray();
        this.Downloaded = in.readInt();
    }

    public static final Parcelable.Creator<ListItem> CREATOR = new Parcelable.Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel source) {
            return new ListItem(source);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };
}
