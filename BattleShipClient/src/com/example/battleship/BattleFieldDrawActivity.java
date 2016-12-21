package com.example.battleship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("ClickableViewAccessibility")
public class BattleFieldDrawActivity extends Activity {

	static final int SQUARE_NO=64,SQUARE_NO_HORIZONTAL=8,SQUARE_NO_VERTICAL=8,PLANE_SQUARES=8,PLANE1=1,PLANE2=2;
	HashMap<Button,Integer> buttons;
	Set<Button> buttons_set;
	Boolean matrix[][] = new Boolean[SQUARE_NO+1][SQUARE_NO+1];
	Integer field[][] = new Integer[SQUARE_NO+1][SQUARE_NO+1];
	Button retry;
	Button start,next;
	TextView tv;
	ArrayList<Position> plane1,plane2;
	String pname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_space);
		
		buttons = new HashMap<Button, Integer>();
		tv = (TextView) findViewById(R.id.PLAYER);
		retry = (Button) findViewById(R.id.RETRY);
		start = (Button) findViewById(R.id.START);
		next = (Button) findViewById(R.id.NEXT);
		
		plane1=new ArrayList<Position>();
		plane2=new ArrayList<Position>();
		
		pname = this.getIntent().getStringExtra("name");
		
		tv.setText(pname+" -PLANE1");
		
		initializeMatrix(matrix,SQUARE_NO_VERTICAL,SQUARE_NO_HORIZONTAL);
		initializeField(field, SQUARE_NO_VERTICAL, SQUARE_NO_HORIZONTAL);
		
		for(int i=1;i<=SQUARE_NO;i++){
			int id = getResources().getIdentifier("button"+i, "id", getPackageName());
			Button b = (Button)findViewById(id);
			buttons.put(b,i);
			b.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					if(event.getAction()==MotionEvent.ACTION_DOWN){
						v.setBackgroundColor(Color.parseColor("#AC8BF4"));
						
						int i;
						i= buttons.get(v);
						int x,y;
						
						x=(i-1)/SQUARE_NO_HORIZONTAL+1;
						y=(i-1)%SQUARE_NO_VERTICAL+1;
						
						matrix[x][y]=true;
						v.setEnabled(false);
					}
					return false;
				}
			});
						
		}
		
		retry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttons_set= buttons.keySet();
				Iterator<Button> it = buttons_set.iterator();
				while(it.hasNext()){
					Button b=(Button) it.next();
					if(!plane1.isEmpty())
					{
						int i;
						i= buttons.get(b);
						int x,y;
												
						x=(i-1)/SQUARE_NO_HORIZONTAL+1;
						y=(i-1)%SQUARE_NO_VERTICAL+1;
					
						if(!plane1.contains(new Position(x, y))){
							b.setBackgroundColor(Color.parseColor("#E8E6E6"));
							b.setEnabled(true);
						}
					}
					else{
						b.setBackgroundColor(Color.parseColor("#E8E6E6"));
						b.setEnabled(true);
					}
				}
				
				initializeMatrix(matrix,SQUARE_NO_VERTICAL,SQUARE_NO_HORIZONTAL);
			}
		});
		
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Boolean verify = checkBattleField(matrix, SQUARE_NO_VERTICAL, SQUARE_NO_HORIZONTAL);
				if(!verify){
					WrongActivity wa = new WrongActivity(BattleFieldDrawActivity.this);
					wa.show();
				}
				else{
					next.setVisibility(View.GONE);
					start.setVisibility(View.VISIBLE);
					
					tv.setText(pname+" -PLANE2");
					
					for(int i=1;i<=SQUARE_NO_VERTICAL;i++){
						for(int j=1;j<=SQUARE_NO_HORIZONTAL;j++){
							if(matrix[i][j]){
								field[i][j]=PLANE1;
								plane1.add(new Position(i, j));
							}
								
						}
					}
					
					initializeMatrix(matrix, SQUARE_NO_VERTICAL,SQUARE_NO_HORIZONTAL );
					buttons_set = buttons.keySet();
					Iterator<Button> it = buttons_set.iterator();
					while(it.hasNext()){
						Button b = it.next();
						b.setOnTouchListener(new OnTouchListener() {
							
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								
								if(event.getAction()==MotionEvent.ACTION_DOWN){
									v.setBackgroundColor(Color.parseColor("#67ADF0"));
									
									int i;
									i= buttons.get(v);
									int x,y;
									
									x=(i-1)/SQUARE_NO_HORIZONTAL+1;
									y=(i-1)%SQUARE_NO_VERTICAL+1;
									
									matrix[x][y]=true;
									v.setEnabled(false);
								}
								return false;
							}
						});
					}
				}
			}
		});
		
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Boolean verify = checkBattleField(matrix, SQUARE_NO_VERTICAL, SQUARE_NO_HORIZONTAL);
				if(!verify){
					WrongActivity wa = new WrongActivity(BattleFieldDrawActivity.this);
					wa.show();
				}
				else{
					for(int i=1;i<=SQUARE_NO_VERTICAL;i++){
						for(int j=1;j<=SQUARE_NO_HORIZONTAL;j++){
							if(matrix[i][j]){
								field[i][j]=PLANE2;
								plane2.add(new Position(i, j));
							}
								
						}
					}
					
					initializeMatrix(matrix, SQUARE_NO_VERTICAL,SQUARE_NO_HORIZONTAL );
					Intent intent = new Intent(BattleFieldDrawActivity.this,MyFieldFight.class);
					for(int j=1;j<=SQUARE_NO_VERTICAL;j++){
						for(int k=1;k<=SQUARE_NO_HORIZONTAL;k++){
							intent.putExtra("cheie"+j+k,field[j][k]);
						}
					}
					intent.putExtra("name", pname);
					startActivity(intent);
					BattleFieldDrawActivity.this.finish();
				}
			}
		});
			
				
	}
	
	
	/*
	 * metoda verifica daca o nava de pe campul field este construita corect
	 */
	public boolean checkBattleField(Boolean field[][],int rows,int cols){
		
		int n=rows;
		int m=cols;
		int no=0;
		Boolean orientation=false;
		
		for(int i=1;i<=n;i++){
			for(int j=1;j<=m;j++){
								
				if(field[i][j]){

					
					no++;
					
					if(j>1 && i+3<=n && j+1<=m)
					if(field[i+3][j])
					{
						{
							if(field[i+1][j-1] && field[i+1][j+1] && field[i+3][j-1] && field[i+3][j+1])
								orientation=true;
						
							if(field[i][j-1] && field[i][j+1] && field[i+2][j-1] && field[i+2][j+1])
							orientation=true;	
						}
					}
					
					if(i>1 && j+3<=m && i+1<=n)
					if(field[i][j+3])
					{
						{
							if(field[i-1][j] && field[i+1][j] && field[i-1][j+2] && field[i+1][j+2])
								orientation=true;
						
							if(field[i-1][j+1] && field[i+1][j+1] && field[i-1][j+3] && field[i+1][j+3])
							orientation=true;	
						}
					}
				}
			}//endfor	
		}//endfor
		
		//Toast.makeText(getApplicationContext(), String.valueOf(no),Toast.LENGTH_SHORT).show();

		if (orientation && no==PLANE_SQUARES)
			return true;
		else
			return false;
		
	}
	
	public void initializeMatrix(Boolean matrix[][],int no_rows,int no_cols){
		for (int i=0;i<=no_rows;i++)
			for(int j=0;j<=no_cols;j++){
				matrix[i][j]=false;
			}
	}
	
	public void initializeField(Integer matrix[][],int rows,int cols){
		for (int i=0;i<=rows;i++)
			for(int j=0;j<=cols;j++){
				matrix[i][j]=0;
			}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
