package android.lab.calculator;

public class History {
	private int _id;
	private String _time;
	private String _equations;
	private String _result;
	
	public History() {
		_id=0;
		_time="";
		_equations="";
		_result="";
	}
	public History(String time,String equations,String result) {
		_time=time;
		_equations=equations;
		_result=result;
	}
	
	public String getTime() {
		return _time;
	}
	
	public String getEquation() {
		return _equations;
	}
	
	public String getResult() {
		return _result;
	}
	
	public void setTime(String time) {
		_time=time;
	}
	
	public void setEquation(String equation) {
		_equations=equation;
	}
	
	public void setResult(String result) {
		_result=result;
	}
	public void setID(int parseInt) {
		_id=parseInt;
		
	}
}
