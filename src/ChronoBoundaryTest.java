import static org.junit.Assert.*;
import junit.framework.TestCase;


public class ChronoBoundaryTest extends TestCase {
	ChronoTimer1009 timer = new ChronoTimer1009(null);
	Competitor One = null;
	Competitor Two = null;
	Competitor Three = null;
	Time validTime = new Time(1,1,1,1);
	Time time = null;
	Time time2 = null;
	//Setup Tests
	public void testEvents(){
		ChronoTimer1009 timer1 = new ChronoTimer1009(new Event(EventType.IND));
		assertEquals(timer1.getCurrentEvent().getType(), EventType.IND);
	}
	public void testChannels(){
		ChronoTimer1009 timer1 = new ChronoTimer1009(new Event(EventType.IND));
		Event one = timer1.getCurrentEvent();
		Channel oneChannel = one.getChannel(1);
		oneChannel.toggleState();
		assertTrue(oneChannel.getState());
		oneChannel.connectSensor(SensorType.EYE);
		assertEquals(SensorType.EYE, oneChannel.getSensor().getType());
		oneChannel.disconnectSensor();
		assertEquals(SensorType.NONE, oneChannel.getSensor().getType());
		oneChannel.toggleState();
		assertFalse(oneChannel.getState());
	}
	//Competitor Tests
	public void testLowBib() {
		try{
			One = new Competitor(1,-1,validTime);
		}catch(Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}	
	}
	public void testHighBib(){
		try{
			One = new Competitor(1, 10000, validTime);
		}catch(Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	//Event Tests
	public void testStart(){
		ChronoTimer1009 timer1 = new ChronoTimer1009(new Event(EventType.IND));
		Channel one = timer1.getCurrentEvent().getChannel(1); 
		one.toggleState();
		try{
			timer1.getCurrentEvent().start();
		}catch(Exception e){
			assertTrue(e instanceof UserErrorException);
		}
	}
	public void testStartChannelsLow(){
		ChronoTimer1009 timer1 = new ChronoTimer1009(new Event(EventType.IND));
		try{
			Channel one = timer1.getCurrentEvent().getChannel(-1);
		}catch(Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	public void testStartChannelsHigh(){
		ChronoTimer1009 timer1 = new ChronoTimer1009(new Event(EventType.IND));
		try{
			Channel one = timer1.getCurrentEvent().getChannel(9);
		}catch(Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	//Time Tests
	public void testTimeConversion(){
		time = new Time(21600000);
		time.setTime(21600000);
		time2 = new Time(6,0,0,0);
		assertTrue(time.equals(time2));
	}
	public void testTimeElapsed(){
		time = new Time(21600000);
		time2 = new Time(7200000);
		Time elapsed = new Time(14400000);
		assertEquals(elapsed.getMillis(), Time.elapsed(time, time2).getMillis());
	}
	public void testRunNum(){
		try{
			One = new Competitor(-1, 45, validTime);
		}catch(Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
}
