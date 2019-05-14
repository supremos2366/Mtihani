package com.wadektech.mtihanirevise.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import java.util.HashMap;
import java.util.List;

/*import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;*/

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
        Log.d("ChatActivity","ONCREATE");

       // firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsername = findViewById(R.id.tv_username);
        mUsername.setText(Constants.getUserName());
        mHandler = new Handler();

      mTabLayout = findViewById(R.id.main_tabs);
        mViewPager = findViewById(R.id.main_tabPager);
        ChatActivityViewModel viewModel = ViewModelProviders.of(this).get(ChatActivityViewModel.class);
        viewModel.downloadUsers();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getUnreadCountFromRoom();
    }

    private void onEmptyUnreadListReceived(String s) {
        if (s != null) {
        }
    }

    /**
     * List of chats has been received
     * make necessary changes to the titles
     * @param chats
     */
    private void onUnreadListReceived(List<Chat> chats) {
       // ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (chats.size() == 0) {
           // viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        } else {
           //update your code here to reflect changes
            //viewPagerAdapter.addFragment(new ChatsFragment(), "(" + chats.size() + ") Chats");
        }

        //viewPagerAdapter.addFragment(new UsersFragment(), "Classroom");
        //viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");
       // mViewPager.setAdapter(viewPagerAdapter);
       // mTabLayout.setupWithViewPager(mViewPager);
    }

    private void onUserReceived(User user) {
        if (user != null) {
           // mUsername.setText(user.getUsername());
        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
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
        reference = FirebaseDatabase.getInstance().getReference("Users").child(Constants.getUserId ());
        reference.keepSynced (true);

        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("status" , status) ;
        reference.updateChildren(hashMap);
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

