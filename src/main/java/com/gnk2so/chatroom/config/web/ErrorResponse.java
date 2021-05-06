package com.gnk2so.chatroom.config.web;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private Integer status;
    private String message;
    private List<ValidationError> errors;

    public ErrorResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

}
