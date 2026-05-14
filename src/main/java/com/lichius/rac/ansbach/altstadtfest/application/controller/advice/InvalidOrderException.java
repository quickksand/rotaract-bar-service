package com.lichius.rac.ansbach.altstadtfest.application.controller.advice;

public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }
}