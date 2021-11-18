package com.libreriaSpring.ej1Spring.controladores;

import com.libreriaSpring.ej1Spring.entidades.Autor;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.servicios.AutorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author leonahuel
 */
@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
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

    //modico en base al id
    @GetMapping("/modificarAutor/{id}")
    //el PathVariable es una variable de que viaja en nuestra url
    //en este caso el id
    //Model Map para inyectar cosas al html
    public String modificar(@PathVariable String id, ModelMap modelo) throws ErrorServicio {

        //busco al autor por id y lo guardo en "autor"
        modelo.put("autor", autorServicio.buscarPorId(id));

        return "modificarAutor";
    }

    @PostMapping("/modificarAutor/{id}")
    public String modificar(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {

        try {
            autorServicio.modificarAutor(id, nombre);
            List<Autor> autores = autorServicio.listarAutores();
            modelo.put("autores", autores);
            modelo.put("exito", "Modificacion exitosa");
            return "listaAutores.html";
        } catch (ErrorServicio ex) {
            modelo.put("error", "Falto algun dato");
            List<Autor> autores = autorServicio.listarAutores();
            modelo.put("autores", autores);
            return "listaAutores.html";
        }
    }

    @GetMapping("/altaAutor/{id}")
    public String alta(ModelMap modelo, @PathVariable String id) {
        try {
            autorServicio.alta(id);
            modelo.put("exito", "Modificacion exitosa");
            return "redirect:/autor/listaAutores";
        } catch (ErrorServicio ex) {
            return "redirect:/";
        }
    }

    @GetMapping("/bajaAutor/{id}")
    public String baja(ModelMap modelo, @PathVariable String id) {
        try {
            autorServicio.baja(id);
            return "redirect:/autor/listaAutores";
        } catch (ErrorServicio ex) {
            return "redirect:/";
        }
    }
}
