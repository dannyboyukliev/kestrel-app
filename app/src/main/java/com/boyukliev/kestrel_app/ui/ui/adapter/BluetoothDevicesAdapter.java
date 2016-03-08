package com.boyukliev.kestrel_app.ui.ui.adapter;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Admin on 3/8/2016.
 */
public interface BluetoothDevicesAdapter {
    interface OnAdapterDeviceClickListener {
        void onAdapterDeviceItemClick(BluetoothDevice device);
    }

    String CONNECTED_DEVICE_DETAIL = "Connected";

    void notifyDataUpdate();

    void addDevice(BluetoothDevice device, Integer rssi);

    void clear();

    BluetoothDevice getDevice(int position);

    void chooseDevice(int position);

    void chooseDevice(BluetoothDevice device);

    BluetoothDevice getChosenDevice();

    void setOnAdapterDeviceClickListener(BluetoothDevicesAdapter.OnAdapterDeviceClickListener onAdapterDeviceClickListener);
}

