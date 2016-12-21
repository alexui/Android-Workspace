package com.example.liste;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

class Personaj
{
    public String nume;
    public String desen;
    public Integer img_src;
 
    @Override
    public String toString ()
    {
        // acesta functie este apelata de catre ArrayAdapter pentru a transforma obiectul intr-un String ce
        // sa fie afisat in lista
        return nume+" din desenul animat "+desen;
    } 
}
 
@SuppressLint("NewApi")
public class MainActivity extends ListActivity
{
    ArrayList<Personaj> personaje;
    ArrayAdapter<Personaj> adapter;
    Button b,new_b,conv_b;
    EditText enume;
	EditText edesen;
    
    @Override
    public void onCreate (Bundle savedInstanceBundle)
    {
    	super.onCreate (savedInstanceBundle);
        personaje = new ArrayList<Personaj>();
        adapter = new ArrayAdapter<Personaj>(this, android.R.layout.simple_list_item_1, personaje);
        setContentView (R.layout.activity_main);
        setListAdapter (adapter);
 
        // adaugam cateva personaje in lista
        adaugaFunnyGuy("Bugs Bunny", "Looney Toons");
        adaugaFunnyGuy("Fred Flinstone", "The Flinstones");
        adaugaFunnyGuy("Betty Rubble", "The Flinstones");
        
        b = (Button) findViewById(R.id.ADD);
        new_b = (Button) findViewById(R.id.BASE_ADAPTER);
        conv_b = (Button) findViewById(R.id.CONVERT_VIEW);
        enume = (EditText)findViewById(R.id.edit_nume);
        edesen = (EditText)findViewById(R.id.edit_desen);
        
        //incarca informatie salvata
        
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        enume.setText(sp.getString("nume", ""));
        edesen.setText(sp.getString("desen", ""));
        
        b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(enume.getText().toString().isEmpty() || edesen.getText().toString().isEmpty()){
					Toast.makeText(MainActivity.this, "Please insert text.", Toast.LENGTH_SHORT).show();
				}	
				
				else
					{
					adaugaFunnyGuy(enume.getText().toString(), edesen.getText().toString());
					enume.setText("");
					edesen.setText("");
					//elimina cursor
					enume.setFocusable(false);
					edesen.setFocusable(false);
					enume.setFocusableInTouchMode(true);
					edesen.setFocusableInTouchMode(true);
					}
			}
		});
    
        new_b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, BaseAdapterActivity.class);
				startActivity(i);
			}
		});
        
        conv_b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,ConvertViewActivity.class);
				startActivity(i);
			}
		});
        
        this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int pos, long id) {
				Toast.makeText(MainActivity.this,"pos:"+pos+" id:"+id, Toast.LENGTH_SHORT).show();
				personaje.remove(pos);
				adapter.notifyDataSetChanged();
				return false;
			}
		});
    }
     
    @Override
    public void onListItemClick (ListView list, View v, int position, long id)
    {
        // afisam numele personajului pe care s-a dat click folosind un Toast
        Toast.makeText(MainActivity.this, personaje.get(position).nume, Toast.LENGTH_SHORT).show();
    }
 
    private void adaugaFunnyGuy (String nume, String desen)
    {
        Personaj p = new Personaj ();
        p.nume = nume;
        p.desen = desen;
        personaje.add (p);
        // acesta functie determina adaptorul sa ceara listei sa reafiseze continutul
        adapter.notifyDataSetChanged();   
    }

	
    @Override
   	protected void onStop() {
   		super.onStop();
   		
   		//salveaza continut 
   		SharedPreferences sp = getPreferences(MODE_PRIVATE);
   		SharedPreferences.Editor editor = sp.edit();
   		
   		editor.putString("nume",enume.getText().toString());
   		editor.putString("desen",edesen.getText().toString());
   		editor.commit();
   	}
    
}
