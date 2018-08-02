package com.tookitaki.cryptocurrency.bitcoin.domain;

import java.util.Map;

public class CustomErrorType {

	private Map<String, String> errorMessage;

	public Map<String, String> getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Map<String, String> errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public boolean isError() {
		return !this.errorMessage.isEmpty();
	}

	@Override
	public String toString() {
		return "CustomErrorType [errorMessage=" + errorMessage + "]";
	}

}
