public class Log {

	private Time startTime;
	private int competitorNumber;
	private EventType eventType;
	private Time elapsedTime;
	
	
	public Log(Time startTime, int competitorNumber, EventType eventType, Time elapsedTime) {
		this.startTime = startTime;
		this.competitorNumber = competitorNumber;
		this.eventType = eventType;
		this.elapsedTime = elapsedTime;
	}
	/**
	 * @return the timestamp
	 */
	public Time getTimestamp() {
		return startTime;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Time timestamp) {
		this.startTime = timestamp;
	}

	/**
	 * @param competitorNumber the competitorNumber to set
	 */
	public void setCompetitorNumber(int competitorNumber) {
		this.competitorNumber = competitorNumber;
	}

	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	/**
	 * @param elapsedTime the elapsedTime to set
	 */
	public void setElapsedTime(Time elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	/**
	 * 
	 */
	public String toString(){
		return "<" + startTime.toString() + ">" + " <" + eventType.toString() + ">" + 
	" <" + competitorNumber + ">" + " <" + elapsedTime.toString() + ">";
	}
}

