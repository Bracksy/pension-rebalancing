package ch.immi.pension.exception;

import java.io.Serial;

public class DatabaseException extends Exception {
	@Serial
    private static final long serialVersionUID = -3666083311495587677L;

	public DatabaseException(String message, String detail) {
		super(message);
	}

	public DatabaseException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
