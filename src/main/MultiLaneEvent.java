 package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class MultiLaneEvent extends Event {
	
	private Queue<Competitor> unfinishedLane1, unfinishedLane2, unfinishedLane3, unfinishedLane4;
	private Queue<Competitor> canceledLane1, canceledLane2, canceledLane3, canceledLane4;
	private boolean canCancelLane1, canCancelLane2, canCancelLane3, canCancelLane4;

	public MultiLaneEvent() throws UserErrorException {
		super();
		unfinishedLane1 = new LinkedList<Competitor>();
		unfinishedLane2 = new LinkedList<Competitor>();
		unfinishedLane3 = new LinkedList<Competitor>();
		unfinishedLane4 = new LinkedList<Competitor>();
		canceledLane1 = new LinkedList<Competitor>();
		canceledLane2= new LinkedList<Competitor>();
		canceledLane3 = new LinkedList<Competitor>();
		canceledLane4 = new LinkedList<Competitor>();
		canCancelLane1 = false;
		canCancelLane2 = false;
		canCancelLane3 = false;
		canCancelLane4 = false;
	}

	@Override
	public void endRun() throws UserErrorException {
		for(Competitor x : getHeats().get(getCurHeat()).getRacers()){
			if(x.getStartTime() == null){
				x.setStartTime(new Time(0));
			}
		}
		if(!unfinishedLane1.isEmpty()){while(!unfinishedLane1.isEmpty()){trigChan(2, false);};}
		if(!unfinishedLane2.isEmpty()){while(!unfinishedLane2.isEmpty()){trigChan(4, false);};}
		if(!unfinishedLane3.isEmpty()){while(!unfinishedLane3.isEmpty()){trigChan(6, false);};}
		if(!unfinishedLane4.isEmpty()){while(!unfinishedLane4.isEmpty()){trigChan(8, false);};}
	}

	private void doTrigStart(int chan, Queue<Competitor> unfinishedLane, Queue<Competitor> cancelLn, boolean canCancelLn) throws UserErrorException{
		if(!ChronoTimer1009System.getChan(chan).getState() || !ChronoTimer1009System.getChan(chan+1).getState()) 
			throw new UserErrorException("please enable the channels to be used");
		//setCurrentCompetitor
		setCurComp(getHeats().get(getCurHeat()).getNextCompetitor());
		//if this is true, throw error if found in other lane;
		if(!getCurComp().equals(canceledLane1.peek())){
			if(getCurComp().equals(canceledLane2.peek()) || getCurComp().equals(canceledLane3.peek()) || getCurComp().equals(canceledLane4.peek())){ 
				getHeats().get(getCurHeat()).fix(getType());
				throw new UserErrorException("you must start this runner in the lane in which it was canelled");
			}
		}
		else{
			cancelLn.remove();
		}
		//trigger the start channel and record time in the competitors appropriate attribute
		ChronoTimer1009System.getChan(chan).setCanTrigger(true);
		//canCancelLn = true;
		setCanCancelLane((int)(Math.ceil(chan/2.0)), true);
		getCurComp().setStartTime(ChronoTimer1009System.getChan(chan).triggerChannel());
		getCurComp().setCompeting(true);
		unfinishedLane.add(getCurComp());
	}
	
	private void doTrigFinish(int chan, Queue<Competitor> ln, boolean dnf) throws UserErrorException{
		if(ChronoTimer1009System.getChan(chan-1).isCanTrigger()){
			if(ln.isEmpty()) throw new UserErrorException("There are no competitors competing in Lane 1");
			Time DNF = new Time(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE); //will represent a DNF 
			//takes the first unfinished runner
			Competitor finished = ln.remove();
			finished.setCompeting(false);
			//sets the endtime for the completed runner only if they finished.
			//computes the elapsed time
			finished.setEndTime(dnf ? ChronoTimer1009System.getChan(chan).triggerChannel() : DNF);
			//computes the elapsed time
			Time elapsed = DNF;
			if(dnf){elapsed = Time.elapsed(finished.getEndTime(), finished.getStartTime());}
			//adds to the log					//adds to the log
			ChronoTimer1009System.getLog().add(new Log(finished.getStartTime(), finished.getIdNum(), getType(), elapsed, finished.getEndTime(), finished.getRunNum()));
			//tells the printer to print if on
			if(ChronoTimer1009System.getPrinter().isOn()) ChronoTimer1009System.getPrinter().print();
		}
		else throw new UserErrorException("You must start this competitor first");
	}
	
	@Override
	public void trigChan(int chan, boolean dnf) throws UserErrorException {
		switch(chan){
		case 1:
			doTrigStart(1, unfinishedLane1, canceledLane1, canCancelLane1);
		break;
		
		case 2:
			doTrigFinish(2, unfinishedLane1, dnf);
		break;
			
		case 3:
			doTrigStart(3,  unfinishedLane2, canceledLane2, canCancelLane2);
		break;
			
		case 4:
			doTrigFinish(4, unfinishedLane2, dnf);
		break;
			
		case 5:
			doTrigStart(5,  unfinishedLane3, canceledLane3, canCancelLane3);
		break;
			
		case 6:
			doTrigFinish(6, unfinishedLane3, dnf);
		break;
			
		case 7:
			doTrigStart(7,  unfinishedLane4, canceledLane4, canCancelLane4);
		break;
			
		case 8:
			doTrigFinish(8, unfinishedLane4, dnf);
		break;
		}
	}
	
	@Override
	public abstract void cancel(int ln) throws UserErrorException;

	@Override
	public abstract EventType getType();

	public Queue<Competitor> getUnfinishedLane(int ln){
		Queue<Competitor> toReturn = null;
		switch(ln){
		case 1:
			toReturn = unfinishedLane1;
		break;
		case 2:
			toReturn = unfinishedLane2;
		break;
		case 3:
			toReturn = unfinishedLane3;
		break;
		case 4:
			toReturn = unfinishedLane4;
		break;
		}
		return toReturn;
	}
	
	public void setUnfinishedLane(int ln, Queue<Competitor> unfinishedLane){
		switch(ln){
		case 1:
			this.unfinishedLane1 = unfinishedLane;
		break;
		case 2:
			this.unfinishedLane2 = unfinishedLane;
		break;
		case 3:
			this.unfinishedLane3 = unfinishedLane;
		break;
		case 4:
			this.unfinishedLane4 = unfinishedLane;
		break;
		}
	}
	
	public boolean isCanCancelLane(int ln){
		boolean toReturn = false;
		switch(ln){
		case 1:
			toReturn = canCancelLane1;
		break;
		case 2:
			toReturn = canCancelLane2;
		break;
		case 3:
			toReturn = canCancelLane3;
		break;
		case 4:
			toReturn = canCancelLane4;
		break;
		}
		return toReturn;
	}

	public void setCanCancelLane(int ln, boolean canCancelLane){
		switch(ln){
		case 1:
			this.canCancelLane1 = canCancelLane;
		break;
		case 2:
			this.canCancelLane2 = canCancelLane;
		break;
		case 3:
			this.canCancelLane3 = canCancelLane;
		break;
		case 4:
			this.canCancelLane4 = canCancelLane;
		break;
		}
	}
	
	public Queue<Competitor> getCanceledLane(int ln){
		Queue<Competitor> toReturn = null;
		switch(ln){
		case 1:
			toReturn = canceledLane1;
		break;
		case 2:
			toReturn = canceledLane2;
		break;
		case 3:
			toReturn = canceledLane3;
		break;
		case 4:
			toReturn = canceledLane4;
		break;
		}
		return toReturn;
	}

	@Override
	public abstract String displayEventType();

	@Override
	public String displayInQueue() {
		String toReturn = "";
		ArrayList<Competitor> z = getHeats().get(getCurHeat()).getRacers();
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
			if(a && i.getEndTime() == null){toReturn += "\n" + i.getIdNum() + " " + i.getEntryTime().toString();}
		}
		return toReturn;
	}

	@Override
	public String displayRunning() {
		//lane1
		String lane1 = "";
		if(unfinishedLane1.isEmpty()) lane1 = "Lane 1 is empty";
		else{
			lane1 = "Lane  1:";
			for(Competitor x : unfinishedLane1){
				Time y = Time.elapsed(ChronoTimer1009System.globalTime, x.getStartTime());
				lane1 += "\n" + x.getIdNum() + " <" + y.getHours() + ":" + y.getMinutes() + ":" + y.getSeconds() + ":" + y.getHundreths() + "> R";
		
			}
		}
		//lane2
		String lane2 = "";
		if(unfinishedLane2.isEmpty()) lane2 = "Lane 2 is empty";
		else{
			lane2 = "Lane 2:";
			for(Competitor x : unfinishedLane2){
				Time y = Time.elapsed(ChronoTimer1009System.globalTime, x.getStartTime());
				lane2 += "\n" + x.getIdNum() + " <" + y.getHours() + ":" + y.getMinutes() + ":" + y.getSeconds() + ":" + y.getHundreths() + "> R";
		
			}
		}
		//lane3
		String lane3 = "";
		if(unfinishedLane3.isEmpty()) lane3 = "Lane 3 is empty";
		else{
			lane3 = "Lane 3:";
			for(Competitor x : unfinishedLane3){
				Time y = Time.elapsed(ChronoTimer1009System.globalTime, x.getStartTime());
				lane3 += "\n" + x.getIdNum() + " <" + y.getHours() + ":" + y.getMinutes() + ":" + y.getSeconds() + ":" + y.getHundreths() + "> R";
		
			}
		}
		//lane4
		String lane4 = "";
		if(unfinishedLane4.isEmpty()) lane4 = "Lane 4 is empty";
		else{
			lane4 = "Lane 4:";
			for(Competitor x : unfinishedLane4){
				Time y = Time.elapsed(ChronoTimer1009System.globalTime, x.getStartTime());
				lane4 += "\n" + x.getIdNum() + " <" + y.getHours() + ":" + y.getMinutes() + ":" + y.getSeconds() + ":" + y.getHundreths() + "> R";
		
			}
		}
		return lane1 + "\n" + lane2 + "\n" + lane3 + "\n" + lane4;
	}
}