package com.kachhua.goal.Fragments;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kachhua.goal.R;
import com.kachhua.goal.database.DataBaseHelper;
import com.kachhua.goal.model.Model_GoalList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.kachhua.goal.Activity.Activity_Main.welcometext;

public class Fragment_ForceReport extends Fragment {
    ArrayList<Model_GoalList.TaskDetail> filtered_rv_tasklist = new ArrayList<>();

    RecyclerView recyclerView;
    RV_Adapter_TaskList adapter;
    ImageView img_creategoal;
    DataBaseHelper dbhelper;
    TextView txt_no_task_label;
    String currentdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_force_report, container, false);
        dbhelper=new DataBaseHelper(getContext());
        recyclerView=view.findViewById(R.id.recyclerview);
        txt_no_task_label=view.findViewById(R.id.txt_no_taskl_label);




        return view;
    }


    public class RV_Adapter_TaskList extends RecyclerView.Adapter<ViewHolder_TaskList>  {




        public RV_Adapter_TaskList(ArrayList<Model_GoalList.TaskDetail> pendingorder) {

            filtered_rv_tasklist = pendingorder;

        }

        @Override
        public ViewHolder_TaskList onCreateViewHolder(ViewGroup parent, int viewType) {


            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_force_report_recyclerview, parent, false);
            ViewHolder_TaskList viewHolder = new ViewHolder_TaskList(itemLayoutView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder_TaskList viewHolder, final int i) {


            viewHolder.txt_taskname.setText(filtered_rv_tasklist.get(i).getTaskname());
            viewHolder.txt_deadline.setText(filtered_rv_tasklist.get(i).getCreated_date());

            if(filtered_rv_tasklist.get(i).getReason()!=null)
                viewHolder.edt_reason.setText(filtered_rv_tasklist.get(i).getReason());


            viewHolder.img_tick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        viewHolder.img_tick.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.tasklist_greentick));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            complate_goal_dialog(i);

                            }
                        }, 500);

                }
            });

            viewHolder.txt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String reasontext= viewHolder.edt_reason.getText().toString();
                    if(reasontext!=null && !reasontext.equals("")){
                        submit_reason_goal_dialog(i,reasontext);
                    }else{
                        Toast.makeText(getContext(),"Please Enter Reason",Toast.LENGTH_SHORT).show();
                    }

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



    }
    public class ViewHolder_TaskList extends RecyclerView.ViewHolder {



        TextView txt_taskname,txt_deadline,txt_submit;
        EditText edt_reason;
        ImageView img_tick;
        public ViewHolder_TaskList(View convertView) {
            super(convertView);


            txt_taskname=convertView.findViewById(R.id.txt_goalname);
            img_tick=convertView.findViewById(R.id.img_tick);
            edt_reason=convertView.findViewById(R.id.edt_reason);
            txt_deadline=convertView.findViewById(R.id.txt_goal_end_deadline);
            txt_submit=convertView.findViewById(R.id.txt_submit);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
         currentdate = df.format(c);
        welcometext.setText("Missed Tasks!");


        filtered_rv_tasklist=dbhelper.get_daily_tasklist_missed();
        if(filtered_rv_tasklist!=null && filtered_rv_tasklist.size()>0){
            adapter = new RV_Adapter_TaskList(filtered_rv_tasklist);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            txt_no_task_label.setVisibility(View.GONE);


        }else {
            recyclerView.setVisibility(View.GONE);
            txt_no_task_label.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(),"No  Daily TaskList Found For Today",Toast.LENGTH_SHORT).show();

        }


    }

    void complate_goal_dialog(final int i){

        final Dialog dialogbox_weekly = new Dialog(getContext());
        dialogbox_weekly.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_weekly.setContentView(R.layout.dialogbox_delete_item);
        dialogbox_weekly. getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout lnr_no = dialogbox_weekly.findViewById(R.id.lnr_no);
        LinearLayout lnr_yes =dialogbox_weekly.findViewById(R.id.lnr_yes);
        TextView txttitle = dialogbox_weekly.findViewById(R.id.txt_title);

        txttitle.setText("Submit Task!");

        lnr_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbox_weekly.dismiss();
            }
        });


        lnr_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogbox_weekly.dismiss();
                dbhelper.update_daily_task_id_db(currentdate,filtered_rv_tasklist.get(i).getId(),filtered_rv_tasklist.get(i).getTaskid());
                filtered_rv_tasklist.clear();;
                filtered_rv_tasklist = dbhelper.get_daily_tasklist_missed();
                if (filtered_rv_tasklist != null && filtered_rv_tasklist.size() > 0) {
                    adapter = new RV_Adapter_TaskList(filtered_rv_tasklist);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    txt_no_task_label.setVisibility(View.GONE);


                } else {
                    recyclerView.setVisibility(View.GONE);
                    txt_no_task_label.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "No  Daily TaskList Found For Today", Toast.LENGTH_SHORT).show();

                }

            }
        });

        dialogbox_weekly.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_weekly.show();




    }

    void submit_reason_goal_dialog(final int i,final String reasontext){

        final Dialog dialogbox_weekly = new Dialog(getContext());
        dialogbox_weekly.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_weekly.setContentView(R.layout.dialogbox_delete_item);
        dialogbox_weekly. getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout lnr_no = dialogbox_weekly.findViewById(R.id.lnr_no);
        LinearLayout lnr_yes =dialogbox_weekly.findViewById(R.id.lnr_yes);
        TextView txttitle = dialogbox_weekly.findViewById(R.id.txt_title);

        txttitle.setText("Submit Task!");

        lnr_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbox_weekly.dismiss();
            }
        });


        lnr_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogbox_weekly.dismiss();
                dbhelper.update_reason_daily_task_id_db(filtered_rv_tasklist.get(i).getId(),reasontext);
                filtered_rv_tasklist.clear();;
                filtered_rv_tasklist = dbhelper.get_daily_tasklist_missed();
                if (filtered_rv_tasklist != null && filtered_rv_tasklist.size() > 0) {
                    adapter = new RV_Adapter_TaskList(filtered_rv_tasklist);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    txt_no_task_label.setVisibility(View.GONE);


                } else {
                    recyclerView.setVisibility(View.GONE);
                    txt_no_task_label.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "No  Daily TaskList Found For Today", Toast.LENGTH_SHORT).show();

                }
            }
        });

        dialogbox_weekly.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_weekly.show();




    }

}
