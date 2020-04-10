package com.kachhua.goal.model;

import com.google.gson.annotations.SerializedName;

public class User {

    public User(int user_id, String department_id, String department_name, int department_head_user_id, String emp_unique_id, String name, String profile, int shift_id,String token,String role) {
        this.user_id = user_id;
        this.department_id = department_id;
        this.department_name = department_name;
        this.department_head_user_id = department_head_user_id;
        this.emp_unique_id = emp_unique_id;
        this.name = name;
        this.profile = profile;
        this.shift_id = shift_id;
        this.token=token;
        this.role=role;

    }

    @SerializedName("user_id")
    public int user_id;

    @SerializedName("department_id")
    public String department_id;

    @SerializedName("department_name")
    public String department_name;

    @SerializedName("department_head_user_id")
    public int department_head_user_id;

    @SerializedName("emp_unique_id")
    public String emp_unique_id;


    @SerializedName("name")
    public String name;

    @SerializedName("profile")
    public String profile;

    @SerializedName("shift_id")
    public int shift_id;

    @SerializedName("token")
    public String token;

    @SerializedName("role")
    public String role;


}
