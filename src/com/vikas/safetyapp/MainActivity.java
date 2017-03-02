package com.vikas.safetyapp;


import java.lang.reflect.Method;
import android.os.PowerManager;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.vikas.womensafetyapp.R;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


public class MainActivity extends Activity {
	
	PowerManager.WakeLock wakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SQLiteDatabase db;
		db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
		String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS con ( " +
	            "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "number INTEGER )";
	    db.execSQL(CREATE_BOOK_TABLE);
	    String CREATE_BOOK_TABLE1 = "CREATE TABLE IF NOT EXISTS con1 ( " +
	            "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "number INTEGER )";
	    db.execSQL(CREATE_BOOK_TABLE1);
	    db.close();
		
		
	}

	
	public void register(View v) {
		Intent i_register=new Intent(MainActivity.this,EnterNo.class);
		startActivity(i_register);
		
	}
	public void instruct(View v) {	
		Intent i_help=new Intent(MainActivity.this,Instructions.class);
		
	//	int i= getDefaultSimmm(this);
		//Toast.makeText(getApplicationContext(),i+"", Toast.LENGTH_SHORT).show();
	    
		
	    startActivity(i_help);
	}
public void start(View v) {
	//Intent i_view=new Intent(MainActivity.this,MainActivity.class);
	
	Cursor c,c1;
	SQLiteDatabase db;
	db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
	   c=db.rawQuery("SELECT * FROM con where id=1 ", null);
	   c1=db.rawQuery("SELECT * FROM con1 where id=1 ", null);
	   c.moveToFirst();
	   c1.moveToFirst();
		//Toast.makeText(getApplicationContext(), c.getString(1)+"**"+c1.getString(1),Toast.LENGTH_SHORT).show();
	 
	   if(c.getCount()!=0&&c1.getCount()!=0)
	   {
			Intent i_startservice=new Intent(MainActivity.this,BgService.class);
			
			startService(i_startservice);Toast.makeText(getApplicationContext(), "Service has been started", Toast.LENGTH_SHORT).show();

			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,"My wakelook");
			wakeLock.acquire();
			
			
	   }else if(c.getCount()==0&&c1.getCount()==0)
	   {
		   Toast.makeText(getApplicationContext(), "Plese Register Both numbers", Toast.LENGTH_SHORT).show();
			
			   
	   }
	   else if(c.getCount()==0&&c1.getCount()!=0)
	   {
		   Toast.makeText(getApplicationContext(), "Plese Register First number", Toast.LENGTH_SHORT).show();
			
			  
	   }
	   else if(c.getCount()!=0&&c1.getCount()==0)
	   {
		   Toast.makeText(getApplicationContext(), "Plese Register Second number", Toast.LENGTH_SHORT).show();
			
			  
	   }
	
	
	   db.close();
		
	}
public void stop(View v) {	
	//Intent i_verify=new Intent(MainActivity.this,MainActivity.class);
	
	

    if ((wakeLock!= null) && (wakeLock.isHeld()))
		{
	wakeLock.release();
		}
  
    stopService(new Intent(MainActivity.this, BgService.class));
	
	 //   Toast.makeText(getApplicationContext(), "Service has been stopped", Toast.LENGTH_SHORT).show();
	  //  startActivity(i_verify);
}
private boolean isMyServiceRunning() {
    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        if ("packagename".equals(service.service.getClassName()))
        {
            return true;
        }
    }
    return false;
}



}
