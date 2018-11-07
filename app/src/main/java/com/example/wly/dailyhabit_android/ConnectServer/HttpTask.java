package com.example.wly.dailyhabit_android.ConnectServer;

import android.content.Context;
import android.os.AsyncTask;


public class HttpTask extends AsyncTask<Void, Integer, String> {
    private String transValue;
    private String url;
    private String method;
    private OnAsyncTaskListener listener;
    public HttpTask(String transValue, String url, String method, OnAsyncTaskListener listener) {
        this.transValue = transValue;
        this.url = url;
        this.method = method;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return WebConnect.postGetJson(transValue, url, method);
    }

    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        listener.onSuccess(str);
    }
}
