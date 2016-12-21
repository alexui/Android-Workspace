package com.example.battleship;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

//trebuie creata o clasa noua cu un constructor care sa obtina contextul unei activitati
class MyThread extends Thread{
	
	MyFieldFight act;
	
	public MyThread(MyFieldFight act){
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
			        act.closeDialog();
			    }
			});
     }
}

class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void onSwipeLeft() {
    }

    public void onSwipeRight() {
    }

    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 1;
        private static final int SWIPE_VELOCITY_THRESHOLD = 1;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight();
                else
                    onSwipeLeft();
                return true;
            }
            return false;
        }
    }
}

public class MyFieldFight extends Activity {

	Animation slide_in_left, slide_out_right;
	ViewSwitcher vs;
	TableLayout tl1,tl2;
	String player1,player2;
	TextView tv,tvp;
	Boolean CONNECTION_ESTABLISHED;
	BluetoothServerSocket mmServerSocket;
	BluetoothSocket socket;
	BluetoothAdapter mBluetoothAdapter;
	Integer TIMEOUT=10000;
	ArrayList<Device> mArrayAdapter;
	ArrayList<BluetoothDevice> mDevices;
	HashMap<Button,Integer> myField,opField;
	final int REQUEST_ENABLE_BT=1;
	Connecting pop;
	final int REQ=2;
	final int MAXLEN=1024;
	
	int Lose=16;
	
	final String NAME_PREFIX="NAME ";
	final String TARGET_PREFIX="POS ";
	final String RESULT_PREFIX="RES ";
	final String LOSER_PREFIX="LOS ";
	
	final int NAME =1,TARGET=2,RESULT=3,LOSER=4;
	
