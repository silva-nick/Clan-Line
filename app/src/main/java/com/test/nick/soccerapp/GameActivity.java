package com.test.nick.soccerapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    public static final int HERO_ONE = 0;
    public static final int HERO_TWO = 1;
    public static final int HERO_THREE = 2;
    public static final int HERO_FOUR = 3;


    @SuppressLint("HandlerLeak")
    private final Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg){
            Log.d(TAG, "incoming message");
            byte[] readBuf  = (byte[]) msg.obj;
            switch (msg.what){
                case 0:
                    switch (readBuf[0]){
                        case 0:
                            gameView.add(new Witch(getResources(), true, readBuf[1]!=1));
                            break;
                        case 1:
                            gameView.add(new Fighter(getResources(), true, readBuf[1]!=1));
                            break;
                        case 2:
                            gameView.add(new Robot(getResources(), true, readBuf[1]!=1));
                            break;
                        case 3:
                            gameView.add(new Slime(getResources(), true, readBuf[1]!=1));
                            break;
                        default:
                            break;
                    }

                    break;
                case 1:
                    Intent intent = new Intent(GameActivity.this, EndActivity.class);
                    intent.putExtra("winlose", readBuf[0]+2);
                    if (readBuf[0]!=-2) {messageThread.write(new byte[]{-2});}
                    startActivity(intent);
                    finish();
                    break;
            }


        }
    };
    private GameView gameView;

    private int typeSelected = 5;
    private int mana = 6;

    private ConnectedThread messageThread;


    @Override
    protected void onDestroy(){
        Log.d(TAG, "onDestroy: destroying game activity");
        super.onDestroy();
        messageThread.cancel();
        messageThread = null;
        gameView.destroy();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d(TAG, "Starting GameActivity");
        GlobalApplication app = (GlobalApplication)getApplication();
        messageThread = new ConnectedThread(app.socket, mHandler);
        messageThread.start();
        gameView = findViewById(R.id.gameView);
        gameView.sendThread(messageThread);
        gameView.add(new Base(getResources(), false, true));

        Timer manaTimer = new Timer();
        manaTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(getMana()<10) {
                            setMana(getMana() + 1);
                        }
                    }
                });
            }
        }, 2000, 2000);

        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    resetTiles();
                    if(event.getX()< Resources.getSystem().getDisplayMetrics().widthPixels/2){
                        switch (typeSelected){
                            case 0:
                                if(getMana()>=4) {
                                    gameView.add(new Witch(getResources(), false, true));
                                    setMana(getMana() - 4);
                                }
                                break;
                            case 1:
                                if(getMana()>=4) {
                                    gameView.add(new Fighter(getResources(), false, true));
                                    setMana(getMana() - 4);
                                }
                                break;
                            case 2:
                                if(getMana()>=6) {
                                    gameView.add(new Robot(getResources(), false, true));
                                    setMana(getMana() - 6);
                                }
                                break;
                            case 3:
                                if(getMana()>=3) {
                                    gameView.add(new Slime(getResources(), false, true));
                                    setMana(getMana() - 3);
                                }
                                break;
                            default: break;
                        }
                    }
                    else{
                        switch (typeSelected){
                            case 0:
                                if(getMana()>=4) {
                                    gameView.add(new Witch(getResources(), false, false));
                                    setMana(getMana() - 4);
                                }
                                break;
                            case 1:
                                if(getMana()>=4) {
                                    gameView.add(new Fighter(getResources(), false, false));
                                    setMana(getMana() - 4);
                                }
                                break;
                            case 2:
                                if(getMana()>=6) {
                                    gameView.add(new Robot(getResources(), false, false));
                                    setMana(getMana() - 6);
                                }
                                break;
                            case 3:
                                if(getMana()>=3) {
                                    gameView.add(new Slime(getResources(), false, false));
                                    setMana(getMana() - 3);
                                }
                                break;
                            default: break;
                        }
                    }
                    typeSelected = 5;
                }
                return true;
            }
        });
    }

    public void charSelected(View view) {
        resetTiles();
        view.setSelected(true);
        switch (view.getId()){
            case R.id.tile1:
                Log.d(TAG, "charSelected: Tile1");
                ((ImageView)view).setImageResource(R.drawable.chartile_selected_1);
                typeSelected = 0;
                break;
            case R.id.tile2:
                Log.d(TAG, "charSelected: Tile2");
                ((ImageView)view).setImageResource(R.drawable.chartile_selected_2);
                typeSelected = 1;
                break;
            case R.id.tile3:
                Log.d(TAG, "charSelected: Tile3");
                ((ImageView)view).setImageResource(R.drawable.chartile_selected_3);
                typeSelected = 2;
                break;
            case R.id.tile4:
                Log.d(TAG, "charSelected: Tile4");
                ((ImageView)view).setImageResource(R.drawable.chartile_selected_4);
                typeSelected = 3;
                break;
        }
    }

    private void resetTiles(){
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
    }

    public int getMana(){
        return mana;
    }

    public void setMana(int x){
        mana = x;
        ImageView manaView = findViewById(R.id.mana_bar);
        TextView manaText = findViewById(R.id.mana_text);

        manaText.setText(getMana()+"");
        switch (mana){
            case 0:
                manaView.setImageResource(R.drawable.mana_0);
                break;
            case 1:
                manaView.setImageResource(R.drawable.mana_1);
                break;
            case 2:
                manaView.setImageResource(R.drawable.mana_2);
                break;
            case 3:
                manaView.setImageResource(R.drawable.mana_3);
                break;
            case 4:
                manaView.setImageResource(R.drawable.mana_4);
                break;
            case 5:
                manaView.setImageResource(R.drawable.mana_5);
                break;
            case 6:
                manaView.setImageResource(R.drawable.mana_6);
                break;
            case 7:
                manaView.setImageResource(R.drawable.mana_7);
                break;
            case 8:
                manaView.setImageResource(R.drawable.mana_8);
                break;
            case 9:
                manaView.setImageResource(R.drawable.mana_9);
                break;
            case 10:
                manaView.setImageResource(R.drawable.mana_10);
                break;
            default:
                manaView.setImageResource(R.drawable.mana_0);
        }
    }
}