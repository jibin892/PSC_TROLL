package techsayas.in.psctrolls.psctroll;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Homepage extends AppCompatActivity {
    boolean b;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // super.onCreate();
        Configuration config = getResources().getConfiguration();
        if (((Configuration) config).smallestScreenWidthDp >= 600)
        {
            setContentView(R.layout.activity_homepage);
        }
        else
        {
            setContentView(R.layout.activity_homepage);
        }



        // setContentView(R.layout.activity_homepage);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        Animation center_reveal_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_down);
        navView.startAnimation(center_reveal_anim);
//         Passing each menu ID as a set of Ids because each
//         menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,R.id.navigation_notifications, R.id.navigation_profile, R.id.navigation_message,R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);


        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);




        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            changeTextStatus(true);
        } else {
            changeTextStatus(false);
        }
    }

    public void changeTextStatus(boolean isConnected) {

        // Change status according to boolean value
        if (isConnected) {
            Crouton.makeText(Homepage.this,"Connected", Style.CONFIRM).show();
        } else {
            Crouton.makeText(Homepage.this," NOT Connected", Style.ALERT).show();
        }
    }
    @Override
    protected void onPause() {

        super.onPause();
        MyApplication.activityPaused();// On Pause notify the Application
    }

    @Override
    protected void onResume() {

        super.onResume();
        MyApplication.activityResumed();// On Resume notify the Application
    }
    @Override
    protected void onRestart() {

        super.onRestart();
        MyApplication.activityRestart();// On Resume notify the Application
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;

            }
        }, 2000);
    }

}
