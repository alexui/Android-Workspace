package com.example.battleship;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class PlayerName extends Dialog implements android.view.View.OnClickListener{

	public Activity c;
	public Dialog d;
	public Button ok;
	public EditText et;
	
	public PlayerName(Activity a) {
	 super(a);
	 this.c = a;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.player_name);
		et = (EditText) findViewById(R.id.et);
		ok = (Button) findViewById(R.id.OK);
		ok.setOnClickListener(this);
		ok.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
		            case MotionEvent.ACTION_DOWN: {
		                v.setBackgroundColor(Color.parseColor("#D1D0CE"));
		               // v.invalidate();
		                break;
		            }
		            case MotionEvent.ACTION_UP: {
		                v.setBackgroundColor(Color.WHITE);
		                //v.invalidate();
		                break;
		            }
				}
				return false;
			}
		});
		
	}
	
	public void onClick(View v) {
	    switch (v.getId()) {
	    case R.id.OK:
	    	
	    	Avion avion = (Avion)c;
	    	Intent i = new Intent(this.getContext(), DrawPlane.class);
	    	avion.pname = et.getText().toString();
	    	i.putExtra("name",avion.pname );
	    	i.putExtra(Avion.MULTI_PLAYER, avion.multyplayer);
	    	this.dismiss();
	    	c.startActivity(i);
	    	c.finish();
	    	break;
	       
	    default:
	      break;
	    }
	   
	}
}