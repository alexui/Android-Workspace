package com.example.battleship;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

class WaitingThread extends Thread{
	
	SinglePlayer act;
	
	public WaitingThread(SinglePlayer act){
		this.act=act;
	}
	
	 @Override
     public void run() {
   	  long startTime = System.currentTimeMillis(); //fetch starting time
		  while((System.currentTimeMillis()-startTime)<act.TIMEOUT);
		  act.runOnUiThread(new Runnable() {
			    public void run() {
			    	act.opponentAttack();
			        act.closeWaitingDialog();
			    }
			});
     }
}

public class SinglePlayer extends PlayerActivity {

	Animation slide_in_left, slide_out_right;
	ViewSwitcher vs;
	TableLayout tl1,tl2;
	String player1,player2;
	TextView p1,p2,result1,result2;
	HashMap<Button,Integer> myField,opField;
	Dialog pop;
	
	//computer prediction
	ArrayList<Position> plane;
	
	Position head1,head2,head1p,head2p;
	
	final int HIT=-1;
	final int MISSED=-2;
	final int HEAD=-3;
	final int TIMEOUT=500;
	final int PROBABILITY=10;
	
	final int NIL_COLOR = Color.parseColor("#E8E6E6");
	final int MISS_COLOR=0;
	final int HIT_COLOR=0;
		
	Integer field[][];
	Integer oponent_field[][];
	Integer oponent_prediction[][];
	
	Boolean RANDOM_ATTACK=true;
	
	public static final Integer PLANE1=1,PLANE2=2,NIL=0;
	public enum Orientation{
		UP,DOWN,RIGHT,LEFT;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_field_activity);
		
		System.out.println("Single Player");
		
		vs = (ViewSwitcher) findViewById(R.id.viewSwitcher);
		tl1 = (TableLayout) findViewById(R.id.TABLE1);
		tl2 = (TableLayout) findViewById(R.id.TABLE2);
		
		p1 = (TextView) findViewById(R.id.PLAYER1);
		p2 = (TextView) findViewById(R.id.PLAYER2);
		result1=(TextView) findViewById(R.id.RESULT1);
		result2=(TextView) findViewById(R.id.RESULT2);
		
		field = new Integer[DrawPlane.SQUARE_NO+1][DrawPlane.SQUARE_NO+1];
		oponent_field = new Integer[DrawPlane.SQUARE_NO+1][DrawPlane.SQUARE_NO+1];
		oponent_prediction = new Integer[DrawPlane.SQUARE_NO+1][DrawPlane.SQUARE_NO+1];
		
		plane = new ArrayList<Position>();
		
		initializeField(field, DrawPlane.SQUARE_NO_VERTICAL, DrawPlane.SQUARE_NO_HORIZONTAL);
		initializeField(oponent_field, DrawPlane.SQUARE_NO_VERTICAL, DrawPlane.SQUARE_NO_HORIZONTAL);
		initializeField(oponent_prediction, DrawPlane.SQUARE_NO_VERTICAL, DrawPlane.SQUARE_NO_HORIZONTAL);
		
		myField = new HashMap<Button, Integer>();
		opField = new HashMap<Button, Integer>();
		
		slide_in_left = AnimationUtils.loadAnimation(this,R.anim.slide_to_left);
		slide_out_right = AnimationUtils.loadAnimation(this,R.anim.slide_to_right);
		  
		View v = findViewById(R.id.main);
		  
		v.setOnTouchListener(new OnSwipeTouchListener(this.getApplicationContext()){
			  @Override
			    public void onSwipeLeft() {
			        vs.setOutAnimation(outToLeftAnimation());
			        vs.setInAnimation(inFromRightAnimation());
			        vs.showPrevious();
			    }
			  
			  @Override
			    public void onSwipeRight() {
				  	vs.setOutAnimation(outToRightAnimation());
			        vs.setInAnimation(inFromLeftAnimation());
			        vs.showNext();
			    }
		  });
		  	  
		player1 = getIntent().getStringExtra("name");
  		p1.setText(player1);
  		
  		createField(oponent_field,DrawPlane.SQUARE_NO_VERTICAL, DrawPlane.SQUARE_NO_HORIZONTAL);
  		p2.setText(android.os.Build.MODEL);
  		
