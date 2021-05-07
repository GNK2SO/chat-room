package com.gnk2so.chatroom.commons;

import java.net.URI;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class BaseController {
    
    protected URI getURI(String path) {
        return ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path(path)
            .build()
            .toUri();
    }

    /**
     * Return current logged user email 
     */
    protected String getPrincipalName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
