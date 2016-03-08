package com.boyukliev.kestrel_app.ui.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.Observer;

/**
 * Created by Admin on 3/8/2016.
 */
public interface BluetoothConnection {
    void addObserver(Observer observer);
    void deleteObserver(Observer observer);
    void scanForDevices(boolean enable);
    void connectToDevice(BluetoothDevice device);
    void disconnect();
    void setOnReceivedValuesListener(OnReceivedValuesListener onReceivedValuesListener);

    interface OnReceivedValuesListener {
        void onReceivedAltitude(String altitude);
    }
}
