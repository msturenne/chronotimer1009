public class Competitor {

	private int idNum;
	private String entryTime;
	private String startTime;
	private String endTime;
	
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
	public String getEntryTime() {
		return entryTime;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
