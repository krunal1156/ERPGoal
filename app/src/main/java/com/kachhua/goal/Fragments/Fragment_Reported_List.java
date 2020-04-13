package com.kachhua.goal.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kachhua.goal.Activity.Activity_CreateGoal;
import com.kachhua.goal.R;
import com.kachhua.goal.database.DataBaseHelper;
import com.kachhua.goal.model.Model_GoalList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;

import static com.kachhua.goal.Activity.Activity_Main.welcometext;

public class Fragment_Reported_List extends Fragment {
    ArrayList<Model_GoalList.TaskDetail> filtered_rv_tasklist = new ArrayList<>();
    ArrayList<Model_GoalList.TaskDetail> rv_tasklist = new ArrayList<>();

    RecyclerView recyclerView;
    RV_Adapter_TaskList adapter;
    ImageView img_creategoal;
    DataBaseHelper dbhelper;
    TextView txt_no_task_label;
    String currentdate,startdate,enddate;
    Calendar currentcalender, startcalender, endcalender;
    Date Date_start, Date_end;

    HorizontalCalendar horizontalCalendar;
    TextView txt_startdate,txt_enddate;
    View mainvuew;

    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        mainvuew= view;
        dbhelper=new DataBaseHelper(getContext());
        recyclerView=view.findViewById(R.id.recyclerview);
        txt_no_task_label=view.findViewById(R.id.txt_no_taskl_label);
        txt_startdate=view.findViewById(R.id.txt_startdate);
        txt_enddate=view.findViewById(R.id.txt_enddate);



        custom_calender_init();


