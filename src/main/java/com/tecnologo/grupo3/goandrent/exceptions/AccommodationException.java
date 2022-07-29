package com.tecnologo.grupo3.goandrent.exceptions;

import org.springframework.http.HttpStatus;

public class AccommodationException extends RuntimeException{
    private static final long serialVersionUID = 1l;

    private HttpStatus estado;
    private String mensaje;

    public AccommodationException(HttpStatus estado, String mensaje){
        super();
        this.estado = estado;
        this.mensaje = mensaje;
    }

    public AccommodationException(HttpStatus estado, String mensaje, String mensaje1){
        super();
        this.estado = estado;
        this.mensaje = mensaje;
        this.mensaje = mensaje1;
    }

    public HttpStatus getEstado() {
        return estado;
    }

    public void setEstado(HttpStatus estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
