package com.libreriaSpring.ej1Spring.controladores;

import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    //SprignSecurity debe autorizar a aingresar a esta url
    //la regla para ingresar es que tenga el cliente algun rol de los que pasamos como parametro
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo) {

            modelo.put("titulo", "Bienvenido a la Libreria!");
            modelo.put("descripcion", "Haz iniciado sesion con exito.");
            return "inicio.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap modelo) { //no siempre puede venir este parametro error
        if (error != null) {
            modelo.put("error", "E-Mail o clave incorrectos.");
        }
        if (logout != null) {
            modelo.put("logout", "Ha salido correctamente.");
        }
        return "login.html";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro.html";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo,
            MultipartFile archivo,
            @RequestParam @Nullable String nombre,
            @RequestParam @Nullable String apellido,
            @RequestParam @Nullable String mail,
            @RequestParam @Nullable String clave1,
            @RequestParam @Nullable String clave2) {
        try {
            clienteServicio.registrar(archivo, nombre, apellido, mail, clave1, clave2);
            modelo.put("titulo", "Bienvenido a la Libreria!");
            modelo.put("descripcion", "El cliente fue registrado con exito!");
            return "inicio.html";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            return "registro.html";
        }
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
