import java.util.Scanner;
import java.io.*;
import java.nio.file.Paths;


public class Driver {
	Time latestTime;
	private static boolean powerOn = false;
	private static ChronoTimer1009 timer;

	public static void main(String[] args){
		//ChronoTimer1009 timer = new ChronoTimer1009(new Event(EventType.IND));
		//timer.on();
		
		Scanner stdIn = new Scanner(System.in);
		String fileInName = "fileInName";
		String entryType;
		
		System.out.println("Choose the entry method:\n1- prompt\n2- file");
		entryType = stdIn.nextLine();
		
		if (entryType.equals("1")){
			readPromptCommand();
		}else if (entryType.equals("2")){
			readCommandFile(fileInName);
		}
		
	}

	private static void initiate(String time, String command,
			String commandVar, String commandVar2) {
		
		String[] split = time.split(":");
		int hours = Integer.parseInt(split[0]);
		int minutes = Integer.parseInt(split[1]);
		int seconds = Integer.parseInt(split[2]);

		int t = 0;
		t += (int)(hours * (1000*60*60));
		t+=(int)(minutes* (1000*60));
		t+= (int)(seconds*(1000));
		
		if (command.equals("ON")){
			powerOn = true;
			if (timer == null){
				timer = new ChronoTimer1009(new Event(EventType.IND));
			}
		}
		
		if (powerOn == true){
			timer.globalTime.setTime(t);
			
			if(command.equals("OFF"))
			{
				powerOn = false;
			}
			if(command.equals("TIME"))
			{
				timer.globalTime.setTime(Integer.parseInt(commandVar));
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
				timer.getCurrentEvent().getPrinter().print();
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
		
		initiate(time, command, commandVar, commandVar2);
	}
	
	/**
	 * Reads the command line from the terminal.
	 */
	public static void readPromptCommand(){
		Scanner stdIn = new Scanner(System.in);
		
		System.out.println("Enter command line ('exit' to finish):");
		
		String command = stdIn.nextLine();
		while(!command.equals("exit")){
			interpretCommand(command);
			
			System.out.println("Enter next command line ('exit' to finish):");
			command = stdIn.nextLine();
		}
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
				interpretCommand(command);
			}
			fileIn.close();
			System.out.println("Command File read succefuly!");
		}catch(IOException e){
			System.out.println("Could not open file! " + e.getMessage() +" (No such file or directory)");
		}
	}

}
