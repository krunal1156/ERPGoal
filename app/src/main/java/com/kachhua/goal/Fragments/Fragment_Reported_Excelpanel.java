package com.kachhua.goal.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kachhua.goal.R;
import com.kachhua.goal.database.ConstantValues;
import com.kachhua.goal.database.DataBaseHelper;
import com.kachhua.goal.excelpanel.CustomAdapter;
import com.kachhua.goal.excelpanel.bean.Cell;
import com.kachhua.goal.excelpanel.bean.ColTitle;
import com.kachhua.goal.excelpanel.bean.RowTitle;
import com.kachhua.goal.model.Model_GoalList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.zhouchaoyuan.excelpanel.ExcelPanel;

import static com.kachhua.goal.Activity.Activity_Main.welcometext;

public class Fragment_Reported_Excelpanel extends Fragment implements ExcelPanel.OnLoadMoreListener {


    String currentdate,DayName,loopdate;
    public static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    public static final String WEEK_FORMAT_PATTERN = "EEEE";
    public static final String[] CHANNEL = {"aajtak", "abp", "zee", "india tv", "republic"};
    public static final String[] NAME = {"reporter 1", "reporter 2", "reporter 3", "reporter 4", "reporeter 5", "reporter 6", "reporter 7"};
    public static final long ONE_DAY = 24 * 3600 * 1000;
    //public static final int PAGE_SIZE = 10;
    public  int PAGE_SIZE = 0;
    public ArrayList<String>calenderdates = new ArrayList<>();
    public ArrayList<String>calenderdays = new ArrayList<>();
    private ExcelPanel excelPanel;
    private ProgressBar progress;
    private CustomAdapter adapter;
    private List<RowTitle> rowTitles;
    private List<ColTitle> colTitles;
    private List<List<Cell>> cells;
    private SimpleDateFormat dateFormatPattern;
    private SimpleDateFormat weekFormatPattern;
    private boolean isLoading;
    private long historyStartTime;
    private long moreStartTime;

