package com.test.nick.soccerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        TextView textView = findViewById(R.id.gameover);
        if (getIntent().getIntExtra("winlose", 0)==0){
            textView.setText("defeat");
        } else {
            textView.setText("victory");
        }
    }
}
