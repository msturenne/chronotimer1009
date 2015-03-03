public class Competitor {

	private int idNum;
	private int runNum;
	private Time entryTime;
	private Time startTime;
	private Time endTime;
	
	/**
	 * Constructor
	 * @param runNum
	 * @param idNum
	 * @param entryTime
	 */
	public Competitor(int runNum, int idNum, Time entryTime){
		//initialize
		this.runNum = runNum;
		this.idNum = idNum;
		this.entryTime = entryTime;
		startTime = null;
		endTime = null;
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
	 * @return the runNum
	 */
	public int getRunNum() {
		return runNum;
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
	/**
	 * toString()
	 */
	public String toString(){
		//A version will be displayed depending on whether the competitor is still
		//in run or has finished
		if(this.endTime == null)
			return runNum + "		" + idNum + "		....";
		else
			return runNum + "		" + idNum + "		" + endTime;
	}
}
