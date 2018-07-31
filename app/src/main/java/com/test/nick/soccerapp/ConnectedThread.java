package com.test.nick.soccerapp;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread{
    private final BluetoothSocket mSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private Handler messageSendHandler;

    private static final String TAG = "ConnectedThread";

    public ConnectedThread(BluetoothSocket socket, Handler mHandler){
        Log.d(TAG, "ConnectThread: started Connected thread");
        messageSendHandler = mHandler;
        mSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try{
            tmpIn = mSocket.getInputStream();
        }
        catch(IOException e){
            Log.e(TAG, "ConnectThread: Input stream connection error",e );
        }
        try{
            tmpOut = mSocket.getOutputStream();
        }
        catch(IOException e){
            Log.e(TAG, "ConnectThread: output stream connection error",e );
        }
        inputStream = tmpIn;
        outputStream = tmpOut;

        Log.d(TAG, "BTSocket, inputstream, outputstream "+(mSocket.isConnected())+(inputStream!=null)+(outputStream!=null));
    }

    public void run(){
        byte[] buffer = new byte[1024];
        int numBytes;
        Log.d(TAG, "run: asdf");
        while(true){
            try {
                numBytes = inputStream.read(buffer);
                Log.d(TAG, "message received");
                if(buffer[0]<0){
                    messageSendHandler.obtainMessage(1, numBytes, -1, buffer)
                            .sendToTarget();
                } else {
                    messageSendHandler.obtainMessage(0, numBytes, -1, buffer)
                            .sendToTarget();
                }

            } catch (IOException e) {
                Log.e(TAG, "run: input read error ", e);
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
            Log.d(TAG, "write: writing to the outputstream");
        } catch (IOException e) {
            Log.e(TAG, "write: Error writing to output stream. ", e );
        }
    }

    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
