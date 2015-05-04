package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PrinterGUI extends JFrame{
	private static final long serialVersionUID = 1L;
	private final JTextArea console;
	private final JScrollPane consoleScrollPane;
	
	public PrinterGUI(){
		this.setVisible(false);
		this.setSize(new Dimension(300, 200));
		this.setTitle("Printer");
		this.setResizable(false);
		//initialize JTextArea
		console = new JTextArea();
		console.setEditable(false);
		console.setBackground(Color.BLACK);
		console.setForeground(Color.LIGHT_GRAY);
		//initialize JScrollPane
		consoleScrollPane = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(consoleScrollPane);
	}
	
	public void off(){
		this.setVisible(true);
	}
	
	public void on(){
		this.setVisible(false);
	}
	public void print(String x){
		console.setText(console.getText() + "\n" + x);
	}
}
