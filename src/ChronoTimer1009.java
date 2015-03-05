import java.util.Date;

public class ChronoTimer1009 {

	private Event currentEvent;
	public static Time globalTime;
	private boolean state;
	
	/**
	 * Contructor
	 * @param e current event
	 */
	public ChronoTimer1009(){
		this.currentEvent = null;
		ChronoTimer1009.globalTime = null;
		this.state = false;
	}
	public Event getCurrentEvent() {
		return currentEvent;
	}
	/**
	 * Turns the system on.
	 * Creates an instance of an Event.
	 */
	public void on(Event e){
		if(!state){
			try{
				this.currentEvent = e;
				ChronoTimer1009.globalTime = new Time(0);
			}catch (Exception x){
				Event k = this.currentEvent;
				reset(); //we want to reset the system but retain the current event.
				on(k);
			}
		}
	}
	
	/**
	 * Turns the system off but the event stays active.
	 */
	public void off(){
		if(state) state = false;
	}
	/**
	 * Exit the simulator
	 */
	public void exit(){
		reset(); off();
	}
	/**
	 * Resets the system to its initial state
	 */
	public void reset(){
		this.currentEvent = null;
		ChronoTimer1009.globalTime = null;
	}
	/**
	 * Return the global time
	 * @return global time
	 */
	public static Date getTime(){
		return new Date();
	}
}
