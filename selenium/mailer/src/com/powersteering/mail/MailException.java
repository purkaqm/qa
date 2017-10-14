package com.powersteering.mail;

public class MailException extends Exception {

	private static final long serialVersionUID = 1L;

	public MailException(String message, Throwable target){
		super(message,target);
	}

	public MailException(String message){
		super(message);
	}

	public MailException(Throwable target){
		super(target);
	}

	public MailException(){
		super();
	}
}
