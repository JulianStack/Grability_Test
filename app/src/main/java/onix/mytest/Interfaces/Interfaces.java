package onix.mytest.Interfaces;

import java.util.ArrayList;

import onix.mytest.Adapters.ListItem;

/**
 * Created by JulianStack on 20/03/2016.
 */
public class Interfaces {

    // communicate Activity with fragment
    public interface FragmentCommunicator{
        public void passDataToFragment(ArrayList<ListItem> list,String someValue);
        public void passMessageToFragment(String someValue);
    }

    // communicate Fragment with activity
    public interface ActivityCommunicator{
        public void passDataToActivity(ArrayList<ListItem> list,int Pos,String someValue);
    }

    // communicate Fragment with activity
    public interface ActivitySnacksbars{
        public void passStringToActivity(String someValue);
    }

    // communicate Fragment with activity
    public interface FragmentSnacksbars{
        public void passStringTofragment(String someValue);
    }
}
