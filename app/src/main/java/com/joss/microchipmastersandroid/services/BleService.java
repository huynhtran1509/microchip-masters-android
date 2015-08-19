package com.joss.microchipmastersandroid.services;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.joss.microchipmastersandroid.persistance.DataStore;
import com.joss.microchipmastersandroid.MainApp;

import java.util.List;

/**
 * Created by: WillowTree
 * Date: 1/12/15
 * Time: 11:32 AM
 */
public class BleService extends Service {
    private final static String TAG = BleService.class.getSimpleName();

    private static final int SERVICE_DISCOVERY_DELAY = 1000;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothScanner;
    private ScanCallback scanCallback;
    private AppsLeScanCallback leScanCallback;

    private String bluetoothDeviceAddress;
    private BluetoothGatt bluetoothGatt;

    private boolean isScanning = false;
    private boolean isInitialized = false;
    private boolean isConnected = false;

    private final IBinder iBinder = new MicrochipBinder();
    private BleInterface bleInterface;

    private final BluetoothGattCallback bleCallback = new AppsBluetoothGattCallback();

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopBluetoothCrashResolver();
    }

    public class MicrochipBinder extends Binder {
        public BleService getService(){
            return BleService.this;
        }
    }

    public void initialize(BleInterface bleInterface){
        this.isInitialized = true;
        this.bleInterface = bleInterface;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.scanCallback = new AppsScanCallback();

            if (isBluetoothEnabled()) {
                bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();
            } else {
                bleInterface.onBleDisabled();
                this.isInitialized = false;
            }
        } else {
            this.leScanCallback = new AppsLeScanCallback();
        }
    }

    /**
     * Start Scanning
     */
    public void startScanning(){
        startBluetoothCrashResolver();
        if (!isScanning) {
            if (isBluetoothEnabled()) {
                isScanning = true;
                bleInterface.onScanningStarted();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ScanSettings scanSettings = new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                            .setScanMode(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                            .build();
                    bluetoothScanner.startScan(null, scanSettings, scanCallback);
                } else {
                    bluetoothAdapter.startLeScan(leScanCallback);
                }
            } else {
                bleInterface.onBleDisabled();
            }
        }
    }

    /**
     * Stop Scanning
     */
    public void stopScanning(){
        if (isScanning) {
            if (isBluetoothEnabled()) {
                isScanning = false;
                bleInterface.onScanningStopped();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    bluetoothScanner.stopScan(scanCallback);
                } else {
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            } else {
                bleInterface.onBleDisabled();
                isInitialized = false;
            }
        }
    }

    /**
     * BLE Scan Callback for Jelly Bean and KitKat Devices
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private class AppsLeScanCallback implements BluetoothAdapter.LeScanCallback {

        /**
         * Callback interface used to deliver LE scan results.
         */
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            bleInterface.onBleScan(device);
        }
    }

    /**
     * BLE Scan Callback for Lollipop devices and higher.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class AppsScanCallback extends ScanCallback {

        /**
         * Callback when a BLE advertisement has been found.
         *
         * @param callbackType Determines how this callback was triggered. Currently could only be
         *            {@link ScanSettings#CALLBACK_TYPE_ALL_MATCHES}.
         * @param result A Bluetooth LE scan result.
         */
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            bleInterface.onBleScan(result.getDevice());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        /**
         * Callback when scan could not be started.
         *
         * @param errorCode Error code (one of SCAN_FAILED_*) for scan failure.
         */
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);

        }
    }

    /**
     * Disconnect
     *
     * Attempt to disconnect to connected device
     */
    public void disconnect() {
        if (bluetoothAdapter == null || bluetoothGatt == null) {
            return;
        }

        if(isConnected)
            bluetoothGatt.disconnect();

        isConnected = false;
        bleInterface.onBleDisabled();
    }

    /**
     * BLE Gatt Server Callback
     */
    private class AppsBluetoothGattCallback extends BluetoothGattCallback {

        /**
         * Callback indicating when Gatt client has connected/disconnected to/from a remote
         * Gatt server.
         *
         * @param gatt Gatt client
         * @param status Status of the connect or disconnect operation.
         *               {@link BluetoothGatt#GATT_SUCCESS} if the operation succeeds.
         * @param newState Returns the new connection state. Can be one of
         *                  {@link BluetoothProfile#STATE_DISCONNECTED} or
         *                  {@link BluetoothProfile#STATE_CONNECTED}
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState){
            switch (newState){
                case BluetoothProfile.STATE_CONNECTED:

                    break;

                case BluetoothProfile.STATE_DISCONNECTED:

                    break;
            }
        }
    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    public void close() {
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.close();
        bluetoothGatt = null;
    }

    /**
     * Util Helper functions
     */
    public boolean isDeviceBleCompatible(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public boolean isConnected(){
        return isConnected;
    }

    public boolean isScanning(){ return isScanning; }

    /**
     * Start BLE Crash Resolver
     */
    private void startBluetoothCrashResolver(){
        try{
            ((MainApp) getApplication()).getBluetoothCrashResolver().start();
        }catch (IllegalArgumentException e){
            Log.e(TAG, e.toString());
        }
    }

    /**
     * Stop BLE Crash Resolver
     */
    private void stopBluetoothCrashResolver() {
        try{
            ((MainApp) getApplication()).getBluetoothCrashResolver().stop();
        }catch (IllegalArgumentException e){
            Log.e(TAG, e.toString());
        }
    }
}
