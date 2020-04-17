package com.kachhua.goal.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kachhua.goal.Fragments.Fragment_Dashboard;
import com.kachhua.goal.Fragments.Fragment_ForceReport;
import com.kachhua.goal.Fragments.Fragment_GoalList;
import com.kachhua.goal.Fragments.Fragment_Reported_Excelpanel;
import com.kachhua.goal.Fragments.Fragment_Reported_List;
import com.kachhua.goal.R;
import com.kachhua.goal.database.ConstantValues;
import com.kachhua.goal.database.DataBaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class Activity_Main extends AppCompatActivity implements View.OnClickListener {

    public static DrawerLayout drawer;
    NavigationView navigationView_left;
    public static ImageView togglebutton,img_slider;
    public static TextView welcometext;
    TextView txt_dashboard,txt_my_goals,txt_top3_goals,txt_top10_goals;
    TextView txt_business_goal,txt_office_goals, txt_relationship_goals,txt_general_goals, txt_financial_goals,txt_spiritual_goal,txt_lifestyle_goal,txt_mental_goal,txt_family_goal,txt_physical_goal,txt_reporeted_list;
    LinearLayout lnr_force_report;
    DataBaseHelper dataBaseHelper;
    SharedPreferences settings1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__main);
        findids();
        actions();
        changefragment(0);

         settings1 = PreferenceManager.getDefaultSharedPreferences(Activity_Main.this);
        int abc = settings1.getInt("db_installed", -1);
        if (abc!=1) {


            SharedPreferences.Editor editor1 = settings1.edit();
            editor1.putInt("db_installed", 1);
            editor1.commit();


            Import_Db();



        }else{
           // Import_Db();

            export_Db();
        }
    }

    void findids(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView_left = (NavigationView) findViewById(R.id.nav_view);

        txt_dashboard =navigationView_left.findViewById(R.id.txt_dashboard);
        txt_my_goals=navigationView_left.findViewById(R.id.txt_my_goals);
        txt_top3_goals=navigationView_left.findViewById(R.id.txt_top_3_goals);
        txt_top10_goals=navigationView_left.findViewById(R.id.txt_top_10_goals);

        txt_relationship_goals =navigationView_left.findViewById(R.id.txt_relationship);
        txt_lifestyle_goal=navigationView_left.findViewById(R.id.txt_lifestyle);
        txt_spiritual_goal=navigationView_left.findViewById(R.id.txt_spiritual);
        txt_family_goal=navigationView_left.findViewById(R.id.txt_family);
        txt_mental_goal=navigationView_left.findViewById(R.id.txt_mental);
        txt_physical_goal=navigationView_left.findViewById(R.id.txt_physical);
        txt_financial_goals =navigationView_left.findViewById(R.id.txt_financial);
        txt_business_goal=navigationView_left.findViewById(R.id.txt_business);
        txt_general_goals=navigationView_left.findViewById(R.id.txt_general);
        txt_office_goals=navigationView_left.findViewById(R.id.txt_office);



        lnr_force_report=navigationView_left.findViewById(R.id.lnr_force_report);
        txt_reporeted_list=navigationView_left.findViewById(R.id.txt_report);

        togglebutton =findViewById(R.id.togglemenu);
        welcometext =findViewById(R.id.txt_welcome_line);
    }
    void actions(){
        togglebutton.setOnClickListener(this);
        txt_my_goals.setOnClickListener(this);
        txt_top3_goals.setOnClickListener(this);
        txt_top10_goals.setOnClickListener(this);


        txt_relationship_goals.setOnClickListener(this);
        txt_lifestyle_goal.setOnClickListener(this);
        txt_spiritual_goal.setOnClickListener(this);
        txt_family_goal.setOnClickListener(this);
        txt_mental_goal.setOnClickListener(this);
        txt_physical_goal.setOnClickListener(this);
        txt_financial_goals.setOnClickListener(this);
        txt_business_goal.setOnClickListener(this);
        txt_general_goals.setOnClickListener(this);
        txt_office_goals.setOnClickListener(this);


        lnr_force_report.setOnClickListener(this);
        txt_reporeted_list.setOnClickListener(this);
        txt_dashboard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.togglemenu:
                drawer.openDrawer(GravityCompat.START);
                break;


            case R.id.txt_dashboard:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(0);
                break;
            case R.id.txt_my_goals:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(1);
                break;
            case R.id.txt_top_3_goals:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(2);
                break;
            case R.id.txt_top_10_goals:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(3);
                break;
            case R.id.txt_relationship:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(4);
                break;

            case R.id.txt_lifestyle:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(5);
                break;
            case R.id.txt_spiritual:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(6);
                break;
            case R.id.txt_family:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(7);
                break;
            case R.id.txt_mental:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(8);
                break;

            case R.id.txt_physical:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(9);
                break;

            case R.id.txt_financial:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(10);
                break;

            case R.id.txt_business:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(11);
                break;

            case R.id.txt_general:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(12);
                break;

            case R.id.txt_office:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(13);
                break;

            case R.id.lnr_force_report:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(14);
                break;
            case R.id.txt_report:
                drawer.closeDrawer(GravityCompat.START);
                changefragment(15);
                break;

        }
    }

    void changefragment(int i){

        Fragment fragment =null;

        if(i==0){
            fragment = new Fragment_Dashboard();
        }else if(i==1) {
            fragment= Fragment_GoalList.newInstance("my");
        }else if(i==2){
            fragment= Fragment_GoalList.newInstance("top3");
        }else if(i==3){
            fragment =Fragment_GoalList.newInstance("top10");
        }else if(i==4){
            fragment =Fragment_GoalList.newInstance(ConstantValues.Category_RelationShip);
        }else if(i==5){
            fragment =Fragment_GoalList.newInstance(ConstantValues.Category_LifeStyle);
        }else if(i==6){
            fragment =Fragment_GoalList.newInstance(ConstantValues.Category_Spiritual);
        }else if(i==7){
            fragment =Fragment_GoalList.newInstance(ConstantValues.Category_Family);
        }else if(i==8){
            fragment =Fragment_GoalList.newInstance(ConstantValues.Category_Mental);
        }
        else if(i==9){
            fragment =Fragment_GoalList.newInstance(ConstantValues.Category_Physical);
        }else if(i==10){
            fragment =Fragment_GoalList.newInstance(ConstantValues.Category_Financial);
        }else if(i==11){
            fragment =Fragment_GoalList.newInstance(ConstantValues.Category_bunishess);
        }else if(i==12){
            fragment =Fragment_GoalList.newInstance(ConstantValues.Category_general);
        }else if(i==13){
            fragment =Fragment_GoalList.newInstance(ConstantValues.Category_office);
        }else if(i==14){
            fragment= new Fragment_ForceReport();
        }else if(i==15){
            fragment= new Fragment_Reported_Excelpanel();
        }







        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();






    }


    public void Import_Db() {

        try {
            String inputString = Environment.getExternalStorageDirectory() + "/"+ConstantValues.foldername+"/"+ConstantValues.DB_Name;
            File file_input=new File(inputString);

            InputStream myInput = null;
            OutputStream outStream;

            myInput = new FileInputStream(file_input);


            String DB_PATH,dumypath;
            String DB_NAME=ConstantValues.DB_Name;

            if (android.os.Build.VERSION.SDK_INT >= 17) {
               // DB_PATH = getApplicationContext().getApplicationInfo().dataDir + "/databases/";
                DB_PATH = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
                dumypath="/data/data/com.kachhua.goal/databases/ERP_GOAL";


            }
            else {
                DB_PATH = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
            }



            String file = DB_PATH + DB_NAME;
            outStream = new FileOutputStream(file);

            File dbshm = new File(file + "-shm");
            File dbwal = new File(file + "-wal");
            if (dbshm.exists()) {
                dbshm.delete();
            }
            if (dbwal.exists()) {
                dbwal.delete();
            }

            byte[] buffer = new byte[1024];
            int length = 0;
          /*  while ((length = myInput.read(buffer)) >= 0) {
                outStream.write(buffer, 0, length);
            }*/

            while ((length = myInput.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }

            outStream.flush();
            myInput.close();
            outStream.close();



             finish();
             Intent myint = new Intent(Activity_Main.this,Activity_Main.class);
             startActivity(myint);
            Toast.makeText(getApplicationContext(),"Db Imported",Toast.LENGTH_SHORT).show();




        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }

    }

    void export_Db(){
        try {



            String dir_path = Environment.getExternalStorageDirectory() + "/"+ConstantValues.foldername+"/"+ConstantValues.DB_Name;
            File sltl_Db_file = new File(dir_path);
            if(sltl_Db_file.exists())
                sltl_Db_file.delete();



            // Open your local db as the input stream
            InputStream myInput =null;//= getApplicationContext().getAssets().open(DataBaseHelper.DATABASE_NAME+".db");

            // Path to the just created empty db
            String outFileName = "/data/data/com.kachhua.goal/databases/"+ConstantValues.DB_Name;

            File file = new File(outFileName);
            myInput = new FileInputStream(file);

            String filename = Environment.getExternalStorageDirectory()+"/"+ConstantValues.foldername+"/"+ConstantValues.DB_Name;
            OutputStream myOutput = new FileOutputStream(filename);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }


            myOutput.flush();
            myOutput.close();
            myInput.close();

            Toast.makeText(getApplicationContext(),"Db Exported",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Log.e("error", e.toString());
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }



}
