package com.libreriaSpring.ej1Spring.controladores;

import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.servicios.ClienteServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
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
@Controller("")
@RequestMapping("/")
/*url que escucha este controlador!
cuando entra a la barra de mi servidor ejecuta todos estos metodos
y renderiza el html*/
public class PortalControlador {

    @Autowired
    private ClienteServicio clienteServicio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro.html";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo,
            @RequestParam @Nullable String nombre,
            @RequestParam @Nullable String apellido,
            @RequestParam @Nullable Long documento,
            @RequestParam @Nullable String mail,
            @RequestParam @Nullable String clave1,
            @RequestParam @Nullable String clave2) {
        try {
            clienteServicio.registrar(nombre, apellido, documento, mail, clave1);
            modelo.put("titulo", "Bienvenido a la Libreria!");
            modelo.put("descripcion", "El cliente fue registrado con exito!");
            return "inicio.html";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("documento", documento);
            modelo.put("mail", mail);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            return "registro.html";
        }

    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/cargas")
    public String cargas() {
        return "cargas.html";
    }

    @GetMapping("/listas")
    public String listas() {
        return "listas.html";
    }

}
