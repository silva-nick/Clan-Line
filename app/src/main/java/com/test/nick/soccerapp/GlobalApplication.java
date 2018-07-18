package com.test.nick.soccerapp;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

public class GlobalApplication extends Application {
    BluetoothSocket socket;

    public void setSocket(BluetoothSocket socket){this.socket = socket;}
}
