package com.kachhua.goal.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Model_GoalList  implements Serializable {


    @SerializedName("id")
    public String id;


    @SerializedName("goalname")
    public String goalname;


    @SerializedName("frequency")
    public String frequency;

    @SerializedName("status")
    public String status;

    @SerializedName("category")
    public String category;

    @SerializedName("start_deadline")
    public String start_Deadline;

    @SerializedName("end_deadline")
    public String end_deadline;

    @SerializedName("intop3")
    public String intop3;

    @SerializedName("intop10")
    public String intop10;

    @SerializedName("is_active")
    public String is_active;

    @SerializedName("created_date")
    public String created_date;

    @SerializedName("is_complate")
    public String is_complate;

    @SerializedName("tasklist")
    public ArrayList<TaskDetail> taskDetailArrayList;

    public Model_GoalList(String goalname, ArrayList<TaskDetail> taskDetailArrayList) {
        this.goalname = goalname;
        this.taskDetailArrayList = taskDetailArrayList;
    }

    public Model_GoalList() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoalname() {
        return goalname;
    }

    public void setGoalname(String goalname) {
        this.goalname = goalname;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStart_Deadline() {
        return start_Deadline;
    }

    public void setStart_Deadline(String start_Deadline) {
        this.start_Deadline = start_Deadline;
    }

    public String getEnd_deadline() {
        return end_deadline;
    }

    public void setEnd_deadline(String end_deadline) {
        this.end_deadline = end_deadline;
    }

    public String getIntop3() {
        return intop3;
    }

    public void setIntop3(String intop3) {
        this.intop3 = intop3;
    }

    public String getIntop10() {
        return intop10;
    }

    public void setIntop10(String intop10) {
        this.intop10 = intop10;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getIs_complate() {
        return is_complate;
    }

    public void setIs_complate(String is_complate) {
        this.is_complate = is_complate;
    }



    public static class TaskDetail implements Serializable{

        public String show;

        @SerializedName("id")
        public String id;

        @SerializedName("goalid")
        public String goalid;

        @SerializedName("goalname")
        public String goalname;

        @SerializedName("taskid")
        public String taskid;


        @SerializedName("Taskname")
        public String taskname;

        @SerializedName("task_status")
        public String taskstatus;

        @SerializedName("task_frequecy")
        public String task_frequecy;

        @SerializedName("task_frequency_value")
        public String task_frequecy_value;


        @SerializedName("start_deadline")
        public String start_deadline;


        @SerializedName("end_deadline")
        public String end_deadline;


        @SerializedName("reason")
        public String reason;

        @SerializedName("is_active")
        public String is_active;

        @SerializedName("created_Date")
        public String created_date;

        @SerializedName("is_complate")
        public String is_complate;

        public String getTaskid() {
            return taskid;
        }

        public void setTaskid(String taskid) {
            this.taskid = taskid;
        }

        public String getTask_frequecy_value() {
            return task_frequecy_value;
        }

        public void setTask_frequecy_value(String task_frequecy_value) {
            this.task_frequecy_value = task_frequecy_value;
        }

        public String getGoalname() {
            return goalname;
        }

        public void setGoalname(String goalname) {
            this.goalname = goalname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoalid() {
            return goalid;
        }

        public void setGoalid(String goalid) {
            this.goalid = goalid;
        }

        public String getTaskname() {
            return taskname;
        }

        public void setTaskname(String taskname) {
            this.taskname = taskname;
        }

        public String getTaskstatus() {
            return taskstatus;
        }

        public void setTaskstatus(String taskstatus) {
            this.taskstatus = taskstatus;
        }

        public String getTask_frequecy() {
            return task_frequecy;
        }

        public void setTask_frequecy(String task_frequecy) {
            this.task_frequecy = task_frequecy;
        }

        public String getStart_deadline() {
            return start_deadline;
        }

        public void setStart_deadline(String start_deadline) {
            this.start_deadline = start_deadline;
        }

        public String getEnd_deadline() {
            return end_deadline;
        }

        public void setEnd_deadline(String end_deadline) {
            this.end_deadline = end_deadline;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getIs_active() {
            return is_active;
        }

        public void setIs_active(String is_active) {
            this.is_active = is_active;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getIs_complate() {
            return is_complate;
        }

        public void setIs_complate(String is_complate) {
            this.is_complate = is_complate;
        }

        public String getShow() {
            return show;
        }

        public void setShow(String show) {
            this.show = show;
        }
    }
}
