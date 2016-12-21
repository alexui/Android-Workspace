package com.example.battleship;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
public class WrongActivity extends Dialog {

	public Activity c;
	public Dialog d;
	
	public WrongActivity(Activity a) {
	 super(a);
	
	this.c = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wrong);
	}


}
