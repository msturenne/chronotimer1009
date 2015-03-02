public class Time {
	
	int hours;
	int minutes;
	int seconds;
	int hundreths;
	
	public Time(long milliseconds){
		//compute hours
		this.hours = (int)((milliseconds/(1000*60*60)) % 24);
		//compute minutes
		this.minutes = (int)((milliseconds/(1000*60)) % 60);
		//compute seconds
		this.seconds = (int)((milliseconds/(1000)) % 60);
		//compute hundreths
		this.hundreths = (int)((milliseconds/10) % 100);
	}
	
	/**
	 * For testing purposes
	 * @param hours
	 * @param min
	 * @param sec
	 * @param hun
	 */
	public Time(int hours, int min, int sec, int hun){
		this.hours = hours;
		this.minutes = min;
		this.seconds = sec;
		this.hundreths = hun;
	}

	/**
	 * @return the hours
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * @return the minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * @return the seconds
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * @return the hundreths
	 */
	public int getHundreths() {
		return hundreths;
	}
	
	public String toString(){
		return "<" + getMinutes() + ":" + getSeconds() + "." + getHundreths() + ">";
	}
	
	public int getTime(){
		int t = 0;
		t += (int) this.hours * 1000 * 60 * 60;
		t += (int) this.minutes * 1000 * 60;
		t += (int) this.seconds * 1000;
		t += (int) this.hundreths * 10;
		
		return t;

	}
	
	public void setTime(int milliseconds){
		//compute hours
		this.hours = (int)((milliseconds/(1000*60*60)) % 24);
		//compute minutes
		this.minutes = (int)((milliseconds/(1000*60)) % 60);
		//compute seconds
		this.seconds = (int)((milliseconds/(1000)) % 60);
		//compute hundreths
		this.hundreths = (int)((milliseconds/10) % 100);
	}
	/**
	 * Given two time, computed the elapsed time
	 */
	public static Time elapsed(Time b, Time a){
		if(b == null || a == null) throw new IllegalArgumentException("argument can't be null!");
		long bToMillis = (b.getHours() * 3600000) + (b.getMinutes() * 60000) + (b.getSeconds() * 1000) * (b.getHundreths() * 10);
		long aToMillis = (a.getHours() * 3600000) + (a.getMinutes() * 60000) + (a.getSeconds() * 1000) * (a.getHundreths() * 10);
		long elapsed = bToMillis-aToMillis;
		//compute hours
		int h = (int)((elapsed/(1000*60*60)) % 24);
		//compute minutes
		int m = (int)((elapsed/(1000*60)) % 60);
		//compute seconds
		int s = (int)((elapsed/(1000)) % 60);
		//compute hundreths
		int hun = (int)((elapsed/10) % 100);
		//return time
		return new Time(h, m, s, hun);
	}
}

