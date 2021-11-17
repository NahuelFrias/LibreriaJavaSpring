package com.libreriaSpring.ej1Spring.servicios;

import com.libreriaSpring.ej1Spring.entidades.Cliente;
import com.libreriaSpring.ej1Spring.entidades.Foto;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.repositorios.ClienteRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClienteServicio implements UserDetailsService { //toma el nombre del usuario como parametro y devuelve un User

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private FotoServicio fotoServicio;

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {

        validar(nombre, apellido, mail, clave, clave2);

        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);

        cliente.setMail(mail);
        //encripto la clave
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        cliente.setClave(encriptada);
        cliente.setAlta(Boolean.TRUE);

        Foto foto = fotoServicio.guardar(archivo);
        cliente.setFoto(foto);

        //notificacionServicio.enviar("Bienvenido a la libreria", "Libreria", usuario.getMail())
        clienteRepositorio.save(cliente);
    }

    @Transactional
    public void modificar(MultipartFile archivo, String id, String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {

        validar(nombre, apellido, mail, clave, clave2);

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setMail(mail);
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            cliente.setClave(encriptada);

            String idFoto = null;
            if (cliente.getFoto().getId() != null) {
                idFoto = cliente.getFoto().getId();
            }

            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            cliente.setFoto(foto);

            clienteRepositorio.save(cliente);
        } else {
            throw new ErrorServicio("No se encontro el Cliente");
        }
    }

    public Cliente buscarPorId(String id) throws ErrorServicio {

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            return cliente;
        } else {
            throw new ErrorServicio("No se encontro el usuario buscado.");
        }
    }

    private void validar(String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido no puede ser nulo");
        }
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo");
        }
        if (mail.isEmpty() || mail.contains("  ")) {
            throw new ErrorServicio("El mail no puede ser nulo");
        }
        /*
        if (clienteRepositorio.buscarPorMail(mail) != null) {
            throw new ErrorServicio("El Email ya esta en uso");
        }*/
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves no coinciden.");
        }
    }

    /*cambiar siempre el String a "mail".
    El metodo recibe el nombre del cliente, lo busca en el repositorio
    y los transforma en un cliente de Spring Security.
    Este metodo es llamado siempre que el cliente haga Login*/
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Cliente cliente = clienteRepositorio.buscarPorMail(mail);
        if (cliente != null) {

            //esta lista contiene el listado de permisos del cliente
            List<GrantedAuthority> permisos = new ArrayList<>();

            //creo los permisos
            //con este rol va a poder ingresar a algunos metodos
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);
            //recupero el usuario que inicio sesion
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("clientesession", cliente);

            //transformo al cliente en un cliente del dominio de Spring
            //nos pide usuario,clave y permisos
            User user = new User(cliente.getMail(), cliente.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        return clienteRepositorio.findAll();
    }

    public void Eliminar(String id) throws ErrorServicio {

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            clienteRepositorio.delete(cliente);
        } else {
            throw new ErrorServicio("No se encontro el cliente.");
        }
    }
}
