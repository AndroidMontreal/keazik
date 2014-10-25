package com.betterxp.keazik;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothBroadcastReceiver extends BroadcastReceiver{

	public BluetoothBroadcastReceiver() {
		Log.d("BT_BC_RECIEVER","INIT");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(intent.getAction())){
			Log.d("BT_BC_RECIEVER","ACTION_ACL_CONNECTED");
			//Do something with bluetooth device connection
		} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(intent.getAction())){
			//Do something with bluetooth device disconnection
			Log.d("BT_BC_RECIEVER","ACTION_ACL_DISCONNECTED");
		} else {
			Log.d("BT_BC_RECIEVER","OTHER_ACTIONS");
		}
	}
}
