package com.example.class_schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.class_schedule.account.TeacherSignInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{
    @BindView(R.id.creatingButton)
    Button mCreatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
    }

//    @Override
//    public void onClick(View v) {
//        if (v == mCreatingButton){
//            Intent intent = new Intent(MainActivity.this, TeacherSignInActivity.class);
//            startActivity(intent);
//        }
//
//    }
}