package com.libreriaSpring.ej1Spring.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author leonahuel
 */
@Controller("")
@RequestMapping("/")
 /*url que escucha este controlador!
cuando entra a la barra de mi servidor ejecuta todos estos metodos
y renderiza el html*/
public class PortalControlador {
    
    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    
    @GetMapping("/listaAutores")
    public String listaAutores() {
        return "listaAutores.html";
    }
    
}
