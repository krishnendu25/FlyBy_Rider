package com.flyby_riders.Ui.Adapter;

import android.graphics.Color;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.flyby_riders.Ui.Fragment.OnBoard_1;
import com.flyby_riders.Ui.Fragment.OnBoard_2;
import com.flyby_riders.Ui.Fragment.OnBoard_3;
import com.flyby_riders.Ui.Fragment.OnBoard_4;

/**
 * Created by KRISHNENDU MANNA on 24,May,2020
 */

public  class Onbroading_adapter extends FragmentPagerAdapter {
    public Onbroading_adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return OnBoard_1.newInstance();
            case 1:
                return OnBoard_2.newInstance();
            case 2:
                return OnBoard_3.newInstance();
            case 3:
                return OnBoard_4.newInstance();
            default:
                return null;
        }
    }
}
