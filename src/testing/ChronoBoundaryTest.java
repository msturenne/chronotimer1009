package testing;
import main.Channel;
import main.ChronoTimer1009System;
import main.Competitor;
import main.EventType;
import main.SensorType;
import main.Time;
import main.UserErrorException;
import junit.framework.TestCase;


public class ChronoBoundaryTest extends TestCase {
	Competitor One = null;
	Competitor Two = null;
	Competitor Three = null;
	Time validTime = new Time(1,1,1,1);
	Time time = null;
	Time time2 = null;
	//Setup Tests
	public void testEvents() throws UserErrorException{
		ChronoTimer1009System timer1 = new ChronoTimer1009System();
		assertEquals(timer1.getCurEvent().getType(), EventType.IND);
	}
	public void testChannels() throws UserErrorException{
		Channel oneChannel = ChronoTimer1009System.getChan(1);
		oneChannel.connectSensor(SensorType.EYE);
		oneChannel.toggleState();
		assertTrue(oneChannel.getState());
		assertEquals(SensorType.EYE, oneChannel.getSensor().getType());
		try {
			oneChannel.disconnectSensor();
		} catch (UserErrorException e) {
			e.printStackTrace();
		}
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
	public void testStart() throws UserErrorException{
		ChronoTimer1009System timer1 = new ChronoTimer1009System();
		Channel one = ChronoTimer1009System.getChan(1);
		one.connectSensor(SensorType.EYE);
		one.toggleState();
		try{
			timer1.getCurEvent().trigChan(1, true);
		}catch(Exception e){
			assertTrue(e instanceof UserErrorException);
		}
	}
	public void testStartChannelsLow() throws UserErrorException{
		try{
			ChronoTimer1009System.getChan(-1);
		}catch(Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	public void testStartChannelsHigh() throws UserErrorException{
		try{
			ChronoTimer1009System.getChan(9);
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
