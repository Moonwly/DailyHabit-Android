package com.example.wly.dailyhabit_android;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wly.dailyhabit_android.ConnectServer.HttpTask;
import com.example.wly.dailyhabit_android.ConnectServer.OnAsyncTaskListener;
import com.example.wly.dailyhabit_android.Info.Goal;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditGoalActivity extends AppCompatActivity{
    private Context context;
    private EditText inputGoalName;
    private RadioGroup selectTypeGroup;
    private EditText inputInspiration;
    private EditText startDate;
    private EditText endDate;
    private CheckBox selectSunday;
    private CheckBox selectMonday;
    private CheckBox selectTuesday;
    private CheckBox selectWednesday;
    private CheckBox selectThursday;
    private CheckBox selectFriday;
    private CheckBox selectSaturday;

    private Button deleteGoal;

    private TextView finishModifyBtn;

    Goal goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initInterface();
    }

    private void initInterface() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
        setContentView(R.layout.activity_edit_goal);

        final RadioButton healthBtn = (RadioButton) findViewById(R.id.select_type_health);
        Drawable healthImg = ContextCompat.getDrawable(this, R.drawable.health);
        healthImg.setBounds(0, 0, 100, 100);
        healthBtn.setCompoundDrawables(null, healthImg, null, null);

        RadioButton studyBtn = (RadioButton) findViewById(R.id.select_type_study);
        Drawable studyImg = ContextCompat.getDrawable(this, R.drawable.study);
        studyImg.setBounds(0, 0, 100, 100);
        studyBtn.setCompoundDrawables(null, studyImg, null, null);

        RadioButton workBtn = (RadioButton) findViewById(R.id.select_type_work);
        Drawable workImg = ContextCompat.getDrawable(this, R.drawable.work);
        workImg.setBounds(0, 0, 100, 100);
        workBtn.setCompoundDrawables(null, workImg, null, null);

        RadioButton dailyBtn = (RadioButton) findViewById(R.id.select_type_daily);
        Drawable dailyImg = ContextCompat.getDrawable(this, R.drawable.daily);
        dailyImg.setBounds(0, 0, 100, 100);
        dailyBtn.setCompoundDrawables(null, dailyImg, null, null);

        RadioButton othersBtn = (RadioButton) findViewById(R.id.select_type_others);
        Drawable othersImg = ContextCompat.getDrawable(this, R.drawable.others);
        othersImg.setBounds(0, 0, 100, 100);
        othersBtn.setCompoundDrawables(null, othersImg, null, null);

        Intent intent = getIntent();
        final int goal_id = intent.getIntExtra("goal_id", -1);

        inputGoalName = (EditText) findViewById(R.id.input_goal_name);
        inputInspiration = (EditText) findViewById(R.id.input_inspiration);
        startDate = (EditText) findViewById(R.id.select_start_date);
        endDate = (EditText) findViewById(R.id.select_end_date);
        selectSunday = (CheckBox) findViewById(R.id.select_sunday);
        selectMonday = (CheckBox) findViewById(R.id.select_monday);
        selectTuesday = (CheckBox) findViewById(R.id.select_tuesday);
        selectWednesday = (CheckBox) findViewById(R.id.select_wednesday);
        selectThursday = (CheckBox) findViewById(R.id.select_thursday);
        selectFriday = (CheckBox) findViewById(R.id.select_friday);
        selectSaturday = (CheckBox) findViewById(R.id.select_saturday);
        finishModifyBtn = (TextView) findViewById(R.id.finish_modify);
        selectTypeGroup = (RadioGroup) findViewById(R.id.select_type_group);
        deleteGoal = (Button) findViewById(R.id.delete_goal);
        getGoalInfo(goal_id);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                startDate.setText(year + "-" + (int)(monthOfYear+1) + "-"
                                        + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                endDate.setText(year + "-" + (int)(monthOfYear+1) + "-"
                                        + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        finishModifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String repeat = "";
                try {
                    goal.setGoalName(inputGoalName.getText().toString());
                    if (selectTypeGroup.getCheckedRadioButtonId() == R.id.select_type_health)
                        goal.setGoalType(1);
                    else if (selectTypeGroup.getCheckedRadioButtonId() == R.id.select_type_study)
                        goal.setGoalType(2);
                    else if (selectTypeGroup.getCheckedRadioButtonId() == R.id.select_type_work)
                        goal.setGoalType(3);
                    else if (selectTypeGroup.getCheckedRadioButtonId() == R.id.select_type_daily)
                        goal.setGoalType(4);
                    else
                        goal.setGoalType(5);
                    goal.setInspiration(inputInspiration.getText().toString());
                    goal.setStartDate(startDate.getText().toString());
                    goal.setEndDate(endDate.getText().toString());
                    if (selectSunday.isChecked()) repeat += "0";
                    if (selectMonday.isChecked()) repeat += "1";
                    if (selectTuesday.isChecked()) repeat += "2";
                    if (selectWednesday.isChecked()) repeat += "3";
                    if (selectThursday.isChecked()) repeat += "4";
                    if (selectFriday.isChecked()) repeat += "5";
                    if (selectSaturday.isChecked()) repeat += "6";
                    goal.setRepeatTime(repeat);

                    String modifyGoalURL = "/goal/modify_goal";
                    String method = "POST";
                    JSONObject goalJSON = new JSONObject();
                    goalJSON.put("goal_id", goal.getGoalId());
                    goalJSON.put("goal_name", goal.getGoalName());
                    goalJSON.put("goal_status", goal.getGoalStatus());
                    goalJSON.put("goal_type", goal.getGoalType());
                    goalJSON.put("inspiration", goal.getInspiration());
                    goalJSON.put("start_date", goal.getStartDate());
                    goalJSON.put("end_date", goal.getEndDate());
                    goalJSON.put("repeat_time", goal.getRepeatTime());
                    goalJSON.put("recorded_times", goal.getRecordedTimes());
                    goalJSON.put("is_reminding", goal.getReminding());
                    goalJSON.put("reminding_time", goal.getRemindingTime());
                    String transValue = goalJSON.toString();
                    HttpTask httpTask = new HttpTask(transValue, modifyGoalURL, method, modifyGoalListener);
                    httpTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        deleteGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String deleteGoalURL = "/goal/delete_goal";
                    String method = "GET";
                    String transValue = URLEncoder.encode("goal_id", "UTF-8") + "=" +
                            URLEncoder.encode(String.valueOf(goal.getGoalId()), "UTF-8");
                    HttpTask httpTask = new HttpTask(transValue, deleteGoalURL, method, deleteGoalListener);
                    httpTask.execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getGoalInfo(int goal_id) {
        try {
            String getUserGoalByIdURL = "/goal/get_user_goal_by_id";
            String method = "GET";
            String transValue = URLEncoder.encode("goal_id", "UTF-8") + "=" +
                    URLEncoder.encode(String.valueOf(goal_id), "UTF-8");
            HttpTask httpTask = new HttpTask(transValue, getUserGoalByIdURL, method, getGoalDetailListener);
            httpTask.execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private OnAsyncTaskListener getGoalDetailListener = new OnAsyncTaskListener() {
        @Override
        public void onSuccess(String string) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD");
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
                int goalId = body.getInt("goal_id");
                String goalName = body.getString("goal_name");
                Boolean goalStatus = body.getBoolean("goal_status");
                int goalType = body.getInt("goal_type");
                String inspiration = body.getString("inspiration");
                String startDateStr = body.getString("start_date");
                String endDateStr = body.getString("end_date");
                String repeatTime = body.getString("repeat_time");
                int recordedTimes = body.getInt("recorded_times");
                Boolean isReminding = body.getBoolean("is_reminding");
                String remindingTime = body.getString("reminding_time");

                goal = new Goal(goalId, goalName, goalStatus, goalType, inspiration, startDateStr,
                        endDateStr, repeatTime, recordedTimes, isReminding, remindingTime);


                inputGoalName.setText(goal.getGoalName());
                switch (goal.getGoalType()) {
                    case 1:
                        findViewById(R.id.select_type_health).performClick();
                        break;
                    case 2:
                        findViewById(R.id.select_type_study).performClick();
                        break;
                    case 3:
                        findViewById(R.id.select_type_work).performClick();
                        break;
                    case 4:
                        findViewById(R.id.select_type_daily).performClick();
                        break;
                    case 5:
                        findViewById(R.id.select_type_others).performClick();
                        break;
                }

                inputInspiration.setText(goal.getInspiration());
                startDate.setText(goal.getStartDate());
                endDate.setText(goal.getEndDate());
                selectSunday.setChecked(goal.getRepeatTime().contains("0"));
                selectMonday.setChecked(goal.getRepeatTime().contains("1"));
                selectTuesday.setChecked(goal.getRepeatTime().contains("2"));
                selectWednesday.setChecked(goal.getRepeatTime().contains("3"));
                selectThursday.setChecked(goal.getRepeatTime().contains("4"));
                selectFriday.setChecked(goal.getRepeatTime().contains("5"));
                selectSaturday.setChecked(goal.getRepeatTime().contains("6"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private OnAsyncTaskListener modifyGoalListener = new OnAsyncTaskListener() {
        @Override
        public void onSuccess(String string){
            try {
                JSONTokener jsonTokener = new JSONTokener(string);
                JSONObject jsonRet = (JSONObject) jsonTokener.nextValue();
                int code = jsonRet.getInt("code");
                String msg = jsonRet.getString("msg");
                if (code != 0) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    return ;
                }
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private OnAsyncTaskListener deleteGoalListener = new OnAsyncTaskListener() {
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
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