  		for(int j=1;j<=DrawPlane.SQUARE_NO_VERTICAL;j++){
			for(int k=1;k<=DrawPlane.SQUARE_NO_HORIZONTAL;k++){
				int s; 
				s= getIntent().getExtras().getInt("cheie"+j+k);
				int nr;
				if (j==1)
					nr=k;
				else nr=8*(j-1)+k;
				
				int id = getResources().getIdentifier("button"+nr, "id", getPackageName());
				int idp = getResources().getIdentifier("button"+nr+"p", "id", getPackageName());
				Button b = (Button)findViewById(id);
				Button bp = (Button)findViewById(idp);
				if(s==1)
					b.setBackgroundColor(Color.parseColor("#AC8BF4"));
				if(s==2)
					b.setBackgroundColor(Color.parseColor("#67ADF0"));
				
				field[j][k]=s;
				
				myField.put(b, nr);
				opField.put(bp,nr);
				
				bp.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Button b = (Button)v;
						
						int i = opField.get(b);
						int id = getResources().getIdentifier("button"+i+"p", "id", getPackageName());
						Button bp = (Button)findViewById(id);
						
						int x =(i-1)/DrawPlane.SQUARE_NO_HORIZONTAL+1;
						int y =(i-1)%DrawPlane.SQUARE_NO_VERTICAL+1;
						
						Position position = new Position(x, y);
						if(oponent_field[x][y]==PLANE1 || oponent_field[x][y]==PLANE2){
							
							if(position.equals(head1p) || position.equals(head2p))
								{
								oponent_field[x][y]=HEAD;
								result2.setText("HEAD");
								}
							else
							{
								oponent_field[x][y]=HIT;
								result2.setText("HIT");
							}
							bp.setBackgroundColor(Color.RED);	
							result2.setTextColor(Color.RED);
						}
						else{
							if(oponent_field[x][y]==NIL){
								bp.setBackgroundColor(Color.GREEN);
								
								result2.setText("MISSED");
								result2.setTextColor(Color.GREEN);
								}
						}
										        
				        				        
				        pop = new Waiting(SinglePlayer.this);
						pop.setCanceledOnTouchOutside(false);
				        pop.show();
				        
				        WaitingThread wt = new WaitingThread(SinglePlayer.this);
				        wt.start();
					}
				});
			
			}
		}
  		
  		findHeads(field, DrawPlane.SQUARE_NO_VERTICAL, DrawPlane.SQUARE_NO_HORIZONTAL);
  		System.out.println("oponent: "+head1p+" "+head2p);
	}
	
	public void closeWaitingDialog(){
		pop.dismiss();
		if(checkLooser()){
			System.out.println("LOOSER");
			pop = new Exiting(SinglePlayer.this, Exiting.ExitType.LOOSER);
      		pop.setCanceledOnTouchOutside(false);
            pop.show();
		}
		if(checkWinner()){
			System.out.println("WINNER");
			pop = new Exiting(SinglePlayer.this, Exiting.ExitType.WINNER);
      		pop.setCanceledOnTouchOutside(false);
            pop.show();
		}
		vs.showPrevious();
	}
	
	public void createField(Integer[][] field,int rows,int cols){
		
		System.out.println("creating field...");
		Orientation o;
		Random random = new Random();
		int x,y,nr;
		
		do{
			y = random.nextInt(DrawPlane.SQUARE_NO_HORIZONTAL)+1;
			x = random.nextInt(DrawPlane.SQUARE_NO_VERTICAL)+1;
			head1p =	new Position(y,x);
			nr = random.nextInt(Orientation.values().length);
			o=Orientation.values()[nr];
			System.out.println("Trying: "+head1p+" "+o);
			System.out.println("x: "+x+" y:="+y);
		}while(!drawPlane(field,head1p,o,PLANE1));
		
		do{
			y=random.nextInt(DrawPlane.SQUARE_NO_HORIZONTAL)+1;
			x = random.nextInt(DrawPlane.SQUARE_NO_VERTICAL)+1;
			head2p =	new Position(y,x);
			nr = random.nextInt(Orientation.values().length);
			o=Orientation.values()[nr];
			System.out.println("Trying: "+head2p+" "+o);
			System.out.println("x: "+x+" y:="+y);
		}while(!drawPlane(field,head2p,o,PLANE2));
		
		for(int t=1;t<=DrawPlane.SQUARE_NO_VERTICAL;t++){
        	for(int u=1;u<=DrawPlane.SQUARE_NO_HORIZONTAL;u++){
        		System.out.printf("%d ",oponent_field[t][u]);
        	}
        	System.out.println();
        }
	}
	
	public void opponentAttack(){
		
		Random random = new Random();
		int x=0,y=0,index;
		Position attack=null;
		
		if(RANDOM_ATTACK ||plane.isEmpty())
		{
			do{
				x = random.nextInt(DrawPlane.SQUARE_NO_HORIZONTAL)+1;
				y = random.nextInt(DrawPlane.SQUARE_NO_VERTICAL)+1;
			}while(oponent_prediction[x][y]!=NIL);
			
			if(random.nextInt(PROBABILITY)==0){
				RANDOM_ATTACK=true;
			}
		}
		else{
			do{
				index = random.nextInt(plane.size());
				attack = plane.get(index);
				x=attack.y; //x=i
				y=attack.x;	//y=j
				plane.remove(attack);
			}while(oponent_prediction[x][y]!=NIL && !plane.isEmpty());
			
			if(plane.isEmpty() && oponent_prediction[x][y]!=NIL)
				do{
					x = random.nextInt(DrawPlane.SQUARE_NO_HORIZONTAL)+1;
					y = random.nextInt(DrawPlane.SQUARE_NO_VERTICAL)+1;
				}while(oponent_prediction[x][y]!=NIL);
			
			if(random.nextInt(PROBABILITY)==0){
				RANDOM_ATTACK=true;
			}
		}
		
		System.out.println("atac la :"+x+" "+y);
		System.out.println("random: "+RANDOM_ATTACK);
			
		int i = (x-1)*DrawPlane.SQUARE_NO_HORIZONTAL+y;
		int id = getResources().getIdentifier("button"+i, "id", getPackageName());
		Button bp = (Button)findViewById(id);
			
		if(field[x][y]==PLANE1 || field[x][y]==PLANE2){
			field[x][y]=HIT;
			bp.setBackgroundColor(Color.RED);	
			oponent_prediction[x][y]=HIT;
			result1.setText("HIT");
			result1.setTextColor(Color.RED);
			RANDOM_ATTACK=false;
			
			Position position = new Position(x, y);
			if(position.equals(head1) || position.equals(head2))
			{
				field[x][y]=HEAD;
				System.out.println("x: "+x+" y: "+y);
				System.out.println("hx1: "+head1.x+" hy1: "+head1.y);
				System.out.println("hx2: "+head2.x+" hy2: "+head2.y);

				oponent_prediction[x][y]=HEAD;
				result1.setText("HEAD");
				if(x>=2)
					plane.remove(new Position(x-1, y));
				if(x<=DrawPlane.SQUARE_NO_HORIZONTAL-1)
					plane.remove(new Position(x+1, y));
				if(y>=2)
					plane.remove(new Position(x, y-1));
				if(y<=DrawPlane.SQUARE_NO_VERTICAL-1)
					plane.remove(new Position(x, y+1));
			}
			
			else{
				//prepare next attack
				if(x>=2)/*
					if(oponent_prediction[x-1][y]==HIT && x<=DrawPlane.SQUARE_NO_HORIZONTAL-2)
						plane.add(new Position(x+2, y));
					else*/
						plane.add(new Position(x-1, y));
				if(x<=DrawPlane.SQUARE_NO_HORIZONTAL-1)
					/*if(oponent_prediction[x+1][y]==HIT && x>=3)
						plane.add(new Position(x-2, y));
					else*/
						plane.add(new Position(x+1, y));
				if(y>=2)/*
					if(oponent_prediction[x][y-1]==HIT && y<=DrawPlane.SQUARE_NO_VERTICAL-2)
						plane.add(new Position(x, y+2));
					else*/
						plane.add(new Position(x, y-1));
				if(y<=DrawPlane.SQUARE_NO_VERTICAL-1)/*
					if(oponent_prediction[x][y+1]==HIT && y>=3)
						plane.add(new Position(x, y-2));
					else*/
						plane.add(new Position(x, y+1));
			}
		}
		else{
			if(field[x][y]==NIL){
				bp.setBackgroundColor(Color.GREEN);
				oponent_prediction[x][y]=MISSED;
				result1.setText("MISSED");
				result1.setTextColor(Color.GREEN);
				
				//prepare next attack
			/*	if(x>=2)
					if(oponent_prediction[x-1][y]==HIT && x<=DrawPlane.SQUARE_NO_HORIZONTAL-1)
						plane.remove(new Position(x+1, y));
				if(x<=DrawPlane.SQUARE_NO_HORIZONTAL-1)
					if(oponent_prediction[x+1][y]==HIT && x>=2)
						plane.remove(new Position(x-1, y));
				if(y>=2)
					if(oponent_prediction[x][y-1]==HIT && y<=DrawPlane.SQUARE_NO_VERTICAL-1)
						plane.remove(new Position(x, y+1));
				if(y<=DrawPlane.SQUARE_NO_VERTICAL-1)
					if(oponent_prediction[x][y+1]==HIT && y>=2)
						plane.remove(new Position(x, y-1));*/
				}
			}
		System.out.println("plane size: "+plane.size());
		System.out.println("plane: "+plane);
		
	}
			
	public boolean drawPlane(Integer[][] field,Position head,Orientation o,Integer plane){
		
		System.out.println("drawing...");
		int i=head.x;
		int j=head.y;
		if(o==Orientation.UP){
			
			if(field[j][i]!=NIL)
				return false;
			
			if(j<4 ||
					i<2 || i>DrawPlane.SQUARE_NO_VERTICAL-1)
				return false;
			for(int k=j;k>j-4;k--){
				if(field[k][i]!=NIL)
					return false;
			}
						
			if(field[j-1][i-1]!=NIL ||
			   field[j-1][i+1]!=NIL ||
			   field[j-3][i-1]!=NIL ||
			   field[j-3][i+1]!=NIL)
				return false;
			
			field[j][i]=plane;
			
			for(int k=j;k>j-4;k--){
				field[k][i]=plane;
			}
			
			field[j-1][i-1]=plane;
			field[j-1][i+1]=plane;
			field[j-3][i-1]=plane;
			field[j-3][i+1]=plane;
		}
		
		if(o==Orientation.DOWN ){
			
			if(field[j][i]!=NIL)
				return false;
			
			if(j+3>DrawPlane.SQUARE_NO_HORIZONTAL ||
					i<2 || i>DrawPlane.SQUARE_NO_VERTICAL-1)
				return false;
			for(int k=j;k<j+4;k++){
				if(field[k][i]!=NIL)
					return false;
			}
			
			if(field[j+1][i-1]!=NIL ||
			   field[j+1][i+1]!=NIL ||
			   field[j+3][i-1]!=NIL ||
			   field[j+3][i+1]!=NIL)
				return false;
			
			field[j][i]=plane;
			
			for(int k=j;k<j+4;k++){
				field[k][i]=plane;
			}
			
			field[j+1][i-1]=plane;
			field[j+1][i+1]=plane;
			field[j+3][i-1]=plane;
			field[j+3][i+1]=plane;
		}
		
		if(o==Orientation.RIGHT){
			
			if(field[j][i]!=NIL)
				return false;
			
			if(i+3>DrawPlane.SQUARE_NO_VERTICAL ||
					j<2 || j>DrawPlane.SQUARE_NO_HORIZONTAL-1)
				return false;
			for(int k=i;k<i+4;k++){
				if(field[j][k]!=NIL)
					return false;
			}
			
			if(field[j-1][i+1]!=NIL ||
			   field[j+1][i+1]!=NIL ||
			   field[j-1][i+3]!=NIL ||
			   field[j+1][i+3]!=NIL)
				return false;
			
			field[j][i]=plane;
			
			for(int k=i;k<i+4;k++){
				field[j][k]=plane;
			}
			
			field[j-1][i+1]=plane;
			field[j+1][i+1]=plane;
			field[j-1][i+3]=plane;
			field[j+1][i+3]=plane;
		}
		
		if(o==Orientation.LEFT){
			
			if(field[j][i]!=NIL)
				return false;
			
			if(i<4 ||
					j<2 || j>DrawPlane.SQUARE_NO_HORIZONTAL-1)
				return false;
			for(int k=i;k>i-4;k--){
				if(field[j][k]!=NIL)
					return false;
			}
			
			if(field[j-1][i-1]!=NIL ||
			   field[j+1][i-1]!=NIL ||
			   field[j-1][i-3]!=NIL ||
			   field[j+1][i-3]!=NIL)
				return false;
			
			field[j][i]=plane;
			
			for(int k=i;k>i-4;k--){
				field[j][k]=plane;
			}
			
			field[j-1][i-1]=plane;
			field[j+1][i-1]=plane;
			field[j-1][i-3]=plane;
			field[j+1][i-3]=plane;
		}
		
		System.out.println("ready...");
		return true;
		
	}
	
	public void findHeads(Integer[][] field,int m,int n){
		
		int i,j,first_plane=NIL;
		head1=null;
		head2=null;
		for(i=1;i<=n;i++)
			for(j=1;j<=m;j++){
				if(field[i][j]!=NIL && field[i][j]!=first_plane){
					if(j>1 && i+3<=n && j+1<=m)
					if(field[i][j]==field[i+3][j]){
						if(field[i+3][j-1]==field[i+3][j] && field[i+3][j]==field[i+3][j+1])
						{
							first_plane=field[i][j];
							if(field[i][j]==PLANE1){
								head1= new Position(i, j);
							}
							if(field[i][j]==PLANE2){
								head2= new Position(i, j);
							}
						}
						if(field[i][j-1]==field[i][j] && field[i][j]==field[i][j+1])
						{
							first_plane=field[i][j];
							if(field[i][j]==PLANE1){
								head1= new Position(i+3,j);
							}
							if(field[i][j]==PLANE2){
								head2= new Position(i+3,j);
							}
						}
					}
					if(i>1 && j+3<=m && i+1<=n)
					if(field[i][j]==field[i][j+3])
					{
						if(field[i-1][j+3]==field[i][j+3] && field[i][j+3]==field[i+1][j+3])
						{
							first_plane=field[i][j];
							if(field[i][j]==PLANE1){
								head1= new Position(i, j);
							}
							if(field[i][j]==PLANE2){
								head2= new Position(i, j);
							}
						}
						if(field[i-1][j]==field[i][j] && field[i][j]==field[i+1][j])
						{
							first_plane=field[i][j];
							if(field[i][j]==PLANE1){
								head1= new Position(i, j+3);
							}
							if(field[i][j]==PLANE2){
								head2= new Position(i, j+3);
							}
						}
					}		
				}
				if(head1!=null && head2!=null)
					break;
			}
	
		System.out.println("head1: "+head1);
		System.out.println("head2: "+head2);

	}
	
	public void initializeField(Integer matrix[][],int rows,int cols){
		for (int i=0;i<=rows;i++)
			for(int j=0;j<=cols;j++){
				matrix[i][j]=NIL;
			}
	}
				
	static public byte[] object2Bytes( Object o ) throws IOException {
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      ObjectOutputStream oos = new ObjectOutputStream( baos );
	      oos.writeObject( o );
	      return baos.toByteArray();
	    }
		
	public Boolean checkLooser(){
		if(field[head1.y][head1.x]==HEAD && field[head2.y][head2.x]==HEAD)
			return true;
		return false;
	}
	
	public Boolean checkWinner(){
		if(oponent_field[head1p.y][head1p.x]==HEAD && oponent_field[head2p.y][head2p.x]==HEAD)
			return true;
		return false;
	}
	
	@Override
	public void onBackPressed() {
		return;
	}
	
	private Animation outToLeftAnimation() {
	    Animation outtoLeft = new TranslateAnimation(
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, -1.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f);
	    outtoLeft.setDuration(200);
	    outtoLeft.setInterpolator(new DecelerateInterpolator());
	    return outtoLeft;
	    }

	private Animation outToRightAnimation() {
	    Animation outtoRight = new TranslateAnimation(
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, +1.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f);
	    outtoRight.setDuration(200);
	    outtoRight.setInterpolator(new DecelerateInterpolator());
	    return outtoRight;
	    }
	
	private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(200);
        inFromRight.setInterpolator(new DecelerateInterpolator());
        return inFromRight;
        }
	
	private Animation inFromLeftAnimation() {
	    Animation inFromLeft = new TranslateAnimation(
	        Animation.RELATIVE_TO_PARENT, -1.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f);
	    inFromLeft.setDuration(200);
	    inFromLeft.setInterpolator(new DecelerateInterpolator());
	    return inFromLeft;
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

	@Override
	public void closeActivity(){		
		finish();
	}

	@Override
	public void restart() {
		
		
	}
	
}
