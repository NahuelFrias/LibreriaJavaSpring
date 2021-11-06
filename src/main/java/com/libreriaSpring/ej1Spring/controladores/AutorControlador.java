package com.libreriaSpring.ej1Spring.controladores;

import com.libreriaSpring.ej1Spring.entidades.Autor;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.servicios.AutorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author leonahuel
 */
@Controller
@RequestMapping("/autor")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/cargarAutor")
    public String cargarAutor() {
        return "cargarAutor.html";
    }

    @PostMapping("/registrandoAutor")
    public String registrandoAutor(ModelMap modelo, @RequestParam String nombre) {
        try {
            autorServicio.registrarAutor(nombre);
            modelo.put("exito", "El Autor fue registrado con exito!");
            return "cargarAutor.html";

        } catch (ErrorServicio ex) {
            modelo.put("error", "Falto algun dato.");
            modelo.put("nombre", nombre);
            return "cargarAutor.html";
        }

    }

    @GetMapping("/listaAutores")
    public String listaAutores(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        modelo.put("autores", autores);

        return "listaAutores.html";
    }

}