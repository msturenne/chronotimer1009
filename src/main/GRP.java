package main;
public class GRP extends SingleLaneEvent{
	
	public GRP() throws UserErrorException {super();}

	@Override
	public void cancel(int ln) throws UserErrorException {
		if(getUnfinished().isEmpty()) throw new UserErrorException("no runners to cancel");
		boolean canRemove = true;
		for(Competitor y : getHeats().get(getCurHeat()).getRacers()){
			if(y.getEndTime() != null){
				canRemove = false;
				break;
			}
		}
		while(!getUnfinished().isEmpty() && canRemove){
			Competitor comp = getUnfinished().remove();
			comp.setStartTime(null);
			comp.setCompeting(false);
			getHeats().get(getCurHeat()).fix(getType());
		}
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.GRP;
	}

	@Override
	public String displayEventType() {
		// TODO Auto-generated method stub
		return getType().toString();
	}

	@Override
	public void trigChan(int chan, boolean dnf) throws UserErrorException {
		switch(chan){
			case 1:
				if(!this.getHeats().get(getCurHeat()).hasNextCompetitor()){
					throw new UserErrorException("There are no competitors in the queue");
				}
				while(this.getHeats().get(getCurHeat()).hasNextCompetitor()){
					start();
				}
			break;
			
			case 2:
				finish(dnf);
			break;
			
			default: throw new IllegalStateException();
		}
	}
}
