package com.qxg.testcdrawerlayout.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qxg.cdrawerlayout.CDrawerLayout;
import com.qxg.testcdrawerlayout.R;

public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        CDrawerLayout drawerLayout = (CDrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(drawerLayout.getAppcompatToolbar());
        drawerLayout.setToolbarColor(R.color.colorAccent)
                .setStatusBarColor(R.color.white)
                .setHomeLayout(R.layout.homelayout)
                .commit();
    }
}
