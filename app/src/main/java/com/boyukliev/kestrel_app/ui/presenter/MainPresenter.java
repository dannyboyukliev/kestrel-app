package com.boyukliev.kestrel_app.ui.presenter;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.boyukliev.kestrel_app.ui.bluetooth.BluetoothConnection;
import com.boyukliev.kestrel_app.ui.bluetooth.BluetoothConnectionImpl;
import com.boyukliev.kestrel_app.ui.presenter.base.BasePresenter;
import com.boyukliev.kestrel_app.ui.ui.view.MainView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Admin on 3/8/2016.
 */
public class MainPresenter implements BasePresenter<MainView>, Observer, BluetoothConnection.OnReceivedValuesListener {

    private BluetoothConnection bluetoothConnection;

    private MainView view;

    public MainPresenter(BluetoothConnection bluetoothConnection) {
        this.bluetoothConnection = bluetoothConnection;
        this.bluetoothConnection.addObserver(this);
    }

    @Override
    public void bindView(MainView mainView) {
        this.view = mainView;

        bluetoothConnection.scanForDevices(true);
        bluetoothConnection.setOnReceivedValuesListener(this);
    }

    @Override
    public void destroy() {
        this.bluetoothConnection.disconnect();
        this.bluetoothConnection.deleteObserver(this);
    }

    @Override
    public void update(Observable observable, final Object o) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (o.equals(BluetoothConnectionImpl.FLAG_START_SCANNING)) {
                    view.addToast("Start scanning", Toast.LENGTH_SHORT);
                } else if (o.equals(BluetoothConnectionImpl.FLAG_STOP_SCANNING)) {
                    view.addToast("Stop scanning", Toast.LENGTH_SHORT);
                } else if (o.equals(BluetoothConnectionImpl.FLAG_CONNECT_WITH_DEVICE)) {
                    view.addToast("Connect with device", Toast.LENGTH_SHORT);
                } else if (o.equals(BluetoothConnectionImpl.FLAG_CONNECTED)) {
                    view.addToast("Connected", Toast.LENGTH_SHORT);
                } else if (o.equals(BluetoothConnectionImpl.FLAG_DISCONNECT)) {
                    view.addToast("Disconnect", Toast.LENGTH_SHORT);
                } else if (o.equals(BluetoothConnectionImpl.FLAG_DISCONNECTED)) {
                    view.addToast("Disconnected", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public void onReceivedAltitude(final String altitude) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                view.displayText(altitude + "m");
            }
        });
    }
}
