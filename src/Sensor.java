public class Sensor {
	
	private SensorType type;
	
	public Sensor(SensorType type){
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public SensorType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(SensorType type) {
		this.type = type;
	}
	
}
