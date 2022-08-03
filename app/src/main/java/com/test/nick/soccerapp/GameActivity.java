package com.test.nick.soccerapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    public static final int HERO_ONE = 0;
    public static final int HERO_TWO = 1;
    public static final int HERO_THREE = 2;
    public static final int HERO_FOUR = 3;
    public static final int HERO_FIVE = 4;
    public static final int HERO_SIX = 5;
    public static final int HERO_SEVEN = 6;
    public static final int HERO_EIGHT = 7;

    public static final int NUM_HEROS = 7;
    public static final int MAX_HEROS = 4;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg){
            Log.d(TAG, "incoming message");
            byte[] readBuf  = (byte[]) msg.obj;
            switch (msg.what){
                case 0:
                    switch (readBuf[0]){
                        case HERO_ONE:
                            gameView.add(new Witch(getResources(), true, readBuf[1]!=1));
                            break;
                        case HERO_TWO:
                            gameView.add(new Fighter(getResources(), true, readBuf[1]!=1));
                            break;
                        case HERO_THREE:
                            gameView.add(new Robot(getResources(), true, readBuf[1]!=1));
                            break;
                        case HERO_FOUR:
                            gameView.add(new Slime(getResources(), true, readBuf[1]!=1));
                            break;
                        case HERO_FIVE:
                            gameView.add(new Goblin(getResources(), true, readBuf[1]!=1));
                            break;
                        case HERO_SIX:
                            gameView.add(new Fireball(getResources(), true, readBuf[1]!=1));
                            break;
                        case HERO_SEVEN:
                            gameView.add(new Catapult(getResources(), true, readBuf[1]!=1, gameView));
                            break;
                        case HERO_EIGHT:
                            gameView.add(new Rock(getResources(), true, readBuf[1]!=1));
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

    private int typeSelected = 50;
    private int mana = 6;

    private final int[] defaultTiles = {R.drawable.chartile_1, R.drawable.chartile_2, R.drawable.chartile_3,
            R.drawable.chartile_4, R.drawable.chartile_5, R.drawable.chartile_6, R.drawable.chartile_7};
    private final int[] selectedTiles = {R.drawable.chartile_selected_1, R.drawable.chartile_selected_2,
            R.drawable.chartile_selected_3, R.drawable.chartile_selected_4, R.drawable.chartile_selected_5,
            R.drawable.chartile_selected_6, R.drawable.chartile_selected_7};
    private int[] currentChars = new int[MAX_HEROS];

    private ConnectedThread messageThread;
    private final Random rng = new Random();

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

        for (int i = 0; i < MAX_HEROS; i++) {
            currentChars[i] = rng.nextInt(NUM_HEROS);
        }
        clearTiles();

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
        }, 1500, 1500);

        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    int selectedTile = getSelectedTile();
                    if (selectedTile < 4) {
                        currentChars[selectedTile] = rng.nextInt(NUM_HEROS);
                    }

                    resetTiles();
                    clearTiles();

                    if(event.getX()< Resources.getSystem().getDisplayMetrics().widthPixels/2){
                        switch (typeSelected){
                            case HERO_ONE:
                                if(getMana()>=4) {
                                    gameView.add(new Witch(getResources(), false, true));
                                    setMana(getMana() - 4);
                                }
                                break;
                            case HERO_TWO:
                                if(getMana()>=4) {
                                    gameView.add(new Fighter(getResources(), false, true));
                                    setMana(getMana() - 4);
                                }
                                break;
                            case HERO_THREE:
                                if(getMana()>=6) {
                                    gameView.add(new Robot(getResources(), false, true));
                                    setMana(getMana() - 6);
                                }
                                break;
                            case HERO_FOUR:
                                if(getMana()>=3) {
                                    gameView.add(new Slime(getResources(), false, true));
                                    setMana(getMana() - 3);
                                }
                                break;
                            case HERO_FIVE:
                                if(getMana()>=3) {
                                    gameView.add(new Goblin(getResources(), false, true));
                                    setMana(getMana() - 3);
                                }
                                break;
                            case HERO_SIX:
                                if(getMana()>=3) {
                                    gameView.add(new Fireball(getResources(), false, true));
                                    setMana(getMana() - 3);
                                }
                                break;
                            case HERO_SEVEN:
                                if(getMana()>=6) {
                                    gameView.add(new Catapult(getResources(), false, true, gameView));
                                    setMana(getMana() - 6);
                                }
                                break;
                            default: break;
                        }
                    }
                    else{
                        switch (typeSelected){
                            case HERO_ONE:
                                if(getMana()>=4) {
                                    gameView.add(new Witch(getResources(), false, false));
                                    setMana(getMana() - 4);
                                }
                                break;
                            case HERO_TWO:
                                if(getMana()>=4) {
                                    gameView.add(new Fighter(getResources(), false, false));
                                    setMana(getMana() - 4);
                                }
                                break;
                            case HERO_THREE:
                                if(getMana()>=6) {
                                    gameView.add(new Robot(getResources(), false, false));
                                    setMana(getMana() - 6);
                                }
                                break;
                            case HERO_FOUR:
                                if(getMana()>=3) {
                                    gameView.add(new Slime(getResources(), false, false));
                                    setMana(getMana() - 3);
                                }
                                break;
                            case HERO_FIVE:
                                if(getMana()>=3) {
                                    gameView.add(new Goblin(getResources(), false, false));
                                    setMana(getMana() - 3);
                                }
                                break;
                            case HERO_SIX:
                                if(getMana()>=3) {
                                    gameView.add(new Fireball(getResources(), false, false));
                                    setMana(getMana() - 3);
                                }
                                break;
                            case HERO_SEVEN:
                                if(getMana()>=6) {
                                    gameView.add(new Catapult(getResources(), false, true, gameView));
                                    setMana(getMana() - 6);
                                }
                                break;
                            default: break;
                        }
                    }
                    typeSelected = 50;
                }
                return true;
            }
        });
    }

    public void charSelected(View view) {
        resetTiles();
        clearTiles();
        view.setSelected(true);
        switch (view.getId()){
            case R.id.tile1:
                Log.d(TAG, "charSelected: Tile1");
                typeSelected = currentChars[0];
                ((ImageView)view).setImageResource(selectedTiles[typeSelected]);
                break;
            case R.id.tile2:
                Log.d(TAG, "charSelected: Tile2");
                typeSelected = currentChars[1];
                ((ImageView)view).setImageResource(selectedTiles[typeSelected]);
                break;
            case R.id.tile3:
                Log.d(TAG, "charSelected: Tile3");
                typeSelected = currentChars[2];
                ((ImageView)view).setImageResource(selectedTiles[typeSelected]);
                break;
            case R.id.tile4:
                Log.d(TAG, "charSelected: Tile4");
                typeSelected = currentChars[3];
                ((ImageView)view).setImageResource(selectedTiles[typeSelected]);
                break;
        }
    }

    private void resetTiles(){
        View resetView = findViewById(R.id.tile1);
        if(resetView.isSelected()){
            resetView.setSelected(false);
        }

        resetView = findViewById(R.id.tile2);
        if(resetView.isSelected()){
            resetView.setSelected(false);
        }

        resetView = findViewById(R.id.tile3);
        if(resetView.isSelected()){
            resetView.setSelected(false);
        }

        resetView = findViewById(R.id.tile4);
        if(resetView.isSelected()){
            resetView.setSelected(false);
        }
    }

    private void clearTiles() {
        View resetView = findViewById(R.id.tile1);
        ((ImageView)resetView).setImageResource(defaultTiles[currentChars[0]]);

        resetView = findViewById(R.id.tile2);
        ((ImageView)resetView).setImageResource(defaultTiles[currentChars[1]]);

        resetView = findViewById(R.id.tile3);
        ((ImageView)resetView).setImageResource(defaultTiles[currentChars[2]]);

        resetView = findViewById(R.id.tile4);
        ((ImageView)resetView).setImageResource(defaultTiles[currentChars[3]]);
    }

    private int getSelectedTile() {
        View resetView = findViewById(R.id.tile1);
        if(resetView.isSelected()){
            return 0;
        }

        resetView = findViewById(R.id.tile2);
        if(resetView.isSelected()){
            return 1;
        }

        resetView = findViewById(R.id.tile3);
        if(resetView.isSelected()){
            return 2;
        }

        resetView = findViewById(R.id.tile4);
        if(resetView.isSelected()){
            return 3;
        }

        return 4;
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