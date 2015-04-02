public class Channel {
	
	private Sensor sensor;
	private boolean state;
	private boolean canTrigger;

	/**
	 * Constructor
	 * @param type the type of sensor for this channel
	 */
	public Channel(SensorType type){
		state = canTrigger = false; //channel initially disabled.
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
	public void disconnectSensor() throws UserErrorException{
		//disconnect whatever sensor is currently attached to the channel.
		if(this.sensor.getType().equals(SensorType.NONE)) throw new UserErrorException("no sensor to disconnect");
		this.sensor = new Sensor(SensorType.NONE);
	}
	/**
	 * Triggers the channel
	 */
	public Time triggerChannel(){
		if(!state) throw new IllegalStateException("You will never see this error in the GUI");
		return new Time(ChronoTimer1009.globalTime.getTime());
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
	/**
	 * @return the canTrigger
	 */
	public boolean isCanTrigger() {
		return canTrigger;
	}
	/**
	 * @param canTrigger the canTrigger to set
	 */
	public void setCanTrigger(boolean canTrigger) {
		this.canTrigger = canTrigger;
	}
}
