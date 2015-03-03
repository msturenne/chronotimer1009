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
	
	public boolean equals(Time x){
		return ((this.hours == x.hours) && (this.minutes == x.minutes) && (this.seconds == x.seconds) && (this.hundreths == x.hundreths));
	}
	
	/**
	 * Given two time, computed the elapsed time
	 */
	public static Time elapsed(Time b, Time a){
		if(b == null || a == null) throw new IllegalArgumentException("argument can't be null!");
		long elapsed = b.getTime()-a.getTime();
		return new Time(elapsed);
	}
}

