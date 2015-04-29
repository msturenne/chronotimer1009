package testing;

import static org.junit.Assert.*;
import main.Event;
import main.GRP;
import main.IND;
import main.PARGRP;
import main.PARIND;
import main.UserErrorException;

import org.junit.Test;

public class TestEvent {
	
	Event e1, e2, e3, e4;

	@Test
	public void testExistenceOfDefaultHeat() throws UserErrorException {
		e1 = new IND();
		e2 = new GRP();
		e3 = new PARGRP();
		e4 = new PARIND();
		
		assertEquals(e1.getHeats().size(), 1);
		assertEquals(e2.getHeats().size(), 1);
		assertEquals(e3.getHeats().size(), 1);
		assertEquals(e4.getHeats().size(), 1);
	}

}
