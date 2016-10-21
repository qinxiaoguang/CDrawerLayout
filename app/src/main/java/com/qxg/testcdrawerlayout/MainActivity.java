package com.qxg.testcdrawerlayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qxg.testcdrawerlayout.samples.Main1Activity;
import com.qxg.testcdrawerlayout.samples.Main2Activity;
import com.qxg.testcdrawerlayout.samples.Main3Activity;
import com.qxg.testcdrawerlayout.samples.Main4Activity;
import com.qxg.testcdrawerlayout.samples.Main5Activity;
import com.qxg.testcdrawerlayout.samples.Main6Activity;
import com.qxg.testcdrawerlayout.samples.Main7Activity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.id1).setOnClickListener(this);
        findViewById(R.id.id2).setOnClickListener(this);
        findViewById(R.id.id3).setOnClickListener(this);
        findViewById(R.id.id4).setOnClickListener(this);
        findViewById(R.id.id5).setOnClickListener(this);
        findViewById(R.id.id6).setOnClickListener(this);
        findViewById(R.id.id7).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id1:
                startActivity(new Intent(MainActivity.this, Main1Activity.class));
                break;
            case R.id.id2:
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                break;
            case R.id.id3:
                startActivity(new Intent(MainActivity.this, Main3Activity.class));
                break;
            case R.id.id4:
                startActivity(new Intent(MainActivity.this, Main4Activity.class));
                break;
            case R.id.id5:
                startActivity(new Intent(MainActivity.this, Main5Activity.class));
                break;
            case R.id.id6:
                startActivity(new Intent(MainActivity.this, Main6Activity.class));
                break;
            case R.id.id7:
                startActivity(new Intent(MainActivity.this, Main7Activity.class));
            default:break;
        }
    }
}
