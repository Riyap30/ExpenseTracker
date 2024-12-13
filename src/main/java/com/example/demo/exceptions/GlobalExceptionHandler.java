package com.example.demo.exceptions;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception ex) {
        ProblemDetail errorDetail = null;

        ex.printStackTrace();

        if (ex instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(401),
                ex.getMessage()
            );
            errorDetail.setProperty("description", "Invalid username or password");

            return errorDetail;
        }

        if (ex instanceof AccountStatusException) {
            errorDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(401),
                ex.getMessage()
            );

            errorDetail.setProperty("description", "Account is not active");

            return errorDetail;
        }

        if (ex instanceof AccessDeniedException){
            errorDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(403),
                ex.getMessage()
            );

            errorDetail.setProperty("description", "Access denied");
        }

        if (ex instanceof SignatureException){
            errorDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(401),
                ex.getMessage()
            );

            errorDetail.setProperty("description", "Invalid token");

            return errorDetail;
        }

        if (ex instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(401),
                ex.getMessage()
            );

            errorDetail.setProperty("description", "Token expired");

            return errorDetail;
        }

        if (errorDetail == null) {
            errorDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(500),
                ex.getMessage()
            );

            errorDetail.setProperty("description", "Internal server error");
        }

        return errorDetail;

    }
}