        txt_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                action_startdeadline();
            }
        });

        txt_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action_enddeadline();
            }
        });
        return view;
    }


    public class RV_Adapter_TaskList extends RecyclerView.Adapter<ViewHolder_TaskList> implements Filterable {




        public RV_Adapter_TaskList(ArrayList<Model_GoalList.TaskDetail> pendingorder) {

            filtered_rv_tasklist = pendingorder;

        }

        @Override
        public ViewHolder_TaskList onCreateViewHolder(ViewGroup parent, int viewType) {


            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_report_recyclerview, parent, false);
            ViewHolder_TaskList viewHolder = new ViewHolder_TaskList(itemLayoutView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder_TaskList viewHolder, final int i) {


            viewHolder.txt_taskname.setText(filtered_rv_tasklist.get(i).getTaskname());

        }


        @Override
        public int getItemCount() {
            return filtered_rv_tasklist.size();

        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        filtered_rv_tasklist = rv_tasklist;

                    } else {
                        ArrayList<Model_GoalList.TaskDetail> filteredList = new ArrayList<>();
                        for (Model_GoalList.TaskDetail row : rv_tasklist) {



                            if(row.goalname!=null){
                                if (row.getCreated_date().equals(charSequence)) {
                                    filteredList.add(row);
                                }
                            }




                        }

                        filtered_rv_tasklist = filteredList;

                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filtered_rv_tasklist;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    filtered_rv_tasklist = (ArrayList<Model_GoalList.TaskDetail>) filterResults.values;
                   // Collections.reverse(filtered_rv_tasklist);

                    notifyDataSetChanged();



                }
            };

        }
    }
    public class ViewHolder_TaskList extends RecyclerView.ViewHolder {



        TextView txt_taskname;
        public ViewHolder_TaskList(View convertView) {
            super(convertView);
            txt_taskname=convertView.findViewById(R.id.txt_goalname);

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        currentdate = df.format(c);
        welcometext.setText("Reported Tasks!");


        rv_tasklist=dbhelper.get_daily_tasklist_runnig(currentdate);
        filtered_rv_tasklist=rv_tasklist;
        if(filtered_rv_tasklist!=null && filtered_rv_tasklist.size()>0){
            adapter = new RV_Adapter_TaskList(filtered_rv_tasklist);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            txt_no_task_label.setVisibility(View.GONE);


        }else {
            recyclerView.setVisibility(View.GONE);
            txt_no_task_label.setVisibility(View.VISIBLE);
          //  Toast.makeText(getContext(),"No  Daily TaskList Found For Today",Toast.LENGTH_SHORT).show();

        }


    }

    void custom_calender_init(){


        try {





            currentcalender = Calendar.getInstance();

            startcalender = Calendar.getInstance();
           // startcalender.setTime(Date_start);
            startcalender.add(Calendar.DAY_OF_WEEK, -1);

            endcalender = Calendar.getInstance();
            //endcalender.setTime(Date_end);
            endcalender.add(Calendar.DAY_OF_WEEK, 10);

            horizontalCalendar = new HorizontalCalendar.Builder(mainvuew, R.id.calendarView)
                    .range(startcalender, endcalender)
                    .datesNumberOnScreen(8)
                    .mode(HorizontalCalendar.Mode.DAYS)
                    .configure()
                    //.formatMiddleText("MMM")
                    //.formatBottomText("yyyy")
                    //.showTopText(false)
                    .showBottomText(false)
                    .selectorColor(getResources().getColor(R.color.trans))
                    .textSize(8, 11, 8)
                    .end()
                    .build();


            horizontalCalendar.selectDate(currentcalender, false);
            horizontalCalendar.setCalendarListener(new devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener() {
                @Override
                public void onDateSelected(Calendar date, int position) {


                    String selecteddate =df.format(date.getTime());
                    adapter.getFilter().filter(selecteddate);



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

        }catch (Exception e){

        }
    }

    void custom_calender_modified(){


        try {




            Date_start = df.parse(startdate);
            Date_end = df.parse(enddate);


            currentcalender = Calendar.getInstance();

            startcalender = Calendar.getInstance();
            startcalender.setTime(Date_start);
            startcalender.add(Calendar.DAY_OF_WEEK, 0);

            endcalender = Calendar.getInstance();
            endcalender.setTime(Date_end);
            endcalender.add(Calendar.DAY_OF_WEEK, 0);


            horizontalCalendar.setRange(startcalender,endcalender);
            horizontalCalendar.refresh();;


        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }


    void action_startdeadline(){

            Calendar myCalendar = Calendar.getInstance();
            Calendar calendar_max = Calendar.getInstance();
            calendar_max.add(Calendar.DATE, 0);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    try {
                        String daynum = "";
                        if (dayOfMonth < 10)
                            daynum = "0" + String.valueOf(dayOfMonth);
                        else
                            daynum = String.valueOf(dayOfMonth);

                        String monthnum = "";
                        if (month + 1 < 10)
                            monthnum = "0" + String.valueOf(month + 1);
                        else
                            monthnum = String.valueOf(month + 1);

                        startdate = daynum + "/" + String.valueOf(monthnum) + "/" + String.valueOf(year);
                        //=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                        txt_startdate.setText("start :" + startdate);

                        Date_start = df.parse(startdate);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(calendar_max.getTimeInMillis());
            datePickerDialog.show();

    }

    void action_enddeadline(){
        Calendar myCalendar= Calendar.getInstance();
        Calendar calendar_max = Calendar.getInstance();
        calendar_max.add(Calendar.DATE, 0);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    String daynum = "";
                    if (dayOfMonth < 10)
                        daynum = "0" + String.valueOf(dayOfMonth);
                    else
                        daynum = String.valueOf(dayOfMonth);


                    String monthnum = "";
                    if (month + 1 < 10)
                        monthnum = "0" + String.valueOf(month + 1);
                    else
                        monthnum = String.valueOf(month + 1);

                    enddate = daynum + "/" + String.valueOf(monthnum) + "/" + String.valueOf(year);
                    // String  to_date =String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                    txt_enddate.setText("end: " + enddate);

                    Date_end = df.parse(enddate);


                     if(!Date_end.before(Date_start))
                            custom_calender_modified();
                     else
                         Toast.makeText(getContext(),"Please select Date after start date",Toast.LENGTH_SHORT).show();
                }catch (Exception e){

                }
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(calendar_max.getTimeInMillis());
        datePickerDialog.show();
    }

}
