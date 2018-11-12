package com.example.wly.dailyhabit_android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wly.dailyhabit_android.ConnectServer.HttpTask;
import com.example.wly.dailyhabit_android.ConnectServer.OnAsyncTaskListener;
import com.example.wly.dailyhabit_android.Info.Goal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TodayFragment extends Fragment {
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_fragment, null);
        context = getActivity().getApplicationContext();
        initInterface();
        return view;
    }

    private void initInterface() {
        try {
            String getUserTodayGoalURL = "/goal/get_user_today_goals";
            String method = "GET";
            String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
            String transValue = URLEncoder.encode("today_date", "UTF-8") + "=" +
                    URLEncoder.encode(todayDate, "UTF-8") + "&" +
                    URLEncoder.encode("today_day", "UTF-8") + "=" +
                    URLEncoder.encode(String.valueOf(todayDay), "UTF-8");
            HttpTask httpTask = new HttpTask(transValue, getUserTodayGoalURL, method, loadTodayGoalListener);
            httpTask.execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private OnAsyncTaskListener loadTodayGoalListener = new OnAsyncTaskListener() {
        @Override
        public void onSuccess(String string) {
            try {
                JSONTokener jsonTokener = new JSONTokener(string);
                JSONObject jsonRet = (JSONObject) jsonTokener.nextValue();
                int code = jsonRet.getInt("code");
                String msg = jsonRet.getString("msg");
                if (code != 0) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject body = jsonRet.getJSONObject("body");
                JSONArray goalList = body.getJSONArray("goal_list");
                GridLayout todayGoalList = getView().findViewById(R.id.today_goal_list);
                todayGoalList.removeAllViews();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                if (goalList != null) {
                    for (int i = 0; i < goalList.length(); i++) {
                        JSONObject goalInfo = goalList.getJSONObject(i);
                        int goalId = goalInfo.getInt("goal_id");
                        String goalName = goalInfo.getString("goal_name");
                        Boolean goalStatus = goalInfo.getBoolean("goal_status");
                        int goalType = goalInfo.getInt("goal_type");
                        String inspiration = goalInfo.getString("inspiration");
                        String startDateStr = goalInfo.getString("start_date");
                        Date startDate = dateFormat.parse(startDateStr);
                        String endDateStr = goalInfo.getString("end_date");
                        Date endDate = dateFormat.parse(endDateStr);
                        String repeatTime = goalInfo.getString("repeat_time");
                        int recordedTimes = goalInfo.getInt("recorded_times");
                        Boolean isReminding = goalInfo.getBoolean("is_reminding");
                        String remindingTimeStr = goalInfo.getString("reminding_time");
                        Boolean isRecoededToday = goalInfo.getBoolean("is_recorded_today");
                        Date remindingTime = new Date();
                        if (!remindingTimeStr.equals("")) {
                            remindingTime = timeFormat.parse(remindingTimeStr);
                        }

                        final Goal goal = new Goal(goalId, goalName, goalStatus, goalType, inspiration, startDate,
                                endDate, repeatTime, recordedTimes, isReminding, remindingTime, isRecoededToday);

                        GoalCard aGoalCard = new GoalCard(goal, context, getActivity());
                        todayGoalList.addView(aGoalCard);
                    }
                }

            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }

        }
    };

}

class GoalCard extends CardView {
    private int goalId;
    private int goalType;
    private String goalName;
    private int recordedTimes;
    private Boolean isRecoeded;

    private ImageView goalImage;
    private TextView goalNameView;
    private TextView haveRecordedView;

    private Context context;
    private Activity activity;
//    1: 可打卡 0:不可打卡
    Boolean goalStatus;

    public GoalCard(final Goal goal, Context context, Activity activity) {
        super(context);
        this.goalId = goal.getGoalId();
        this.goalName = goal.getGoalName();
        this.recordedTimes = goal.getRecordedTimes();
        this.goalType = goal.getGoalType();
        this.isRecoeded = goal.getRecoededToday();
        this.activity = activity;
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.today_card, this);

        goalImage = findViewById(R.id.card_type_image);
//        1:健康 2:学习 3:工作 4:日常
        switch (this.goalType) {
            case 1:
                goalImage.setBackgroundResource(R.drawable.health);
                break;
            case 2:
                goalImage.setBackgroundResource(R.drawable.study);
                break;
            case 3:
                goalImage.setBackgroundResource(R.drawable.work);
                break;
            case 4:
                goalImage.setBackgroundResource(R.drawable.daily);
                break;
            default:
                goalImage.setBackgroundResource(R.drawable.others);
                break;
        }
        if (isRecoeded) {
            goalImage.setImageResource(R.drawable.ok);
            goalImage.setAlpha((float) 0.5);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 300);
        goalImage.setLayoutParams(params);
        goalNameView = findViewById(R.id.goal_name);
        goalNameView.setText(this.goalName);
        haveRecordedView = findViewById(R.id.have_recorded_times);
        haveRecordedView.setText("已完成" + this.recordedTimes + "天");

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecoeded) {
                    cancelRecord(goal);
                }
                else {
                    record(goal);
                }
            }
        });
    }

    private void record(Goal goal) {
        try {
            String newRecordURL = "/record/new_record";
            String method = "POST";
            JSONObject recordJSON = new JSONObject();
            recordJSON.put("goal_id", goal.getGoalId());
            recordJSON.put("record_date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            String transValue = recordJSON.toString();
            HttpTask httpTask = new HttpTask(transValue, newRecordURL, method, recordListener);
            httpTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private OnAsyncTaskListener recordListener = new OnAsyncTaskListener() {
        @Override
        public void onSuccess(String string) {
            try {
                JSONTokener jsonTokener = new JSONTokener(string);
                JSONObject jsonRet = (JSONObject) jsonTokener.nextValue();
                int code = jsonRet.getInt("code");
                String msg = jsonRet.getString("msg");
                if (code != 0) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    return ;
                }
//                设置背景变化
                goalImage.setImageResource(R.drawable.ok);
                goalImage.setAlpha((float) 0.5);
                isRecoeded = true;
                recordedTimes++;
                haveRecordedView.setText("已完成" + recordedTimes + "天");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void cancelRecord(Goal goal) {
        try {
            String cancelRecordURL = "/record/cancel_record";
            String method = "GET";
            String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String transValue = URLEncoder.encode("goal_id", "UTF-8") + "=" +
                    URLEncoder.encode(String.valueOf(goal.getGoalId()), "UTF-8") + "&" +
                    URLEncoder.encode("record_date", "UTF-8") + "=" +
                    URLEncoder.encode(todayDate.toString(), "UTF-8");
            HttpTask httpTask = new HttpTask(transValue, cancelRecordURL, method, cancelRecordListener);
            httpTask.execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private OnAsyncTaskListener cancelRecordListener = new OnAsyncTaskListener() {
        @Override
        public void onSuccess(String string) {
            try {
                JSONTokener jsonTokener = new JSONTokener(string);
                JSONObject jsonRet = (JSONObject) jsonTokener.nextValue();
                int code = jsonRet.getInt("code");
                String msg = jsonRet.getString("msg");
                if (code != 0) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    return ;
                }
//                设置背景变化
                goalImage.setImageResource(0);
                goalImage.setAlpha((float) 1.0);
                isRecoeded = false;
                recordedTimes--;
                haveRecordedView.setText("已完成" + recordedTimes + "天");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
