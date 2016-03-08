package com.boyukliev.kestrel_app.ui.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Admin on 3/8/2016.
 */
public class BluetoothUtils {
    private final static String[] KESTREL_DEVICE_NAMES = new String[] {
            "enviro", "racing", "pro", "heat", "weather",
            "sport", "ab", "horus"
    };

    public static boolean checkIfDeviceIsKestrelWeatherStation(BluetoothDevice device) {
        String deviceName = device.getName().toLowerCase();

        for (int i = 0; i < KESTREL_DEVICE_NAMES.length; i++) {
            if (deviceName.contains(KESTREL_DEVICE_NAMES[i])) return true;
        }

        return false;
    }

    public static Double extractInt24Value(byte[] bytes,
                                     int position,
                                     int length,
                                     int badValue,
                                     double exponent) {
        byte[] newBytes = new byte[length];

        for (int i = 0; i < length; i++) {
            newBytes[i] = bytes[position + i];
        }
        int raw = bytesToSInt(newBytes);
        Double value = raw * Math.pow(10, exponent);
        if (value == badValue) {
            return null;
        } else {
            return value;
        }
    }

    private static int bytesToSInt(byte[] input) {
        return (input[2]) << 16 | (input[1] & 0xFF) << 8 | (input[0] & 0xFF);
    }
}
