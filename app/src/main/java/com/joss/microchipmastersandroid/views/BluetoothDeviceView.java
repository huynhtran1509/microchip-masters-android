package com.joss.microchipmastersandroid.views;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joss.microchipmastersandroid.R;

/**
 * Created by: jossayjacobo
 * Date: 5/20/15
 * Time: 4:15 PM
 */
public class BluetoothDeviceView extends LinearLayout{

    TextView title;
    TextView subtitle;

    public BluetoothDeviceView(Context context) {
        this(context, null);
    }

    public BluetoothDeviceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BluetoothDeviceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_bluetooth_device, this, true);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
    }

    public void setContent(BluetoothDevice device){
        title.setText(device.getName());
        subtitle.setText(device.getAddress());
    }
}
