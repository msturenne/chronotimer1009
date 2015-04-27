package main;
import java.util.ArrayList;

public abstract class Event extends Display{
	
	private ArrayList<Heat> heats;
	private int curHeat; //private means only this class can modify, not the subclasses
	private Competitor curComp;
	private String name;
	
	public Event(String name) throws UserErrorException{
		this.name = name;
		heats = new ArrayList<Heat>();
		curHeat = -1;
		curComp = null;
		createRun();
	}
	/**
	 * This method will be used by all EventTypes and will not change
	 * regardless of the EventType.
	 * @throws UserErrorException
	 */
	public void createRun() throws UserErrorException{
		heats.add(new Heat()); ++curHeat;
	}
	/**
	 * @return the heats
	 */
	public ArrayList<Heat> getHeats() {
		return heats;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the currentHeat
	 */
	public int getCurHeat() {
		return curHeat;
	}
	/**
	 * @return the curComp
	 */
	public Competitor getCurComp() {
		return curComp;
	}
	/**
	 * @param curComp the curComp to set
	 */
	public void setCurComp(Competitor curComp) {
		this.curComp = curComp;
	}
	/* (non-Javadoc)
	 * @see Display#displayHeatNumber()
	 */
	@Override
	public String displayHeatNumber() {
		// TODO Auto-generated method stub
		return "Heat: " + (curHeat+1);
	}

	/* (non-Javadoc)
	 * @see Display#displayFinished()
	 */
	@Override
	public String displayFinished() {
		String toReturn = "";
		boolean noRunners = true;
		for(Competitor x : getHeats().get(getCurHeat()).getRacers()){
			if(x.getEndTime() != null){
				toReturn += "\n" + x.getIdNum() + " " + x.getEndTime().toString() + " F";
				noRunners = false;
			}
		}
		if(noRunners){toReturn = "no runners have finished";}
		return toReturn;
	}

	public abstract void endRun() throws UserErrorException;
	
	public abstract void trigChan(int chan, boolean dnf) throws UserErrorException;
	
	public abstract void cancel(int ln) throws UserErrorException;
	
	public abstract EventType getType();
}
