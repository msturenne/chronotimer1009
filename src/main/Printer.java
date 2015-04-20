package main;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

	public class Printer{
		private boolean state;
		private String outFileName;
		/**
		 * Erase the content of the file
		 */
		public Printer(){
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
		/**
		 * print the information on the top of the Log stack;
		 */
		public void print(){
			try{
				PrintWriter fileOut = new PrintWriter (new FileWriter(outFileName, true));
				Log auxLog = ChronoTimer1009System.getLog().peek();
				fileOut.println(auxLog.toString());
				fileOut.close();
			}catch(IOException e){
				System.out.println("Could not open file! " + e.getMessage() +" (No such file or directory)");
			}
		}
	}
