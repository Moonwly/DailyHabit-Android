package com.example.wly.dailyhabit_android;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener {
    Context context;
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initInterface();
    }

    private void initInterface() {
        setContentView(R.layout.activity_main);

        RadioButton today_btn = findViewById(R.id.tab_today);
        Drawable today_img = ContextCompat.getDrawable(this, R.drawable.today_image_selector);
        today_img.setBounds(0, 0, 70, 70);
        today_btn.setCompoundDrawables(null, today_img, null, null);

        RadioButton all_btn = findViewById(R.id.tab_all);
        Drawable all_img = ContextCompat.getDrawable(this, R.drawable.all_image_selector);
        all_img.setBounds(0, 0, 70, 70);
        all_btn.setCompoundDrawables(null, all_img, null, null);


        RadioButton mine_btn = findViewById(R.id.tab_mine);
        Drawable mine_img = ContextCompat.getDrawable(this, R.drawable.mine_image_selector);
        mine_img.setBounds(0, 0, 70, 70);
        mine_btn.setCompoundDrawables(null, mine_img, null, null);


        fragmentManager = getFragmentManager();
        radioGroup = findViewById(R.id.button_lists);
        radioGroup.setOnCheckedChangeListener(this);
        findViewById(R.id.tab_today).performClick();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = FragmentFactory.getBottomTabInstanceByIndex(checkedId);
        transaction.replace(R.id.main_container, fragment);
        transaction.commit();
    }

}