	Integer field[][];
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_field_activity);
		
		vs = (ViewSwitcher) findViewById(R.id.viewSwitcher);
		tl1 = (TableLayout) findViewById(R.id.TABLE1);
		tl2 = (TableLayout) findViewById(R.id.TABLE2);
		tv = (TextView) findViewById(R.id.PLAYER1);
		tvp = (TextView) findViewById(R.id.PLAYER2);
		field = new Integer[BattleFieldDrawActivity.SQUARE_NO+1][BattleFieldDrawActivity.SQUARE_NO+1];
		initializeField(field, BattleFieldDrawActivity.SQUARE_NO_VERTICAL, BattleFieldDrawActivity.SQUARE_NO_HORIZONTAL);
		myField = new HashMap<Button, Integer>();
		opField = new HashMap<Button, Integer>();
		
		for(int j=1;j<=BattleFieldDrawActivity.SQUARE_NO_VERTICAL;j++){
			for(int k=1;k<=BattleFieldDrawActivity.SQUARE_NO_HORIZONTAL;k++){
				int s; 
				s= getIntent().getExtras().getInt("cheie"+j+k);
				int nr;
				if (j==1)
					nr=k;
				else nr=8*(j-1)+k;
				int id = getResources().getIdentifier("button"+nr, "id", getPackageName());
				int idp = getResources().getIdentifier("button"+nr+"p", "id", getPackageName());
				Button b = (Button)findViewById(id);
				Button bp = (Button)findViewById(idp);
				if(s==1)
					b.setBackgroundColor(Color.parseColor("#AC8BF4"));
				if(s==2)
					b.setBackgroundColor(Color.parseColor("#67ADF0"));
				field[j][k]=s;
								
				myField.put(b, nr);
				opField.put(bp,nr);
				
				bp.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Button b = (Button)v;
						
						int i = opField.get(b);
												
						String text = TARGET_PREFIX+i;
						byte[] bytes = text.getBytes();
						ConnectedThread ct = new ConnectedThread(socket);
						ct.write(bytes);
					}
				});
			}
		}
		
		player1 = getIntent().getStringExtra("name");
		tv.setText(player1);

		  slide_in_left = AnimationUtils.loadAnimation(this,
		    R.anim.slide_to_left);
		  slide_out_right = AnimationUtils.loadAnimation(this,
		    R.anim.slide_to_right);
		  
		  
		  View v = findViewById(R.id.main);
		  
		  v.setOnTouchListener(new OnSwipeTouchListener(this.getApplicationContext()){
			  @Override
			    public void onSwipeLeft() {
			        vs.setOutAnimation(outToLeftAnimation());
			        vs.setInAnimation(inFromRightAnimation());
			        vs.showPrevious();
			    }
			  
			  @Override
			    public void onSwipeRight() {
				  	vs.setOutAnimation(outToRightAnimation());
			        vs.setInAnimation(inFromLeftAnimation());
			        vs.showNext();
			    }
		  });
		  	
		  pop = new Connecting(MyFieldFight.this);
		  pop.setCanceledOnTouchOutside(false);
          pop.show();
		  
		  Thread t = new MyThread(this);
		  t.start();	  
	}
	
	public void initializeField(Integer matrix[][],int rows,int cols){
		for (int i=0;i<=rows;i++)
			for(int j=0;j<=cols;j++){
				matrix[i][j]=0;
			}
	}
	
	public void closeDialog(){
		pop.dismiss();
		Intent i = new Intent(MyFieldFight.this,Devices.class);
		mArrayAdapter.add(new Device("alex", "1234"));
		mArrayAdapter.add(new Device("iulia", "1234:5678"));
		
		i.putExtra("nr", mArrayAdapter.size());
		for(int k=0;k<mArrayAdapter.size();k++){
			i.putExtra("name"+k, mArrayAdapter.get(k).name);
			i.putExtra("address"+k, mArrayAdapter.get(k).address);
		}
		
		startActivityForResult(i, REQ);
	}
	
	public void onActivityResult(int requestCode,int resultCode,Intent data){
		if(requestCode==REQ){
			if(resultCode==Activity.RESULT_OK){
				int pos = data.getIntExtra("pos", 0);
				String addr = mArrayAdapter.get(pos).address;
				System.out.println("address:"+addr);
				Thread server = new AcceptThread();
				server.start();
			}
		}
	}
		
	static public byte[] object2Bytes( Object o ) throws IOException {
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      ObjectOutputStream oos = new ObjectOutputStream( baos );
	      oos.writeObject( o );
	      return baos.toByteArray();
	    }
		
	public void makeConnection(){
				
		Thread thread = new Thread()
		  {
		      @Override
		      public void run() {
			    	HttpClient httpclient = new DefaultHttpClient();
			        HttpResponse response;
			        String responseString=null;
					try {
						response = httpclient.execute(new HttpGet("http://www.google.com"));
						StatusLine statusLine = response.getStatusLine();
					        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					            ByteArrayOutputStream out = new ByteArrayOutputStream();
					            response.getEntity().writeTo(out);
					            out.close();
					             responseString= out.toString();
					            //..more logic
					        } else{
					            //Closes the connection.
					            response.getEntity().getContent().close();
					            throw new IOException(statusLine.getReasonPhrase());
					        }
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					System.out.println("string: "+responseString);
		      }
		  };

		 thread.start();
	}

	@SuppressLint("ShowToast")
	public void makeBlueToothConnection(){
		
		mArrayAdapter= new ArrayList<Device>();
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
		    	mDevices.add(device);
		        mArrayAdapter.add(new Device(device.getName(),device.getAddress()));
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
		            mArrayAdapter.add(new Device(device.getName(),device.getAddress()));
		            mDevices.add(device);
			        System.out.println(""+device.getName()+"  "+device.getAddress());

		        }
		    }
		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		
		
	}
	
	@SuppressLint("NewApi")
	private class AcceptThread extends Thread {
	    	 
	    public AcceptThread() {
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(this.getName(), new UUID(NORM_PRIORITY, NORM_PRIORITY));
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
	                break;
	            }
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
	                System.out.println("received");
	                
	                manageConnectedSocket(socket);
	                try {
						mmServerSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	                break;
	            }
	        }
	    }
	 
	    //waiting for data
	    public void manageConnectedSocket(BluetoothSocket socket) {
	    	
	    	ConnectedThread ct = new ConnectedThread(socket);
	    	ct.start();
	    	
	    	String start = NAME_PREFIX+player1;
	    	byte[] bytes = start.getBytes();
	    	System.out.println("writing:"+start);
	    	//ct.write(bytes);
		}

		/** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	private class ConnectedThread extends Thread {
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
	 
	    public void run() {
	        byte[] buffer = new byte[1024];  // buffer store for the stream
	        int bytes=0; // bytes returned from read()
	 
	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	                // Read from the InputStream
	            	
	            	mmInStream = socket.getInputStream();
	                bytes = mmInStream.read(buffer);
	                System.out.println(""+bytes+"received");
	                System.out.println("reading");
	                
	                String data = new String(buffer,0,bytes);
	                System.out.println(data);
	                
	                String message;
	                message = getMessageType(data);
	                
	                switch(msg_type){
	                case NAME: 
	                	System.out.println("message:"+message);
	                	tvp.setText(message);
	                	String start = NAME_PREFIX+player1;
		                byte[] bts = start.getBytes();
		    	    	System.out.println("writing:"+start);
		                this.write(bts);
	                	break;
	                	
	                case TARGET:
	                	int i = Integer.parseInt(message); 
	                	int x,y;
	                	x =(i-1)/BattleFieldDrawActivity.SQUARE_NO_HORIZONTAL+1;
						y=(i-1)%BattleFieldDrawActivity.SQUARE_NO_VERTICAL+1;
						
						if(field[x][y]!=0){
							field[x][y]=0;
							int id = getResources().getIdentifier("button"+i, "id", getPackageName());
							Button b = (Button)findViewById(id);
							b.setBackgroundColor(Color.RED);	
							
							//send result
							String text;
							text=RESULT_PREFIX+i;
							bts=text.getBytes();
							ConnectedThread ct = new ConnectedThread(socket);
							ct.write(bts);
						}
						else{
							int id = getResources().getIdentifier("button"+i, "id", getPackageName());
							Button b = (Button)findViewById(id);
							b.setBackgroundColor(Color.GREEN);	
							//send result
							String text;
							text=RESULT_PREFIX+0;
							bts=text.getBytes();
							write(bts);
						}
							
	                	System.out.println(i);
	                	break;
	                	
	                case RESULT:
	                	int j = Integer.parseInt(message); 
	                							
						if(j!=0){
							int id = getResources().getIdentifier("button"+j+"p", "id", getPackageName());
							Button b = (Button)findViewById(id);
							b.setBackgroundColor(Color.GREEN);					
						}
						else{
							int id = getResources().getIdentifier("button"+j+"p", "id", getPackageName());
							Button b = (Button)findViewById(id);
							b.setBackgroundColor(Color.RED);
						}
	                	
	                case LOSER:
	                default:
	                }
	                
	                
	                	                
	            } catch (IOException e) {
	                return;
	            }
	        }
	    }
	 
	    public String getMessageType(String data){
	    	
	    	StringTokenizer st = new StringTokenizer(data, " ");
	    	String type = st.nextToken();
	    	type.trim();
	    	System.out.println("type: "+type);
	    	String message=(st.nextToken());
	    	
	    	if(type.compareTo(NAME_PREFIX.trim())==0)
	    		msg_type=NAME;
	    	else if(type.compareTo(TARGET_PREFIX.trim())==0)
	    		msg_type=TARGET;
	    	if(type.compareTo(RESULT_PREFIX.trim())==0)
	    		msg_type= RESULT;
	    	if(type.compareTo(LOSER_PREFIX.trim())==0)
	    		msg_type=LOSER;
	    	
	    	return message;
	    }
	    
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	        	System.out.println("writing");
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
	
	@Override
	public void onBackPressed() {
		return;
	}
	
	private Animation outToLeftAnimation() {
	    Animation outtoLeft = new TranslateAnimation(
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, -1.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f);
	    outtoLeft.setDuration(200);
	    outtoLeft.setInterpolator(new DecelerateInterpolator());
	    return outtoLeft;
	    }

	private Animation outToRightAnimation() {
	    Animation outtoRight = new TranslateAnimation(
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, +1.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f);
	    outtoRight.setDuration(200);
	    outtoRight.setInterpolator(new DecelerateInterpolator());
	    return outtoRight;
	    }
	
	private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(200);
        inFromRight.setInterpolator(new DecelerateInterpolator());
        return inFromRight;
        }
	
	private Animation inFromLeftAnimation() {
	    Animation inFromLeft = new TranslateAnimation(
	        Animation.RELATIVE_TO_PARENT, -1.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f);
	    inFromLeft.setDuration(200);
	    inFromLeft.setInterpolator(new DecelerateInterpolator());
	    return inFromLeft;
	    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.battle_field_draw, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch(id){
		case R.id.EXIT: finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
