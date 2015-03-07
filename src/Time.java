public class Time {
	
	int hours;
	int minutes;
	int seconds;
	double hundreths;
	long lastMilliseconds;
	
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
		this.lastMilliseconds = System.currentTimeMillis();
	}
	/**
	 * For testing purposes
	 * @param hours
	 * @param min
	 * @param sec
	 * @param d
	 */
	public Time(int hours, int min, int sec, double d){
		this.hours = hours;
		this.minutes = min;
		this.seconds = sec;
		this.hundreths = d;
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
	public double getHundreths() {
		return hundreths;
	}
	
	/**
	 * toString()
	 */
	public String toString(){
		return "<" + getHours() + ":" + getMinutes() + ":" + getSeconds() + "." + getHundreths() + ">";
	}
	
	/**
	 * Returns the time in milliseconds
	 * @return
	 */
	public int getTime(){
		return (int)((this.hours * 1000 * 60 * 60)+(this.minutes * 1000 * 60)+(this.seconds * 1000)+(this.hundreths * 10));
	}
	/**
	 * set the time
	 * @param milliseconds
	 */
	public void setTime(long milliseconds){
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
	public int updateTime() {
		this.setTime(this.getTime() + System.currentTimeMillis() - this.lastMilliseconds);
		this.lastMilliseconds = System.currentTimeMillis();
		return this.getTime();
	}
}

