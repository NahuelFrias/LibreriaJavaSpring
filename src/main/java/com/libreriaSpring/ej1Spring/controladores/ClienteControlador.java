package com.libreriaSpring.ej1Spring.controladores;

import com.libreriaSpring.ej1Spring.entidades.Cliente;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.servicios.ClienteServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    private ClienteServicio clienteServicio;

    @GetMapping("/editar-perfil/{id}")
    public String editarPerfil(@PathVariable String id, ModelMap modelo) throws ErrorServicio {

        try {
            Cliente cliente = clienteServicio.buscarPorId(id);
            modelo.put("perfil", cliente);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
        return "perfil.html";
    }

    @PostMapping("/actualizar-perfil")
    public String registrar(ModelMap modelo,
            HttpSession session,
            MultipartFile archivo,
            @RequestParam String id,
            @RequestParam @Nullable String nombre,
            @RequestParam @Nullable String apellido,
            @RequestParam @Nullable String mail,
            @RequestParam @Nullable String clave1,
            @RequestParam @Nullable String clave2) {
        Cliente cliente = null;
        try {
            cliente = clienteServicio.buscarPorId(id);
            clienteServicio.modificar(archivo, id, nombre, apellido, mail, clave1, clave2);
            modelo.put("exito", "Modificacion exitosa!");
            session.setAttribute("clientesession", cliente);
            return "inicio";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            return "perfil.html";
        }
    }
}
