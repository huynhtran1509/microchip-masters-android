package com.joss.microchipmastersandroid.activities;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.joss.microchipmastersandroid.R;
import com.joss.microchipmastersandroid.models.Status;
import com.joss.microchipmastersandroid.services.BleInterface;
import com.joss.microchipmastersandroid.services.BleService;
import com.joss.microchipmastersandroid.utils.GGson;

/**
 * Created by: jossayjacobo
 * Date: 6/4/15
 * Time: 9:04 PM
 */
public class BleConnectActivity extends Activity implements BleInterface {

    public static final String BLE_DEVICE = "ble_device";

    View loadingContainer;
    TextView status;
    TextView button1;
    TextView button2;
    TextView button3;
    TextView button4;

    BleService bleService;
    MicrochipBleConnection bleConnection;

    BluetoothDevice bluetoothDevice;
    Handler handler;

    public static void startBleConnectActivity(Context context, BluetoothDevice device){
        Intent i = new Intent(context, BleConnectActivity.class);
        i.putExtra(BLE_DEVICE, GGson.toJson(device));
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_connect);

        loadingContainer = findViewById(R.id.ble_connect_loading_container);
        status = (TextView) findViewById(R.id.ble_connect_status);
        button1 = (TextView) findViewById(R.id.ble_connect_button_1);
        button2 = (TextView) findViewById(R.id.ble_connect_button_2);
        button3 = (TextView) findViewById(R.id.ble_connect_button_3);
        button4 = (TextView) findViewById(R.id.ble_connect_button_4);

        // Get the serialized ble device object
        bluetoothDevice = GGson.fromJson(getIntent().getStringExtra(BLE_DEVICE), BluetoothDevice.class);

        bleConnection = new MicrochipBleConnection();
        handler = new Handler();
    }

    @Override
    public void onResume(){
        super.onResume();

        // Bind to ble service
        Intent intent = new Intent(this, BleService.class);
        bindService(intent, bleConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause(){
        super.onPause();

        if (bleService != null) {
            bleService.disconnect();
            unbindService(bleConnection);
        }
    }

    private class MicrochipBleConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            BleService.MicrochipBinder b = (BleService.MicrochipBinder) binder;
            bleService = b.getService();
            bleService.initialize(BleConnectActivity.this);
            connect(bluetoothDevice);
        }

        public void onServiceDisconnected(ComponentName className) {
            bleService = null;
        }
    }

    private void connect(BluetoothDevice bluetoothDevice) {
        if (bleService != null && bleService.isInitialized() && bluetoothDevice != null) {
            bleService.connect(bluetoothDevice);
        }
    }

    @Override
    public void onButtonStateChanged(final Status status) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                button1.setText("Button 1: " + (status.button1 ? "ON" : "OFF"));
                button2.setText("Button 2: " + (status.button2 ? "ON" : "OFF"));
                button3.setText("Button 3: " + (status.button3 ? "ON" : "OFF"));
                button4.setText("Button 4: " + (status.button4 ? "ON" : "OFF"));
            }
        });
    }

    @Override
    public void onBleDisabled() {

    }

    @Override
    public void onBleScan(BluetoothDevice device) {

    }

    @Override
    public void onBleScanFailed(String message) {

    }

    @Override
    public void onScanningStarted() {

    }

    @Override
    public void onScanningStopped() {

    }

    @Override
    public void onDisconnected() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                status.setText("Not Connected");
            }
        });
    }

    @Override
    public void onConnectingToDevice() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                loadingContainer.setVisibility(View.VISIBLE);
                status.setText("");
            }
        });
    }

    @Override
    public void onConnected() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                loadingContainer.setVisibility(View.GONE);
                status.setText("Connected");
            }
        });
    }

}
