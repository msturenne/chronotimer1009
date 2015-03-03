import java.util.Date;

public class ChronoTimer1009 {

	private Event currentEvent;
	public static Time globalTime;
	
	/**
	 * Contructor
	 * @param e current event
	 */
	public ChronoTimer1009(Event e){
		this.currentEvent = e;
		this.globalTime = new Time(0);
	}
	/**
	 * For testing purposes
	 */
	public ChronoTimer1009(EventType e, Time t){
		this.currentEvent = new Event(e);
		this.globalTime = t;
	}
	public Event getCurrentEvent() {
		return currentEvent;
	}
	/**
	 * Turns the system on.
	 * Creates an instance of an Event.
	 */
	public void on(){
		throw new UnsupportedOperationException("on() not yet implemented and should not be called");
	}
	
	/**
	 * Turns the system off but the event stays active.
	 */
	public void off(){
		throw new UnsupportedOperationException("off() not yet implemented and should not be called");
	}
	
	/**
	 * Exit the simulator
	 */
	public void exit(){
		throw new UnsupportedOperationException("exit() not yet implemented and should not be called");
	}
	
	/**
	 * Resets the system to its initial state
	 */
	public void reset(){
		throw new UnsupportedOperationException("reset() not yet implemented and should not be called");
	}
	
	/**
	 * Return the global time
	 * @return global time
	 */
	public static Date getTime(){
		return new Date();
	}
}
