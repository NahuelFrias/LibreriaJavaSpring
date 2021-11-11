package com.libreriaSpring.ej1Spring.servicios;

import com.libreriaSpring.ej1Spring.entidades.Cliente;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.repositorios.ClienteRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio implements UserDetailsService { //toma el nombre del usuario como parametro y devuelve un User

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public void registrar(String nombre, String apellido, Long documento, String mail, String clave) throws ErrorServicio {
        
        //agregar MultipartFile archivo para la foto
        validar(nombre, apellido, documento, mail, clave);

        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setDocumento(documento);
        cliente.setMail(mail);
        //encripto la clave
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        cliente.setClave(encriptada);
        cliente.setAlta(Boolean.TRUE);
        
        //Foto foto = fotoServicio.guardar(archivo)
        //cliente.setFoto(foto)
        
        //notificacionServicio.enviar("Bienvenido a la libreria", "Libreria", usuario.getMail())

        clienteRepositorio.save(cliente);
    }

    public void modificar(String id, String nombre, String apellido, Long documento, String mail, String clave) throws ErrorServicio {

        validar(nombre, apellido, documento, mail, clave);

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDocumento(documento);
            cliente.setMail(mail);
            cliente.setClave(clave);

            clienteRepositorio.save(cliente);
        } else {
            throw new ErrorServicio("No se encontro el Cliente");
        }
    }

    private void validar(String nombre, String apellido, Long documento, String mail, String clave) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido no puede ser nulo");
        }
        if (documento == null) {
            throw new ErrorServicio("El documento no puede ser nulo");
        }
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo");
        }
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
