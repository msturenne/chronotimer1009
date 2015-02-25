import java.io.File;
import java.util.ArrayList;
import java.util.Queue;

public class Event {

	private Channel[] channels;
	private ArrayList<Heat> runs;
	private String[] log;
	private EventType type;
	private Time lastTrigger;
	private Competitor currentCompetitor;
	private Printer p;
	
	/**
	 * Constructor
	 */
	public Event(EventType type){
		//initialize
		runs = new ArrayList<Heat>();
		this.type = type;
		
	}
	
	/**
	 * Input new competitor into the system
	 */
	public void createRun(){
		throw new UnsupportedOperationException("createRun() not yet implemented and should not be called");
	}
	
	/**
	 * This method will end a current run.
	 * Disables all channels used.
	 * Computes final times.
	 */
	public void endRun(){
		for(int i = 0; i<channels.length; i++)
		{
			channels[i].triggerChannel();
		}
	}
	
	public ArrayList<Heat> getHeats(){
		return runs;
	}
	
	/**
	 * This method returns the channel that the GUI/driver wants since they can't see the variables
	 * @param channel the channel needed
	 * @return
	 */
	public Channel getChannel(int channel){
		//additional code needed
		return channels[channel-1];
	}
	/**
	 * Getter method for lastTrigger variable
	 * @return the last trigger event
	 */
	public Time getLastTrigger(){
		lastTrigger = currentCompetitor.getEndTime();
		
		return lastTrigger;
	}
	
	/**
	 * Export run in XML to file
	 * @return
	 */
	public File exportLogEvent(){
		return null;
	}
	/**
	 * Triggers the first channel
	 * @return the time at which the channel is triggered.
	 */
	public void start(){
		//check to see if this method can even be called.
		if(getChannel(1).getState() != true && getChannel(2).getState() != true) throw new IllegalStateException("The "
				+ "start and finish channel must be enabled prior to run start!");
		//trigger the start channel and record time in the competitors appropriate attribute
		currentCompetitor.setStartTime(getChannel(1).triggerChannel());
	}
	/**
	 * Triggers the last channel
	 * @return the time at which the channel is triggered.
	 */
	public Time finish(){
		return getChannel(2).triggerChannel();
	}
	
	/**
	 * The Display Class
	 */
	public class Display{
		private String display;
		/**
		 * Constructor
		 */
		public Display(){
			display = "";
		}
		
		/**
		 * toString method
		 */
		public String toString(){
			if(type == EventType.IND) display = "Independent";
			if(type == EventType.GRP) display = "Group";
			if(type == EventType.PARGRP) display = "Parallel Group";
			if(type == EventType.PARIND) display = "Parallel Individual";
			Heat heat1 = runs.get(1);
			for(Competitor x: heat1.getRacers())
			{
				display += "\n" + x.toString();
			}
			return display;
		}
	}
	
	/**
	 * The Printer Class
	 */
	public class Printer{
		
		private boolean state;
		
		/**
		 * Constructor
		 */
		public Printer(){
			state = false;
		}
		
		/**
		 * Is the printer on?
		 * @return weather the printer is on.
		 */
		public boolean isOn(){
			return state;
		}
		
		/**
		 * Toggles the state of the printer
		 */
		public void toggleState(){
			if(state == false) state = true;
			else state = false;
		}
		
		/**
		 * Print (if on) various fields about runner
		 * @return
		 */
		public File print(){
			
		}
	}
}
