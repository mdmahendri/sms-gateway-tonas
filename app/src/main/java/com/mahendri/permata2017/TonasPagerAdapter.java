package com.mahendri.permata2017;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by mahendri on 12/31/2016.
 * like i said, this is for design
 */

class TonasPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;

    TonasPagerAdapter (FragmentManager fm, Context context){
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DaftarFragment();
            case 1:
                return new SmsFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.regist_tab);
            case 1:
                return mContext.getString(R.string.sms_tab);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
