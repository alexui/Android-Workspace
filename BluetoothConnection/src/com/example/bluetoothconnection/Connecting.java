package com.example.bluetoothconnection;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

public class Connecting extends Dialog {

	public Activity c;
	public Dialog d;
	
	public Connecting(Activity a) {
	 super(a);
	
	this.c = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_connect);
	}
	

	@Override
	public void onBackPressed() {
		return;
	}

	
}