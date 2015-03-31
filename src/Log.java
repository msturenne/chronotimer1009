public class Log {

	private Time startTime;
	private int competitorNumber;
	private EventType eventType;
	private Time elapsedTime;
	private Time endTime;
	private int runNum;
	
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
		this.startTime = startTime;
		this.competitorNumber = competitorNumber;
		this.eventType = eventType;
		this.elapsedTime = elapsedTime;
		this.endTime = endTime;
		this.runNum = runNum;
	}
	/**
	 * toString
	 */
	public String toString(){
		Time x = new Time(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
		return "HEAT: " + (runNum+1) + ", ID: " + competitorNumber + ", START: " + startTime.toString() + ", " 
				+ "EVENT: " + eventType.toString() + ", " + "RESULT: " + (elapsedTime.equals(x) ? "DNF" : elapsedTime.toString());
	}
	/**
	 * @return elapsedTime
	 */
	public Time getElapsedTimer(){
		return elapsedTime;
	}
	/**
	 * @return getCompetitorNumber
	 */
	public int getCompetitorNumber(){
		return competitorNumber;
	}
}

