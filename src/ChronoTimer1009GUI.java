import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.*;

public class ChronoTimer1009GUI {
	
	private static ChronoTimer1009 timer;

	private JFrame f;
	private JPanel parentPanel;
	private JPanel parentLeft;
	private JPanel parentMiddle;
	private JPanel parentRight;
	
	private JLabel middleTop;
	
	private JPanel middleGridParent;
	private JPanel optionsGrid;
	private JPanel channelsGrid;
	
	private JPanel leftTop;
	private JPanel leftBottom;
	
	private JPanel rightTop;
	private JTextArea rightBottom;
	
	private JTextArea error;
	
	private boolean manualModeEnabled;
	private Timer clockTimer;
	private boolean canCancel;
	
	private final JComboBox<EventType> eventTypes = new JComboBox<EventType>(EventType.values());
	private final JTextField idNum = new JTextField("Competitor");
	private String idNumText = "";
	
	private final JButton[] channels = new JButton[8];
	
	private final JButton one = new JButton("1");
	private final JButton two = new JButton("2");
	private final JButton three = new JButton("3");
	private final JButton four = new JButton("4");
	private final JButton five = new JButton("5");
	private final JButton six = new JButton("6");
	private final JButton seven = new JButton("7");
	private final JButton eight = new JButton("8");
	private final JButton nine = new JButton("9");
	private final JButton zero = new JButton("0");
	private final JButton[] keypad = {one, two, three, four, five, six, seven, eight,
			nine, zero};
	
	private final JButton exit = new JButton("exit");
	private final JButton power = new JButton("ON/OFF");
	private final JButton printer = new JButton("printer");
	private final JButton reset = new JButton("reset");
	
	private final JButton new_heat = new JButton("New Heat");
	private final JButton start = new JButton("start");
	private final JButton finish = new JButton("Finish");
	private final JButton cancel = new JButton("Cancel");
	private final JButton dnf = new JButton("DNF");
	private final JButton swap = new JButton("Swap");
	private final JButton manual = new JButton("Manual");
	private final JButton clear = new JButton("Clear");
	private final JButton export = new JButton("Export");
	
	private final JButton add = new JButton("add");
	private final JButton create = new JButton("create");
	
	public ChronoTimer1009GUI(){
		setup();
	}
	