    ArrayList<Model_GoalList.TaskDetail> alltasklist =new ArrayList<>();
   // ArrayList<Model_GoalList.TaskDetail> final_tasklist =new ArrayList<>();
    DataBaseHelper dataBaseHelper;
    TextView txt_label;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_report_excellayout, container, false);
        dataBaseHelper = new DataBaseHelper(getContext());
        alltasklist = dataBaseHelper.get_tasklist_reportpage();


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        SimpleDateFormat weekformate = new SimpleDateFormat(WEEK_FORMAT_PATTERN);
        DayName = weekformate.format(c);
        currentdate = dateformat.format(c);

        //String currentdatename = df2.format(c);


        progress = (ProgressBar) root.findViewById(R.id.progress);
        excelPanel = (ExcelPanel) root.findViewById(R.id.content_container);
        txt_label=(TextView)root.findViewById(R.id.txtlable);
        adapter = new CustomAdapter(getActivity(), blockListener,strtdate_listener,enddate_listener);

        excelPanel.setAdapter(adapter);
        excelPanel.setOnLoadMoreListener(this);
        excelPanel.addOnScrollListener(onScrollListener);


        if(alltasklist!=null && alltasklist.size()>0){
            initData();
            txt_label.setVisibility(View.GONE);
        }else{
            txt_label.setVisibility(View.VISIBLE);
        }


        return root;
    }


   @Override
    public void onResume() {
        super.onResume();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        currentdate = df.format(c);
        welcometext.setText("Reported Tasks!");





    }

    private ExcelPanel.OnScrollListener onScrollListener = new ExcelPanel.OnScrollListener() {
        @Override
        public void onScrolled(ExcelPanel excelPanel, int dx, int dy) {
            super.onScrolled(excelPanel, dx, dy);
            Log.e("acjiji", dx + "     " + dy);
        }
    };

    private View.OnClickListener blockListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Cell cell = (Cell) view.getTag();
            if (cell != null) {
                if (cell.getStatus() == 0) {
                    //Toast.makeText(getActivity(), "cell status 0", Toast.LENGTH_SHORT).show();
                } else if (cell.getStatus() == 1) {
                    //Toast.makeText(getActivity(), "cell status 1" + cell.getBookingName(), Toast.LENGTH_SHORT).show();
                } else if (cell.getStatus() == 2) {
                    String message = cell.getChannelName();
                    submit_reason_goal_dialog(message);
                    //Toast.makeText(getActivity(), "cell status 2" + cell.getBookingName(), Toast.LENGTH_SHORT).show();
                } else if (cell.getStatus() == 3) {
                   // Toast.makeText(getActivity(), "cell status 4：" + cell.getBookingName(), Toast.LENGTH_SHORT).show();

                }
            }
        }
    };


    private View.OnClickListener strtdate_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"Start date clicked",Toast.LENGTH_SHORT).show();
            CustomAdapter.txtstartdate.setText("01/02/2020");
        }
    };

    private View.OnClickListener enddate_listener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"end date clicked",Toast.LENGTH_SHORT).show();
            CustomAdapter.txtenddate.setText("10/02/2020");

        }
    };

    @Override
    public void onLoadMore() {
        if (!isLoading) {
           // loadData(moreStartTime, false);
        }
    }

    @Override
    public void onLoadHistory() {
        if (!isLoading) {
            loadData(historyStartTime, true);
        }
    }

    private void initData() {
        moreStartTime = Calendar.getInstance().getTimeInMillis();
        historyStartTime = moreStartTime - ONE_DAY * PAGE_SIZE;
        dateFormatPattern = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        weekFormatPattern = new SimpleDateFormat(WEEK_FORMAT_PATTERN);
        rowTitles = new ArrayList<>();
        colTitles = new ArrayList<>();
        cells = new ArrayList<>();
        for (int i = 0; i < alltasklist.size(); i++) {
            cells.add(new ArrayList<Cell>());
        }


        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println(c.getTime());
         moreStartTime =c.getTimeInMillis();

        loadData(moreStartTime, false);
    }

    private void loadData(long startTime, final boolean history) {
        //模拟网络加载
        isLoading = true;
        Message message = new Message();
        message.arg1 = history ? 1 : 2;
        message.obj = new Long(startTime);
        loadDataHandler.sendMessageDelayed(message, 1000);
    }

    private Handler loadDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isLoading = false;
            long startTime = (Long) msg.obj;
            List<RowTitle> rowTitles1 = genRowData(startTime);
            List<List<Cell>> cells1 = genCellData();
            if (msg.arg1 == 1) {//history
                historyStartTime -= ONE_DAY * PAGE_SIZE;
                rowTitles.addAll(0, rowTitles1);
                for (int i = 0; i < cells1.size(); i++) {
                    cells.get(i).addAll(0, cells1.get(i));
                }

                //加载了数据之后偏移到上一个位置去
                if (excelPanel != null) {
                    excelPanel.addHistorySize(PAGE_SIZE);
                }
            } else {
                moreStartTime += ONE_DAY * PAGE_SIZE;
                rowTitles.addAll(rowTitles1);
                for (int i = 0; i < cells1.size(); i++) {
                    cells.get(i).addAll(cells1.get(i));
                }
            }
            if (colTitles.size() == 0) {
                colTitles.addAll(genColData());
            }
            progress.setVisibility(View.GONE);
            adapter.setAllData(colTitles, rowTitles, cells);
           // adapter.enableFooter();
           // adapter.enableHeader();
        }
    };

    //====================================模拟生成数据==========================================
    private List<RowTitle> genRowData(long startTime) {
        List<RowTitle> rowTitles = new ArrayList<>();

        calenderdates.clear();
            do{

                RowTitle rowTitle = new RowTitle();
                rowTitle.setDateString(dateFormatPattern.format(startTime + PAGE_SIZE * ONE_DAY));
                rowTitle.setWeekString(weekFormatPattern.format(startTime + PAGE_SIZE * ONE_DAY));
                rowTitles.add(rowTitle);

                loopdate = dateFormatPattern.format(startTime+PAGE_SIZE*ONE_DAY);
                calenderdates.add(dateFormatPattern.format(startTime + PAGE_SIZE * ONE_DAY));
                calenderdays.add(weekFormatPattern.format(startTime + PAGE_SIZE * ONE_DAY));
                PAGE_SIZE++;

            }while (!loopdate.equals(currentdate));

        return rowTitles;
    }

    private List<ColTitle> genColData() {
        List<ColTitle> colTitles = new ArrayList<>();


        for (int i = 0; i < alltasklist.size(); i++) {
            ColTitle colTitle = new ColTitle();
            colTitle.setTasktitle(alltasklist.get(i).getTaskname());
            colTitle.setGoalname(alltasklist.get(i).getGoalname());
            colTitles.add(colTitle);
        }
        return colTitles;
    }

    private List<List<Cell>> genCellData() {
        List<List<Cell>> cells = new ArrayList<>();


        for (int i = 0; i < alltasklist.size(); i++) {
            List<Cell> cellList = new ArrayList<>();
            cells.add(cellList);

            String taskid=alltasklist.get(i).getId();
            String frequency =alltasklist.get(i).getTask_frequecy();
            String frequency_value = alltasklist.get(i).getTask_frequecy_value();
            String finished_date =alltasklist.get(i).getFinished_date();



            ArrayList<Model_GoalList.TaskDetail>daily_tasklist_by_Taskids =dataBaseHelper.get_all_daily_tasklist_by_taskid(taskid);


            for (int j = 0; j < calenderdates.size(); j++) {
                        String calenderdate =calenderdates.get(j);
                         int status=0;
                         Cell cell = new Cell();

                               if(daily_tasklist_by_Taskids!=null && daily_tasklist_by_Taskids.size()>0)
                                for(int k=0;k<daily_tasklist_by_Taskids.size();k++){

                                    String daily_task_createddate = daily_tasklist_by_Taskids.get(k).getCreated_date();
                                    String taskcomplate =daily_tasklist_by_Taskids.get(k).getIs_complate();

                                    if(calenderdate.equals(daily_task_createddate)){
                                        if(taskcomplate.equals(ConstantValues.Complated)){
                                            status=1;
                                        }else if(taskcomplate.equals(ConstantValues.Status_Reported)){
                                            status=2;
                                            cell.setChannelName(daily_tasklist_by_Taskids.get(k).getReason());

                                        }else{
                                            status=3;

                                        }

                                        if(currentdate.equals(daily_task_createddate)){
                                            if(taskcomplate.equals(ConstantValues.Incomplated))
                                                status=4;

                                            }
                                        break;
                                    }
                                }



                            cell.setStatus(status);
                            cellList.add(cell);
                        }
        }
        return cells;
    }

    void submit_reason_goal_dialog( String reasontext){

        final Dialog dialogbox_weekly = new Dialog(getContext());
        dialogbox_weekly.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogbox_weekly.setContentView(R.layout.dialogbox_reported_message);
        dialogbox_weekly. getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout lnr_yes =dialogbox_weekly.findViewById(R.id.lnr_yes);
        TextView txttitle = dialogbox_weekly.findViewById(R.id.txt_msgid);

        txttitle.setText(reasontext);

        lnr_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogbox_weekly.dismiss();

            }
        });

        dialogbox_weekly.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_weekly.show();




    }

}