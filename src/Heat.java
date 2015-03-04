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
	public void clearNextCompetitor(){
		if(racers.size() == 0) throw new IllegalStateException("No runners to clear!");
		for(int i = currentCompetitor+1; i<racers.size(); ++i){
			racers.set(i-1, racers.get(i));
		}
		racers.remove(racers.size()-1);
	}
	/**
	 * Swaps two runners positions in line
	 */
	public void swap(){
		if(racers.size() > 1 && currentCompetitor + 1 <= racers.size()){
			Competitor first = racers.get(currentCompetitor);
			Competitor second = racers.get(currentCompetitor+1);
			racers.set(currentCompetitor, second);
			racers.set(currentCompetitor+1, first);
		}
	}
	/**
	 * Add a competitor to the end of the current line of competitors if any
	 * @param x the competitor to add
	 */
	public void addCompetitor(Competitor x){
		racers.add(x);
	}
	/**
	 * Retrieve the next competitor if there is one
	 * @return the next competitor
	 */
	public Competitor getNextCompetitor(){
		if(!hasNextCompetitor()) throw new IllegalStateException("There are no more competitors!");
		return racers.get(currentCompetitor++);
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
		return racers;
	}
}
