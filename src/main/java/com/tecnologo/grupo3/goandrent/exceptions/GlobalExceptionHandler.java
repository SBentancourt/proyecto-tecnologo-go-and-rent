package com.tecnologo.grupo3.goandrent.exceptions;

import com.tecnologo.grupo3.goandrent.dtos.errors.ErrorDetail;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.ParseException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // -- ******************************* -- //
    // -- Manejo de excepciones de Spring -- //
    // -- ******************************* -- //

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> globalExceptionHandler(Exception excepcion, WebRequest webRequest){
        ErrorDetail detalleDeError = new ErrorDetail(excepcion.getMessage());
        return new ResponseEntity<>(detalleDeError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDetail> authenticationExceptionHandler(AuthenticationException excepcion, WebRequest webRequest){
        ErrorDetail detalleDeError = new ErrorDetail("Los datos ingresados no son correctos.");
        return new ResponseEntity<>(detalleDeError, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorDetail> ExpiredJwtExceptionHandler(ExpiredJwtException exception, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail(exception.getMessage());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ErrorDetail> ExpiredJwtExceptionHandler(ParseException exception, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail("El formato de la fecha indicado no es correcto.");
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetail> ExpiredJwtExceptionHandler(AccessDeniedException exception, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail("Usuario no habilitado para realizar esta acción.");
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }



    // -- ***************************** -- //
    // -- Manejo de excepciones creadas -- //
    // -- ***************************** -- //

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetail> resourceNotFoundExceptionHandler(ResourceNotFoundException exception, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail(exception.getMessage());
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetail> userExceptionHandler(UserException exception, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail(exception.getMensaje());
        return new ResponseEntity<>(errorDetail, exception.getEstado());
    }

    @ExceptionHandler(AccommodationException.class)
    public ResponseEntity<ErrorDetail> accommodationExceptionHandler(AccommodationException exception, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail(exception.getMensaje());
        return new ResponseEntity<>(errorDetail, exception.getEstado());
    }

    @ExceptionHandler(RecoveryPasswordException.class)
    public ResponseEntity<ErrorDetail> recoveryPasswordExceptionHandler(RecoveryPasswordException exception, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail(exception.getMensaje());
        return new ResponseEntity<>(errorDetail, exception.getEstado());
    }
    @ExceptionHandler(UserInvalidException.class)
    public ResponseEntity<ErrorDetail> userInvalidExceptionHandler(UserInvalidException exception, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail(exception.getMensaje());
        return new ResponseEntity<>(errorDetail, exception.getEstado());
    }



}
