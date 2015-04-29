package main;

import java.util.LinkedList;
import java.util.Queue;

public class PARIND extends MultiLaneEvent{
	
	public PARIND() throws UserErrorException{super();}
	
	private boolean doCancel(int ln, boolean canCancelLn, Queue<Competitor> canceledLn, Queue<Competitor> unfinishedLane) throws UserErrorException{
		if(!canCancelLn) throw new UserErrorException("You can only cancel the current competitor");
		Queue<Competitor> unfinishedDummyLane = new LinkedList<Competitor>();
		if(unfinishedLane.isEmpty()) throw new UserErrorException("no runners to cancel");
		int sizeLane = 0;
		int prevUnfinishedSize = unfinishedLane.size()-1;
		while(sizeLane < prevUnfinishedSize){
			unfinishedDummyLane.add(unfinishedLane.remove());
			++sizeLane;
		}
		Competitor j = unfinishedLane.remove();
		j.setStartTime(null);
		j.setCompeting(false);
		//if canceled, add to canceled queue
		canceledLn.add(j);
		setUnfinishedLane(ln, unfinishedDummyLane);
		getHeats().get(getCurHeat()).fix(getType());
		return false;
	}

	@Override
	public void cancel(int ln) throws UserErrorException {
		switch(ln){
		case 1:
			setCanCancelLane(1, doCancel(1, isCanCancelLane(1), getCanceledLane(1), getUnfinishedLane(ln)));
		break;
		
		case 2: 
			setCanCancelLane(2, doCancel(2, isCanCancelLane(2), getCanceledLane(2), getUnfinishedLane(ln)));
		break;
			
		case 3:
			setCanCancelLane(3, doCancel(3, isCanCancelLane(3), getCanceledLane(3), getUnfinishedLane(ln)));
		break;
			
		case 4:
			setCanCancelLane(4, doCancel(4, isCanCancelLane(4), getCanceledLane(4), getUnfinishedLane(ln)));
		break;
		}
	}
	
	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.PARIND;
	}

	@Override
	public String displayEventType() {
		// TODO Auto-generated method stub
		return getType().toString();
	}
}
