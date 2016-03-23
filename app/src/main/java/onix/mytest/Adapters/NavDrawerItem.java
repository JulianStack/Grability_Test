package onix.mytest.Adapters;

/**
 * Created by julian on 19/03/2016.
 */
public class NavDrawerItem {

    private String title;
    private int icon;


    public NavDrawerItem(String title){
        this.title = title;
        this.icon = icon;
    }


    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

}
