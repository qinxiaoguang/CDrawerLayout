package com.qxg.testcdrawerlayout.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qxg.cdrawerlayout.CDrawerLayout;
import com.qxg.testcdrawerlayout.R;


public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        CDrawerLayout drawerLayout = (CDrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setHomeLayout(R.layout.homelayout).commit();
    }
}
