package com.tecnologo.grupo3.goandrent.dtos.push;

import lombok.Data;

@Data
public class Push {
    private String to;
    private String title;
    private String body;

    public Push() {
    }

    public Push(String to, String title, String body) {
        this.to = to;
        this.title = title;
        this.body = body;
    }
}
