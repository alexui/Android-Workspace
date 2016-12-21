package com.example.bluetoothconnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

class SearchingThread extends Thread{
	
	MainActivity act;
	
	public SearchingThread(MainActivity act){
		this.act=act;
	}
	
	 @Override
     public void run() {
   	  act.makeBlueToothConnection();
   	  long startTime = System.currentTimeMillis(); //fetch starting time
		  while((System.currentTimeMillis()-startTime)<act.TIMEOUT);
		  act.mBluetoothAdapter.cancelDiscovery();
		  act.runOnUiThread(new Runnable() {
			    public void run() {
			        act.closeSearchingDialog();
			    }
			});
     }
}

class ConnectingThread extends Thread{
	
	MainActivity act;
	
	public ConnectingThread(MainActivity act){
		this.act=act;
	}
	
	 @Override
     public void run() {
   	  act.makeServerClientConnection();
   	  long startTime = System.currentTimeMillis(); //fetch starting time
		  while((System.currentTimeMillis()-startTime)<act.TIMEOUT/2);
		  act.runOnUiThread(new Runnable() {
			    public void run() {
			        act.closeConnectingDialog();
			    }
			});
     }
}

public class MainActivity extends Activity {

	final int REQ=2;
	int position;
	
	Integer TIMEOUT =15000;
	Dialog pop;
	ArrayList<Dispozitiv> mArrayAdapter;
	ArrayList<BluetoothDevice> mDevices;
	BluetoothAdapter mBluetoothAdapter;
	BluetoothServerSocket mmServerSocket;
	BluetoothSocket socket=null;
	BluetoothDevice btd;
	
	ConnectedThread ct=null;
	
	//elements
	Button search,start,send;
	TextView tv1,tv2,tv3,tv4;
	EditText et;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		System.out.println("Hello");
		
		//add TextView
		send = (Button) findViewById(R.id.SEND);
		et = (EditText) findViewById(R.id.ET);
		tv1 = (TextView) findViewById(R.id.TV1);
		tv2 = (TextView) findViewById(R.id.TV2);
		tv3 = (TextView) findViewById(R.id.TV3);
		tv4 = (TextView) findViewById(R.id.TV4);
		//add button
		search = (Button) findViewById(R.id.SEARCH);
		//add listener
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//afiseaza dialog de asteptare
				pop = new Searching(MainActivity.this);
				pop.setCanceledOnTouchOutside(false);
		        pop.show();
				
