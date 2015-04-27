package main;

import com.google.gson.annotations.Expose;

public class Log {

	@Expose private int compNum;
	@Expose private Time start;
	@Expose private EventType eventType;
	@Expose private int heatNum;
	@Expose private Time elapsed;
	
	/**
	 * Constructor
	 * @param startTime
	 * @param competitorNumber
	 * @param eventType
	 * @param elapsedTime
	 * @param endTime
	 * @param runNum
	 */
	public Log(Time startTime, int competitorNumber, EventType eventType, Time elapsedTime, Time endTime, int runNum) {
		this.start = startTime;
		this.compNum = competitorNumber;
		this.eventType = eventType;
		this.elapsed = elapsedTime;
		this.heatNum = runNum+1;
	}
	/**
	 * toString
	 */
	public String toString(){
		Time x = new Time(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
		return "HEAT: " + (heatNum+1) + ", ID: " + compNum + ", START: " + start.toString() + ", " 
				+ "EVENT: " + eventType.toString() + ", " + "RESULT: " + (elapsed.equals(x) ? "DNF" : elapsed.toString());
	}
	/**
	 * @return elapsedTime
	 */
	public Time getElapsedTime(){
		return elapsed;
	}
	/**
	 * @return getCompetitorNumber
	 */
	public int getCompetitorNumber(){
		return compNum;
	}
	/**
	 * @return the eventType
	 */
	public EventType getEventType() {
		return eventType;
	}
}

