package com.example.wly.dailyhabit_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wly.dailyhabit_android.Info.User;


public class MineFragment extends Fragment {
    Context context;
    private TextView showEmail;
    private Button logoutBtn;
    private SharedPreferences account;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, null);
        context = getActivity().getApplicationContext();
        TextView tabTitleText = getActivity().findViewById(R.id.tab_title_text);
        tabTitleText.setText("我的");
        TextView addGoal = getActivity().findViewById(R.id.add_goal_button);
        addGoal.setText("");
        showEmail = view.findViewById(R.id.show_email);
        showEmail.setText(User.email);
        logoutBtn = view.findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = account.edit();
                editor.clear();
                editor.commit();
                getActivity().finish();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
