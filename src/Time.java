public class Time {
	
	int hours;
	int minutes;
	int seconds;
	int hundreths;
	
	/**
	 * Constructor
	 * @param milliseconds
	 */
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
	
	/**
	 * toString()
	 */
	public String toString(){
		return "<" + getMinutes() + ":" + getSeconds() + "." + getHundreths() + ">";
	}
	
	/**
	 * Returns the time in milleseconds
	 * @return
	 */
	public int getTime(){
		return (int)((this.hours * 1000 * 60 * 60)+(this.minutes * 1000 * 60)+(this.seconds * 1000)+(this.hundreths * 10));
	}
	/**
	 * set the time
	 * @param milliseconds
	 */
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
	 * Checks equality of variables
	 * @param x
	 * @return
	 */
	public boolean equals(Time x){
		return ((this.hours == x.hours) && (this.minutes == x.minutes) && (this.seconds == x.seconds) && (this.hundreths == x.hundreths));
	}
	/**
	 * Given two time, computed the elapsed time
	 */
	public static Time elapsed(Time b, Time a){
		if(b == null || a == null) throw new IllegalArgumentException("argument can't be null!");
		return new Time(b.getTime()-a.getTime());
	}
}

