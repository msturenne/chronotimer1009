package main;
import java.util.ArrayList;

public class Heat {

	private ArrayList<Competitor> racers;
	private int currentCompetitor;
	
	/**
	 * Constructor
	 */
	public Heat(){
		racers = new ArrayList<Competitor>();
		currentCompetitor = 0;
	}
	/**
	 * Set selected racer as next on to start
	 * @param racer the racer to start next
	 */
	public void setNextCompetitor(Competitor x){
		int pos = racers.indexOf(x);
		if(pos == -1 || pos<currentCompetitor) throw new IllegalArgumentException("Competitor not in the race! Please add first");
		for(int i = pos; i>currentCompetitor; --i){
			racers.set(i, racers.get(i-1));
		}
		racers.set(currentCompetitor, x);
		
	}
	/**
	 * Take the selected runner (the next runner) out from the race
	 * @param racer the runner to be cleared
	 */
	public void clearNextCompetitor() throws UserErrorException {
		if(racers.size()-(currentCompetitor)<1) throw new UserErrorException("No runners to clear!");
		for(int i = currentCompetitor+1; i<racers.size(); ++i){
			racers.set(i-1, racers.get(i));
		}
		racers.remove(racers.size()-1);
	}
	/**
	 * basically a remove method
	 * @param x
	 */
	public void remove(Competitor x){
		int pos = racers.indexOf(x);
		if(pos < 0) throw new IllegalArgumentException("runner does not exists");
		racers.remove(pos);
	}
	/**
	 * Swaps two runners positions in line
	 */
	public void swap() throws UserErrorException{
		int count = 0;
		for(Competitor x : racers){
			if(x.getStartTime() == null) ++count;
		}
		if(count > 1 && currentCompetitor + 1 <= racers.size()){
			Competitor first = racers.get(currentCompetitor);
			Competitor second = racers.get(currentCompetitor+1);
			racers.set(currentCompetitor, second);
			racers.set(currentCompetitor+1, first);
		}
		else{
			throw new UserErrorException("Not enough competitors to swap");
		}
	}
	/**
	 * Add a competitor to the end of the current line of competitors if any
	 * @param x the competitor to add
	 */
	public boolean addCompetitor(Competitor x) throws UserErrorException{
		if(x.getIdNum() < 0 || x.getIdNum() > 99999) throw new UserErrorException("ID number out of range");
		if(x.getRunNum() < 0) throw new IllegalArgumentException("Run Num Out of range");
		boolean add = true;
		for(Competitor i : racers){
			if(i.getIdNum() == x.getIdNum()){
				add = false;
				break;
			}
		}
		if(add){
			racers.add(x);
		}
		return add;
	}
	/**
	 * Retrieve the next competitor if there is one
	 * @return the next competitor
	 */
	public Competitor getNextCompetitor() throws UserErrorException{
		if(!hasNextCompetitor()) throw new UserErrorException("There are no more competitors!");
		while(racers.get(currentCompetitor).isCompeting()){++currentCompetitor;}
		return racers.get(currentCompetitor++);
	}
	/**
	 * used to fix the order of the queue after cancel is called
	 */
	public void fix(EventType x){
		switch(x){
		case IND: 
			--currentCompetitor;
		break;
			
		case GRP: case PARGRP: case PARIND:
			for(int i = 0; i<racers.size(); ++i){
				if(racers.get(i).getStartTime() == null){
					currentCompetitor = i;
					break;
				}
			}
		break;
		}
	}
	/**
	 * Is there another competitor to go?
	 * @return whether or not there is another competitor to go.
	 */
	public boolean hasNextCompetitor(){
		return currentCompetitor < racers.size();
	}
	/**
	 * Return a 1D array view of the competitors
	 * @return
	 */
	public ArrayList<Competitor> getRacers(){
		return this.racers;
	}
}
