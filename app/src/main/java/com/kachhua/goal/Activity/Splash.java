package com.kachhua.goal.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.kachhua.goal.R;
import com.kachhua.goal.Utility.PrefUtils;
import com.kachhua.goal.database.ConstantValues;
import com.kachhua.goal.database.DataBaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.widget.Toast.*;
import static com.kachhua.goal.Activity.Activity_Main.welcometext;

public class Splash extends AppCompatActivity {


    public static final int RequestPermissionCode = 7;
    Thread background ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
      DataBaseHelper  dataBaseHelper = new DataBaseHelper(getApplicationContext());
      dataBaseHelper.get_tasklist();

        background = new Thread() {

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






        if (Build.VERSION.SDK_INT >= 23)
        {

            if(CheckingPermissionIsEnabledOrNot())
            {

                String dir_path = Environment.getExternalStorageDirectory() +"/"+ ConstantValues.foldername;
                if (!dir_exists(dir_path)){
                    File directory = new File(dir_path);
                    directory.mkdirs();

                }

                background.start();

            } else {


                RequestMultiplePermission();



            }

        }
        else
        {



            String dir_path = Environment.getExternalStorageDirectory() + "/"+ConstantValues.foldername;
            if (!dir_exists(dir_path)){
                File directory = new File(dir_path);
                directory.mkdirs();
            }


            background.start();




        }





    }

    public boolean dir_exists(String dir_path) {
        boolean ret = false;
        File dir = new File(dir_path);
        if(dir.exists() && dir.isDirectory())
            ret = true;
        return ret;
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int permission_camera = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int permission_storage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission_fine_location = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int permission_core_access =ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);

        return permission_camera == PackageManager.PERMISSION_GRANTED
                && permission_storage == PackageManager.PERMISSION_GRANTED
                && permission_core_access== PackageManager.PERMISSION_GRANTED
                && permission_fine_location == PackageManager.PERMISSION_GRANTED;

    }
    private void RequestMultiplePermission() {

        ActivityCompat.requestPermissions(Splash.this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE,ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION}, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storage_permssion = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean fine_core = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean fine_location = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && storage_permssion && fine_core && fine_location ) {

                        background.start();

                    }
                    else {
                        RequestMultiplePermission();
                        makeText(Splash.this,"Permission Denied", LENGTH_LONG).show();

                    }
                }

                break;
        }

    }





}
