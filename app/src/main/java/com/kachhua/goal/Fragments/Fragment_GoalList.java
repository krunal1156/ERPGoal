package com.kachhua.goal.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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

import com.kachhua.goal.Activity.Activity_CreateGoal;
import com.kachhua.goal.Activity.Activity_GoalDetail;
import com.kachhua.goal.Activity.Activity_UpdateGoal;
import com.kachhua.goal.R;
import com.kachhua.goal.Utility.GoalList_RecyclerItemTouchHelper;
import com.kachhua.goal.database.ConstantValues;
import com.kachhua.goal.database.DataBaseHelper;
import com.kachhua.goal.model.Model_GoalList;
import java.util.ArrayList;
import java.util.Collections;

import static com.kachhua.goal.Activity.Activity_Main.welcometext;

public class Fragment_GoalList extends Fragment  implements GoalList_RecyclerItemTouchHelper.RecyclerItemTouchHelperListener
 {

    ArrayList<Model_GoalList> filtered_rv_goallist = new ArrayList<>();
    ArrayList<Model_GoalList> rv_goallist= new ArrayList<>();
    RecyclerView recyclerView;
    RV_Adapter_GoalList adapter;
    ImageView img_creategoal;
    DataBaseHelper dbhelper;
    String condition;
    LinearLayout bottomlayout;
    TextView txt_no_task_label;


    public static Fragment_GoalList newInstance(String condition) {
        Fragment_GoalList fragment = new Fragment_GoalList();
        Bundle bundle = new Bundle();
        bundle.putSerializable("condition", condition);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goallist, container, false);
        condition =(String)getArguments().getSerializable("condition");
        dbhelper=new DataBaseHelper(getContext());
        recyclerView=view.findViewById(R.id.recyclerview);
        img_creategoal =view.findViewById(R.id.img_plus);
        bottomlayout=view.findViewById(R.id.bottomlayout);
        txt_no_task_label=view.findViewById(R.id.txt_no_taskl_label);


        img_creategoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myint = new Intent(getContext(),Activity_CreateGoal.class);
                startActivity(myint);

            }
        });




        return view;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ViewHolder_Leave_Status) {
            String name = filtered_rv_goallist.get(viewHolder.getAdapterPosition()).getGoalname();

            // remove the item from recycler view



            final int deletedIndex = viewHolder.getAdapterPosition();
            delete_goal_dialog(deletedIndex);



        }
    }


    public class RV_Adapter_GoalList extends RecyclerView.Adapter<ViewHolder_Leave_Status> implements Filterable {




        public RV_Adapter_GoalList(ArrayList<Model_GoalList> pendingorder) {

           // filtered_rv_goallist = pendingorder;

        }

        @Override
        public ViewHolder_Leave_Status onCreateViewHolder(ViewGroup parent, int viewType) {


            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_goallist_recyclerview, parent, false);
            ViewHolder_Leave_Status viewHolder = new ViewHolder_Leave_Status(itemLayoutView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder_Leave_Status viewHolder, final int i) {


            viewHolder.txt_goal_cateogry.setText(filtered_rv_goallist.get(i).getCategory());
            viewHolder.txt_goalname.setText(filtered_rv_goallist.get(i).getGoalname());
            viewHolder.txt_deadline.setText("Start:"+filtered_rv_goallist.get(i).getStart_Deadline() +" End:"+filtered_rv_goallist.get(i).getEnd_deadline());
            viewHolder.txt_status.setText(filtered_rv_goallist.get(i).getStatus());

            if(filtered_rv_goallist.get(i).getIntop3().equals("True")){
                viewHolder.txt_intop.setText("In Top 3");
            }else if(filtered_rv_goallist.get(i).getIntop10().equals("True")){
                viewHolder.txt_intop.setText("In Top 10");
            }else{
                viewHolder.txt_intop.setVisibility(View.GONE);
            }

            viewHolder.txt_goal_cateogry.setTextColor(getResources().getColor(R.color.blue));

            if(filtered_rv_goallist.get(i).getStatus().equals(ConstantValues.Status_Active)){
                viewHolder.txt_status.setTextColor(getResources().getColor(R.color.green_light));

            }else if(filtered_rv_goallist.get(i).getStatus().equals(ConstantValues.Status_InActive)){
                viewHolder.txt_status.setTextColor(getResources().getColor(R.color.gray));

            }

            viewHolder.lnr_main_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myint = new Intent(getContext(),Activity_GoalDetail.class);
                    myint.putExtra("goal_detail",filtered_rv_goallist.get(i));
                    startActivity(myint);

                }
            });
            viewHolder.txt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent myint = new Intent(getContext(),Activity_UpdateGoal.class);
                    myint.putExtra("goal_detail",filtered_rv_goallist.get(i));
                    startActivity(myint);
                }
            });

            viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),"Position : "+String.valueOf(i),Toast.LENGTH_SHORT).show();
                }
            });



        }


        @Override
        public int getItemCount() {
            return filtered_rv_goallist.size();

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
                        filtered_rv_goallist = rv_goallist;

                    } else {
                        ArrayList<Model_GoalList> filteredList = new ArrayList<>();
                        for (Model_GoalList row : rv_goallist) {



                                if(row.goalname!=null){
                                    if (row.goalname.equals(charSequence)) {
                                        filteredList.add(row);
                                    }
                                }




                        }

                        filtered_rv_goallist = filteredList;

                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filtered_rv_goallist;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    filtered_rv_goallist = (ArrayList<Model_GoalList>) filterResults.values;
                    Collections.reverse(filtered_rv_goallist);

                    notifyDataSetChanged();



                }
            };
        }


        public void removeItem(int position) {
            filtered_rv_goallist.remove(position);
            // notify the item removed by position
            // to perform recycler view delete animations
            // NOTE: don't call notifyDataSetChanged()
            notifyItemRemoved(position);
        }

        /*public void restoreItem(Item item, int position) {
            cartList.add(position, item);
            // notify item added by position
            notifyItemInserted(position);
        }*/
        public void remove(int positon){

        }

    }
    public class ViewHolder_Leave_Status extends RecyclerView.ViewHolder {



        TextView txt_goalname,txt_status,txt_deadline,txt_goal_cateogry,txt_intop;
        LinearLayout lnr_main_container;
        ImageView txt_edit,btn_delete;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolder_Leave_Status(View convertView) {
            super(convertView);


            lnr_main_container=convertView.findViewById(R.id.lnr_top_container);
            txt_goalname=convertView.findViewById(R.id.txt_goalname);
            txt_goal_cateogry=convertView.findViewById(R.id.txt_goalcategory);
            txt_status=convertView.findViewById(R.id.txt_goal_status);
            txt_deadline=convertView.findViewById(R.id.txt_goaldeadline);
            txt_edit=convertView.findViewById(R.id.txt_editgoal);
            btn_delete=convertView.findViewById(R.id.delete_icon);
            viewBackground = convertView.findViewById(R.id.view_background);
            viewForeground = convertView.findViewById(R.id.view_foreground);
            txt_intop=convertView.findViewById(R.id.txt_intop);

        }

        public void removeItem(int position) {
            filtered_rv_goallist.remove(position);
            // notify the item removed by position
            // to perform recycler view delete animations
            // NOTE: don't call notifyDataSetChanged()
            adapter.notifyItemRemoved(position);
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        /*category_type_list.add("Business");
        category_type_list.add("Office");
        category_type_list.add("Personal");
        category_type_list.add("General");
        category_type_list.add("Other");
*/
        String query="";
        if(condition.equals("my")){
            welcometext.setText("My Goals");
            query ="select * from " + ConstantValues.GoalList_Table;}//+ " WHERE " + ConstantValues.GoalCategory + "= '" + "My" + "'"; }
         else  if(condition.equals("top3")){
            welcometext.setText("Top 3 Goals");
            query ="select * from " + ConstantValues.GoalList_Table+ " WHERE " + ConstantValues.InTop3 + "= '" + "True" + "'"; }
        else  if(condition.equals("top10")){
            welcometext.setText("Top 10 Goals");
            query ="select * from " + ConstantValues.GoalList_Table+ " WHERE " + ConstantValues.InTop10 + "= '" + "True" + "'"; }
        else  if(condition.equals(ConstantValues.Category_bunishess)){
            welcometext.setText("Business Goals");
            query ="select * from " + ConstantValues.GoalList_Table+ " WHERE " + ConstantValues.GoalCategory + "= '" + ConstantValues.Category_bunishess + "'"; }
        else  if(condition.equals(ConstantValues.Category_office)){
            welcometext.setText("Office Goals");
            query ="select * from " + ConstantValues.GoalList_Table+ " WHERE " + ConstantValues.GoalCategory + "= '" + ConstantValues.Category_office + "'"; }
        else  if(condition.equals(ConstantValues.Category_RelationShip)){
            welcometext.setText("Relationship Goals");
            query ="select * from " + ConstantValues.GoalList_Table+ " WHERE " + ConstantValues.GoalCategory + "= '" + ConstantValues.Category_RelationShip + "'"; }
        else  if(condition.equals(ConstantValues.Category_general)){
            welcometext.setText("General Goals");
            query ="select * from " + ConstantValues.GoalList_Table+ " WHERE " + ConstantValues.GoalCategory + "= '" + ConstantValues.Category_general + "'"; }
        else  if(condition.equals(ConstantValues.Category_LifeStyle)){
            welcometext.setText("LifeStyle Goals");
            query ="select * from " + ConstantValues.GoalList_Table+ " WHERE " + ConstantValues.GoalCategory + "= '" + ConstantValues.Category_LifeStyle + "'"; }
        else  if(condition.equals(ConstantValues.Category_Spiritual)){
            welcometext.setText("Spiritual Goals");
            query ="select * from " + ConstantValues.GoalList_Table+ " WHERE " + ConstantValues.GoalCategory + "= '" + ConstantValues.Category_Spiritual + "'"; }
        else  if(condition.equals(ConstantValues.Category_Family)){
            welcometext.setText("Family Goals");
            query ="select * from " + ConstantValues.GoalList_Table+ " WHERE " + ConstantValues.GoalCategory + "= '" + ConstantValues.Category_Family + "'"; }
        else  if(condition.equals(ConstantValues.Category_Mental)){
            welcometext.setText("Mental Goals");
            query ="select * from " + ConstantValues.GoalList_Table+ " WHERE " + ConstantValues.GoalCategory + "= '" + ConstantValues.Category_Mental + "'"; }
        else  if(condition.equals(ConstantValues.Category_Physical)){
            welcometext.setText("Physical Goals");
            query ="select * from " + ConstantValues.GoalList_Table+ " WHERE " + ConstantValues.GoalCategory + "= '" + ConstantValues.Category_Physical + "'"; }
        else  if(condition.equals(ConstantValues.Category_Financial)){
            welcometext.setText("Financial Goals");
            query ="select * from " + ConstantValues.GoalList_Table+ " WHERE " + ConstantValues.GoalCategory + "= '" + ConstantValues.Category_Financial + "'"; }

        filtered_rv_goallist=dbhelper.get_goallist(query);
        if(filtered_rv_goallist!=null && filtered_rv_goallist.size()>0){
            adapter = new RV_Adapter_GoalList(filtered_rv_goallist);
           // recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            //recyclerView.setItemAnimator(new DefaultItemAnimator());
          //  recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);

          txt_no_task_label.setVisibility(View.GONE);
          recyclerView.setVisibility(View.VISIBLE);


            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new GoalList_RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        }else{
            txt_no_task_label.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
          //  Toast.makeText(getContext(),"No goalList Found !",Toast.LENGTH_SHORT).show();
        }

    }

     void delete_goal_dialog(final int position){

         final Dialog dialogbox_weekly = new Dialog(getContext());
         dialogbox_weekly.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialogbox_weekly.setContentView(R.layout.dialogbox_delete_item);
         dialogbox_weekly. getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

         LinearLayout lnr_no = dialogbox_weekly.findViewById(R.id.lnr_no);
         LinearLayout lnr_yes =dialogbox_weekly.findViewById(R.id.lnr_yes);

         lnr_no.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialogbox_weekly.dismiss();
             }
         });


         lnr_yes.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {



                 dbhelper.delete_goal(filtered_rv_goallist.get(position).getId());
                 filtered_rv_goallist.remove(position);
                 adapter.notifyDataSetChanged();
                 dialogbox_weekly.dismiss();
             }
         });

         dialogbox_weekly.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         dialogbox_weekly.show();




     }



}







// showing snack bar with Undo option
          /*  Snackbar snackbar = Snackbar.make(bottomlayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                   // adapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();*/