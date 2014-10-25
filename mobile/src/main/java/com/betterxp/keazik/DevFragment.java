package com.betterxp.keazik;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/*
Ne devrait comporter de que MAJ UI
 */
public class DevFragment extends Fragment {
	public static final int MESSAGE_READ = 200;
	protected static final String TAG = "BLUETOOTH";
	private final Handler messageHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_READ:
					byte[] readBuf = (byte[]) msg.obj;
					// construct a string from the valid bytes in the buffer
					String readMessage = new String(readBuf, 0, msg.arg1);
					Log.d(TAG, "WTF " + readMessage);
			}
		}
	};
	private BluetoothZikService bluetoothZikService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bluetoothZikService = new BluetoothZikService(getActivity(), messageHandler);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_dev, container, false);
		initViewsAndListeners(rootView);
		return rootView;
	}

	private void initViewsAndListeners(View rootView) {
		View activateNoiseCancel = rootView.findViewById(R.id.activateNoiseCancel);
		activateNoiseCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] request = ParrotCommands.setNoiseCancellationEnabled("true");
				//TODO : devrait faire appel à une methode du BT Service quand elle sera implémentée
				bluetoothZikService.write(request);
				Log.d(TAG, new String(request));
			}
		});

		View deactivateNoisecancel = rootView.findViewById(R.id.deactivateNoiseCancel);
		deactivateNoisecancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] request = ParrotCommands.setNoiseCancellationEnabled("false");
				//TODO : devrait faire appel à une methode du BT Service quand elle sera implémentée
				bluetoothZikService.write(request);
			}
		});

		View activateConcertHall = rootView.findViewById(R.id.activateConcert);
		activateConcertHall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] request = ParrotCommands.setSoundEffectEnabled("true");
				//TODO : devrait faire appel à une methode du BT Service quand elle sera implémentée
				bluetoothZikService.write(request);
				Log.d(TAG, new String(request));
			}
		});

		View deactivateConcertHall = rootView.findViewById(R.id.deactivateConcert);
		deactivateConcertHall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] request = ParrotCommands.setSoundEffectEnabled("false");
				//TODO : devrait faire appel à une methode du BT Service quand elle sera implémentée
				bluetoothZikService.write(request);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		bluetoothZikService.initConnection();
		Log.d(TAG, "DevFragment : onResume");
	}

	@Override
	public void onPause() {
		bluetoothZikService.stopConnection();
		super.onPause();
	}

}
