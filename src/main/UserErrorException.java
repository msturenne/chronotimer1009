package main;
public class UserErrorException extends Exception {
	private static final long serialVersionUID = 1L;
	public UserErrorException(){
		super();
	}
	public UserErrorException(String message){
		super(message);
	}
}
