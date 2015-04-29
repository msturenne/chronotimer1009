package main;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;


public abstract class SingleLaneEvent extends Event {

	private Queue<Competitor> unfinished; //used for pending competitors.  for the purpose of multiple competitors using a 'track' at one time.
	
	public SingleLaneEvent(){
		unfinished = new LinkedList<Competitor>();
	}

	@Override
	public void endRun() throws UserErrorException {
		if(getHeats().get(getCurHeat()).getRacers().isEmpty()) throw new UserErrorException("Utilize the current heat!");
		for(Competitor x : getHeats().get(getCurHeat()).getRacers()){
			if(x.getStartTime() == null){
				//if runner hasn't gone yet we need to remove that runner
				getHeats().get(getCurHeat()).remove(x);
			}
		}
		if(!unfinished.isEmpty()){while(!unfinished.isEmpty()){trigChan(2, false);};}
	}

	/**
	 * @return the unfinished
	 */
	public Queue<Competitor> getUnfinished() {
		return unfinished;
	}
	
	/**
	 * @param unfinished the unfinished to set
	 */
	public void setUnfinished(Queue<Competitor> unfinished) {
		this.unfinished = unfinished;
	}

	public void finish(boolean dnf) throws UserErrorException{
		if(this.unfinished.isEmpty()) throw new UserErrorException("There are no competitors competing");
		Time DNF = new Time(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE); //will represent a DNF 
		//takes the first unfinished runner
		Competitor finished = unfinished.remove();
		finished.setCompeting(false);
		//sets the endtime for the completed runner only if they finished.
		finished.setEndTime(dnf ? Time.elapsed(ChronoTimer1009System.getChan(2).triggerChannel(), finished.getStartTime()) : DNF);
		//tells the printer to print if on
		if(ChronoTimer1009System.getPrinter().isOn()) ChronoTimer1009System.getPrinter().print(finished.toString());
		ChronoTimer1009System.export();
	}
	
	public void start() throws UserErrorException{
		//check to see if the channels are enabled
		if(ChronoTimer1009System.getChan(1).getState() != true || ChronoTimer1009System.getChan(2).getState() != true) throw new UserErrorException("The "
				+ "start and finish channel must be enabled prior to run start!");
		//setCurrentCompetitor
		setCurComp(getHeats().get(getCurHeat()).getNextCompetitor());
		//trigger the start channel and record time in the competitors appropriate attribute
		getCurComp().setStartTime(ChronoTimer1009System.getChan(1).triggerChannel());
		getCurComp().setCompeting(true);
		unfinished.add(getCurComp());
	}

	@Override
	public String displayInQueue(){
		String toReturn = "";
		ArrayList<Competitor> z = getHeats().get(getCurHeat()).getRacers();
		boolean isComp = false;
		for(Competitor b : z){
			if(b.getStartTime() == null){
				isComp = true;
			}
		}
		if(!isComp) toReturn = "No competitors in queue";
		else{
			toReturn = "";
			for(Competitor i : z){
				boolean a = true;
				for(Competitor j : unfinished){
					if(j.getIdNum() == i.getIdNum()){a = false;}
				}
				if(a && i.getEndTime() == null){toReturn += "\n" + i.getIdNum() + " " + i.getEntryTime().toString();}
			}
		}
		return toReturn;
	}

	@Override
	public String displayRunning() {
		// TODO Auto-generated method stub
		String toReturn = "";
		if(unfinished.isEmpty()) toReturn = "No competitors competing";
		else{
			toReturn = "";
			for(Competitor x : unfinished){
				Time y = Time.elapsed(ChronoTimer1009System.globalTime, x.getStartTime());
				toReturn += "\n" + x.getIdNum() + " <" + y.getHours() + ":" + y.getMinutes() + ":" + y.getSeconds() + ":" + y.getHundreths() + "> R";
			}
		}
		return toReturn;
	}

	@Override
	public abstract void cancel(int ln) throws UserErrorException;

	@Override
	public abstract EventType getType();

	@Override
	public abstract String displayEventType();

	@Override
	public abstract void trigChan(int chan, boolean dnf) throws UserErrorException;
}
