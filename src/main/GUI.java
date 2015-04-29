package main;
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

@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener{
	//ChronoTimer1009System
	private static ChronoTimer1009System timer;
	//JPanel
	private JPanel parentPanel, parentLeft, leftTop, leftBottom, parentMiddle, middleGridParent, optionsGrid, channelsGrid, parentRight, rightTop;
	//JLabel
	private JLabel middleTop, connSensor, discSensor;
	//JButton
	private final JButton channels[], keys[], connect, disconnect, exit, power, printer, reset, newHeat, start, finish, cancel, dnf, swap, manual, clear, add, create;
	//JComboBox
	private final JComboBox<String> connChan, discChan;
	private final JComboBox<SensorType> sensors;
	private final JComboBox<EventType> eventTypes;
	//JTextArea
	private JTextArea console;
	//JScrollPane
	private JScrollPane consoleScrollPane;
	//Timer
	private Timer clockTimer;
	//JTextField
	private final JTextField idNum;
	//other
	private final GridBagConstraints gbc;
	private boolean manualModeEnabled, canCancel;
	private String idNumText = "";
	
	public GUI() throws UserErrorException{
		//initialize ChronoTimer1009system
		timer = new ChronoTimer1009System();
		//initialize & setup JPanel
		parentPanel = new JPanel(new GridLayout(1,3));
		parentLeft = new JPanel(new GridBagLayout());
		leftTop = new JPanel(new GridBagLayout());
		leftBottom = new JPanel(new GridLayout(4,3));
		parentMiddle = new JPanel(new GridBagLayout());
		middleGridParent = new JPanel(new GridLayout(2,1,0,25));
		optionsGrid = new JPanel(new GridLayout(5,2));
		channelsGrid = new JPanel(new GridLayout(2,4,5,5));
		parentRight = new JPanel(new GridBagLayout());
		rightTop = new JPanel(new GridLayout(2,2));
		setupJPanels();
		//initialize & setup JLabel
		connSensor = new JLabel("Conn Sensor");
		middleTop = new JLabel("ChronoTimer1009");
		discSensor = new JLabel("Disc Sensor");
		setupJLabels();
		//initialize & setup JButton
		keys = new JButton[10]; for(int i=0;i<keys.length;++i){keys[i] = new JButton(String.valueOf(i+1));keys[i].setName(String.valueOf(i+1));}
		channels = new JButton[8]; for(int i = 0; i<channels.length; ++i){channels[i] = new JButton("CHAN" + (i+1));}
		connect = new JButton("Connect");
		disconnect = new JButton("Disconnect");
		exit = new JButton("exit");
		power = new JButton("ON");
		printer = new JButton("printer");
		reset = new JButton("reset");
		newHeat = new JButton("New Heat");
		start = new JButton("start");
		finish = new JButton("Finish");
		cancel = new JButton("Cancel");
		dnf = new JButton("DNF");
		swap = new JButton("Swap");
		manual = new JButton("Manual");
		clear = new JButton("Clear");
		add = new JButton("add");
		create = new JButton("create");
		addJButtonActionListeners(new JButton[]{connect, disconnect, exit, power, printer, reset, newHeat, start, finish, cancel, dnf, swap, manual, clear, /*export,*/ add, create});
		addJButtonActionListeners(keys);
		addJButtonActionListeners(channels);
		setupChannels();
		//initialize JComboBox
		connChan = new JComboBox<String>(new String[]{"Chan 1", "Chan 2", "Chan 3", "Chan 4", "Chan 5", "Chan 6", "Chan 7", "Chan 8"});
		sensors = new JComboBox<SensorType>(new SensorType[]{SensorType.EYE, SensorType.GATE, SensorType.PAD});
		eventTypes = new JComboBox<EventType>(EventType.values());
		discChan = new JComboBox<String>(new String[]{"Chan 1", "Chan 2", "Chan 3", "Chan 4", "Chan 5", "Chan 6", "Chan 7", "Chan 8"});
		//initialize JTextArea
		console = new JTextArea(15,15);
		//initialize JScrollPane
		consoleScrollPane = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//initialize JTextField
		idNum = new JTextField("Competitor");
		//initialize Timer
		clockTimer = new Timer(10, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//update the display every hundreth of a second
				updateDisplay();
			}
		});
		//initialize other
		manualModeEnabled = canCancel = false;
		gbc = new GridBagConstraints();
		
		//CREATE GUI
		this.setTitle("ChronoTimer1009");
		this.setSize(1200, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for(int i=0;i<keys.length;++i){leftBottom.add(keys[i]);}
		//start off the system by disabling all button except the power
		setEnabledSelectedJComponents(keys, false);
		setEnabledSelectedJComponents(new JComponent[]{exit, printer, reset, newHeat, start, finish,
				cancel, dnf, swap, manual, clear, connect, disconnect, add, create, eventTypes, discChan, idNum, sensors, connChan}, false);
		gbc.gridx = gbc.gridy = 0;
		leftTop.add(new JLabel("New Comp"), gbc);
		++gbc.gridx;
		leftTop.add(idNum, gbc);
		idNum.setEditable(false);
		++gbc.gridx;
		leftTop.add(add, gbc);
		gbc.gridx = 0; gbc.gridy = 1;
		leftTop.add(new JLabel("New Event"), gbc);
		++gbc.gridx;
		leftTop.add(eventTypes, gbc);
		++gbc.gridx;
		leftTop.add(create, gbc);
		gbc.gridx = 0; gbc.gridy = 2;
		leftTop.add(connSensor, gbc);
		++gbc.gridx;
		leftTop.add(connChan, gbc);
		++gbc.gridx;
		leftTop.add(sensors, gbc);
		++gbc.gridx;
		leftTop.add(connect, gbc);
		++gbc.gridx;
		gbc.gridx = 0; gbc.gridy = 3;
		leftTop.add(discSensor, gbc);
		++gbc.gridx;
		leftTop.add(discChan, gbc);
		++gbc.gridx;
		leftTop.add(disconnect, gbc);
		gbc.gridx+=2;
		gbc.gridx = gbc.gridy = 0;
		rightTop.add(printer);
		rightTop.add(power);
		rightTop.add(exit);
		rightTop.add(reset);
		console.setEditable(false);
		optionsGrid.add(newHeat);
		optionsGrid.add(clear);
		optionsGrid.add(start);
		optionsGrid.add(finish);
		optionsGrid.add(cancel);
		optionsGrid.add(dnf);
		optionsGrid.add(swap);
		optionsGrid.add(manual);
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
		gbc.gridheight = 2;
		//the following two lines of code will eliminate the jitter cause by updating the text every hundreth of a second
		DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		parentRight.add(consoleScrollPane, gbc);
		this.add(parentPanel);
		this.setVisible(true);
	}
	//setup JPanel methods
	public void setupJPanels(){setBackground(new JPanel[]{parentLeft, leftTop, leftBottom, parentMiddle, middleGridParent, optionsGrid, channelsGrid, parentRight, rightTop});}
	public void setBackground(JPanel[] x){for(JPanel y : x)y.setBackground(Color.DARK_GRAY);}
	//setup JLabel methods
	public void setupJLabels(){middleTop.setFont(new Font("Bazooka", Font.PLAIN, 36));middleTop.setForeground(Color.WHITE);}
	//setup Channels
	public void setupChannels(){
		for(int i = 0; i<channels.length; ++i){
			channels[i].setEnabled(false);
			setColorRed(channels[i]);
			try {
				addSensorPicture(channels[i], SensorType.NONE);
			} catch (UserErrorException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			channels[i].setName(String.valueOf(i+1));
		}
	}
	//add JButton action listeners
	public void addJButtonActionListeners(JButton[] x){for(JButton y : x)y.addActionListener(this);}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JComponent btn = (JComponent) e.getSource();
		if(btn.equals(add))doAdd();
		if(btn.equals(create))doCreate();
		if(btn.equals(connect))doConnSensor();
		if(btn.equals(disconnect))doDiscSensor();
		if(btn.equals(newHeat))doNewHeat();
		if(btn.equals(clear))doClear();
		if(btn.equals(start))doStart();
		if(btn.equals(finish))doFinish();
		if(btn.equals(cancel))doCancel();
		if(btn.equals(dnf))doDNF();
		if(btn.equals(swap))doSwap();
		if(btn.equals(manual))doManual();
		if(btn.equals(power))doPower();
		if(btn.equals(exit))doExit();
		if(btn.equals(reset))doReset();
		if(btn.equals(printer))doPrinter();
		for(JButton x : keys){if(btn.equals(x)) doKey((JButton)btn);}
		for(JButton x : channels){if(btn.equals(x)) doChannel((JButton)btn);}
	}
	
	public void doPower(){
		power.setEnabled(false); //disable the power button after it is clicked as the system can only be turned on, not off
		if(timer.isState()) throw new IllegalStateException("The system should not be off");
		try {
			timer.on();
		} catch (UserErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clockTimer.start();
		idNum.setText("Competitor");
		setEnabledSelectedJComponents(keys, true);
		setEnabledSelectedJComponents(new JComponent[]{idNum, discChan, sensors, connChan, eventTypes}, true);
		setupEventTypeEnabledButtons(timer.getCurEvent().getType());
	}
	
	public void doExit(){
		timer.exit();
		clockTimer.stop();
		setEnabledSelectedJComponents(keys,false);
		setEnabledSelectedJComponents(channels,false);
		setEnabledSelectedJComponents(new JComponent[]{exit, printer, reset, newHeat, start, finish,
				cancel, dnf, swap, manual, clear, connect, disconnect, add, create, eventTypes, connChan, discChan, discChan, sensors, idNum,}, false);
		power.setEnabled(true);
		for(JButton x : channels){setColorRed(x);}
		idNum.setText("Competitor");
		console.setText("");
	}
	
	public void doReset(){
		try {
			timer.reset();
		} catch (UserErrorException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		clockTimer.restart();
		for(JButton x : channels){setColorRed(x); try {
			addSensorPicture(x, SensorType.NONE);
			if(!ChronoTimer1009System.getChan(Integer.parseInt(x.getName())).getSensor().getType().equals(SensorType.NONE)) ChronoTimer1009System.getChan(Integer.parseInt(x.getName())).disconnectSensor();
		} catch (UserErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		setEnabledSelectedJComponents(new JComponent[]{connect, connChan, sensors, disconnect, discChan}, true);
		idNum.setText("Competitor");
	}
	
	public void doPrinter(){
		if(timer != null && timer.getCurEvent() != null){ChronoTimer1009System.getPrinter().toggleState();}
	}
	
	public void resetChannels(){
		for(JButton x : channels){
			x.setBackground(Color.RED);
			try {
				addSensorPicture(x, SensorType.NONE);
			} catch (UserErrorException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void doNewHeat(){
		try {
			timer.getCurEvent().endRun();
			timer.getCurEvent().createRun();
		} catch (UserErrorException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		setEnabledSelectedJComponents(new JComponent[]{sensors, connect, connChan, disconnect, discChan}, true);
	}
	
	public void doClear(){
		try {
			timer.getCurEvent().getHeats().get(timer.getCurEvent().getCurHeat()).clearNextCompetitor();
		} catch (UserErrorException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void doStart(){
		try {
			if(timer.getCurEvent().getType().equals(EventType.PARGRP)){
				//first check for sensors enabled
				for(JButton x : channels){
					if(x.getBackground().equals(Color.GREEN)){
						if(!ChronoTimer1009System.getChan(Integer.parseInt(x.getName())).getState() || !ChronoTimer1009System.getChan(Integer.parseInt(x.getName())+1).getState()){
							start.setEnabled(true);
							throw new UserErrorException("Please connect all sensors for all channels used");
						}
					}
				}
				//then trigger
				for(JButton x : channels){
					if(x.getBackground().equals(Color.GREEN)){
						ChronoTimer1009System.getChan(Integer.parseInt(x.getName())+1).setCanTrigger(true);
						timer.getCurEvent().trigChan(Integer.parseInt(x.getName()), true);
					}
				}
			}else{ //IND OR GRP
				timer.getCurEvent().trigChan(1, true);
			}
			canCancel = true;
			cancel.setEnabled(true);
			disableSensorConnection();
		} catch (UserErrorException e1) {
			// TODO Auto-generated catch block
			if(canEnableSensorConnection()){
				enableSensorConnection();
			}
			JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void doFinish(){
		try {
			timer.getCurEvent().trigChan(2, true);
			cancel.setEnabled(false);
			if(canEnableSensorConnection()){enableSensorConnection();}
		} catch (UserErrorException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doCancel(){
		try {
			if(timer.getCurEvent().getType().equals(EventType.PARIND)){
				for(int i = 1; i<channels.length; i+=2){
					if(channels[i].getBackground().equals(Color.YELLOW)){
						channels[i].setBackground(Color.ORANGE);
						JOptionPane.showMessageDialog(this, "Click on the orange channel to activate cancel", "WARNING", JOptionPane.WARNING_MESSAGE);
					}
					else if(channels[i].getBackground().equals(Color.ORANGE)){
						channels[i].setBackground(Color.YELLOW);
					}
				}
			}
			else if(canCancel){
				timer.getCurEvent().cancel(1);
			}
			else{
				JOptionPane.showMessageDialog(this, "You can only cancel the current competitor!", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			canCancel = false;
			EventType thisEvent = timer.getCurEvent().getType();
			if(thisEvent.equals(EventType.IND) || thisEvent.equals(EventType.GRP)){
				if(canEnableSensorConnection()){enableSensorConnection();}
			}
			if(thisEvent.equals(EventType.PARGRP)){
				start.setEnabled(true);
				enableSensorConnection();
			}
		} catch (UserErrorException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void doDNF(){
		try {
			if(timer.getCurEvent().getType().equals(EventType.GRP) || timer.getCurEvent().getType().equals(EventType.IND)){
				timer.getCurEvent().trigChan(2, false);
				if(canEnableSensorConnection()) enableSensorConnection();
			}
			else{
				for(int i = 1; i<channels.length; i+=2){
					if(channels[i].getBackground().equals(Color.YELLOW)){
						channels[i].setBackground(Color.cyan);
						JOptionPane.showMessageDialog(this, "Click on the blue channel to activate DNF", "WARNING", JOptionPane.WARNING_MESSAGE);
					}
					else if(channels[i].getBackground().equals(Color.cyan)){
						channels[i].setBackground(Color.YELLOW);
					}
				}
			}
		} catch (UserErrorException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doSwap(){
		try {
			timer.getCurEvent().getHeats().get(timer.getCurEvent().getCurHeat()).swap();
		} catch (UserErrorException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void doManual(){
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
				if(timer.getCurEvent().getType().equals(EventType.PARGRP)){
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
	
	public void doKey(JButton btn){idNum.setText(idNumText += btn.getName());}
	
	public void doAdd(){
		int toDisplay = 0;
		if(idNum.getText().matches("^([0-9][0-9]{0,4}|99999)$")){
			toDisplay = Integer.parseInt(idNum.getText());
			int currentHeat = timer.getCurEvent().getCurHeat();
			boolean added = false;
			try {
				added = timer.getCurEvent().getHeats().get(currentHeat).addCompetitor(new Competitor(currentHeat, toDisplay, new Time(ChronoTimer1009System.globalTime.getTime())));
				idNum.setText("Competitor");
				idNumText = "";
			} catch (UserErrorException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			if(added){updateDisplay();}
		}
		else{
			JOptionPane.showMessageDialog(this, "Not a valid competitor number!", "ERROR", JOptionPane.ERROR_MESSAGE);
			idNum.setText("Not Valid");
		}
	}
	
	public void doCreate(){
		int result = JOptionPane.showConfirmDialog(this, "Are you sure you would like to create an event?", "Create Event", JOptionPane.WARNING_MESSAGE);
		if(result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) return;
		EventType x = (EventType) eventTypes.getSelectedItem();
		try {
			timer.newEvent(x);
		} catch (UserErrorException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		setupEventTypeEnabledButtons(x);
		for(JButton y : channels){
			y.setBackground(Color.RED);
		}
		setEnabledSelectedJComponents(new JComponent[]{sensors, discChan, connChan}, true);
		try {
			timer.getCurEvent().endRun();
		} catch (UserErrorException e1) {
			// TODO Auto-generated catch block
			//doNothing
		}
		manualModeEnabled = false;
		updateDisplay();
	}
	
	public void doConnSensor(){
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
			ChronoTimer1009System.getChan(parameter).connectSensor(selectedSensor);
		} catch (UserErrorException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void doDiscSensor(){
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
			ChronoTimer1009System.getChan(parameter).disconnectSensor();
		} catch (UserErrorException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void doChannel(JButton button){
		if(button.getBackground().equals(Color.YELLOW)){
			try {
				timer.getCurEvent().trigChan(Integer.parseInt(button.getName()), true);
				canCancel = true;
				if(button.getName().equals("1") ||
						button.getName().equals("3") ||
						button.getName().equals("5") || 
						button.getName().equals("7")){
					disableSensorConnection();
				}
				else{ //it is a finish channel and yellow
					if(canEnableSensorConnection()) enableSensorConnection();
				}
				if(timer.getCurEvent().getType().equals(EventType.PARGRP)){
					if(button.getName().equals("2") ||
							button.getName().equals("4") ||
							button.getName().equals("6") || 
							button.getName().equals("8")){
						if(canEnableSensorConnection())start.setEnabled(true);
					}
				}
			} catch (UserErrorException e1) {
				// TODO Auto-generated catch block
				enableSensorConnection();
				JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		if(button.getBackground().equals(Color.cyan)){
			try {
				timer.getCurEvent().trigChan(Integer.parseInt(button.getName()), false);
				if(canEnableSensorConnection()) enableSensorConnection();
				if(timer.getCurEvent().getType().equals(EventType.PARGRP)){
					if(canEnableSensorConnection()) start.setEnabled(true);
				}
			} catch (UserErrorException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		if(button.getBackground().equals(Color.ORANGE)){
			try {
				timer.getCurEvent().cancel((int)(Math.ceil(Integer.parseInt(button.getName())/2.0)));
				if(canEnableSensorConnection()) enableSensorConnection();
			} catch (UserErrorException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		try {
			changeButtonColor(button);
		} catch (UserErrorException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setColorRed(JButton x){
		x.setBackground(Color.RED);
		x.setBorder(null);
		x.setOpaque(true);
	}
	/**
	 * Enable selected JComponents
	 * @param x the buttons to enable
	 */
	public void setEnabledSelectedJComponents(JComponent[] x, boolean z){
		for(JComponent y : x){
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
			setEnabledSelectedJComponents(new JButton[]{channels[0], /*export,*/ disconnect, clear, channels[1], cancel, swap, newHeat, add, printer, reset, 
					exit, create, start, finish, connect, dnf, manual}, true);
			setEnabledSelectedJComponents(new JButton[]{channels[2], channels[3], channels[4], channels[5], channels[6], channels[7], manual}, false);
		break;
		
		case PARIND:
			setEnabledSelectedJComponents(new JButton[]{/*export,*/ clear, cancel, swap, newHeat, add, printer, reset, 
					exit, create, finish, dnf, connect, disconnect, manual}, true);
			for(JButton chan : channels){chan.setEnabled(true);}
			setEnabledSelectedJComponents(new JButton[]{start, finish}, false);
		break;
		
		case GRP: 
			setEnabledSelectedJComponents(new JButton[]{channels[0], /*export,*/ clear, channels[1], cancel, swap, newHeat, add, printer, reset, dnf, 
					exit, create, start, finish, disconnect, connect, manual}, true);
			setEnabledSelectedJComponents(new JButton[]{channels[2], channels[3], channels[4], channels[5], channels[6], channels[7], manual}, false);
		break;
		
		case PARGRP: 
			setEnabledSelectedJComponents(new JButton[]{/*export,*/ connect, dnf, clear, cancel, swap, newHeat, add, printer, reset, 
					exit, create, finish, disconnect, manual}, true);
			for(JButton chan : channels){chan.setEnabled(true);}
			setEnabledSelectedJComponents(new JButton[]{ start, finish}, false);					break;
	}
	}
	
	public void changeButtonColor(JButton x) throws NumberFormatException, UserErrorException{
		if(timer.getCurEvent().getType().equals(EventType.PARGRP)){
			if(manualModeEnabled){
				return;
			}
		}
		boolean change = true;
		for(Competitor y : timer.getCurEvent().getHeats().get(timer.getCurEvent().getCurHeat()).getRacers()){
			if(y.isCompeting() || manualModeEnabled){
				change = false; 
				break;
			}
		}
		if(change){
			if(!x.getBackground().equals(Color.YELLOW)){
				if(x.getBackground().equals(Color.RED)){
					if(ChronoTimer1009System.getChan(Integer.parseInt(x.getName())).getSensor().getType().equals(SensorType.NONE))
						throw new UserErrorException("Your must connect a sensor before enabling the sensor!");
					else x.setBackground(Color.GREEN);
				}
				else x.setBackground(Color.RED);
				ChronoTimer1009System.getChan(Integer.parseInt(x.getName())).toggleState();
			}
		}
	}
	
	public void doAddPicture(String path, JButton btn){
		ImageIcon icon = new ImageIcon(getClass().getResource(path));
		Image img = icon.getImage();  
		Image newimg = img.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH ) ;  
		icon = new ImageIcon(newimg);
		btn.setHorizontalTextPosition(JButton.CENTER);
		btn.setBorderPainted(false);
		btn.setVerticalTextPosition(JButton.TOP);
		btn.setIcon(icon);
	}
	
	public void addSensorPicture(JButton x, SensorType y) throws UserErrorException{
		if(x.isEnabled()){
			switch(y){
			case EYE:
				doAddPicture("/images/Eye_open_font_awesome.svg.png", x);
			break;
			
			case GATE:
				doAddPicture("/images/wicket_gate.jpg", x);
			break;
			
			case PAD:
				doAddPicture("/images/sponge.png", x);
			break;
			
			case NONE:
				doAddPicture("/images/none.jpg.png", x);
			break;
			}
		}
		else if(!x.isEnabled() && y.equals(SensorType.NONE)){
			doAddPicture("/images/none.jpg.png", x);
		}
		else if(!x.isEnabled())	throw new UserErrorException("this channel is not enabled");
	}
	
	public void updateDisplay(){
		console.setText(timer.getCurEvent().updateConsole());
	}
	
	public boolean canEnableSensorConnection(){
		boolean running = false;
		for(Competitor comp : timer.getCurEvent().getHeats().get(timer.getCurEvent().getCurHeat()).getRacers()){
			if(comp.isCompeting()){
				running = true; break;
			}
		}
		return !running;
	}
	
	public void enableSensorConnection(){
		setEnabledSelectedJComponents(new JComponent[]{connect, disconnect, sensors, connChan, discChan}, true);
	}
	public void disableSensorConnection(){
		setEnabledSelectedJComponents(new JComponent[]{connect, disconnect, sensors, connChan, discChan}, false);
	}
	public static void main(String[] args) throws UserErrorException{
		new GUI();
	}
}