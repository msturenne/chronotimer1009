public class Competitor {

	private int idNum;
	private Time entryTime;
	private Time startTime;
	private Time endTime;
	
	public Competitor(int runNum, int idNum, int entryTime){
		//initialize
	}
	/**
	 * @return the idNum
	 */
	public int getIdNum() {
		return idNum;
	}
	/**
	 * @return the entryTime
	 */
	public Time getEntryTime() {
		return entryTime;
	}
	/**
	 * @return the startTime
	 */
	public Time getStartTime() {
		return startTime;
	}
	/**
	 * @return the endTime
	 */
	public Time getEndTime() {
		return endTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
}
