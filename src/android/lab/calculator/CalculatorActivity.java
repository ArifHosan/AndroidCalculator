package android.lab.calculator;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatorActivity extends Activity{
	public static final String PREFERENCES_FILE_NAME="MyPrefs";
	public static final String FILE_NAME="Storage.dat";
	public static DatabaseHandler db;
	private TextView display,history;
	private String first,second,op;
	private boolean clear,resultmode;
	private MediaPlayer media;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		db=new DatabaseHandler(CalculatorActivity.this);
		this.history=(TextView)findViewById(R.id.history);
		this.display=(TextView)findViewById(R.id.display);
		this.display.setText("0");
		this.first="";
		this.second="";
		this.op="";
		this.clear=false;
		this.resultmode=false;
		
		Bundle extras=getIntent().getExtras();
		if(extras!=null) {
			this.history.setText(extras.getString("history"));
			this.display.setText(extras.getString("result"));
			this.resultmode=true;
			if(extras.getString("history")!=null) {
				String parts[]=extras.getString("history").split(" ");
				this.first=extras.getString("result");
				this.op=parts[1];
				this.second=parts[2];
			}
		}
		
		Log.d("EntryLog","Inside OnCreate of Main Activity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("EntryLog","Inside OnResume of Main Activity");
		this.media=MediaPlayer.create(this,R.raw.audio);
		Toast.makeText(this, "Current Value in M is "+this.getMemValue(), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("EntryLog","Inside OnPause of Main Activity");
		this.media.release();
		this.media=null;
	}

	public void onNumClick(View arg0) {		
		if(this.media.isPlaying()) this.media.stop();
		this.media.start();
		Button btn=(Button)arg0;
		Log.d("DebugLog","Inside onNumClick because of "+btn.getText());
		Log.d("DebugLog",this.display.getText().length()+"");	
		
		if(this.resultmode) {
			onReset();
		}
		if(this.display.getText().equals("0") && !btn.getText().equals(".")) {
			this.display.setText("");
		}
		if(this.clear) this.display.setText("");
		if(btn.getText().equals(".") && this.display.getText().length()==0) this.display.setText("0"); 
		if(btn.getText().equals(".") && this.display.getText().toString().indexOf('.')>=0) return;
		this.display.setText(this.display.getText().toString() + btn.getText().toString());	
	}
	
	public void onOpClick(View arg0) {
		if(this.media.isPlaying()) this.media.stop();
		this.media.start();
		Button btn=(Button)arg0;
		Log.d("DebugLog","Inside onOpClick because of "+op);
		
		if(this.display.getText().equals("0") && btn.getText().equals("-")) {
			this.display.setText("-");
			this.clear=false;
		}
		
		else if(this.first.length()==0) {
			this.op=btn.getText().toString();
			this.first=this.display.getText().toString();
			this.history.setText(this.first+" "+op);
			this.display.setText("0");
		}
		else if(this.resultmode) {
			this.op=btn.getText().toString();
			this.resultmode=false;
			this.history.setText(this.first+" "+op);
			this.display.setText("0");
		}
		else {
			this.second=this.display.getText().toString();
			if(this.second.length()==0) {
				this.op=btn.getText().toString();
				this.history.setText(this.first+" "+op);
				return;
			}
			this.history.setText(this.first+" "+this.op+" "+this.second);
			this.performCalculation();
			this.op=btn.getText().toString();
			this.history.setText(this.first+" "+op);
			this.display.setText("0");
			clear=true;
		}
	}
	
	public void onEqualClick(View arg0) {
		if(this.media.isPlaying()) this.media.stop();
		this.media.start();
		Log.d("DebugLog","Inside onEqualClick");
		if(this.first.length()==0) return;
		if(this.resultmode) {
			this.history.setText(this.first+" "+op+" "+this.second);
			performCalculation();
		}
		else {
			second=this.display.getText().toString();
			this.history.setText(this.first+" "+op+" "+this.second);
			
			this.performCalculation();			
			this.resultmode=true;
		}
		
		//this.display.setText(first);
	}
	
	public void performCalculation() {
		/*Intent i=new Intent(getApplicationContext(),CalculationActivity.class);
		i.putExtra("first", this.first);
		i.putExtra("second", this.second);
		i.putExtra("op", this.op);
		i.putExtra("history", this.history.getText());
		startActivityForResult(i, 1);*/
		
		if(this.first.length()==0 || this.second.length()==0) return;
		Log.d("EntryLog","Inside PerformCalculation of Main Activity");
		double a=Double.valueOf(first);
		Log.d("DebugLog","Value of a is: "+a);
		double b=Double.valueOf(second);
		Log.d("DebugLog","Value of b is: "+b);
		double res=a;
		if(op.equals("+")) res=a+b;
		else if(op.equals("-")) res=a-b;
		else if(op.equals("X")) res=a*b;
		else if(op.equals("/")) res=a/b;
		int rs=(int)res;
		if(rs==res) this.first=Integer.toString(rs);
		else this.first= Double.toString(res);
		this.display.setText(this.first);
		InsertDb(this.history.getText().toString(), this.first);
		//this.WriteFile(this.history.getText().toString(),this.first);
	}
	
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1) {
			if(resultCode==RESULT_OK) {
				Log.d("OnActivity Result",data.getStringExtra("result"));
				this.first=data.getStringExtra("result");
				this.display.setText(this.first);
				InsertDb(this.history.getText().toString(), this.first);
			}
			
		}
	}*/

	public void onClear(View arg0) {
		if(this.media.isPlaying()) this.media.stop();
		this.media.start();
		Log.d("DebugLog","Inside onClear");
		this.display.setText("0");
	}
	
	public void onReset(View arg0) {
		if(this.media.isPlaying()) this.media.stop();
		this.media.start();
		Log.d("DebugLog","Inside onReset by AC");
		onReset();
	}
	
	public void onReset() {
		Log.d("DebugLog","Inside onReset");
		this.resultmode=false;
		this.first="";
		this.second="";
		this.clear=false;
		this.op="";
		this.history.setText("");
		this.display.setText("0");
	}

	public void onMemStore(View arg0) {
		if(this.media.isPlaying()) this.media.stop();
		this.media.start();
		Button btn=(Button) arg0;
		String s;
		Log.d("DebugLog",this.first.length()+"");
		if(this.first.length()==0) s=this.display.getText().toString();
		else if(this.resultmode) s=this.display.getText().toString();
		else {
			this.second=this.display.getText().toString();
			this.history.setText(this.history.getText()+" "+this.second);
			this.performCalculation();
			this.resultmode=true;
			this.display.setText(this.first);
			s=this.first;
			
		}
		String currValue=this.getMemValue();
		double a=Double.parseDouble(currValue);
		double b=Double.parseDouble(s);
		int aa=(int)a,bb=(int)b;
		if(aa==a && bb==b) {
			if(btn.getText().equals("M+")) this.setMemValue(String.valueOf(aa+bb));
			else this.setMemValue(String.valueOf(aa-bb));
		}
		else {
			if(btn.getText().equals("M+")) this.setMemValue(String.valueOf(a+b));
			else this.setMemValue(String.valueOf(a-b));
		}
		Toast.makeText(this, "Value Stored", Toast.LENGTH_SHORT).show();
	}
	
	public void onMemDisplay(View arg0) {
		if(this.media.isPlaying()) this.media.stop();
		this.media.start();
		this.display.setText(this.getMemValue());
		Toast.makeText(this, "Current Value in M is "+this.getMemValue(), Toast.LENGTH_SHORT).show();
	}
	
	public void onMemClear(View arg0) {
		this.setMemValue("0");
		this.history.setText("");
		this.display.setText("0");
		Toast.makeText(this, "Value Cleared", Toast.LENGTH_SHORT).show();
	}
	
	public void setMemValue(String s) {
		SharedPreferences settings=
				getSharedPreferences(CalculatorActivity.PREFERENCES_FILE_NAME,0);
		SharedPreferences.Editor editor=settings.edit();
		editor.putString("mem",s);
		editor.commit();
	}
	
	public String getMemValue() {
		SharedPreferences settings=
				getSharedPreferences(CalculatorActivity.PREFERENCES_FILE_NAME,0);
		String t=settings.getString("mem", "0");
		if(t.length()==0) t="0";
		return t;
	}
	
	public void onHistory(View arg0) {
		Log.d("DebugLog","Inside OnHistory");
		Intent i=new Intent(getApplicationContext(),HistoryActivity.class);
		startActivity(i);
	}
	
	public void InsertDb(String data,String data2) {
		SimpleDateFormat df=new SimpleDateFormat("dd-MMM-yyyy hh:mm");
		String date=df.format(new Date());
		db.addHistory(new History(date,data,data2));
	}
	
	public void WriteFile(String data,String data2) {
		FileOutputStream fs;
		SimpleDateFormat df=new SimpleDateFormat("dd-MMM-yyyy hh:mm");
		String date=df.format(new Date());
		//String LB="\r\n";
		try{
			fs=openFileOutput(FILE_NAME, MODE_PRIVATE|MODE_APPEND);
			fs.write(date.getBytes());
			fs.write(System.getProperty("line.separator").getBytes());
			fs.write(data.getBytes());
			fs.write(System.getProperty("line.separator").getBytes());
			fs.write(data2.getBytes());
			fs.write(System.getProperty("line.separator").getBytes());
			fs.flush();
			fs.close();
		}
		catch(Exception e) {
			Log.d("ExceptionLog",e.getMessage());
		}
	}
}