package com.test.nick.soccerapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class BluetoothConnectService {

    private final GlobalApplication globalApp;

    private static final String TAG = "BluetoothConnectService";
    private static final String NAME = "SoccerGame";
    private static final UUID MY_UUID =
            UUID.fromString("16d02b8e-ec1e-4a00-bc20-ad8a84416b04");

    private final BluetoothAdapter mBluetoothAdapter;
    private Context context;

    private transient AcceptThread acceptThread;
    private transient ConnectThread connectThread;
    private Handler handler;

    public BluetoothConnectService(Context context, Handler h, GlobalApplication app){
        this.context = context;
        handler = h;
        globalApp = app;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }

    //Starts the connection service by causing the AcceptThread to begin
    //Listening in server mode.
    public synchronized void start(){
        Log.d(TAG, "starting bt connection");

        if(connectThread != null){
            connectThread.cancel();
            connectThread = null;
        }

        if (acceptThread == null){
            acceptThread = new AcceptThread();
            acceptThread.start();
        }
    }

    //Starts ConnectThread to connect as client to remote device.
    public synchronized void connect(BluetoothDevice device){
        Log.d(TAG, "connect: connecting to "+ device);

        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        connectThread = new ConnectThread(device);
        connectThread.start();
    }

    //cancels server and client threads once a socket is connected
    public synchronized void connected(BluetoothSocket socket) {
        Log.d(TAG, "connected");

        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (acceptThread != null){
            acceptThread.cancel();
            acceptThread = null;
        }

        //sets socket in global application
        globalApp.setSocket(socket);
        //sends homeActivity connected message
        handler.sendEmptyMessage(0);
        this.stop();
    }

    //cancels threads
    public synchronized void stop(){
        Log.d(TAG, "stop: stopping threads");
        
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (acceptThread != null){
            acceptThread.cancel();
            acceptThread = null;
        }
    }


    //server thread
    private class AcceptThread extends Thread{
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket temp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                temp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = temp;
        }

        public void run() {
            Log.d(TAG, "Starting AcceptThread");
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            try {

                socket = mmServerSocket.accept();
                Log.d(TAG, "RFCOM server socket accepted");
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
            }

            if (socket != null) {
                try {
                    connected(socket);
                    mmServerSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing serverSocket",e );
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            Log.d(TAG, "canceling acceptThread");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    //client thread
    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            Log.d(TAG, "Starting ConnectThread");
            BluetoothSocket tmp = null;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }
            connected(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            Log.d(TAG, "canceling connectThread");
            try {
                if(!mmSocket.isConnected()){mmSocket.close();}
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
}
