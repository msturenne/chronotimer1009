package main;
public class Competitor {

	private int idNum;
	private int runNum;
	private Time entryTime;
	private Time startTime;
	private Time endTime;
	private boolean isCompeting;
	
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
		this.startTime = null;
		this.endTime = null;
		this.isCompeting = false;
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
	 * @return the isCompeting
	 */
	public boolean isCompeting() {
		return isCompeting;
	}
	/**
	 * @param isCompeting the isCompeting to set
	 */
	public void setCompeting(boolean isCompeting) {
		this.isCompeting = isCompeting;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "heat: " + getRunNum()+1 + ", ID#: " + getIdNum() + ", elapsed: " + getEndTime();
	}
	
}
