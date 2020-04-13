package com.kachhua.goal.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kachhua.goal.R;
import com.kachhua.goal.database.ConstantValues;
import com.kachhua.goal.database.DataBaseHelper;
import com.kachhua.goal.model.Model_GoalList;

import java.util.ArrayList;
import java.util.Calendar;

public class Activity_UpdateGoal extends AppCompatActivity  implements View.OnClickListener {


    EditText edt_title;
    TextView txt_startdeadline,txt_enddeadline,txt_status,txt_category;
    ImageView btn_back;
    Calendar myCalendar;
    LinearLayout btn_create,btn_top3,btn_top10;

    ArrayList<String> category_type_list = new ArrayList<>();
    ArrayList<String> status_type_list = new ArrayList<>();

    String status_type="",category_type="",start_deadline="",end_deadline="",is_top3="False",is_top10="True";
    DataBaseHelper dbhelper;
    Model_GoalList goaldetail;

    ArrayList<Model_GoalList.TaskDetail>tasklist_by_goalid=new ArrayList<>();
    ArrayList<Model_GoalList.TaskDetail>daily_tasklist_by_taskid = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__update_goal);
        dbhelper=new DataBaseHelper(getApplicationContext());
        goaldetail =(Model_GoalList)getIntent().getSerializableExtra("goal_detail");

        myCalendar =Calendar.getInstance();

        category_type_list.add(ConstantValues.Category_bunishess);
        category_type_list.add(ConstantValues.Category_office);
        category_type_list.add(ConstantValues.Category_RelationShip);
        category_type_list.add(ConstantValues.Category_general);
        category_type_list.add(ConstantValues.Category_LifeStyle);
        category_type_list.add(ConstantValues.Category_Spiritual);
        category_type_list.add(ConstantValues.Category_Family);
        category_type_list.add(ConstantValues.Category_Mental);
        category_type_list.add(ConstantValues.Category_Physical);
        category_type_list.add(ConstantValues.Category_Financial);

        status_type_list.add(ConstantValues.Status_Active);
        status_type_list.add(ConstantValues.Status_InActive);
        //status_type_list.add(ConstantValues.Status_finished);

        Findids();
        setup_data();
    }

    void Findids(){

        btn_back =findViewById(R.id.img_back_button);
        edt_title =findViewById(R.id.edt_title);
        txt_startdeadline=findViewById(R.id.txt_startdeadline);
        txt_enddeadline=findViewById(R.id.txt_end_deadline);
        txt_category=findViewById(R.id.txt_category);
        txt_status=findViewById(R.id.txt_status);
        btn_create=findViewById(R.id.button1);
        btn_top3=findViewById(R.id.lnr_intop3);
        btn_top10=findViewById(R.id.lnr_intop10);


        btn_back.setOnClickListener(this);
        txt_startdeadline.setOnClickListener(this);
        txt_enddeadline.setOnClickListener(this);
        txt_category.setOnClickListener(this);
        txt_status.setOnClickListener(this);
        btn_create.setOnClickListener(this);
        btn_top3.setOnClickListener(this);
        btn_top10.setOnClickListener(this);
    }

   void  setup_data(){
        start_deadline =goaldetail.getStart_Deadline();
        end_deadline=goaldetail.getEnd_deadline();
        category_type=goaldetail.getCategory();
        status_type=goaldetail.getStatus();
        is_top3=goaldetail.getIntop3();
        is_top10=goaldetail.getIntop10();

        edt_title.setText(goaldetail.getGoalname());
       txt_startdeadline.setText(goaldetail.getStart_Deadline());
       txt_enddeadline.setText(goaldetail.getEnd_deadline());
       txt_category.setText(goaldetail.getCategory());
       txt_status.setText(goaldetail.getStatus());

       if(status_type.equals(ConstantValues.Status_running)){
           txt_status.setTextColor(getResources().getColor(R.color.dark_ground));
       }else if(status_type.equals(ConstantValues.Status_not_started)){
           txt_status.setTextColor(getResources().getColor(R.color.blue));

       }else if(status_type.equals(ConstantValues.Status_finished)){
           txt_status.setTextColor(getResources().getColor(R.color.green_light));

       }

       if(goaldetail.intop3!=null)
       if(goaldetail.intop3.equals("True")){
           btn_top3.setBackground(getResources().getDrawable(R.drawable.four_corner_round_green_10));
           btn_top10.setBackground(getResources().getDrawable(R.drawable.four_corner_round_white_10));
       }

       if(goaldetail.intop10!=null)
       if(goaldetail.intop10.equals("True")){
           btn_top10.setBackground(getResources().getDrawable(R.drawable.four_corner_round_green_10));
           btn_top3.setBackground(getResources().getDrawable(R.drawable.four_corner_round_white_10));

       }

   }

    void action_startdeadline(){
        Calendar myCalendar= Calendar.getInstance();
        Calendar calendar_max = Calendar.getInstance();
        calendar_max.add(Calendar.DATE, 0);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_UpdateGoal.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String daynum="";
                if(dayOfMonth<10)
                    daynum="0"+String.valueOf(dayOfMonth);
                else
                    daynum=String.valueOf(dayOfMonth);
                start_deadline= String.valueOf(daynum)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                //=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                txt_startdeadline.setText(start_deadline);

            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(calendar_max.getTimeInMillis());
        datePickerDialog.show();

    }

    void action_enddeadline(){
        Calendar myCalendar= Calendar.getInstance();
        Calendar calendar_max = Calendar.getInstance();
        calendar_max.add(Calendar.DATE, 0);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_UpdateGoal.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String daynum="";
                if(dayOfMonth<10)
                    daynum="0"+String.valueOf(dayOfMonth);
                else
                    daynum=String.valueOf(dayOfMonth);
                end_deadline = String.valueOf(daynum)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                // String  to_date =String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                txt_enddeadline.setText(end_deadline);

            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(calendar_max.getTimeInMillis());
        datePickerDialog.show();
    }


    private void show_dialogbox_category_type() {

        final Dialog dialogbox_type = new Dialog(Activity_UpdateGoal.this);
        dialogbox_type.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_type.setContentView(R.layout.dialogbox_category_type);
        dialogbox_type. getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout lnr_levetype_container = dialogbox_type.findViewById(R.id.lnr_levetype_container);
        lnr_levetype_container.removeAllViews();
        for(int i = 0; i< category_type_list.size(); i++){

            LayoutInflater inflater1 = inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater1.inflate(R.layout.single_row_category_type, null);

            TextView txt_leave_type = view.findViewById(R.id.txt_leave_type);
            txt_leave_type.setText(category_type_list.get(i));

            if(category_type.equals(category_type_list.get(i)))
                txt_leave_type.setTextColor(getResources().getColor(R.color.dark_ground));
            else
                txt_leave_type.setTextColor(getResources().getColor(R.color.dashboard_threbutton_color));


            final int finalI = i;
            txt_leave_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    category_type = category_type_list.get(finalI);
                    txt_category.setText(category_type);
                    dialogbox_type.dismiss();
                }
            });



            lnr_levetype_container.addView(view);
        }


        dialogbox_type.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_type.show();





    }
    private void show_dialogbox_status_type() {

        final Dialog dialogbox_type = new Dialog(Activity_UpdateGoal.this);
        dialogbox_type.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_type.setContentView(R.layout.dialogbox_status_type);
        dialogbox_type. getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout lnr_levetype_container = dialogbox_type.findViewById(R.id.lnr_levetype_container);
        lnr_levetype_container.removeAllViews();
        for(int i = 0; i< status_type_list.size(); i++){

            LayoutInflater inflater1 = inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater1.inflate(R.layout.single_row_category_type, null);

            TextView txt_leave_type = view.findViewById(R.id.txt_leave_type);
            txt_leave_type.setText(status_type_list.get(i));

            if(status_type.equals(status_type_list.get(i)))
                txt_leave_type.setTextColor(getResources().getColor(R.color.dark_ground));
            else
                txt_leave_type.setTextColor(getResources().getColor(R.color.dashboard_threbutton_color));


            final int finalI = i;
            txt_leave_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    status_type = status_type_list.get(finalI);
                    txt_status.setText(status_type);
                    dialogbox_type.dismiss();
                }
            });



            lnr_levetype_container.addView(view);
        }


        dialogbox_type.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_type.show();





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_back_button:
                finish();
                break;

            case R.id.txt_startdeadline:
                action_startdeadline();
                break;

            case R.id.txt_end_deadline:
                action_enddeadline();
                break;

            case R.id.txt_category:
                show_dialogbox_category_type();
                break;

            case R.id.txt_status:
                show_dialogbox_status_type();
                break;


            case R.id.button1:
                Update_Task();
                break;

            case R.id.lnr_intop3:
                is_top3="True";
                is_top10="False";
                btn_top3.setBackground(getResources().getDrawable(R.drawable.four_corner_round_green_10));
                btn_top10.setBackground(getResources().getDrawable(R.drawable.four_corner_round_white_10));

                break;

            case R.id.lnr_intop10:
                is_top10="True";
                is_top3="False";
                btn_top10.setBackground(getResources().getDrawable(R.drawable.four_corner_round_green_10));
                btn_top3.setBackground(getResources().getDrawable(R.drawable.four_corner_round_white_10));


                break;

        }
    }

    void Update_Task(){

        String taskname =edt_title.getText().toString();

        if(TextUtils.isEmpty(taskname)){
            Toast.makeText(getApplicationContext(),"Please enter Task Name",Toast.LENGTH_SHORT).show();
        }else if(status_type.equals("")){
            Toast.makeText(getApplicationContext(),"Please select Task Status",Toast.LENGTH_SHORT).show();

        }else if(category_type.equals("")){
            Toast.makeText(getApplicationContext(),"Please select Task Category",Toast.LENGTH_SHORT).show();

        }else if(start_deadline.equals("")){
            Toast.makeText(getApplicationContext(),"Please select Task Start Date",Toast.LENGTH_SHORT).show();

        }else if(end_deadline.equals("")){
            Toast.makeText(getApplicationContext(),"Please select Task Deadline",Toast.LENGTH_SHORT).show();

        }else{
            dbhelper.update_goallist(goaldetail.id,taskname,category_type,status_type,start_deadline,end_deadline,is_top3,is_top10);


            if(status_type.equals(ConstantValues.Status_InActive))
            tasklist_by_goalid =dbhelper.get_tasklist(goaldetail.id);
            for(int i=0;i<tasklist_by_goalid.size();i++){
                String taskid = tasklist_by_goalid.get(i).getId();
                  dbhelper.update_status_daily_task_id_db(taskid);
            }

            finish();
        }

    }
}
