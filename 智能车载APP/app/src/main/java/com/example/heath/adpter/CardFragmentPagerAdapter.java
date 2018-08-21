package com.example.heath.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;


import com.example.heath.Main_Fragment.CardFragment;
import com.example.heath.Main_Fragment.CardFragment2;
import com.example.heath.Main_Fragment.CardFragment3;
import com.example.heath.Main_Fragment.CardFragment4;
import com.example.heath.Main_Fragment.CardFragment5;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    private List<Fragment> mFragments;
    private float mBaseElevation;

    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
        super(fm);
        mFragments = new ArrayList<>();
        mBaseElevation = baseElevation;
        addCardFragment(new CardFragment());
        addCardFragment(new CardFragment2());
        addCardFragment(new CardFragment3());
        addCardFragment(new CardFragment4());
        addCardFragment(new CardFragment5());
    }


    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    public void addCardFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

}
