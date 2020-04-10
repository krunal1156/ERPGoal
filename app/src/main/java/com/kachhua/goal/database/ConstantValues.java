package com.kachhua.goal.database;

public class ConstantValues {

    public static final String Status_running ="Running";
    public static final String Status_not_started ="Not Started";
    public static final String Status_finished ="Finished";
    public static final String Status_Missed="Missed";
    public static final String Status_Active="Active";
    public static final String Status_InActive="InActive";
    public static final String Status_Reported="Reported";

    public static final String Complated="Complated";
    public static final String Incomplated="Incomplated";

    public static final String Category_bunishess ="Business";
    public static final String Category_office ="Office";
    public static final String Category_RelationShip ="Relationship";
    public static final String Category_general ="General";
    public static final String Category_LifeStyle ="Lifestyle";
    public static final String Category_Spiritual ="Spiritual";
    public static final String Category_Family ="Family";
    public static final String Category_Mental ="Mental";
    public static final String Category_Physical="Physical";
    public static final String Category_Financial ="Financial";




    public static final String Frequency_Daily="Daily";
    public static final String Frequency_Weekly="Weekly";
    public static final String Frequency_Monthly="Monthly";
    public static final String Frequency_OneTime="OneTime";

    public static final String WeekDay_Monday="Monday";
    public static final String WeekDay_Tuesday="Tuesday";
    public static final String WeekDay_Wednesday="Wednesday";
    public static final String WeekDay_Thursday="Thursday";
    public static final String WeekDay_Friday="Friday";
    public static final String WeekDay_Saturday="Saturday";
    public static final String WeekDay_Sunday="Sunday";


    public static final String GoalList_Table = "goalList_tbl";
    public static final String GoalName ="GoalName";
    public static final String GoalCategory="GoalCategory";
    public static final String GoalStatus="GoalStatus";
    public static final String Goal_Frequency="Goal_Frequency";
    public static final String Goal_startDeadLine="Goal_startDeadLine";
    public static final String Goal_endDeadLine="Goal_endDeadLine";
    public static final String InTop3="InTop3";
    public static final String InTop10="InTop10";
    public static final String Goal_Active_InActive="Goal_Actie_InActive";
    public static final String Goal_Created_Date="Goal_Created_Date";
    public static final String GoalComplate="GoalComplate";
    public static final String  goallist_table_query = "create table "+ GoalList_Table + "( ID INTEGER  PRIMARY KEY AUTOINCREMENT,"
            +GoalName+" TEXT,"+GoalCategory+" TEXT,"+GoalStatus+" TEXT,"+Goal_Frequency+" TEXT,"+Goal_startDeadLine+" TEXT,"+Goal_endDeadLine+" TEXT,"+Goal_Active_InActive+" TEXT,"+Goal_Created_Date+" TEXT,"+GoalComplate+" TEXT,"+InTop3+" TEXT,"+InTop10+" TEXT)";




    public static final String Tasklist_Table="Tasklist_Table";
    public static final String TaskName="TaskName";
    public static final String GoalId ="Goal_Id";
    public static final String Task_Frequency ="Task_Frequency";
    public static final String Task_Frequency_value ="Task_Frequency_value";
    public static final String TaskStatus="TaskStatus";
    public static final String Task_startDeadline="Task_startDeadline";
    public static final String Task_endDeadline="Task_endDeadline";
    public static final String Reason="Reason";
    public static final String Task_Active_InActive="Task_Active_InActive";
    public static final String Task_Created_Date="Task_Created_Date";
    public static final String Task_Finished_Date="Task_Finished_Date";
    public static final String TaskComplate="TaskComplate";
    public static final String  tasklist_table_query = "create table "+ Tasklist_Table + "( ID INTEGER  PRIMARY KEY AUTOINCREMENT,"
            +GoalId+" TEXT," +TaskName+" TEXT,"+ Task_Frequency +" TEXT,"+ Task_Frequency_value +" TEXT,"+TaskStatus+" TEXT,"+Task_startDeadline+" TEXT,"+Task_endDeadline+" TEXT,"+Task_Active_InActive+" TEXT,"+Task_Created_Date+" TEXT,"+Task_Finished_Date+" TEXT,"+TaskComplate+" TEXT,"+Reason+" TEXT)";



    public static final String Daily_Tasklist_Table="Daily_Tasklist_Table";
    public static final String TaskId="Task_Id";
    public static final String  Daily_tasklist_table_query = "create table "+ Daily_Tasklist_Table + "( ID INTEGER  PRIMARY KEY AUTOINCREMENT,"
            +GoalId+" TEXT,"+TaskId+" TEXT," +TaskName+" TEXT,"+ Task_Frequency +" TEXT,"+ Task_Frequency_value +" TEXT,"+TaskStatus+" TEXT,"+Task_startDeadline+" TEXT,"+Task_endDeadline+" TEXT,"+Task_Active_InActive+" TEXT,"+Task_Created_Date+" TEXT,"+Task_Finished_Date+" TEXT,"+TaskComplate+" TEXT,"+Reason+" TEXT)";



}
