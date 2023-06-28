package logic;
/*
* fragment for list adapter
* */
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.saveethaevents.all_events;
import com.example.saveethaevents.dashboard;
public class adapter_fragment extends FragmentPagerAdapter{

    public adapter_fragment(@NonNull FragmentManager fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = new dashboard();
        else if (position == 1)
            fragment = new all_events();
        return fragment;
    }

    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position)
    {
        String title = null;
        if (position == 0)
            title = "Dashboard";
        else if (position == 1)
            title = "All Events";
        return title;
    }
}
