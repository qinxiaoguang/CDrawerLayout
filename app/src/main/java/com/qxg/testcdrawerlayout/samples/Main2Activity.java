package com.qxg.testcdrawerlayout.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qxg.cdrawerlayout.CDrawerLayout;
import com.qxg.testcdrawerlayout.R;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        CDrawerLayout drawerLayout = (CDrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setCustomNavLayout(R.layout.tmp)
                .setHomeLayout(R.layout.homelayout)
                .commit();
    }
}
