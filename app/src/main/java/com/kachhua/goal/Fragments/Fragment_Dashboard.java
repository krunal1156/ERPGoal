package com.kachhua.goal.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kachhua.goal.Activity.Activity_CreateGoal;
import com.kachhua.goal.Activity.Activity_UpdateTask;
import com.kachhua.goal.R;
import com.kachhua.goal.Utility.PrefUtils;
import com.kachhua.goal.database.ConstantValues;
import com.kachhua.goal.database.DataBaseHelper;
import com.kachhua.goal.model.Model_GoalList;
import com.kachhua.goal.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;

import static com.kachhua.goal.Activity.Activity_Main.welcometext;

public class Fragment_Dashboard extends Fragment {

    User userdetai;
    HorizontalCalendar horizontalCalendar;
    Calendar currentcalender, startcalender, endcalender;
    ProgressBar progressBar;
    ImageView img_creategoal;
    LinearLayout lnr_all_task,lnr_running_ask,lnr_finished_task,lnr_missed_task;
    TextView txt_no_task_label,txt_all,txt_running,txt_finished,txt_missed;

    ArrayList<Model_GoalList> filtered_rv_goallist = new ArrayList<>();
    ArrayList<Model_GoalList.TaskDetail> all_tasklist_from_db = new ArrayList<>();
    ArrayList<Model_GoalList.TaskDetail> daily_tasklist_from_db = new ArrayList<>();
    ArrayList<Model_GoalList.TaskDetail> rv_daily_tasklist_from_db = new ArrayList<>();

