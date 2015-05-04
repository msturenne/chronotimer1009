package main;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChronoTimer1009System {

	private Event curEvent;
	private static Channel[] channels = new Channel[8];
	private boolean state;
	private static ArrayList<Event> masterLog;
	private static Printer p;
	public static Time globalTime;
	
	public ChronoTimer1009System() throws UserErrorException{
		for(int i=0; i<channels.length; ++i){channels[i] = new Channel(SensorType.NONE);}  // initialize channels
		masterLog = new ArrayList<Event>(); //this holds references to each event
		this.state = false; //system is initally off
		p = new Printer();
		globalTime = null;
		this.newEvent(EventType.IND);
	}

	public void newEvent(EventType e) throws UserErrorException {
		if(this.curEvent != null){
			try{this.curEvent.endRun();}catch(UserErrorException x){}
			if(this.curEvent.getHeats().get(this.curEvent.getCurHeat()).getRacers().size() == 0) this.curEvent.getHeats().remove(this.curEvent.getCurHeat());
			if(this.curEvent.getHeats().size() == 0) ChronoTimer1009System.getMasterLog().remove(ChronoTimer1009System.getMasterLog().indexOf(this.curEvent));
		}
		
		switch(e){
			case IND: this.curEvent = new IND(); break;
			case PARIND: this.curEvent = new PARIND(); break;
			case GRP: this.curEvent = new GRP(); break;
			case PARGRP: this.curEvent = new PARGRP(); break;
		}
		for(Channel x : channels){if(x.getState()) x.toggleState();}
		masterLog.add(curEvent);
	}
	
	public void on() throws UserErrorException{
		if(state) throw new IllegalStateException();
		if(this.curEvent == null){
			newEvent(EventType.IND);
		}
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
	public static void export(){
		//*****FORMAT JSON*****
		//before formating, a sort of the runners within each heat is needed to determine place.
		String toJson = "{\"events\":[";
		//iterate through each event
		for(int i = ChronoTimer1009System.getMasterLog().size()-1; i >= 0; --i){
			//iterate through each heat of each event
			toJson += "{\"heats\":[";
			for(int j = ChronoTimer1009System.getMasterLog().get(i).getHeats().size()-1; j >= 0; --j){
				//remove heats that are empty
				if(ChronoTimer1009System.getMasterLog().get(i).getHeats().get(j).getRacers().size() == 0) continue;
				//iterate through each competitor in each heat
				toJson += "{\"runners\":[";
				ArrayList<Competitor> y = new ArrayList<Competitor>();
				//only sort competitors that have finished
				for(Competitor x : ChronoTimer1009System.getMasterLog().get(i).getHeats().get(j).getRacers()){if(x.getEndTime() != null) y.add(x);}
				y = sortByPlace(y);
				for(int k = 0; k < y.size(); ++k){
					//notice we are working with a sorted copy
					if(y.get(k).getEndTime() == null) continue;
					toJson += "{\"place\":\"" + String.valueOf(k+1) + "\",\"compNum\":\"" + y.get(k).getIdNum() + "\", \"elapsed\":\"" + y.get(k).getEndTime().toString() + "\"}";
					if(k < y.size()-1) toJson += ",";
				}
				toJson += "]}";
				if(j > 0) toJson += ",";
			}
			toJson += "]}";
			if((i > 0)){
				toJson += ",";
			}
		}
		toJson += "]}";
		
		//System.out.println(toJson);
		
		try{
			URL site = new URL("http://5-dot-eastern-cosmos-92417.appspot.com/server");
			HttpURLConnection conn = (HttpURLConnection) site.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());

			String data = "data=" + toJson;
			out.writeBytes(data);
			out.flush();
			out.close();
			//System.out.println("Done sent to server");

			new InputStreamReader(conn.getInputStream());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	private static ArrayList<Competitor> sortByPlace(ArrayList<Competitor> whole){
        ArrayList<Competitor> left = new ArrayList<Competitor>();
        ArrayList<Competitor> right = new ArrayList<Competitor>();
        int center;
        if(whole.size()==1)    
            return whole;
        else{
            center = whole.size()/2;
            // copy the left half of whole into the left.
            for(int i=0; i<center; i++){
                    left.add(whole.get(i));
            }
            //copy the right half of whole into the new arraylist.
            for(int i=center; i<whole.size(); i++){
                    right.add(whole.get(i));
            }
 
            // Sort the left and right halves of the arraylist.
            left  = sortByPlace(left);
            right = sortByPlace(right);
            // Merge the results back together.
            merge(left,right,whole);
        }
        return whole;
    }
    private static void merge(ArrayList<Competitor> left, ArrayList<Competitor> right, ArrayList<Competitor> whole) {
        int leftIndex = 0;
        int rightIndex = 0;
        int wholeIndex = 0;
        // As long as neither the left nor the right arraylist has
        // been used up, keep taking the smaller of left.get(leftIndex)
        // or right.get(rightIndex) and adding it at both.get(bothIndex).
        while (leftIndex < left.size() && rightIndex < right.size()){
            if ((left.get(leftIndex).getEndTime().compareTo(right.get(rightIndex).getEndTime())) <= 0){
                whole.set(wholeIndex,left.get(leftIndex));
                leftIndex++;
            }
            else{
                whole.set(wholeIndex, right.get(rightIndex));
                rightIndex++;
            }
            wholeIndex++;
        }
        ArrayList<Competitor>rest;
        int restIndex;
        if (leftIndex >= left.size()) {
            // The left arraylist has been use up...
            rest = right;
            restIndex = rightIndex;
        }
        else {
            // The right arraylist has been used up...
            rest = left;
            restIndex = leftIndex;
        }
        // Copy the rest of whichever arraylist (left or right) was
        // not used up.
        for (int i=restIndex; i<rest.size(); i++) {
            whole.set(wholeIndex, rest.get(i));
            wholeIndex++;
        }
    }
	/**
	 * @return the masterLog
	 */
	public static ArrayList<Event> getMasterLog() {
		return masterLog;
	}
	/**
	 * @return the p
	 */
	public static Printer getPrinter() {
		return p;
	}
}
