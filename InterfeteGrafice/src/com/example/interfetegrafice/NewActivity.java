package com.example.interfetegrafice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NewActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity_main);
		
		final Button back = (Button) findViewById(R.id.back);
		TextView tv = (TextView) findViewById(R.id.TEXTVIEW);
		final Button chg_img = (Button) findViewById(R.id.CHG_IMG);
		final ImageView iv = (ImageView) findViewById(R.id.image);
		
		String txt = getIntent().getStringExtra("button");
		if(txt!=null){
			tv.setVisibility(View.VISIBLE);
			tv.setText(txt);
		}
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		chg_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iv.setImageResource(R.drawable.image2);
			}
		});
	}
	
}
