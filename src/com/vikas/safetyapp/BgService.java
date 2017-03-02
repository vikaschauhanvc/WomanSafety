package com.vikas.safetyapp;




import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("HandlerLeak")


public class BgService extends Service implements AccelerometerListener{
	
    String str_address;


    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
	  
    // Handler that receives messages from the thread.
    private final class ServiceHandler extends Handler {
    	
        public ServiceHandler(Looper looper) {
        	
        	super(looper);
        }
	    @Override
	    public void handleMessage(Message msg) {

	    
	    }
    }
    
    
	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
    }
	
	
	
	@Override
	public void onCreate() {
		super.onCreate();
        
		
		 if (AccelerometerManager.isSupported(this)) {
             
             AccelerometerManager.startListening(this);
         }
	    HandlerThread thread = new HandlerThread("ServiceStartArguments",android.os.Process.THREAD_PRIORITY_BACKGROUND);
	    thread.start();
	    
	    mServiceLooper = thread.getLooper();
	    
	    mServiceHandler = new ServiceHandler(mServiceLooper);		
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
	    Message msg = mServiceHandler.obtainMessage();
	    msg.arg1 = startId;
	    mServiceHandler.sendMessage(msg);	
		return START_STICKY;
	}	
	
	

	@Override
	public void onAccelerationChanged(float x, float y, float z) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onShake(float force) throws IOException {
		
		GPSTracker gps;
		gps = new GPSTracker(BgService.this);
        if(gps.canGetLocation())
        {
        	
        	double latitude = gps.getLatitude();
        	double longitude = gps.getLongitude();
        	Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                   	String result = null;
           
             	
                 List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                 if (addressList != null && addressList.size() > 0)
                 {
                     Address address = addressList.get(0);
                     StringBuilder sb = new StringBuilder();
                     for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                         sb.append(address.getAddressLine(i)).append("\n");
                     }
                     sb.append(address.getLocality()).append("\n");
                     sb.append(address.getPostalCode()).append("\n");
                     sb.append(address.getCountryName());
                     result = sb.toString();
                              
                 }
                 String num1="";
                 String num2="";
                 SQLiteDatabase db;
         		db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
         		Cursor c=db.rawQuery("SELECT * FROM con where id = 1 ", null);
         		Cursor c1=db.rawQuery("SELECT * FROM con1 where id =1 ", null);
         		c.moveToFirst();
         		c1.moveToFirst(); 
         		num1=c.getString(1);
         		num2=c1.getString(1);
         		       
        db.close();
      	Intent intent=new Intent(getApplicationContext(),MainActivity.class);
		PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
     	SmsManager sms=SmsManager.getDefault();
		sms.sendTextMessage(num1, null,"I am in danger and my current location is "+result, pi,null);
		SmsManager sms1=SmsManager.getDefault();
		sms1.sendTextMessage(num2, null, "I am in danger and my current location is "+result, pi,null);
	
        }
        else{
        	gps.showSettingsAlert();
		}   
		
	}
	
	
	
	
	
	// onDestroy method.   Display toast that service has stopped.
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// Toast Service Stopped.
		Context context = getApplicationContext();
		
		   Log.i("Sensor", "Service  distroy");
	         
	        if (AccelerometerManager.isListening()) {
	             
	            AccelerometerManager.stopListening();
	             
	        }
		
		CharSequence text = "Women Safety App Service Stopped";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		
	}
	
	
	
}