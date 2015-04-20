package main;
public class UserErrorException extends Exception {
	
	private String message;
	
	public UserErrorException(){
		super();
		message = "";
	}
	public UserErrorException(String message){
		super(message);
		this.message = message;
	}
}
