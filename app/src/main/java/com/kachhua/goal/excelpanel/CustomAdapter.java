package com.kachhua.goal.excelpanel;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kachhua.goal.R;
import com.kachhua.goal.excelpanel.bean.Cell;
import com.kachhua.goal.excelpanel.bean.ColTitle;
import com.kachhua.goal.excelpanel.bean.RowTitle;

import cn.zhouchaoyuan.excelpanel.BaseExcelPanelAdapter;

/**
 * Created by zhouchaoyuan on 2017/1/14.
 */

public class CustomAdapter extends BaseExcelPanelAdapter<RowTitle, ColTitle, Cell> {

    private Context context;
    private View.OnClickListener blockListener,startdate_listener,enddate_listener;
    public static  TextView txtstartdate,txtenddate;

    public CustomAdapter(Context context, View.OnClickListener blockListener,View.OnClickListener startdate_lisner,View.OnClickListener enddate_lisner) {
        super(context);
        this.context = context;
        this.blockListener = blockListener;
        this.startdate_listener=startdate_lisner;
        this.enddate_listener=enddate_lisner;

    }

    //=========================================content's cell===========================================
    @Override
    public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_status_normal_cell, parent, false);
        CellHolder cellHolder = new CellHolder(layout);
        return cellHolder;
    }

    @Override
    public void onBindCellViewHolder(RecyclerView.ViewHolder holder, int verticalPosition, int horizontalPosition) {
        Cell cell = getMajorItem(verticalPosition, horizontalPosition);
        if (null == holder || !(holder instanceof CellHolder) || cell == null) {
            return;
        }
        CellHolder viewHolder = (CellHolder) holder;
        viewHolder.cellContainer.setTag(cell);
        viewHolder.cellContainer.setOnClickListener(blockListener);
        if (cell.getStatus() == 0) {
            viewHolder.img_status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.not_avialble));

            //viewHolder.bookingName.setText("");
            //viewHolder.channelName.setText("");
            //viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        } else {
           // viewHolder.bookingName.setText(cell.getBookingName());
           // viewHolder.channelName.setText(cell.getChannelName());
            if (cell.getStatus() == 1) {
                viewHolder.img_status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.complated_task));
              //  viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.left));
            } else if (cell.getStatus() == 2) {
              //  viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.staying));
                viewHolder.img_status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.reported_task));
            } else if(cell.getStatus()==3){
              //  viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.booking));
                viewHolder.img_status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.missed_task));

            }else if(cell.getStatus()==4){
                //  viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.booking));
                viewHolder.img_status.setVisibility(View.GONE);

            }
        }
    }

    static class CellHolder extends RecyclerView.ViewHolder {

       // public final TextView bookingName;
        //public final TextView channelName;
        public final LinearLayout cellContainer;
        public final ImageView img_status;

        public CellHolder(View itemView) {
            super(itemView);
           // bookingName = (TextView) itemView.findViewById(R.id.booking_name);
            //channelName = (TextView) itemView.findViewById(R.id.channel_name);
            cellContainer = (LinearLayout) itemView.findViewById(R.id.pms_cell_container);
            img_status=(ImageView)itemView.findViewById(R.id.img_status);
        }
    }


    //=========================================top cell===========================================
    @Override
    public RecyclerView.ViewHolder onCreateTopViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_status_top_header_item, parent, false);
        TopHolder topHolder = new TopHolder(layout);
        return topHolder;
    }

    @Override
    public void onBindTopViewHolder(RecyclerView.ViewHolder holder, int position) {
        RowTitle rowTitle = getTopItem(position);
        if (null == holder || !(holder instanceof TopHolder) || rowTitle == null) {
            return;
        }
        TopHolder viewHolder = (TopHolder) holder;
        viewHolder.title_week.setText(rowTitle.getWeekString());
        viewHolder.title_Date.setText(rowTitle.getDateString().substring(0,8));
    }

    static class TopHolder extends RecyclerView.ViewHolder {

        public final TextView title_Date;
        public final TextView title_week;

        public TopHolder(View itemView) {
            super(itemView);
            title_Date = (TextView) itemView.findViewById(R.id.data_label);
            title_week = (TextView) itemView.findViewById(R.id.week_label);
        }
    }

    //=========================================left cell===========================================
    @Override
    public RecyclerView.ViewHolder onCreateLeftViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_status_left_header_item, parent, false);
        LeftHolder leftHolder = new LeftHolder(layout);
        return leftHolder;
    }

    @Override
    public void onBindLeftViewHolder(RecyclerView.ViewHolder holder, int position) {
        ColTitle colTitle = getLeftItem(position);
        if (null == holder || !(holder instanceof LeftHolder) || colTitle == null) {
            return;
        }
        LeftHolder viewHolder = (LeftHolder) holder;
        viewHolder.task_title.setText(colTitle.getTasktitle());
        viewHolder.txt_goalname.setText("("+colTitle.getGoalname()+")");
        //ViewGroup.LayoutParams lp = viewHolder.root.getLayoutParams();
       // viewHolder.root.setLayoutParams(lp);
        //viewHolder.root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    static class LeftHolder extends RecyclerView.ViewHolder {

        public final TextView task_title;
        public final TextView txt_goalname;
        public final View root;

        public LeftHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            task_title = (TextView) itemView.findViewById(R.id.room_number_label);
            txt_goalname = (TextView) itemView.findViewById(R.id.txt_goalname);
        }
    }

    //=========================================left-top cell===========================================
    @Override
    public View onCreateTopLeftView() {
      //  return LayoutInflater.from(context).inflate(R.layout.room_status_normal_cell, null);
        View layout = LayoutInflater.from(context).inflate(R.layout.room_status_topleft_header_item, null);
        //TopLeftHolder leftHolder = new TopLeftHolder(layout);

        //txtstartdate = layout.findViewById(R.id.txt_select_start_date);
        //txtstartdate.setText("Select start date");
        //txtstartdate.setOnClickListener(startdate_listener);


        //txtenddate = layout.findViewById(R.id.txt_select_enddate);
        //txtenddate.setText("Select end date");
        //txtenddate.setOnClickListener(enddate_listener);


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context,"topleft clicked",Toast.LENGTH_SHORT).show();
            }
        });
        return layout;
    }

}
