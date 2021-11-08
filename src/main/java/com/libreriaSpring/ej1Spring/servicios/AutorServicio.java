package com.libreriaSpring.ej1Spring.servicios;

import com.libreriaSpring.ej1Spring.entidades.Autor;
import com.libreriaSpring.ej1Spring.errores.ErrorServicio;
import com.libreriaSpring.ej1Spring.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {

    @Autowired
    //no hace falta inicializar esta variable
    //ya que con autowired lo incializa el servidor de aplicaciones
    private AutorRepositorio autorRepositorio;

    /*
    Si el metodo se ejecuta correctamente se hace un commit a la base de datos
    Si salta una excepcion se vuelve atras y no se realizan los cambios
     */
    @Transactional
    public Autor registrarAutor(String nombre) throws ErrorServicio {

        //el id se generaba automaticamente
        //si se dispara algun error de servicio no se crea la entidad!
        validar(nombre);

        Autor a = buscarAutorPorNombre(nombre);

        if (a != null) {
            return a;
        } else {

            Autor autor = new Autor();
            autor.setNombre(nombre);
            //consultar!!
            autor.setAlta(Boolean.TRUE);

            //el repositorio almacena el objeto
            autorRepositorio.save(autor);

            return autor;
        }
    }

    @Transactional
    public Autor modificarAutor(String id, String nombre) throws ErrorServicio {

        //validamos si los datos son correctos
        validar(nombre);
        //buscamos el usuario por id y lo devolvemos
        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            //actualizamos
            autorRepositorio.save(autor);
            return autor;
        } else {
            throw new ErrorServicio("No se encontro el autor.");
        }
    }

    @Transactional
    public Autor alta(String id) throws ErrorServicio {

        Autor autor = autorRepositorio.getOne(id);
        autor.setAlta(true);
        return autorRepositorio.save(autor);
    }
    
    @Transactional
    public Autor baja(String id) throws ErrorServicio {

        Autor autor = autorRepositorio.getOne(id);
        autor.setAlta(false);
        return autorRepositorio.save(autor);
    }

    public Autor buscarPorId(String id) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            return autor;
        } else {
            throw new ErrorServicio("No se encontro el Autor.");
        }
    }

    public List<Autor> listarAutores() {
        return autorRepositorio.listarAutores();
    }

    public Autor buscarAutorPorNombre(String nombre) {
        return autorRepositorio.buscarAutorPorNombre(nombre);
    }

    public void validar(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }
    }
}
