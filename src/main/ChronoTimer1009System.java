package main;

import java.util.LinkedList;
import java.util.Stack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChronoTimer1009System {

	private Event curEvent;
	private static Channel[] channels = new Channel[8];
	private boolean state;
	private static Stack<Log> log;
	private static Printer p;
	public static Time globalTime;
	
	public ChronoTimer1009System() throws UserErrorException{
		for(int i=0; i<channels.length; ++i){channels[i] = new Channel(SensorType.NONE);}  // initialize channels
		this.newEvent(EventType.IND);
		this.state = false; //system is initally off
		log = new Stack<Log>();
		p = new Printer();
		globalTime = null;
	}

	public void newEvent(EventType e) throws UserErrorException {
		switch(e){
			case IND: this.curEvent = new IND(); break;
			case PARIND: this.curEvent = new PARIND(); break;
			case GRP: this.curEvent = new GRP(); break;
			case PARGRP: this.curEvent = new PARGRP(); break;
		}
		for(Channel x : channels){if(x.getState()) x.toggleState();}
	}
	
	public void on() throws UserErrorException{
		if(state) throw new IllegalStateException();
		this.curEvent = new IND();
		ChronoTimer1009System.globalTime = new Time(0);
		state = true;
	}
	
	public void reset() throws UserErrorException{
		if(state) state = false;
		on();
	}
	
	public void exit(){
		this.curEvent = null;
		ChronoTimer1009System.globalTime = null;
		if(!state) throw new IllegalStateException();
		state = false;
	}
	
	public static Time searchElapsedByID(int idNum){
		Time toReturn = null;
		for(Log item : log){
			if(item.getCompetitorNumber() == idNum){
				toReturn = item.getElapsedTime(); break;
			}
		}
		return toReturn;
	}

	/**
	 * @return the curEvent
	 */
	public Event getCurEvent() {
		return curEvent;
	}

	
	/**
	 * @return the state
	 */
	public boolean isState() {
		return state;
	}
	
	public static Channel getChan(int chan){
		if(chan < 1 || chan > 8) throw new IllegalArgumentException("Argument is not in range");
		return channels[chan-1];
	}
	
	@SuppressWarnings("unchecked")
	public void export(){
		//TODO for the server
		//convert the stack to a linked list
		Stack<Log> copy = (Stack<Log>) log.clone(); //is this safe?
		Stack<Log> reversed = new Stack<Log>();
		while(!copy.isEmpty()){reversed.push(copy.pop());}
		LinkedList<Log> LLlog = new LinkedList<Log>();
		while(!reversed.isEmpty()){LLlog.add(reversed.pop());}
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(LLlog); //json is a String that holds the JSON representation of the log in the correct order
		System.out.println(json); //for testing
	}

	/**
	 * @return the log
	 */
	public static Stack<Log> getLog() {
		return log;
	}
	
	/**
	 * @return the p
	 */
	public static Printer getPrinter() {
		return p;
	}
}
