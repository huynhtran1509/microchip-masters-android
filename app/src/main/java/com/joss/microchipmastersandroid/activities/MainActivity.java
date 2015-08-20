package com.joss.microchipmastersandroid.activities;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.joss.microchipmastersandroid.R;
import com.joss.microchipmastersandroid.services.BleInterface;
import com.joss.microchipmastersandroid.services.BleService;
import com.joss.microchipmastersandroid.views.BluetoothDeviceView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements BleInterface{

    public static final String TAG = MainActivity.class.getSimpleName();

    ProgressBar progressBar;
    Button startScanningButton;
    ListView listView;
    BleAdapter adapter;

    BleService bleService;
    MicrochipBleConnection bleConnection;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        startScanningButton = (Button) findViewById(R.id.start_scanning_button);
        adapter = new BleAdapter(this);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        // Code Goes Here
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Code Goes Here
    }

    @Override
    protected void onPause(){
        super.onPause();

        // Code Goes Here
    }

    public void onStartScanningButtonClicked(View view){
        // Code Goes Here
    }

    private class MicrochipBleConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            // Code Goes Here
        }

        public void onServiceDisconnected(ComponentName className) {
            // Code Goes Here
        }
    }

    @Override
    public void onBleDisabled() {

    }

    @Override
    public void onBleScan(BluetoothDevice device) {
        // Code Goes Here
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

    }

    @Override
    public void onConnectingToDevice() {

    }

    @Override
    public void onConnected() {

    }

    class BleAdapter extends BaseAdapter{

        Context context;

        List<BluetoothDevice> devices;
        public BleAdapter(Context context){
            this.context = context;
            this.devices = new ArrayList<>();
        }

        public void addDevice(BluetoothDevice d){
            if(!devices.contains(d)){
                devices.add(d);
                this.notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public Object getItem(int i) {
            return devices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            BluetoothDeviceView bluetoothDeviceView = view == null
                    ? new BluetoothDeviceView(context)
                    : (BluetoothDeviceView) view;
            bluetoothDeviceView.setContent(devices.get(i));
            return bluetoothDeviceView;
        }
    }

    private void showButton(){
        startScanningButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.INVISIBLE);
    }

    private void showLoading(){
        startScanningButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
    }

    private void showContent(){
        startScanningButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }
}
