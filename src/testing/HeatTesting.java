package testing;

import java.util.ArrayList;

import main.*;
import junit.framework.TestCase;


public class HeatTesting extends TestCase{
	Time validTime = new Time(1,1,1,1);
	Competitor One = new Competitor(1, 1, validTime);
	Competitor Two = new Competitor(1, 2, validTime);
	Competitor Three = new Competitor(1, 111, validTime);
	
	public void testEventStart() throws UserErrorException{
		ChronoTimer1009System timer1 = new ChronoTimer1009System();
		assertEquals(timer1.getCurEvent().getType(), EventType.IND);
	}
	public void testHasNextEmpty(){
		Heat heat1 = new Heat();
		assertFalse(heat1.hasNextCompetitor());
	}
	public void testHasNextFull(){
		Heat heat1 = new Heat();
		assertFalse(heat1.hasNextCompetitor());
		try{heat1.addCompetitor(One);
			heat1.addCompetitor(Two);
			heat1.addCompetitor(Three);
		}catch(Exception e){System.out.println("Should not throw exception");}
		heat1.setNextCompetitor(One);
		assertTrue(heat1.hasNextCompetitor());
		heat1.setNextCompetitor(Three);
		assertTrue(heat1.hasNextCompetitor());
		try{
			assertEquals(heat1.getNextCompetitor(), Three);
		}catch(Exception e){System.out.println("Should not throw exception");}
	}
	public void testArrayList(){
		Heat heat1 = new Heat();
		ArrayList<Competitor> control = new ArrayList<Competitor>();
		try{
			heat1.addCompetitor(One);
			heat1.addCompetitor(Two);
			heat1.addCompetitor(Three);
			control.add(One);
			control.add(Two);
			control.add(Three);
		}catch(Exception e){System.out.println("Should not throw exception");}
		heat1.setNextCompetitor(One);
		assertEquals(control, heat1.getRacers());
	}
	public void testNextOnEmpty(){
		Heat heat1 = new Heat();
		try{
			heat1.getNextCompetitor();
		}catch(Exception e){
			assertTrue(e instanceof UserErrorException);
		}
	}
	public void testNextNotEmpty(){
		Heat heat1 = new Heat();
		try{
			heat1.addCompetitor(One);
			heat1.addCompetitor(Two);
		}catch(Exception e){System.out.println("Should not throw exception");}
		heat1.setNextCompetitor(One);
		heat1.setNextCompetitor(Two);
		try{
			heat1.getNextCompetitor();
		}catch(Exception e){
			assertTrue(e instanceof UserErrorException);
		}
	}
	public void testSwap(){
		Heat heat1 = new Heat();
		try{
			heat1.addCompetitor(One);
			heat1.addCompetitor(Two);
		}catch(Exception e){System.out.println("Should not throw exception");}
		try {
			heat1.swap();
			heat1.setNextCompetitor(One);
		} catch (UserErrorException e) {System.out.println("Should not throw exception");
		}
		try{
			assertEquals(One, heat1.getNextCompetitor());
		}catch(Exception e ){System.out.println("Should not throw");}
	}
	
	public void testSetNextCompetitor() throws UserErrorException {
		Heat heat1 = new Heat();
		try{
			heat1.setNextCompetitor(One);
		}catch(Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	public void testClearOnEmpty(){
		Heat heat1 = new Heat();
		try{
			heat1.clearNextCompetitor();
		}catch(Exception e){
			assertTrue(e instanceof UserErrorException);
		}
	}
	public void testClearOneByOne(){
		Heat heat1 = new Heat();
		try {
			heat1.addCompetitor(One);
			heat1.addCompetitor(Two);
		} catch (UserErrorException e) { System.out.println("Should not throw exception");}
		int count = 0;
		for(int i = 0; i<heat1.getRacers().size();i++)
		{
			count ++;
		}
		assertEquals(2,count);
		try{
			heat1.clearNextCompetitor();
		}catch(Exception e){System.out.println("Should not throw exception");}
		count = 0;
		for(int i = 0; i<heat1.getRacers().size();i++)
		{
			count ++;
		}
		assertEquals(1,count);
		try{
			heat1.clearNextCompetitor();
		}catch(Exception e){System.out.println("Should not throw exception");}
		count = 0;
		for(int i = 0; i<heat1.getRacers().size();i++)
		{
			count ++;
		}
		assertEquals(0,count);
	}
	public void testClearAllAtOnce(){
		Heat heat1 = new Heat();
		try {
			heat1.addCompetitor(One);
			heat1.addCompetitor(Two);
		} catch (UserErrorException e) { System.out.println("Should not throw exception");}
		while(heat1.hasNextCompetitor())
		{
			try{
				heat1.clearNextCompetitor();
			}catch(Exception e){System.out.println("Should not throw exception");}
		}
		assertEquals(0, heat1.getRacers().size());
	}

}
