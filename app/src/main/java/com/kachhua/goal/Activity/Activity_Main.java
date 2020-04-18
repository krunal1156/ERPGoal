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
import java.nio.file.NoSuchFileException;
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
    String DB_PATH,file;
    String DB_NAME=ConstantValues.DB_Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__main);
        findids();
        actions();

        settings1 = PreferenceManager.getDefaultSharedPreferences(Activity_Main.this);
        int abc = settings1.getInt("db_installed", -1);
        if (abc!=1) {

            SharedPreferences.Editor editor1 = settings1.edit();
            editor1.putInt("db_installed", 1);
            editor1.commit();





            Import_Db();



        }else{

            delete_files();
            export_Db();

        }

        changefragment(0);

    }

    void delete_files(){
        String db_file_path = Environment.getExternalStorageDirectory() + "/"+ConstantValues.foldername+"/"+ConstantValues.DB_Name;
        File Db_file = new File(db_file_path);
        if(Db_file.exists())
            Db_file.delete();



        String db_shm_file_path = Environment.getExternalStorageDirectory() + "/"+ConstantValues.foldername+"/"+ConstantValues.DB_Name;
        File Db_shm_file = new File(db_shm_file_path);
        if(Db_shm_file.exists())
            Db_shm_file.delete();



        String db_wal_file_path = Environment.getExternalStorageDirectory() + "/"+ConstantValues.foldername+"/"+ConstantValues.DB_Name;
        File Db_wal_file = new File(db_wal_file_path);
        if(Db_wal_file.exists())
            Db_wal_file.delete();


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
           String db_file_path = Environment.getExternalStorageDirectory() + "/"+ConstantValues.foldername+"/"+ConstantValues.DB_Name;
            File db_file=new File(db_file_path);

            InputStream db_inputStream = null;
            OutputStream db_outputStream =null;


            if (android.os.Build.VERSION.SDK_INT >= 17) {
                //DB_PATH = getApplicationContext().getApplicationInfo().dataDir + "/databases/";
                DB_PATH = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
            }
            else {
                DB_PATH = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
            }
            file = DB_PATH + DB_NAME;


            db_inputStream = new FileInputStream(db_file);
            db_outputStream = new FileOutputStream(file);


            byte[] db_buffer = new byte[1024];
            int db_length = 0;
            while ((db_length = db_inputStream.read(db_buffer)) > 0) {
                db_outputStream.write(db_buffer, 0, db_length);
            }

            db_outputStream.flush();
            db_inputStream.close();
            db_outputStream.close();

///////////////////////////////////////////////////////

            String db_shm_file_path = Environment.getExternalStorageDirectory() + "/"+ConstantValues.foldername+"/"+ConstantValues.DB_Name+"-shm";
            File db_shm_file=new File(db_shm_file_path);

            InputStream db_shm_inputStream = null;
            OutputStream db_shm_outputStream =null;


            if (android.os.Build.VERSION.SDK_INT >= 17) {
                //DB_PATH = getApplicationContext().getApplicationInfo().dataDir + "/databases/";
                DB_PATH = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
            }
            else {
                DB_PATH = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
            }
            file = DB_PATH + DB_NAME+"-shm";


            db_shm_inputStream = new FileInputStream(db_shm_file);
            db_shm_outputStream = new FileOutputStream(file);

            File dbshm = new File(file + "-shm");
            if (dbshm.exists()) {
                dbshm.delete();
            }

            byte[] db_shm_buffer = new byte[1024];
            int db_shm_length = 0;
            while ((db_shm_length = db_shm_inputStream.read(db_shm_buffer)) > 0) {
                db_shm_outputStream.write(db_shm_buffer, 0, db_shm_length);
            }

            db_shm_outputStream.flush();
            db_shm_inputStream.close();
            db_shm_outputStream.close();
            /////////////////////////////////////////////

            String db_wal_file_path = Environment.getExternalStorageDirectory() + "/"+ConstantValues.foldername+"/"+ConstantValues.DB_Name+"-wal";
            File db_wal_file=new File(db_wal_file_path);

            InputStream db_wal_inputStream = null;
            OutputStream db_wal_outputStream =null;


            if (android.os.Build.VERSION.SDK_INT >= 17) {
                //DB_PATH = getApplicationContext().getApplicationInfo().dataDir + "/databases/";
                DB_PATH = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
            }
            else {
                DB_PATH = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
            }
            file = DB_PATH + DB_NAME+"-wal";


            db_wal_inputStream = new FileInputStream(db_wal_file);
            db_wal_outputStream = new FileOutputStream(file);

             File dbwal = new File(file + "-wal");

             if (dbwal.exists()) {
                 dbwal.delete();
             }

            byte[] db_wal_buffer = new byte[1024];
            int db_wal_length = 0;
            while ((db_wal_length = db_wal_inputStream.read(db_wal_buffer)) > 0) {
                db_wal_outputStream.write(db_wal_buffer, 0, db_wal_length);
            }

            db_wal_outputStream.flush();
            db_wal_inputStream.close();
            db_wal_outputStream.close();











            finish();
             Intent myint = new Intent(Activity_Main.this,Activity_Main.class);
             startActivity(myint);
            Toast.makeText(getApplicationContext(),"Db Imported",Toast.LENGTH_SHORT).show();

            changefragment(0);



        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"IMPORT : "+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            changefragment(0);


        }

    }

    void export_Db(){
        try {

            String inputfile_path_db = "/data/data/com.kachhua.goal/databases/"+ConstantValues.DB_Name;
            File input_file_db = new File(inputfile_path_db);
            InputStream Inputstream_dbfile = new FileInputStream(input_file_db);

            String output_file_path = Environment.getExternalStorageDirectory()+"/"+ConstantValues.foldername+"/"+ConstantValues.DB_Name;
            OutputStream myOutput = new FileOutputStream(output_file_path);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = Inputstream_dbfile.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }


            myOutput.flush();
            myOutput.close();
            Inputstream_dbfile.close();
           //////////////////////////////

            String inputfile_path_db_shm = "/data/data/com.kachhua.goal/databases/"+ConstantValues.DB_Name+"-shm";
            File input_file_db_shm = new File(inputfile_path_db_shm);
            InputStream Inputstream_dbfile_shm = new FileInputStream(input_file_db_shm);

            String output_file_path_shm = Environment.getExternalStorageDirectory()+"/"+ConstantValues.foldername+"/"+ConstantValues.DB_Name+"-shm";
            OutputStream myOutput_shm = new FileOutputStream(output_file_path_shm);
            byte[] buffer_shm = new byte[1024];
            int length_shm;
            while ((length_shm = Inputstream_dbfile_shm.read(buffer_shm)) > 0)
            {
                myOutput_shm.write(buffer_shm, 0, length_shm);
            }


            myOutput_shm.flush();
            myOutput_shm.close();
            Inputstream_dbfile_shm.close();

            /////////////////////////////
            String inputfile_path_db_wal = "/data/data/com.kachhua.goal/databases/"+ConstantValues.DB_Name+"-wal";
            File input_file_db_wal = new File(inputfile_path_db_wal);
            InputStream Inputstream_dbfile_wal = new FileInputStream(input_file_db_wal);

            String output_file_path_wal = Environment.getExternalStorageDirectory()+"/"+ConstantValues.foldername+"/"+ConstantValues.DB_Name+"-wal";
            OutputStream myOutput_wal = new FileOutputStream(output_file_path_wal);
            byte[] buffer_wal = new byte[1024];
            int length_wal;
            while ((length_wal = Inputstream_dbfile_wal.read(buffer_wal)) > 0)
            {
                myOutput_wal.write(buffer_wal, 0, length_wal);
            }


            myOutput_wal.flush();
            myOutput_wal.close();
            Inputstream_dbfile_wal.close();



            Toast.makeText(getApplicationContext(),"Db Exported",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Log.e("error", e.toString());
            Toast.makeText(getApplicationContext(),"EXPORT : "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }finally {

            changefragment(0);

        }
    }



}
