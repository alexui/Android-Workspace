package com.example.battleship;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;



public class Pop extends Dialog implements
android.view.View.OnClickListener{

	public Activity c;
	public Dialog d;
	public Button yes, no;
	
	public Pop(Activity a) {
	 super(a);
	
	this.c = a;
	}

@SuppressLint("ClickableViewAccessibility")
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_pop);
	yes = (Button) findViewById(R.id.button1);
	no = (Button) findViewById(R.id.button2);
	yes.setOnClickListener(this);
	no.setOnClickListener(this);
	
	OnTouchListener l = new OnTouchListener() {
		
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
        }			return false;
		}
	};
	
	yes.setOnTouchListener(l);
	
	no.setOnTouchListener(l);
}

	public void onClick(View v) {
	    switch (v.getId()) {
	    case R.id.button1:
	    	this.dismiss();
	      
	      break;
	    case R.id.button2:
	    {
	    	
	    	PlayerName cdd=new PlayerName(c);
	    	this.dismiss();
	    	cdd.show();
	  	
	    }
	      
	    default:
	      break;
	    }
	   
	  }
	
}
