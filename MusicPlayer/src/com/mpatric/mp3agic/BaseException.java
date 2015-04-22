package com.mpatric.mp3agic;

public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BaseException() {
		super();
	}
	
	public BaseException(String message) {
		super(message);
	}
	
	public String getDetailedMessage() {
		Throwable t = this;
		StringBuffer s = new StringBuffer();
		while (true) {
			s.append('[');
			s.append(t.getClass().getName());
			if (t.getMessage() != null && t.getMessage().length() > 0) {
				s.append(": ");
				s.append(t.getMessage());
			}
			s.append(']');
			t = null;//t.getCause();
			if (t != null) {
				s.append(" caused by ");
			} else {
				break;
			}
		} 
		return s.toString();
	}
}
