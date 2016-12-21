package com.example.interfetegrafice;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class SharedPreferencesActivity extends Activity {

	CheckBox ic,ch;
	RadioButton p,a;
	EditText et;
	Button b;
	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shared_pref_activity);
				
		b=(Button) findViewById(R.id.SAVE);
		et=(EditText) findViewById(R.id.editText);
		TextView tv = (TextView) findViewById(R.id.textView);
		ic = (CheckBox) findViewById(R.id.ICECREAM);
		ch = (CheckBox) findViewById(R.id.CHOCO);
		p= (RadioButton) findViewById(R.id.PEANUT);
		a= (RadioButton) findViewById(R.id.ALMOND);
		
		//se incarca informatia salvata inainte de distrugerea aplicatiei
		SharedPreferences sp =getPreferences(MODE_PRIVATE);
		String s= sp.getString("text", ""); //al doilea parametru este un String default
		tv.setText(s);
		ic.setChecked(sp.getBoolean("icecream", false));
		ch.setChecked(sp.getBoolean("choco", false));
		p.setChecked(sp.getBoolean("peanut", false));
		a.setChecked(sp.getBoolean("almond", false));
		//
				
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//se salveaza informatia in telefon
				SharedPreferences sp = getPreferences(MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("text", et.getText().toString());
				editor.commit();
				//
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		//salvare informatie
		SharedPreferences sp = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("icecream", ic.isChecked());
		editor.putBoolean("choco", ch.isChecked());
		editor.putBoolean("peanut", p.isChecked());
		editor.putBoolean("almond", a.isChecked());
		editor.commit();
	}
	
	
	
}
