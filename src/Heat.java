import java.util.Queue;


public class Heat {

	private Queue<Competitor> racers;
	
	public Heat(Queue<Competitor> racers){
		
	}
	/**
	 * @return the racers
	 */
	public Competitor[] getRacers() {
		return (Competitor[])racers.toArray();
	}
	/**
	 * Set selected racer as next on to start
	 * @param racer the racer to start next
	 */
	public void setNextCompetitor(int racer){
		
	}
	
	/**
	 * Take the selected runner (the next runner) out from the race
	 * @param racer the runner to be cleared
	 */
	public void clearNextCompetitor(int racer){
		
	}
	/**
	 * Swaps two runners positions in line
	 */
	public void swap(){
		
	}
	
	public void addCompetitor(Competitor x){
		racers.add(x);
	}
	/**
	 * @return the current competitor
	 */
	public Competitor getCurrentRacer(){
		return null;
	}
}
