import java.util.ArrayList;
import java.util.Queue;

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
	public void setNextCompetitor(Competitor x){}
	/**
	 * Take the selected runner (the next runner) out from the race
	 * @param racer the runner to be cleared
	 */
	public void clearNextCompetitor(){}
	/**
	 * Swaps two runners positions in line
	 */
	public void swap(){}
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
	public Competitor[] getRacers(){
		return (Competitor[]) racers.toArray();
	}
}
