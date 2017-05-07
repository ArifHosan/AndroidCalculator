package android.lab.calculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CalculationActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras=getIntent().getExtras();
		this.Perform(extras);
	}
	
	public void Perform(Bundle extras) {
		Intent intent=new Intent();
		if(extras==null) setResult(RESULT_CANCELED,intent);
		else {
			String first,second,op;
			first=extras.getString("first");
			second=extras.getString("second");
			op=extras.getString("op");
			if(first.length()==0 || second.length()==0) 
				setResult(RESULT_CANCELED,intent);
			//Log.d("EntryLog","Inside PerformCalculation of Main Activity");
			else {
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
				if(rs==res) first=Integer.toString(rs);
				else first= Double.toString(res);
				setResult(RESULT_OK,intent);
				intent.putExtra("result", first);
			}
			//this.WriteFile(this.history.getText().toString(),this.first);
		}
		finish();
	}
	
}
