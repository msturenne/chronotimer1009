import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.io.*;

public class Event {

	private Channel[] channels; //this 1D array will hold the 8 channels that will be used during a race. Even indexes imply start channels and odd indexes imply finish channels.
	private ArrayList<Heat> runs; //hold Heat objects.  ArrayList is chosen as Heats will be dynamically added.
	private Stack<Log> log; //hold important data concerning the event.
	private EventType type; //specifies the type of event from the given ENUM choices
	private Competitor currentCompetitor; //reference to the current competitor
	private Queue<Competitor> unfinished; //used for pending competitors.  for the purpose of multiple competitors using a 'track' at one time.
	private Printer p; //the printer to be used for this event.
	private int currentHeat; //the current heat.  NOTE: REDUNDANT FOR RELEASE 1.0
	
	/**
	 * Constructor
	 */
	public Event(EventType type){
		if(type == null) throw new IllegalArgumentException();
		//initialize
		channels = new Channel[8];
		for(int i=0; i<channels.length; ++i){channels[i] = new Channel(SensorType.NONE);} //creates 8 channels and disables them.
		runs = new ArrayList<Heat>();
		log = new Stack<Log>();
		//lastTrigger = null;
		currentCompetitor = null;
		unfinished = new LinkedList<Competitor>();
		p = new Printer();
		currentHeat = -1;
		this.type = type;
		createRun(); //when an event is created, it creates a heat under the assumption that an event will have at least on heat.
	}
	/**
	 * Input new heat into the system
	 */
	public void createRun(){
		runs.add(new Heat());
		++currentHeat;
	}
	/**
	 * 
	 */
	public void endRun(){
		for(int i = 0; i<channels.length; i++){if((channels[i]).getState() == true) channels[i].toggleState();}
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<Heat> getHeats(){
		return runs;
	}
	/**
	 * This method returns the channel that the GUI/driver wants since they can't see the variables
	 * @param channel the channel needed
	 * @return
	 */
	public Channel getChannel(int channel){
		if(channel < 1 || channel > 8) throw new IllegalArgumentException("Argument is not in range");
		return channels[channel-1];
	}
	/**
	 * Triggers the first channel
	 * @return the time at which the channel is triggered.
	 */
	public void start(){
		//check to see if this method can even be called.
		if(getChannel(1).getState() != true || getChannel(2).getState() != true) throw new IllegalStateException("The "
				+ "start and finish channel must be enabled prior to run start!");
		//setCurrentCompetitor
		currentCompetitor = runs.get(currentHeat).getNextCompetitor();
		//trigger the start channel and record time in the competitors appropriate attribute
		currentCompetitor.setStartTime(getChannel(1).triggerChannel());
		unfinished.add(currentCompetitor);
	}
	/**
	 * Triggers the last channel
	 * @param x true if completed run, false if DNF
	 * @return the time at which the channel is triggered.
	 */
	public void finish(boolean x){
		Time DNF = new Time(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE); //will represent a DNF 
		//takes the first unfinished runner
		Competitor finished = unfinished.remove();
		//sets the endtime for the completed runner only if they finished.
		finished.setEndTime(x ? getChannel(2).triggerChannel() : DNF);
		//computes the elapsed time
		Time elapsed = DNF;
		if(x){elapsed = Time.elapsed(finished.getEndTime(), finished.getStartTime());}
		//adds to the log
		log.add(new Log(finished.getStartTime(), finished.getIdNum(), this.type, elapsed, finished.getEndTime(), finished.getRunNum()));
		//tells the printer to print if on
		if(p.isOn()) p.print();
	}
	/**
	 * @return the log
	 */
	public Stack<Log> getLog() {
		return log;
	}
	/**
	 * @return the currentHeat
	 */
	public int getCurrentHeat() {
		return currentHeat;
	}
	/**
	 * return the printer
	 * @return
	 */
	public Printer getPrinter(){
		return p;
	}
	/**
	 * @return the type
	 */
	public EventType getType() {
		return type;
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
			if(type == EventType.IND) display = "Event: Independent\n";
			if(type == EventType.GRP) display = "Event: Group\n";
			if(type == EventType.PARGRP) display = "Event: Parallel Group\n";
			if(type == EventType.PARIND) display = "Event: Parallel Individual\n";
			display+= "Run		Bib		Time\n";
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
		private String outFileName;
		/**
		 * Erase the content of the file
		 */
		public Printer(){
			state = false;
			outFileName = "printerOutFile";
			try{
				PrintWriter fileOut = new PrintWriter (outFileName);
				fileOut.print("");
				fileOut.close();
			}catch(IOException e){
				System.out.println("Could not open file! " + e.getMessage() +" (No such file or directory)");
			}
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
		 * print the information on the top of the Log stack;
		 */
		public void print(){
			try{
				PrintWriter fileOut = new PrintWriter (new FileWriter(outFileName, true));
				Log auxLog = log.peek();
				fileOut.println(auxLog.toString());
				fileOut.close();
			}catch(IOException e){
				System.out.println("Could not open file! " + e.getMessage() +" (No such file or directory)");
			}
		}
	}
}
