package com.joss.microchipmastersandroid.persistance;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.joss.microchipmastersandroid.models.Status;
import com.joss.microchipmastersandroid.services.BleService;
import com.joss.microchipmastersandroid.utils.GGson;

/**
 * Created by: jossayjacobo
 * Date: 5/20/15
 * Time: 1:37 PM
 */
public class DataStore {

    private static final String BLE_DEVICE_NAME = "ble_device_name";
    private static final String BLE_DEVICE_MAC_ADDRESS = "ble_devie_mac_address";
    private static final String BLE_DEVICE_UUID = "ble_device_uuid";
    private static final String BLE_DEVICE_BUTTONS = "ble_device_buttons";

    private static final String NOT_CONFIGURED = "NOT_CONFIGURED";

    private static SharedPreferences getDataStore(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getDataStore(context).edit();
    }

    private static SharedPreferences getPrefs(Context context) {
        return getDataStore(context);
    }

    public static String getBleDeviceName(Context context) {
        return getPrefs(context).getString(BLE_DEVICE_NAME, "");
    }

    public static void persistBleDeviceName(Context context, String name) {
        getEditor(context).putString(BLE_DEVICE_NAME, name).commit();
    }

    public static String getBleDeviceMacAddress(Context context){
        return getPrefs(context).getString(BLE_DEVICE_MAC_ADDRESS, "");
    }

    public static void persistBleDeviceMacAddress(Context context, String mac){
        getEditor(context).putString(BLE_DEVICE_MAC_ADDRESS, mac).commit();
    }

    public static String getBleDeviceUuid(Context context){
        return getPrefs(context).getString(BLE_DEVICE_UUID, NOT_CONFIGURED);
    }

    public static void persistBleDeviceUuid(Context context, String uuid){
        getEditor(context).putString(BLE_DEVICE_UUID, uuid).commit();
    }

    public static void persistBleStatus(Context context, Status status) {
        getEditor(context).putString(BLE_DEVICE_BUTTONS, GGson.toJson(status)).commit();
    }

    public static Status getBleStatus(Context context){
        return GGson.fromJson(getPrefs(context).getString(BLE_DEVICE_BUTTONS, ""), Status.class);
    }

    public static void clearBleDevice(Context context){
        persistBleDeviceMacAddress(context, "");
        persistBleDeviceName(context, "");
        persistBleDeviceUuid(context, NOT_CONFIGURED);
    }

}