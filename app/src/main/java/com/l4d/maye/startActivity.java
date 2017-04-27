package com.l4d.maye;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class startActivity extends AppCompatActivity {

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mViewPager = (ViewPager) findViewById(R.id.pager);
/** set the adapter for ViewPager */
        mViewPager.setAdapter(new SamplePagerAdapter(
                getSupportFragmentManager()));
    }

    public void onClick(View v){
        Intent i = new Intent(startActivity.this,MainActivity.class);
        startActivity(i);
    }

    /** Defining a FragmentPagerAdapter class for controlling the fragments to be shown when user swipes on the screen. */
    public class SamplePagerAdapter extends FragmentPagerAdapter {
        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                return new SampleFragment();
            } else if(position == 1)
            {
                return new SampleFragmentTwo();
            } else
            {
                return new SampleFragmentThree();
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }
    }

}