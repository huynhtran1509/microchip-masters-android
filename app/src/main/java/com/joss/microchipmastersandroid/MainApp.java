package com.joss.microchipmastersandroid;

import android.app.Application;

import com.joss.microchipmastersandroid.services.BluetoothCrashResolver;

/**
 * Created by: jossayjacobo
 * Date: 5/20/15
 * Time: 1:29 PM
 */
public class MainApp extends Application{

    private BluetoothCrashResolver bluetoothCrashResolver = null;

    @Override
    public void onCreate(){
        super.onCreate();

        bluetoothCrashResolver = new BluetoothCrashResolver(this);
        bluetoothCrashResolver.start();
    }

    public BluetoothCrashResolver getBluetoothCrashResolver(){
        return bluetoothCrashResolver;
    }

}
