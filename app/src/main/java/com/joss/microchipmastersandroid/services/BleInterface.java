package com.joss.microchipmastersandroid.services;

import android.bluetooth.BluetoothDevice;

import com.joss.microchipmastersandroid.models.Status;

/**
 * Created by: WillowTree
 * Date: 1/12/15
 * Time: 1:55 PM
 */
public interface BleInterface {

    void onBleDisabled();

    void onBleScan(BluetoothDevice device);

    void onBleScanFailed(String message);

    void onScanningStarted();

    void onScanningStopped();

    void onDisconnected();

    void onConnectingToDevice();

    void onConnected();

    void onButtonStateChanged(Status status);
}
