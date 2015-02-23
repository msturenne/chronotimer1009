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
}
