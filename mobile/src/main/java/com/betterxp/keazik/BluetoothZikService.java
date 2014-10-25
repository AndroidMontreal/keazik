package com.betterxp.keazik;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Un classe qui à pour but de d'initier les connexion Bluetooth et d'en réaliser les transactions
 */
public class BluetoothZikService {

	public static final int MESSAGE_READ = 200;
	private final BluetoothAdapter bluetoothAdapter;
	private final Handler messageHandler;
	private String uuidString = "0ef0f502-f0ee-46c9-986c-54ed027807fb";
	protected static final String TAG = "BLUETOOTH";
	private BluetoothSocket transferSocket;
	private ConnectedThread connectedThread;

	public BluetoothZikService(Context context, Handler handler) {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		this.messageHandler = handler;
	}

	public void initConnection(){
		Log.d(TAG, " initConnection()");
		new InitBtAsyncTask().execute();
	}

	public void stopConnection(){
		stopConnectedThread();
	}

	private void startConnectedThread() {
		Log.d(TAG, "startConnectedThread()");
		if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
			BluetoothDevice device = getDevice();
			if(device != null) {

				startClientSocket(device);
				if(transferSocket != null && messageHandler != null) {

					connectedThread = new ConnectedThread(transferSocket, messageHandler);
					connectedThread.start();
					if(connectedThread.isAlive()) {
						byte[] firstMessage = {0x00, 0x03, 0x00};
						connectedThread.write(firstMessage);
					}
				}
			}

		}
	}

	private void startClientSocket(BluetoothDevice device) {
		Log.d(TAG, "startClientSocket");
		UUID uuid = UUID.fromString(uuidString);
		try {
			BluetoothSocket bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
			bluetoothSocket.connect();
			transferSocket = bluetoothSocket;
		} catch (IOException e) {
			Log.e(TAG, "Vérifier que l'application Parrot Zik ne soit pas lancée", e);
			Log.e(TAG, "Bluetooth Client I/O exception", e);
		}
	}

	private BluetoothDevice getDevice() {
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			BluetoothDevice btDevice = null;
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a ListView
				Log.d(TAG, device.getName() + "\n" + device.getAddress());
				if(device.getName().contains("casque")){
					btDevice = device;
				}
			}
			return btDevice;
		}
		return null;
	}


	public void write(byte[] request) {
		if(connectedThread !=null){
			connectedThread.write(request);
		} else {
			Log.e(TAG,"connectedThread is NULL, cant write request");
		}
	}


	public class ConnectedThread extends Thread {

		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		private final Handler mHandler;

		public ConnectedThread(BluetoothSocket socket, Handler handler) {
			mmSocket = socket;
			mHandler = handler;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				if (socket != null) {
					tmpIn = socket.getInputStream();
					tmpOut = socket.getOutputStream();
				}
			} catch (IOException e) {
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(byte[] bytes) {
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) {
			}
		}


		public void run() {
			byte[] buffer = new byte[1024];  // buffer store for the stream
			int bytes; // bytes returned from read()

			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.read(buffer);
					// Send the obtained bytes to the UI activity
					mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
				} catch (IOException e) {
					Log.e("thread run exception", e.getMessage());
					break;
				}
			}
		}

		/* Call this from the main activity to shutdown the connection */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}

	}

	private void stopConnectedThread() {
		if(connectedThread != null) {
			connectedThread.cancel();
		}
		if(transferSocket != null) {
			try {
				transferSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class InitBtAsyncTask extends AsyncTask<Void,Void,Void> {

		@Override
		protected Void doInBackground(Void... params) {
			startConnectedThread();
			return null;
		}
	}


}

