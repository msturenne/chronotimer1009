package testing;

import static org.junit.Assert.*;
import main.ChronoTimer1009System;
import main.Competitor;
import main.Event;
import main.GRP;
import main.IND;
import main.PARGRP;
import main.PARIND;
import main.SensorType;
import main.Time;
import main.UserErrorException;

import org.junit.Test;

public class TestEvent {
	
	Event e1, e2, e3, e4;
	ChronoTimer1009System timer;
	
	public TestEvent() throws UserErrorException{
		timer = new ChronoTimer1009System();
		timer.on();
	}
	
	@Test
	public void testExistenceOfDefaultHeat(){
		e1 = new IND();
		e2 = new GRP();
		e3 = new PARGRP();
		e4 = new PARIND();
		
		assertEquals(e1.getHeats().size(), 1);
		assertEquals(e2.getHeats().size(), 1);
		assertEquals(e3.getHeats().size(), 1);
		assertEquals(e4.getHeats().size(), 1);
	}
	@Test
	public void testIND(){
		Throwable x = null;
		e1 = new IND();
		//cancel should throw an exception
		try {e1.cancel(999);} catch (UserErrorException e2) {x = e2;}
		assertTrue(x instanceof UserErrorException);
		x = null;
		//finish should throw an exception
		try {e1.trigChan(2, true);} catch (UserErrorException e) {x = e;}
		assertTrue(x instanceof UserErrorException);
		x = null;
		//start should throw an exception
		try {e1.trigChan(1, true);} catch (UserErrorException e) {x = e;}
		assertTrue(x instanceof UserErrorException);
		ChronoTimer1009System.getChan(1).connectSensor(SensorType.EYE);
		ChronoTimer1009System.getChan(2).connectSensor(SensorType.EYE);
		x = null;
		//start should still throw an exception
		try {e1.trigChan(1, true);} catch (UserErrorException e) {x = e;}
		assertTrue(x instanceof UserErrorException);
		//add a competitor
		x = null;
		try {
			e1.getHeats().get(e1.getCurHeat()).addCompetitor(new Competitor(e1.getCurHeat(), 1, new Time(12, 00, 00, 00)));
		} catch (UserErrorException e) {x = e;}
		assertEquals(x, null);
		//enable channels
		try {
			ChronoTimer1009System.getChan(1).toggleState();
			ChronoTimer1009System.getChan(2).toggleState();
		} catch (UserErrorException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//start should work now
		x = null;
		try {e1.trigChan(1,  true);} catch (UserErrorException e) {x = e;}
		assertEquals(x, null);
	}

}
