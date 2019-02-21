package com.example.calendardemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import butterknife.ButterKnife;

/**
 * Created by lxc on 2019/2/19
 * e-mail ：18867762063@163.com
 */
public class BaseActivity extends AppCompatActivity {
    protected String TAG = "";

    protected LinearLayout mBaseLayout;
    private ConstraintLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        initBaseView();
    }

    private void initBaseView() {
        super.setContentView(R.layout.base_activity);
        this.mBaseLayout = (LinearLayout)this.findViewById(R.id.content);
        this.container = (ConstraintLayout)this.findViewById(R.id.container);
    }

    public void setContentView(int layoutResID){
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(layoutResID, this.mBaseLayout);
        ButterKnife.bind(this);
    }

    public void startCOActivity(Class<?> c) {
        Intent intent = new Intent(this, c);
        this.startActivity(intent);
    }
}
