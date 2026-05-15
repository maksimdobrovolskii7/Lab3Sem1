package com.jujutsu.archive.exception;

public class InvalidMissionFormatException extends Exception {
    public InvalidMissionFormatException(String message) {
        super(message);
    }
    public InvalidMissionFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}