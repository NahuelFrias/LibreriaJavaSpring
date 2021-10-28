package com.libreriaSpring.ej1Spring.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author leonahuel
 */
@Controller("")
@RequestMapping("")
public class PortalControlador {
    
    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    
}
