package com.example.documentservice.exceptions;

public class FileException extends RuntimeException  {
    public FileException(String message, Exception e) {
        super(message, e);
    }
    public FileException(String message) {
        super(message);
    }
}
