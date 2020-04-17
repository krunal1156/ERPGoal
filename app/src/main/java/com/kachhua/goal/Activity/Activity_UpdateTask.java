package com.kachhua.goal.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kachhua.goal.R;
import com.kachhua.goal.database.ConstantValues;
import com.kachhua.goal.database.DataBaseHelper;
import com.kachhua.goal.model.Model_GoalList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Activity_UpdateTask extends AppCompatActivity implements View.OnClickListener {

    EditText edt_title,edt_reason;
    TextView txt_startdeadline,txt_enddeadline,txt_status, txt_frequency,txt_start_time,txt_end_time;
    ImageView btn_back;
    LinearLayout btn_create;
    Calendar myCalendar;

    ArrayList<String> frequency_type_list = new ArrayList<>();
    ArrayList<String> status_type_list = new ArrayList<>();
    ArrayList<String> weekday_list=new ArrayList<>();

    String status_type,start_deadline,end_Deadline,frequency,frequency_value,goalid,starttime="",endtime="",currentdate,DayName,title,reason;
    DataBaseHelper dbhelper;
    int max_day,min_day,max_month,min_month,max_year,min_year;
    Model_GoalList.TaskDetail taskdetail;
    ArrayList<String> selected_days = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__update_task);
        myCalendar =Calendar.getInstance();
        dbhelper =new DataBaseHelper(getApplicationContext());
        goalid=getIntent().getStringExtra("goal_id");
        taskdetail=(Model_GoalList.TaskDetail)getIntent().getSerializableExtra("taskdetail");
        String mindate[] =getIntent().getStringExtra("mindate").split("/")  ;
        String maxdate[] =getIntent().getStringExtra("maxdate").split("/")  ;
        min_year=Integer.parseInt(mindate[2]);
        min_month=Integer.parseInt(mindate[1]);
        min_day=Integer.parseInt(mindate[0]);

        max_year=Integer.parseInt(maxdate[2]);
        max_month=Integer.parseInt(maxdate[1]);
        max_day=Integer.parseInt(maxdate[0]);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateformate = new SimpleDateFormat("EEEE");
        DayName = dateformate.format(c);
        currentdate = df.format(c);



        frequency_type_list.add(ConstantValues.Frequency_Daily);
        frequency_type_list.add(ConstantValues.Frequency_Weekly);
        frequency_type_list.add(ConstantValues.Frequency_Monthly);
        frequency_type_list.add(ConstantValues.Frequency_OneTime);


        status_type_list.add(ConstantValues.Status_Active);
        status_type_list.add(ConstantValues.Status_InActive);
       // status_type_list.add(ConstantValues.Status_finished);

        weekday_list.add(ConstantValues.WeekDay_Monday);
        weekday_list.add(ConstantValues.WeekDay_Tuesday);
        weekday_list.add(ConstantValues.WeekDay_Wednesday);
        weekday_list.add(ConstantValues.WeekDay_Thursday);
        weekday_list.add(ConstantValues.WeekDay_Friday);
        weekday_list.add(ConstantValues.WeekDay_Saturday);
        weekday_list.add(ConstantValues.WeekDay_Sunday );



        Findids();
        setdata();
    }
    void Findids(){

        btn_back =findViewById(R.id.img_back_button);
        edt_title =findViewById(R.id.edt_title);
        txt_startdeadline=findViewById(R.id.txt_startdeadline);
        txt_enddeadline=findViewById(R.id.txt_end_deadline);
        txt_frequency =findViewById(R.id.txt_frequncy);
        txt_status=findViewById(R.id.txt_status);
        btn_create=findViewById(R.id.button1);
        edt_reason=findViewById(R.id.edt_reason);
        txt_start_time=findViewById(R.id.txt_start_time);
        txt_end_time=findViewById(R.id.txt_end_time);


        btn_back.setOnClickListener(this);
        txt_startdeadline.setOnClickListener(this);
        txt_enddeadline.setOnClickListener(this);
        txt_frequency.setOnClickListener(this);
        txt_status.setOnClickListener(this);
        btn_create.setOnClickListener(this);
        txt_start_time.setOnClickListener(this);
        txt_end_time.setOnClickListener(this);

    }
    void setdata(){
        //status_type,start_deadline,end_Deadline,frequency,frequency_value
        start_deadline =taskdetail.getStart_deadline();
        end_Deadline=taskdetail.getEnd_deadline();
        frequency=taskdetail.getTask_frequecy();
        frequency_value=taskdetail.getTask_frequecy_value();
        status_type=taskdetail.getTaskstatus();
        starttime =taskdetail.getStarttime();
        endtime=taskdetail.getEndtime();

          edt_title.setText(taskdetail.getTaskname());;
          txt_startdeadline.setText(start_deadline);
          txt_enddeadline.setText(end_Deadline);
          txt_frequency.setText(frequency);
          txt_status.setText(status_type);
          txt_start_time.setText(starttime);
          txt_end_time.setText(endtime);

          if(frequency.equals(ConstantValues.Frequency_Weekly)){
              String[] multipledays = frequency_value.split(",");
                if(multipledays.length>0){
                    StringBuilder sb = new StringBuilder();
                    for (int i=0;i<multipledays.length;i++){
                        selected_days.add(multipledays[i]);
                        sb.append(multipledays[i].substring(0,3));
                        sb.append(",");

                    }
                    txt_frequency.setText("Every "+sb.toString());

                }
          }


    }

    void action_startdeadline(){

        try {
            Calendar myCalendar = Calendar.getInstance();

            Calendar calendar_max = Calendar.getInstance();
            Calendar calendar_min = Calendar.getInstance();
            calendar_max.set(max_year, max_month-1, max_day);
            calendar_min.set(min_year, min_month-1, min_day);



            DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_UpdateTask.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String daynum="";
                    if(dayOfMonth<10)
                        daynum="0"+String.valueOf(dayOfMonth);
                    else
                        daynum=String.valueOf(dayOfMonth);
                    start_deadline = String.valueOf(daynum) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
                    //  String  from_date =String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                    txt_startdeadline.setText(start_deadline);

                }
            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(calendar_min.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(calendar_max.getTimeInMillis());
            datePickerDialog.show();
        }catch (Exception e){

        }

    }
    void action_enddeadline(){

        try {



            Calendar calendar_max = Calendar.getInstance();
            Calendar calendar_min = Calendar.getInstance();
            calendar_max.set(max_year,max_month-1,max_day);
            calendar_min.set(min_year,min_month-1,min_day);

            Calendar myCalendar= Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_UpdateTask.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String daynum="";
                    if(dayOfMonth<10)
                        daynum="0"+String.valueOf(dayOfMonth);
                    else
                        daynum=String.valueOf(dayOfMonth);
                    end_Deadline = String.valueOf(daynum)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                    // String  to_date =String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                    txt_enddeadline.setText(end_Deadline);

                }
            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(calendar_min.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(calendar_max.getTimeInMillis());
            datePickerDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("end date error :",e.getMessage());
        }

    }

    void action_onetime(){

        try {

            Calendar myCalendar= Calendar.getInstance();

            Calendar calendar_max = Calendar.getInstance();
            Calendar calendar_min = Calendar.getInstance();
            calendar_max.set(2020,myCalendar.get(Calendar.MONTH),30);
            calendar_min.set(2020,myCalendar.get(Calendar.MONTH),1);

            DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_UpdateTask.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String daynum="";

                    if(dayOfMonth<10)
                        daynum="0"+String.valueOf(dayOfMonth);
                    else
                        daynum=String.valueOf(dayOfMonth);

                    String monthnum="";
                    if(month<10)
                        monthnum="0"+String.valueOf(month+1);
                    else
                        monthnum=String.valueOf(month+1);


                    frequency_value = String.valueOf(daynum)+"/"+String.valueOf(monthnum)+"/"+String.valueOf(year);
                    txt_frequency.setText("at "+frequency_value);

                    //txt_frequency_value.setText(" - "+frequency_value);
                    // String  to_date =String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                    // txt_enddeadline.setText(end_Deadline);
                    //Toast.makeText(getApplicationContext(),frequency+"\n"+frequency_value,Toast.LENGTH_SHORT).show();

                }
            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(calendar_min.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(calendar_max.getTimeInMillis());
            datePickerDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    void action_montly(){

        try {

            Calendar myCalendar= Calendar.getInstance();

            Calendar calendar_max = Calendar.getInstance();
            Calendar calendar_min = Calendar.getInstance();
            calendar_max.set(2020,myCalendar.get(Calendar.MONTH),30);
            calendar_min.set(2020,myCalendar.get(Calendar.MONTH),1);

            DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_UpdateTask.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String daynum="";
                    if(dayOfMonth<10)
                        daynum="0"+String.valueOf(dayOfMonth);
                    else
                        daynum=String.valueOf(dayOfMonth);
                    frequency_value = String.valueOf(daynum)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                    txt_frequency.setText("  every "+daynum+" Of Month");

                    // String  to_date =String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                    // txt_enddeadline.setText(end_Deadline);
                    Toast.makeText(getApplicationContext(),frequency+"\n"+frequency_value,Toast.LENGTH_SHORT).show();

                }
            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(calendar_min.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(calendar_max.getTimeInMillis());
            datePickerDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    void action_weekly(){

        final Dialog dialogbox_weekly = new Dialog(Activity_UpdateTask.this);
        dialogbox_weekly.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_weekly.setContentView(R.layout.dialogbox_frequency_weekly);
        dialogbox_weekly. getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout lnr_levetype_container = dialogbox_weekly.findViewById(R.id.lnr_levetype_container);
        lnr_levetype_container.removeAllViews();
        for(int i = 0; i< weekday_list.size(); i++){

            LayoutInflater inflater1  = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater1.inflate(R.layout.single_row_category_type, null);

            TextView txt_leave_type = view.findViewById(R.id.txt_leave_type);
            txt_leave_type.setText(weekday_list.get(i));

            //if(frequency_value!=null)
            if(selected_days.size()>0){

                //for(int a=0;a<selected_days.size();a++){
                if(selected_days.contains(weekday_list.get(i)))
                    txt_leave_type.setTextColor(getResources().getColor(R.color.dark_ground));
                else
                    txt_leave_type.setTextColor(getResources().getColor(R.color.dashboard_threbutton_color));

                //  }
            }


            final int finalI = i;
            txt_leave_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frequency_value = weekday_list.get(finalI);
                    txt_frequency.setText(" Every "+frequency_value);
                    selected_days.add(frequency_value);
                    dialogbox_weekly.dismiss();

                    if(selected_days.size()>1){

                        txt_frequency.setText("Multiple Weekdays selected");

                    }


                }
            });



            lnr_levetype_container.addView(view);
        }


        dialogbox_weekly.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_weekly.show();




    }


    private void show_dialogbox_frequency() {

        final Dialog dialogbox_type = new Dialog(Activity_UpdateTask.this);
        dialogbox_type.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_type.setContentView(R.layout.dialogbox_category_type);
        dialogbox_type. getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout lnr_levetype_container = dialogbox_type.findViewById(R.id.lnr_levetype_container);
        lnr_levetype_container.removeAllViews();
        for(int i = 0; i< frequency_type_list.size(); i++){

            LayoutInflater inflater1 = inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater1.inflate(R.layout.single_row_category_type, null);

            TextView txt_leave_type = view.findViewById(R.id.txt_leave_type);
            txt_leave_type.setText(frequency_type_list.get(i));

            if(frequency!=null)
                if(frequency.equals(frequency_type_list.get(i)))
                    txt_leave_type.setTextColor(getResources().getColor(R.color.dark_ground));
                else
                    txt_leave_type.setTextColor(getResources().getColor(R.color.dashboard_threbutton_color));


            final int finalI = i;
            txt_leave_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frequency = frequency_type_list.get(finalI);
                    //  txt_frequency.setText(frequency);
                    dialogbox_type.dismiss();
                    if(frequency.equals(ConstantValues.Frequency_Daily)){
                        // action_daily();
                        frequency_value=ConstantValues.Frequency_Daily;
                        txt_frequency.setText("Everyday");
                    }else if(frequency.equals(ConstantValues.Frequency_OneTime)){
                        action_onetime();

                    }else if(frequency.equals(ConstantValues.Frequency_Monthly)){
                        action_montly();
                    }else if(frequency.equals(ConstantValues.Frequency_Weekly)){
                        action_weekly();
                    }
                }
            });



            lnr_levetype_container.addView(view);
        }


        dialogbox_type.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_type.show();





    }
    private void show_dialogbox_status_type() {

        final Dialog dialogbox_type = new Dialog(Activity_UpdateTask.this);
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

            if(status_type!=null)
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

            case R.id.txt_frequncy:
                show_dialogbox_frequency();
                break;

            case R.id.txt_status:
                show_dialogbox_status_type();
                break;
            case R.id.txt_start_time:
                starttime_dialog();
                break;

            case R.id.txt_end_time:
                endtime_dialog();
                break;
            case R.id.button1:
                Update_Taask();
                break;


        }
    }

    void Update_Taask(){

         title = edt_title.getText().toString();
         reason =edt_reason.getText().toString();

        if(TextUtils.isEmpty(title)){

            Toast.makeText(getApplicationContext(),"Enter Title",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(frequency)){
            Toast.makeText(getApplicationContext(),"Select Frequency",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(status_type)){
            Toast.makeText(getApplicationContext(),"Select Status",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(start_deadline)){
            Toast.makeText(getApplicationContext(),"Select Start Date",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(end_Deadline)){
            Toast.makeText(getApplicationContext(),"Select Deadline",Toast.LENGTH_SHORT).show();
        }
        else{

            if(frequency==ConstantValues.Frequency_Weekly){
                StringBuilder sb = new StringBuilder();
                if(selected_days.size()>0)
                    for(int a =0;a<selected_days.size();a++){
                        sb.append(selected_days.get(a));
                        sb.append(",");
                    }
                frequency_value =sb.toString();
            }


            if(frequency_value==null)
                frequency_value="";
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String currentdate = df.format(c);


            dbhelper.update_task_id_db(taskdetail.getId(),goalid,title,frequency,frequency_value,status_type,start_deadline,end_Deadline,"",currentdate,ConstantValues.Status_Active,ConstantValues.Incomplated,starttime,endtime);




            finish();
        }

    }

    void insertdata_daily_once(String taskid){

        try {

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date Date_Current = df.parse(currentdate);




            Date Date_start =df.parse(start_deadline);
            Date Date_end = df.parse(end_Deadline);

            if (frequency.equals(ConstantValues.Frequency_Daily)) {


                if(is_in_betweendate(Date_Current,Date_start,Date_end))
                    dbhelper.insert_dialy_task_id_db(goalid,taskid,title, frequency, frequency_value, status_type, start_deadline, end_Deadline, "", currentdate, ConstantValues.Status_Active, ConstantValues.Incomplated,starttime,endtime); }

            else if (frequency.equals(ConstantValues.Frequency_Weekly)) {
                if (is_in_betweendate(Date_Current, Date_start, Date_end)) {
                    String[] multipledays = frequency_value.split(",");

                    for(int a=0;a<multipledays.length;a++){
                        if (DayName.equals(multipledays[a]))
                            dbhelper.insert_dialy_task_id_db(goalid, taskid, title, frequency, frequency_value, status_type, start_deadline, end_Deadline, "", currentdate, ConstantValues.Status_Active, ConstantValues.Incomplated,starttime,endtime);
                    }

                }
            }
            else if (frequency.equals(ConstantValues.Frequency_Monthly)) {

                if(is_in_betweendate(Date_Current,Date_start,Date_end)){
                    String []currentdate_array=currentdate.split("/");
                    String date1 =currentdate_array[0];

                    String frequncy_value_array[]=frequency_value.split("/");
                    String date2 =frequncy_value_array[0];
                    if (date1.equals(date2))
                        dbhelper.insert_dialy_task_id_db(goalid,taskid,title, frequency, frequency_value, status_type, start_deadline, end_Deadline, "", currentdate, ConstantValues.Status_Active, ConstantValues.Incomplated,starttime,endtime); }


            }





            else if (frequency.equals(ConstantValues.Frequency_OneTime)) {
                if(is_in_betweendate(Date_Current,Date_start,Date_end))
                    if (currentdate.equals(frequency_value))
                        dbhelper.insert_dialy_task_id_db(goalid,taskid,title, frequency, frequency_value, status_type, start_deadline, end_Deadline, "", currentdate, ConstantValues.Status_Active, ConstantValues.Incomplated,starttime,endtime); }







            finish();


        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }




    }
    boolean is_in_betweendate(Date CurrntDate,Date Startdate,Date Enddate){

        return Startdate.compareTo(CurrntDate) * CurrntDate.compareTo(Enddate)>=0;
    }

    public void starttime_dialog(){
        final Calendar c = Calendar.getInstance();
        int   mHour = c.get(Calendar.HOUR_OF_DAY);
        int  mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                        String AM_PM ;
                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                            hourOfDay=hourOfDay-12;
                        }
                        if(hourOfDay==0)
                            hourOfDay=12;

                         starttime = formatedhour(hourOfDay)+":"+formatedminute(minute)+" "+ AM_PM;

                        txt_start_time.setText(starttime);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    public void endtime_dialog(){
        final Calendar c = Calendar.getInstance();
        int   mHour = c.get(Calendar.HOUR_OF_DAY);
        int  mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {



                        String AM_PM ;
                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                            hourOfDay=hourOfDay-12;
                        }
                        if(hourOfDay==0)
                            hourOfDay=12;

                         endtime = formatedhour(hourOfDay)+":"+formatedminute(minute)+" "+ AM_PM;


                        txt_end_time.setText(endtime);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    String formatedhour(int hour){
        String test="";
         if(hour==0){
             test="12";
         }else if(hour==1){
             test="01";
         }else if(hour==2){
             test="22";
         }else if(hour==3){
             test="03";
         }else if(hour==4){
             test="04";
         }else if(hour==5){
             test="05";
         }else if(hour==6){
             test="06";
         }else if(hour==7){
             test="07";
         }else if(hour==8){
             test="08";
         }else if(hour==9){
             test="09";
         }else if(hour==10){
             test="10";
         }else if(hour==11){
             test="11";
         }else {
             test=String.valueOf(hour);
         }


        return test;
    }
    String formatedminute(int hour){
        String test="";
        if(hour==0){
            test="00";
        }else if(hour==1){
            test="01";
        }else if(hour==2){
            test="22";
        }else if(hour==3){
            test="03";
        }else if(hour==4){
            test="04";
        }else if(hour==5){
            test="05";
        }else if(hour==6){
            test="06";
        }else if(hour==7){
            test="07";
        }else if(hour==8){
            test="08";
        }else if(hour==9){
            test="09";
        }else if(hour==10){
            test="10";
        }else if(hour==11){
            test="11";
        }else {
            test=String.valueOf(hour);
        }


        return test;
    }

}
