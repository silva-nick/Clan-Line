package com.test.nick.soccerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        TextView textView = findViewById(R.id.gameover);
        ImageView imageView = findViewById(R.id.endbackground);
        if (getIntent().getIntExtra("winlose", 0)==0){
            textView.setText("defeat");
            imageView.setImageResource(R.drawable.winscreen);
        } else {
            textView.setText("victory");
            imageView.setImageResource(R.drawable.losescreen);
        }
    }
}
