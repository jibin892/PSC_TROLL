package techsayas.in.psctrolls.psctroll;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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

        checkInternet();

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
//
//        if(NetworkUtils.isNetworkConnected(this)){
//          //  Crouton.makeText(Homepage.this,"Conected", Style.CONFIRM).show();
//
//        }
//        else{
//
//            Crouton.makeText(Homepage.this,"No Internet", Style.ALERT).show();
//
//
//        }


    }

    private void checkInternet() {




    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

}
