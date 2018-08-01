package com.test.nick.soccerapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = "HomeActivity";
    private GlobalApplication globalApplication;

    private ArrayList<BluetoothDevice> newDeviceList = new ArrayList<BluetoothDevice>();
    private ListView listView;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothConnectService mBluetoothConnection;
    private static final UUID MY_UUID =
            UUID.fromString("16d02b8e-ec1e-4a00-bc20-ad8a84416b04");
    private BluetoothDevice mBTDevice;

    private final Handler connectedHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){leaveActivity();}
        }
    };

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "OnReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "OnReceive: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "On Receive: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "On Receive: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d(TAG, "OnReceive: found device");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                newDeviceList.add(device);
                BTItemAdapter btAdapter = new BTItemAdapter();
                listView.setAdapter(btAdapter);
            }
        }
    };

        class BTItemAdapter extends BaseAdapter{

            @Override
            public int getCount() {
                return newDeviceList.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = getLayoutInflater().inflate(R.layout.btdevicelayout, null);

                ImageView imageView = view.findViewById(R.id.imageView);
                TextView textViewName = view.findViewById(R.id.textView_name);

                if(newDeviceList.get(i).getName()!=null)textViewName.setText(newDeviceList.get(i).getName());
                else{textViewName.setText(newDeviceList.get(i).getAddress());}
                return view;
            }
        }


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(mBluetoothDevice.getBondState()==BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "onReceive: BondBonded");
                    mBTDevice = mBluetoothDevice;
                    mBluetoothConnection = new BluetoothConnectService(HomeActivity.this, connectedHandler, globalApplication);
                    startBTConnection(mBTDevice, MY_UUID);
                }
                if(mBluetoothDevice.getBondState()==BluetoothDevice.BOND_BONDING){
                    Log.d(TAG, "onReceive: BOND_BONDING");
                }
                if(mBluetoothDevice.getBondState()==BluetoothDevice.BOND_NONE){
                    Log.d(TAG, "onReceive: BOND_NONE");
                }
            }
        }
    };


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 0.9f);
        animation1.setDuration(2000);
        animation1.setStartOffset(1000);
        animation1.setFillAfter(true);
        findViewById(R.id.connect_text).startAnimation(animation1);

        globalApplication = ((GlobalApplication)getApplication());
        Log.d(TAG, "onCreate: THE APP IS "+(globalApplication==null));
        //turns on discoverability on startup
        enableDisableConnect();

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(HomeActivity.this);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: starting connection");
        mBluetoothConnection.connect(mBTDevice);
    }

    //Method that changes bton/off status
    /*public void enableDisableBT(){
        if(mBluetoothAdapter == null){
            Log.d(TAG,"enableDisableBT: Does not have BT");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver, BTIntent);
        }
        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver, BTIntent);
        }
    }*/

    public void enableDisableConnect() {
        Log.d(TAG, "enableDisableConnect: Making device discoverable");

        Intent connectableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        connectableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(connectableIntent);

        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, intentFilter);
    }

    public void discoverDevices(View view) {
        Log.d(TAG, "Discovering Devices");

        ((LinearLayout)view).removeView(findViewById(R.id.connect_text));

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, intentFilter);
        }else if (!mBluetoothAdapter.isDiscovering()){
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, intentFilter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mBluetoothAdapter.cancelDiscovery();
        Log.d(TAG, "onItemClick: device chosen");
        Log.d(TAG, "onItemClick: trying to pair with " + newDeviceList.get(i).getName());
        ImageView avdView = view.findViewById(R.id.imageView);
        Drawable d = avdView.getDrawable();
        AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat)d;
        avd.start();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        mBTDevice = newDeviceList.get(i);
        if(Build.VERSION.SDK_INT >= 19 ){
            mBTDevice.createBond();
        }
        if(pairedDevices.contains(newDeviceList.get(i))){
            Log.d(TAG, "Devices already paired");
            mBluetoothConnection = new BluetoothConnectService(HomeActivity.this, connectedHandler, globalApplication);
            startBTConnection(mBTDevice,MY_UUID);
        }
    }

    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > 22){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < 22.");
        }
    }

    private void leaveActivity(){
        Intent leaveIntent = new Intent(HomeActivity.this, GameActivity.class);
        startActivity(leaveIntent);
    }
}
