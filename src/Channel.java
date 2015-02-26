public class Channel {
	
	private Sensor sensor;
	private boolean state;

	/**
	 * Constructor
	 * @param type the type of sensor for this channel
	 */
	public Channel(SensorType type){
		state = false; //channel initially disabled.
		this.sensor = new Sensor(type);
	}
	
	/**
	 * Changes the state of the channel
	 */
	public void toggleState(){
		//if on then off
		//if off then on
		state = !state; 
	}
	/**
	 * Connects a certain sensor to a channel
	 * @param type the type of sensor
	 */
	public void connectSensor(SensorType type){
		this.sensor = new Sensor(type); //connect the new sensor.
	}
	
	/**
	 * Disconnects a sensor from a channel
	 */
	public void disconnectSensor(){
		//disconnect whatever sensor is currently attactched to the channel.
		this.sensor = new Sensor(SensorType.NONE);
	}
	
	/**
	 * Triggers the channel
	 */
	public Time triggerChannel(){
		//long millis = ChronoTimer1009.getTime().getTime();
		//return new Time(millis);
		return ChronoTimer1009.globalTime;
	}
	/**
	 * @return the type
	 */
	public Sensor getSensor() {
		return sensor;
	}

	/**
	 * @return the state
	 */
	public boolean getState() {
		return state;
	}
}
