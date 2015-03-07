import java.util.Scanner;
import java.io.*;
import java.nio.file.Paths;


public class Driver {
	Time latestTime;
	private static boolean powerOn = false;
	private static ChronoTimer1009 timer;

	public static void main(String[] args){
		timer = new ChronoTimer1009();
		
		Scanner stdIn = new Scanner(System.in);
		String fileInName = "file.txt";
		String entryType;
		
		System.out.println("Choose the entry method:\n1- prompt\n2- file");
		entryType = stdIn.nextLine();
		
		if (entryType.equals("1")){
			readPromptCommand();
		}else if (entryType.equals("2")){
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
			String commandVar, String commandVar2) {
		
		
		
		if (command.equals("ON")){
			powerOn = true;
			if (timer == null){
				timer = new ChronoTimer1009();
			}
			if(timer.getCurrentEvent() == null){ //exit must have been called or this is the first time we are using the timer
				timer.on(new Event(EventType.IND));
				ChronoTimer1009.globalTime.setTime(time);
			}
			else{timer.on(timer.getCurrentEvent());} 
		}
		
		if (powerOn == true){
			ChronoTimer1009.globalTime.setTime(time);
			
			if(command.equals("OFF"))
			{
				powerOn = false;
			}
			if(command.equals("TIME"))
			{
				String[] times = commandVar.split(":");
				int hours, minutes, seconds;
				hours = minutes = seconds = 0;
				double hundreths = 0;
				if(times.length > 0)
					hours = Integer.parseInt(times[0]);
				if(times.length > 1)
					minutes = Integer.parseInt(times[1]);
				if(times.length > 2)
					seconds = Integer.parseInt(times[2]);
				if(times.length > 3)
					hundreths = Double.parseDouble(times[3]);
				Time variable = new Time(hours, minutes, seconds, hundreths);
				ChronoTimer1009.globalTime.setTime(variable.getTime());
			}
			else if(command.equals("CONN"))
			{
				timer.getCurrentEvent().getChannel(Integer.parseInt(commandVar2)).connectSensor(SensorType.valueOf(commandVar));
				
			}
			else if(command.equals("TOGGLE"))
			{
				timer.getCurrentEvent().getChannel(Integer.parseInt(commandVar)).toggleState();
			}
			else if(command.equals("NUM"))
			{
				//adds new heat with individual runner ?
				//at least that's how i think it should be
				//set up because of what the test output is supposed to be
				int currentHeat = timer.getCurrentEvent().getCurrentHeat();
				timer.getCurrentEvent().getHeats().get(currentHeat).addCompetitor(new Competitor(currentHeat, Integer.parseInt(commandVar), ChronoTimer1009.globalTime));
			}
			else if(command.equals("FIN"))
			{
				timer.getCurrentEvent().finish(true);
			}
			else if(command.equals("START"))
			{
				timer.getCurrentEvent().start();
			}
			else if(command.equals("DNF"))
			{
				//add dnf method ?
				timer.getCurrentEvent().finish(false);
			}
			else if(command.equals("PRINT"))
			{
				//the event class will need either a printer variable that is
				//initialized in the constructor, or a method that accesses the printer
				//class
				//timer.getCurrentEvent().getPrinter().print();
				for(Log x : timer.getCurrentEvent().getLog()){
					System.out.print(x.toString());
					System.out.println();
				}
			}
			else if(command.equals("EXIT"))
			{
				timer = null;
				powerOn = false;
			}
			else if(command.equals("RESET"))
			{
				timer = null;
			}
			
		}
		
	}

	/**
	 * Interpret and initiate the command received as parameter.
	 * @param line
	 */
	public static void interpretCommand(String line){
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
	 */
	public static void readPromptCommand(){
		Scanner stdIn = new Scanner(System.in);
		
		System.out.println("Enter command line ('exit' to finish):");
		
		String command = stdIn.nextLine();
		while(!command.equals("exit")){
			interpretCommandLine(command);
			if(ChronoTimer1009.globalTime != null)
				System.out.print(ChronoTimer1009.globalTime.toString() + ": ");
			command = stdIn.nextLine();
		}
		stdIn.close();
	}
	
	
	private static void interpretCommandLine(String line) {
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
		initiate(ChronoTimer1009.globalTime == null ? 0 : ChronoTimer1009.globalTime.updateTime(), command, commandVar, commandVar2);
		//initiate(ChronoTimer1009.globalTime.getTime(), command, commandVar, commandVar2);
		
	}

	/**
	 * Open the command file and read and interpret each command line.
	 * @param filename
	 */
	public static void readCommandFile (String filename){
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
