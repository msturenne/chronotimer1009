import java.io.File;
import java.util.Queue;

public class Event {

	private Channel[] channels;
	private Heat[] runs;
	private String[] log;
	private EventType type;
	private Time lastTrigger; //type change??
	private Competitor currentCompetitor;
	//private Heat[] runs;
	
	/**
	 * Constructor
	 */
	public Event(EventType type){
		//initialize
	}
	
	/**
	 * Input new competitor into the system
	 */
	public void createRun(){
		
	}
	
	/**
	 * This method will end a current run.
	 * Disables all channels used.
	 * Computes final times.
	 */
	public void endRun(){
		
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
	public String getLastTrigger(){
		return null;
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
	public Time start(){
		return getChannel(1).triggerChannel();
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
	 * @author fbgreco
	 */
	public class Display{
		
		/**
		 * Constructor
		 */
		public Display(){
			
		}
		
		/**
		 * toString method
		 */
		public String toString(){
			return null;
		}
	}
	
	/**
	 * The Printer Class
	 * @author fbgreco
	 */
	public class Printer{
		
		private boolean state;
		
		/**
		 * Constructor
		 */
		public Printer(){
			
		}
		
		/**
		 * Is the printer on?
		 * @return weather the printer is on.
		 */
		public boolean isOn(){
			return true;
		}
		
		/**
		 * Toggles the state of the printer
		 */
		public void toggleState(){
			
		}
		
		/**
		 * Print (if on) various fields about runner
		 * @return
		 */
		public File print(){
			return null;
		}
	}
}
