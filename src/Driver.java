import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;
public class Driver {
	Time latestTime;

	public static void main(String[] args){
		ChronoTimer1009 timer = new ChronoTimer1009(new Event(EventType.IND));
		timer.on();
		
		Scanner stdIn = new Scanner(System.in);
		String fileInName = "fileInName";
		String entryType;
		
		System.out.println("Choose the entry method:\n1- prompt\n2- file");
		entryType = stdIn.nextLine();
		
		if (entryType.equals("1")){
			readPromptCommand(timer);
		}else if (entryType.equals("2")){
			readCommandFile(timer, fileInName);
		}
		
		
	}
	
	/**
	 * Interpret and initiate the command received as parameter.
	 * @param timer
	 * @param line
	 */
	public static void interpretCommand(ChronoTimer1009 timer, String line){
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
		
		initiate(timer, time, command, commandVar, commandVar2);
	}
	
	/**
	 * Reads the command line from the terminal.
	 * @param timer
	 */
	public static void readPromptCommand(ChronoTimer1009 timer){
		Scanner stdIn = new Scanner(System.in);
		
		System.out.println("Enter command line ('exit' to finish):");
		
		String command = stdIn.nextLine();
		while(!command.equals("exit")){
			interpretCommand(timer, command);
			
			System.out.println("Enter next command line ('exit' to finish):");
			command = stdIn.nextLine();
		}
	}
	
	
	/**
	 * Open the command file and read and interpret each command line.
	 * @param filename
	 */
	public static void readCommandFile (ChronoTimer1009 timer, String filename){
		String command;
		try{
			Scanner fileIn = new Scanner (Paths.get(filename));	
			while(fileIn.hasNextLine()){
				command = fileIn.nextLine();
				interpretCommand(timer, command);
			}
			fileIn.close();
			System.out.println("Command File read succefuly!");
		}catch(IOException e){
			System.out.println("Could not open file! " + e.getMessage() +" (No such file or directory)");
		}
	}

	private static void initiate(ChronoTimer1009 timer, String time, String command,
			String commandVar, String commandVar2) {
		String[] split = time.split(":");
		int hours = Integer.parseInt(split[0]);
		int minutes = Integer.parseInt(split[1]);
		int seconds = Integer.parseInt(split[2]);

		int t = 0;
		t += (int)(hours * (1000*60*60));
		t+=(int)(minutes* (1000*60));
		t+= (int)(seconds*(1000));
		
		timer.globalTime.setTime(t);
		
		if(command.equals("TIME"))
		{
			timer.globalTime.setTime(commandVar);
		}
		else if(command.equals("ON"))
		{
			timer.on();
		}
		else if(command.equals("OFF"))
		{
			timer.off();
		}
		else if(command.equals("CONN"))
		{
			timer.getCurrentEvent().getChannel(commandVar2).connectSensor(commandVar);
			
		}
		else if(command.equals("TOGGLE"))
		{
			timer.on();
		}
		else if(command.equals("NUM"))
		{
			timer.on();
		}
		else if(command.equals("FIN"))
		{
			timer.on();
		}
		else if(command.equals("START"))
		{
			timer.on();
		}
		else if(command.equals("DNF"))
		{
			timer.on();
		}
		else if(command.equals("PRINT"))
		{
			timer.on();
		}
		else if(command.equals("OFF"))
		{
			timer.on();
		}
		
		
		
	}
}