		        //cauta dispozitive bluetooth
				Thread t = new SearchingThread(MainActivity.this);
				t.start();
			}
		}); 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("ShowToast")
	public void makeBlueToothConnection(){
		
		mArrayAdapter= new ArrayList<Dispozitiv>();
		mDevices = new ArrayList<BluetoothDevice>();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		

		//Get the BluetoothAdapter
		if (mBluetoothAdapter == null) {
			return ;
		}
				
		//Enable Bluetooth
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivity(enableBtIntent);
		}
			
		//Discovering devices
		//Querying paired devices
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		    	Dispozitiv new_d = new Dispozitiv(device.getName(),device.getAddress());
		    	if(!mArrayAdapter.contains(new_d)){
		    		mArrayAdapter.add(new_d);
		    		mDevices.add(device);
		    	}
		    }
		}
				
		//Enabling discoverability
		Intent discoverableIntent = new
				Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		startActivity(discoverableIntent);
				
		if(mBluetoothAdapter.isDiscovering()){
			mBluetoothAdapter.cancelDiscovery();
		}
		else{
			Boolean discovery= mBluetoothAdapter.startDiscovery();
			if(!discovery){
				finish();
			}		
		}
			
		
		// Create a BroadcastReceiver for ACTION_FOUND
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        // When discovery finds a device
		        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		            // Get the BluetoothDevice object from the Intent
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		            // Add the name and address to an array adapter to show in a ListView
		            
		            //adaugam nume si adresa dispozitiv daca nu exista deja in lista
		            Dispozitiv new_d = new Dispozitiv(device.getName(),device.getAddress());
			    	if(!mArrayAdapter.contains(new_d)){
			    		mArrayAdapter.add(new_d);
			    		mDevices.add(device);
			    	}
			    		
			    	//adaugam dispozitiv
			        System.out.println(""+device.getName()+"  "+device.getAddress());

		        }
		    }
		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy	
	}

	public void closeSearchingDialog(){
		pop.dismiss();
		Intent i = new Intent(MainActivity.this,Devices.class);
		
		i.putExtra("nr", mArrayAdapter.size());
		for(int k=0;k<mArrayAdapter.size();k++){
			i.putExtra("name"+k, mArrayAdapter.get(k).name);
			i.putExtra("address"+k, mArrayAdapter.get(k).address);
		}
		
		System.out.println("no of devices: "+mDevices.size());
		startActivityForResult(i, REQ);
		et.setVisibility(View.VISIBLE);
		et.setText("DefaultText");
		send.setVisibility(View.VISIBLE);
	}
	
	public void closeConnectingDialog(){
		pop.dismiss();
	}

	public void onActivityResult(int requestCode,int resultCode,Intent data){
		if(requestCode==REQ){
			if(resultCode==Activity.RESULT_OK){
				
				if(data.getBooleanExtra("REDO", false))
				{
					//afiseaza dialog de asteptare
					pop = new Searching(MainActivity.this);
					pop.setCanceledOnTouchOutside(false);
			        pop.show();
					
			        //cauta dispozitive bluetooth
					Thread t = new SearchingThread(MainActivity.this);
					t.start();
				}
				else{
				
					position = data.getIntExtra("pos", 0);
					String addr = mArrayAdapter.get(position).address;
					System.out.println("address:"+addr);
					addr = addr+"\n"+mArrayAdapter.get(position).name;
					tv2.setText(addr);
					
					/*pop= new Connecting(MainActivity.this);
					pop.setCanceledOnTouchOutside(false);
					pop.show();*/
					
					/*Thread t = new ConnectingThread(MainActivity.this);
					t.start();*/
					makeServerClientConnection();
					
				}
			}
		}
	}
	
	public void makeServerClientConnection(){
		AcceptThread server = new AcceptThread(MainActivity.this);
		server.start();
		
		if(socket==null){
			btd = mDevices.get(position);
			System.out.println("Connecting to "+btd.getName());
			ConnectThread client = new ConnectThread(btd,MainActivity.this);
			client.start();
		}
	}
	
	@SuppressLint("NewApi")
	private class AcceptThread extends Thread {
   	 
		private MainActivity act;
		
	    public AcceptThread(MainActivity act) {
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	    	this.act =act;
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(this.getName(), new UUID(NORM_PRIORITY, NORM_PRIORITY));
	        } catch (IOException e) { }
	        mmServerSocket = tmp;
	        System.out.println("accept thread");
	    }
	 
	    public void run() {
	        socket = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {
	            try {
	            	System.out.println("listening");
	                socket = mmServerSocket.accept(TIMEOUT);
	            } catch (IOException e) {
	            	System.out.println("socket down");
	                break;
	            }
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
	                System.out.println("received");
	                	                
	                try {
						mmServerSocket.close();
						System.out.println("Socket closed.");
					} catch (IOException e) {
						e.printStackTrace();
					}
	                break;
	            }
	        }
	        
	        act.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					System.out.println("Managing connection");
					System.out.println("Conectat: "+(socket!=null)+" "+socket.isConnected());
					manageConnectedSocket(socket);
				}
			});
	    }
	 
	    //waiting for data
	    public void manageConnectedSocket(BluetoothSocket socket) {
	  
            ct = new ConnectedThread(socket);
            
            send.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View arg0) {
    				String data = et.getText().toString();
    				ct.write(data.getBytes());
    			}
    		});
            
            System.out.println("Time for reading");
	    	ct.read();	
	    	System.out.println("reading...");
		}

		/** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}

	@SuppressLint("NewApi")
	private class ConnectThread extends Thread {
	    private final BluetoothDevice mmDevice;
	    private MainActivity act;
	    
	    public ConnectThread(BluetoothDevice device,MainActivity act) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	        this.act = act;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = mmDevice.createRfcommSocketToServiceRecord(new UUID(NORM_PRIORITY, NORM_PRIORITY));
	        } catch (IOException e) { }
	        socket = tmp;
	        	    	
	        System.out.println("connect to thread");
	    }
	 
	    public void run() {
	        // Cancel discovery because it will slow down the connection
	        mBluetoothAdapter.cancelDiscovery();
	 
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            socket.connect();
	            System.out.println("socket connected");
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	            try {
	                socket.close();
	            } catch (IOException closeException) {
	            	closeException.printStackTrace();
	            }
	            return;
	        }
	 
            System.out.println("connected");
            act.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					System.out.println("Managing connection");
					System.out.println("Conectat: "+(socket!=null)+" "+socket.isConnected());
					manageConnectedSocket(socket);
				}
			});
	        // Do work to manage the connection (in a separate thread)
	    }
	    	 
	    public void manageConnectedSocket(BluetoothSocket socket) {
	  	  
            ct = new ConnectedThread(socket);
            
            send.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View arg0) {
    				String data = et.getText().toString();
    				ct.write(data.getBytes());
    			}
    		});
            
            System.out.println("Time for reading");
	    	ct.read();	
	    	System.out.println("reading...");
		}
	    
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            socket.close();
	        } catch (IOException e) { }
	    }
	}
	
	private class MessagePoster implements Runnable{

		String string;
		TextView tv;
		
		public MessagePoster(TextView tv,String string){
			this.string=string;
			this.tv=tv;
		}
		
		@Override
		public void run() {
			tv.setText(string);
		}
		
	}
		
	private class ConnectedThread{
	    private final BluetoothSocket mmSocket;
	    private InputStream mmInStream;
	    private OutputStream mmOutStream;
	    int MESSAGE_READ;
	    int msg_type=0;
	 
	    public ConnectedThread(BluetoothSocket socket) {
	        mmSocket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	        
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void read() {

	    	Handler handler=new Handler();
	    	BluetoothSocketListener bsl = new BluetoothSocketListener(socket,handler,tv4);
	    	Thread messageListener = new Thread(bsl);
	    	System.out.println("Create listener thread.");
	    	messageListener.start();
		}

		private class BluetoothSocketListener implements Runnable{
	    	private BluetoothSocket socket;
	    	private TextView textView;
	    	private Handler handler;
	    	public BluetoothSocketListener(BluetoothSocket socket,Handler handler, TextView textView) {
		    	this.socket = socket;
		    	this.textView = textView;
		    	this.handler = handler;
	    	}

	    	@Override
	    	public void run() {
		    	int bufferSize = 1024;
		    	byte[] buffer = new byte[bufferSize];
		    	try {
			    	int bytesRead = -1;
			    	String message = "";
			    	while (true) {
				    	message = "";
				    	bytesRead = mmInStream.read(buffer);
				    	if (bytesRead != -1) {
					    	while ((bytesRead==bufferSize)&&(buffer[bufferSize-1] != 0)) {
						    	message = message + new String(buffer, 0, bytesRead);
						    	bytesRead = mmInStream.read(buffer);
				    		}
					    	message = message + new String(buffer, 0, bytesRead);
					    	handler.post(new MessagePoster(textView, message));
					    	socket.getInputStream();
				    	}
			    	}
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    	}
	    	}//end run
    	}//end class
	        
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	        	System.out.println("writing "+new String(bytes));
	        	mmOutStream = socket.getOutputStream();
	            mmOutStream.write(bytes);
	        } catch (IOException e) { }
	    }
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
}
