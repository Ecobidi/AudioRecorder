package com.example.obidi.audiorecorder;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RecordFragment.Callbacks {
    public static final String TAG = MainActivity.class.getSimpleName();

    private RecordingListFragment mRecordingListFragment;

    @Override
    public void onRecordingSaved() {
        // when a new recording is saved in the RecordFragment, it calls this method in the Hosting
        // Activity which notifies the soundListRecyclerView adapter of a data change
        // thereby updating the list
//        RecyclerView soundListRecyclerView = findViewById(R.id.recycler_view);
//        soundListRecyclerView.getAdapter().notifyDataSetChanged();
        if (mRecordingListFragment != null) {
            mRecordingListFragment.reloadList();
            Log.v(TAG, "Reload list called");
            Toast.makeText(this, "List Reloaded", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "List could not be reloaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0: return new RecordFragment();
                    case 1: {
                        mRecordingListFragment = new RecordingListFragment();
                        return mRecordingListFragment;
                    }
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0: return getString(R.string.home_tab_label);
                    case 1: return getString(R.string.sound_list_tab_label);
                    default: return null;
                }
            }
        });

        // this sets the RecordFragment as the default Fragment to show
        viewPager.setCurrentItem(0);

        // reference the tabBar and setup with viewPager
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.sound_item_context_menu, menu);
    }

}
