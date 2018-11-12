package com.example.wly.dailyhabit_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AllFragment extends Fragment {
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_fragment, null);
        context = getActivity().getApplicationContext();
        initInterface();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initInterface();
    }

    private void initInterface() {
        TextView tabTitleText = getActivity().findViewById(R.id.tab_title_text);
        tabTitleText.setText("全部目标");
        TextView addGoal = getActivity().findViewById(R.id.add_goal_button);
        addGoal.setText("+");
        String getUserAllGoalURL = "/goal/get_goal_by_user";
        String method = "GET";
        HttpTask httpTask = new HttpTask("", getUserAllGoalURL, method, loadAllGoalListener);
        httpTask.execute();
    }

    private OnAsyncTaskListener loadAllGoalListener = new OnAsyncTaskListener() {
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
                JSONObject body = jsonRet.getJSONObject("body");
                JSONArray goalList = body.getJSONArray("goal_list");
                LinearLayout allGoalList = getView().findViewById(R.id.all_goal_list);
                allGoalList.removeAllViews();
                if (goalList != null) {
                    for (int i = 0; i < goalList.length(); i++) {
                        JSONObject goalInfo = goalList.getJSONObject(i);
                        int goalId = goalInfo.getInt("goal_id");
                        String goalName = goalInfo.getString("goal_name");
                        Boolean goalStatus = goalInfo.getBoolean("goal_status");
                        int goalType = goalInfo.getInt("goal_type");
                        String inspiration = goalInfo.getString("inspiration");
                        String startDate = goalInfo.getString("start_date");
                        String endDate = goalInfo.getString("end_date");
                        String repeatTime = goalInfo.getString("repeat_time");
                        int recordedTimes = goalInfo.getInt("recorded_times");
                        Boolean isReminding = goalInfo.getBoolean("is_reminding");
                        String remindingTime = goalInfo.getString("reminding_time");
                        Goal goal = new Goal(goalId, goalName, goalStatus, goalType, inspiration, startDate,
                                endDate, repeatTime, recordedTimes, isReminding, remindingTime);

                        AllGoalCard allGoalCard = new AllGoalCard(goal, context, getActivity());
                        allGoalList.addView(allGoalCard);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}

class AllGoalCard extends CardView {
    private String goalName;
    private int goalType;
    private String inspiration;
    private Boolean goalStatus;

    private ImageView goalImageView;
    private TextView goalNameView;
    private TextView inspirationView;
    private TextView goalStatusView;

    private Context context;
    private Activity activity;


    public AllGoalCard(final Goal goal, final Context context, final Activity activity) {
        super(context);
        this.goalName = goal.getGoalName();
        this.goalType = goal.getGoalType();
        this.inspiration = goal.getInspiration();
        this.goalStatus = goal.getGoalStatus();
        this.context = context;
        this.activity = activity;

        LayoutInflater.from(context).inflate(R.layout.all_card, this);

        goalImageView = findViewById(R.id.all_card_type_image);
        switch (this.goalType) {
            case 1:
                goalImageView.setImageResource(R.drawable.health);
                break;
            case 2:
                goalImageView.setImageResource(R.drawable.study);
                break;
            case 3:
                goalImageView.setImageResource(R.drawable.work);
                break;
            case 4:
                goalImageView.setImageResource(R.drawable.daily);
                break;
            default:
                goalImageView.setImageResource(R.drawable.others);
                break;
        }
        goalNameView = findViewById(R.id.all_goal_name);
        goalNameView.setText(this.goalName);
        inspirationView = findViewById(R.id.all_inspiration);
        inspirationView.setText(this.inspiration);
        goalStatusView = findViewById(R.id.all_goal_status);
        if (this.goalStatus) {
            goalStatusView.setText("进行中");
        }
        else {
            goalStatusView.setText("已完成");
        }
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editGoalIntent = new Intent(context, EditGoalActivity.class);
                editGoalIntent.putExtra("goal_id", goal.getGoalId());
                activity.startActivity(editGoalIntent);
            }
        });
    }

}