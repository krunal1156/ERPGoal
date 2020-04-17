package com.kachhua.goal.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import com.kachhua.goal.Activity.Activity_Main;
import com.kachhua.goal.model.Model_GoalList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = ConstantValues.DB_Name;
    private static final int DATABASE_VERSION =10;


    Context context;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConstantValues.goallist_table_query);
        db.execSQL(ConstantValues.tasklist_table_query);
        db.execSQL(ConstantValues.Daily_tasklist_table_query);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion<newVersion) {

        }

    }

    public void insert_goal_id_db(String goal_name, String goal_category, String goal_status, String goal_startdeadline, String goal_enddeadline, String intop3, String intop10) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantValues.GoalName, goal_name);
        values.put(ConstantValues.GoalCategory, goal_category);
        values.put(ConstantValues.GoalStatus, goal_status);
        values.put(ConstantValues.Goal_startDeadLine, goal_startdeadline);
        values.put(ConstantValues.Goal_endDeadLine, goal_enddeadline);
        values.put(ConstantValues.InTop3,intop3);
        values.put(ConstantValues.InTop10,intop10);
        db.insert(ConstantValues.GoalList_Table, null, values);
        db.close();
    }
    public void update_goallist(String goalid, String goalname,String category,String status,String startdate,String deadline,String intop3,String intop10) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantValues.GoalName, goalname);
        values.put(ConstantValues.GoalCategory, category);
        values.put(ConstantValues.GoalStatus, status);
        values.put(ConstantValues.Goal_startDeadLine, startdate);
        values.put(ConstantValues.Goal_endDeadLine, deadline);
        values.put(ConstantValues.InTop3, intop3);
        values.put(ConstantValues.InTop10, intop10);
        db.update(ConstantValues.GoalList_Table, values,  " ID = ?", new String[]{goalid});
        db.close();

        if(status==ConstantValues.Status_InActive){
            SQLiteDatabase db1 = this.getWritableDatabase();
            ContentValues values1 = new ContentValues();
            values1.put(ConstantValues.TaskStatus, ConstantValues.Status_InActive);
            db1.update(ConstantValues.Tasklist_Table, values1,  ConstantValues.GoalId+" = ?", new String[]{goalid});
            db1.close();

        }



    }


    public void delete_goal(String goal_id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(ConstantValues.GoalList_Table, "ID = ?", new String[]{goal_id});
        db.close();
    }

    public void delete_task(String task_id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(ConstantValues.Tasklist_Table, "ID = ?", new String[]{task_id});
        db.close();
    }
    public String getgoalname(String goalid){
        String goalname ="";
        String query = "select "+ConstantValues.GoalName+" from " + ConstantValues.GoalList_Table+ " WHERE "  + "ID = '" + goalid + "'"; ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                try {

                    goalname =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));


                } catch (Exception e) {
                    e.printStackTrace();
                }




            } while (cursor.moveToNext());
        }
        return  goalname;
    }
    public ArrayList<Model_GoalList> get_goallist(String query) {
        ArrayList<Model_GoalList> modelList = new ArrayList<Model_GoalList>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ///////alwasy start from 0 culumn index 0 means first column////////////
                Model_GoalList model = new Model_GoalList();
                model.setId(cursor.getString(cursor.getColumnIndex("ID")));
                model.setGoalname(cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName)));
                model.setCategory(cursor.getString(cursor.getColumnIndex(ConstantValues.GoalCategory)));
                model.setStatus(cursor.getString(cursor.getColumnIndex(ConstantValues.GoalStatus)));
                model.setFrequency(cursor.getString(cursor.getColumnIndex(ConstantValues.Goal_Frequency)));
                model.setStart_Deadline(cursor.getString(cursor.getColumnIndex(ConstantValues.Goal_startDeadLine)));
                model.setEnd_deadline(cursor.getString(cursor.getColumnIndex(ConstantValues.Goal_endDeadLine)));
                model.setIntop3(cursor.getString(cursor.getColumnIndex(ConstantValues.InTop3)));
                model.setIntop10(cursor.getString(cursor.getColumnIndex(ConstantValues.InTop10)));

                    modelList.add(model);




            } while (cursor.moveToNext());
        }
        return modelList;
    }
    public int get_intop3() {
        String countQuery = "SELECT  * FROM " + ConstantValues.GoalList_Table+" WHERE "+ConstantValues.InTop3+" = 'True'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public int get_intop10() {
        String countQuery = "SELECT  * FROM " + ConstantValues.GoalList_Table+" WHERE "+ConstantValues.InTop10+" = 'True'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void insert_task_id_db(String goalid, String taskname, String task_frequency, String frequency_value,String task_status, String start_deadline, String end_deadline, String reason,String createddte,String active,String complate,String starttime,String endtime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantValues.GoalId, goalid);
        values.put(ConstantValues.TaskName, taskname);
        values.put(ConstantValues.Task_Frequency, task_frequency);
        values.put(ConstantValues.Task_Frequency_value,frequency_value);
        values.put(ConstantValues.TaskStatus, task_status);
        values.put(ConstantValues.Task_startDeadline, start_deadline);
        values.put(ConstantValues.Task_endDeadline,end_deadline);
        values.put(ConstantValues.Reason,reason);
        values.put(ConstantValues.Task_Created_Date,createddte);
        values.put(ConstantValues.TaskStartTime,starttime);
        values.put(ConstantValues.TaskEndTime,endtime);
        values.put(ConstantValues.TaskComplate,complate);
        values.put(ConstantValues.Task_Active_InActive,active);
        db.insert(ConstantValues.Tasklist_Table, null, values);
        db.close();
    }
    public void update_task_id_db(String id,String goalid, String taskname, String task_frequency,String frequency_value ,String task_status, String start_deadline, String end_deadline, String reason,String createddte,String active,String complate,String starttime,String endtime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantValues.GoalId, goalid);
        values.put(ConstantValues.TaskName, taskname);
        values.put(ConstantValues.Task_Frequency, task_frequency);
        values.put(ConstantValues.Task_Frequency_value, frequency_value);
        values.put(ConstantValues.TaskStatus, task_status);
        values.put(ConstantValues.Task_startDeadline, start_deadline);
        values.put(ConstantValues.Task_endDeadline,end_deadline);
        values.put(ConstantValues.Reason,reason);
        //values.put(ConstantValues.Task_Created_Date,createddte);
        values.put(ConstantValues.TaskComplate,complate);
        values.put(ConstantValues.Task_Active_InActive,active);
        values.put(ConstantValues.TaskStartTime,starttime);
        values.put(ConstantValues.TaskEndTime,endtime);
        db.update(ConstantValues.Tasklist_Table, values,  " ID = ?", new String[]{id});
        db.close();


        if(task_status==ConstantValues.Status_InActive){
            SQLiteDatabase db1 = this.getWritableDatabase();
            ContentValues values1 = new ContentValues();
            values1.put(ConstantValues.TaskStatus, ConstantValues.Status_InActive);
            values1.put(ConstantValues.Task_Frequency,task_frequency);
            values.put(ConstantValues.Task_Frequency_value, frequency_value);
            db1.update(ConstantValues.Daily_Tasklist_Table, values1,  ConstantValues.TaskId+" = ?", new String[]{id});
            db1.close();

        }else{
            SQLiteDatabase db1 = this.getWritableDatabase();
            ContentValues values1 = new ContentValues();
            values1.put(ConstantValues.TaskStatus, ConstantValues.Status_Active);
            values1.put(ConstantValues.Task_Frequency,task_frequency);
            values.put(ConstantValues.Task_Frequency_value, frequency_value);
            db1.update(ConstantValues.Daily_Tasklist_Table, values1,  ConstantValues.TaskId+" = ?", new String[]{id});
            db1.close();
        }


    }



    public ArrayList<Model_GoalList.TaskDetail> get_tasklist(String goalid) {
        ArrayList<Model_GoalList.TaskDetail> modelList = new ArrayList<Model_GoalList.TaskDetail>();
        String query = "select * from " + ConstantValues.Tasklist_Table+ " WHERE " + ConstantValues.GoalId + "= '" + goalid + "'"; ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor_one = db.rawQuery(query, null);

        String show_goalname="";
        if (cursor_one.moveToFirst()) {
            do {
                ///////alwasy start from 0 culumn index 0 means first column////////////
                try {
                    Model_GoalList.TaskDetail model = new Model_GoalList.TaskDetail();
                    model.setId(cursor_one.getString(cursor_one.getColumnIndex("ID")));
                    model.setGoalid(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.GoalId)));
                    model.setTaskname(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskName)));
                    model.setTask_frequecy(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Frequency)));
                    model.setTaskstatus(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskStatus)));
                    model.setStart_deadline(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_startDeadline)));
                    model.setEnd_deadline(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_endDeadline)));
                    model.setReason(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Reason)));
                    model.setTask_frequecy_value(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Frequency_value)));
                    model.setCreated_date(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Created_Date)));
                    model.setIs_active(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Active_InActive)));
                    model.setIs_complate(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskComplate)));
                    model.setStarttime(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskStartTime)));
                    model.setEndtime(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskEndTime)));


                   // String goalid =cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.GoalId));
                    String query1 = "select "+ConstantValues.GoalName+" from " + ConstantValues.GoalList_Table+ " WHERE "  + "ID = '" +goalid + "'"; ;
                    SQLiteDatabase db2 = this.getWritableDatabase();
                    Cursor cursor = db2.rawQuery(query1, null);

                    if (cursor.moveToFirst()) {
                        do {

                            try {

                                String name =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));
                                if(show_goalname.equals(name)){
                                    model.setShow("1");
                                }else{
                                    model.setShow("0");
                                }

                                show_goalname =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));
                                model.setGoalname(show_goalname);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }




                        } while (cursor.moveToNext());
                    }




                    //if(Date_corrent.after(Date_start) && Date_corrent.before(Date_end))
                    //  if(is_in_betweendate(Date_corrent,Date_start,Date_end))
                    modelList.add(model);


                } catch (Exception e) {
                    e.printStackTrace();
                }





            } while (cursor_one.moveToNext());
        }
        return modelList;
    }
    public ArrayList<Model_GoalList.TaskDetail> get_tasklist() {
        ArrayList<Model_GoalList.TaskDetail> modelList = new ArrayList<Model_GoalList.TaskDetail>();
        String query = "select * from " + ConstantValues.Tasklist_Table;//+ " WHERE " + ConstantValues.TaskComplate + "= '" + ConstantValues.Incomplated + "'"; ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor_one = db.rawQuery(query, null);

        String show_goalname="";
        if (cursor_one.moveToFirst()) {
            do {
                ///////alwasy start from 0 culumn index 0 means first column////////////

                try {
                    Model_GoalList.TaskDetail model = new Model_GoalList.TaskDetail();
                    model.setId(cursor_one.getString(cursor_one.getColumnIndex("ID")));
                    model.setGoalid(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.GoalId)));
                    model.setTaskname(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskName)));
                    model.setTask_frequecy(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Frequency)));
                    model.setTaskstatus(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskStatus)));
                    model.setStart_deadline(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_startDeadline)));
                    model.setEnd_deadline(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_endDeadline)));
                    model.setReason(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Reason)));
                    model.setTask_frequecy_value(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Frequency_value)));
                    model.setCreated_date(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Created_Date)));
                    model.setIs_active(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Active_InActive)));
                    model.setIs_complate(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskComplate)));
                    model.setStarttime(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskStartTime)));
                    model.setEndtime(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskEndTime)));



                    String goalid =cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.GoalId));
                    String query1 = "select "+ConstantValues.GoalName+" from " + ConstantValues.GoalList_Table+ " WHERE "  + "ID = '" +goalid + "'"; ;
                    SQLiteDatabase db1 = this.getWritableDatabase();
                    Cursor cursor = db1.rawQuery(query1, null);

                    if (cursor.moveToFirst()) {
                        do {

                            try {

                                String name =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));
                                if(show_goalname.equals(name)){
                                    model.setShow("1");
                                }else{
                                    model.setShow("0");
                                }

                                show_goalname =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));
                                model.setGoalname(show_goalname);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }




                        } while (cursor.moveToNext());
                    }




                    //if(Date_corrent.after(Date_start) && Date_corrent.before(Date_end))
                    //  if(is_in_betweendate(Date_corrent,Date_start,Date_end))
                    modelList.add(model);


                } catch (Exception e) {
                    e.printStackTrace();
                }




            } while (cursor_one.moveToNext());
        }
        return modelList;
    }


    public ArrayList<Model_GoalList.TaskDetail> get_tasklist_force_report(String currentdate) {
        ArrayList<Model_GoalList.TaskDetail> modelList = new ArrayList<Model_GoalList.TaskDetail>();
        String query = "select * from " + ConstantValues.Tasklist_Table;//+ " WHERE " + ConstantValues.GoalId + "= '" + goalid + "'"; ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ///////alwasy start from 0 culumn index 0 means first column////////////


                try {
                    Model_GoalList.TaskDetail model = new Model_GoalList.TaskDetail();
                    model.setId(cursor.getString(cursor.getColumnIndex("ID")));
                    model.setTaskname(cursor.getString(cursor.getColumnIndex(ConstantValues.TaskName)));
                    model.setTask_frequecy(cursor.getString(cursor.getColumnIndex(ConstantValues.Task_Frequency)));
                    model.setTaskstatus(cursor.getString(cursor.getColumnIndex(ConstantValues.TaskStatus)));
                    model.setStart_deadline(cursor.getString(cursor.getColumnIndex(ConstantValues.Task_startDeadline)));
                    model.setEnd_deadline(cursor.getString(cursor.getColumnIndex(ConstantValues.Task_endDeadline)));
                    model.setReason(cursor.getString(cursor.getColumnIndex(ConstantValues.Reason)));
                    model.setCreated_date(cursor.getString(cursor.getColumnIndex(ConstantValues.Task_Created_Date)));
                    model.setIs_active(cursor.getString(cursor.getColumnIndex(ConstantValues.Task_Active_InActive)));
                    model.setIs_complate(cursor.getString(cursor.getColumnIndex(ConstantValues.TaskComplate)));

                    String str_startdate =cursor.getString(cursor.getColumnIndex(ConstantValues.Task_startDeadline));
                    String lstr_astdate=cursor.getString(cursor.getColumnIndex(ConstantValues.Task_endDeadline));
                    SimpleDateFormat formate = new SimpleDateFormat("dd/MM/yyyy");

                    Date Date_corrent=formate.parse(currentdate);
                    Date Date_start =formate.parse(str_startdate);
                    Date Date_end =formate.parse(lstr_astdate);

                    //if(Date_corrent.after(Date_end))
                    modelList.add(model);
                   // if(is_in_betweendate(Date_corrent,Date_start,Date_end))
                   //     modelList.add(model);


                } catch (ParseException e) {
                    e.printStackTrace();
                }




            } while (cursor.moveToNext());
        }
        return modelList;
    }



    public void insert_dialy_task_id_db(String goalid, String taskid,String taskname, String task_frequency, String frequency_value,String task_status, String start_deadline, String end_deadline, String reason,String created_date,String active,String complate,String starttime,String endtime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantValues.GoalId, goalid);
        values.put(ConstantValues.TaskId,taskid);
        values.put(ConstantValues.TaskName, taskname);
        values.put(ConstantValues.Task_Frequency, task_frequency);
        values.put(ConstantValues.Task_Frequency_value, frequency_value);
        values.put(ConstantValues.TaskStatus, task_status);
        values.put(ConstantValues.Task_startDeadline, start_deadline);
        values.put(ConstantValues.Task_endDeadline,end_deadline);
        values.put(ConstantValues.Reason,reason);
        values.put(ConstantValues.Task_Created_Date,created_date);
        values.put(ConstantValues.Task_Active_InActive,active);
        values.put(ConstantValues.TaskComplate,complate);
        values.put(ConstantValues.TaskStartTime,starttime);
        values.put(ConstantValues.TaskEndTime,endtime);
        db.insert(ConstantValues.Daily_Tasklist_Table, null, values);
        db.close();
    }
    public void update_daily_task_id_db(String currentdate,String taskid,String original_taskid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantValues.TaskComplate,ConstantValues.Complated);
        values.put(ConstantValues.Task_Finished_Date,currentdate);
        db.update(ConstantValues.Daily_Tasklist_Table, values,  " ID = ?", new String[]{taskid});
        db.close();

        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();
       // values1.put(ConstantValues.TaskComplate,ConstantValues.Complated);
        values1.put(ConstantValues.Task_Finished_Date,currentdate);
        db1.update(ConstantValues.Tasklist_Table, values1,  " ID = ?", new String[]{original_taskid});
        db1.close();
    }
    public void update_reason_daily_task_id_db(String taskid,String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantValues.TaskComplate,ConstantValues.Status_Reported);
        values.put(ConstantValues.Reason,reason);

        db.update(ConstantValues.Daily_Tasklist_Table, values,  " ID = ?", new String[]{taskid});
        db.close();
    }
    public void update_status_daily_task_id_db(String taskid){
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put(ConstantValues.TaskStatus, ConstantValues.Status_InActive);
        db1.update(ConstantValues.Daily_Tasklist_Table, values1,  ConstantValues.TaskId+" = ?", new String[]{taskid});
        db1.close();

    }

    public ArrayList<Model_GoalList.TaskDetail> get_daily_tasklist_runnig(String currentdate) {


        ArrayList<Model_GoalList.TaskDetail> modelList = new ArrayList<Model_GoalList.TaskDetail>();
        //String query_one = "select * from " + ConstantValues.Daily_Tasklist_Table+ " WHERE " + ConstantValues.TaskComplate + "= '" + ConstantValues.Incomplated + "AND " + ConstantValues.TaskStatus + "= '" + ConstantValues.Status_Active  +"'"; ;
        String query_one = "select * from " + ConstantValues.Daily_Tasklist_Table + " WHERE " + ConstantValues.TaskComplate + "= '" + ConstantValues.Incomplated +"'"  +" AND " +ConstantValues.TaskStatus+ " = '" + ConstantValues.Status_Active +"'"+" AND "+ConstantValues.Task_Created_Date+" = '"+currentdate+"' "+"ORDER BY "+ConstantValues.GoalId+ "" ;

        SQLiteDatabase db_one = this.getWritableDatabase();
        Cursor cursor_one = db_one.rawQuery(query_one, null);

        String show_goalname="";
        if (cursor_one.moveToFirst()) {
            do {
                ///////alwasy start from 0 culumn index 0 means first column////////////


                try {
                    Model_GoalList.TaskDetail model = new Model_GoalList.TaskDetail();
                    model.setId(cursor_one.getString(cursor_one.getColumnIndex("ID")));
                    model.setGoalid(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.GoalId)));
                    model.setTaskid(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskId)));
                    model.setTaskname(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskName)));
                    model.setTask_frequecy(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Frequency)));
                    model.setTaskstatus(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskStatus)));
                    model.setStart_deadline(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_startDeadline)));
                    model.setEnd_deadline(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_endDeadline)));
                    model.setReason(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Reason)));
                    model.setTask_frequecy_value(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Frequency_value)));
                    model.setCreated_date(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Created_Date)));
                    model.setIs_active(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Active_InActive)));
                    model.setIs_complate(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskComplate)));

                    model.setStarttime(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskStartTime)));
                    model.setEndtime(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskEndTime)));


                    String goalid =cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.GoalId));
                    String query = "select "+ConstantValues.GoalName+" from " + ConstantValues.GoalList_Table+ " WHERE "  + "ID = '" +goalid + "'"; ;
                    SQLiteDatabase db = this.getWritableDatabase();
                    Cursor cursor = db.rawQuery(query, null);

                    if (cursor.moveToFirst()) {
                        do {

                            try {

                                String name =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));
                                if(show_goalname.equals(name)){
                                    model.setShow("1");
                                }else{
                                    model.setShow("0");
                                }

                                show_goalname =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));
                                model.setGoalname(show_goalname);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                        } while (cursor.moveToNext());
                    }




                  /*  String currrent_date_from_db =cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Created_Date));
                    if(currrent_date_from_db.equals(currentdate))
                        modelList.add(model);
                    else
                        modelList.size();
*/

                    modelList.add(model);


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }




            } while (cursor_one.moveToNext());
        }
        return modelList;
    }
    public ArrayList<Model_GoalList.TaskDetail> get_all_daily_tasklist_by_taskid(String taskid) {


        ArrayList<Model_GoalList.TaskDetail> modelList = new ArrayList<Model_GoalList.TaskDetail>();
        String query_one = "select * from " + ConstantValues.Daily_Tasklist_Table + " WHERE " + ConstantValues.TaskId + "= '" + taskid +"'"+" ORDER BY "+ConstantValues.Task_Created_Date;

        SQLiteDatabase db_one = this.getWritableDatabase();
        Cursor cursor_one = db_one.rawQuery(query_one, null);

        String show_goalname="";
        if (cursor_one.moveToFirst()) {
            do {
                ///////alwasy start from 0 culumn index 0 means first column////////////


                try {
                    Model_GoalList.TaskDetail model = new Model_GoalList.TaskDetail();
                    model.setId(cursor_one.getString(cursor_one.getColumnIndex("ID")));
                    model.setGoalid(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.GoalId)));
                    model.setTaskid(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskId)));
                    model.setTaskname(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskName)));
                    model.setTask_frequecy(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Frequency)));
                    model.setTaskstatus(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskStatus)));
                    model.setStart_deadline(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_startDeadline)));
                    model.setEnd_deadline(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_endDeadline)));
                    model.setReason(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Reason)));
                    model.setTask_frequecy_value(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Frequency_value)));
                    model.setCreated_date(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Created_Date)));
                    model.setIs_active(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Active_InActive)));
                    model.setIs_complate(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskComplate)));
                    model.setStarttime(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskStartTime)));
                    model.setEndtime(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskEndTime)));
                    model.setFinished_date(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Finished_Date)));


                    String goalid =cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.GoalId));
                    String query = "select "+ConstantValues.GoalName+" from " + ConstantValues.GoalList_Table+ " WHERE "  + "ID = '" +goalid + "'"; ;
                    SQLiteDatabase db = this.getWritableDatabase();
                    Cursor cursor = db.rawQuery(query, null);

                    if (cursor.moveToFirst()) {
                        do {

                            try {

                                String name =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));
                                if(show_goalname.equals(name)){
                                    model.setShow("1");
                                }else{
                                    model.setShow("0");
                                }

                                show_goalname =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));
                                model.setGoalname(show_goalname);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                        } while (cursor.moveToNext());
                    }






                      modelList.add(model);


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }




            } while (cursor_one.moveToNext());
        }
        return modelList;
    }

    public ArrayList<Model_GoalList.TaskDetail> get_daily_tasklist_finished(String currentdate) {
        ArrayList<Model_GoalList.TaskDetail> modelList = new ArrayList<Model_GoalList.TaskDetail>();
      //  String query_one = "select * from " + ConstantValues.Daily_Tasklist_Table+ " WHERE " + ConstantValues.TaskComplate + "= '" + ConstantValues.Complated + "'" ;
        String query_one = "select * from " + ConstantValues.Daily_Tasklist_Table + " WHERE " + ConstantValues.TaskComplate + "= '" + ConstantValues.Complated +"'" +" AND "+ConstantValues.Task_Created_Date+" = '"+currentdate+"' "+"ORDER BY "+ConstantValues.GoalId+ "" ;

        SQLiteDatabase db_one = this.getWritableDatabase();
        Cursor cursor_one = db_one.rawQuery(query_one, null);

        String show_goalname="";
        if (cursor_one.moveToFirst()) {
            do {
                ///////alwasy start from 0 culumn index 0 means first column////////////


                try {
                    Model_GoalList.TaskDetail model = new Model_GoalList.TaskDetail();
                    model.setId(cursor_one.getString(cursor_one.getColumnIndex("ID")));
                    model.setGoalid(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.GoalId)));
                    model.setTaskid(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskId)));

                    model.setTaskname(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskName)));
                    model.setTask_frequecy(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Frequency)));
                    model.setTaskstatus(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskStatus)));
                    model.setStart_deadline(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_startDeadline)));
                    model.setEnd_deadline(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_endDeadline)));
                    model.setReason(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Reason)));
                    model.setTask_frequecy_value(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Frequency_value)));
                    model.setCreated_date(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Created_Date)));
                    model.setIs_active(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Active_InActive)));
                    model.setIs_complate(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskComplate)));
                    model.setStarttime(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskStartTime)));
                    model.setEndtime(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskEndTime)));



                    String goalid =cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.GoalId));
                    String query = "select "+ConstantValues.GoalName+" from " + ConstantValues.GoalList_Table+ " WHERE "  + "ID = '" +goalid + "'"; ;
                    SQLiteDatabase db = this.getWritableDatabase();
                    Cursor cursor = db.rawQuery(query, null);

                    if (cursor.moveToFirst()) {
                        do {

                            try {

                                String name =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));
                                if(show_goalname.equals(name)){
                                    model.setShow("1");
                                }else{
                                    model.setShow("0");
                                }

                                show_goalname =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));
                                model.setGoalname(show_goalname);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }




                        } while (cursor.moveToNext());
                    }



                    /*String currrent_date_from_db =cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Finished_Date));
                    if(currrent_date_from_db.equals(currentdate))
                        modelList.add(model);
*/
                    modelList.add(model);


                } catch (Exception e) {
                    e.printStackTrace();
                }




            } while (cursor_one.moveToNext());
        }
        return modelList;
    }
    public ArrayList<Model_GoalList.TaskDetail> get_daily_tasklist_missed() {
        ArrayList<Model_GoalList.TaskDetail> modelList = new ArrayList<Model_GoalList.TaskDetail>();
        String query_one = "select * from " + ConstantValues.Daily_Tasklist_Table+ " WHERE " + ConstantValues.TaskComplate + "= '" + ConstantValues.Incomplated + "'"+" ORDER BY "+ConstantValues.GoalId; ;
        SQLiteDatabase db_one = this.getWritableDatabase();
        Cursor cursor_one = db_one.rawQuery(query_one, null);

        String show_goalname="";
        if (cursor_one.moveToFirst()) {
            do {
                ///////alwasy start from 0 culumn index 0 means first column////////////


                try {
                    Model_GoalList.TaskDetail model = new Model_GoalList.TaskDetail();
                    model.setId(cursor_one.getString(cursor_one.getColumnIndex("ID")));
                    model.setGoalid(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.GoalId)));
                    model.setTaskid(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskId)));

                    model.setTaskname(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskName)));
                    model.setTask_frequecy(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Frequency)));
                    model.setTaskstatus(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskStatus)));
                    model.setStart_deadline(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_startDeadline)));
                    model.setEnd_deadline(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_endDeadline)));
                    model.setReason(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Reason)));
                    model.setTask_frequecy_value(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Frequency_value)));
                    model.setCreated_date(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Created_Date)));
                    model.setIs_active(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Active_InActive)));
                    model.setIs_complate(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskComplate)));
                    model.setStarttime(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskStartTime)));
                    model.setEndtime(cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.TaskEndTime)));



                    String goalid =cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.GoalId));
                    String query = "select "+ConstantValues.GoalName+" from " + ConstantValues.GoalList_Table+ " WHERE "  + "ID = '" +goalid + "'"; ;
                    SQLiteDatabase db = this.getWritableDatabase();
                    Cursor cursor = db.rawQuery(query, null);

                    if (cursor.moveToFirst()) {
                        do {

                            try {

                                String name =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));
                                if(show_goalname.equals(name)){
                                    model.setShow("1");
                                }else{
                                    model.setShow("0");
                                }

                                show_goalname =cursor.getString(cursor.getColumnIndex(ConstantValues.GoalName));
                                model.setGoalname(show_goalname);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }




                        } while (cursor.moveToNext());
                    }




                    try {


                        SimpleDateFormat formate= new SimpleDateFormat("dd/MM/yyyy");


                        Date Cureent = Calendar.getInstance().getTime();

                        String str_task_created_tdate =cursor_one.getString(cursor_one.getColumnIndex(ConstantValues.Task_Created_Date));

                        String str_currentdate = formate.format(Cureent);

                        Date Date_Current = formate.parse(str_currentdate);
                        Date Date_Task_crated = formate.parse(str_task_created_tdate);

                        if(Date_Current.after(Date_Task_crated) )
                            modelList.add(model);




                    } catch (ParseException e) {
                        e.printStackTrace();
                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }




            } while (cursor_one.moveToNext());
        }
        return modelList;
    }




}
