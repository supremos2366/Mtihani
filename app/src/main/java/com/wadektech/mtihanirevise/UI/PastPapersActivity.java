package com.wadektech.mtihanirevise.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.wadektech.mtihanirevise.Adapter.RecyclerViewAdapter;
import com.wadektech.mtihanirevise.Auth.LoginActivity;
import com.wadektech.mtihanirevise.POJO.RowModel;
import com.wadektech.mtihanirevise.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hotchemi.android.rate.AppRate;

public class PastPapersActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private GridLayoutManager lLayout;
    FirebaseAuth mAuth ;
    CircleImageView userProfile ;
    GoogleApiClient mGoogleApiClient ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_papers);
        setTitle(null);
        Toolbar topToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In Api and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mAuth = FirebaseAuth.getInstance() ;
        userProfile = findViewById(R.id.profileImage);

        FirebaseUser user = mAuth.getCurrentUser() ;

        if (user != null) {
            Picasso.with(this)
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.profile)
                    .into(userProfile);
        }

       mAuth = FirebaseAuth.getInstance() ;
        List<RowModel> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(PastPapersActivity.this, 2);

        RecyclerView rView = findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(PastPapersActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuAbout) {
            //open the developer profile
            startActivity(new Intent(getApplicationContext(), DeveloperProfile.class));
            return true;
        }
        if (id == R.id.rate_app) {
            //we will call our rateApp method here
            rateApp();
        }
        if (id == R.id.menuLogout) {
            //we will call our signOut method here
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private List<RowModel> getAllItemList(){

        List<RowModel> allItems = new ArrayList<>();
        allItems.add(new RowModel("KCSE 1996 - 2009", R.drawable.pdf));
        allItems.add(new RowModel("KCSE 2006", R.drawable.pdf));
        allItems.add(new RowModel("KCSE 2007", R.drawable.pdf));
        allItems.add(new RowModel("KCSE 2008", R.drawable.pdf));
        allItems.add(new RowModel("KCSE 2009", R.drawable.pdf));
        allItems.add(new RowModel("KCSE 2010", R.drawable.pdf));
        allItems.add(new RowModel("KCSE 2011", R.drawable.pdf));
        allItems.add(new RowModel("KCSE 2012", R.drawable.pdf));
        allItems.add(new RowModel("KCSE 2013", R.drawable.pdf));
        allItems.add(new RowModel("KCSE 2014", R.drawable.pdf));
        allItems.add(new RowModel("KCSE 2015", R.drawable.pdf));
        allItems.add(new RowModel("KCSE 2016", R.drawable.pdf));
        allItems.add(new RowModel("KCSE 2017", R.drawable.pdf));
        allItems.add(new RowModel("SBC MOCK PAPERS", R.drawable.pdf));

        return allItems;
    }
    //method to logout
    private void signOut(){
        //signOut user from firebase database
        mAuth.signOut();
        //clear user account
        //send intent to the Login activity
        Auth.GoogleSignInApi.signOut(mGoogleApiClient) ;
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    protected void onStart(){
        super.onStart();
        //if user is not signed in
        //open login activity
        if (mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }
    //Implement the rate app functionality from the rateApp library
    private void rateApp(){
        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);
        AppRate.with(this).showRateDialog(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
