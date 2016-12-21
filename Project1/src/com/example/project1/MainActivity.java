package com.example.project1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MainActivity extends ActionBarActivity {

	public void apasa(View v) // trebuie sa fie functie generica cu parametru de tip View
    {
    	Toast.makeText(this, "Hello3!", Toast.LENGTH_SHORT).show();
    	System.out.println("Apasat.");
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); // se alege din fisier xml sa se citeasca info pt layout
		if (savedInstanceState == null) {
			Log.println(Log.DEBUG, "created", "fereastra noua");
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		else
			Log.println(Log.DEBUG, "created", "fereastra ce trebuie reprodusa");

		
		Button b = (Button)findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Nu ma mai apasa, ca ma doare!!", Toast.LENGTH_LONG).show();
			}
		});
		
		System.out.println("Created.");
		Log.println(Log.DEBUG, "created", "Created.!");
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		System.out.println("Paused.");
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
		System.out.println("Restarted.");
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		System.out.println("Stopped.");
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		System.out.println("Resumed.");
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		System.out.println("Destroyed.");
	}
	
	 @Override
	    public void onSaveInstanceState (Bundle outState)
	    {
	        // apelarea functiei din activitatea parinte este recomandata, dar nu obligatorie
	    	super.onSaveInstanceState(outState);
	    	System.out.println("Instance Saved.");
	    }
	 
	    @Override
	    public void onRestoreInstanceState (Bundle inState)
	    {
	        // apelarea functiei din activitatea parinte este recomandata, dar nu obligatorie
	    	super.onRestoreInstanceState(inState);
	    	System.out.println("Instance Restored.");
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
