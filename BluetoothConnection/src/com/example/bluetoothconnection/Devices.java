package com.example.bluetoothconnection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

class MyAdapter extends BaseAdapter
{
	
	private Activity context;
	private ArrayList<Dispozitiv> myList;
	
	public MyAdapter(Activity context, ArrayList<Dispozitiv> list){
		this.context=context;
		myList=list;
	}
	
	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView (int position, View convertView, ViewGroup list) 
	{
		// functia trebuie sa intoarca view-ul de pe pozitia position din lista
		// convertView este un element din lista ce nu mai este vizibil si poate fi convertit
		View v;
		LayoutInflater inflater = context.getLayoutInflater();
		//se seteaza layout pentru un singur elemental listei
		TextView nume;
		
		v = inflater.inflate(R.layout.activity_device, null);	
		nume = (TextView) v.findViewById(R.id.name);
		nume.setText(myList.get(position).name);
			
		return v;
	}
 
	@Override
        public int getCount ()
        {
			return myList.size();
		// intoarce nr de elemente din lista
	}
 
	@Override
	public Object getItem(int position) 
	{
		return myList.get(position);
		// intoarce elementul de pe pozitia position din model
	}
 
	@Override
	public long getItemId(int position) 
	{
		return 0;
		// fiecare element din lista poate avea un id, nu este insa obligatoriu
	}
}

public class Devices extends ListActivity {

	ArrayList<Dispozitiv> mArrayAdapter;
	MyAdapter adapter;
	TextView again;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent i = getIntent();
		
		setContentView(R.layout.activity_devices);
		int nr = i.getIntExtra("nr", 0);
		mArrayAdapter = new ArrayList<Dispozitiv>();
		adapter=new MyAdapter(this, mArrayAdapter);
		setListAdapter(adapter);
		
		for(int k=0;k<nr;k++){
			mArrayAdapter.add(new Dispozitiv(i.getStringExtra("name"+k), "address"+k));
		}
		
		System.out.println("list size: "+mArrayAdapter.size());
		adapter.notifyDataSetChanged();

        this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,long id) {
				v.setBackgroundColor(Color.parseColor("#D1D0CE"));
				Intent i = new Intent();
				i.putExtra("pos", pos);
				setResult(Activity.RESULT_OK, i);
				finish();
			}
		});
        
        again = (TextView)findViewById(R.id.Again);
        again.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra("REDO", true);
				setResult(Activity.RESULT_OK, i);
				finish();
			}
		});
	}

	static public Object bytes2Object( byte raw[] )
	        throws IOException, ClassNotFoundException {
	      ByteArrayInputStream bais = new ByteArrayInputStream( raw );
	      ObjectInputStream ois = new ObjectInputStream( bais );
	      Object o = ois.readObject();
	      return o;
	    }
	
}