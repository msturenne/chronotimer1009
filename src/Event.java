import java.io.File;

public class Event {

	private Channel[] channels;
	private Competitor[] competitors;
	private String[] log;
	private String type; //enum class??
	private String lastTrigger; //type change??
	private int currentCompetitor;
	
	/**
	 * Constructor
	 */
	public Event(){
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
	 * Swaps two runners positions in line
	 */
	public void swap(){
		
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
