
import java.io.File;
import java.io.IOException;
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
	private Display d;
	private String exportFileName;
	private Queue<Competitor> unfinishedLane1;
	private Queue<Competitor> unfinishedLane2;
	private Queue<Competitor> unfinishedLane3;
	private Queue<Competitor> unfinishedLane4;
	private Queue<Competitor> canceledLane1;
	private Queue<Competitor> canceledLane2;
	private Queue<Competitor> canceledLane3;
	private Queue<Competitor> canceledLane4;
	private boolean canCancelLane1;
	private boolean canCancelLane2;
	private boolean canCancelLane3;
	private boolean canCancelLane4;
	
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
		unfinishedLane1 = new LinkedList<Competitor>();
		unfinishedLane2 = new LinkedList<Competitor>();
		unfinishedLane3 = new LinkedList<Competitor>();
		unfinishedLane4 = new LinkedList<Competitor>();
		canceledLane1 = new LinkedList<Competitor>();
		canceledLane2 = new LinkedList<Competitor>();
		canceledLane3 = new LinkedList<Competitor>();
		canceledLane4 = new LinkedList<Competitor>();
		canCancelLane1 = canCancelLane2 = canCancelLane3 = canCancelLane4 = false;
		p = new Printer();
		d = new Display();
		exportFileName = "";
		currentHeat = -1;
		this.type = type;
		try {
			createRun();
		} catch (UserErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //when an event is created, it creates a heat under the assumption that an event will have at least on heat.
	}
	/**
	 * Input new heat into the system
	 */
	public void createRun() throws UserErrorException{
		runs.add(new Heat());
		++currentHeat;
	}
	/**
	 * 
	 */
	public void endRun() throws UserErrorException{
		EventType e = this.type;
		for(Competitor x : runs.get(currentHeat).getRacers()){
			if(x.getStartTime() == null){
				x.setStartTime(new Time(0));
			}
		}
		if(e.equals(EventType.IND) || e.equals(EventType.GRP)){
			if(!unfinished.isEmpty()){while(!unfinished.isEmpty()){finish(false);};}
		}
		else if(e.equals(EventType.PARIND)){
			if(!unfinishedLane1.isEmpty()){while(!unfinishedLane1.isEmpty()){triggerChannel(2, false);};}
			if(!unfinishedLane2.isEmpty()){while(!unfinishedLane2.isEmpty()){triggerChannel(4, false);};}
			if(!unfinishedLane3.isEmpty()){while(!unfinishedLane3.isEmpty()){triggerChannel(6, false);};}
			if(!unfinishedLane4.isEmpty()){while(!unfinishedLane4.isEmpty()){triggerChannel(8, false);};}
		}
		else{ //EventType is PARGRP
			
		}
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
	public void start() throws UserErrorException{
		//check to see if this method can even be called.
		if(getChannel(1).getState() != true || getChannel(2).getState() != true) throw new UserErrorException("The "
				+ "start and finish channel must be enabled prior to run start!");
		if(getChannel(1).getSensor().getType().equals(SensorType.NONE) || getChannel(2).getSensor().getType().equals(SensorType.NONE))throw new 
		UserErrorException("Please connect sensors to all channels used");
		//setCurrentCompetitor
		currentCompetitor = runs.get(currentHeat).getNextCompetitor();
		//trigger the start channel and record time in the competitors appropriate attribute
		currentCompetitor.setStartTime(getChannel(1).triggerChannel());
		currentCompetitor.setCompeting(true);
		unfinished.add(currentCompetitor);
	}
	/**
	 * Triggers the last channel
	 * @param x true if completed run, false if DNF
	 * @return the time at which the channel is triggered.
	 */
	public void finish(boolean x) throws UserErrorException{
		if(this.unfinished.isEmpty()) throw new UserErrorException("There are no competitors competing");
		Time DNF = new Time(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE); //will represent a DNF 
		//takes the first unfinished runner
		Competitor finished = unfinished.remove();
		finished.setCompeting(false);
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
	public void triggerChannel(int channel, boolean dnfCheck) throws UserErrorException{
		switch(channel){
		case 1:
			if(this.type.equals(EventType.IND)){
				try {
					start();
				} catch (UserErrorException e) {
					// TODO Auto-generated catch block
					throw new UserErrorException(e.getMessage());
				}
			}
			else if(this.type.equals(EventType.GRP)){
				if(!this.getHeats().get(currentHeat).hasNextCompetitor()){
					throw new UserErrorException("There are no competitors in the queue");
				}
				while(this.getHeats().get(currentHeat).hasNextCompetitor()){
					start();
				}
			}
			else{ //EventType is PARIND or PARGRP
				if(!getChannel(1).getState() || !getChannel(2).getState()) throw new UserErrorException("please enable the channels to be used");
				if(getChannel(1).getSensor().getType().equals(SensorType.NONE) || getChannel(2).getSensor().getType().equals(SensorType.NONE)){
					throw new UserErrorException("Please enable sensors to be used");
				}
				//setCurrentCompetitor
				currentCompetitor = runs.get(currentHeat).getNextCompetitor();
				//if this is true, throw error if found in other lane;
				if(!currentCompetitor.equals(canceledLane1.peek())){
					if(currentCompetitor.equals(canceledLane2.peek()) ||
							currentCompetitor.equals(canceledLane3.peek()) ||
							currentCompetitor.equals(canceledLane4.peek())){ 
						runs.get(currentHeat).fix(type);
						throw new UserErrorException("you must start this runner in "
								+ "the lane in which it was canelled");
					}
				}
				else{
					canceledLane1.remove();
				}
				//trigger the start channel and record time in the competitors appropriate attribute
				channels[1].setCanTrigger(true);
				canCancelLane1 = true;
				currentCompetitor.setStartTime(getChannel(1).triggerChannel());
				currentCompetitor.setCompeting(true);
				unfinishedLane1.add(currentCompetitor);
			}
		break;
		
		case 2:
			if(this.type.equals(EventType.IND) || this.type.equals(EventType.GRP)){
				try {
					finish(true);
				} catch (UserErrorException e) {
					// TODO Auto-generated catch block
					// do nothing
				}
			}
			else{
				if(channels[1].isCanTrigger()){
					if(this.unfinishedLane1.isEmpty()) throw new UserErrorException("There are no competitors competing in Lane 1");
					Time DNF = new Time(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE); //will represent a DNF 
					//takes the first unfinished runner
					Competitor finished = unfinishedLane1.remove();
					finished.setCompeting(false);
					//sets the endtime for the completed runner only if they finished.
					//computes the elapsed time
					finished.setEndTime(dnfCheck ? getChannel(2).triggerChannel() : DNF);
					//computes the elapsed time
					Time elapsed = DNF;
					if(dnfCheck){elapsed = Time.elapsed(finished.getEndTime(), finished.getStartTime());}
					//adds to the log					//adds to the log
					log.add(new Log(finished.getStartTime(), finished.getIdNum(), this.type, elapsed, finished.getEndTime(), finished.getRunNum()));
					//tells the printer to print if on
					if(p.isOn()) p.print();
				}
				else throw new UserErrorException("You must start this competitor first");
			}
		break;
		
		case 3:
			if(this.type.equals(EventType.PARGRP) || this.type.equals(EventType.PARIND)){
				if(!getChannel(3).getState() || !getChannel(4).getState()) throw new UserErrorException("please enable the channels to be used");
				if(getChannel(3).getSensor().getType().equals(SensorType.NONE) || getChannel(4).getSensor().getType().equals(SensorType.NONE)){
					throw new UserErrorException("Please enable sensors to be used");
				}
				//setCurrentCompetitor
				currentCompetitor = runs.get(currentHeat).getNextCompetitor();
				//trigger the start channel and record time in the competitors appropriate attribute
				if(!currentCompetitor.equals(canceledLane2.peek())){
					if(currentCompetitor.equals(canceledLane1.peek()) ||
							currentCompetitor.equals(canceledLane3.peek()) ||
							currentCompetitor.equals(canceledLane4.peek())){ 
						runs.get(currentHeat).fix(type);
						throw new UserErrorException("you must start this runner in "
								+ "the lane in which it was canelled");
					}
				}
				else{
					canceledLane2.remove();
				}
				channels[3].setCanTrigger(true);
				canCancelLane2 = true;
				currentCompetitor.setStartTime(getChannel(1).triggerChannel());
				currentCompetitor.setCompeting(true);
				unfinishedLane2.add(currentCompetitor);
			}
		break;
		
		case 4:
			if(this.type.equals(EventType.PARGRP) || this.type.equals(EventType.PARIND)){
				if(channels[3].isCanTrigger()){
					if(this.unfinishedLane2.isEmpty()) throw new UserErrorException("There are no competitors competing in Lane 2");
					//takes the first unfinished runner
					Time DNF = new Time(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE); //will represent a DNF 
					Competitor finished = unfinishedLane2.remove();
					finished.setCompeting(false);
					//sets the endtime for the completed runner only if they finished.
					finished.setEndTime(dnfCheck ? getChannel(4).triggerChannel() : DNF);
					//computes the elapsed time
					Time elapsed = DNF;
					if(dnfCheck){elapsed = Time.elapsed(finished.getEndTime(), finished.getStartTime());}
					//adds to the log
					log.add(new Log(finished.getStartTime(), finished.getIdNum(), this.type, elapsed, finished.getEndTime(), finished.getRunNum()));
					//tells the printer to print if on
					if(p.isOn()) p.print();
				}
				else throw new UserErrorException("You must start this competitor first");
			}
		break;
		
		case 5:
			if(this.type.equals(EventType.PARGRP) || this.type.equals(EventType.PARIND)){
				if(!getChannel(5).getState() || !getChannel(6).getState()) throw new UserErrorException("please enable the channels to be used");
				if(getChannel(5).getSensor().getType().equals(SensorType.NONE) || getChannel(6).getSensor().getType().equals(SensorType.NONE)){
					throw new UserErrorException("Please enable sensors to be used");
				}
				//setCurrentCompetitor
				currentCompetitor = runs.get(currentHeat).getNextCompetitor();
				//trigger the start channel and record time in the competitors appropriate attribute
				if(!currentCompetitor.equals(canceledLane3.peek())){
					if(currentCompetitor.equals(canceledLane2.peek()) ||
							currentCompetitor.equals(canceledLane1.peek()) ||
							currentCompetitor.equals(canceledLane4.peek())){ 
						runs.get(currentHeat).fix(type);
						throw new UserErrorException("you must start this runner in "
								+ "the lane in which it was canelled");
					}
				}
				else{
					canceledLane3.remove();
				}
				channels[5].setCanTrigger(true);
				canCancelLane3 = true;
				currentCompetitor.setStartTime(getChannel(1).triggerChannel());
				currentCompetitor.setCompeting(true);
				unfinishedLane3.add(currentCompetitor);
			}
		break;
		
		case 6:
			if(this.type.equals(EventType.PARGRP) || this.type.equals(EventType.PARIND)){
				if(channels[5].isCanTrigger()){
					if(this.unfinishedLane3.isEmpty()) throw new UserErrorException("There are no competitors competing in Lane 3");
					//takes the first unfinished runner
					Time DNF = new Time(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE); //will represent a DNF 
					Competitor finished = unfinishedLane3.remove();
					finished.setCompeting(false);
					//sets the endtime for the completed runner only if they finished.
					finished.setEndTime(dnfCheck ? getChannel(6).triggerChannel() : DNF);
					//computes the elapsed time
					Time elapsed = DNF;
					if(dnfCheck){elapsed = Time.elapsed(finished.getEndTime(), finished.getStartTime());}
					//adds to the log
					log.add(new Log(finished.getStartTime(), finished.getIdNum(), this.type, elapsed, finished.getEndTime(), finished.getRunNum()));
					//tells the printer to print if on
					if(p.isOn()) p.print();
				}
				else throw new UserErrorException("You must start this competitor first");
			}
		break;
		
		case 7:
			if(this.type.equals(EventType.PARGRP) || this.type.equals(EventType.PARIND)){
				if(!getChannel(7).getState() || !getChannel(8).getState()) throw new UserErrorException("please enable the channels to be used");
				if(getChannel(7).getSensor().getType().equals(SensorType.NONE) || getChannel(8).getSensor().getType().equals(SensorType.NONE)){
					throw new UserErrorException("Please enable sensors to be used");
				}
				//setCurrentCompetitor
				currentCompetitor = runs.get(currentHeat).getNextCompetitor();
				//trigger the start channel and record time in the competitors appropriate attribute
				if(!currentCompetitor.equals(canceledLane4.peek())){
					if(currentCompetitor.equals(canceledLane2.peek()) ||
							currentCompetitor.equals(canceledLane3.peek()) ||
							currentCompetitor.equals(canceledLane1.peek())){ 
						runs.get(currentHeat).fix(type);
						throw new UserErrorException("you must start this runner in "
								+ "the lane in which it was canelled");
					}
				}
				else{
					canceledLane4.remove();
				}
				channels[7].setCanTrigger(true);
				canCancelLane4 = true;
				currentCompetitor.setStartTime(getChannel(1).triggerChannel());
				currentCompetitor.setCompeting(true);
				unfinishedLane4.add(currentCompetitor);
			}
		break;
		
		case 8:
			if(this.type.equals(EventType.PARGRP) || this.type.equals(EventType.PARIND)){
				if(channels[7].isCanTrigger()){
					if(this.unfinishedLane4.isEmpty()) throw new UserErrorException("There are no competitors competing in Lane 4");
					//takes the first unfinished runner
					Time DNF = new Time(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE); //will represent a DNF 
					Competitor finished = unfinishedLane4.remove();
					finished.setCompeting(false);
					//sets the endtime for the completed runner only if they finished.
					finished.setEndTime(dnfCheck ? getChannel(8).triggerChannel() : DNF);
					//computes the elapsed time
					Time elapsed = DNF;
					if(dnfCheck){elapsed = Time.elapsed(finished.getEndTime(), finished.getStartTime());}
					//adds to the log
					log.add(new Log(finished.getStartTime(), finished.getIdNum(), this.type, elapsed, finished.getEndTime(), finished.getRunNum()));
					//tells the printer to print if on
					if(p.isOn()) p.print();
				}
				else throw new UserErrorException("You must start this competitor first");
			}
		break;
		
		default:
			throw new IllegalStateException("channel: " + channel + " cannot be triggered in the systems current state!");
		}
	}
	/**
	 * start was not valid. competitor is still in queue to start
	 */
	public void cancel(EventType x, int lane)throws UserErrorException{
		switch(x){
		case IND: 
			Queue<Competitor> unfinishedDummy = new LinkedList<Competitor>();
			if(unfinished.isEmpty()) throw new UserErrorException("no runners to cancel");
			int size = 0;
			while(size < (unfinished.size()-1)){
				unfinishedDummy.add(unfinished.remove());
				++size;
			}
			Competitor j = unfinished.remove();
			j.setStartTime(null);
			j.setCompeting(false);
			this.unfinished = unfinishedDummy;
			runs.get(currentHeat).fix(x);
		break;
		
		case GRP:
			if(unfinished.isEmpty()) throw new UserErrorException("no runners to cancel");
			boolean canRemove = true;
			for(Competitor y : runs.get(currentHeat).getRacers()){
				if(y.getEndTime() != null){
					canRemove = false;
					break;
				}
			}
			while(!unfinished.isEmpty() && canRemove){
				Competitor comp = unfinished.remove();
				comp.setStartTime(null);
				comp.setCompeting(false);
				runs.get(currentHeat).fix(x);
			}
		break;
			
		case PARGRP: 
			while(!unfinishedLane1.isEmpty()){
				Competitor comp = unfinishedLane1.remove();
				comp.setStartTime(null);
				comp.setCompeting(false);
				runs.get(currentHeat).fix(x);
			}
			while(!unfinishedLane2.isEmpty()){
				Competitor comp = unfinishedLane2.remove();
				comp.setStartTime(null);
				comp.setCompeting(false);
				runs.get(currentHeat).fix(x);
			}
			while(!unfinishedLane3.isEmpty()){
				Competitor comp = unfinishedLane3.remove();
				comp.setStartTime(null);
				comp.setCompeting(false);
				runs.get(currentHeat).fix(x);
			}
			while(!unfinishedLane4.isEmpty()){
				Competitor comp = unfinishedLane4.remove();
				comp.setStartTime(null);
				comp.setCompeting(false);
				runs.get(currentHeat).fix(x);
			}
		break;
			
		case PARIND: 
			switch(lane){
			case 1:
				if(canCancelLane1){
					Queue<Competitor> unfinishedDummyLane1 = new LinkedList<Competitor>();
					if(unfinishedLane1.isEmpty()) throw new UserErrorException("no runners to cancel");
					int sizeLane1 = 0;
					while(sizeLane1 < (unfinishedLane1.size()-1)){
						unfinishedDummyLane1.add(unfinishedLane1.remove());
						++sizeLane1;
					}
					Competitor j1 = unfinishedLane1.remove();
					j1.setStartTime(null);
					j1.setCompeting(false);
					//if canceled, add to canceled queue
					canceledLane1.add(j1);
					this.unfinishedLane1 = unfinishedDummyLane1;
					runs.get(currentHeat).fix(x);
					canCancelLane1 = false;
				}
				else throw new UserErrorException("You can only cancel the current competitor");
			break;
			
			case 2: 
				if(canCancelLane2){
					Queue<Competitor> unfinishedDummyLane2 = new LinkedList<Competitor>();
					if(unfinishedLane2.isEmpty()) throw new UserErrorException("no runners to cancel");
					int sizeLane2 = 0;
					while(sizeLane2 < (unfinishedLane2.size()-1)){
						unfinishedDummyLane2.add(unfinishedLane2.remove());
						++sizeLane2;
					}
					Competitor j2 = unfinishedLane2.remove();
					j2.setStartTime(null);
					j2.setCompeting(false);
					//if canceled, add to canceled queue
					canceledLane2.add(j2);
					this.unfinishedLane2 = unfinishedDummyLane2;
					runs.get(currentHeat).fix(x);
					canCancelLane2 = false;
				}
				else throw new UserErrorException("You can only cancel the current competitor");
			break;
				
			case 3:
				if(canCancelLane3){
					Queue<Competitor> unfinishedDummyLane3 = new LinkedList<Competitor>();
					if(unfinishedLane3.isEmpty()) throw new UserErrorException("no runners to cancel");
					int sizeLane3 = 0;
					while(sizeLane3 < (unfinishedLane3.size()-1)){
						unfinishedDummyLane3.add(unfinishedLane3.remove());
						++sizeLane3;
					}
					Competitor j3 = unfinishedLane3.remove();
					j3.setStartTime(null);
					j3.setCompeting(false);
					//if canceled, add to canceled queue
					canceledLane3.add(j3);
					this.unfinishedLane3 = unfinishedDummyLane3;
					runs.get(currentHeat).fix(x);
					canCancelLane3 = false;
				}
				else throw new UserErrorException("You can only cancel the current competitor");
			break;
				
			case 4:
				if(canCancelLane4){
					Queue<Competitor> unfinishedDummyLane4 = new LinkedList<Competitor>();
					if(unfinishedLane4.isEmpty()) throw new UserErrorException("no runners to cancel");
					int sizeLane4 = 0;
					while(sizeLane4 < (unfinishedLane4.size()-1)){
						unfinishedDummyLane4.add(unfinishedLane4.remove());
						++sizeLane4;
					}
					Competitor j4 = unfinishedLane4.remove();
					j4.setStartTime(null);
					j4.setCompeting(false);
					//if canceled, add to canceled queue
					canceledLane4.add(j4);
					this.unfinishedLane4 = unfinishedDummyLane4;
					runs.get(currentHeat).fix(x);
					canCancelLane4 = false;
				}
				else throw new UserErrorException("You can only cancel the current competitor");
			break;
			}
		break;
		}
	}
	/**
	 * Exports the log to a file
	 */
	public void export() throws UserErrorException{
		if(log.isEmpty()) throw new UserErrorException("There is nothing to export");
		exportFileName = "exportFile";
		PrintWriter fileOut;
		try {
			File file = new File(exportFileName);
			if(file.exists()){file.delete(); file = new File(exportFileName);}

			fileOut = new PrintWriter (new FileWriter(file, true));
			Stack<Log> toExport = new Stack<Log>();
			Stack<Log> thisLog = (Stack<Log>) log.clone();
			while(!thisLog.isEmpty()){toExport.push(thisLog.pop());}
			while(!toExport.isEmpty()){
				fileOut.println(toExport.pop().toString());
			}
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Time findElapsedInLog(int idNum){
		Time toReturn = null;
		for(Log item : log){
			if(item.getCompetitorNumber() == idNum){
				toReturn = item.getElapsedTimer(); break;
			}
		}
		return toReturn;
	}
	/**
	 * @return the currentCompetitor
	 */
	public Competitor getCurrentCompetitor(){
		return currentCompetitor;
	}
	/**
	 * @return the log
	 */
	public Stack<Log> getLog() {
		return log;
	}
	/**
	 * @param x
	 */
	public void setLog(Stack<Log> x){
		this.log = x;
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
	 * @return the display
	 */
	public Display getDisplay(){
		return d;
	}
	/**
	 * The Display Class
	 */
	public class Display{
		private String systemTime;
		private String running;
		private String finished;
		private String eventType;
		private String inQueue;
		private String printerStatus;
		private String heatNum;
		private int logSize;
		private int queueSize;
		private int pendingSize;
		private int pendingSizeLane1;
		private int pendingSizeLane2;
		private int pendingSizeLane3;
		private int pendingSizeLane4;
		private String noFinished;
		/**
		 * Constructor
		 */
		public Display(){
			running = finished = heatNum = noFinished = printerStatus = systemTime = eventType = inQueue = "";
			logSize = queueSize = pendingSize = 0;
		}
		public String display(){
			ChronoTimer1009.globalTime.updateTime();
			systemTime = "System Time: " + ChronoTimer1009.globalTime.toString();
			
			heatNum = "\nHeat: " + (currentHeat+1);
			
			eventType = "\nEvent Type: " + Event.this.getType().toString();
			
			printerStatus = "\nPrinter is " + (Event.this.getPrinter().state ? "on" : "off") + "\n";
			
			ArrayList<Competitor> z = Event.this.getHeats().get(currentHeat).getRacers();
			boolean comp = false;
			for(Competitor b : z){
				if(b.getStartTime() == null){
					comp = true;
				}
			}
			if(!comp) inQueue = "\nNo competitors in queue";
			else{
				inQueue = "";
				if(Event.this.type.equals(EventType.IND) || Event.this.type.equals(EventType.GRP)){
					queueSize = z.size();
					for(Competitor i : z){
						boolean a = true;
						for(Competitor j : unfinished){
							if(j.getIdNum() == i.getIdNum()){a = false;}
						}
						if(a && i.getEndTime() == null){inQueue += "\n" + i.getIdNum() + " " + i.getEntryTime().toString();}
					}
				}
				else{
					queueSize = z.size();
					for(Competitor i : z){
						boolean a = true;
						for(Competitor j : unfinishedLane1){
							if(j.getIdNum() == i.getIdNum()){a = false;}
						}
						for(Competitor j : unfinishedLane2){
							if(j.getIdNum() == i.getIdNum()){a = false;}
						}
						for(Competitor j : unfinishedLane3){
							if(j.getIdNum() == i.getIdNum()){a = false;}
						}
						for(Competitor j : unfinishedLane4){
							if(j.getIdNum() == i.getIdNum()){a = false;}
						}
						if(a && i.getEndTime() == null){inQueue += "\n" + i.getIdNum() + " " + i.getEntryTime().toString();}
					}
				}
			}
			
			Time max = new Time(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
			if(Event.this.type.equals(EventType.IND) || Event.this.type.equals(EventType.GRP)){
				if(unfinished.isEmpty()) running = "\nno competitors competing";
				else{
					running = "\n";
					pendingSize = unfinished.size();
					for(Competitor x : unfinished){
						Time y = Time.elapsed(ChronoTimer1009.globalTime, x.getStartTime());
						running += "\n" + x.getIdNum() + " <" + y.getHours() + ":" + y.getMinutes() + ":" + y.getSeconds() + ":" + y.getHundreths() + "> R";
					}
				}
			}
			else if(Event.this.type.equals(EventType.PARGRP) || Event.this.type.equals(EventType.PARIND)){
				running = "";
				//lane1
				String lane1 = "";
				if(unfinishedLane1.isEmpty()) lane1 = "\nLane 1 is empty";
				else{
					lane1 = "\nLane  1:";
					pendingSizeLane1 = unfinishedLane1.size();
					for(Competitor x : unfinishedLane1){
						Time y = Time.elapsed(ChronoTimer1009.globalTime, x.getStartTime());
						lane1 += "\n" + x.getIdNum() + " <" + y.getHours() + ":" + y.getMinutes() + ":" + y.getSeconds() + ":" + y.getHundreths() + "> R";
				
					}
				}
				//lane2
				String lane2 = "";
				if(unfinishedLane2.isEmpty()) lane2 = "\nLane 2 is empty";
				else{
					lane2 = "\nLane 2:";
					pendingSizeLane2 = unfinishedLane2.size();
					for(Competitor x : unfinishedLane2){
						Time y = Time.elapsed(ChronoTimer1009.globalTime, x.getStartTime());
						lane2 += "\n" + x.getIdNum() + " <" + y.getHours() + ":" + y.getMinutes() + ":" + y.getSeconds() + ":" + y.getHundreths() + "> R";
				
					}
				}
				//lane3
				String lane3 = "";
				if(unfinishedLane3.isEmpty()) lane3 = "\nLane 3 is empty";
				else{
					lane3 = "\nLane 3:";
					pendingSizeLane3 = unfinishedLane3.size();
					for(Competitor x : unfinishedLane3){
						Time y = Time.elapsed(ChronoTimer1009.globalTime, x.getStartTime());
						lane3 += "\n" + x.getIdNum() + " <" + y.getHours() + ":" + y.getMinutes() + ":" + y.getSeconds() + ":" + y.getHundreths() + "> R";
				
					}
				}
				//lane4
				String lane4 = "";
				if(unfinishedLane4.isEmpty()) lane4 = "\nLane 4 is empty";
				else{
					lane4 = "\nLane 4:";
					pendingSizeLane4 = unfinishedLane4.size();
					for(Competitor x : unfinishedLane4){
						Time y = Time.elapsed(ChronoTimer1009.globalTime, x.getStartTime());
						lane4 += "\n" + x.getIdNum() + " <" + y.getHours() + ":" + y.getMinutes() + ":" + y.getSeconds() + ":" + y.getHundreths() + "> R";
				
					}
				}
				running = lane1 + "\n" + lane2 + "\n" + lane3 + "\n" + lane4;
			}
			
			finished = "";
			boolean noRunners = true;
			for(Competitor x : runs.get(currentHeat).getRacers()){
				if(x.getEndTime() != null){
					finished += "\n" + x.getIdNum() + " " + (Event.this.findElapsedInLog(x.getIdNum()).equals(max) ? "DNF" : Event.this.findElapsedInLog(x.getIdNum()).toString() + " F");
					noRunners = false;
				}
			}
			if(noRunners){noFinished = "\nno runners have finished";}
			
			
			/*if(log.isEmpty()) noFinished = "\nno runners have finished";
			else if(!log.isEmpty() && (log.size() > logSize)){
				logSize = log.size();
				finished += "\n" + log.peek().getCompetitorNumber() + " " + (log.peek().getElapsedTimer().equals(max) ? "DNF" : log.peek().getElapsedTimer().toString() + " F");
			}*/
			return systemTime + heatNum + eventType + printerStatus + inQueue + running + (noRunners ? noFinished : finished);
			//return systemTime + heatNum + eventType + printerStatus + inQueue + running + (log.isEmpty() ? noFinished : finished);
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
