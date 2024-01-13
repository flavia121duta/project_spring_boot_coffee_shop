package com.api.project.exception;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class ErrorResponse {
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public int getStatus() {
        return status;
    }

    public ErrorResponse() {}

    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
//        this.timestamp = LocalDateTime.parse(DATE_FORMAT.format(timestamp));;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
//        this.timestamp = LocalDateTime.parse(DATE_FORMAT.format(timestamp));
    }
}
