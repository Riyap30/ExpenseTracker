package com.example.demo.exceptions;

public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException(String message) {
        super(message);
    }

    public UsernameNotFoundException(){
        super();
    }
}
