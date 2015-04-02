import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ChronoTimer1009GUI {
	
	private static ChronoTimer1009 timer;

	private JFrame f;
	private JPanel parentPanel;
	private JPanel parentLeft;
	private JPanel parentMiddle;
	private JPanel parentRight;
	private int resetErrorDisplay;
	
	private JLabel middleTop;
	private final JLabel connSensor = new JLabel("Conn Sensor");
	private final JComboBox<String> connChan = new JComboBox<String>(new String[]{"Chan 1", "Chan 2", "Chan 3", "Chan 4", "Chan 5", "Chan 6", "Chan 7", "Chan 8"});
	private final JComboBox<SensorType> sensors = new JComboBox<SensorType>(new SensorType[]{SensorType.EYE, SensorType.GATE, SensorType.PAD});
	private final JButton connect = new JButton("Connect");
	
	private final JLabel discSensor = new JLabel("Disc Sensor");
	private final JButton disconnect = new JButton("Disconnect");
	private final JComboBox<String> discChan = new JComboBox<String>(new String[]{"Chan 1", "Chan 2", "Chan 3", "Chan 4", "Chan 5", "Chan 6", "Chan 7", "Chan 8"});
	
	private JPanel middleGridParent;
	private JPanel optionsGrid;
	private JPanel channelsGrid;
	
	private JPanel leftTop;
	private JPanel leftBottom;
	
	private final GridLayout optionsGridLayout = new GridLayout(5,2);
	
	private JPanel rightTop;
	private JTextArea rightBottom;
	private JScrollPane displayHolder;
	
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
	private final JButton power = new JButton("ON");
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
		resetErrorDisplay = 0;
		f = new JFrame("ChronoTimer1009");
		f.setSize(1200, 400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//start off the system by disabling all button except the power
		setEnabledSelectedButtons(keypad, false);
		setEnabledSelectedButtons(new JButton[]{exit, printer, reset, new_heat, start, finish,
				cancel, dnf, swap, manual, clear, connect, disconnect, export, add, create}, false);
		eventTypes.setEnabled(false);
		discChan.setEnabled(false);
		idNum.setEnabled(false);
		sensors.setEnabled(false);
		connChan.setEnabled(false);
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
		
		this.optionsGrid = new JPanel(optionsGridLayout);
		optionsGrid.setBackground(Color.DARK_GRAY);
		
		this.channelsGrid = new JPanel(new GridLayout(2,4,5,5));
		channelsGrid.setBackground(Color.DARK_GRAY);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		leftTop = new JPanel(new GridBagLayout());
		leftTop.setBackground(Color.DARK_GRAY);
		gbc.gridx = gbc.gridy = 0;
		leftTop.add(new JLabel("New Comp"), gbc);
		++gbc.gridx;
		
		leftTop.add(idNum, gbc);
		idNum.setEditable(false);
		++gbc.gridx;
		
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
		++gbc.gridx;
		
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
				sensors.setEnabled(true);
				discChan.setEnabled(true);
				connChan.setEnabled(true);
				try {
					timer.getCurrentEvent().endRun();
				} catch (UserErrorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateDisplay();
			}
		});
		
		connect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int parameter = 0;
				String selectedChannel = (String) connChan.getSelectedItem();
				SensorType selectedSensor = (SensorType) sensors.getSelectedItem();
				if(selectedChannel.equals("Chan 1")) parameter = 1;
				if(selectedChannel.equals("Chan 2")) parameter = 2;
				if(selectedChannel.equals("Chan 3")) parameter = 3;
				if(selectedChannel.equals("Chan 4")) parameter = 4;
				if(selectedChannel.equals("Chan 5")) parameter = 5;
				if(selectedChannel.equals("Chan 6")) parameter = 6;
				if(selectedChannel.equals("Chan 7")) parameter = 7;
				if(selectedChannel.equals("Chan 8")) parameter = 8;
				try {
					addSensorPicture(channels[parameter-1], selectedSensor);
					timer.getCurrentEvent().getChannel(parameter).connectSensor(selectedSensor);
				} catch (UserErrorException e1) {
					// TODO Auto-generated catch block
					error.setText(e1.getMessage());
				}
			}
		});
		
		gbc.gridx = 0; gbc.gridy = 2;
		leftTop.add(connSensor, gbc);
		++gbc.gridx;
		leftTop.add(connChan, gbc);
		++gbc.gridx;
		leftTop.add(sensors, gbc);
		++gbc.gridx;
		leftTop.add(connect, gbc);
		++gbc.gridx;
		
		disconnect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int parameter = 0;
				String selectedChannel = (String) discChan.getSelectedItem();
				if(selectedChannel.equals("Chan 1")) parameter = 1;
				if(selectedChannel.equals("Chan 2")) parameter = 2;
				if(selectedChannel.equals("Chan 3")) parameter = 3;
				if(selectedChannel.equals("Chan 4")) parameter = 4;
				if(selectedChannel.equals("Chan 5")) parameter = 5;
				if(selectedChannel.equals("Chan 6")) parameter = 6;
				if(selectedChannel.equals("Chan 7")) parameter = 7;
				if(selectedChannel.equals("Chan 8")) parameter = 8;
				try {
					addSensorPicture(channels[parameter-1], SensorType.NONE);
					timer.getCurrentEvent().getChannel(parameter).disconnectSensor();
				} catch (UserErrorException e1) {
					// TODO Auto-generated catch block
					error.setText(e1.getMessage());
				}
			}
		});
		
		gbc.gridx = 0; gbc.gridy = 3;
		leftTop.add(discSensor, gbc);
		++gbc.gridx;
		leftTop.add(discChan, gbc);
		++gbc.gridx;
		leftTop.add(disconnect, gbc);
		gbc.gridx+=2;
		
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
				JButton button = (JButton) e.getSource();
				button.setEnabled(false);
				if(!timer.getState()){
					if (timer == null){
						timer = new ChronoTimer1009();
						timer.newEvent(EventType.IND);
					}
					if(timer.getCurrentEvent() == null){ //exit must have been called or this is the first time we are using the timer
						timer.on(new Event(EventType.IND));
						ChronoTimer1009.globalTime.setTime(ChronoTimer1009.globalTime == null ? 0 : ChronoTimer1009.globalTime.updateTime());
					}
					else{timer.on(timer.getCurrentEvent());} 
					clockTimer.start();
					//updateDisplay();
					idNum.setText("Competitor");
					setEnabledSelectedButtons(keypad, true);
					setupEventTypeEnabledButtons(timer.getCurrentEvent().getType());
					idNum.setEnabled(true);
					discChan.setEnabled(true);
					sensors.setEnabled(true);
					connChan.setEnabled(true);
					eventTypes.setEnabled(true);
				}
			}
		});
		rightTop.add(power);
		
		clockTimer = new Timer(10, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//only show error messages for 5 seconds
				if(resetErrorDisplay == 500){
					error.setText("");
					resetErrorDisplay = 0;
				}
				++resetErrorDisplay;
				//update the display every hundreth of a second
				updateDisplay();
			}
		});
		
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(timer != null) timer.exit();
				for(int i = 0; i<channels.length; ++i){
					channels[i].setBackground(Color.RED);
					try {
						addSensorPicture(channels[i], SensorType.NONE);
					} catch (UserErrorException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				timer.off();
				clockTimer.stop();
				setEnabledSelectedButtons(keypad,false);
				setEnabledSelectedButtons(new JButton[]{channels[0], channels[1], exit, printer, reset, new_heat, start, finish,
						cancel, dnf, swap, manual, clear, export, connect, disconnect, add, create}, false);
				eventTypes.setEnabled(false);
				connChan.setEnabled(false);
				discChan.setEnabled(false);
				sensors.setEnabled(false);
				idNum.setEnabled(false);
				idNum.setText("");
				power.setEnabled(true);
				rightBottom.setText("");
				error.setText("");
			}
		});
		rightTop.add(exit);
		
		reset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(timer != null){
					timer.reset();
					try {
						if(timer.getCurrentEvent() != null)timer.getCurrentEvent().endRun();
					} catch (UserErrorException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				for(JButton x : channels){
					x.setBackground(Color.RED);
					try {
						addSensorPicture(x, SensorType.NONE);
					} catch (UserErrorException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				clockTimer.restart();
				connect.setEnabled(true);
				connChan.setEnabled(true);
				sensors.setEnabled(true);
				disconnect.setEnabled(true);
				discChan.setEnabled(true);
				idNum.setText("Competitor");
				error.setText("");
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
					timer.getCurrentEvent().endRun();
					timer.getCurrentEvent().createRun();
				} catch (UserErrorException e1) {
					// TODO Auto-generated catch block
					error.setText(e1.getMessage());
				}
				sensors.setEnabled(true);
				connect.setEnabled(true);
				connChan.setEnabled(true);
				disconnect.setEnabled(true);
				discChan.setEnabled(true);
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
				JButton button = (JButton) e.getSource();
				if(timer.getState()){
					try {
						if(timer.getCurrentEvent().getType().equals(EventType.PARGRP)){
							button.setEnabled(false);
							//first check for sensors enabled
							for(JButton x : channels){
								if(x.getBackground().equals(Color.GREEN)){
									if(timer.getCurrentEvent().getChannel(Integer.parseInt(x.getName())).getSensor().getType().equals(SensorType.NONE) ||
											timer.getCurrentEvent().getChannel(Integer.parseInt(x.getName())+1).getSensor().getType().equals(SensorType.NONE)){
										start.setEnabled(true);
										throw new UserErrorException("Please connect all sensors for all channels used");
									}
								}
							}
							//then trigger
							for(JButton x : channels){
								if(x.getBackground().equals(Color.GREEN)){
									timer.getCurrentEvent().getChannel(Integer.parseInt(x.getName())+1).setCanTrigger(true);
									timer.getCurrentEvent().triggerChannel(Integer.parseInt(x.getName()), true);
								}
							}
						}else{ //EventType is individual or group
							timer.getCurrentEvent().triggerChannel(1, true);
						}
						canCancel = true;
						cancel.setEnabled(true);
						
						disableSensorConnection();
					} catch (UserErrorException e1) {
						// TODO Auto-generated catch block
						if(canEnableSensorConnection(timer.getCurrentEvent().getType())){
							enableSensorConnection();
						}
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
					if(canEnableSensorConnection(timer.getCurrentEvent().getType())){enableSensorConnection();}
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
					if(timer.getCurrentEvent().getType().equals(EventType.PARIND)){
						for(int i = 1; i<channels.length; i+=2){
							if(channels[i].getBackground().equals(Color.YELLOW)){
								channels[i].setBackground(Color.ORANGE);
								error.setText("Click on the orange channel to activate cancel");
							}
							else if(channels[i].getBackground().equals(Color.ORANGE)){
								channels[i].setBackground(Color.YELLOW);
							}
						}
					}
					else if(canCancel){
						timer.getCurrentEvent().cancel(timer.getCurrentEvent().getType(), 0);
					}
					else{
						error.setText("can only cancel the current competitor");
					}
					canCancel = false;
					EventType thisEvent = timer.getCurrentEvent().getType();
					if(thisEvent.equals(EventType.IND) || thisEvent.equals(EventType.GRP)){
						if(canEnableSensorConnection(timer.getCurrentEvent().getType())){enableSensorConnection();}
					}
					if(thisEvent.equals(EventType.PARGRP)){
						start.setEnabled(true);
						enableSensorConnection();
					}
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
					switch(timer.getCurrentEvent().getType()){
					case GRP:
						timer.getCurrentEvent().finish(false);
						if(canEnableSensorConnection(timer.getCurrentEvent().getType())) enableSensorConnection();
					break;
					
					case IND:
						timer.getCurrentEvent().finish(false);
						if(canEnableSensorConnection(timer.getCurrentEvent().getType())){enableSensorConnection();}
					break;
					
					case PARGRP:
						for(int i = 1; i<channels.length; i+=2){
							if(channels[i].getBackground().equals(Color.YELLOW)){
								channels[i].setBackground(Color.cyan);
								error.setText("Click on the blue channel to activate DNF");
							}
							else if(channels[i].getBackground().equals(Color.cyan)){
								channels[i].setBackground(Color.YELLOW);
							}
						}
					break;
					
					case PARIND:
						for(int i = 1; i<channels.length; i+=2){
							if(channels[i].getBackground().equals(Color.YELLOW)){
								channels[i].setBackground(Color.cyan);
								error.setText("Click on the blue channel to activate DNF");
							}
							else if(channels[i].getBackground().equals(Color.cyan)){
								channels[i].setBackground(Color.YELLOW);
							}
						}
					break;
					}
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
				try {
					timer.getCurrentEvent().getHeats().get(timer.getCurrentEvent().getCurrentHeat()).swap();
				} catch (UserErrorException e1) {
					// TODO Auto-generated catch block
					error.setText(e1.getMessage());
				}
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
					for(int i = 0; i<channels.length; ++i){
						if(timer.getCurrentEvent().getType().equals(EventType.PARGRP)){
							start.setEnabled(true);
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
			try {
				addSensorPicture(channels[i], SensorType.NONE);
			} catch (UserErrorException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			channels[i].setName(String.valueOf(i+1));
			channels[i].addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JButton button = (JButton) e.getSource();
					if(timer != null && timer.getCurrentEvent() != null){
						if(button.getBackground().equals(Color.YELLOW)){
							try {
								timer.getCurrentEvent().triggerChannel(Integer.parseInt(button.getName()), true);
								canCancel = true;
								if(button.getName().equals("1") ||
										button.getName().equals("3") ||
										button.getName().equals("5") || 
										button.getName().equals("7")){
									disableSensorConnection();
								}
								else{ //it is a finish channel and yellow
									if(canEnableSensorConnection(timer.getCurrentEvent().getType())) enableSensorConnection();
								}
								if(timer.getCurrentEvent().getType().equals(EventType.PARGRP)){
									if(button.getName().equals("2") ||
											button.getName().equals("4") ||
											button.getName().equals("6") || 
											button.getName().equals("8")){
										if(canEnableSensorConnection(timer.getCurrentEvent().getType())) start.setEnabled(true);
									}
								}
							} catch (UserErrorException e1) {
								// TODO Auto-generated catch block
								enableSensorConnection();
								error.setText(e1.getMessage());
							}
						}
						if(button.getBackground().equals(Color.cyan)){
							try {
								timer.getCurrentEvent().triggerChannel(Integer.parseInt(button.getName()), false);
								if(canEnableSensorConnection(timer.getCurrentEvent().getType())) enableSensorConnection();
								if(timer.getCurrentEvent().getType().equals(EventType.PARGRP)){
									if(canEnableSensorConnection(timer.getCurrentEvent().getType())) start.setEnabled(true);
								}
							} catch (UserErrorException e1) {
								// TODO Auto-generated catch block
								error.setText(e1.getMessage());;
							}
						}
						if(button.getBackground().equals(Color.ORANGE)){
							try {
								int lane = 0;
								if(button.getName().equals("2")){lane = 1;}
								else if(button.getName().equals("4")){lane = 2;}
								else if(button.getName().equals("6")){lane = 3;}
								else if(button.getName().equals("8")){lane = 4;}
								timer.getCurrentEvent().cancel(timer.getCurrentEvent().getType(), lane);
								if(canEnableSensorConnection(timer.getCurrentEvent().getType())) enableSensorConnection();
							} catch (UserErrorException e1) {
								// TODO Auto-generated catch block
								error.setText(e1.getMessage());;
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
		//the following two lines of code will eliminate the jitter cause by updating the text every hundreth of a second
		DefaultCaret caret = (DefaultCaret) rightBottom.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		displayHolder = new JScrollPane(rightBottom, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		parentRight.add(displayHolder, gbc);
		
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
			setEnabledSelectedButtons(new JButton[]{channels[0], export, disconnect, clear, channels[1], cancel, swap, new_heat, add, printer, reset, 
					exit, create, start, finish, connect, dnf, manual}, true);
			setEnabledSelectedButtons(new JButton[]{channels[2], channels[3], channels[4], channels[5], channels[6], channels[7], manual}, false);
		break;
		
		case PARIND:
			setEnabledSelectedButtons(new JButton[]{export, clear, cancel, swap, new_heat, add, printer, reset, 
					exit, create, finish, dnf, connect, disconnect, manual}, true);
			for(JButton chan : channels){chan.setEnabled(true);}
			setEnabledSelectedButtons(new JButton[]{start, finish}, false);
		break;
		
		case GRP: 
			setEnabledSelectedButtons(new JButton[]{channels[0], export, clear, channels[1], cancel, swap, new_heat, add, printer, reset, dnf, 
					exit, create, start, finish, disconnect, connect, manual}, true);
			setEnabledSelectedButtons(new JButton[]{channels[2], channels[3], channels[4], channels[5], channels[6], channels[7], manual}, false);
		break;
		
		case PARGRP: 
			setEnabledSelectedButtons(new JButton[]{export, connect, dnf, clear, cancel, swap, new_heat, add, printer, reset, 
					exit, create, finish, disconnect, manual}, true);
			for(JButton chan : channels){chan.setEnabled(true);}
			setEnabledSelectedButtons(new JButton[]{ start, finish}, false);					break;
	}
	}
	public void changeButtonColor(JButton x){
		if(timer.getCurrentEvent().getType().equals(EventType.PARGRP)){
			if(manualModeEnabled){
				return;
			}
		}
		boolean change = true;
		for(Competitor y : timer.getCurrentEvent().getHeats().get(timer.getCurrentEvent().getCurrentHeat()).getRacers()){
			if(y.isCompeting() || manualModeEnabled){
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
	public void addSensorPicture(JButton x, SensorType y) throws UserErrorException{
		ImageIcon icon;
		if(x.isEnabled()){
			switch(y){
			case EYE:
				icon = new ImageIcon(getClass().getResource("/images/Eye_open_font_awesome.svg.png"));
				
				Image img = icon.getImage();  
				Image newimg = img.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH ) ;  
				icon = new ImageIcon(newimg);
				x.setHorizontalTextPosition(JButton.CENTER);
				x.setBorderPainted(false);
				x.setVerticalTextPosition(JButton.TOP);
				x.setIcon(icon);
			break;
			
			case GATE:
				icon = new ImageIcon(getClass().getResource("/images/wicket_gate.jpg"));
				Image img2 = icon.getImage();  
				Image newimg2 = img2.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH ) ;  
				icon = new ImageIcon(newimg2);
				x.setHorizontalTextPosition(JButton.CENTER);
				x.setVerticalTextPosition(JButton.TOP);
				x.setBorderPainted(false);
				x.setIcon(icon);
			break;
			
			case PAD:
				icon = new ImageIcon(getClass().getResource("/images/sponge.png"));
				Image img3 = icon.getImage();  
				Image newimg3 = img3.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH ) ;  
				icon = new ImageIcon(newimg3);
				x.setHorizontalTextPosition(JButton.CENTER);
				x.setVerticalTextPosition(JButton.TOP);
				x.setBorderPainted(false);
				x.setIcon(icon);
			break;
			
			case NONE:
				icon = new ImageIcon("/images/none.jpg.png");
				Image img4 = icon.getImage();  
				Image newimg4 = img4.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH ) ;  
				icon = new ImageIcon(newimg4);
				x.setHorizontalTextPosition(JButton.CENTER);
				x.setVerticalTextPosition(JButton.TOP);
				x.setBorderPainted(false);
				x.setIcon(icon);
			break;
			}
		}
		else if(!x.isEnabled() && y.equals(SensorType.NONE)){
			icon = new ImageIcon(getClass().getResource("/images/none.jpg.png"));
			Image img4 = icon.getImage();  
			Image newimg4 = img4.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH ) ;  
			icon = new ImageIcon(newimg4);
			x.setHorizontalTextPosition(JButton.CENTER);
			x.setVerticalTextPosition(JButton.TOP);
			x.setBorderPainted(false);
			x.setIcon(icon);
		}
		else if(!x.isEnabled())	throw new UserErrorException("this channel is not enabled");
	}
	public void updateDisplay(){
		rightBottom.setText(timer.getCurrentEvent().getDisplay().display());
	}
	public boolean canEnableSensorConnection(EventType x){
		boolean running = false;
		boolean toReturn = false;
		switch(x){
		case GRP:
			for(Competitor comp : timer.getCurrentEvent().getHeats().get(timer.getCurrentEvent().getCurrentHeat()).getRacers()){
				if(comp.isCompeting()){
					running = true; break;
				}
			}
			if(!running){toReturn = true;}
		break;
		
		case IND:
			for(Competitor comp : timer.getCurrentEvent().getHeats().get(timer.getCurrentEvent().getCurrentHeat()).getRacers()){
				if(comp.isCompeting()){
					running = true; break;
				}
			}
			if(!running){toReturn = true;}
		break;
		
		case PARGRP:
			for(Competitor comp : timer.getCurrentEvent().getHeats().get(timer.getCurrentEvent().getCurrentHeat()).getRacers()){
				if(comp.isCompeting()){
					running = true; break;
				}
			}
			if(!running){toReturn = true;}
		break;
		
		case PARIND:
			for(Competitor comp : timer.getCurrentEvent().getHeats().get(timer.getCurrentEvent().getCurrentHeat()).getRacers()){
				if(comp.isCompeting()){
					running = true; break;
				}
			}
			if(!running){toReturn = true;}
		break;
		}
		return toReturn;
	}
	public void enableSensorConnection(){
		connect.setEnabled(true);
		disconnect.setEnabled(true);
		sensors.setEnabled(true);
		connChan.setEnabled(true);
		discChan.setEnabled(true);
	}
	public void disableSensorConnection(){
		connect.setEnabled(false);
		disconnect.setEnabled(false);
		sensors.setEnabled(false);
		connChan.setEnabled(false);
		discChan.setEnabled(false);
	}
	public static void main(String[] args){
		new ChronoTimer1009GUI();
		timer = new ChronoTimer1009();
	}

}