    DataBaseHelper dbhelper;
    RecyclerView rv_horizonal,rv_vertical_tasklist;
    RV_Adapter_TaskList adapter_tasklist;
    int selected_index_horizontal_rv =0;
    String goal_id,goal_startdate,goal_enddate;
    String DayName,currentdate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_test, container, false);
        userdetai = PrefUtils.getCurrentUser(getContext());
        dbhelper=new DataBaseHelper(getContext());



        custom_calender_init(view);

        findIds(view);






        return view;

    }

    void findIds(View view){
        rv_horizonal=view.findViewById(R.id.rv_horizontal);
        rv_vertical_tasklist=view.findViewById(R.id.rv_vertical_tasklist);

        txt_no_task_label=view.findViewById(R.id.txt_no_taskl_label);

        txt_all=view.findViewById(R.id.txt_all);
        txt_running=view.findViewById(R.id.txt_running);
        txt_finished=view.findViewById(R.id.txt_finished);
        txt_missed=view.findViewById(R.id.txt_missed);
        lnr_all_task=view.findViewById(R.id.lnr_all_task);
        lnr_running_ask=view.findViewById(R.id.lnr_running_task);
        lnr_finished_task=view.findViewById(R.id.lnr_finished_task);
        lnr_missed_task=view.findViewById(R.id.lnr_missed_task);


        img_creategoal=view.findViewById(R.id.img_plus);
        img_creategoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myint = new Intent(getContext(),Activity_CreateGoal.class);
                startActivity(myint);
            }
        });

        lnr_all_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"All Task",Toast.LENGTH_SHORT).show();
                txt_all.setTextColor(getResources().getColor(R.color.white));
                txt_running.setTextColor(getResources().getColor(R.color.black));;
                txt_finished.setTextColor(getResources().getColor(R.color.black));
                txt_missed.setTextColor(getResources().getColor(R.color.black));
            }
        });

        lnr_running_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Running Task",Toast.LENGTH_SHORT).show();

                txt_all.setTextColor(getResources().getColor(R.color.black));
                txt_running.setTextColor(getResources().getColor(R.color.white));;
                txt_finished.setTextColor(getResources().getColor(R.color.black));
                txt_missed.setTextColor(getResources().getColor(R.color.black));

                daily_tasklist_from_db.clear();;
                daily_tasklist_from_db=dbhelper.get_daily_tasklist_runnig(currentdate);
                if(daily_tasklist_from_db!=null && daily_tasklist_from_db.size()>0){
                    adapter_tasklist = new RV_Adapter_TaskList(daily_tasklist_from_db,ConstantValues.Incomplated);
                    rv_vertical_tasklist.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv_vertical_tasklist.setAdapter(adapter_tasklist);
                    rv_vertical_tasklist.setVisibility(View.VISIBLE);
                    txt_no_task_label.setVisibility(View.GONE);

                }else {
                    rv_vertical_tasklist.setVisibility(View.GONE);
                    txt_no_task_label.setVisibility(View.VISIBLE);
                    txt_no_task_label.setText("No Daily Task Found !");
                   // Toast.makeText(getContext(),"No  Daily TaskList Found For Today",Toast.LENGTH_SHORT).show();

                }
            }
        });

        lnr_finished_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Finished Task",Toast.LENGTH_SHORT).show();

                txt_all.setTextColor(getResources().getColor(R.color.black));
                txt_running.setTextColor(getResources().getColor(R.color.black));;
                txt_finished.setTextColor(getResources().getColor(R.color.white));
                txt_missed.setTextColor(getResources().getColor(R.color.black));


                daily_tasklist_from_db.clear();;
                daily_tasklist_from_db=dbhelper.get_daily_tasklist_finished(currentdate);
                if(daily_tasklist_from_db!=null && daily_tasklist_from_db.size()>0){
                    adapter_tasklist = new RV_Adapter_TaskList(daily_tasklist_from_db,ConstantValues.Complated);
                    rv_vertical_tasklist.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv_vertical_tasklist.setAdapter(adapter_tasklist);
                    rv_vertical_tasklist.setVisibility(View.VISIBLE);
                    txt_no_task_label.setVisibility(View.GONE);


                }else {
                    rv_vertical_tasklist.setVisibility(View.GONE);
                    txt_no_task_label.setVisibility(View.VISIBLE);
                    txt_no_task_label.setText("No Finished Task Found !");
                  //  Toast.makeText(getContext(),"No  Daily TaskList Found For Today",Toast.LENGTH_SHORT).show();

                }
            }
        });

        lnr_missed_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Missed Task",Toast.LENGTH_SHORT).show();

                txt_all.setTextColor(getResources().getColor(R.color.black));
                txt_running.setTextColor(getResources().getColor(R.color.black));;
                txt_finished.setTextColor(getResources().getColor(R.color.black));
                txt_missed.setTextColor(getResources().getColor(R.color.white));


                daily_tasklist_from_db.clear();;
                daily_tasklist_from_db=dbhelper.get_daily_tasklist_missed();
                if(daily_tasklist_from_db!=null && daily_tasklist_from_db.size()>0){
                    adapter_tasklist = new RV_Adapter_TaskList(daily_tasklist_from_db,ConstantValues.Status_Missed);
                    rv_vertical_tasklist.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv_vertical_tasklist.setAdapter(adapter_tasklist);
                    rv_vertical_tasklist.setVisibility(View.VISIBLE);
                    txt_no_task_label.setVisibility(View.GONE);


                }else {
                    rv_vertical_tasklist.setVisibility(View.GONE);
                    txt_no_task_label.setVisibility(View.VISIBLE);
                    txt_no_task_label.setText("No Missed Task Found !");
                  //  Toast.makeText(getContext(),"No  Daily TaskList Found For Today",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    void custom_calender_init(View view){
        currentcalender = Calendar.getInstance();
        startcalender = Calendar.getInstance();
        startcalender.add(Calendar.DAY_OF_WEEK, 0);

        endcalender = Calendar.getInstance();
        endcalender.add(Calendar.DAY_OF_WEEK, 6);

        horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startcalender, endcalender)
                .datesNumberOnScreen(5)
                .configure()
                .selectorColor(getResources().getColor(R.color.trans))
                .textSize(8,11,8)
                .end()
                .build();


        horizontalCalendar.selectDate(currentcalender, false);
        horizontalCalendar.setCalendarListener(new devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {


                    horizontalCalendar.getSelectedItemStyle()
                            .setColorTopText(getResources().getColor(R.color.black))
                            .setColorMiddleText(getResources().getColor(R.color.black))
                            .setColorBottomText(getResources().getColor(R.color.black))
                            .setBackground(getResources().getDrawable(R.drawable.four_corner_round_white_10));

                    horizontalCalendar.refresh();

            }

            @Override
            public void onCalendarScroll(devs.mulham.horizontalcalendar.HorizontalCalendarView calendarView, int dx, int dy) {
                // calendarView.getPositionOfCenterItem();
                //horizontalCalendar.selectDate( startDate, true);
            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                //  horizontalCalendar.getDateAt(position);

                return true;
            }
        });

    }

   /* public class RV_Adapter_GoalList extends RecyclerView.Adapter<ViewHolder_GoalList>  {




        public RV_Adapter_GoalList(ArrayList<Model_GoalList> pendingorder) {

            filtered_rv_goallist = pendingorder;

        }

        @Override
        public ViewHolder_GoalList onCreateViewHolder(ViewGroup parent, int viewType) {


            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_goallist_dashboard_recyclerview, parent, false);
            ViewHolder_GoalList viewHolder = new ViewHolder_GoalList(itemLayoutView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder_GoalList viewHolder, final int i) {


            viewHolder.txt_goalname.setText(filtered_rv_goallist.get(i).getGoalname());
            viewHolder.txt_deadline.setText(filtered_rv_goallist.get(i).getEnd_deadline());
            viewHolder.txt_status.setText(filtered_rv_goallist.get(i).getStatus());

            if(filtered_rv_goallist.get(i).getStatus().equals(ConstantValues.Status_running)){
                viewHolder.txt_status.setTextColor(getResources().getColor(R.color.dashboard_threbutton_color));

            }else if(filtered_rv_goallist.get(i).getStatus().equals(ConstantValues.Status_not_started)){
                viewHolder.txt_status.setTextColor(getResources().getColor(R.color.blue));

            }else if(filtered_rv_goallist.get(i).getStatus().equals(ConstantValues.Status_finished)){
                viewHolder.txt_status.setTextColor(getResources().getColor(R.color.green_light));
            }

            viewHolder.lnr_main_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    *//*Intent myint = new Intent(getContext(),Activity_UpdateGoal.class);
                    myint.putExtra("goal_detail",filtered_rv_goallist.get(i));
                    startActivity(myint);*//*
                    selected_index_horizontal_rv=i;
                    goal_id =filtered_rv_goallist.get(i).getId();
                    goal_startdate=filtered_rv_goallist.get(i).getStart_Deadline();
                    goal_enddate=filtered_rv_goallist.get(i).getEnd_deadline();
                   adapter.notifyDataSetChanged();;
                  // get_tasklist(filtered_rv_goallist.get(i).getId());
                }
            });


            if(selected_index_horizontal_rv==i){
                viewHolder.lnr_main_container.setBackground(getResources().getDrawable(R.drawable.four_corner_round_green_10));
            }else{
                viewHolder.lnr_main_container.setBackground(getResources().getDrawable(R.drawable.four_corner_round_white_10));
            }


        }


        @Override
        public int getItemCount() {
            return filtered_rv_goallist.size();

        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


    }
    public class ViewHolder_GoalList extends RecyclerView.ViewHolder {



        TextView txt_goalname,txt_status,txt_deadline;
        LinearLayout lnr_main_container;
        public ViewHolder_GoalList(View convertView) {
            super(convertView);


            lnr_main_container=convertView.findViewById(R.id.lnr_top_container);
            txt_goalname=convertView.findViewById(R.id.txt_goalname);
            txt_status=convertView.findViewById(R.id.txt_goal_status);
            txt_deadline=convertView.findViewById(R.id.txt_goaldeadline);
        }

    }
*/

    public class RV_Adapter_TaskList extends RecyclerView.Adapter<ViewHolder_TaskList>  {



        ArrayList<Model_GoalList.TaskDetail> adapterlist = new ArrayList<>();
        String is_finished ;

        public RV_Adapter_TaskList(ArrayList<Model_GoalList.TaskDetail> pendingorder,String is_finished) {

            this.adapterlist=pendingorder;
            this.is_finished=is_finished;

        }

        @Override
        public ViewHolder_TaskList onCreateViewHolder(ViewGroup parent, int viewType) {


            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single__row_dashboard_all_list, parent, false);
            ViewHolder_TaskList viewHolder = new ViewHolder_TaskList(itemLayoutView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder_TaskList viewHolder, final int i) {


            viewHolder.txt_taskname.setText(adapterlist.get(i).getTaskname());
            viewHolder.txt_frequency.setText(adapterlist.get(i).getTask_frequecy());;
            viewHolder.txt_deadline.setText(""+adapterlist.get(i).getCreated_date());
            viewHolder.txt_goalname.setText(adapterlist.get(i).getGoalname());

            if(is_finished.equals(ConstantValues.Incomplated)) {
                viewHolder.txt_status.setText("Running");
                viewHolder.txt_status.setTextColor(getResources().getColor(R.color.ornage_light));

            }
             else if(is_finished.equals(ConstantValues.Complated)) {
                viewHolder.txt_status.setText("Finished");
                viewHolder.txt_status.setTextColor(getResources().getColor(R.color.green));
                viewHolder.img_tick.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.tasklist_greentick));

            }else if(is_finished.equals(ConstantValues.Status_Missed)){
                viewHolder.txt_status.setText("Missed");
                viewHolder.txt_status.setTextColor(getResources().getColor(R.color.red));
                viewHolder.img_tick.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.tasklist_nuntick));
                viewHolder.img_tick.setVisibility(View.GONE);
               // viewHolder.txt_deadline.setText("Created On: "+adapterlist.get(i).getCreated_date());

            }


            if (adapterlist.get(i).getTask_frequecy().equals(ConstantValues.Frequency_Daily)) {

                viewHolder.txt_frequency.setText("Everyday");;


            } else if (adapterlist.get(i).getTask_frequecy().equals(ConstantValues.Frequency_Weekly)) {

                viewHolder.txt_frequency.setText("Every "+adapterlist.get(i).getTask_frequecy_value());;


            } else if (adapterlist.get(i).getTask_frequecy().equals(ConstantValues.Frequency_Monthly)) {

                String gfrequeny_value = adapterlist.get(i).getTask_frequecy_value();
                String frequncy_value_array[]=gfrequeny_value.split("/");
                String date2 =frequncy_value_array[0];
                viewHolder.txt_frequency.setText("Every "+date2+" of the Month");;


            } else if (adapterlist.get(i).getTask_frequecy().equals(ConstantValues.Frequency_OneTime)) {

                viewHolder.txt_frequency.setText("at "+adapterlist.get(i).getTask_frequecy_value());;

            }


            if(adapterlist.get(i).getShow().equals("1")){
                viewHolder.txt_goalname.setVisibility(View.GONE);
            }else{
                viewHolder.txt_goalname.setVisibility(View.VISIBLE);
            }

            /*if(adapterlist.get(i).getTaskstatus().equals(ConstantValues.Status_running)){
            }else if(adapterlist.get(i).getTaskstatus().equals(ConstantValues.Status_not_started)){
                viewHolder.txt_status.setTextColor(getResources().getColor(R.color.blue));
            }else if(adapterlist.get(i).getTaskstatus().equals(ConstantValues.Status_finished)){
                viewHolder.txt_status.setTextColor(getResources().getColor(R.color.green));
            }*/


            viewHolder.txt_taskname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myint = new Intent(getContext(),Activity_UpdateTask.class);
                    myint.putExtra("taskdetail", adapterlist.get(i));
                    myint.putExtra("goal_id",adapterlist.get(i).getGoalid());
                    myint.putExtra("mindate",adapterlist.get(i).getStart_deadline());
                    myint.putExtra("maxdate",adapterlist.get(i).getEnd_deadline());
                  //  startActivity(myint);
                }
            });

            viewHolder.img_tick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(is_finished.equals(ConstantValues.Incomplated)) {
                        viewHolder.img_tick.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.tasklist_greentick));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                dbhelper.update_daily_task_id_db(currentdate,adapterlist.get(i).getId(),adapterlist.get(i).getTaskid());
                                daily_tasklist_from_db.clear();;
                                daily_tasklist_from_db = dbhelper.get_daily_tasklist_runnig(currentdate);
                                if (daily_tasklist_from_db != null && daily_tasklist_from_db.size() > 0) {
                                    adapter_tasklist = new RV_Adapter_TaskList(daily_tasklist_from_db, ConstantValues.Incomplated);
                                    rv_vertical_tasklist.setLayoutManager(new LinearLayoutManager(getContext()));
                                    rv_vertical_tasklist.setAdapter(adapter_tasklist);
                                    notifyDataSetChanged();
                                    rv_vertical_tasklist.setVisibility(View.VISIBLE);
                                    txt_no_task_label.setVisibility(View.GONE);


                                } else {
                                    rv_vertical_tasklist.setVisibility(View.GONE);
                                    txt_no_task_label.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "No  Daily TaskList Found For Today", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }, 500);
                    }else{
                        Toast.makeText(getContext(), "Task Is Complated", Toast.LENGTH_SHORT).show();

                    }


                }
            });

        }


        @Override
        public int getItemCount() {
            return daily_tasklist_from_db.size();

        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }



    }
    public class ViewHolder_TaskList extends RecyclerView.ViewHolder {



        TextView txt_taskname,txt_status,txt_frequency,txt_deadline,txt_goalname;
        ImageView img_tick;
        public ViewHolder_TaskList(View convertView) {
            super(convertView);


            txt_taskname=convertView.findViewById(R.id.txt_title);
            img_tick=convertView.findViewById(R.id.img_tick);
            txt_status=convertView.findViewById(R.id.txt_status);
            txt_frequency=convertView.findViewById(R.id.txt_frequeny);
            txt_deadline=convertView.findViewById(R.id.txt_deadline);
            txt_goalname=convertView.findViewById(R.id.goalname);
        }

    }



    @Override
    public void onResume() {
        super.onResume();


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("dd MMM");

        SimpleDateFormat dateformate = new SimpleDateFormat("EEEE");
        DayName = dateformate.format(c);
        currentdate = df.format(c);

        String currentdatename = df2.format(c);



        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        int lastTimeStarted = settings.getInt("last_time_started", -1);
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_YEAR);
        welcometext.setText("My Today's Tasks - "+currentdatename);


        if (today != lastTimeStarted) {


            insertdata_daily_once();

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("last_time_started", today);
            editor.commit();

        }else{
            get_tasklist();
        }





    }



    void insertdata_daily_once(){

        try {

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date Date_Current = df.parse(currentdate);

            all_tasklist_from_db =dbhelper.get_tasklist();


            if(all_tasklist_from_db !=null && all_tasklist_from_db.size()>0) {

                for (int i = 0; i < all_tasklist_from_db.size(); i++) {

                    String frequency = all_tasklist_from_db.get(i).getTask_frequecy();
                    String gfrequeny_value = all_tasklist_from_db.get(i).getTask_frequecy_value();

                    String goalid = all_tasklist_from_db.get(i).getGoalid();
                    String taskid =all_tasklist_from_db.get(i).getId();
                    String taskname = all_tasklist_from_db.get(i).getTaskname();
                    String taskstatus = all_tasklist_from_db.get(i).getTaskstatus();
                    String starttime = all_tasklist_from_db.get(i).getStart_deadline();
                    String endtime = all_tasklist_from_db.get(i).getEnd_deadline();
                    String reason = all_tasklist_from_db.get(i).getReason();
                    String createddate = currentdate;
                    String isactive = all_tasklist_from_db.get(i).getIs_active();
                    String complate = all_tasklist_from_db.get(i).getIs_complate();

                    Date Date_start =df.parse(starttime);
                    Date Date_end = df.parse(endtime);

                    if (frequency.equals(ConstantValues.Frequency_Daily)) {


                        if(is_in_betweendate(Date_Current,Date_start,Date_end))
                            dbhelper.insert_dialy_task_id_db(goalid,taskid ,taskname, frequency, gfrequeny_value, taskstatus, starttime, endtime, reason, createddate, isactive, complate);

                    } else if (frequency.equals(ConstantValues.Frequency_Weekly)) {
                        if(is_in_betweendate(Date_Current,Date_start,Date_end))
                            if (DayName.equals(gfrequeny_value))
                                 dbhelper.insert_dialy_task_id_db(goalid,taskid ,taskname, frequency, gfrequeny_value, taskstatus, starttime, endtime, reason, createddate, isactive, complate);


                    } else if (frequency.equals(ConstantValues.Frequency_Monthly)) {

                        if(is_in_betweendate(Date_Current,Date_start,Date_end)){
                            String []currentdate_array=currentdate.split("/");
                            String date1 =currentdate_array[0];

                            String frequncy_value_array[]=gfrequeny_value.split("/");
                            String date2 =frequncy_value_array[0];
                            if (date1.equals(date2))
                                dbhelper.insert_dialy_task_id_db(goalid,taskid, taskname, frequency, gfrequeny_value, taskstatus, starttime, endtime, reason, createddate, isactive, complate);


                        }


                    } else if (frequency.equals(ConstantValues.Frequency_OneTime)) {
                        if(is_in_betweendate(Date_Current,Date_start,Date_end))
                             if (currentdate.equals(gfrequeny_value))
                               dbhelper.insert_dialy_task_id_db(goalid,taskid, taskname, frequency, gfrequeny_value, taskstatus, starttime, endtime, reason, createddate, isactive, complate);

                    }

                }

                get_tasklist();


            }

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }




    }
    void get_tasklist(){



        daily_tasklist_from_db.clear();;
            daily_tasklist_from_db=dbhelper.get_daily_tasklist_runnig(currentdate);

            if(daily_tasklist_from_db!=null && daily_tasklist_from_db.size()>0){
                adapter_tasklist = new RV_Adapter_TaskList(daily_tasklist_from_db,ConstantValues.Incomplated);
                rv_vertical_tasklist.setLayoutManager(new LinearLayoutManager(getContext()));
                rv_vertical_tasklist.setAdapter(adapter_tasklist);
                rv_vertical_tasklist.setVisibility(View.VISIBLE);
                txt_no_task_label.setVisibility(View.GONE);


            }else {

                rv_vertical_tasklist.setVisibility(View.GONE);
                txt_no_task_label.setVisibility(View.VISIBLE);
                txt_no_task_label.setText("No Daily Task Found !");
              //  insertdata_daily_once();
            }


    }

    boolean is_in_betweendate(Date CurrntDate,Date Startdate,Date Enddate){

        return Startdate.compareTo(CurrntDate) * CurrntDate.compareTo(Enddate)>=0;
    }

}