	public void setup(){
		manualModeEnabled = canCancel = false;
		f = new JFrame("ChronoTimer1009");
		f.setSize(1200, 400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//start off the system by disabling all button except the power
		setEnabledSelectedButtons(keypad, false);
		setEnabledSelectedButtons(new JButton[]{exit, printer, reset, new_heat, start, finish,
				cancel, dnf, swap, manual, clear, export, add, create}, false);
		eventTypes.setEnabled(false);
		idNum.setEnabled(false);
		//done disabling all buttons except power
		
		this.parentPanel = new JPanel(new GridLayout(1,3));
		
		this.parentLeft = new JPanel(new GridBagLayout());
		parentLeft.setBackground(Color.DARK_GRAY);
		
		this.parentMiddle = new JPanel(new GridBagLayout());
		parentMiddle.setBackground(Color.DARK_GRAY);
		
		this.parentRight = new JPanel(new GridBagLayout());
		parentRight.setBackground(Color.DARK_GRAY);
		
		middleTop = new JLabel("ChronoTimer1009");
		middleTop.setFont(new Font("Bazooka", Font.PLAIN, 36));
		middleTop.setForeground(Color.WHITE);
		
		this.middleGridParent = new JPanel(new GridLayout(2,1,0,25));
		middleGridParent.setBackground(Color.DARK_GRAY);
		
		this.optionsGrid = new JPanel(new GridLayout(5,2));
		optionsGrid.setBackground(Color.DARK_GRAY);
		
		this.channelsGrid = new JPanel(new GridLayout(2,4,5,5));
		channelsGrid.setBackground(Color.DARK_GRAY);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		leftTop = new JPanel(new GridBagLayout());
		leftTop.setBackground(Color.DARK_GRAY);
		gbc.gridx = gbc.gridy = 0;
		leftTop.add(new JLabel("New Competitor"), gbc);
		++gbc.gridx;
		
		leftTop.add(idNum, gbc);
		idNum.setEditable(false);
		gbc.gridx += 2;
		
		add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(timer.getState()){
					String a = idNum.getText();
					int toDisplay = 0;
					if(a.matches("^([0-9][0-9]{0,4}|99999)$")){
						toDisplay = Integer.parseInt(a);
						int currentHeat = timer.getCurrentEvent().getCurrentHeat();
						boolean added = false;
						try {
							added = timer.getCurrentEvent().getHeats().get(currentHeat).addCompetitor(new Competitor(currentHeat, toDisplay, new Time(ChronoTimer1009.globalTime.getTime())));
						} catch (UserErrorException e1) {
							// TODO Auto-generated catch block
							error.setText(e1.getMessage());
						}
						ChronoTimer1009.globalTime.updateTime();
						if(added){
							updateDisplay();
						}
					}
					else{
						idNum.setText("Not Valid");
					}
				}
				idNumText = "";
			}
		});
		leftTop.add(add, gbc);
		gbc.gridx = 0; gbc.gridy = 1;
		leftTop.add(new JLabel("New Event"), gbc);
		++gbc.gridx;
		leftTop.add(eventTypes, gbc);
		gbc.gridx += 2;
		
		leftTop.add(create, gbc);
		create.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//backup log
				Stack<Log> backup = new Stack<Log>();
				Stack<Log> toBackup = new Stack<Log>();
				Stack<Log> oldLog = timer.getCurrentEvent().getLog();
				while(!oldLog.isEmpty()){
					toBackup.push(oldLog.pop());
				}
				while(!toBackup.isEmpty()){
					backup.push(toBackup.pop());
				}
				EventType x = (EventType) eventTypes.getSelectedItem();
				timer.newEvent(x);
				timer.getCurrentEvent().setLog(backup);
				setupEventTypeEnabledButtons(x);
				for(JButton y : channels){
					y.setBackground(Color.RED);
				}
				timer.getCurrentEvent().endRun();
				updateDisplay();
			}
		});
		
		leftBottom = new JPanel(new GridLayout(4,3));
		leftBottom.setBackground(Color.DARK_GRAY);
		
		leftBottom.add(one);
		one.setName("1");
		leftBottom.add(two);
		two.setName("2");
		leftBottom.add(three);
		three.setName("3");
		leftBottom.add(four);
		four.setName("4");
		leftBottom.add(five);
		five.setName("5");
		leftBottom.add(six);
		six.setName("6");
		leftBottom.add(seven);
		seven.setName("7");
		leftBottom.add(eight);
		eight.setName("8");
		leftBottom.add(nine);
		nine.setName("9");
		leftBottom.add(zero);
		zero.setName("0");
		
		ActionListener keypadAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton current = (JButton) e.getSource();
				idNumText+= current.getName();
				idNum.setText(idNumText);
			}	
		};
		
		gbc.gridx = gbc.gridy = 0;
		rightTop = new JPanel(new GridLayout(2,2));
		rightTop.setBackground(Color.DARK_GRAY);
		
		printer.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(timer != null && timer.getCurrentEvent() != null){
					timer.getCurrentEvent().getPrinter().toggleState();
				}
			}
		});
		rightTop.add(printer);
		
		power.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// system is on
				if(timer.getState()){
					timer.off();
					clockTimer.stop();
					setEnabledSelectedButtons(keypad,false);
					setEnabledSelectedButtons(channels, false);
					setEnabledSelectedButtons(new JButton[]{printer, reset, new_heat, start, finish,
							cancel, dnf, swap, manual, clear, export, add, create}, false);
					eventTypes.setEnabled(false);
					idNum.setEnabled(false);
					updateDisplay();
				}
				//system. is off
				else{
					if (timer == null || timer.getCurrentEvent() == null){
						timer = new ChronoTimer1009();
						//timer.newEvent(EventType.IND);
					}
					if(timer.getCurrentEvent() == null){ //exit must have been called or this is the first time we are using the timer
						timer.on(new Event(EventType.IND));
						ChronoTimer1009.globalTime.setTime(ChronoTimer1009.globalTime == null ? 0 : ChronoTimer1009.globalTime.updateTime());
					}
					else{timer.on(timer.getCurrentEvent());} 
					clockTimer.start();
					updateDisplay();
					idNum.setText("Competitor");
					setEnabledSelectedButtons(keypad, true);
					setupEventTypeEnabledButtons(timer.getCurrentEvent().getType());
					idNum.setEnabled(true);
					eventTypes.setEnabled(true);
				}
			}
		});
		rightTop.add(power);
		
		clockTimer = new Timer(10, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				updateDisplay();
			}
		});
		
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(timer != null) timer.exit();
				for(int i = 0; i<channels.length; ++i){
					channels[i].setBackground(Color.RED);
				}
				timer.off();
				clockTimer.stop();
				setEnabledSelectedButtons(keypad,false);
				setEnabledSelectedButtons(new JButton[]{channels[0], channels[1], exit, printer, reset, new_heat, start, finish,
						cancel, dnf, swap, manual, clear, export, add, create}, false);
				eventTypes.setEnabled(false);
				idNum.setEnabled(false);
				idNum.setText("");
				rightBottom.setText("");
				error.setText("");
			}
		});
		rightTop.add(exit);
		
		reset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(clockTimer != null) clockTimer.stop();
				if(timer != null){
					timer.reset();
					timer.getCurrentEvent().endRun();
				}
				for(JButton x : channels){if(x.getBackground().equals(Color.GREEN) || x.getBackground().equals(Color.YELLOW))x.setBackground(Color.RED);}
				clockTimer.restart();
				idNum.setText("Competitor");
				updateDisplay();
			}
		});
		rightTop.add(reset);
		
		rightBottom = new JTextArea(15,15);
		rightBottom.setEditable(false);
		
		error = new JTextArea(2,15);
		error.setLineWrap(true);
		error.setEditable(false);
		
		optionsGrid.add(new_heat);
		new_heat.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					timer.getCurrentEvent().createRun();
				} catch (UserErrorException e1) {
					// TODO Auto-generated catch block
					error.setText(e1.getMessage());
				}
			}
		});
		
		optionsGrid.add(clear);
		clear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					timer.getCurrentEvent().getHeats().get(timer.getCurrentEvent().getCurrentHeat()).clearNextCompetitor();
				} catch (UserErrorException e1) {
					// TODO Auto-generated catch block
					error.setText(e1.getMessage());
				}
			}
		});
		
		start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(timer.getState()){
					try {
						if(timer.getCurrentEvent().getType().equals(EventType.PARGRP)){
							for(JButton x : channels){
								if(x.getBackground().equals(Color.GREEN)){
									timer.getCurrentEvent().getChannel(Integer.parseInt(x.getName())+1).setCanTrigger(true);
									timer.getCurrentEvent().triggerChannel(Integer.parseInt(x.getName()));
								}
							}
						}else{
							timer.getCurrentEvent().triggerChannel(1);
						}
						canCancel = true;
						cancel.setEnabled(true);
					} catch (UserErrorException e1) {
						// TODO Auto-generated catch block
						error.setText(e1.getMessage());
					}
				}
			}
		});
		optionsGrid.add(start);
		
		finish.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					timer.getCurrentEvent().finish(true);
					cancel.setEnabled(false);
				} catch (UserErrorException e1) {
					// TODO Auto-generated catch block
					error.setText(e1.getMessage());
				}
			}
		});
		optionsGrid.add(finish);
		
		optionsGrid.add(cancel);
		cancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(canCancel){
						timer.getCurrentEvent().cancel(timer.getCurrentEvent().getType());
					}
					else error.setText("can only cancel the current competitor");
					canCancel = false;
				} catch (UserErrorException e1) {
					// TODO Auto-generated catch block
					error.setText(e1.getMessage());
				}
			}
		});
		
		dnf.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					timer.getCurrentEvent().finish(false);
				} catch (UserErrorException e1) {
					// TODO Auto-generated catch block
					error.setText(e1.getMessage());
				}
			}
		});
		optionsGrid.add(dnf);
		
		swap.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.getCurrentEvent().getHeats().get(timer.getCurrentEvent().getCurrentHeat()).swap();
			}
		});
		optionsGrid.add(swap);
		
		optionsGrid.add(export);
		export.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					timer.getCurrentEvent().export();
				} catch (UserErrorException e1) {
					// TODO Auto-generated catch block
					error.setText(e1.getMessage());
				}
			}
		});
		
		manual.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(manualModeEnabled){
					start.setEnabled(false);
					for(int i = 0; i<channels.length; ++i){
						if(channels[i].getBackground().equals(Color.YELLOW)){channels[i].setBackground(Color.GREEN);}
						channels[i].setEnabled(true);
					}
					manualModeEnabled = false;
				}
				else{
					start.setEnabled(true);
					for(int i = 0; i<channels.length; ++i){
						if(timer.getCurrentEvent().getType().equals(EventType.PARGRP)){
							if(channels[0].getBackground().equals(Color.GREEN)){channels[1].setBackground(Color.YELLOW);}
							if(channels[2].getBackground().equals(Color.GREEN)){channels[3].setBackground(Color.YELLOW);}
							if(channels[4].getBackground().equals(Color.GREEN)){channels[5].setBackground(Color.YELLOW);}
							if(channels[6].getBackground().equals(Color.GREEN)){channels[7].setBackground(Color.YELLOW);}
						}else{
							if(channels[i].getBackground().equals(Color.GREEN)){channels[i].setBackground(Color.YELLOW);}
							else channels[i].setEnabled(false);
						}
						manualModeEnabled = true;
					}
				}
			}
		});
		optionsGrid.add(manual);
		
		for(int i = 0; i<channels.length; ++i){
			channels[i] = new JButton("CHAN" + (i+1));
			channels[i].setEnabled(false);
			setColor(channels[i]);
			channels[i].setName(String.valueOf(i+1));
			channels[i].addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JButton button = (JButton) e.getSource();
					if(timer != null && timer.getCurrentEvent() != null){
						if(button.getBackground().equals(Color.YELLOW)){
							try {
								timer.getCurrentEvent().triggerChannel(Integer.parseInt(button.getName()));
							} catch (UserErrorException e1) {
								// TODO Auto-generated catch block
								error.setText(e1.getMessage());
							}
						}
						changeButtonColor(button);
					}
				}
			});
		}
		for(JButton c : keypad){c.addActionListener(keypadAction);}
		//add the channel buttons to the grid
		channelsGrid.add(channels[0]);
		channelsGrid.add(channels[2]);
		channelsGrid.add(channels[4]);
		channelsGrid.add(channels[6]);
		channelsGrid.add(channels[1]);
		channelsGrid.add(channels[3]);
		channelsGrid.add(channels[5]);
		channelsGrid.add(channels[7]);
		
		parentPanel.add(parentLeft);
		parentPanel.add(parentMiddle);
		parentPanel.add(parentRight);
		
		gbc.gridx = gbc.gridy = 0;
		++gbc.weighty;
		gbc.anchor = GridBagConstraints.SOUTH;
		parentMiddle.add(middleTop, gbc);
		++gbc.gridy;
		++gbc.weighty;
		gbc.anchor = GridBagConstraints.SOUTH;
		parentMiddle.add(middleGridParent, gbc);
		
		middleGridParent.add(optionsGrid);
		middleGridParent.add(channelsGrid);
		
		gbc.gridheight = 1;
		gbc.weighty = 1;
		gbc.anchor = gbc.SOUTH;
		parentLeft.add(leftTop, gbc);
		++gbc.gridy;
		gbc.gridheight = 2;
		parentLeft.add(leftBottom, gbc);
		
		gbc.gridheight = 1;
		parentRight.add(rightTop, gbc);
		++gbc.gridy;
		parentRight.add(error, gbc);
		++gbc.gridy;
		gbc.gridheight = 2;
		parentRight.add(rightBottom, gbc);
		
		f.add(parentPanel);
		f.setVisible(true);
	}
	
	public void setColor(JButton x){
		x.setBackground(Color.RED);
		x.setBorder(null);
		x.setOpaque(true);
	}
	/**
	 * Enable selected buttons
	 * @param x the buttons to enable
	 */
	public void setEnabledSelectedButtons(JButton[] x, boolean z){
		for(JButton y : x){
			if(z){y.setEnabled(true);}
			else{y.setEnabled(false);}
		}
	}
	/**
	 * enables/disables button depending on the eventype
	 * @param x
	 */
	public void setupEventTypeEnabledButtons(EventType x){
		switch(x){
		case IND: 
			setEnabledSelectedButtons(new JButton[]{channels[0], export, clear, channels[1], cancel, swap, new_heat, add, printer, reset, 
					exit, create, start, finish, dnf, manual}, true);
			setEnabledSelectedButtons(new JButton[]{channels[2], channels[3], channels[4], channels[5], channels[6], channels[7], manual}, false);
		break;
		
		case PARIND:  
			setEnabledSelectedButtons(new JButton[]{export, clear, cancel, swap, new_heat, add, printer, reset, 
					exit, create, start, finish, dnf, manual}, true);
			for(JButton chan : channels){chan.setEnabled(true);}
			setEnabledSelectedButtons(new JButton[]{start, finish}, false);
		break;
		
		case GRP: 
			setEnabledSelectedButtons(new JButton[]{channels[0], export, clear, channels[1], cancel, swap, new_heat, add, printer, reset, 
					exit, create, start, finish, manual}, true);
			setEnabledSelectedButtons(new JButton[]{channels[2], channels[3], channels[4], channels[5], channels[6], channels[7], manual, dnf}, false);
		break;
		
		case PARGRP: 
			setEnabledSelectedButtons(new JButton[]{export, clear, cancel, swap, new_heat, add, printer, reset, 
					exit, create, finish, manual}, true);
			for(JButton chan : channels){chan.setEnabled(true);}
			setEnabledSelectedButtons(new JButton[]{dnf, start, finish}, false);					break;
	}
	}
	public void changeButtonColor(JButton x){
		boolean change = true;
		for(Competitor y : timer.getCurrentEvent().getHeats().get(timer.getCurrentEvent().getCurrentHeat()).getRacers()){
			if(y.isCompeting()){
				change = false; 
				break;
			}
		}
		if(change){
			if(!x.getBackground().equals(Color.YELLOW)){
				if(x.getBackground().equals(Color.RED)){
					x.setBackground(Color.GREEN);
				}
				else x.setBackground(Color.RED);
				timer.getCurrentEvent().getChannel(Integer.parseInt(x.getName())).toggleState();

			}
		}
	}
	public void updateDisplay(){
		rightBottom.setText(timer.getCurrentEvent().getDisplay().display());
	}
	public static void main(String[] args){
		new ChronoTimer1009GUI();
		timer = new ChronoTimer1009();
	}

}