public class ChronoTimer1009 {

	public static Channel[] channels = new Channel[8];
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
		for(int i=0; i<channels.length; ++i){channels[i] = new Channel(SensorType.NONE);}
	}
	public ChronoTimer1009(Event e){
		this.currentEvent = e;
		ChronoTimer1009.globalTime = null;
		this.state = false;
		for(int i=0; i<channels.length; ++i){channels[i] = new Channel(SensorType.NONE);}
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
				state = true;
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
		reset();
		if(state) state = false;
	}
	/**
	 * Exit the simulator
	 */
	public void exit(){
		this.currentEvent = null;
		ChronoTimer1009.globalTime = null;
		off();
	}
	/**
	 * Resets the system to its initial state
	 */
	public void reset(){
		if(state) state = false;
		on(new Event(EventType.IND, ChronoTimer1009.channels));
	}
	/**
	 * Readies the system for anew event;
	 */
	public void newEvent(EventType x){
		switch(x){
			case IND: this.currentEvent = new Event(EventType.IND, ChronoTimer1009.channels); break;
			case PARIND: this.currentEvent = new Event(EventType.PARIND, ChronoTimer1009.channels); break;
			case GRP: this.currentEvent = new Event(EventType.GRP, ChronoTimer1009.channels); break;
			case PARGRP: this.currentEvent = new Event(EventType.PARGRP, ChronoTimer1009.channels); break;
		}
	}
	/**
	 * @return state
	 */
	public boolean getState(){
		return state;
	}
}