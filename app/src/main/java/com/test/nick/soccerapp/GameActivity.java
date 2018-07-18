package com.test.nick.soccerapp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    private final Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg){
            Log.d(TAG, "incoming message");
            byte[] readBuf  = (byte[]) msg.obj;
            String writeMessage = new String(readBuf, 0, msg.arg1);;

        }
    };


    private ConnectedThread messageThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
        Log.d(TAG, "Starting GameActivity");
        GlobalApplication app = (GlobalApplication)getApplication();
        //messageThread = new ConnectedThread(app.socket, mHandler);
        //messageThread.start();
    }

}