package com.example.battleship;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

public class Searching extends Dialog {

	public Activity c;
	public Dialog d;
	
	public Searching(Activity a) {
	 super(a);
	
	this.c = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_searching);
	}
	

	@Override
	public void onBackPressed() {
		return;
	}

	
}