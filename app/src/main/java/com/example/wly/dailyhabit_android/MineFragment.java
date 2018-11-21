package com.example.wly.dailyhabit_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wly.dailyhabit_android.ConnectServer.HttpTask;
import com.example.wly.dailyhabit_android.ConnectServer.OnAsyncTaskListener;
import com.example.wly.dailyhabit_android.Info.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


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
                String logoutURL = "/user/logout";
                String method = "GET";
                HttpTask httpTask = new HttpTask("", logoutURL, method, logoutListener);
                httpTask.execute();
            }
        });
        return view;
    }

    private OnAsyncTaskListener logoutListener = new OnAsyncTaskListener() {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            account = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = account.edit();
            editor.clear();
            editor.commit();

            getActivity().finish();
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);

        }
    };

}
