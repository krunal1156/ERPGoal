package com.kachhua.goal.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kachhua.goal.R;
import com.kachhua.goal.Utility.TaskList_RecyclerItemTouchHelper;
import com.kachhua.goal.database.ConstantValues;
import com.kachhua.goal.database.DataBaseHelper;
import com.kachhua.goal.model.Model_GoalList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Activity_GoalDetail extends AppCompatActivity  implements TaskList_RecyclerItemTouchHelper.TaskListRecyclerItemTouchHelperListener {


    ArrayList<Model_GoalList.TaskDetail> filtered_rv_tasklist = new ArrayList<>();
    ArrayList<Model_GoalList.TaskDetail> rv_tasklist = new ArrayList<>();
    DataBaseHelper dbhelper;
    RV_Adapter_TaskList adapter;
    RecyclerView recyclerView;
    ImageView img_addtask,btn_back;
    TextView goal_title;
    Model_GoalList goaldetail;
    SimpleDateFormat formate= new SimpleDateFormat("dd/MM/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__goal_detail);
        dbhelper=new DataBaseHelper(getApplicationContext());
        goaldetail =(Model_GoalList)getIntent().getSerializableExtra("goal_detail");

        recyclerView = findViewById(R.id.rv_tasklist);
        img_addtask=findViewById(R.id.img_addtask);
        btn_back=findViewById(R.id.img_back_button);
        goal_title=findViewById(R.id.txt_goal_title);

        goal_title.setText("Tasks - "+goaldetail.getGoalname());




        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myint=new Intent(Activity_GoalDetail.this,Activity_CreateTask.class);
                myint.putExtra("goal_id",goaldetail.getId());
                myint.putExtra("mindate",goaldetail.getStart_Deadline());
                myint.putExtra("maxdate",goaldetail.getEnd_deadline());
                startActivity(myint);

            }
        });

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ViewHolder_TaskList) {
            String name = filtered_rv_tasklist.get(viewHolder.getAdapterPosition()).getGoalname();

            // remove the item from recycler view



            final int deletedIndex = viewHolder.getAdapterPosition();
            delete_task_dialog(deletedIndex);



        }
    }

    public class RV_Adapter_TaskList extends RecyclerView.Adapter<ViewHolder_TaskList> implements Filterable {




        public RV_Adapter_TaskList(ArrayList<Model_GoalList.TaskDetail> pendingorder) {

            filtered_rv_tasklist = pendingorder;

        }

        @Override
        public ViewHolder_TaskList onCreateViewHolder(ViewGroup parent, int viewType) {


            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_tasklist_goaldetail_recyclerview, parent, false);
            ViewHolder_TaskList viewHolder = new ViewHolder_TaskList(itemLayoutView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder_TaskList viewHolder, final int i) {


            viewHolder.txt_taskname.setText(filtered_rv_tasklist.get(i).getTaskname());
            viewHolder.txt_status.setText(filtered_rv_tasklist.get(i).getTaskstatus());
            viewHolder.txt_deadline.setText("Start : "+filtered_rv_tasklist.get(i).getStart_deadline());
            viewHolder.txt_deadline2.setText("End  : "+filtered_rv_tasklist.get(i).getEnd_deadline()+"");


           if(filtered_rv_tasklist.get(i).getTaskstatus().equals(ConstantValues.Status_InActive)){
                viewHolder.txt_status.setTextColor(getResources().getColor(R.color.gray));
            }else if(filtered_rv_tasklist.get(i).getTaskstatus().equals(ConstantValues.Status_Active)){
                viewHolder.txt_status.setTextColor(getResources().getColor(R.color.green));
            }

            if (filtered_rv_tasklist.get(i).getTask_frequecy().equals(ConstantValues.Frequency_Daily)) {

                viewHolder.txt_frequency.setText("Everyday");;


            }
            else if (filtered_rv_tasklist.get(i).getTask_frequecy().equals(ConstantValues.Frequency_Weekly)) {


                    String[] multipledays = filtered_rv_tasklist.get(i).getTask_frequecy_value().split(",");
                    if(multipledays.length>0){
                        String  sb = "";
                        for (int a=0;a<multipledays.length;a++){
                            sb+=multipledays[a].substring(0,3);
                            sb+=",";

                        }
                        viewHolder.txt_frequency.setText("Every "+sb);

                    }


            }
            else if (filtered_rv_tasklist.get(i).getTask_frequecy().equals(ConstantValues.Frequency_Monthly)) {

                String gfrequeny_value = filtered_rv_tasklist.get(i).getTask_frequecy_value();
                String frequncy_value_array[]=gfrequeny_value.split("/");
                String date2 =frequncy_value_array[0];
                viewHolder.txt_frequency.setText("Every "+date2+" of the Month");;


            }
            else if (filtered_rv_tasklist.get(i).getTask_frequecy().equals(ConstantValues.Frequency_OneTime)) {

                viewHolder.txt_frequency.setText("at "+filtered_rv_tasklist.get(i).getTask_frequecy_value());;

            }





            try {
                Date Cureent = Calendar.getInstance().getTime();

                String str_currentdate = formate.format(Cureent);
                String str_startdate=filtered_rv_tasklist.get(i).getStart_deadline();
                String str_enddate=filtered_rv_tasklist.get(i).getEnd_deadline();


                Date Date_Current = formate.parse(str_currentdate);
                Date Date_Start = formate.parse(str_startdate);
                Date Date_end=formate.parse(str_enddate);

                if(is_in_betweendate(Date_Current,Date_Start,Date_end)){
                    viewHolder.txt_current_status.setText("Running");
                    viewHolder.txt_current_status.setTextColor(getResources().getColor(R.color.dark_ground));
                }else{

                    if(Date_Current.before(Date_Start) ){
                        viewHolder.txt_current_status.setText("Not Started");
                        viewHolder.txt_current_status.setTextColor(getResources().getColor(R.color.blue));

                    }else if(Date_Current.after(Date_end)){
                        viewHolder.txt_current_status.setText("Finished");
                        viewHolder.txt_current_status.setTextColor(getResources().getColor(R.color.green));

                    }

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }




            viewHolder.txt_taskname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myint = new Intent(Activity_GoalDetail.this,Activity_UpdateTask.class);
                    myint.putExtra("taskdetail",filtered_rv_tasklist.get(i));
                    myint.putExtra("goal_id",goaldetail.getId());
                    myint.putExtra("mindate",goaldetail.getStart_Deadline());
                    myint.putExtra("maxdate",goaldetail.getEnd_deadline());
                    startActivity(myint);
                }
            });

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



                            if(row.taskname!=null){
                                if (row.taskname.equals(charSequence)) {
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
                    Collections.reverse(filtered_rv_tasklist);

                    notifyDataSetChanged();



                }
            };
        }

    }
    public class ViewHolder_TaskList extends RecyclerView.ViewHolder {



        TextView txt_taskname,txt_status,txt_frequency,txt_deadline,txt_deadline2,txt_current_status;
        public RelativeLayout viewForeground;
        public ViewHolder_TaskList(View convertView) {
            super(convertView);


            txt_taskname=convertView.findViewById(R.id.txt_title);
            txt_status=convertView.findViewById(R.id.txt_status);
            txt_frequency=convertView.findViewById(R.id.txt_frequeny);
            txt_deadline=convertView.findViewById(R.id.txt_deadline);
            viewForeground=convertView.findViewById(R.id.view_foreground);
            txt_current_status=convertView.findViewById(R.id.txt_current_status);
            txt_deadline2=convertView.findViewById(R.id.txt_deadline2);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        filtered_rv_tasklist=dbhelper.get_tasklist(goaldetail.getId());
        if(filtered_rv_tasklist!=null && filtered_rv_tasklist.size()>0) {
            adapter = new RV_Adapter_TaskList(filtered_rv_tasklist);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new TaskList_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        }
        else{
            Toast.makeText(getApplicationContext(),"No TaskList Found In This Goal",Toast.LENGTH_SHORT).show();
        }
    }

    void delete_task_dialog(final int position){

        final Dialog dialogbox_weekly = new Dialog(Activity_GoalDetail.this);
        dialogbox_weekly.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_weekly.setContentView(R.layout.dialogbox_delete_item);
        dialogbox_weekly. getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout lnr_no = dialogbox_weekly.findViewById(R.id.lnr_no);
        LinearLayout lnr_yes =dialogbox_weekly.findViewById(R.id.lnr_yes);
        TextView txttitle= dialogbox_weekly.findViewById(R.id.txt_title);

        txttitle.setText("Delete Task ");
        lnr_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbox_weekly.dismiss();
            }
        });


        lnr_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbhelper.delete_task(filtered_rv_tasklist.get(position).getId());
                filtered_rv_tasklist.remove(position);
                adapter.notifyDataSetChanged();
                // adapter.removeItem(viewHolder.getAdapterPosition());

                dialogbox_weekly.dismiss();
            }
        });

        dialogbox_weekly.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_weekly.show();




    }

    boolean is_in_betweendate(Date CurrntDate, Date Startdate, Date Enddate){

        return Startdate.compareTo(CurrntDate) * CurrntDate.compareTo(Enddate)>=0;
    }
}
