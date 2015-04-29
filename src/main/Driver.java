package main;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Paths;


public class Driver {
	Time latestTime;
	private static boolean powerOn = false;
	private static ChronoTimer1009System timer;

	public static void main(String[] args) throws UserErrorException{
		timer = new ChronoTimer1009System();
		
		Scanner stdIn = new Scanner(System.in);
		String fileInName = "test_files/";
		String entryType;
		
		System.out.println("Choose the entry method:\n1- prompt\n2- file");
		entryType = stdIn.nextLine();
		
		if (entryType.equals("1")){
			readPromptCommand();
		}else if (entryType.equals("2")){
			System.out.println("Enter the test file name (E.g. file.txt ):");
			fileInName += stdIn.nextLine();
			readCommandFile(fileInName);
		}
		stdIn.close();
	}
	
	private static int toTime(String time){
		String[] split = time.split(":");
		int hours = Integer.parseInt(split[0]);
		int minutes = Integer.parseInt(split[1]);
		double seconds = Double.parseDouble(split[2]);

		int t = 0;
		t += (int)(hours * (1000*60*60));
		t+=(int)(minutes* (1000*60));
		t+= (int)(seconds*(1000));
		
		return t;
	}

	private static void initiate(int time, String command,
			String commandVar, String commandVar2) throws NumberFormatException, UserErrorException {
		
		
		
		if (command.equals("ON")){
			powerOn = true;			
			if (timer == null){
				timer = new ChronoTimer1009System();
				timer.on();
			}else if (!timer.isState()){
				timer.on();
			}
			
			if(timer.getCurEvent() == null){ //exit must have been called or this is the first time we are using the timer
				timer.on();
				ChronoTimer1009System.globalTime.setTime(time);
			}
			
		}
		
		if (powerOn == true){
			ChronoTimer1009System.globalTime.setTime(time);
			
			if(command.equals("OFF"))
			{
				powerOn = false;
			}
			else if(command.equals("TIME"))
			{
				String[] times = commandVar.split(":");
				int hours, minutes, seconds, hundreths;
				hours = minutes = seconds = hundreths = 0;
				if(times.length > 0)
					hours = Integer.parseInt(times[0]);
				if(times.length > 1)
					minutes = Integer.parseInt(times[1]);
				if(times.length > 2)
					seconds = Integer.parseInt(times[2]);
				if(times.length > 3)
					hundreths = Integer.parseInt(times[3]);
				Time variable = new Time(hours, minutes, seconds, hundreths);
				ChronoTimer1009System.globalTime.setTime(variable.getTime());
			}
			else if(command.equals("CONN"))
			{
				ChronoTimer1009System.getChan(Integer.parseInt(commandVar2)).connectSensor(SensorType.valueOf(commandVar));
				
			}
			else if(command.equals("TOGGLE"))
			{
				ChronoTimer1009System.getChan(Integer.parseInt(commandVar)).toggleState();
			}
			else if(command.equals("NUM"))
			{
				int currentHeat = timer.getCurEvent().getCurHeat();
				try {
					timer.getCurEvent().getHeats().get(currentHeat).addCompetitor(new Competitor(currentHeat, Integer.parseInt(commandVar), ChronoTimer1009System.globalTime));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UserErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(command.equals("FIN"))
			{
				try {
					timer.getCurEvent().trigChan(2, true);
				} catch (UserErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(command.equals("START"))
			{
				try {
					timer.getCurEvent().trigChan(1, true);
				} catch (UserErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(command.equals("DNF"))
			{
				//add dnf method ?
				try {
					timer.getCurEvent().trigChan(2, false);
				} catch (UserErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(command.equals("PRINT"))
			{
				//the event class will need either a printer variable that is
				//initialized in the constructor, or a method that accesses the printer
				//class
				ChronoTimer1009System.getPrinter().toggleState();
			}
			else if(command.equals("EXIT"))
			{
				timer.exit();
				//timer = null;
				powerOn = false;
			}
			else if(command.equals("RESET"))
			{
				timer.reset();
				//timer = null;
			}
			else if(command.equals("EVENT"))
			{
				EventType x = EventType.valueOf(commandVar);
				timer.newEvent(x);
			}
			else if(command.equals("TOGGLE"))
			{
				ChronoTimer1009System.getChan(Integer.parseInt(commandVar)).toggleState();
			}
			else if(command.equals("TRIG"))
			{
				//timer.getCurrentEvent().getChannel(Integer.parseInt(commandVar)).triggerChannel();
				try {
					timer.getCurEvent().trigChan(Integer.parseInt(commandVar), true);
				} catch (UserErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(command.equals("EXPORT"))
			{
				//export heats by heat #?
				timer.export();
			}
			else if(command.equals("ENDRUN"))
			{
				try {
					timer.getCurEvent().endRun();
				} catch (UserErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(command.equals("NEWRUN"))
			{
				timer.getCurEvent().createRun();
			}
			
			else if(!command.equals("ON") && !command.equals("OFF"))
			{
				System.out.println("Invalid Command");
			}
			
		}
		
	}

	/**
	 * Interpret and initiate the command received as parameter.
	 * @param line
	 * @throws UserErrorException 
	 * @throws NumberFormatException 
	 */
	public static void interpretCommand(String line) throws NumberFormatException, UserErrorException{
		String[] split = line.split("\\s+");
		String time = split[0];
		String command = split[1];
		String commandVar = null;
		String commandVar2 = null;
		if(split.length > 2)
		{
			commandVar = split[2];
			if(split.length > 3)
				commandVar2 = split[3];
		}
		
		initiate(toTime(time), command, commandVar, commandVar2);
	}
	
	/**
	 * Reads the command line from the terminal.
	 * @throws UserErrorException 
	 * @throws NumberFormatException 
	 */
	public static void readPromptCommand() throws NumberFormatException, UserErrorException{
		Scanner stdIn = new Scanner(System.in);
		
		System.out.println("Enter command line ('exit' to finish):");
		
		String command = stdIn.nextLine();
		while(!command.equals("exit")){
			interpretCommandLine(command);
			if(ChronoTimer1009System.globalTime != null)
				System.out.print(ChronoTimer1009System.globalTime.toString() + ": ");
			command = stdIn.nextLine();
		}
		stdIn.close();
	}
	
	
	private static void interpretCommandLine(String line) throws NumberFormatException, UserErrorException {
		String[] split = line.split("\\s+");
		String command = split[0];
		String commandVar = null;
		String commandVar2 = null;
		if(split.length > 1)
		{
			commandVar = split[1];
			if(split.length > 2)
				commandVar2 = split[2];
		}
		initiate(ChronoTimer1009System.globalTime == null ? 0 : ChronoTimer1009System.globalTime.updateTime(), command, commandVar, commandVar2);
		//initiate(ChronoTimer1009System.globalTime.getTime(), command, commandVar, commandVar2);
		
	}

	/**
	 * Open the command file and read and interpret each command line.
	 * @param filename
	 * @throws UserErrorException 
	 * @throws NumberFormatException 
	 */
	public static void readCommandFile (String filename) throws NumberFormatException, UserErrorException{
		String command;
		try{
			Scanner fileIn = new Scanner (Paths.get(filename));	
			while(fileIn.hasNextLine()){
				command = fileIn.nextLine();
				System.out.println(command);
				interpretCommand(command);
			}
			fileIn.close();
			System.out.println("Command File read succefuly!");
		}catch(IOException e){
			System.out.println("Could not open file! " + e.getMessage() +" (No such file or directory)");
		}
	}

}
