package main;
import java.util.LinkedList;
import java.util.Queue;

public class IND extends SingleLaneEvent{
	
	public IND(String name) throws UserErrorException{super(name);}
	
	public IND() throws UserErrorException{super("insert name here");}

	@Override
	public void trigChan(int chan, boolean dnf) throws UserErrorException {
		switch(chan){
			case 1:
				start();
			break;
			
			case 2:
				finish(dnf);
			break;
			
			default: throw new IllegalStateException();
		}
	}

	@Override
	public void cancel(int ln) throws UserErrorException {
		// TODO Auto-generated method stub
		Queue<Competitor> unfinishedDummy = new LinkedList<Competitor>();
		if(getUnfinished().isEmpty()) throw new UserErrorException("no runners to cancel");
		int size = 0;
		while(size < (getUnfinished().size()-1)){
			unfinishedDummy.add(getUnfinished().remove());
			++size;
		}
		Competitor j = getUnfinished().remove();
		j.setStartTime(null);
		j.setCompeting(false);
		setUnfinished(unfinishedDummy);
		getHeats().get(getCurHeat()).fix(getType());
	}

	@Override
	public String displayEventType() {
		// TODO Auto-generated method stub
		return getType().toString();
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.IND;
	}
}
