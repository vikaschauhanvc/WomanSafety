package com.vikas.safetyapp;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.vikas.womensafetyapp.R;

public class EnterNo extends Activity {
	
	EditText name,number;
	public static final String KEY_ROWID = "id";
	  public static final String KEY_NUM = "number";
	  int flag1=0;
	  int flag2=0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.numberenter);
		
	}
	
	
	
	public void instructions(View v) {
		Intent i_view=new Intent(EnterNo.this,MainActivity.class);
		startActivity(i_view);
			
		}
	
	public void storeInDB(View v) {
	
		number = (EditText) this.findViewById(R.id.editText1);
		String str_number=number.getText().toString();
	if(	number.getText().toString().trim().equals(""))
	{
		
		number.setHint("Please enter first No.");
		//Toast.makeText(getApplicationContext(), "Please enter first No.",Toast.LENGTH_SHORT).show();
		
	}
	else
	{

		
		
		SQLiteDatabase db;
		db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
		String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS con ( " +
	            "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "number INTEGER )";
	    db.execSQL(CREATE_BOOK_TABLE);
		Cursor c=db.rawQuery("SELECT * FROM con where id=1 ", null);
		if(c.getCount()==0)
		{
			ContentValues values = new ContentValues();
		    values.put("number", str_number);
		    db.insert("con", null, values);
		}
		else
		{
			String where = "id=1";
			ContentValues up = new ContentValues();
		    up.put("number", str_number);
			db.update("con", up, where, null);
		}
		
		db.close();
	}
	
			
		}
public void storeInDB1(View v) {
	

	//Toast.makeText(getApplicationContext(), "save started",Toast.LENGTH_LONG).show();
	number = (EditText) this.findViewById(R.id.editText2);
	String str_number=number.getText().toString();
	if(	number.getText().toString().trim().equals(""))
	{
		number.setHint("Please enter Second No.");
		//Toast.makeText(getApplicationContext(), "Please enter Second No.",Toast.LENGTH_SHORT).show();
		
	}
	else
	{
		SQLiteDatabase db;
		db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
		String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS con1 ( " +
	            "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "number INTEGER )";
	    db.execSQL(CREATE_BOOK_TABLE);
		Cursor c=db.rawQuery("SELECT * FROM con1 where id=1 ", null);
		if(c.getCount()==0)
		{
			ContentValues values = new ContentValues();
		    values.put("number", str_number);
		    db.insert("con1", null, values);
		}
		else
		{
			String where = "id=1";
			ContentValues up = new ContentValues();
		    up.put("number", str_number);
			db.update("con1", up, where, null);
		}
		
		db.close();
	}
	
}

public void show(View v) {
	Cursor c,c1;
	SQLiteDatabase db;

	db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
	
	   c=db.rawQuery("SELECT * FROM con where id=1 ", null);
	   c1=db.rawQuery("SELECT * FROM con1 where id=1 ", null);
	   c.moveToFirst();
	   c1.moveToFirst();
		//Toast.makeText(getApplicationContext(), c.getString(1)+"**"+c1.getString(1),Toast.LENGTH_SHORT).show();
	   StringBuffer buffer=new StringBuffer();
	   if(c.getCount()!=0)
	   {
		   buffer.append("First No. "+"\n");
	       buffer.append("Number: "+c.getString(1)+"\n");
		// showMessage("Error", "First no. not found");
	      // return;
	   }
	   else
	   {
		   buffer.append("First No. "+"\n");
	       buffer.append("Not found "+"\n");   
	   }
	   if(c1.getCount()!=0)
	   {
		   buffer.append("Second No. "+"\n");
	       buffer.append("Number: "+c1.getString(1)+"\n");
		// showMessage("Error", "First no. not found");
	     //  return;
	   }
	   else
	   {
		   buffer.append("Second No. "+"\n");
	       buffer.append("Not found "+"\n");    
	   }
	

	  showMessage("Details", buffer.toString());
	//Intent i_startservice=new Intent(Display.this,BgService.class);
	//startService(i_startservice);
      
	   db.close();
}

public void showMessage(String title,String message)
{
    Builder builder=new Builder(this);
    builder.setCancelable(true);
    builder.setTitle(title);
    builder.setMessage(message);
    builder.show();
}





}
