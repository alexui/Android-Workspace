package com.example.interfetegrafice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint({ "NewApi", "CutPasteId" })
public class MainActivity extends ActionBarActivity {

	static final int REQ_CODE =1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		final TextView tv = (TextView)findViewById(R.id.TEXTVIEW);
		
		final Button b1 = (Button) findViewById(R.id.button1);
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv.setText(b1.getText().toString());
				tv.setVisibility(View.VISIBLE);
				Intent i  = new Intent(MainActivity.this,NewActivity.class);
				i.putExtra("button", b1.getText());
				startActivity(i);
			}
		});
		
		final Button b2 = (Button) findViewById(R.id.NEW_WINDOW);
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv.setText(b2.getText().toString());
				Intent i = new Intent(MainActivity.this,NewActivity.class);
				startActivity(i);
			}
		});
		
		final Button b3 = (Button) findViewById(R.id.button2);
		b3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv.setText(b3.getText().toString());
				Intent i = new Intent(MainActivity.this,NewActivity.class);
				startActivity(i);
				startActivity(i);
			}
		});
		
		final Button b5 = (Button) findViewById(R.id.CHG_CLR);
		b5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv.setText(b5.getText());
				v.setBackgroundColor(0xFF00FF00);
				v.setPressed(true);
			}
		});
		
		final Button b4 = (Button) findViewById(R.id.INV);
		b4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv.setText(b4.getText());
				v.setVisibility(View.INVISIBLE);
			}
		});
		
		final Button b6= (Button) findViewById(R.id.ACTION);
		b6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv.setText(b6.getText());
				Intent i = new Intent(MainActivity.this,ResActivity.class);	
				startActivityForResult(i, REQ_CODE);
			}
		});
		
		final Button b7=(Button) findViewById(R.id.DRAW);
		b7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv.setText(b7.getText());
				Intent i = new Intent(MainActivity.this,DrawActivity.class);
				startActivity(i);
			}
		});
		
		final Button b8=(Button) findViewById(R.id.SHARED);
		b8.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv.setText(b8.getText());
				Intent i = new Intent(MainActivity.this,SharedPreferencesActivity.class);
				startActivity(i);
			}
		});
	} 

	public void onActivityResult(int Req_Code,int Res_Code,Intent data){
		
		if(Res_Code!=Activity.RESULT_OK)
			return;
		if(Req_Code!=REQ_CODE)
			return;
		
		String b = data.getStringExtra("button");
		TextView tv  = (TextView) findViewById(R.id.TEXTVIEW);
		tv.setText("Return from : "+b);
		
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
