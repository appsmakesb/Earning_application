package com.Part.Time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class App_Controller {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final char DOT = '.';

    @SuppressLint("CommitPrefEdits")


    public void setSuccessImpressionCount(int count) {
        editor.putInt("TaskApi", count);
        editor.apply();
    }

    public int getSuccessImpressionCount() {
        int date;
        date = sharedPreferences.getInt("TaskApi", 0);
        return date;
    }




    public  void setDate(int date){
        editor.putInt("date",date);
        editor.commit();
    }
    public int getDate(){
        int date = sharedPreferences.getInt("date",0);
        return date;
    }


    public  void setBalance(int balance){
        editor.putInt("balance",balance);
        editor.commit();
    }
    public int getBalance(){
        int balance = sharedPreferences.getInt("balance",0);
        return balance;
    }


    public  void setMobile(String mobile){
        editor.putString("mobile",mobile);
        editor.commit();
    }
    public String getMobile(){
        String mobile = sharedPreferences.getString("mobile","0");
        return mobile;
    }




    public void dataStore (String spin_limit ,String daliy_task_limit, String spin1_control, String spin_reward, String spin_waitingTime){
        editor.putString("spin_limit",spin_limit);
        editor.putString("daily_task_limit",daliy_task_limit);
        editor.putString("spin_control",spin1_control);
        editor.putString("spin_reward",spin_reward);
        editor.putString("spin_waitingTime",spin_waitingTime);
        editor.commit();
    }


    public String getSpinReward (){
        String spin_reward = sharedPreferences.getString("spin_reward","100");
        return spin_reward;
    }

    public String getSpinWaitingTime (){
        String spin_waitingTime = sharedPreferences.getString("spin_waitingTime","0");
        return spin_waitingTime;
    }


    public String getSpin_limit (){
        String spin_limit = sharedPreferences.getString("spin_limit","0");
        return spin_limit;
    }
    public String getDaliy_task_limit (){
        String daliy_task_limit = sharedPreferences.getString("daily_task_limit","0");
        return daliy_task_limit;
    }

    public String getSpin1_control (){
        String spin1_control = sharedPreferences.getString("spin_control","");
        return spin1_control;
    }



    public  void spin1DailyTaskCounter(int value){
        editor.putInt("taskCounter",value);
        editor.commit();
    }
    public int getSpin1DailyTaskCounter1(){
        int taskCounter = sharedPreferences.getInt("taskCounter",0);
        return taskCounter;
    }

    public  void spin1DailyTaskLimitCounter(int value){
        editor.putInt("taskLimitCounter",value);
        editor.commit();
    }
    public int getSpin1DailyTaskLimitCounter1(){
        int taskCounter = sharedPreferences.getInt("taskLimitCounter",0);
        return taskCounter;
    }



    public  void setLuckySpinDailyTaskLimitCounter(int value){
        editor.putInt("luckSpinLimitCounter",value);
        editor.commit();
    }
    public int getLuckySpinTaskLimitCounter1(){
        int taskCounter = sharedPreferences.getInt("luckSpinLimitCounter",0);
        return taskCounter;
    }





}
