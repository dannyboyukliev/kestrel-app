package com.boyukliev.kestrel_app.ui.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.boyukliev.kestrel_app.BuildConfig;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by Admin on 3/8/2016.
 */
public class BluetoothConnectionImpl extends Observable implements BluetoothConnection {

    public static final String FLAG_START_SCANNING = "start_scanning";
    public static final String FLAG_STOP_SCANNING = "stop_scanning";
    public static final String FLAG_CONNECT_WITH_DEVICE = "connect_to_device";
    public static final String FLAG_DISCONNECT = "disconnect";
    public static final String FLAG_CONNECTED = "connected";
    public static final String FLAG_DISCONNECTED = "disconnected";

    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = "BluetoothConnection";
    private static final long SCAN_PERIOD = 10000;
    private static final long CONNECTING_TIMEOUT = 7000;
    private static final long CONNECTING_DELAY = 1000;

    private Context context;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;

    private Handler handler;

    private boolean isScanning = false;
    private boolean isConnecting = false;
    private boolean isConnected = false;

    private Timer timer;

    private OnReceivedValuesListener onReceivedValuesListener;

    public BluetoothConnectionImpl(Context context) {
        this.context = context;
        this.bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();

        this.handler = new Handler();
    }

    @Override
    public void scanForDevices(boolean enable) {
        if (enable) {
            Runnable stopScanRunnable = new Runnable() {
                @Override
                public void run() {
                    if (bluetoothAdapter != null && leScanCallback != null) {
                        bluetoothAdapter.stopLeScan(leScanCallback);
                        isScanning = false;
                        setChanged();
                        notifyObservers(FLAG_STOP_SCANNING);
                    }
                }
            };
            handler.postDelayed(stopScanRunnable, SCAN_PERIOD);

            bluetoothAdapter.startLeScan(leScanCallback);
            isScanning = true;
            setChanged();
            notifyObservers(FLAG_START_SCANNING);
        } else {
            bluetoothAdapter.stopLeScan(leScanCallback);
            isScanning = false;
            setChanged();
            notifyObservers(FLAG_STOP_SCANNING);
        }
    }

    @Override
    public void connectToDevice(final BluetoothDevice device) {
        if (!isConnecting && !isConnected) {
            if (DEBUG) Log.i(TAG, "Connect to device: " + device.getName());
            if (bluetoothGatt != null) {
                bluetoothGatt.disconnect();
            }

            isConnecting = true;

            setConnectingTimeout(CONNECTING_TIMEOUT);

            if (isConnected || isConnecting) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        disconnect();
                    }
                });
            }

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothGatt = device.connectGatt(context, false, gattCallback);
                    setChanged();
                    notifyObservers(FLAG_CONNECT_WITH_DEVICE);
                }
            }, CONNECTING_DELAY);
        }
    }

    @Override
    public void disconnect() {
        if (bluetoothGatt != null) {
            if (bluetoothGatt.getServices() != null) {
                for (BluetoothGattService service : bluetoothGatt.getServices()) {
                    if (service.getCharacteristics() != null && service.getUuid().equals(KestrelUuids.KESTREL_MEASUREMENT_SERVICE)) {
                        for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                            if (characteristic != null && characteristic.getUuid().equals(KestrelUuids.SENSOR_MEASUREMENT)) {
                                setCharacteristicNotifications(characteristic, false);
                            }
                        }
                    }
                }
            }
            bluetoothGatt.disconnect();

            setChanged();
            notifyObservers(FLAG_DISCONNECT);
        }
    }

    @Override
    public void setOnReceivedValuesListener(OnReceivedValuesListener onReceivedValuesListener) {
        this.onReceivedValuesListener = onReceivedValuesListener;
    }

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    isConnecting = false;
                    isConnected = true;
                    gatt.discoverServices();
                    bluetoothGatt = gatt;
                    cancelTimeout();
                    setChanged();
                    notifyObservers(FLAG_CONNECTED);
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    isConnecting = true;
                    isConnected = false;
                    gatt.disconnect();
                    bluetoothGatt = null;
                    setChanged();
                    notifyObservers(FLAG_DISCONNECTED);
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (gatt.getServices() != null) {
                for (BluetoothGattService service : gatt.getServices()) {
                    if (service.getCharacteristics() != null && service.getUuid().equals(KestrelUuids.KESTREL_MEASUREMENT_SERVICE)) {
                        for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                            if (characteristic != null && characteristic.getUuid().equals(KestrelUuids.DERIVED_MEASUREMENTS_1)) {
                                setCharacteristicNotifications(characteristic, true);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            if (DEBUG) Log.i(TAG, "onCharacteristicChanged()");

            if (characteristic.getUuid().equals(KestrelUuids.DERIVED_MEASUREMENTS_1)) {

                final int ALTITUDE_POSITION = 4;
                final int ALTITUDE_LENGTH = 3;
                final int ALTITUDE_BAD_VALUE = 0x800001;
                final double ALTITUDE_EXPONENT = -1;
                Double altitude = BluetoothUtils.extractInt24Value(characteristic.getValue(),
                        ALTITUDE_POSITION,
                        ALTITUDE_LENGTH,
                        ALTITUDE_BAD_VALUE,
                        ALTITUDE_EXPONENT);
                if (altitude != null && onReceivedValuesListener != null) {
                    onReceivedValuesListener.onReceivedAltitude(altitude.toString());
                }
            }
        }
    };

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            if (bluetoothDevice.getName() != null) {
                if (BluetoothUtils.checkIfDeviceIsKestrelWeatherStation(bluetoothDevice)) {
                    if (!isConnecting || !isConnected) {
                        scanForDevices(false);
                        connectToDevice(bluetoothDevice);
                    }
                }
            }
        }
    };

    private void setConnectingTimeout(long delay) {
        timer = new Timer();
        TimerTask timeoutTask = new TimerTask() {
            @Override
            public void run() {
                isConnecting = false;
            }
        };
        timer.schedule(timeoutTask, delay);
    }

    private void cancelTimeout() {
        timer.cancel();
        timer = null;
    }

    private boolean setCharacteristicNotifications(BluetoothGattCharacteristic characteristic, boolean enable) {
        bluetoothGatt.setCharacteristicNotification(characteristic, enable);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
                .fromString("00002902-0000-1000-8000-00805f9b34fb"));
        descriptor.setValue(enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : new byte[]{0x00, 0x00});
        return bluetoothGatt.writeDescriptor(descriptor);
    }
}
