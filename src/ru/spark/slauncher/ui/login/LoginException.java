package ru.spark.slauncher.ui.login;

public class LoginException extends RuntimeException {
	private static final long serialVersionUID = -1186718369369624107L;

	public LoginException(String reason) {
		super(reason);
	}

}
