package com.libreriaSpring.ej1Spring.controladores;

import com.libreriaSpring.ej1Spring.entidades.Cliente;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.servicios.ClienteServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar-perfil/{id}")
    public String editarPerfil(HttpSession session, @PathVariable String id, ModelMap modelo) throws ErrorServicio {

        //securiso la sesion para que no pueda pegar id de otra persona
        //y editar su perfil
        Cliente login = (Cliente) session.getAttribute("clientesession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Cliente cliente = clienteServicio.buscarPorId(id);
            modelo.put("perfil", cliente);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
        return "perfil.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
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
        try {
            Cliente login = (Cliente) session.getAttribute("clientesession");
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }
            Cliente cliente = clienteServicio.buscarPorId(id);
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
