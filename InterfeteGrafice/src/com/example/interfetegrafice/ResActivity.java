package com.example.interfetegrafice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ResActivity extends Activity {

	final Intent i  = new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.res_activity);
		
		final Button b1 = (Button) findViewById(R.id.b1);
		final Button b2 = (Button) findViewById(R.id.b2);
		
		
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				i.putExtra("button", b1.getText());
				setResult(Activity.RESULT_OK,i);
				finish();
			}
		});
		
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				i.putExtra("button", b2.getText());
				setResult(Activity.RESULT_OK,i);
				finish();				
			}
		});
				
	}

	@Override
	public void onBackPressed() {
		i.putExtra("button", "Phone BACK");
		setResult(Activity.RESULT_OK,i);
		finish();	
	}

}
