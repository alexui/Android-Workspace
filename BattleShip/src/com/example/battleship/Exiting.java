package com.example.battleship;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Exiting extends Dialog {

	public PlayerActivity c;
	public Dialog d;
	public ExitType et;
	TextView Text;
	Button retry,close;
	
	static enum ExitType{
		WINNER,LOOSER
	}
	
	public Exiting(PlayerActivity a,ExitType et) {
	 super(a);
	 this.et=et;
	 c=a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Exiting");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exiting);
		Text = (TextView) findViewById(R.id.TEXT);
		retry = (Button) findViewById(R.id.RETRY);
		close = (Button) findViewById(R.id.CANCEL);
		
		switch(et){
		case WINNER:Text.setText("WINNER");
					Text.setTextColor(Color.GREEN);
					break;
		case LOOSER:Text.setText("LOOSER");
					Text.setTextColor(Color.RED);
					break;
		default:Text.setText("Exiting");
		}
		
		retry.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Exiting.this.dismiss();
				c.closeActivity();
			}
		});		
		
		close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Exiting.this.dismiss();
				c.closeActivity();
			}
		});
		
	}
	

	@Override
	public void onBackPressed() {
		return;
	}

	
}