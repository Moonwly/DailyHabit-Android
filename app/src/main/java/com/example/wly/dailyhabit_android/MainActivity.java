package com.example.wly.dailyhabit_android;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener {
    Context context;
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    private TextView addGoalBtn;

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
        setContentView(R.layout.activity_main);

        RadioButton todayBtn = findViewById(R.id.tab_today);
        Drawable todayImg = ContextCompat.getDrawable(this, R.drawable.today_image_selector);
        todayImg.setBounds(0, 0, 70, 70);
        todayBtn.setCompoundDrawables(null, todayImg, null, null);

        RadioButton allBtn = findViewById(R.id.tab_all);
        Drawable allImg = ContextCompat.getDrawable(this, R.drawable.all_image_selector);
        allImg.setBounds(0, 0, 70, 70);
        allBtn.setCompoundDrawables(null, allImg, null, null);


        RadioButton mineBtn = findViewById(R.id.tab_mine);
        Drawable mineImg = ContextCompat.getDrawable(this, R.drawable.mine_image_selector);
        mineImg.setBounds(0, 0, 70, 70);
        mineBtn.setCompoundDrawables(null, mineImg, null, null);


        fragmentManager = getFragmentManager();
        radioGroup = findViewById(R.id.button_lists);
        radioGroup.setOnCheckedChangeListener(this);
        findViewById(R.id.tab_today).performClick();

        addGoalBtn = findViewById(R.id.add_goal_button);
        addGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newGoalIntent = new Intent(context, NewGoalActivity.class);
                startActivity(newGoalIntent);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = FragmentFactory.getBottomTabInstanceByIndex(checkedId);
        transaction.replace(R.id.main_container, fragment);
        transaction.commit();
    }

}
