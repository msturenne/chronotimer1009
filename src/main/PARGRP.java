package main;

import java.util.Queue;

public class PARGRP extends MultiLaneEvent {
	
	public PARGRP() throws UserErrorException{super();}
	
	private void doCancel(int ln, Queue<Competitor> unfinishedLn){
		while(!getUnfinishedLane(ln).isEmpty()){
			Competitor comp = getUnfinishedLane(ln).remove();
			comp.setStartTime(null);
			comp.setCompeting(false);
			getHeats().get(getCurHeat()).fix(getType());
		}
	}

	@Override
	public void cancel(int ln) throws UserErrorException {
		doCancel(1, getUnfinishedLane(1));
		doCancel(2, getUnfinishedLane(2));
		doCancel(3, getUnfinishedLane(3));
		doCancel(4, getUnfinishedLane(4));
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.PARGRP;
	}

	@Override
	public String displayEventType() {
		// TODO Auto-generated method stub
		return getType().toString();
	}
	

}
