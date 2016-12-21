package com.example.battleship;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressLint("ClickableViewAccessibility")
public class Avion extends Activity {

	String pname;
	static final String MULTI_PLAYER="multiplayer";
	Boolean multyplayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_avion);
		final Button b=(Button)findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//R.id.TXT_Exit;
				
					System.out.println("Battlleship");
					Pop cdd=new Pop(Avion.this);
					cdd.show();
								
		}} 
		);
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.battle_field_draw, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch(id){
		case R.id.EXIT: finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}