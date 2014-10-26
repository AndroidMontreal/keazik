package com.betterxp.keazik;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

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
		Synchronise(rootView);
		return rootView;
	}

	public void Synchronise(View rootView) {
		Log.d(TAG, "Synchronise etat toggle button");
		getHeadsetNoiseCancelState();
		ToggleButton noiseCancelToggleButton = (ToggleButton) rootView.findViewById(R.id.noiseCancellationToggleButton);
		noiseCancelToggleButton.setChecked(false);

	}

	private void getHeadsetNoiseCancelState() {
		byte[] noiseCancelStateCommand = ParrotCommands.getNoiseCancelStateCommand();
		bluetoothZikService.write(noiseCancelStateCommand);
	}

	private void initViewsAndListeners(View rootView) {

		ToggleButton noiseCancelToggleButton = (ToggleButton) rootView.findViewById(R.id.noiseCancellationToggleButton);
		noiseCancelToggleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean on = ((ToggleButton) v).isChecked();
				byte[] request;

				if (on) {
					request = ParrotCommands.setNoiseCancellationEnabled("true");
				} else {
					request = ParrotCommands.setNoiseCancellationEnabled("false");
				}
				bluetoothZikService.write(request);
				Log.d(TAG, new String(request));
			}
		});

		ToggleButton concertHallToggleButton = (ToggleButton) rootView.findViewById(R.id.concertHallToggleButton);
		concertHallToggleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean on = ((ToggleButton) v).isChecked();
				byte[] request;

				if (on) {
					request = ParrotCommands.setSoundEffectEnabled("true");
				} else {
					request = ParrotCommands.setSoundEffectEnabled("false");
				}
				bluetoothZikService.write(request);
				Log.d(TAG, new String(request));
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
