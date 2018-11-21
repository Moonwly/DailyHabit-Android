package com.example.wly.dailyhabit_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wly.dailyhabit_android.ConnectServer.HttpTask;
import com.example.wly.dailyhabit_android.ConnectServer.OnAsyncTaskListener;
import com.example.wly.dailyhabit_android.Info.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class RegisterActivity extends AppCompatActivity{
    private EditText email;
    private EditText password;
    private Button registerBtn;
    private Context context;
    private SharedPreferences account;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;

        email = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);
        account = PreferenceManager.getDefaultSharedPreferences(this);
        registerBtn = (Button) findViewById(R.id.register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRegister();
            }
        });
    }

    private void userRegister() {
        try {
            String registerURL = "/user/create";
            String method = "POST";
            User.email = email.getText().toString();
            User.password = password.getText().toString();
            JSONObject userJSON = new JSONObject();
            userJSON.put("email", User.email);
            userJSON.put("password", User.password);
            String transValue = userJSON.toString();
            HttpTask httpTask = new HttpTask(transValue, registerURL, method, registerListener);
            httpTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private OnAsyncTaskListener registerListener = new OnAsyncTaskListener() {
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
                User.id = body.getInt("user_id");
                User.username = body.getString("username");

                SharedPreferences.Editor editor = account.edit();
                editor.putString("email", User.email);
                editor.putString("password", User.password);
                editor.putInt("id", User.id);
                editor.putString("username", User.username);
                editor.putString("session", User.session);
                editor.commit();
                finish();
                Intent mainIntent = new Intent(context, MainActivity.class);
                startActivity(mainIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
