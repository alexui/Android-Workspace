package com.example.interfetegrafice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

@SuppressLint("ClickableViewAccessibility")
public class DrawActivity extends Activity {
	
	public static boolean REC = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw_activity);
		
		//se foloseste un surfaceView
		SurfaceView sv = (SurfaceView) findViewById(R.id.surfaceView1);
		
		//se obtine un surfaceHolder din surfaceView
		final SurfaceHolder holder = sv.getHolder();
		
		//se adauga un callBack pe Holder
	/*	holder.addCallback(new Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				//obtine Canvas si bloacheaza threadul curent
				Canvas canvas = holder.lockCanvas();
				
				//editare Canvas
				Paint p = new Paint();
				p.setColor(Color.RED);
				canvas.drawCircle(40, 40, 30, p);

				//postare Canvas
				holder.unlockCanvasAndPost(canvas);
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
		});*/
			
		sv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
					Canvas canvas = holder.lockCanvas();
					float x = event.getX();
					float y = event.getY();
					Paint p = new Paint();
					p.setColor(Color.GREEN);
					canvas.drawColor(Color.BLACK);
					canvas.drawCircle(x, y, 20, p);
					holder.unlockCanvasAndPost(canvas);
				
				return false;
			}
			
		});
	}
	
	

}
