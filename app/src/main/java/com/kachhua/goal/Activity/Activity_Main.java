package com.kachhua.goal.Activity;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kachhua.goal.Fragments.Fragment_Dashboard;
import com.kachhua.goal.Fragments.Fragment_ForceReport;
import com.kachhua.goal.Fragments.Fragment_GoalList;
import com.kachhua.goal.Fragments.Fragment_Reported_List;
import com.kachhua.goal.R;
import com.kachhua.goal.database.ConstantValues;

public class Activity_Main extends AppCompatActivity implements View.OnClickListener {

    public static DrawerLayout drawer;
    NavigationView navigationView_left;
    public static ImageView togglebutton,img_slider;
    public static TextView welcometext;
    TextView txt_dashboard,txt_my_goals,txt_top3_goals,txt_top10_goals;
    TextView txt_business_goal,txt_office_goals, txt_relationship_goals,txt_general_goals, txt_financial_goals,txt_spiritual_goal,txt_lifestyle_goal,txt_mental_goal,txt_family_goal,txt_physical_goal,txt_reporeted_list;
    LinearLayout lnr_force_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__main);
        findids();
        actions();
        changefragment(0);
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
            fragment= new Fragment_Reported_List();
        }







        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}
