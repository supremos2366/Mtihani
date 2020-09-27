package com.wadektech.mtihanirevise.ui;

import androidx.lifecycle.ViewModelProviders;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wadektech.mtihanirevise.R;
import com.wadektech.mtihanirevise.database.MtihaniDatabase;
import com.wadektech.mtihanirevise.fragments.ChatsFragment;
import com.wadektech.mtihanirevise.fragments.ProfileFragment;
import com.wadektech.mtihanirevise.fragments.UsersFragment;
import com.wadektech.mtihanirevise.room.Chat;
import com.wadektech.mtihanirevise.room.User;
import com.wadektech.mtihanirevise.utils.Constants;
import com.wadektech.mtihanirevise.viewmodels.ChatActivityViewModel;
import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {
    TextView mUsername, mStatus;
   // FirebaseUser firebaseUser;
    DatabaseReference reference;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mUsername = findViewById(R.id.tv_username);
        mUsername.setText(Constants.getUserName());
        mHandler = new Handler();

      mTabLayout = findViewById(R.id.main_tabs);
        mViewPager = findViewById(R.id.main_tabPager);
        mTabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
        ChatActivityViewModel viewModel = ViewModelProviders.of(this).get(ChatActivityViewModel.class);
        viewModel.downloadUsers();

    }

    @Override
    protected void onStart() {
        super.onStart();
        status("online");
        getUnreadCountFromRoom();
    }

    /**
     * List of chats has been received
     * make necessary changes to the titles
     * @param
     */

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status) {
        FirebaseFirestore
                .getInstance()
                .collection("Users")
                .document(Constants.getUserId())
                .update("status" , status) ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    protected void onStop() {
        super.onStop();
        status("offline");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        status("offline");
    }

    private void getUnreadCountFromRoom() {
//      New thread to perform background operation
        new Thread(() -> {

           final int  unreadCount = MtihaniDatabase
                   .getInstance(ChatActivity.this)
                   .singleMessageDao()
                   .getUnreadCount(Constants.getUserId(),false);


//                  Update the value background thread to UI thread
                mHandler.post(() -> {

                    if(unreadCount==0){
                        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
                        viewPagerAdapter.addFragment(new UsersFragment (), "Classroom");
                        viewPagerAdapter.addFragment(new ProfileFragment (), "Profile");
                        mViewPager.setAdapter(viewPagerAdapter);
                        mTabLayout.setupWithViewPager(mViewPager);
                    }else{
                        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                        viewPagerAdapter.addFragment(new ChatsFragment(), "("+unreadCount+") Chats");
                        viewPagerAdapter.addFragment(new UsersFragment (), "Classroom");
                        viewPagerAdapter.addFragment(new ProfileFragment (), "Profile");
                        mViewPager.setAdapter(viewPagerAdapter);
                        mTabLayout.setupWithViewPager(mViewPager);
                    }

                });

        }).start();
    }

}

