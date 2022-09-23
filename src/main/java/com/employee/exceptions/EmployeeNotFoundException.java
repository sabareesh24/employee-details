package com.employee.exceptions;

public class EmployeeNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private String errorCode;
	
	public EmployeeNotFoundException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		return this.errorCode+" : "+super.getMessage();
	}
}
