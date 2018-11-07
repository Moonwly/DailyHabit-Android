package com.example.wly.dailyhabit_android.ConnectServer;

import android.util.Log;

import com.example.wly.dailyhabit_android.Info.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class WebConnect {
    private static String headUrl = "http://118.25.73.227/api";

    private static String response(int code, String msg) {
        return "{ \"code\":" + code + ", \"msg\":\"" + msg + "\"}";
    }

    static String postGetJson(String transValue, String url, String method) {
        try {
            URL mUrl = new URL(headUrl + url);
            HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
            mHttpURLConnection.setConnectTimeout(15000);
            mHttpURLConnection.setReadTimeout(15000);
            mHttpURLConnection.setRequestMethod(method);
            if (User.session != null)
                mHttpURLConnection.setRequestProperty("Cookie", User.session);
            else
                mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            mHttpURLConnection.setDoInput(true);
            mHttpURLConnection.setDoOutput(true);
            mHttpURLConnection.setUseCaches(false);
            mHttpURLConnection.connect();

            DataOutputStream dos = new DataOutputStream(mHttpURLConnection.getOutputStream());
            dos.write(transValue.getBytes());
            dos.flush();
            dos.close();

            int respondCode = mHttpURLConnection.getResponseCode();
            Log.d("respondCode","respondCode="+respondCode );
            String type = mHttpURLConnection.getContentType();
            Log.d("type", "type="+type);
            String encoding = mHttpURLConnection.getContentEncoding();
            Log.d("encoding", "encoding="+encoding);
            int length = mHttpURLConnection.getContentLength();
            Log.d("length", "length=" + length);
            String cookieValue = mHttpURLConnection.getHeaderField("Set-Cookie");
            System.out.println("cookie value:" + cookieValue);
            if (cookieValue != null) {
                try {
                    User.session = cookieValue.substring(0, cookieValue.indexOf(";"));
                } catch (Exception e) {
                    return response(300, "获取session错误");
                }
            }

            if (respondCode == 200) {
                InputStream is = mHttpURLConnection.getInputStream();
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                int len;
                byte buffer[] = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    message.write(buffer, 0, len);
                }
                is.close();
                message.close();
                String msg = new String(message.toByteArray());
                return msg;
            }
            return response(200, "连接错误");
        }catch(Exception e) {
            System.out.print(e);
            return response(100, "无法连接");
        }
    }
}

