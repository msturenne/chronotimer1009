package main;

import com.google.gson.annotations.Expose;

public class Time implements Comparable{
	
	private int hours;
	private int minutes;
	private int seconds;
	private int hundreths;
	private long lastMilliseconds;
	@Expose private String time; //used for JSON
	
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
		time = this.toString().substring(1, this.toString().length()-1);
	}
	/**
	 * @param hours
	 * @param min
	 * @param sec
	 * @param d
	 */
	public Time(int hours, int min, int sec, int d){
		this.hours = hours;
		this.minutes = min;
		this.seconds = sec;
		this.hundreths = d;
		time = this.toString();
	}
	/**
	 * @return lastMilliseconds
	 */
	public long getMillis(){
		return this.lastMilliseconds;
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
		boolean isDNF = false;
		if((this.hours == Integer.MAX_VALUE) && (this.minutes == Integer.MAX_VALUE) && (this.seconds == Integer.MAX_VALUE) && (this.hundreths == Integer.MAX_VALUE)) isDNF = true;
		return isDNF ? "DNF" : "<" + getHours() + ":" + getMinutes() + ":" + getSeconds() + "." + getHundreths() + ">";
	}
	/**
	 * Returns the time in milliseconds
	 * @return
	 */
	public int getTime(){
		return (int)((this.hours * 1000 * 60 * 60)+(this.minutes * 1000 * 60)+(this.seconds * 1000)+(this.hundreths * 10));
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
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Time x = (Time) o;
		int toReturn = 0;
		if(this.getTime() < x.getTime()) toReturn = -1;
		if(this.getTime() > x.getTime()) toReturn = 1;
		return toReturn;
	}
}

