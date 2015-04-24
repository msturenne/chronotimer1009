package main;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
	private int oldLogSize; //used only in this.export()
	
	public ChronoTimer1009System() throws UserErrorException{
		for(int i=0; i<channels.length; ++i){channels[i] = new Channel(SensorType.NONE);}  // initialize channels
		this.newEvent(EventType.IND);
		this.state = false; //system is initally off
		log = new Stack<Log>();
		p = new Printer();
		globalTime = null;
		oldLogSize = 0;
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
	
	public void export(){
		//an alternative implementation is to use only covert/sent the last finished:
		if(log.size() <= oldLogSize) return; //only execute as log changes
		oldLogSize = log.size();
		//TODO for the server
		LinkedList<Log> LLlog2 = new LinkedList<Log>();
		if(!log.isEmpty()){LLlog2.add(log.peek());}
		Gson gson2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json2 = gson2.toJson(LLlog2); //json is a String that holds the JSON representation of the log in the correct order
		System.out.println(json2); //for testing
		/**
		 * TODO json2 should be a json array in the following format:
		 * 
{  
  "heats":[  
    {  
      "runners":[  
        {  
          "place":"1",
          "compNum":"1",
          "elapsed":"0:0:0.83"
        },
        {  
          "place":"1",
          "compNum":"1",
          "elapsed":"0:0:0.83"
        }
      ]
    },
    {  
      "runners":[  
        {  
          "place":"1",
          "compNum":"1",
          "elapsed":"0:0:0.83"
        },
        {  
          "place":"1",
          "compNum":"1",
          "elapsed":"0:0:0.83"
        }
      ]
    },
    {  
      "runners":[  
        {  
          "place":"1",
          "compNum":"1",
          "elapsed":"0:0:0.83"
        },
        {  
          "place":"1",
          "compNum":"1",
          "elapsed":"0:0:0.83"
        }
      ]
    },
    {  
      "runners":[  
        {  
          "place":"1",
          "compNum":"1",
          "elapsed":"0:0:0.83"
        },
        {  
          "place":"1",
          "compNum":"1",
          "elapsed":"0:0:0.83"
        }
      ]
    }
  ]
}
			so this demo json i've been using has 4 identical heats, and each heat has the two identical runners
			and the only data the server currently uses is the place #, the bid # and the elapsed time
			but other than that i have the server program done and css'd so we just need to send it correctly
			formatted json arrays
			
			
			idk what the whole gson thing is doing, but the gson thing doesn't even validate as correct json :/
			we can probably just create our own method for this,
			especially because they should be ordered by fastest time, so like placing/rank
		 */
		
		try{
			URL site = new URL("http://7-dot-eastern-cosmos-92417.appspot.com/chronoserver");
			HttpURLConnection conn = (HttpURLConnection) site.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());

			String data = "data=" + json2;
			out.writeBytes(data);
			out.flush();
			out.close();
			System.out.println("Done sent to server");

			new InputStreamReader(conn.getInputStream());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
