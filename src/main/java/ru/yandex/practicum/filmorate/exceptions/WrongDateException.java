package ru.yandex.practicum.filmorate.exceptions;

public class WrongDateException extends RuntimeException {

    public WrongDateException(String message) {
        super(message);
    }
}