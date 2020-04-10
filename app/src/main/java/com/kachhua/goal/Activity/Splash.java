package com.kachhua.goal.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kachhua.goal.R;
import com.kachhua.goal.Utility.PrefUtils;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread background = new Thread() {

            public void run() {
                try {

                    sleep(3 * 1000);




                    if (PrefUtils.getCurrentUser(Splash.this) != null)
                    {
                        Intent intent = new Intent(Splash.this, Activity_Main.class);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {
                        Intent intent = new Intent(Splash.this, Activity_Main.class);
                        startActivity(intent);
                        finish();
                    }



                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }

}
