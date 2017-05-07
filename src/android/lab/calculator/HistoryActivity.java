package android.lab.calculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HistoryActivity extends Activity {
	public static final String FILE_NAME="storage.dat";
	private TableLayout table;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("DebugLog","Inside onCreate of History");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_main);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("DebugLog","Inside onResume of History");
		//textView=(TextView)findViewById(R.id.textHistory);
		//this.ReadFile();
		this.GetDb();
		
	}
	
	public void onBackClick(View arg0) {
		Log.d("DebugLog","Inside OnBackClick");
		Intent i=new Intent(getApplicationContext(),CalculatorActivity.class);
		startActivity(i);
	}
	
	public void ReadFile() {
		table=(TableLayout)findViewById(R.id.historyTable);
		try{
			int count=0;
			TableRow tr = null;
			TextView tv = null;
			String tText="";
			BufferedReader inputReader=new BufferedReader(
					new InputStreamReader(openFileInput(CalculatorActivity.FILE_NAME)));
			String input;
			while((input=inputReader.readLine())!=null) {
				Log.d("DebugLog",input);
				if(count==0) {
					tText="";
					tr=new TableRow(this);
					tr.setLayoutParams(new TableRow.LayoutParams(
							TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT));
					tr.setGravity(android.view.Gravity.CENTER);
					tv=new TextView(this);
					tv.setLayoutParams(new TableRow.LayoutParams(
							TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT));
					tv.setClickable(true);
					tv.setOnClickListener(new View.OnClickListener() {
						public void onClick(View arg0) {
							onViewClick(arg0);	
						}
					});
				}
				tText=tText+input+"\n";
				count++;
				if(count==3) {
					count=0;
					tv.setText(tText);
					tr.addView(tv);
					table.addView(tr,new TableLayout.LayoutParams(
							TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
				}
				//this.textView.setText(this.textView.getText()+input+"\n");
			}
		}
		catch(Exception e) {
			Log.d("ExceptionLog",e.getMessage());
		}
	}
	
	public void GetDb() {
		List<History> allHistory=CalculatorActivity.db.getAllHistory();
		table=(TableLayout)findViewById(R.id.historyTable);
		for(History his:allHistory) {
			assignRow(his);
		}
	}
	
	public void assignRow(History history) {
		//table=(TableLayout)findViewById(R.id.historyTable);
		TableRow tr;
		TextView tv;
		String tText=""+history.getTime()+"\n"+history.getEquation()+"\n"
				+history.getResult();
		tr=new TableRow(this);
		tr.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT));
		tr.setGravity(android.view.Gravity.CENTER);
		tv=new TextView(this);
		tv.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT));
		tv.setClickable(true);
		tv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				onViewClick(arg0);	
			}
		});
		tv.setText(tText);
		tr.addView(tv);
		table.addView(tr,new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
	}
	
	public void onViewClick(View arg0) {
		TextView view=(TextView) arg0;
		Log.d("DebugLog","Inside OnViewClick");
		Intent i=new Intent(getApplicationContext(),CalculatorActivity.class);
		String text[]=view.getText().toString().split("\\n");
		i.putExtra("history", text[1]);
		i.putExtra("result", text[2]);
		
		startActivity(i);
	}
}