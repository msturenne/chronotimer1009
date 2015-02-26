import java.util.Scanner;
public class Driver {
	Time latestTime;

	public static void main(String[] args){
		ChronoTimer1009 timer = new ChronoTimer1009(new Event(EventType.IND));
		timer.on();
		
		Scanner stdIn = new Scanner(System.in);
		
		String line = stdIn.nextLine();
		do
		{
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
			
			line = stdIn.nextLine();
		}
		while(line != null);
		
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
			timer.currentEvent.getChannel(commandVar2).connectSensor(commandVar);
			
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
