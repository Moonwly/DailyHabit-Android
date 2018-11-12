package com.example.wly.dailyhabit_android;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wly.dailyhabit_android.ConnectServer.HttpTask;
import com.example.wly.dailyhabit_android.ConnectServer.OnAsyncTaskListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Calendar;

public class NewGoalActivity extends AppCompatActivity {
    private Context context;
    private EditText newGoalName;
    private RadioGroup newTypeGroup;
    private EditText newInspiration;
    private EditText newStartDate;
    private EditText newEndDate;
    private CheckBox newSelectSunday;
    private CheckBox newSelectMonday;
    private CheckBox newSelectTuesday;
    private CheckBox newSelectWednesday;
    private CheckBox newSelectThursday;
    private CheckBox newSelectFriday;
    private CheckBox newSelectSaturday;

    private TextView newGoalBtn;

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
        setContentView(R.layout.activity_new_goal);

        final RadioButton healthBtn = (RadioButton) findViewById(R.id.new_type_health);
        Drawable healthImg = ContextCompat.getDrawable(this, R.drawable.health);
        healthImg.setBounds(0, 0, 100, 100);
        healthBtn.setCompoundDrawables(null, healthImg, null, null);

        RadioButton studyBtn = (RadioButton) findViewById(R.id.new_type_study);
        Drawable studyImg = ContextCompat.getDrawable(this, R.drawable.study);
        studyImg.setBounds(0, 0, 100, 100);
        studyBtn.setCompoundDrawables(null, studyImg, null, null);

        RadioButton workBtn = (RadioButton) findViewById(R.id.new_type_work);
        Drawable workImg = ContextCompat.getDrawable(this, R.drawable.work);
        workImg.setBounds(0, 0, 100, 100);
        workBtn.setCompoundDrawables(null, workImg, null, null);

        RadioButton dailyBtn = (RadioButton) findViewById(R.id.new_type_daily);
        Drawable dailyImg = ContextCompat.getDrawable(this, R.drawable.daily);
        dailyImg.setBounds(0, 0, 100, 100);
        dailyBtn.setCompoundDrawables(null, dailyImg, null, null);

        RadioButton othersBtn = (RadioButton) findViewById(R.id.new_type_others);
        Drawable othersImg = ContextCompat.getDrawable(this, R.drawable.others);
        othersImg.setBounds(0, 0, 100, 100);
        othersBtn.setCompoundDrawables(null, othersImg, null, null);

        newGoalName = (EditText) findViewById(R.id.new_goal_name);
        newTypeGroup = (RadioGroup) findViewById(R.id.new_type_group);
        newInspiration = (EditText) findViewById(R.id.new_inspiration);
        newStartDate = (EditText) findViewById(R.id.new_start_date);
        newEndDate = (EditText) findViewById(R.id.new_end_date);
        newSelectSunday = (CheckBox) findViewById(R.id.new_sunday);
        newSelectMonday = (CheckBox) findViewById(R.id.new_monday);
        newSelectTuesday = (CheckBox) findViewById(R.id.new_tuesday);
        newSelectWednesday = (CheckBox) findViewById(R.id.new_wednesday);
        newSelectThursday = (CheckBox) findViewById(R.id.new_thursday);
        newSelectFriday = (CheckBox) findViewById(R.id.new_friday);
        newSelectSaturday = (CheckBox) findViewById(R.id.new_saturday);
        newGoalName.setText("");
        findViewById(R.id.new_type_health).performClick();
        newInspiration.setText("");
        newStartDate.setText("1970-01-01");
        newEndDate.setText("1970-01-01");
        newSelectSunday.setChecked(true);
        newSelectMonday.setChecked(true);
        newSelectTuesday.setChecked(true);
        newSelectWednesday.setChecked(true);
        newSelectThursday.setChecked(true);
        newSelectFriday.setChecked(true);
        newSelectSaturday.setChecked(true);

        newStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                newStartDate.setText(year + "-" + (int)(monthOfYear+1) + "-"
                                        + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        newEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                newEndDate.setText(year + "-" + (int)(monthOfYear+1) + "-"
                                        + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });



        newGoalBtn = (TextView) findViewById(R.id.finish_new);
        newGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String repeat = "";
                try {
                    String goalName = newGoalName.getText().toString();
                    int goalType = 5;
                    if (newTypeGroup.getCheckedRadioButtonId() == R.id.new_type_health)
                        goalType = 1;
                    else if (newTypeGroup.getCheckedRadioButtonId() == R.id.new_type_study)
                        goalType = 2;
                    else if (newTypeGroup.getCheckedRadioButtonId() == R.id.new_type_work)
                        goalType = 3;
                    else if (newTypeGroup.getCheckedRadioButtonId() == R.id.new_type_daily)
                        goalType = 4;
                    else
                        goalType = 5;
                    String inspiration = newInspiration.getText().toString();
                    String startDate = newStartDate.getText().toString();
                    String endDate = newEndDate.getText().toString();

                    if (newSelectSunday.isChecked()) repeat += "0";
                    if (newSelectMonday.isChecked()) repeat += "1";
                    if (newSelectTuesday.isChecked()) repeat += "2";
                    if (newSelectWednesday.isChecked()) repeat += "3";
                    if (newSelectThursday.isChecked()) repeat += "4";
                    if (newSelectFriday.isChecked()) repeat += "5";
                    if (newSelectSaturday.isChecked()) repeat += "6";

                    String modifyGoalURL = "/goal/new_goal";
                    String method = "POST";
                    JSONObject goalJSON = new JSONObject();
                    goalJSON.put("goal_name", goalName);
                    goalJSON.put("goal_status", true);
                    goalJSON.put("goal_type", goalType);
                    goalJSON.put("inspiration", inspiration);
                    goalJSON.put("start_date", startDate);
                    goalJSON.put("end_date", endDate);
                    goalJSON.put("repeat_time", repeat);
                    goalJSON.put("recorded_times", 0);
                    goalJSON.put("is_reminding", false);
                    goalJSON.put("reminding_time", "12:00:00");
                    String transValue = goalJSON.toString();
                    HttpTask httpTask = new HttpTask(transValue, modifyGoalURL, method, newGoalListener);
                    httpTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private OnAsyncTaskListener newGoalListener = new OnAsyncTaskListener() {
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
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
