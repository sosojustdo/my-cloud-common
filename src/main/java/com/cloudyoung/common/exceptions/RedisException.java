package com.cloudyoung.common.exceptions;

public class RedisException extends RuntimeException {

	private static final long serialVersionUID = -7360024709594578218L;

	public RedisException() {
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisException(String message) {
        super(message);
    }

}