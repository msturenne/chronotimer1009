import java.util.Date;

public class ChronoTimer1009 {

	private Event currentEvent;
	
	/**
	 * Contructor
	 * @param e current event
	 */
	public ChronoTimer1009(Event e){
		this.currentEvent = e;
	}
	
	/**
	 * Turns the system on.
	 * Creates an instance of an Event.
	 */
	public void on(){
		
	}
	
	/**
	 * Turns the system off but the event stays active.
	 */
	public void off(){
		
	}
	
	/**
	 * Exit the simulator
	 */
	public void exit(){
		
	}
	
	/**
	 * Resets the system to its initial state
	 */
	public void reset(){
		
	}
	
	/**
	 * Return the global time
	 * @return global time
	 */
	public static Date getTime(){
		return new Date();
	}
}
