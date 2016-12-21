package com.example.liste;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//imbunatatire: pentru a preveni accesarea xml urilor in randuri repetate se folosesc taguri (pointer)
class myTag{
	public TextView nume;
	public TextView desen;
	public ImageView img;
}

@SuppressLint({ "ViewHolder", "NewApi" })

class NewAdapter extends BaseAdapter
{
	
	private Activity context;
	private ArrayList<Personaj> myList;
	
	public NewAdapter(Activity context, ArrayList<Personaj> list){
		this.context=context;
		myList=list;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView (int position, View convertView, ViewGroup list) 
	{
		// functia trebuie sa intoarca view-ul de pe pozitia position din lista
		// convertView este un element din lista ce nu mai este vizibil si poate fi convertit
		View v;
		
		//se seteaza layout pentru un singur elemental listei
		myTag tag = new myTag();
		
		//imbunatatire: daca convert_view e null inseamna ca nu s-a folosit scroll->inflate
		//daca s-a dat scroll e mai bine sa refolosim primele viewuri pentru a le adauga la sfarsit
		if(convertView==null){
			
			LayoutInflater inflater=context.getLayoutInflater();
			v = inflater.inflate(R.layout.personaj, null);
			/*
			nume = (TextView) v.findViewById(R.id.personaj_nume);
			desen = (TextView) v.findViewById(R.id.personaj_desen);
			*/
			tag.nume = (TextView) v.findViewById(R.id.personaj_nume);
			tag.desen=(TextView) v.findViewById(R.id.personaj_desen);
			tag.img=(ImageView) v.findViewById(R.id.IMG);
			
			if(position%2==0){
				
				//ImageView img = (ImageView) v.findViewById(R.id.IMG);	
				tag.img.setImageResource(myList.get(position).img_src);
			}
			
			tag.nume.setText(myList.get(position).nume);
			tag.desen.setText(myList.get(position).desen);
		}
		
		else{
			v=convertView;
			tag = (myTag) v.getTag();
					
			if(position%2==0){
				
				tag.img.setImageResource(myList.get(position).img_src);
			}
			
			tag.nume.setText(myList.get(position).nume);
			tag.desen.setText(myList.get(position).desen);
		}
	
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

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		return position%2;
	}
}

@SuppressLint("NewApi")
public class ConvertViewActivity extends ListActivity {

	ArrayList<Personaj> personaje;
	BaseAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_adapter_activity);
		
		personaje = new ArrayList<Personaj>();
		myAdapter = new MyAdapter(this, personaje);
		setListAdapter(myAdapter);
		
        // adaugam cateva personaje in lista
        adaugaFunnyGuy("Bugs Bunny", "Looney Toons",R.drawable.bunny);
        adaugaFunnyGuy("Fred Flinstone", "The Flinstones",R.drawable.fred);
        adaugaFunnyGuy("Betty Rubble", "The Flinstones",R.drawable.betty);
        adaugaFunnyGuy("Bugs Bunny", "Looney Toons",R.drawable.bunny);
        adaugaFunnyGuy("Fred Flinstone", "The Flinstones",R.drawable.fred);
        adaugaFunnyGuy("Betty Rubble", "The Flinstones",R.drawable.betty);
        adaugaFunnyGuy("Bugs Bunny", "Looney Toons",R.drawable.bunny);
        adaugaFunnyGuy("Fred Flinstone", "The Flinstones",R.drawable.fred);
        adaugaFunnyGuy("Betty Rubble", "The Flinstones",R.drawable.betty);
        adaugaFunnyGuy("Bugs Bunny", "Looney Toons",R.drawable.bunny);
        adaugaFunnyGuy("Fred Flinstone", "The Flinstones",R.drawable.fred);
        adaugaFunnyGuy("Betty Rubble", "The Flinstones",R.drawable.betty);
        adaugaFunnyGuy("Bugs Bunny", "Looney Toons",R.drawable.bunny);
        adaugaFunnyGuy("Fred Flinstone", "The Flinstones",R.drawable.fred);
        adaugaFunnyGuy("Betty Rubble", "The Flinstones",R.drawable.betty);adaugaFunnyGuy("Bugs Bunny", "Looney Toons",R.drawable.bunny);
        adaugaFunnyGuy("Fred Flinstone", "The Flinstones",R.drawable.fred);
        adaugaFunnyGuy("Betty Rubble", "The Flinstones",R.drawable.betty);
        adaugaFunnyGuy("Bugs Bunny", "Looney Toons",R.drawable.bunny);
        adaugaFunnyGuy("Fred Flinstone", "The Flinstones",R.drawable.fred);
        adaugaFunnyGuy("Betty Rubble", "The Flinstones",R.drawable.betty);
        
        Button b = (Button) findViewById(R.id.ADD);
        final EditText enume = (EditText)findViewById(R.id.edit_nume);
        final EditText edesen = (EditText)findViewById(R.id.edit_desen);
        b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(enume.getText().toString().isEmpty() || edesen.getText().toString().isEmpty()){
					Toast.makeText(ConvertViewActivity.this, "Please insert text.", Toast.LENGTH_SHORT).show();
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
    
        
        
        this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int pos, long id) {
				Toast.makeText(ConvertViewActivity.this,"pos:"+pos+" id:"+id, Toast.LENGTH_SHORT).show();
				personaje.remove(pos);
				myAdapter.notifyDataSetChanged();
				return false;
			}
		});
    }
		
	public void adaugaFunnyGuy (String nume, String desen,Integer src)
    {
        Personaj p = new Personaj ();
        p.nume = nume;
        p.desen = desen;
        p.img_src=src;
        personaje.add (p);
        // acesta functie determina adaptorul sa ceara listei sa reafiseze continutul
        myAdapter.notifyDataSetChanged();   
    }
	
	public void adaugaFunnyGuy (String nume, String desen)
    {
        Personaj p = new Personaj ();
        p.nume = nume;
        p.desen = desen;
        p.img_src=R.drawable.nophoto;
        personaje.add (p);
        // acesta functie determina adaptorul sa ceara listei sa reafiseze continutul
        myAdapter.notifyDataSetChanged();   
    }
	
}


