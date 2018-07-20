package com.test.nick.soccerapp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    private final Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg){
            Log.d(TAG, "incoming message");
            byte[] readBuf  = (byte[]) msg.obj;
            String writeMessage = new String(readBuf, 0, msg.arg1);

        }
    };
    private GameView gameView;


    private ConnectedThread messageThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d(TAG, "Starting GameActivity");
        GlobalApplication app = (GlobalApplication)getApplication();
        gameView = findViewById(R.id.gameView);
        //messageThread = new ConnectedThread(app.socket, mHandler);
        //messageThread.start();
    }

    public void charSelected(View view) {
        View resetView = findViewById(R.id.tile1);
        if(resetView.isSelected()){
            resetView.setSelected(false);
            ((ImageView)resetView).setImageResource(R.drawable.chartile_1);
        }
        resetView = findViewById(R.id.tile2);
        if(resetView.isSelected()){
            resetView.setSelected(false);
            ((ImageView)resetView).setImageResource(R.drawable.chartile_2);
        }
        resetView = findViewById(R.id.tile3);
        if(resetView.isSelected()){
            resetView.setSelected(false);
            ((ImageView)resetView).setImageResource(R.drawable.chartile_3);
        }
        resetView = findViewById(R.id.tile4);
        if(resetView.isSelected()){
            resetView.setSelected(false);
            ((ImageView)resetView).setImageResource(R.drawable.chartile_4);
        }
        view.setSelected(true);
        switch (view.getId()){
            case R.id.tile1:
                Log.d(TAG, "charSelected: Tile1");
                ((ImageView)view).setImageResource(R.drawable.chartile_selected_1);
                gameView.add(new Robot(getResources(), false, true));
                break;
            case R.id.tile2:
                Log.d(TAG, "charSelected: Tile2");
                ((ImageView)view).setImageResource(R.drawable.chartile_selected_2);
                gameView.add(new Robot(getResources(), true, false));
                break;
            case R.id.tile3:
                Log.d(TAG, "charSelected: Tile3");
                ((ImageView)view).setImageResource(R.drawable.chartile_selected_3);
                break;
            case R.id.tile4:
                Log.d(TAG, "charSelected: Tile4");
                ((ImageView)view).setImageResource(R.drawable.chartile_selected_4);
                break;
        }
    }
}