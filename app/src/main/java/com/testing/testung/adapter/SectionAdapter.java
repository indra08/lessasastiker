package com.testing.testung.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.util.Log;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.testing.testung.fragment.MakeStickerSelf;
import com.testing.testung.fragment.StoreFragment;

public class SectionAdapter extends FragmentPagerAdapter {
    BillingProcessor inve;
    public SectionAdapter(FragmentManager fm,BillingProcessor inventory) {
        super(fm);
        this.inve=inventory;
    }

    @Override
    public Fragment getItem(int i) {
        Log.d("DSIU", "getItem: ");
        if(i==0){
            return StoreFragment.newInisiate(i,inve);
        }else if(i==1){
            return MakeStickerSelf.newInisiate(i,inve);
        }else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
