package com.example.wly.dailyhabit_android;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MineFragment extends Fragment {
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, null);
        context = getActivity().getApplicationContext();
        initInterface();
        return view;
    }

    private void initInterface() {
    }
}
