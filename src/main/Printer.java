package main;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

	public class Printer{
		private boolean state;
		private String outFileName;
		private String sendToPrinter;
		/**
		 * Erase the content of the file
		 */
		public Printer(){
			sendToPrinter = null;
			state = false;
			outFileName = "printerOutFile";
			try{
				PrintWriter fileOut = new PrintWriter (outFileName);
				fileOut.print("");
				fileOut.close();
			}catch(IOException e){
				System.out.println("Could not open file! " + e.getMessage() +" (No such file or directory)");
			}
		}
		/**
		 * Is the printer on?
		 * @return weather the printer is on.
		 */
		public boolean isOn(){
			return state;
		}
		/**
		 * Toggles the state of the printer
		 */
		public void toggleState(){
			if(state == false) state = true;
			else state = false;
		}
		public void received(){
			sendToPrinter = null;
		}
		public String toSend(){
			return sendToPrinter;
		}
		/**
		 * print the information on the top of the Log stack;
		 */
		public void print(String toPrint){
			try{
				PrintWriter fileOut = new PrintWriter (new FileWriter(outFileName, true));
				this.sendToPrinter = toPrint;
				fileOut.println(toPrint);
				fileOut.close();
			}catch(IOException e){
				System.out.println("Could not open file! " + e.getMessage() +" (No such file or directory)");
			}
		}
	}
