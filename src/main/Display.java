package main;
public abstract class Display {
	
	public String displaySystemTime(){
		return "System Time: " + ChronoTimer1009System.globalTime.toString();
	}
	
	public abstract String displayHeatNumber();
	
	public abstract String displayEventType();
	
	public String displayPrinterStatus(){
		return "Printer is " + (ChronoTimer1009System.getPrinter().isOn() ? "on" : "off");

	}
	
	public abstract String displayInQueue();
	
	public abstract String displayRunning();
	
	public abstract String displayFinished();
	
	public String updateConsole(){
		ChronoTimer1009System.globalTime.updateTime();
		return displaySystemTime() + "\n" + displayHeatNumber() + "\n" + 
				displayEventType() + "\n" + displayPrinterStatus() + "\n" + displayInQueue() + "\n" + 
				displayRunning() + "\n" + displayFinished();
	}
}
