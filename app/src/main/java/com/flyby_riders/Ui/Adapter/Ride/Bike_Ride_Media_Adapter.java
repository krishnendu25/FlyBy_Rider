package com.flyby_riders.Ui.Adapter.Ride;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.flyby_riders.Ui.Fragment.All_Media_Ride_Fragment;
import com.flyby_riders.Ui.Fragment.My_Media_Ride_Fragment;

/**
 * Created by KRISHNENDU MANNA on 11,July,2020
 */
public class Bike_Ride_Media_Adapter extends FragmentPagerAdapter {
    private int NUM_ITEMS = 1;
    public Bike_Ride_Media_Adapter(FragmentManager fragmentManager) {
        super(fragmentManager);

    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:// Fragment # 0 - This will show Pending
                return All_Media_Ride_Fragment.newInstance();
          /*  case 1: //  # 1 - This will show Answered
                return My_Media_Ride_Fragment.newInstance();*/
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
            return "ALL";
      /*  } else  {
            return "My Uploads";
        }*/

    }
}